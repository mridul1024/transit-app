package com.zuk0.gaijinsmash.riderz.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.zuk0.gaijinsmash.riderz.R;

public class AlertDialogUtils {

    public static void launchLocationAlertDialog(Context context, View parentView) {
        String message = context.getResources().getString(R.string.gps_permission_alert);
        String yesAction = context.getResources().getString(R.string.alert_dialog_yes);
        Snackbar.make(parentView, message, Snackbar.LENGTH_INDEFINITE)
                .setAnchorView(R.id.bottom_navigation)
                .setAction(yesAction, view -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                })
                .setActionTextColor(Color.RED)
                .show();
    }

}
