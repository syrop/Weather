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

package pl.org.seva.weather.archive

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fr_archive.*
import pl.org.seva.weather.R
import pl.org.seva.weather.WeatherViewModel
import pl.org.seva.weather.api.WeatherJson
import pl.org.seva.weather.main.extension.invoke
import pl.org.seva.weather.main.extension.nav
import pl.org.seva.weather.main.extension.verticalDivider

class ArchiveFragment : Fragment(R.layout.fr_archive) {

    private val archiveViewModel
            by lazy { ViewModelProvider(this)[ArchiveViewModel::class.java] }

    private val viewModel
            by navGraphViewModels<WeatherViewModel>(R.id.nav_graph)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fun inProgress() {
            progress.visibility = View.VISIBLE
            recycler.visibility = View.GONE
            no_data.visibility = View.GONE
        }

        fun noData() {
            progress.visibility = View.GONE
            recycler.visibility = View.GONE
            no_data.visibility = View.VISIBLE
        }

        fun showList(list: List<WeatherEntity>) {
            progress.visibility = View.GONE
            recycler.visibility = View.VISIBLE
            no_data.visibility = View.GONE

            recycler.setHasFixedSize(true)
            recycler.layoutManager = LinearLayoutManager(requireContext())
            recycler.verticalDivider()
            recycler.adapter = WeatherDateAdapter(list) { position ->
                val weather = list[position]
                viewModel.setWeather(WeatherJson(weather.name, WeatherJson.Main(
                        temp = weather.temp,
                        pressure = weather.pressure,
                        humidity = weather.humidity,
                        temp_min = weather.temp_min,
                        temp_max = weather.temp_max
                )))
                nav(R.id.action_archive_to_presentation)
            }
        }

        (archiveViewModel.liveState to this) { state ->
            when (state) {
                is ArchiveViewModel.State.InProgress -> inProgress()
                is ArchiveViewModel.State.Success -> when (state.list.size) {
                    0 -> noData()
                    else -> showList(state.list)
                }
            }
        }
    }
}
