package pl.org.seva.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import pl.org.seva.weather.form.LocationAddress

class WeatherViewModel : ViewModel() {

    private val mutableAddress by lazy { MutableLiveData<String?>() }
    val addressLiveData get() = mutableAddress as LiveData<String?>

    var location: LatLng? = null
    var address: String? = null

    fun setLocation(locationAddress: LocationAddress?) {
        if (locationAddress != null) {
            location = locationAddress.first
            address = locationAddress.second
        }
        mutableAddress.value = locationAddress?.second ?: ""
    }
}
