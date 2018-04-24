package com.zuk0.gaijinsmash.riderz.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.database.StationDatabase;
import com.zuk0.gaijinsmash.riderz.database.StationDbHelper;
import com.zuk0.gaijinsmash.riderz.model.bart.Station;
import com.zuk0.gaijinsmash.riderz.network.CheckInternet;
import com.zuk0.gaijinsmash.riderz.network.FetchGPS;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.ref.WeakReference;
import java.util.List;

public class GoogleMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private static final boolean DEBUG = false;
    private MapView mMapView;
    private ProgressBar mProgressBar;
    private View mInflatedView;

    //---------------------------------------------------------------------------------------------
    // Lifecycle Events - MapView must be used for Fragments to prevent nested fragments.
    //---------------------------------------------------------------------------------------------

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (mMapView != null)
            mMapView.onCreate(bundle);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null)
            mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView != null)
            mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mMapView != null)
            mMapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null)
            mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView != null)
            mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapView != null)
            mMapView.onLowMemory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.google_map_view, container, false);
        return mInflatedView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button mButton = mInflatedView.findViewById(R.id.googleMap_btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction tx = manager.beginTransaction();
                Fragment newFrag = new BartMapFragment();
                tx.replace(R.id.fragmentContent, newFrag).addToBackStack(null).commit();
            }
        });
        mProgressBar = mInflatedView.findViewById(R.id.googleMap_progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);
        try {
            mMapView = mInflatedView.findViewById(R.id.googleMap_mapView);
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(this);
        } catch (Exception e) {
            Log.i("Map View Error:", e.toString());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    //---------------------------------------------------------------------------------------------
    // Google Maps
    //---------------------------------------------------------------------------------------------

    @Override // This is called when google map is initialized
    public void onMapReady(GoogleMap googleMap) {

        // GoogleMaps settings
        initMapSettings(googleMap);
        initUserLocation(getActivity(), googleMap);

        // Populate map with all the stations (markers)
        new GetMarkersTask(googleMap, this).execute();

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //todo show a dialog on marker click

                // start here

                // end here

                // then move to results page with trip query results

                return false;
            }
        });

        Bundle mBundle = getArguments();
        if(mBundle != null) {
            String stationTitle = mBundle.getString("StationTitle");
            LatLng latLng = new LatLng(Double.valueOf(mBundle.getString("StationLat")), Double.valueOf(mBundle.getString("StationLong")));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(14).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(stationTitle));
            marker.showInfoWindow();
        }

        mMapView.onResume();
    }
    //---------------------------------------------------------------------------------------------
    // Helpers
    //---------------------------------------------------------------------------------------------

    public void initMapSettings(GoogleMap map) {
        // Set boundary of map area
        LatLngBounds bayArea = new LatLngBounds(
                new LatLng(37.2982, -121.5363), //southwest
                new LatLng(38.0694, -121.8494)); //northeast
        map.setLatLngBoundsForCameraTarget(bayArea);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.setMinZoomPreference(9f);
    }

    private void initUserLocation(Context context, GoogleMap map) {
        FetchGPS gps;
        Location loc = null;
        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                gps = new FetchGPS(context);
                loc = gps.getLocation();

                map.setMyLocationEnabled(true);
                map.setOnMyLocationButtonClickListener(this);
                map.setOnMyLocationClickListener(this);
            } else {
                //TODO: abstract AlertDialog code
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Location Settings");
                alertDialog.setMessage("Location is not available. Do you want to turn it on?");
                alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        } catch(SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }

        boolean gpsCheck = CheckInternet.isGPSEnabled(context);
        if (gpsCheck) {
            // move camera to user location
            LatLng userLocation = null;
            if(loc != null) {
                userLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
            }

            CameraUpdate update = null;
            if(userLocation != null) {
                update = CameraUpdateFactory.newLatLngZoom(userLocation, 10f);
            }

            if(update != null) {
                map.moveCamera(update);
            }
            } else {
            // This is the default zoom
            LatLng marker = new LatLng(37.803768, -122.271450);
            map.moveCamera(CameraUpdateFactory.newLatLng(marker));
        }
    }

    private static List<Station> initMarkers(Context context) {
        StationDatabase db = StationDatabase.getRoomDB(context);
        List<Station> stationList = db.getStationDAO().getAllStations();
        if(DEBUG) {
            if(stationList.isEmpty())
                Log.e("initMarkerS()", "stationList is EMPTY");
        }
        return stationList;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    //---------------------------------------------------------------------------------------------
    // AsyncTask for call to database and create Markers
    //---------------------------------------------------------------------------------------------
    // 3 params are Params, Progress, Result
    private static class GetMarkersTask extends AsyncTask<Void, Integer, List<Station>> {
        WeakReference<GoogleMapFragment> mWeakRef;
        private GoogleMap mGoogleMap;

        private GetMarkersTask(GoogleMap map, GoogleMapFragment context) {
                mWeakRef = new WeakReference<>(context);
                this.mGoogleMap = map;
        }

        @Override
        protected List<Station> doInBackground(Void...voids) {
            GoogleMapFragment frag = mWeakRef.get();
            StationDbHelper helper = new StationDbHelper(frag.getActivity());
            try {
                helper.initStationDb();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                helper.getDb().close();
            }
            return initMarkers(frag.getActivity());
        }

        @Override
        protected void onPostExecute(List<Station> list) {
            GoogleMapFragment frag = mWeakRef.get();
            // fill map with markers
            for(Station station : list) {
                LatLng latLng = new LatLng(station.getLatitude(), station.getLongitude());

                //add marker to map
                //mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(station.getName()));

                //add marker to list
                mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(station.getName()));
            }
            frag.mProgressBar.setVisibility(View.GONE);
        }
    }
}