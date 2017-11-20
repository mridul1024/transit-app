package com.example.gaijinsmash.transitapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gaijinsmash.transitapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class BartMapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    private GoogleMap mGoogleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.bart_map_view, container, false);

        try {
            mMapView = (MapView) inflatedView.findViewById(R.id.mapView);
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(this::onMapReady);
        } catch (Exception e) {
            Log.i("Map View Error:", e.toString());
        }
        return inflatedView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        // TODO: add all markers eventually
        LatLng marker = new LatLng(37.803768, -122.271450);
        mGoogleMap.addMarker(new MarkerOptions().position(marker).title("12th St. Oakland City Center"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
        mGoogleMap.setMinZoomPreference(12f);

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });

        // todo: default zoom in on current location if gps is enabled, else zoom to whole map of bay area.
        // if(gps.isDisabled) { show a picture of a map } else { show the map }
        mMapView.onResume();
    }

    private List<LatLng> initMarkers() {
        // create runnable

        // get all stations from sqlite

        // get lat/lng for each station

        // create a marker for each

        // store in List

        return null;
    }

    //---------------------------------------------------------------------------------------------
    // Lifecycle Events
    //---------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if(mMapView != null)
            mMapView.onCreate(bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mMapView != null)
            mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        if(mMapView != null)
            mMapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mMapView != null)
            mMapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if(mMapView != null)
            mMapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mMapView != null)
            mMapView.onSaveInstanceState(outState);
    }
}
