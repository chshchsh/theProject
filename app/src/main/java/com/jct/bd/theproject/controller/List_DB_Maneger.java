package com.jct.bd.theproject.controller;

import com.jct.bd.theproject.model.backend.IDB_Maneger;
import com.jct.bd.theproject.model.entities.Ride;

import java.util.ArrayList;
import java.util.List;

public class List_DB_Maneger implements IDB_Maneger {
    static List<Ride> rideList;
    static {
        rideList = new ArrayList<>();
       }
    @Override
    public void AskNewRide(Ride ride) {
        rideList.add(ride);
    }
}
