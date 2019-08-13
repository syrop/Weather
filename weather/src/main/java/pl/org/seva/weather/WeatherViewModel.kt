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
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.org.seva.weather.api.WeatherJson
import pl.org.seva.weather.api.WeatherService
import pl.org.seva.weather.archive.WeatherDao
import pl.org.seva.weather.main.extension.log
import pl.org.seva.weather.main.init.instance

class WeatherViewModel : ViewModel() {

    private val weatherService by instance<WeatherService>()
    private var currentState: State = State.Idle
    private val mutableLiveState = MutableLiveData(currentState)
    val liveState get() = mutableLiveState as LiveData<State>
    private val weatherDao by instance<WeatherDao>()

    var searchJob: Job? = null

    fun pendingSearch(location: LatLng) {
        currentState = State.Pending(
                WeatherService.Query.Location(LatLng(location.latitude, location.longitude)))
        mutableLiveState.value = currentState
    }

    fun pendingSearch(city: String) {
        currentState = State.Pending(WeatherService.Query.City(city))
        mutableLiveState.value = currentState
    }

    fun launchSearch() {
        currentState.let { state ->
            check(state is State.Pending) { "Only launch search from Pending state" }
            currentState = State.InProgress
            mutableLiveState.value = currentState
            searchJob = viewModelScope.launch {
                val response = when (val query = state.query) {
                    is WeatherService.Query.City -> weatherService.getCity(query.city)
                    is WeatherService.Query.Location ->
                        weatherService.getLocation(query.location.latitude, query.location.longitude)
                }
                log.info(response.raw().toString())
                currentState = if (response.isSuccessful) {
                    val weather = checkNotNull(response.body())
                    withContext(NonCancellable) {
                        weatherDao.add(weather)
                    }
                    State.Success(weather)
                }
                else State.Error
                mutableLiveState.value = currentState
            }
        }
    }

    fun setWeather(weather: WeatherJson) {
        check(currentState == State.Idle) { "Only set fixed weather from Idle state" }
        currentState = State.Success(weather)
        mutableLiveState.value = currentState
    }

    fun reset() {
        searchJob?.cancel()
        currentState = State.Idle
        mutableLiveState.value = currentState
    }

    sealed class State {
        object Idle : State()
        data class Pending(val query: WeatherService.Query) : State()
        object InProgress : State()
        object Error : State()
        data class Success(val weather: WeatherJson) : State()
    }
}
