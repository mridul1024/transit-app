package com.zuk0.gaijinsmash.riderz.data.remote.repository;

import androidx.lifecycle.MutableLiveData;

import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.TripJsonResponse;
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.TripDao;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.BartService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Executor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TripRepositoryTest {

    @Mock
    private BartService service;

    @Mock
    private TripDao dao;

    @Mock
    private Executor executor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void repoShould_properlyCallApi() {
        MutableLiveData<TripJsonResponse> call = new MutableLiveData<>();
        TripRepository repository = mock(TripRepository.class);
        when(repository.getTrip("12th","16th","today" ,"now"))
                .thenReturn(call);
        assert(repository.getTrip("12th","16th" ,"today" ,"now") != null );

    }
}
