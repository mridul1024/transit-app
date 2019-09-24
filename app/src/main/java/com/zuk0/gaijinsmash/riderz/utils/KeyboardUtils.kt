package com.zuk0.gaijinsmash.riderz.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardUtils {

    /**
     * Request to open the Keyboard,
     * if it's open already, it will close instead.
     */
    fun openKeyboard(context: Context?) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    /**
     * Force close the keyboard
     */
    fun closeKeyboard(activity: Activity?) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        val view = activity?.findViewById<View>(android.R.id.content)?.rootView
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}