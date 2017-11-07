package com.example.gaijinsmash.transitapp.activity.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gaijinsmash.transitapp.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * MapFragment > API 12
 */

public class GmapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mGoogleMap;
    private final int[] MAP_TYPES = {GoogleMap.MAP_TYPE_SATELLITE,
                                     GoogleMap.MAP_TYPE_NORMAL,
                                     GoogleMap.MAP_TYPE_HYBRID,
                                     GoogleMap.MAP_TYPE_TERRAIN,
                                     GoogleMap.MAP_TYPE_NONE};
    private int currentMapTypeIndex = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.gmap_view, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_gmap_container);
        //getFragmentManager().findFragmentById(R.id.fragment_gmap_container);
        mapFragment.getMapAsync(this);

        return inflatedView;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        LatLng sydney = new LatLng(-34, 151);
        mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //initMap();
    }

    @SuppressLint("MissingPermission")
    public void initMap() {
        mGoogleMap.setMapType(currentMapTypeIndex);
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setTrafficEnabled(false);
        mGoogleMap.setIndoorEnabled(true);
        mGoogleMap.setBuildingsEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
    }
}
