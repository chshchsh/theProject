package com.jct.bd.theproject.model.datasource;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jct.bd.theproject.model.backend.IDB_Maneger;
import com.jct.bd.theproject.model.entities.Ride;

public class FireBase_DB_manager implements IDB_Maneger {
    @Override
    public void AskNewRide(final Ride ride) {
        String key = ride.getId();
        RidesRef.child(key).push().setValue(ride);
    }
    static DatabaseReference RidesRef;
    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        RidesRef = database.getReference("rides");
    }
}

