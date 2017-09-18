package com.example.gaijinsmash.transitapp.error;

import android.content.Context;
import android.widget.Toast;

/**
 * This class handles common error messages to send to the UI
 */

public class ErrorToast {

    public void noInternetErrorToast(Context context) {
        Toast.makeText(context, "Cannot connect to Internet.", Toast.LENGTH_LONG).show();
    }

    public void unknownErrorToast(Context context) {
        Toast.makeText(context, "An unknown error occurred.", Toast.LENGTH_LONG).show();
    }

    public void noGPSErrorToast(Context context) {
        Toast.makeText(context, "GPS is currently unavailable", Toast.LENGTH_LONG).show();
    }
}
