package com.example.gaijinsmash.transitapp.model;

import com.example.gaijinsmash.transitapp.model.abstractClass.AbstractModelTest;
import com.example.gaijinsmash.transitapp.model.bart.Station;
import static org.junit.Assert.assertTrue;

import org.junit.Before;

public class StationTest extends AbstractModelTest<Station> {


    public StationTest() {
        super(Station.class);
    }

    @Before
    @Override
    public void setupTest() throws Exception {

    }


    @Override
    public int compare(Station o1, Station o2) {
        return 0;
    }
}