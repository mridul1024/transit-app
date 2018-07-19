package com.zuk0.gaijinsmash.riderz.view.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.model.bsa_response.BsaXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.view_model.HomeViewModel;
import com.zuk0.gaijinsmash.riderz.xml_adapter.advisory.BsaRecyclerAdapter;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

// must use android.support.v4.app.Fragment for ViewModelProvider compatibility
public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    @BindView(R.id.home_bsa_recyclerView) RecyclerView mAdvisoryRecyclerView;
    @BindView(R.id.home_etd_recyclerView) RecyclerView mEstimateRecyclerView;
    @BindView(R.id.home_etd_title) TextView mEtdTitle;
    @BindView(R.id.home_etd_error) TextView mEstimateErrorTv;
    @BindView(R.id.home_view_timeTv) TextView mBsaTimeTv;
    @BindView(R.id.home_etd_progressBar) ProgressBar mEtdProgressBar;
    @BindView(R.id.home_banner_imageView) ImageView mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mInflatedView = inflater.inflate(R.layout.view_home, container, false);
        ButterKnife.bind(this, mInflatedView);
        return mInflatedView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);  //todo: check behavior of lifecycle for this
        mEtdProgressBar.setVisibility(View.VISIBLE);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        // Display image of bay bridge
        mViewModel.initPic(mViewModel.getHour(), mImageView);

        // Show the BSA time
        String time = mViewModel.initTime(mViewModel.is24HrTimeOn(), mViewModel.getBsaRepo().getAdvisory());
        mBsaTimeTv.setText(mViewModel.initMessage(time));

        // todo: show BSA msg
        // Create the observer which updates the UI
        final Observer<BsaXmlResponse> bsaObserver = new Observer<BsaXmlResponse>() {
            @Override
            public void onChanged(@Nullable BsaXmlResponse bsaXmlResponse) {
                // update the UI
            }
        };
        BsaRecyclerAdapter adapter = new BsaRecyclerAdapter(Objects.requireNonNull(mViewModel.getBsaRepo().getAdvisory().getValue()).getBsaList());
        mAdvisoryRecyclerView.setAdapter(adapter);

        // todo: show favorites if available
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
