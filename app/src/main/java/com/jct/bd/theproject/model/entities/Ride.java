package com.jct.bd.theproject.model.entities;

import android.location.Location;
import com.jct.bd.theproject.R;

import java.sql.Date;

public class Ride {
    private TypeOfDrive drive;
    private String id;
    private String email;
    private String name;
    private Date startDrive;//when the ride is starting
    private Date endDrive;//when the ride is ending
    private Location startLocation;//your source location
    private Location endLocation;//your destination
    private String phone;
    private String driverName;
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
        this.endLocation = null;
        this.startLocation = null;
        this.phone = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception {
        if(email.contains("@"))//if the email don't contain a @
         this.email = email;
        else
            throw new Exception(String.valueOf((R.string.email)));
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

    public void setPhone(String phone) throws Exception {
        if (phone.length() == 10 || phone.length() == 9)//check if the phone number is make sense
            this.phone = phone;
        else
            throw new Exception(String.valueOf(R.string.phone));
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

    public void setId(String id) throws Exception {
        if (IDCheck(id))
        this.id = id;
        else
            throw new Exception (String.valueOf(R.string.id));
    }
    //this func check if the id is true
    static boolean IDCheck(String strID)
    {
        int[] id_12_digits = { 1, 2, 1, 2, 1, 2, 1, 2, 1 };
        int count = 0;
        if (strID == null)
            return false;
        for (int i = 0; i < 9; i++)
        {
            int num = Integer.parseInt(strID.substring(i, i+1)) * id_12_digits[i];
            if (num > 9)
                num = (num / 10) + (num % 10);
            count += num;
        }
        return (count % 10 == 0);
    }
    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

}
