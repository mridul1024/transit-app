package com.zuk0.gaijinsmash.riderz.view_model;

import android.content.Context;

import com.zuk0.gaijinsmash.riderz.data.local.constants.StationList;
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip;
import com.zuk0.gaijinsmash.riderz.ui.fragment.bart_results.BartResultsViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.inject.Inject;

import static org.junit.Assert.assertTrue;

public class BartResultsViewModelTest {

    @Inject
    BartResultsViewModel viewModel;

    private Favorite favorite;
    private Trip trip1, trip2;

    @Mock
    private Context context;

    @Mock
    StationList stationList;

    @Before
    public void setup() {
        favorite = new Favorite();

        trip1 = new Trip();
        trip1.setOrigin("ASHB");
        trip1.setDestination("12TH");

        trip2 = new Trip();
        trip2.setOrigin("24TH");
        trip2.setDestination("COLM");

        stationList = new StationList();
    }

    @Test
    public void isTripFavoritedTest() {
        //FavoriteDatabase.getRoomDB(context).getFavoriteDAO().isTripFavorited(trip1, trip2);
    }

}
