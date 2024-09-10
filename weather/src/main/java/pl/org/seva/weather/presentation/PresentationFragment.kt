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

package pl.org.seva.weather.presentation

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import pl.org.seva.weather.R
import pl.org.seva.weather.WeatherViewModel
import pl.org.seva.weather.api.WeatherJson
import pl.org.seva.weather.main.extension.back
import pl.org.seva.weather.main.extension.invoke


class PresentationFragment : Fragment(R.layout.fr_presentation) {

    private val viewModel
            by navGraphViewModels<WeatherViewModel>(R.id.nav_graph)

    private val error by lazy { requireActivity().findViewById<View>(R.id.error) }
    private val details by lazy { requireActivity().findViewById<View>(R.id.details) }
    private val progress by lazy { requireActivity().findViewById<View>(R.id.progress) }
    private val name by lazy { requireActivity().findViewById<TextView>(R.id.name) }
    private val temp by lazy { requireActivity().findViewById<TextView>(R.id.temp) }
    private val pressure by lazy { requireActivity().findViewById<TextView>(R.id.pressure) }
    private val humidity by lazy { requireActivity().findViewById<TextView>(R.id.humidity) }
    private val temp_min by lazy { requireActivity().findViewById<TextView>(R.id.temp_min) }
    private val temp_max by lazy { requireActivity().findViewById<TextView>(R.id.temp_max) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fun inProgress() {
            error.visibility = View.GONE
            details.visibility = View.GONE
            progress.visibility = View.VISIBLE
        }

        fun error() {
            error.visibility = View.VISIBLE
            details.visibility = View.GONE
            progress.visibility = View.GONE
        }

        fun details(weather: WeatherJson) {
            error.visibility = View.GONE
            details.visibility = View.VISIBLE
            progress.visibility = View.GONE
            name.text = weather.name
            temp.text = weather.main.temp.toString()
            pressure.text = weather.main.pressure.toString()
            humidity.text = weather.main.humidity.toString()
            temp_min.text = weather.main.temp_min.toString()
            temp_max.text = weather.main.temp_max.toString()
        }

        (viewModel.liveState to this) { state ->
            when (state) {
                is WeatherViewModel.State.InProgress -> inProgress()
                is WeatherViewModel.State.Success -> details(state.weather)
                is WeatherViewModel.State.Error -> error()
                else -> Unit
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.reset()
            back()
        }
    }
}
