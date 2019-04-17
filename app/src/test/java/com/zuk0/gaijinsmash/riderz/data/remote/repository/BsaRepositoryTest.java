package com.zuk0.gaijinsmash.riderz.data.remote.repository;

import androidx.lifecycle.MutableLiveData;

import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.Bsa;
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.BsaDao;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.BartService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BsaRepositoryTest {

    @Mock
    private BartService service;

    @Mock
    private BsaDao dao;

    @Mock
    private Executor executor;

    private BsaRepository repository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        repository = new BsaRepository(service, dao, executor);
    }

    @Test
    public void repoShould_properlyCallApi() {

        MutableLiveData<BsaXmlResponse> call = new MutableLiveData<>();
        assert(repository != null);

        BsaRepository repository = mock(BsaRepository.class);
        when(repository.getBsa()).thenReturn(call);
        assert(repository.getBsa() != null);
    }

    @Test
    public void repoShould_cacheResult() {
        //todo: add logic
    }

    @Test
    public void repoShouldLoadCache_ifAvailable() {
        //todo: add logic
    }

    private BsaXmlResponse getBsaXmlResponse() {
        BsaXmlResponse response = new BsaXmlResponse();
        response.setTime("10:23 PM PST");
        response.setId(1);
        List<Bsa> list = new ArrayList<>();
        Bsa bsa1 = new Bsa();
        bsa1.setDescription("nothing to report");
        list.add(bsa1);
        response.setBsaList(list);

        return response;
    }


}
