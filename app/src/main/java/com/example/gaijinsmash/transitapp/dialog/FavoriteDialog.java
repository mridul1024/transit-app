package com.example.gaijinsmash.transitapp.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.fragment.BartResultsFragment;


public class FavoriteDialog extends DialogFragment {
    private Fragment mFragment;

    static FavoriteDialog newInstance(Fragment context, AsyncTask task) {
        return new FavoriteDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.stat_notify_error)
                .setTitle(getResources().getString(R.string.alert_dialog_favorite_title))
                .setMessage(getResources().getString(R.string.alert_dialog_favorite_message))
                .setPositiveButton(getResources().getString(R.string.alert_dialog_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Todo: remove selected from favorites

                    }
                })
                .setNegativeButton(getResources().getString(R.string.alert_dialog_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).create();
    }
}
