package com.zuk0.gaijinsmash.riderz.ui.fragment.station_info;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.StationXmlResponse;
import com.zuk0.gaijinsmash.riderz.ui.fragment.google_map.GoogleMapFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class StationInfoFragment extends Fragment {

    @Inject
    StationInfoViewModelFactory mStationInfoViewModelFactory;

    private StationInfoViewModel mViewModel;
    private String mStationAddress, mStationAbbr;
    private Station mStationObject;

    @BindView(R.id.stationInfo_title_textView) TextView mTitle;
    @BindView(R.id.stationInfo_address_textView) TextView mAddress;
    @BindView(R.id.stationInfo_city_textView) TextView mCity;
    @BindView(R.id.stationInfo_crossStreet_textView) TextView mCrossStreet;
    @BindView(R.id.stationInfo_link_textView) TextView mLink;
    @BindView(R.id.stationInfo_attraction_textView) TextView mAttraction;
    @BindView(R.id.stationInfo_shopping_textView) TextView mShopping;
    @BindView(R.id.stationInfo_intro_textView) TextView mIntro;
    @BindView(R.id.stationInfo_food_textView) TextView mFood;
    @BindView(R.id.stationInfo_map_btn) Button mMapButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mInflatedView = inflater.inflate(R.layout.view_station_info, container, false);
        ButterKnife.bind(this, mInflatedView);
        return mInflatedView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initDagger();
        this.initViewModel();
        getBundleArgs();

        mMapButton.setOnClickListener(v -> handleMapButtonClick());

        if(mStationAddress != null) {
            mViewModel.initStation(getActivity(), mStationAbbr);
            initStationDetails(mViewModel.getStationLiveData());
        }
    }

    private void initDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this, mStationInfoViewModelFactory).get(StationInfoViewModel.class);
    }

    private void handleMapButtonClick() {
        FragmentManager manager = getFragmentManager();
        if (manager != null && mStationObject != null) {
            FragmentTransaction tx = manager.beginTransaction();
            Fragment newFrag = new GoogleMapFragment();
            newFrag.setArguments(mViewModel.getBundle(mStationObject));
            tx.replace(R.id.fragmentContent, newFrag).addToBackStack(null).commit();
        }
    }

    private void getBundleArgs() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            mStationAddress = bundle.getString("StationAddress");
            mStationAbbr = bundle.getString("StationAbbr");
        }
    }

    private void initStationDetails(LiveData<StationXmlResponse> data) {
        data.observe(this, stationObject -> {
            //update the ui
            Station station1 = null;
            if (stationObject != null) {
                station1 = stationObject.getStationList().get(0);
            }
            if(station1 != null) {
                // build station object for map button
                mStationObject = station1;

                //update ui
                mTitle.setText(station1.getName());
                Log.i("name", station1.getName());
                mAddress.setText(station1.getAddress());
                mCity.setText(station1.getCity());
                mCrossStreet.setText(station1.getCrossStreet());
                mLink.setText(station1.getLink());
                mIntro.setText(station1.getIntro());

                if(Build.VERSION.SDK_INT >= 24) {
                    mAttraction.setText(Html.fromHtml(station1.getAttraction(), Html.FROM_HTML_MODE_LEGACY));
                    mShopping.setText(Html.fromHtml(station1.getShopping(), Html.FROM_HTML_MODE_LEGACY));
                    mFood.setText(Html.fromHtml(station1.getFood(), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    mAttraction.setText(Html.fromHtml(station1.getAttraction()));
                    mShopping.setText(Html.fromHtml(station1.getShopping()));
                    mFood.setText(Html.fromHtml(station1.getFood()));
                }
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
        });
    }
}
