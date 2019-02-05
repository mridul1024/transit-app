package com.zuk0.gaijinsmash.riderz.ui.fragment.home;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.Bsa;
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Estimate;
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Etd;
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.TripJsonResponse;
import com.zuk0.gaijinsmash.riderz.data.local.room.database.FavoriteDatabase;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.BsaRepository;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.EtdRepository;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.TripRepository;
import com.zuk0.gaijinsmash.riderz.utils.SharedPreferencesUtils;
import com.zuk0.gaijinsmash.riderz.utils.StationUtils;
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HomeViewModel extends ViewModel {

    private Application mApplication;
    private TripRepository mTripRepository;
    private BsaRepository mBsaRepository;
    private EtdRepository mEtdRepository;
    private List<Bsa> mBsaList;
    private Parcelable mEtdState;

    @Inject
    public HomeViewModel(Application application,
                         TripRepository tripRepository,
                         BsaRepository bsaRepository,
                         EtdRepository etdRepository) {
        mApplication = application;
        mTripRepository = tripRepository;
        mBsaRepository = bsaRepository;
        mEtdRepository = etdRepository;
    }

    public void setEtdState(Parcelable state) {
        mEtdState = state;
    }

    public Parcelable getState() {
        return mEtdState;
    }

    public void setBsaList(List<Bsa> bsaList) {
        mBsaList = bsaList;
    }

    public List<Bsa> getBsaList() {
        return mBsaList;
    }

    public LiveData<BsaXmlResponse> getBsaLiveData() {
        return mBsaRepository.getBsa();
    }

    public int getHour() {
        return TimeDateUtils.getCurrentHour();
    }

    public boolean is24HrTimeOn(Context context) {
        return SharedPreferencesUtils.getTimePreference(context);
    }

    private String initTime(boolean is24HrTimeOn, String time) {
        String result;
        if(is24HrTimeOn) {
            result = TimeDateUtils.format24hrTime(time);
        } else {
            result = TimeDateUtils.convertTo12Hr(time);
        }
        return result;
    }

    String initMessage(Context context, boolean is24HrTimeOn, String time) {
        return context.getResources().getString(R.string.last_update) + " " + initTime(is24HrTimeOn, time);
    }

    void initPic(Context context, int hour, ImageView imageView) {
        if (hour < 6 || hour >= 21) {
            // show night picture
            Glide.with(context)
                    .load(R.drawable.sf_night)
                    .into(imageView);
        } else if (hour >= 17) {
            // show dusk picture
            Glide.with(context)
                    .load(R.drawable.sf_dusk)
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(R.drawable.sf_day)
                    .into(imageView);
        }
    }

    boolean doesPriorityExist() {
        FavoriteDatabase db = Room.databaseBuilder(mApplication, FavoriteDatabase.class,
                "favorites").allowMainThreadQueries().build();
        int count = db.getFavoriteDAO().getPriorityCount();
        db.close();
        return count > 0;
    }

    public Favorite getFavorite() {
        FavoriteDatabase db = Room.databaseBuilder(mApplication, FavoriteDatabase.class,
                "favorites").allowMainThreadQueries().build();
        Favorite favorite = db.getFavoriteDAO().getPriorityFavorite();
        db.close();
        return favorite;
    }

    LiveData<EtdXmlResponse> getEtdLiveData(String origin) {
        String originAbbr = StationUtils.getAbbrFromStationName(origin);
        return mEtdRepository.getEtd(originAbbr);
    }

    public Favorite createFavoriteInverse(List<Trip> trips, Favorite favorite) {
        ArrayList<String> trainHeaders = new ArrayList<>();
        for(Trip trip : trips) {
            String header = trip.getLegList().get(0).getTrainHeadStation();
            if(!trainHeaders.contains(header)) {
                trainHeaders.add(header);
                Log.i("HEADER", header);
            }
        }
        Favorite favoriteInverse = new Favorite();
        favoriteInverse.setOrigin(favorite.getDestination());
        favoriteInverse.setDestination(favorite.getOrigin());
        favoriteInverse.setTrainHeaderStations(trainHeaders);
        return favoriteInverse;
    }

    LiveData<TripJsonResponse> getTripLiveData(String origin, String destination) {
        return mTripRepository.getTrip(StationUtils.getAbbrFromStationName(origin),
                StationUtils.getAbbrFromStationName(destination),"TODAY" ,"NOW");
    }

    List<Estimate> getEstimatesFromEtd(Favorite favorite, List<Etd> etds) {
        List<Estimate> results = new ArrayList<>();
        for(Etd etd : etds) {
            if(favorite.getTrainHeaderStations().contains(etd.getDestinationAbbr())) {
                Estimate estimate = etd.getEstimateList().get(0);
                estimate.setOrigin(favorite.getOrigin());
                estimate.setDestination(favorite.getDestination());
                results.add(estimate);
            }
        }
        return results;
    }

    public static CountDownTimer beginTimer(TextView textView, int minutesLeft) {
        long untilFinished = (long) (minutesLeft * 60000);
        return new CountDownTimer(untilFinished, 1000) {
            String minutes = textView.getContext().getResources().getString(R.string.minutes);
            //String seconds = textView.getContext().getResources().getString(R.string.seconds);

            @Override
            public void onTick(long millisUntilFinished) {
                String remainingTime = String.valueOf(millisUntilFinished / 60000)+ " " + minutes;
                textView.setText(remainingTime);
            }

            @Override
            public void onFinish() {
                textView.setText(textView.getContext().getResources().getString(R.string.leaving));
                // todo: if leaving, wait 1 minute, then destroy itemListRow.
            }
        }.start();
    }
}
