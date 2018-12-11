package com.jct.bd.theproject.controller.splashscreen;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import com.jct.bd.theproject.model.backend.FactoryBackend;
import com.jct.bd.theproject.model.datasource.FireBase_DB_manager;
import com.jct.bd.theproject.model.entities.Ride;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends Activity implements View.OnClickListener {
    private long backPressedTime;
    boolean notError;//check if was Error somewhere on the code
    private Button getLocationButton, stopUpdateButton, addRideButton;
    LocationManager locationManager;
    LocationListener locationListener;
    private EditText Email, name, id, phoneNumber;
    private PlaceAutocompleteFragment placeAutocompleteFragment1;
    private PlaceAutocompleteFragment placeAutocompleteFragment2;
    String to, from;
    Location locationA = new Location("A");//= new Location(from);
    Location locationB = new Location("B");//= new Location(to);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViews();
    }
    private void findViews() {
        getLocationButton = (Button) findViewById(R.id.getLocationButton);
        getLocationButton.setOnClickListener(this);
        stopUpdateButton = (Button) findViewById(R.id.stopUpdateButton);
        stopUpdateButton.setOnClickListener(this);
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
        placeAutocompleteFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                locationA.setLatitude(place.getLatLng().latitude);
                locationA.setLongitude(place.getLatLng().longitude);
                from = place.getAddress().toString();//get place details here
            }

            @Override
            public void onError(Status status) {

            }
        });

        placeAutocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                to = place.getAddress().toString();//get place details here
                locationB.setLatitude(place.getLatLng().latitude);
                locationB.setLongitude(place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {

            }
        });
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                placeAutocompleteFragment1.setText(getPlace(location));//this put the current location on the fragment
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
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

    private void getLocation() {
        int locationOff = 0;
        try {
            //gets the status mode of the location's settings in the device
            locationOff = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        //if the GPS in the the device is off
        if(locationOff==0){
            //Moves to the device's location settings and asks to enable the GPS
            Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            Toast.makeText(this, R.string.gpsloc, Toast.LENGTH_SHORT).show();
            startActivity(onGPS);
        }
        //     Check the SDK version and whether the permission is already granted or not.
       else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);

        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            stopUpdateButton.setEnabled(true);//this button stop of search a location
            getLocationButton.setEnabled(false);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    //this func get location and convert it to name of location
    public String getPlace(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addresses.size() > 0) {
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);
                from = stateName + "\n" + cityName + "\n" + countryName;
                return from;
            }

            return "no place: \n (" + location.getLongitude() + " , " + location.getLatitude() + ")";
        } catch (
                IOException e)

        {
            e.printStackTrace();
        }
        return "IOException ...";
    }

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
    //ask the owner of the app prention to use the location of the phone
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                stopUpdateButton.setEnabled(true);
                getLocationButton.setEnabled(false);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            } else {
                Toast.makeText(this, R.string.prem, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == getLocationButton)//if he want is current location
            getLocation();
        if (v == stopUpdateButton) {//if he want to stop search is location
            // Remove the listener you previously added
            locationManager.removeUpdates(locationListener);
            stopUpdateButton.setEnabled(false);
            getLocationButton.setEnabled(true);
        }
        if (v == addRideButton) {//if he want to ask ride from taxi
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
                       return  FactoryBackend.getInstance().AskNewRide(ride, new FireBase_DB_manager.Action<String>()
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
