package com.example.gaijinsmash.transitapp.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gaijinsmash.transitapp.R;

/**
 * SharedPreferences
 */

public class SettingsFragment extends Fragment {

    private Context mContext;
    private SharedPreferences mSharedPreferences;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.settings_view, container, false);
        mContext = getActivity();

        // retrieve settings from User
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);


        return inflatedView;
    }

}
