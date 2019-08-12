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

package pl.org.seva.weather.main.init

import android.content.Context
import android.location.Geocoder
import android.os.Build
import org.kodein.di.Kodein
import org.kodein.di.KodeinProperty
import org.kodein.di.conf.global
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.multiton
import org.kodein.di.generic.singleton
import pl.org.seva.weather.BuildConfig
import java.util.*
import java.util.logging.Logger

inline fun <reified T : Any> instance(tag: Any? = null) = Kodein.global.instance<T>(tag)

inline fun <reified A, reified T : Any> instance(tag: Any? = null, arg: A) =
        Kodein.global.instance<A, T>(tag, arg = arg)

inline val <T> KodeinProperty<T>.value get() = provideDelegate(null, Build::ID).value

class KodeinModuleBuilder(private val ctx: Context) {

    fun build() = Kodein.Module("main") {
        bind<Bootstrap>() with singleton { Bootstrap() }
        bind<Logger>() with multiton { tag: String ->
            Logger.getLogger(tag)!!.apply {
                if (!BuildConfig.DEBUG) {
                    @Suppress("UsePropertyAccessSyntax")
                    setFilter { false }
                }
            }
        }
        bind<Geocoder>() with singleton { Geocoder(ctx, Locale.getDefault()) }
    }
}
