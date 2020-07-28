package com.zuk0.gaijinsmash.riderz.utils

import android.annotation.SuppressLint
import androidx.fragment.app.FragmentManager
import com.crashlytics.android.Crashlytics
import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.BuildConfig
import java.io.PrintWriter
import java.io.StringWriter


object CrashLogUtil {

    private const val ENABLE = false

    /**
     * A convenient wrapper for crashlytics logger to check for Fabric initialization first
     * @param logType: Int (ERROR, VERBOSE, DEBUG, etc)
     * @param tag: String
     * @param msg : String
     */
    fun log(logType: Int?, tag: String?, msg: String) {
        if (logType != null && !tag.isNullOrBlank())
            Crashlytics.log(logType, tag, msg)
    }

    /**
     * A convenient wrapper for crashlytics logger to check for Fabric initialization first
     * @param msg : String
     */
    fun log(msg: String?) {
        if (!msg.isNullOrEmpty())
            Crashlytics.log(msg)
        if(BuildConfig.DEBUG)
                Logger.d(msg)
    }

    /**
     * A convenient wrapper for crashlytics logger to check for Fabric initialization first
     * @param t : Throwable
     */
    fun logException(t: Throwable) {
        if(BuildConfig.DEBUG) Logger.e(t.toString())
        Crashlytics.logException(t)
    }

    /**
     * Log an event to crashlytics
     * this type will show regardless of a crash once the user restarts the app
     */
    fun logEvent(msg: String) {
        if (ENABLE) {
            Crashlytics.logException(Exception(msg))
            Logger.d(msg)
        }
    }

    /**
     * Dump the fragment manager state to log
     */
    @SuppressLint("RestrictedApi")
    fun logBackStack(fragmentManager: FragmentManager) {
        if (ENABLE) {
            val count = fragmentManager.backStackEntryCount
            val msg1 = "BackStack Entry Count : $count"
            Logger.d(msg1)
            Crashlytics.logException(Exception(msg1))

            val writer = StringWriter()
            fragmentManager.dump("", null, PrintWriter(writer, true), null)
            Logger.d(writer)
            Crashlytics.logException(java.lang.Exception(writer.toString()))
        }
    }
}