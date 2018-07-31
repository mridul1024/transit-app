package com.zuk0.gaijinsmash.riderz.ui.fragment.home;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.ui.ViewModelProviderFactory;
import com.zuk0.gaijinsmash.riderz.data.model.bsa_response.BsaXmlResponse;
import com.zuk0.gaijinsmash.riderz.xml_adapter.advisory.BsaRecyclerAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.DaggerFragment;
import dagger.android.support.HasSupportFragmentInjector;

// must use android.support.v4.app.Fragment for ViewModelProvider compatibility
public class HomeFragment extends Fragment  {

    @Inject
    HomeViewModel mViewModel;

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        AndroidSupportInjection.inject(this);
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

        // Display image of bay bridge
        mViewModel.initPic(getActivity(), mViewModel.getHour(), mImageView);

        // todo: show BSA ms
        final Observer<BsaXmlResponse> bsaObserver = bsaXmlResponse -> {
            //update the ui here
            BsaRecyclerAdapter bsaAdapter = new BsaRecyclerAdapter(bsaXmlResponse.getBsaList());
            mAdvisoryRecyclerView.setAdapter(bsaAdapter);

            //update the time
            String time = bsaXmlResponse.getTime();
            mBsaTimeTv.setText(mViewModel.initMessage(time));

            //todo: hide progressbar
            mEtdProgressBar.setVisibility(View.GONE);
        };

        // Observe the LiveData
        if(mViewModel==null) Log.i("viewModel", "is NULL");
        if(mViewModel.getBsaLiveData() == null) Log.wtf("LiveData", " IS NULL");
        mViewModel.getBsaLiveData().observe(this, bsaObserver);

        // todo: show favorites if available
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
