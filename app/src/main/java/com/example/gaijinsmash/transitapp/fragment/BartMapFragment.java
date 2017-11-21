package com.example.gaijinsmash.transitapp.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.database.StationDbFacade;
import com.example.gaijinsmash.transitapp.model.bart.Station;
import com.example.gaijinsmash.transitapp.network.FetchGPS;
import com.example.gaijinsmash.transitapp.utils.CheckInternet;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class BartMapFragment extends Fragment implements OnMapReadyCallback {

    private static final boolean DEBUG = true;
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

    // This is called when google map is initialized
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        // GoogleMaps settings
        initMapSettings(mGoogleMap);

        // Populate map with all the stations (markers)
        new GetMarkersTask(mGoogleMap, getActivity()).execute();

        // Handles click event on markers
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
        mMapView.onResume();
    }

    public void initMapSettings(GoogleMap map) {
        map.setMinZoomPreference(9f);
        //LatLngBounds bayArea = new LatLngBounds(new LatLng(37.3382,121.8863), new LatLng(37.7749, 122.4194));
        //map.setLatLngBoundsForCameraTarget(bayArea);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        //map.setLatLngBoundsForCameraTarget();
        //map.setPadding(0, 0, 0, 80); // leftPad, topPad, rightPad, bottomPad


        // todo: default zoom in on current location if gps is enabled, else zoom to whole map of bay area.
        boolean gpsCheck = true; // need to replace with real check logic
        if(gpsCheck) {
            LatLng marker = new LatLng(37.803768, -122.271450);
            map.moveCamera(CameraUpdateFactory.newLatLng(marker));
        } else {
            // This is the default zoom
            LatLng marker = new LatLng(37.803768, -122.271450);
            map.moveCamera(CameraUpdateFactory.newLatLng(marker));
        }

        // todo: add station names to all markers
        //map.addMarker(new MarkerOptions().position(marker).title("12th St. Oakland City Center Station"));

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
    //---------------------------------------------------------------------------------------------
    // Thread for call to database
    //---------------------------------------------------------------------------------------------

    private List<LatLng> initMarkers(GoogleMap map, Context context) {
        List<LatLng> latLngList = new ArrayList<LatLng>();
        StationDbFacade helper = new StationDbFacade(context);
        List<Station> stationList = helper.getAllStations();
        if(DEBUG) {
            if(stationList.isEmpty())
                Log.e("initMarkerS()", "stationList is EMPTY");
        }
        for (Station station : stationList) {
            LatLng marker = new LatLng(station.getLatitude(), station.getLongitude());
            latLngList.add(marker);
        }
        return latLngList;
    }

    // 3 params are Params, Progress, Result
    private class GetMarkersTask extends AsyncTask<Void, Void, List<LatLng>> {
        private Context mContext;
        private GoogleMap mGoogleMap;

        public GetMarkersTask(GoogleMap map, Context context) {
                this.mContext = context;
                this.mGoogleMap = map;
        }

        @Override
        protected List<LatLng> doInBackground(Void...voids) {
            return initMarkers(mGoogleMap, mContext);
        }

        @Override
        protected void onPostExecute(List<LatLng> list) {
            // fill map with markers
            for(LatLng latLng : list) {
                mGoogleMap.addMarker(new MarkerOptions().position(latLng));
            }
        }
    }
}
