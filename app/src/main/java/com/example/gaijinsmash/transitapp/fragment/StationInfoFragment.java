package com.example.gaijinsmash.transitapp.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.database.StationDatabase;
import com.example.gaijinsmash.transitapp.model.bart.Station;
import com.example.gaijinsmash.transitapp.network.xmlparser.StationXmlParser;
import com.example.gaijinsmash.transitapp.utils.ApiStringBuilder;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class StationInfoFragment extends Fragment {

    private View mInflatedView;
    private String mStationAddress;
    private TextView mTitle, mAddress, mCity, mLink, mIntro, mAttraction, mCrossStreet, mShopping, mFood;
    private Station mStationObject;

    //---------------------------------------------------------------------------------------------
    // Lifecycle Events
    //---------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.station_info_view, container, false);
        return mInflatedView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mTitle = mInflatedView.findViewById(R.id.stationInfo_title_textView);
        mAddress = mInflatedView.findViewById(R.id.stationInfo_address_textView);
        mCity = mInflatedView.findViewById(R.id.stationInfo_city_textView);
        mCrossStreet = mInflatedView.findViewById(R.id.stationInfo_crossStreet_textView);
        mLink = mInflatedView.findViewById(R.id.stationInfo_link_textView);
        mIntro = mInflatedView.findViewById(R.id.stationInfo_intro_textView);
        mAttraction = mInflatedView.findViewById(R.id.stationInfo_attraction_textView);
        mShopping = mInflatedView.findViewById(R.id.stationInfo_shopping_textView);
        mFood = mInflatedView.findViewById(R.id.stationInfo_food_textView);
        Button mapButton = mInflatedView.findViewById(R.id.stationInfo_map_btn);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                //String stationAddress = ((TextView) view.findViewById(R.id.stationInfo_address_textView)).getText().toString();
                bundle.putString("StationTitle",mStationObject.getName());
                bundle.putString("StationLat", String.valueOf(mStationObject.getLatitude()));
                bundle.putString("StationLong", String.valueOf(mStationObject.getLongitude()));
                FragmentManager manager = getFragmentManager();
                FragmentTransaction tx = manager.beginTransaction();
                Fragment newFrag = new GoogleMapFragment();
                newFrag.setArguments(bundle);
                tx.replace(R.id.fragmentContent, newFrag).addToBackStack(null).commit();
            }
        });
        Button returnButton = mInflatedView.findViewById(R.id.stationInfo_return_btn);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null) {
            mStationAddress = bundle.getString("StationAddress");
        }
        if(mStationAddress != null) {
            new GetStationDetails(this, mStationAddress).execute();
        }
    }

    //---------------------------------------------------------------------------------------------
    // AsyncTask
    //---------------------------------------------------------------------------------------------
    private static class GetStationDetails extends AsyncTask<Void,Void,Boolean> {
        private WeakReference<StationInfoFragment> mWeakRef;
        private String mStationAddress, mAbbr;

        private GetStationDetails(StationInfoFragment context, String stationAddress) {
            mWeakRef = new WeakReference<>(context);
            this.mStationAddress = stationAddress;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            StationInfoFragment frag = mWeakRef.get();
            StationDatabase db = StationDatabase.getRoomDB(frag.getActivity());
            mAbbr = db.getStationDAO().getStationByAddress(mStationAddress).getAbbreviation();
            if(mAbbr != null) {
                String uri = ApiStringBuilder.getStationInfo(mAbbr);
                StationXmlParser parser = new StationXmlParser(frag.getActivity());
                List<Station> list = new ArrayList<>();
                try {
                    list = parser.getList(uri);
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }
                if(list != null) {
                    frag.mStationObject = list.get(0);
                }
            } else {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            StationInfoFragment frag = mWeakRef.get();
            if(result) {
                frag.mTitle.setText(frag.mStationObject.getName());
                frag.mAddress.setText(frag.mStationObject.getAddress());
                frag.mCity.setText(frag.mStationObject.getCity());
                frag.mCrossStreet.setText(frag.mStationObject.getCrossStreet());
                frag.mLink.setText(frag.mStationObject.getLink());
                frag.mIntro.setText(frag.mStationObject.getIntro());

                // Check api level 23 and lower
                if(Build.VERSION.SDK_INT >= 24) {
                    frag.mAttraction.setText(Html.fromHtml(frag.mStationObject.getAttraction(), Html.FROM_HTML_MODE_LEGACY));
                    frag.mShopping.setText(Html.fromHtml(frag.mStationObject.getShopping(), Html.FROM_HTML_MODE_LEGACY));
                    frag.mFood.setText(Html.fromHtml(frag.mStationObject.getFood(), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    frag.mAttraction.setText(Html.fromHtml(frag.mStationObject.getAttraction()));
                    frag.mShopping.setText(Html.fromHtml(frag.mStationObject.getShopping()));
                    frag.mFood.setText(Html.fromHtml(frag.mStationObject.getFood()));
                }
            } else {
                frag.mTitle.setText(frag.getResources().getString(R.string.stationInfo_oops));
                frag.mAddress.setVisibility(View.GONE);
                frag.mCity.setVisibility(View.GONE);
                frag.mCrossStreet.setVisibility(View.GONE);
                frag.mIntro.setText(frag.getResources().getString(R.string.stationInfo_error));
                frag.mLink.setVisibility(View.GONE);
                frag.mAttraction.setVisibility(View.GONE);
                frag.mShopping.setVisibility(View.GONE);
                frag.mFood.setVisibility(View.GONE);
            }
        }
    }
}
