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

package pl.org.seva.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import pl.org.seva.weather.api.WeatherJson
import pl.org.seva.weather.form.LocationAddress

class WeatherViewModel : ViewModel() {

    private val mutableAddress by lazy { MutableLiveData<String?>() }
    val addressLiveData get() = mutableAddress as LiveData<String?>

    var state = State.None

    var location: LatLng? = null
    var address: String? = null

    fun setLocation(locationAddress: LocationAddress?) {
        if (locationAddress != null) {
            location = locationAddress.first
            address = locationAddress.second
        }
        mutableAddress.value = locationAddress?.second ?: ""
    }

    sealed class State {
        object None : State()
        object InProgress : State()
        object Error : State()
        data class Success(val weather: WeatherJson) : State()
    }
}