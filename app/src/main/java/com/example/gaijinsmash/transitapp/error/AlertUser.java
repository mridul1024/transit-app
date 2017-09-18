package com.example.gaijinsmash.transitapp.error;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

/**
 * Created by ryanj on 9/17/2017.
 */

public class AlertUser {
    private Context mContext = null;

    protected AlertUser(Context mContext) {
        this.mContext = mContext;
    }

    public Context getContext() { return mContext; }

    public void NoGPSAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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


}
