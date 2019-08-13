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

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pl.org.seva.weather.api.WeatherJson
import java.time.LocalDateTime

@Dao
abstract class WeatherDao {

    suspend fun add(weather: WeatherJson) {
        add(WeatherEntity(
                time = LocalDateTime.now().toString(),
                name = weather.name,
                temp = weather.main.temp,
                pressure = weather.main.pressure,
                humidity = weather.main.humidity,
                temp_min = weather.main.temp_min,
                temp_max = weather.main.temp_min))
    }

    @Insert
    abstract suspend fun add(weather: WeatherEntity)

    @Query("select * from ${WeatherDb.WEATHER_TABLE}")
    abstract suspend fun getAllEntities(): List<WeatherEntity>
}
