package com.jct.bd.theproject.controller.splashscreen;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jct.bd.theproject.R;
import com.jct.bd.theproject.controller.MainActivity;
import com.jct.bd.theproject.model.backend.FactoryBackend;
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
    private EditText Address;

    private void findViews() {
        addRideButton = (Button) findViewById(R.id.addRaidButton);
        addRideButton.setOnClickListener(this);
        Email = (EditText) findViewById(R.id.email2);
        id = (EditText) findViewById(R.id.id);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        name = (EditText) findViewById(R.id.name);
        Address = (EditText) findViewById(R.id.Address);
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
            FireBase_DB_manager backend= FactoryBackend.getInstance();
            Ride ride = new Ride();
            ride.setId(id.toString());
            ride.setName(name.toString());
            ride.setEmail(Email.toString());
            ride.setPhone(phoneNumber.toString());
            //ride.setEndLocation(Address.toString());
            ride.setDrive(TypeOfDrive.PROGRESS);
            backend.AskNewRide(ride);
        }
    }
}