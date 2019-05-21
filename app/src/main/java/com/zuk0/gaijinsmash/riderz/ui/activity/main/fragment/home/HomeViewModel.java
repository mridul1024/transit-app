package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import android.content.Context;
import android.location.Location;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
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
import com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response.WeatherResponse;
import com.zuk0.gaijinsmash.riderz.data.local.room.database.FavoriteDatabase;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.BsaRepository;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.EtdRepository;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.TripRepository;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.WeatherRepository;
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper;
import com.zuk0.gaijinsmash.riderz.utils.GpsUtils;
import com.zuk0.gaijinsmash.riderz.utils.SharedPreferencesUtils;
import com.zuk0.gaijinsmash.riderz.utils.StationUtils;
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HomeViewModel extends ViewModel {

    private Application mApplication;
    private TripRepository mTripRepository;
    private BsaRepository mBsaRepository;
    private EtdRepository mEtdRepository;
    private WeatherRepository mWeatherRepository;

    private FavoriteDatabase db;
    private static final String TAG = "HomeViewModel";

    @Inject
    public HomeViewModel(Application application,
                         TripRepository tripRepository,
                         BsaRepository bsaRepository,
                         EtdRepository etdRepository,
                         WeatherRepository weatherRepository) {
        mApplication = application;
        mTripRepository = tripRepository;
        mBsaRepository = bsaRepository;
        mEtdRepository = etdRepository;
        mWeatherRepository = weatherRepository;

        initDb();
    }

    private void initDb() {
        db = Room.databaseBuilder(Objects.requireNonNull(mApplication.getApplicationContext()), FavoriteDatabase.class,
                "favorites").build();
    }

    public LiveData<BsaXmlResponse> getBsaLiveData() {
        return mBsaRepository.getBsa();
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

    public Maybe<Favorite> getMaybeFavorite() { //todo db calls should be abstracted to a repository class
        return db.getFavoriteDAO().getPriorityFavorite();
    }

    LiveData<EtdXmlResponse> getEtdLiveData(String origin) {
        String originAbbr = StationUtils.getAbbrFromStationName(origin);
        return mEtdRepository.getEtd(originAbbr);
    }

    public void setTrainHeaders(List<Trip> trips, Favorite favorite) {
        ArrayList<String> trainHeaders = new ArrayList<>();
        for(Trip trip : trips) {
            String header = StationUtils.getAbbrFromStationName(trip.getLegList().get(0).getTrainHeadStation()).toUpperCase();
            if(!trainHeaders.contains(header)) {
                trainHeaders.add(header);
                Log.i("HEADER", header);
            }
        }
        favorite.setTrainHeaderStations(trainHeaders); //todo: use hashset
    }

    public Favorite createFavoriteInverse(List<Trip> trips, Favorite favorite) {
        Favorite favoriteInverse = new Favorite();
        favoriteInverse.setOrigin(favorite.getDestination());
        favoriteInverse.setDestination(favorite.getOrigin());
        setTrainHeaders(trips, favoriteInverse);
        return favoriteInverse;
    }

    LiveData<LiveDataWrapper<TripJsonResponse>> getTripLiveData(String origin, String destination) {
        return mTripRepository.getTrip(StationUtils.getAbbrFromStationName(origin),
                StationUtils.getAbbrFromStationName(destination),"TODAY" ,"NOW");
    }

    /*
        For comparisons - make sure all train headers are abbreviated and capitalized
     */
    List<Estimate> getEstimatesFromEtd(Favorite favorite, List<Etd> etds) {
        List<Estimate> results = new ArrayList<>();
        for(Etd etd : etds) {
            if(favorite.getTrainHeaderStations().contains(etd.getDestinationAbbr().toUpperCase())) {
                Estimate estimate = etd.getEstimateList().get(0);
                estimate.setOrigin(favorite.getOrigin());
                estimate.setDestination(favorite.getDestination());
                estimate.setTrainHeaderStation(etd.getDestination());
                results.add(estimate);
            }
        }
        return results;
    }

    /*
        TODO grab user input from the CREATE view and automatically refresh the homepage.
     */

    LiveData<Location> getUserLocationLiveData() {
        //todo - abstract to repository
        MutableLiveData<Location> userLocationLiveData = new MutableLiveData<>();
        //if you have a commute route, get geoloc of destination
        //else use user's current location
        Location userLocation;
        GpsUtils gps = new GpsUtils(mApplication);
        userLocation = gps.getLocation();
        userLocationLiveData.postValue(userLocation);
        return userLocationLiveData;
    }

    //F = 9/5 (K - 273) + 32
    double kelvinToFahrenheit(double temp) {
        return (9f / 5f) * (temp - 273) + 32;
    }

    //C = K - 273
    double kelvinToCelcius(double temp) {
        return temp - 273;
    }

    void checkHolidaySchedule() {
        //TODO: push news to home fragment if it's a holiday
    }

    LiveData<LiveDataWrapper<WeatherResponse>> getWeather(int zipcode) {
        return mWeatherRepository.getWeather(zipcode);
    }

    boolean isDaytime() {
        return TimeDateUtils.isDaytime();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "onCleared()");
        db.close();
    }
}
