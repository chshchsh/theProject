package com.jct.bd.theproject.model.entities;

import android.content.ContentValues;
import android.location.Location;
import com.google.firebase.database.Exclude;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Ride {
    private TypeOfDrive drive;
    private String id;
    private String email;
    private String name;
    private Date startDrive;
    private Date endDrive;
    private Location startLocation;
    private Location endLocation;
    private String phone;


    public Ride(TypeOfDrive drive, String id, String email, String name, Date startDrive, Date endDrive, Location startLocation, Location endLocation, String phone) {
        this.drive = drive;
        this.id = id;
        this.email = email;
        this.name = name;
        this.startDrive = startDrive;
        this.endDrive = endDrive;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.phone = phone;
    }

    public Ride() {
        this.drive = TypeOfDrive.AVAILABLE;
        this.id = "";
        this.email = "";
        this.name = "";
        Calendar calendar = null;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
       // this.startDrive = Date.valueOf(format.format(calendar.getTime()));
        this.phone = "";
    }

    @Exclude
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getStartDrive() {

        return startDrive;
    }

    public void setStartDrive(Date startDrive) {
        this.startDrive = startDrive;
    }

    public Date getEndDrive() {
        return endDrive;
    }

    public void setEndDrive(Date endDrive) {
        this.endDrive = endDrive;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeOfDrive getDrive() {
        return drive;
    }

    public void setDrive(TypeOfDrive drive) {
        this.drive = drive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
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
