package com.example.gaijinsmash.transitapp.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.example.gaijinsmash.transitapp.R;

public class LocationPermissionDialog extends DialogFragment {

    static LocationPermissionDialog newInstance(int num) {
        LocationPermissionDialog lpd = new LocationPermissionDialog();
        Bundle args = new Bundle();
        args.putInt("num", num);
        lpd.setArguments(args);
        return lpd;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.stat_notify_error)
                .setTitle(getResources().getString(R.string.alert_dialog_location_title))
                .setMessage(getResources().getString(R.string.alert_dialog_location_message))
                .setPositiveButton(getResources().getString(R.string.alert_dialog_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Pressed OK", Toast.LENGTH_SHORT);

                    }
                })
                .setNegativeButton(getResources().getString(R.string.alert_dialog_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT);
                    }
                }).create();
    }
}
