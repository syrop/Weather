package pl.org.seva.weather.main.extension

import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.nav(@IdRes resId: Int): Boolean {
    findNavController().navigate(resId)
    return true
}

fun Fragment.back() = findNavController().popBackStack()

val Fragment.prefs: SharedPreferences get() = requireContext().prefs

fun Fragment.checkPermission(permission: String) =
        ContextCompat.checkSelfPermission(context!!, permission) == PackageManager.PERMISSION_GRANTED
