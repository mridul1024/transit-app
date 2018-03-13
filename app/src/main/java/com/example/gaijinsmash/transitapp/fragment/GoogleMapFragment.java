package com.example.gaijinsmash.transitapp.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.database.StationDatabase;
import com.example.gaijinsmash.transitapp.database.StationDbHelper;
import com.example.gaijinsmash.transitapp.model.bart.Station;
import com.example.gaijinsmash.transitapp.network.CheckInternet;
import com.example.gaijinsmash.transitapp.network.FetchGPS;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class GoogleMapFragment extends Fragment implements OnMapReadyCallback {

    private static final boolean DEBUG = true;
    private MapView mMapView;
    private ProgressBar mProgressBar;
    private View mInflatedView;
    private Bundle mBundle;
    private Button mButton;

    //---------------------------------------------------------------------------------------------
    // Lifecycle Events
    //---------------------------------------------------------------------------------------------

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
        if (mMapView != null)
            mMapView.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.google_map_view, container, false);
        return mInflatedView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mButton = (Button) mInflatedView.findViewById(R.id.googleMap_btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction tx = manager.beginTransaction();
                Fragment newFrag = new BartMapFragment();
                tx.replace(R.id.fragmentContent, newFrag).addToBackStack(null).commit();
            }
        });
        mProgressBar = (ProgressBar) mInflatedView.findViewById(R.id.googleMap_progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);
        try {
            mMapView = (MapView) mInflatedView.findViewById(R.id.googleMap_mapView);
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(this);
        } catch (Exception e) {
            Log.i("Map View Error:", e.toString());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String stationAddress = "";
        mBundle = getArguments();
        if (mBundle != null) {
            stationAddress = mBundle.getString("StationAddress");
        }
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
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapView != null)
            mMapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView != null)
            mMapView.onSaveInstanceState(outState);
    }

    //---------------------------------------------------------------------------------------------
    // Google Maps
    //---------------------------------------------------------------------------------------------

    @Override // This is called when google map is initialized
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap mGoogleMap = googleMap;

        // GoogleMaps settings
        initMapSettings(mGoogleMap);
        try {
            initUserLocation(getActivity(), mGoogleMap);
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

        // Populate map with all the stations (markers)
        new GetMarkersTask(mGoogleMap, getActivity()).execute();
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //todo show a dialog on marker click

                // start here

                // end here

                // then move to results page with trip query results

                return false;
            }
        });
        mMapView.onResume();
    }

    public void initMapSettings(GoogleMap map) {
        map.setMinZoomPreference(9f);
        LatLngBounds bayArea = new LatLngBounds(
                new LatLng(37.2982, -121.5363), //southwest
                new LatLng(38.0694, -121.8494)); //northeast
        map.setLatLngBoundsForCameraTarget(bayArea);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
    }

    private void initUserLocation(Context context, GoogleMap map) throws GooglePlayServicesNotAvailableException {
        FetchGPS gps = new FetchGPS(context);
        Location loc = gps.getLocation();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.setOnMyLocationButtonClickListener((GoogleMap.OnMyLocationButtonClickListener) this);
            map.setOnMyLocationClickListener((GoogleMap.OnMyLocationClickListener) this);
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

        boolean gpsCheck = CheckInternet.isGPSEnabled(getActivity());
        if (gpsCheck) {
            //todo: move camera to user location
            LatLng marker = new LatLng(37.803768, -122.231450);
            map.moveCamera(CameraUpdateFactory.newLatLng(marker));
        } else {
            // This is the default zoom
            LatLng marker = new LatLng(37.803768, -122.271450);
            map.moveCamera(CameraUpdateFactory.newLatLng(marker));
        }

        // if bundle arguments are present
        //todo : where to put this?

    }

    private List<Station> initMarkers(GoogleMap map, Context context) {
        StationDatabase db = StationDatabase.getRoomDB(context);
        List<Station> stationList = db.getStationDAO().getAllStations();
        if(DEBUG) {
            if(stationList.isEmpty())
                Log.e("initMarkerS()", "stationList is EMPTY");
        }
        return stationList;
    }

    //---------------------------------------------------------------------------------------------
    // Thread for call to database and create Markers
    //---------------------------------------------------------------------------------------------
    // 3 params are Params, Progress, Result
    private class GetMarkersTask extends AsyncTask<Void, Integer, List<Station>> {
        private Context mContext;
        private GoogleMap mGoogleMap;

        public GetMarkersTask(GoogleMap map, Context context) {
                this.mContext = context;
                this.mGoogleMap = map;
        }

        @Override
        protected List<Station> doInBackground(Void...voids) {
            try {
                StationDbHelper.initStationDb(mContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return initMarkers(mGoogleMap, mContext);
        }

        @Override
        protected void onPostExecute(List<Station> list) {
            // fill map with markers
            for(Station station : list) {
                LatLng latLng = new LatLng(station.getLatitude(), station.getLongitude());
                mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(station.getName()));
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
