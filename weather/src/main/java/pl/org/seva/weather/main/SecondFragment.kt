package pl.org.seva.weather.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fr_main.*
import pl.org.seva.weather.R
import pl.org.seva.weather.main.extension.nav
import pl.org.seva.weather.main.extension.invoke

class SecondFragment : Fragment(R.layout.fr_main) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        next { nav(R.id.action_secondFragment_to_thirdFragment) }
    }
}
