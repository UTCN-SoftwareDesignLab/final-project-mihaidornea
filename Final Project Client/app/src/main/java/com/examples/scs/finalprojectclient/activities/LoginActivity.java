package com.examples.scs.finalprojectclient.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.examples.scs.finalprojectclient.R;
import com.examples.scs.finalprojectclient.messages.Content;
import com.examples.scs.finalprojectclient.messages.GeneralMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompHeader;
import ua.naiksoftware.stomp.client.StompClient;

import static com.examples.scs.finalprojectclient.utilities.Constants.IP_ADDRESS;
import static com.examples.scs.finalprojectclient.utilities.Constants.PORT;

/**
 * Created by mihaidornea on 5/16/2018.
 */

public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "LoginActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    private StompClient client;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private String username;
    private String password;
    private Button loginButton;
    private Button registerButton;
    private Boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String USERNAME_HEADER = "USERNAME_HEADER";
    private static final String PASSWORD_HEADER = "PASSWORD_HEADER";

    private List<StompHeader> stompHeaders;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLocationPermission();
        if (isServicesOk()){
            try {
                if (mLocationPermissionGranted) {
                    client = ((ChatingApplication) getApplication()).getClient();
                    checkSharedPreferences();
                    init();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void init() throws InterruptedException {
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        usernameEditText = findViewById(R.id.loginUsername);
        passwordEditText = findViewById(R.id.loginPassword);

        if (client.isConnected()) {
            loginButton.setOnClickListener(view -> {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                List<StompHeader> stompHeaders = new ArrayList<>();
                stompHeaders.add(new StompHeader(USERNAME_HEADER, username));
                stompHeaders.add(new StompHeader(PASSWORD_HEADER, password));
                client.topic("/login/" + username, stompHeaders).subscribe(message -> {
                    try {
                        JSONObject jsonObject = new JSONObject(message.getPayload());
                        String status = jsonObject.get("status").toString();
                        if (status.equals("Access Granted!")) {
                            Intent intent = new Intent(getApplicationContext(), UserSelectionActivity.class).putExtra("StringName", username);
                            createToast(status);
                            SharedPreferences.Editor editor = getSharedPreferences("", MODE_PRIVATE).edit();
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.apply();
                            startActivity(intent);
                            finish();
                        } else {
                            createToast(status);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

                String contentJson = "";

                try {
                    GeneralMessage generalMessage = new GeneralMessage();
                    Content content = new Content();
                    content.setUsername(username);
                    content.setPassword(password);
                    generalMessage.setContent(content);
                    ObjectMapper mapper = new ObjectMapper();
                    contentJson = mapper.writeValueAsString(generalMessage);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                client.send("/app/login/" + username, contentJson).subscribe(
                        () -> Log.d(TAG, "init: Message was sent successfuly!"),
                        error -> Log.d(TAG, "init: Error while sending the message!", error)
                );
            });

            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                    startActivity(intent);
                }
            });
        }else {
            createToast("Cannot connect to the server!");
        }
    }

    private void createToast(String message){
        runOnUiThread(() -> Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show());
    }

    private boolean isServicesOk(){
        Log.d(TAG, "isServicesOK: checking google services version");
        int availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(LoginActivity.this);
        if (availability == ConnectionResult.SUCCESS) {
            //All is good
            Log.d(TAG, " isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(availability)){
            //error occured but we can resolve it
            Log.d(TAG, "isServicesOk: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(LoginActivity.this, availability, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Google Play Services are not available", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void getLocationPermission(){
        Log.d(TAG, "onRequestPermissionResult: called");
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "onRequestPermissionResult: permissed");
                mLocationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    private void checkSharedPreferences(){
        SharedPreferences prefs = getSharedPreferences("", MODE_PRIVATE);
        username = prefs.getString("username", null);
        password = prefs.getString("password", null);
        if (username != null && password != null){
            stompHeaders = new ArrayList<>();
            stompHeaders.add(new StompHeader(USERNAME_HEADER, username));
            stompHeaders.add(new StompHeader(PASSWORD_HEADER, password));
            client.topic("/login/" + username, stompHeaders).subscribe(message -> {
                try {
                    JSONObject jsonObject = new JSONObject(message.getPayload());
                    String status = jsonObject.get("status").toString();
                    Intent intent = new Intent(getApplicationContext(), UserSelectionActivity.class).putExtra("StringName", username);
                    createToast(status);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            String contentJson = "";
            try {
                GeneralMessage generalMessage = new GeneralMessage();
                Content content = new Content();
                content.setUsername(username);
                content.setPassword(password);
                generalMessage.setContent(content);
                ObjectMapper mapper = new ObjectMapper();
                contentJson = mapper.writeValueAsString(generalMessage);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            client.send("/app/login/" + username, contentJson).subscribe(
                    () -> Log.d(TAG, "init: Message was sent successfuly!"),
                    error -> Log.d(TAG, "init: Error while sending the message!", error)
            );
        }
    }

}
