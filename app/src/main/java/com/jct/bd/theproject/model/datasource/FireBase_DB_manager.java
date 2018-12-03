package com.jct.bd.theproject.model.datasource;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jct.bd.theproject.model.backend.IDB_Maneger;
import com.jct.bd.theproject.model.entities.Ride;

import java.util.ArrayList;
import java.util.List;

public class FireBase_DB_manager implements IDB_Maneger {
    public interface Action<T> {
        void onSuccess(String obj);

        void onFailure(Exception exception);

        void onProgress(String status, double percent);
    }

    public interface NotifyDataChange<T> {
        void OnDataChanged(T obj);

        void onFailure(Exception exception);
    }

    static DatabaseReference RidesRef;
    static List<Ride> rideList;

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        RidesRef = database.getReference("rides");
        rideList = new ArrayList<>();
    }
    public void AskNewRide(final Ride ride, final Action<String> action) {
        String key = ride.getId();
        RidesRef.child(key).setValue(ride).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.onSuccess(ride.getId());
                action.onProgress("upload student data", 100);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.onFailure(e);
                action.onProgress("error upload student data", 100);
            }
        });
    }
}


