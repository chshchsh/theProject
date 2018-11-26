package com.jct.bd.theproject.model.backend;

import android.content.ContentValues;

import com.jct.bd.theproject.model.entities.Ride;
import com.jct.bd.theproject.model.entities.TypeOfDrive;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class GetTaxiConst  {
    public static ContentValues RideToContentValues(Ride ride) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", ride.getId());
        contentValues.put("NAME", ride.getName());
        contentValues.put("EMAIL", ride.getEmail());
        contentValues.put("PHONE", ride.getPhone());
        contentValues.put("TYPE OF RIDE", ride.getDrive().toString());
        contentValues.put("DESTINATION",ride.getEndLocation().toString());
        contentValues.put("START OF DRIVE",ride.getStartDrive().toString());
        contentValues.put("START LOCATION",ride.getStartLocation().toString());
        return contentValues;
    }
    public static Ride ContentValuesToRide(ContentValues contentValues) throws ParseException {
        Ride ride = new Ride();
        ride.setId(contentValues.getAsString("ID"));
        ride.setName(contentValues.getAsString("NAME"));
        ride.setPhone(contentValues.getAsString("PHONE"));
        ride.setEmail(contentValues.getAsString("EMAIL"));
        ride.setDrive(TypeOfDrive.valueOf(contentValues.getAsString("TYPE OF RIDE")));
        //ride.setEndLocation(contentValues.getAsString("DESTINATION"));
        ride.setStartDrive(Date.valueOf(contentValues.getAsString("START OF DRIVE")));
        //ride.setStartLocation(contentValues.getAsString("START LOCATION"));
    return ride;
    }
}
