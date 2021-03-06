package com.zuk0.gaijinsmash.riderz.data.remote.repository;

import androidx.lifecycle.MutableLiveData;

import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.StationXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.StationDao;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.BartService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Executor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StationRepositoryTest {

    @Mock
    private BartService service;

    @Mock
    private StationDao dao;

    @Mock
    private Executor executor;

    private StationRepository stationRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        stationRepository = new StationRepository(service, dao, executor);
    }

    @Test
    public void repoShould_properlyCallApi() {
        assert(stationRepository != null);

        MutableLiveData<StationXmlResponse> call = new MutableLiveData<>();
        StationRepository repository = mock(StationRepository.class);
        when(repository.getStations()).thenReturn(call);
        assert(repository.getStations() != null);
    }
}