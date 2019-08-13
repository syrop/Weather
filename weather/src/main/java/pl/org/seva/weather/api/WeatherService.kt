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

package pl.org.seva.weather.api

import com.google.android.gms.maps.model.LatLng
import retrofit2.Response
import retrofit2.http.GET

interface WeatherService {

    @GET("weather")
    suspend fun getCity(
            @retrofit2.http.Query("q") city: String,
            @retrofit2.http.Query("units") units: String = UNITS,
            @retrofit2.http.Query("APPID") id: String = APPID): Response<WeatherJson>

    @GET("weather")
    suspend fun getLocation(
            @retrofit2.http.Query("lat") lat: Double,
            @retrofit2.http.Query("lon") lon: Double,
            @retrofit2.http.Query("units") units: String = UNITS,
            @retrofit2.http.Query("APPID") id: String = APPID): Response<WeatherJson>

    sealed class Query {
        data class City(val city: String) : Query()
        data class Location(val location: LatLng) : Query()
    }

    companion object {
        const val APPID = "83f8aa22f836aeee8b81c98f63bd1c06"
        const val UNITS = "metric"
    }
}
