package com.zuk0.gaijinsmash.riderz.view_model;

import android.app.Application;

import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.BsaRepository;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.EtdRepository;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.TripRepository;
import com.zuk0.gaijinsmash.riderz.ui.fragment.home.HomeViewModel;
import com.zuk0.gaijinsmash.riderz.ui.fragment.home.HomeViewModelFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class HomeViewModelTest {

    @Mock private Application application;
    @Mock private TripRepository tripRepository;
    @Mock private HomeViewModelFactory viewModelFactory;
    @Mock private BsaRepository bsaRepository;
    @Mock private EtdRepository etdRepository;

    private HomeViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        viewModel = new HomeViewModel(application, tripRepository, bsaRepository, etdRepository);
    }

    @Test
    public void testViewModel_isCreated() {
        assert(viewModel != null);
    }


}
