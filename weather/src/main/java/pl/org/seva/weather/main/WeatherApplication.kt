package pl.org.seva.weather.main

import android.app.Application
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.conf.global
import pl.org.seva.weather.main.init.bootstrap
import pl.org.seva.weather.main.init.KodeinModuleBuilder

@Suppress("unused")
class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Kodein.global.addImport(KodeinModuleBuilder(this).build())
        GlobalScope.launch { bootstrap.boot() }
    }
}
