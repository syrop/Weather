/*
 * Copyright (C) 2019 Wiktor Nizio
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * If you like this program, consider donating bitcoin: bc1qncxh5xs6erq6w4qz3a7xl7f50agrgn3w58dsfp
 */

package pl.org.seva.weather.form

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.annotation.IdRes
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

open class MapHolder {
    private var map: GoogleMap? = null
    private var requestLocationPermission: ((onGranted: () -> Unit) -> Unit)? = null
    var prefs: SharedPreferences? = null
    private val mutableMap = MutableLiveData<GoogleMap>()

    @SuppressLint("MissingPermission")
    open infix fun withMap(map: GoogleMap) {
        this@MapHolder.map = map
        val cameraUpdate = prefs?.let { prefs ->
            val latLng = LatLng(
                    prefs.getFloat(LAT_PROPERTY, DEFAULT_LAT.toFloat()).toDouble(),
                    prefs.getFloat(LON_PROPERTY, DEFAULT_LON.toFloat()).toDouble())
            val zoom = prefs.getFloat(ZOOM_PROPERTY, DEFAULT_ZOOM)
            CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        } ?: CameraUpdateFactory.newLatLngZoom(LatLng(DEFAULT_LAT, DEFAULT_LON), DEFAULT_ZOOM)
        map.moveCamera(cameraUpdate)
        map.setOnCameraIdleListener {
            prefs?.edit {
                val position = map.cameraPosition
                putFloat(ZOOM_PROPERTY, position.zoom)
                putFloat(LAT_PROPERTY, position.target.latitude.toFloat())
                putFloat(LON_PROPERTY, position.target.longitude.toFloat())
            }
        }
        mutableMap.value = map
        requestLocationPermission?.invoke {
            map.isMyLocationEnabled = true
        }
    }

    protected fun markPosition(latLng: LatLng?) {
        with (checkNotNull(map)) {
            clear()
            latLng ?: return
            addMarker(MarkerOptions().position(latLng))!!
                    .setIcon(BitmapDescriptorFactory.defaultMarker(0f))
            animateCamera(CameraUpdateFactory.newLatLng(latLng))
        }
    }

    companion object {
        private const val DEFAULT_LAT = 51.1
        private const val DEFAULT_LON = 17.033333
        private const val DEFAULT_ZOOM = 8.0f

        private const val ZOOM_PROPERTY = "zoom_property"
        private const val LAT_PROPERTY = "latitude_property"
        private const val LON_PROPERTY = "longitude_property"
    }
}

fun Fragment.withMapHolder(pair: Pair<MapHolder, Int>) {
    val (holder, id) = pair
    lifecycleScope.launch(Dispatchers.Main) {
        holder withMap googleMap(id)
    }
}

private suspend fun Fragment.googleMap(@IdRes id: Int) = suspendCancellableCoroutine<GoogleMap> { continuation ->
    val mapFragment = childFragmentManager.findFragmentById(id) as SupportMapFragment
    mapFragment.getMapAsync { map -> continuation.resume(map) }
}
