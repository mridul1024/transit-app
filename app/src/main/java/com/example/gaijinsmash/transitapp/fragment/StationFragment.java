package com.example.gaijinsmash.transitapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gaijinsmash.transitapp.R;

import java.util.List;

/**
 * Created by ryanj on 6/30/2017.
 */

public class StationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        List stations = null;

        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.station_view, container, false);
    }
}
