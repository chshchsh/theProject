package com.jct.bd.theproject.model.backend;

import com.jct.bd.theproject.model.datasource.FireBase_DB_manager;
import com.jct.bd.theproject.model.entities.Ride;

public interface IDB_Maneger {
     public Void AskNewRide(Ride ride, FireBase_DB_manager.Action<String> action);
}
