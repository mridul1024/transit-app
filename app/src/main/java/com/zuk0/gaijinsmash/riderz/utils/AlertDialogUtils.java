package com.zuk0.gaijinsmash.riderz.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;
import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.trip.TripFragment;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class AlertDialogUtils {

    public static void launchLocationAlertDialog(Activity activity, Context context, View parentView) {
        String message = context.getResources().getString(R.string.gps_permission_alert);
        String yesAction = context.getResources().getString(R.string.alert_dialog_yes);
        Snackbar.make(parentView, message, Snackbar.LENGTH_INDEFINITE)
                .setAnchorView(R.id.bottom_navigation)
                .setAction(yesAction, view -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + context.getPackageName()));
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                })
                .setActionTextColor(Color.RED)
                .show();
    }

    public static void launchNotificationDialog(Activity activity, String title, String message) {
        String yesAction = activity.getResources().getString(R.string.alert_dialog_ok);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(yesAction, (dialog, which) ->  {
            dialog.dismiss();
            SharedPreferencesUtils.disableDevUpdatePreference(activity);
        });
        alertDialog.show();
    }

    public static void launchGoogleMapDialog(View view, Fragment context, Spinner spinner, Station station, String destination) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity());
        builder.setView(view);
        builder.setTitle(context.getResources().getString(R.string.googleMap_dialog_title))
                .setCancelable(true)
                .setPositiveButton(context.getResources().getString(R.string.alert_dialog_go), (dialog, which) -> {
                    String origin = spinner.getSelectedItem().toString();
                    if(!origin.isEmpty()){
                        Bundle bundle = new Bundle();
                        bundle.putString(TripFragment.TripBundle.ORIGIN.getValue(), station.getName());
                        bundle.putString(TripFragment.TripBundle.DESTINATION.getValue(), destination);
                        bundle.putString(TripFragment.TripBundle.DATE.getValue(), "TODAY");
                        bundle.putString(TripFragment.TripBundle.TIME.getValue(), "NOW");
                        NavHostFragment.findNavController(context).navigate(R.id.action_googleMapFragment_to_resultsFragment, bundle, null, null);

                        dialog.dismiss();
                    }
                })
                .setNegativeButton(context.getResources().getString(R.string.alert_dialog_cancel), (dialog, which) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.mp_primary_dark));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setBackgroundColor(android.R.attr.selectableItemBackground);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.mp_primary_dark));
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(android.R.attr.selectableItemBackground);
    }
}
