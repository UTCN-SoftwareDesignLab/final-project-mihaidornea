package com.examples.scs.finalprojectclient;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by mihaidornea on 5/20/2018.
 */

public class MapsActivity  extends AppCompatActivity{

    private Button btnMap;
    private static final String TAG = "MapsActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if(isServicesOk()){
            init();
        }
    }

    private void init(){
        btnMap = findViewById(R.id.mapButton);
        btnMap.setOnClickListener(view -> {
            Intent intent = new Intent(MapsActivity.this, RealMapActivity.class);
            startActivity(intent);
        });
    }

    public boolean isServicesOk(){
        Log.d(TAG, "isServicesOK: checking google services version");
        int availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapsActivity.this);
        if (availability == ConnectionResult.SUCCESS) {
            //All is good
            Log.d(TAG, " isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(availability)){
            //error occured but we can resolve it
            Log.d(TAG, "isServicesOk: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MapsActivity.this, availability, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You cannot make maps requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}
