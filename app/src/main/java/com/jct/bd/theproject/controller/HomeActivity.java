package com.jct.bd.theproject.controller;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.jct.bd.theproject.model.entities.MyLocation;
import com.jct.bd.theproject.model.datasource.Action;
import com.jct.bd.theproject.model.backend.FactoryBackend;
import com.jct.bd.theproject.model.entities.Ride;

import static com.jct.bd.theproject.model.entities.MyLocation.locationA;
import static com.jct.bd.theproject.model.entities.MyLocation.locationB;

public class HomeActivity extends Activity implements View.OnClickListener {
    private long backPressedTime;
    MyLocation myLocation;
    boolean notError;//check if was Error somewhere on the code
    private Button getLocationButton, addRideButton;
    private EditText Email, name, id, phoneNumber;
    public static PlaceAutocompleteFragment placeAutocompleteFragment1;
    private PlaceAutocompleteFragment placeAutocompleteFragment2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        myLocation = new MyLocation(this.getApplicationContext());
        findViews();
    }
    private void findViews() {
        getLocationButton = (Button) findViewById(R.id.getLocationButton);
        getLocationButton.setOnClickListener(this);
        addRideButton = (Button) findViewById(R.id.addRaidButton);
        addRideButton.setOnClickListener(this);
        addRideButton.setEnabled(false);//the button is enabled because that not all the fields are full
        Email = (EditText) findViewById(R.id.email2);
        id = (EditText) findViewById(R.id.id);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        name = (EditText) findViewById(R.id.name);
        Email.addTextChangedListener(AddTextWatcer);
        id.addTextChangedListener(AddTextWatcer);
        phoneNumber.addTextChangedListener(AddTextWatcer);
        name.addTextChangedListener(AddTextWatcer);
        placeAutocompleteFragment1 = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment1);
        placeAutocompleteFragment2 = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);
        placeAutocompleteFragment1.setHint(getString(R.string.source));
        placeAutocompleteFragment2.setHint(getString(R.string.destination));
        placeAutocompleteFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                locationA.setLatitude(place.getLatLng().latitude);
                locationA.setLongitude(place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {

            }
        });

        placeAutocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                locationB.setLatitude(place.getLatLng().latitude);
                locationB.setLongitude(place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {

            }
        });
    }

    //Watcher if the fields are full or not
    private TextWatcher AddTextWatcer = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String inputName = name.getText().toString().trim();
            String inputEmail = Email.getText().toString().trim();
            String inputId = id.getText().toString().trim();
            String inputPhone = phoneNumber.getText().toString().trim();
            //if they full the button was enable else is not
            addRideButton.setEnabled(!inputEmail.isEmpty() && !inputName.isEmpty() && !inputId.isEmpty() && !inputPhone.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    //this func is don't give you promotion to get out when you press back you need to press again to exit
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            ActivityCompat.finishAffinity(HomeActivity.this);//finish all the activities
        } else {
            Toast.makeText(getBaseContext(), R.string.pressBack, Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
    @Override
    public void onClick(View v) {
        if (v == getLocationButton)//if he want is current location
            myLocation.getLocation();
        if (v == addRideButton) {//if he want to ask ride from taxi
            myLocation.locationManager.removeUpdates(myLocation.locationListener);
            addRideButton.setEnabled(false);
            getLocationButton.setEnabled(true);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.sample_anim);
            addRideButton.startAnimation(animation);//this do animation on the button when you click on him
            addRide();
            //after he send the details all fields again empty
            name.setText("");
            Email.setText("");
            phoneNumber.setText("");
            id.setText("");
            placeAutocompleteFragment1.setText("");
            placeAutocompleteFragment2.setText("");
        }
    }
    private void addRide() {
        try {
            final Ride ride = getRide();//return new ride that you get from the view
            if (notError) {//if not happened error on all the running
                addRideButton.setEnabled(false);
                //the AsyncTask for the func on the backend
                new AsyncTask<Void,Void,Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        //go to the func on the backend and also is do the action on the backend
                       return  FactoryBackend.getInstance().AskNewRide(ride, new Action<String>()
                        {
                            @Override
                            public void onSuccess(String obj) {
                                //if is success to send that to the firebase
                                Toast.makeText(getBaseContext(), getString(R.string.insertId) + obj, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Exception exception) {
                                //if he don't success to send to the firebase
                                Toast.makeText(getBaseContext(), getString(R.string.Error) + exception.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            public void onProgress(String status, double percent) {
                                if (percent != 100)
                                    addRideButton.setEnabled(false);
                            }
                        });
                    }
                }.execute();
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), R.string.Error , Toast.LENGTH_LONG).show();
        }
    }
    //make new Ride from the view
    public Ride getRide() {
        notError = true;
        Ride ride = new Ride();
        try {
            ride.setId(id.getText().toString());
            ride.setName(name.getText().toString());
            ride.setEmail(Email.getText().toString());
            ride.setPhone(phoneNumber.getText().toString());
            ride.setStartLocation(locationA);
            ride.setEndLocation(locationB);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            notError = false;
        }
        return ride;
    }
}
