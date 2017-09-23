package com.example.gaijinsmash.transitapp.model;

import com.example.gaijinsmash.transitapp.model.bart.Station;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class StationTest extends AbstractModelTest<Station> {

    public StationTest() {
        super(Station.class);
    }

    @Before
    @Override
    public void setupTest() throws Exception {

    }

    @Override
    public int compare(Station station, Station t1) {
        return 0;
    }
}