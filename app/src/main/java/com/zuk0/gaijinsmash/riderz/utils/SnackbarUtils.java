package com.zuk0.gaijinsmash.riderz.utils;

import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackbarUtils {

    public static void snackbarError(View parentView, String message) {
        Snackbar snackbar = Snackbar.make(parentView, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static void snackbarMessage(View parentView, String message) {
        Snackbar.make(parentView, message, Snackbar.LENGTH_LONG)
        .show();
    }

    public static void snackbarAlertWithAction(View parentView, String message) {
        Snackbar.make(parentView, message, Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(Color.RED)
                .show();
    }
}
