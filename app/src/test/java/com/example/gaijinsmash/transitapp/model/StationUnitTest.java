package com.example.gaijinsmash.transitapp.model;

import com.example.gaijinsmash.transitapp.model.bart.Station;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Created by ryanj on 7/16/2017.
 */

public class StationUnitTest extends AbstractModelTest<Station> {

    public StationUnitTest() {
        super(Station.class);
    }

    @Test
    public void stationModel_IfValid_ReturnsTrue() {
        Station station = new Station();
        station.setName("19th Street");
        assertTrue("Name is not null", station.getName() != null);
    }
}