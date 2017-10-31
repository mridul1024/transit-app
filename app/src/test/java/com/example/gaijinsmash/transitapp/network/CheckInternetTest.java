package com.example.gaijinsmash.transitapp.network;

import android.content.Context;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.Assert.assertTrue;

/**
 * Created by ryanj on 9/17/2017.
 */

public class CheckInternetTest {

    @Mock
    Context context;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void checkInternetConnection_should_return_true() throws Exception {
        boolean result = CheckInternet.isNetworkActive(context);
        assertTrue("Network is off", result == true);
    }
}
