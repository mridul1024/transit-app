package com.zuk0.gaijinsmash.riderz.view_model;

import android.app.Application;

import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_map.BartMapViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class BartMapViewModelTest {

    @Mock
    Application application;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private BartMapViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        viewModel = new BartMapViewModel(application);
    }

    @Test
    public void testBartMap_loadsProperly() {
        //todo: instrumentation test
    }
}
