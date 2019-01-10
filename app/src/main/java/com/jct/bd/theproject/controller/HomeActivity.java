package com.jct.bd.theproject.controller;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
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
import static com.jct.bd.theproject.model.entities.Ride.IDCheck;

public class HomeActivity extends Activity implements View.OnClickListener {
    private long backPressedTime;
    MyLocation myLocation;
    boolean notError;//check if was Error somewhere on the code
    private Button getLocationButton, addRideButton;
    private EditText Email, name, id, phoneNumber;
    private TextInputLayout InputEmail,InputName,InputId,InputPhone;
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
        //addRideButton.setEnabled(false);//the button is enabled because that not all the fields are full
        Email = (EditText) findViewById(R.id.email2);
        id = (EditText) findViewById(R.id.id);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        name = (EditText) findViewById(R.id.name);
        InputEmail = (TextInputLayout) findViewById(R.id.InputEmail);
        InputId = (TextInputLayout) findViewById(R.id.InputIdNumber);
        InputPhone = (TextInputLayout) findViewById(R.id.InputPhone);
        InputName = (TextInputLayout) findViewById(R.id.InputName);
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
            //addRideButton.setEnabled(false);
            getLocationButton.setEnabled(true);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.sample_anim);
            addRideButton.startAnimation(animation);//this do animation on the button when you click on him
            confirmInput(v);
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
                //addRideButton.setEnabled(false);
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
                                //if (percent != 100)
                                    //addRideButton.setEnabled(false);
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
            Toast.makeText(getBaseContext(), "error on the fields", Toast.LENGTH_SHORT).show();
            notError = false;
        }
        return ride;
    }

    private boolean validateEmail(){
        String emailInput = InputEmail.getEditText().getText().toString();
        if(emailInput.isEmpty()){
            InputEmail.setError(getString(R.string.fill_email));
            InputEmail.setErrorEnabled(true);
            Email.requestFocus();
            Toast.makeText(this,getString(R.string.fill_email),Toast.LENGTH_LONG).show();
            return false;
        }else if (Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            InputEmail.setErrorEnabled(true);
            InputEmail.setError(getString(R.string.contains));
            Email.requestFocus();
            Toast.makeText(this,getString(R.string.contains),Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            InputEmail.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateId(){
        String IdNumberInput = InputId.getEditText().getText().toString();
        if(IdNumberInput.isEmpty()){
            InputId.setError(getString(R.string.fill_id));
            InputId.setErrorEnabled(true);
            id.requestFocus();
            Toast.makeText(this,getString(R.string.fill_id),Toast.LENGTH_LONG).show();
            return false;
        }else if(!IDCheck(IdNumberInput)){
            InputId.setError(getString(R.string.Extract_id));
            InputId.setErrorEnabled(true);
            id.requestFocus();
            Toast.makeText(this,getString(R.string.Extract_id),Toast.LENGTH_LONG).show();
            return false;
        }else if(IdNumberInput.length()!=9){
            InputId.setError(getString(R.string.length_id));
            InputId.setErrorEnabled(true);
            id.requestFocus();
            Toast.makeText(this,getString(R.string.length_id),Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            InputId.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateFullName(){
        String UserNameInput = InputName.getEditText().getText().toString();
        if(UserNameInput.isEmpty()){
            InputName.setError(getString(R.string.fill_name));
            InputName.setErrorEnabled(true);
            name.requestFocus();
            Toast.makeText(this,getString(R.string.fill_name),Toast.LENGTH_LONG).show();
            return false;
        }else {
            InputName.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePhone(){
        String phoneInput = InputPhone.getEditText().getText().toString();
        if(phoneInput.isEmpty()){
            InputPhone.setError(getString(R.string.fill_phone));
            phoneNumber.requestFocus();
            InputPhone.setErrorEnabled(true);
            Toast.makeText(this,getString(R.string.fill_phone),Toast.LENGTH_LONG).show();
            return false;
        }else if(Patterns.PHONE.matcher(phoneInput).matches()){
            InputPhone.setError(getString(R.string.length_phone));
            phoneNumber.requestFocus();
            InputPhone.setErrorEnabled(true);
            Toast.makeText(this,getString(R.string.length_phone),Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            InputPhone.setErrorEnabled(false);
            return true;
        }
    }
    public void confirmInput(View v) {
        if (!validateFullName()|| !validateId() || !validatePhone() || !validateEmail())
        return;
        else
         addRide();
    }
}
