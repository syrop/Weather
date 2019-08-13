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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.org.seva.weather.api.WeatherJson
import pl.org.seva.weather.api.WeatherService
import pl.org.seva.weather.main.init.instance
import java.lang.IllegalStateException

class WeatherViewModel : ViewModel() {

    private val weatherService by instance<WeatherService>()
    private var currentState: State = State.Idle
    val liveState = MutableLiveData(currentState)

    var searchJob: Job? = null

    fun pendingSearch(location: LatLng) {
        currentState = State.Pending(
                WeatherService.Query.Location(LatLng(location.latitude, location.longitude)))
        liveState.value = currentState
    }

    fun pendingSearch(city: String) {
        currentState = State.Pending(WeatherService.Query.City(city))
        liveState.value = currentState
    }

    fun launchSearch() {
        currentState.let { state ->
            if (state is State.Pending) {
                currentState = State.InProgress
                liveState.value = currentState
                searchJob = viewModelScope.launch {
                    val response = when (val query = state.query) {
                        is WeatherService.Query.City -> weatherService.getCity(query.city)
                        is WeatherService.Query.Location ->
                            weatherService.getLocation(query.location.latitude, query.location.longitude)
                    }
                    currentState = if (response.isSuccessful)
                        State.Success(checkNotNull(response.body()))
                        else State.Error
                    liveState.value = currentState
                }
            } else throw IllegalStateException("Only launch in Pending state")
        }
    }

    fun cancelSearch() {
        searchJob?.cancel()
        currentState = State.Idle
        liveState.value = currentState
    }

    sealed class State {
        object Idle : State()
        data class Pending(val query: WeatherService.Query) : State()
        object InProgress : State()
        object Error : State()
        data class Success(val weather: WeatherJson) : State()
    }
}
