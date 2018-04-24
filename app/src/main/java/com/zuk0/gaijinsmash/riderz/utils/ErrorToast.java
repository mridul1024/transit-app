package com.zuk0.gaijinsmash.riderz.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * This class handles common error messages to send to the UI
 */

public class ErrorToast {

    public static void noInternetErrorToast(Context context) {
        Toast.makeText(context, "Cannot connect to Internet.", Toast.LENGTH_LONG).show();
    }

    public static void unknownErrorToast(Context context) {
        Toast.makeText(context, "An unknown error occurred.", Toast.LENGTH_LONG).show();
    }

    public static void noGPSErrorToast(Context context) {
        Toast.makeText(context, "GPS is currently unavailable", Toast.LENGTH_LONG).show();
    }

    public static void networkConnectionErrorToast(Context context, int responseCode) {
        Toast.makeText(context, "There was a problem with the network (code: " + responseCode + "). Please try again", Toast.LENGTH_LONG).show();
    }

}
