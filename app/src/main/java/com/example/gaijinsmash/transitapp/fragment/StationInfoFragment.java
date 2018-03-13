package com.example.gaijinsmash.transitapp.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.database.StationDatabase;
import com.example.gaijinsmash.transitapp.model.bart.Station;
import com.example.gaijinsmash.transitapp.network.xmlparser.StationXmlParser;
import com.example.gaijinsmash.transitapp.utils.ApiStringBuilder;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StationInfoFragment extends Fragment {

    private View mInflatedView;
    private String mStation;
    private TextView mTitle, mAddress, mCity, mLink, mIntro, mAttraction, mCrossStreet, mShopping, mFood;

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
        mTitle = (TextView) mInflatedView.findViewById(R.id.stationInfo_title_textView);
        mAddress = (TextView) mInflatedView.findViewById(R.id.stationInfo_address_textView);
        mCity = (TextView) mInflatedView.findViewById(R.id.stationInfo_city_textView);
        mCrossStreet = (TextView) mInflatedView.findViewById(R.id.stationInfo_crossStreet_textView);
        mLink = (TextView) mInflatedView.findViewById(R.id.stationInfo_link_textView);
        mIntro = (TextView) mInflatedView.findViewById(R.id.stationInfo_intro_textView);
        mAttraction = (TextView) mInflatedView.findViewById(R.id.stationInfo_attraction_textView);
        mShopping = (TextView) mInflatedView.findViewById(R.id.stationInfo_shopping_textView);
        mFood = (TextView) mInflatedView.findViewById(R.id.stationInfo_food_textView);
        Button mapButton = (Button) mInflatedView.findViewById(R.id.stationInfo_map_btn);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String stationAddress = ((TextView) view.findViewById(R.id.stationInfo_address_textView)).getText().toString();
                bundle.putString("Station", stationAddress);
                FragmentManager manager = getFragmentManager();
                FragmentTransaction tx = manager.beginTransaction();
                Fragment newFrag = new GoogleMapFragment();
                newFrag.setArguments(bundle);
                tx.replace(R.id.fragmentContent, newFrag).addToBackStack(null).commit();
            }
        });
        Button returnButton = (Button) mInflatedView.findViewById(R.id.stationInfo_return_btn);
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
            mStation = bundle.getString("StationAddress");
        }
        if(mStation != null) {
            new GetStationDetails(getActivity(), mStation).execute();
        }
    }

    //---------------------------------------------------------------------------------------------
    // AsyncTask
    //---------------------------------------------------------------------------------------------
    private class GetStationDetails extends AsyncTask<Void,Void,Boolean> {
        private Context mContext;
        private String mStationAddress, mStationName, mAbbr;
        private Station mStation;

        public GetStationDetails(Context context, String stationAddress) {
            this.mContext = context;
            this.mStationAddress = stationAddress;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            StationDatabase db = StationDatabase.getRoomDB(mContext);
            mAbbr = db.getStationDAO().getStationByAddress(mStationAddress).getAbbreviation();
            if(mAbbr != null) {
                String uri = ApiStringBuilder.getStationInfo(mAbbr);
                StationXmlParser parser = new StationXmlParser(mContext);
                List<Station> list = new ArrayList<Station>();
                try {
                    list = parser.getList(uri);
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }
                if(list != null) {
                   mStation = list.get(0);
                }
            } else {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                mTitle.setText(mStation.getName());
                mAddress.setText(mStation.getAddress());
                mCity.setText(mStation.getCity());
                mCrossStreet.setText(mStation.getCrossStreet());
                mLink.setText(mStation.getLink());
                mIntro.setText(mStation.getIntro());
                mAttraction.setText(Html.fromHtml(mStation.getAttraction(), Html.FROM_HTML_MODE_LEGACY));
                mShopping.setText(Html.fromHtml(mStation.getShopping(), Html.FROM_HTML_MODE_LEGACY));
                mFood.setText(Html.fromHtml(mStation.getFood(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                mTitle.setText(getResources().getString(R.string.stationInfo_oops));
                mAddress.setVisibility(View.GONE);
                mCity.setVisibility(View.GONE);
                mCrossStreet.setVisibility(View.GONE);
                mIntro.setText(getResources().getString(R.string.stationInfo_error));
                mLink.setVisibility(View.GONE);
                mAttraction.setVisibility(View.GONE);
                mShopping.setVisibility(View.GONE);
                mFood.setVisibility(View.GONE);
            }
        }
    }
}
