package com.example.gaijinsmash.transitapp.fragment;

import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gaijinsmash.transitapp.R;


public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.about_view, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        FragmentManager fm = getFragmentManager();
        fm.popBackStack();
    }
}
