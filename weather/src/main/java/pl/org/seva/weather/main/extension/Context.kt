package pl.org.seva.weather.main.extension

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

val Context.prefs: SharedPreferences get() = PreferenceManager.getDefaultSharedPreferences(this)
