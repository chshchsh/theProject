package com.jct.bd.theproject.controller.splashscreen;


import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.jct.bd.theproject.R;
import com.jct.bd.theproject.model.datasource.FireBase_DB_manager;
import com.jct.bd.theproject.model.entities.Ride;
import com.jct.bd.theproject.model.entities.TypeOfDrive;

public class HomeActivity extends Activity implements View.OnClickListener  {
    private long backPressedTime;
    private Button addRideButton;
    private EditText Email;
    private EditText name;
    private EditText id;
    private EditText phoneNumber;
    private PlaceAutocompleteFragment placeAutocompleteFragment1;
    private PlaceAutocompleteFragment placeAutocompleteFragment2;
    Location locationA = new Location("A");//= new Location(from);
    Location locationB = new Location("B") ;//= new Location(to);



    private void findViews() {
        addRideButton = (Button) findViewById(R.id.addRaidButton);
        addRideButton.setOnClickListener(this);
        Email = (EditText) findViewById(R.id.email2);
        id = (EditText) findViewById(R.id.id);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        name = (EditText) findViewById(R.id.name);
        placeAutocompleteFragment1 = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById( R.id.place_autocomplete_fragment1 );
        placeAutocompleteFragment2 = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById( R.id.place_autocomplete_fragment2 );
        placeAutocompleteFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                locationA.setLatitude(place.getLatLng().latitude);
                locationA.setLongitude(place.getLatLng().longitude);
                // .getAddress().toString();//get place details here
            }
            @Override
            public void onError(Status status) {

            }
        });

        placeAutocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //  to = place.getAddress().toString();//get place details here
                locationB.setLatitude(place.getLatLng().latitude);
                locationB.setLongitude(place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {

            }
        });
    }
    @Override
    public void onBackPressed(){
        if(backPressedTime + 2000> System.currentTimeMillis()){
            ActivityCompat.finishAffinity(HomeActivity.this);
        }else {
            Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViews();
    }
    @Override
    public void onClick(View v) {
        if (v == addRideButton) {
            Animation animation = AnimationUtils.loadAnimation(this,R.anim.sample_anim);
            addRideButton.startAnimation(animation);
            AskNewRide();
        }
    }
    private Ride getRide() {
        Ride ride = new Ride();
        ride.setId(id.getText().toString());
        ride.setName(name.getText().toString());
        ride.setEmail(Email.getText().toString());
        ride.setPhone(phoneNumber.getText().toString());
        //ride.setEndLocation(DestinationAddress.getText());
        ride.setDrive(TypeOfDrive.AVAILABLE);
        return ride;
    }
    public void AskNewRide() {
        {
            try {
                Ride ride = getRide();
                addRideButton.setEnabled(false);
                FireBase_DB_manager.addRide(ride, new FireBase_DB_manager.Action<Long>() {
                    @Override
                    public void onSuccess(Long obj) {
                        Toast.makeText(getBaseContext(), "insert id " + obj, Toast.LENGTH_LONG).show();
                        resetView();
                    }
                    @Override
                    public void onFailure(Exception exception) {
                        Toast.makeText(getBaseContext(), "Error \n" + exception.getMessage(), Toast.LENGTH_LONG).show();
                        resetView();
                    }
                    @Override
                    public void onProgress(String status, double percent) {
                        if (percent != 100)
                            addRideButton.setEnabled(false);
                    }
                });
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Error ", Toast.LENGTH_LONG).show();
                resetView();
            }
        }
    }
    private void resetView() {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        addRideButton.setEnabled(true);
                    }
                },
                1500);
    }
}