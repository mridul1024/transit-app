package com.zuk0.gaijinsmash.riderz.utils;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class GpsUtilsTest {

    private GpsUtils utils;
    @Mock
    private Context context;

    @Before
    private void setup() {
        utils = new GpsUtils(context);
    }

    @Test
    private void locationProviderTest() {
        //todo: save logic
    }

    @Test
    private void locationTest() {
        //todo: save logic
    }
}
