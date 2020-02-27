package com.zuk0.gaijinsmash.riderz.utils

import android.content.Context
import java.io.InputStream

object MockBartApiUtil {

    fun getMockTripResponse(context: Context?) : InputStream? {
        return context?.assets?.open("mock_trips.xml")
    }

    fun getMockEtdResponse(context: Context?) : InputStream? {
        return context?.assets?.open("mock_etd.xml")
    }

    fun getMockBsaResponse(context: Context?) : InputStream? {
        return context?.assets?.open("mock_bsa.xml")
    }
}