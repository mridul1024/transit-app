package com.example.gaijinsmash.transitapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gaijinsmash.transitapp.R;
import com.google.android.gms.maps.MapView;

public class DummyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.bart_map_view, container, false);

        return inflatedView;
    }
}
