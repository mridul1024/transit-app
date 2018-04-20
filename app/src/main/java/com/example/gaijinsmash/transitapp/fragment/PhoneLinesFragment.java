package com.example.gaijinsmash.transitapp.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gaijinsmash.transitapp.R;

public class PhoneLinesFragment extends Fragment {
    private View mInflatedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.phone_lines_view, container, false);
        return mInflatedView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onPause() {
        super.onPause();
        FragmentManager fm = getFragmentManager();
        fm.popBackStack();
    }
}
