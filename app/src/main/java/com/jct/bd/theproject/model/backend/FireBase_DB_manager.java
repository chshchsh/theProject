package com.jct.bd.theproject.model.backend;

import com.jct.bd.theproject.model.entities.Ride;

import java.util.ArrayList;
import java.util.List;

public class FireBase_DB_manager implements DB_manager {
    static List<Ride> rides;
    static {
        rides = new ArrayList<>();
    }
    @Override
    public void AskNewRide(Ride ride) {

        rides.add(ride);
    }
}
