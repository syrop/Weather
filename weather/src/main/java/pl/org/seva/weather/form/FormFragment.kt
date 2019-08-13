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

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import kotlinx.android.synthetic.main.fr_form.*
import pl.org.seva.weather.R
import pl.org.seva.weather.WeatherViewModel
import pl.org.seva.weather.main.extension.invoke
import pl.org.seva.weather.main.extension.nav

class FormFragment : Fragment(R.layout.fr_form) {

    private val viewModel
            by navGraphViewModels<WeatherViewModel>(R.id.nav_graph)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        location {
            nav(R.id.action_form_to_location_picker)
        }

        fun launchSearch() {
            viewModel.launchSearch()
            nav(R.id.action_form_to_presentation)
        }

        city_name.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.pendingSearch(city_name.text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        (viewModel.liveState to this) { state ->
            when (state) {
                is WeatherViewModel.State.Pending -> launchSearch()
            }
        }
    }
}
