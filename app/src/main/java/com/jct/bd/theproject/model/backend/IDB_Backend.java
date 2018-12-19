package com.jct.bd.theproject.model.backend;
import com.jct.bd.theproject.model.backend.Action;
import com.jct.bd.theproject.model.entities.Ride;
//the interface of backend
public interface IDB_Backend {
     Void AskNewRide(Ride ride, Action<String> action);
}
