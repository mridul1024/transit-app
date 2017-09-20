package com.example.gaijinsmash.transitapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

/**
 * Prompt Alert messages to the User for common scenarios - i.e. Enabling GPS or Network Settings.
 */

public class AlertUser {
    public static Context mContext = null;

    protected AlertUser(Context mContext) {
        if(mContext == null) {
            this.mContext = mContext;
        }
    }

    public Context getContext() { return mContext; }

    public void noGPSAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Your GPS is not on. Would you like to enable it now?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent); // is this right?
                    }
                });
        builder.setNegativeButton("Do Nothing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void noInternetAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("You do not have internet connection. Would you like to enable it now?")
                .setCancelable(false)
                .setPositiveButton("Check Network Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                        mContext.startActivity(intent);
                    }
                });
        builder.setNegativeButton("Do Nothing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
