package com.jct.bd.theproject.model.entities;

import android.location.Location;
import com.google.firebase.database.Exclude;
import java.sql.Date;
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
}
