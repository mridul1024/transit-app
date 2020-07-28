package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.report

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

class ReportFragment: DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
                .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        //restore state
    }

    override fun onPause() {
        //save state
        super.onPause()
    }

    override fun onStop() {
        dismiss()
        super.onStop()
    }

    companion object {
        private const val TAG = "ReportFragment"
    }
}