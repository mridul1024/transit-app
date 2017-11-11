package com.example.gaijinsmash.transitapp.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.gaijinsmash.transitapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by ryanj on 11/6/2017.
 */

public class ApiFragment extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gmap_view);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_gmap_container);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    /*
            mapBtn   = (Button) mInflatedView.findViewById(R.id.home_view_btn2);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment newFragment = new GmapFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContent, newFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        routeBtn = (Button) mInflatedView.findViewById(R.id.home_view_btn3);
        routeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment newFragment = new ScheduleFragment();
                ft.replace(R.id.fragmentContent, newFragment);
                ft.addToBackStack(null);
                ft.commitAllowingStateLoss();

            }
        });
     */
}
