package com.example.gaijinsmash.transitapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gaijinsmash.transitapp.R;
import com.google.android.gms.maps.MapView;

/*
    This is purely for creating new Fragments - copy and paste!
 */

public class DummyFragment extends Fragment {

    private View mInflatedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // only create View here
        // Example:
        // mInflatedView = inflater.inflate(R.layout.bart_map_view, container, false);

        return mInflatedView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // All other UI elements are called here
        // Example:
        //mButton = (Button) mInflatedView.findViewById(R.id.help_report_button);
    }
}
