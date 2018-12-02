package com.jct.bd.theproject.model.entities;

import android.content.ContentValues;

import java.sql.Date;
import java.text.ParseException;

public class Ride {
    private TypeOfDrive drive;
    private String id;
    private String email;
    private String name;
    private Date startDrive;
    private Date endDrive;
    private String startLocation;
    private String endLocation;
    private String phone;


    public Ride(TypeOfDrive drive, String id, String email, String name, Date startDrive, Date endDrive, String startLocation, String endLocation, String phone) {
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
        this.endLocation = "";
        this.startLocation = "";
        this.phone = "";
    }

    //@Exclude
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception {
        if(email.contains("@"))
         this.email = email;
        else
            throw new Exception("the email mast contain @");
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) throws Exception {
        if(phone.contains("1")||phone.contains("2")||phone.contains("3")||phone.contains("4")||phone.contains("5")||phone.contains("6")||phone.contains("7")||phone.contains("8")||phone.contains("9")||phone.contains("0"))
        this.phone = phone;
        else
            throw new Exception("the phone must contains only numbers");
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

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
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
            throw new Exception("this id not exists");
    }
    static boolean IDCheck(String strID)
    {
        int[] id_12_digits = { 1, 2, 1, 2, 1, 2, 1, 2, 1 };
        int count = 0;
        if (strID == null)
            return false;
        strID = leftPad(strID,9,'0');
        for (int i = 0; i < 9; i++)
        {
            int num = Integer.parseInt(strID.substring(i, 1)) * id_12_digits[i];
            if (num > 9)
                num = (num / 10) + (num % 10);
            count += num;
        }
        return (count % 10 == 0);
    }
    public static String leftPad(String originalString, int length,
                                 char padCharacter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(padCharacter);
        }
        String padding = sb.toString();
        String paddedString = padding.substring(originalString.length())
                + originalString;
        return paddedString;
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
    public static Ride ContentValuesToRide(ContentValues contentValues) throws Exception {
        Ride ride = new Ride();
        ride.setId(contentValues.getAsString("ID"));
        ride.setName(contentValues.getAsString("NAME"));
        ride.setPhone(contentValues.getAsString("PHONE"));
        ride.setEmail(contentValues.getAsString("EMAIL"));
        ride.setDrive(TypeOfDrive.valueOf(contentValues.getAsString("TYPE OF RIDE")));
        ride.setEndLocation(contentValues.getAsString("DESTINATION"));
        ride.setStartDrive(Date.valueOf(contentValues.getAsString("START OF DRIVE")));
        ride.setStartLocation(contentValues.getAsString("START LOCATION"));
        return ride;
    }
}
