package com.jct.bd.theproject.model.backend;

import com.jct.bd.theproject.model.datasource.FireBase_DB_manager;
import com.jct.bd.theproject.model.entities.Ride;
//the interface of backend
public interface IDB_Backend {
     Void AskNewRide(Ride ride, FireBase_DB_manager.Action<String> action);
}
