package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.phone_lines

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import javax.inject.Inject

class PhoneLinesViewModel
@Inject constructor(application: Application) : AndroidViewModel(application) {


    fun formatTelephoneNumber(number: String?) : String {
        number?.let {
            val regex = Regex("[aA-zZ]+ [-][()]*")
            val formattedNumber = number.replace(regex, "")
            Log.d(TAG, "formatted number: $formattedNumber")
            return formattedNumber
        }
        return ""
    }
    companion object {
        private const val TAG = "PhoneLinesViewModel"
    }
}