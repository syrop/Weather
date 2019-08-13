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
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import kotlinx.android.synthetic.main.fr_presentation.*
import pl.org.seva.weather.R
import pl.org.seva.weather.WeatherViewModel
import pl.org.seva.weather.api.WeatherJson
import pl.org.seva.weather.main.extension.back
import pl.org.seva.weather.main.extension.invoke
import pl.org.seva.weather.main.extension.onBack

class PresentationFragment : Fragment(R.layout.fr_presentation) {

    private val viewModel
            by navGraphViewModels<WeatherViewModel>(R.id.nav_graph)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fun inProgress() {
            details.visibility = View.GONE
            progress.visibility = View.VISIBLE
        }

        fun details(weather: WeatherJson) {
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
            }
        }

        onBack {
            viewModel.cancelSearch()
            back()
        }
    }
}
