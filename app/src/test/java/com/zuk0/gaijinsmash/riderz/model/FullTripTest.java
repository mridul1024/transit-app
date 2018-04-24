package com.zuk0.gaijinsmash.riderz.model;

import com.zuk0.gaijinsmash.riderz.model.bart.Fare;
import com.zuk0.gaijinsmash.riderz.model.bart.FullTrip;
import com.zuk0.gaijinsmash.riderz.model.bart.Leg;
import com.zuk0.gaijinsmash.riderz.model.bart.Trip;

import org.junit.Before;

import java.util.List;

public class FullTripTest  {

    private FullTrip same1;
    private FullTrip same2;
    private FullTrip different;

    private Trip trip1;
    private Trip trip2;
    private Trip trip3;

    private List<Leg> legList;
    private List<Fare> fareList;

    @Before
    public void setUp() throws Exception {
        same1 = new FullTrip();
        same2 = new FullTrip();
        different = new FullTrip();

        trip1 = new Trip();
        trip2 = new Trip();
        trip3 = new Trip();
        trip1.setOrigin("Oakland");
        trip2.setOrigin("Oakland");
        trip3.setOrigin("Richmond");

        same1.setTrip(trip1);
        same1.setTrip(trip2);
        different.setTrip(trip3);
    }


}
