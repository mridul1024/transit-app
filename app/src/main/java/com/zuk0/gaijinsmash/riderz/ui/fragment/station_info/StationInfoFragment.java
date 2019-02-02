package com.zuk0.gaijinsmash.riderz.ui.fragment.station_info;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.StationXmlResponse;
import com.zuk0.gaijinsmash.riderz.databinding.ViewStationInfoBinding;
import com.zuk0.gaijinsmash.riderz.ui.fragment.google_map.GoogleMapFragment;

import javax.inject.Inject;

import androidx.navigation.fragment.NavHostFragment;
import dagger.android.support.AndroidSupportInjection;

public class StationInfoFragment extends Fragment {

    @Inject
    StationInfoViewModelFactory mStationInfoViewModelFactory;

    private ViewStationInfoBinding mDataBinding;
    private StationInfoViewModel mViewModel;
    private String mStationAddress, mStationAbbr;
    private Station mStationObject;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.view_station_info, container, false);
        return mDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initDagger();
        this.initViewModel();
        getBundleArgs();

        mDataBinding.stationInfoMapBtn.setOnClickListener(v -> handleMapButtonClick());

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
        Bundle bundle = mViewModel.getBundle(mStationObject);
        NavHostFragment.findNavController(this).navigate(R.id.action_stationInfoFragment_to_googleMapFragment, bundle, null,null);
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
            mDataBinding.stationInfoProgressBar.setVisibility(View.GONE);

            //update the ui
            Station station1 = null;
            if (stationObject != null) {
                station1 = stationObject.getStationList().get(0);
            }
            if(station1 != null) {
                // build station object for map button
                mStationObject = station1;

                //update ui
                mDataBinding.stationInfoTitleTextView.setText(station1.getName());
                Log.i("name", station1.getName());
                mDataBinding.stationInfoAddressTextView.setText(station1.getAddress());
                mDataBinding.stationInfoCityTextView.setText(station1.getCity());
                mDataBinding.stationInfoCrossStreetTextView.setText(station1.getCrossStreet());
                mDataBinding.stationInfoLinkTextView.setText(station1.getLink());
                mDataBinding.stationInfoIntroTextView.setText(station1.getIntro());

                if(Build.VERSION.SDK_INT >= 24) {
                    mDataBinding.stationInfoAttractionTextView.setText(Html.fromHtml(station1.getAttraction(), Html.FROM_HTML_MODE_LEGACY));
                    mDataBinding.stationInfoShoppingTextView.setText(Html.fromHtml(station1.getShopping(), Html.FROM_HTML_MODE_LEGACY));
                    mDataBinding.stationInfoFoodTextView.setText(Html.fromHtml(station1.getFood(), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    mDataBinding.stationInfoAttractionTextView.setText(Html.fromHtml(station1.getAttraction()));
                    mDataBinding.stationInfoShoppingTextView.setText(Html.fromHtml(station1.getShopping()));
                    mDataBinding.stationInfoFoodTextView.setText(Html.fromHtml(station1.getFood()));
                }
            } else {
                mDataBinding.stationInfoTitleTextView.setText(getResources().getString(R.string.stationInfo_oops));
                mDataBinding.stationInfoAddressTextView.setVisibility(View.GONE);
                mDataBinding.stationInfoCityTextView.setVisibility(View.GONE);
                mDataBinding.stationInfoCrossStreetTextView.setVisibility(View.GONE);
                mDataBinding.stationInfoIntroTextView.setText(getResources().getString(R.string.stationInfo_error));
                mDataBinding.stationInfoLinkTextView.setVisibility(View.GONE);
                mDataBinding.stationInfoAttractionTextView.setVisibility(View.GONE);
                mDataBinding.stationInfoShoppingTextView.setVisibility(View.GONE);
                mDataBinding.stationInfoFoodTextView.setVisibility(View.GONE);
            }
        });
    }
}
