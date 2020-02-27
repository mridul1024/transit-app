package com.zuk0.gaijinsmash.riderz.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.trip.TripFragment
import com.zuk0.gaijinsmash.riderz.utils.SharedPreferencesUtils

object AlertDialogUtils {
    fun launchLocationAlertDialog(activity: Activity?, context: Context, parentView: View?) {
        val message = context.resources.getString(R.string.gps_permission_alert)
        val yesAction = context.resources.getString(R.string.alert_dialog_yes)
        Snackbar.make(parentView!!, message, Snackbar.LENGTH_INDEFINITE)
                .setAnchorView(R.id.main_bottom_navigation)
                .setAction(yesAction) { view: View? ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + context.packageName))
                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                }
                .setActionTextColor(Color.RED)
                .show()
    }

    fun launchNotificationDialog(activity: Activity, title: String?, message: String?) {
        val yesAction = activity.resources.getString(R.string.alert_dialog_ok)
        val alertDialog = AlertDialog.Builder(activity)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton(yesAction) { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
            SharedPreferencesUtils.disableDevUpdatePreference(activity)
        }
        alertDialog.show()
    }

    fun launchGoogleMapDialog(view: View?, context: Fragment, spinner: Spinner, station: Station, destination: String?) {
        val builder = AlertDialog.Builder(context.activity)
        builder.setView(view)
        builder.setTitle(context.resources.getString(R.string.googleMap_dialog_title))
                .setCancelable(true)
                .setPositiveButton(context.resources.getString(R.string.alert_dialog_go)) { dialog: DialogInterface, which: Int ->
                    val origin = spinner.selectedItem.toString()
                    if (!origin.isEmpty()) {
                        val bundle = Bundle()
                        bundle.putString(TripFragment.TripBundle.ORIGIN.value, station.name)
                        bundle.putString(TripFragment.TripBundle.DESTINATION.value, destination)
                        bundle.putString(TripFragment.TripBundle.DATE.value, "TODAY")
                        bundle.putString(TripFragment.TripBundle.TIME.value, "NOW")
                        NavHostFragment.findNavController(context).navigate(R.id.action_googleMapFragment_to_resultsFragment, bundle, null, null)
                        dialog.dismiss()
                    }
                }
                .setNegativeButton(context.resources.getString(R.string.alert_dialog_cancel)) { dialog: DialogInterface, which: Int -> dialog.cancel() }
        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(context.resources.getColor(R.color.primaryDarkColor))
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setBackgroundColor(android.R.attr.selectableItemBackground)
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(context.resources.getColor(R.color.primaryDarkColor))
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(android.R.attr.selectableItemBackground)
    }
}