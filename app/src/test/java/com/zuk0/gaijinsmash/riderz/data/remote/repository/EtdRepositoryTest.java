package com.zuk0.gaijinsmash.riderz.data.remote.repository;

import androidx.lifecycle.MutableLiveData;

import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.EtdDao;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.RetrofitService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Executor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EtdRepositoryTest {

    @Mock
    private RetrofitService service;

    @Mock
    private EtdDao dao;

    @Mock
    private Executor executor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void repoShould_properlyCallApi() {
        MutableLiveData<EtdXmlResponse> call = new MutableLiveData<>();
        EtdRepository repository = mock(EtdRepository.class);
        when(repository.getEtd("12")).thenReturn(call);
        assert(repository.getEtd("12th") != null);
    }
}
