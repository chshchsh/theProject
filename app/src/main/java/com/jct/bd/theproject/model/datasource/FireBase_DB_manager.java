package com.jct.bd.theproject.model.datasource;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jct.bd.theproject.model.backend.IDB_Backend;
import com.jct.bd.theproject.model.entities.Ride;

import java.util.Calendar;
import java.util.Date;

public class FireBase_DB_manager implements IDB_Backend {
    static DatabaseReference RidesRef;

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        RidesRef = database.getReference("rides");//the root of the firebase
    }
    public Void AskNewRide(final Ride ride, final Action<String> action) {
        Date currentDate = Calendar.getInstance().getTime();
        ride.setWhenLoadToFirebase(currentDate);
        String key = ride.getId();//get the id because is the root of the all details on firabase
        //send the details of ride to firebase
                RidesRef.child(key).setValue(ride).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            //when is success to upload to firebase
            public void onSuccess(Void aVoid) {
                action.onSuccess(ride.getId());
                action.onProgress("upload ride data", 100);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            //when he don't success to upload to firebase
            public void onFailure(@NonNull Exception e) {
                action.onFailure(e);
                action.onProgress("error upload ride data", 100);
            }
        });
        return null;
    }
}


