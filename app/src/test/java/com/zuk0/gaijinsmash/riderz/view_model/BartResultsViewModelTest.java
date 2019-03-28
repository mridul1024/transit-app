package com.zuk0.gaijinsmash.riderz.view_model;

import android.content.Context;

import com.zuk0.gaijinsmash.riderz.data.local.constants.StationList;
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip;
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_results.BartResultsViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;

public class BartResultsViewModelTest {

    @Inject
    BartResultsViewModel viewModel;

    @Mock
    private Context context;

    @Mock
    StationList stationList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        Favorite favorite = new Favorite();

        Trip trip1 = new Trip();
        trip1.setOrigin("ASHB");
        trip1.setDestination("12TH");

        Trip trip2 = new Trip();
        trip2.setOrigin("24TH");
        trip2.setDestination("COLM");

        stationList = new StationList();
    }

    @Test
    public void isTripFavoritedTest() {
        //FavoriteDatabase.getRoomDB(context).getFavoriteDAO().isTripFavorited(trip1, trip2);
    }

}
