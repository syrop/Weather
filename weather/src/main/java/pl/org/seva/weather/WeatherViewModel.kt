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
import pl.org.seva.weather.api.WeatherJson
import pl.org.seva.weather.api.WeatherService

class WeatherViewModel : ViewModel() {

    val state = MutableLiveData<State>(State.None)

    fun launchSearch() {
        state.value = State.InProgress((state.value as State.Launch).query)
    }

    sealed class State {
        object None : State()
        data class Launch(val query: WeatherService.Query) : State()
        data class InProgress(val query: WeatherService.Query) : State()
        object Error : State()
        data class Success(val weather: WeatherJson) : State()
    }
}
