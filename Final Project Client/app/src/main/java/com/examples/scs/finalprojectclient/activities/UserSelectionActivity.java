package com.examples.scs.finalprojectclient.activities;

import android.database.DataSetObserver;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.examples.scs.finalprojectclient.RealMapActivity;
import com.examples.scs.finalprojectclient.messages.Content;
import com.examples.scs.finalprojectclient.messages.GeneralMessage;
import com.examples.scs.finalprojectclient.R;
import com.examples.scs.finalprojectclient.utilities.Constants;
import com.examples.scs.finalprojectclient.utilities.UserDto;
import com.examples.scs.finalprojectclient.utilities.arrayAdapters.UserArrayAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

public class UserSelectionActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private Thread thread;
    private StompClient client;
    private String username;
    private UserArrayAdapter userArrayAdapter;
    private ListView listView;
    private ArrayList<UserDto> userDtos;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);
        username = getIntent().getStringExtra("StringName");
        client = Stomp.over(Stomp.ConnectionProvider.JWS, "http://" + Constants.IP_ADDRESS + Constants.PORT + "/chat/websocket");
        client.connect();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (client.isConnected()) {
            init();
            client.topic("/broker/" + username).subscribe(message -> {
                try {
                    JSONObject jsonObject = new JSONObject(message.getPayload());
                    JSONArray userArray = jsonObject.getJSONArray("users");
                    if (userArray != null) {
                        for (int i = 0; i < userArray.length(); i++) {
                            JSONObject user = (JSONObject) userArray.get(i);
                            if (user != null) {
                                UserDto userDto = new UserDto();
                                userDto.setFirstName(user.getString("firstName"));
                                userDto.setLastName(user.getString("lastName"));
                                userDto.setUsername(user.getString("username"));
                                boolean exists = false;
                                for (UserDto userDto1: userDtos){
                                    if (userDto1.getUsername().equals(userDto.getUsername()) || userDto1.getUsername().equals(username))
                                        exists = true;
                                }
                                if(exists)
                                    exists = false;
                                else {
                                    userDtos.add(userDto);
                                    addUsers();
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

            thread = new Thread() {
                @Override
                public void run() {
                    try {
                        while(true) {
                            getUsers();
                            getDeviceLocation();
                            sleep(10000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        } else {
            createToast("Could not connect to the server");
        }
    }

    private void init(){
        userArrayAdapter = new UserArrayAdapter(this, R.layout.user_entry, username);
        userDtos = new ArrayList<>();
        listView = findViewById(R.id.userList);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
        listView.setAdapter(userArrayAdapter);

        userArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(userArrayAdapter.getCount() - 1);
            }
        });


    }

    private void getUsers(){
        String contentJson = "";
        try {
            GeneralMessage generalMessage = new GeneralMessage();
            Content content = new Content();
            content.setUsername(username);
            generalMessage.setContent(content);
            ObjectMapper mapper = new ObjectMapper();
            contentJson = mapper.writeValueAsString(generalMessage);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        client.send("/app/getUsers/" + username, contentJson).subscribe(
                ()-> Log.d(TAG, "Message sent!"),
                error -> Log.d(TAG, "An error ocurred at sending the message!", error)
        );


    }



    private void addUsers() {
        for (UserDto userDto : userDtos) {
            runOnUiThread(() -> userArrayAdapter.add(userDto));
        }
    }

    private void createToast(String message){
        runOnUiThread(() -> Toast.makeText(UserSelectionActivity.this, message, Toast.LENGTH_SHORT).show());
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the device location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Log.d(TAG, "onComplete: I found the location");
                    Location currentLocation = (Location) task.getResult();
                    update(currentLocation.getLatitude(), currentLocation.getLongitude());
                } else {
                    Log.d(TAG, "onComplete: Unable to locate");
                    createToast("Unable to locate the device!");
                }
            });
        } catch (SecurityException e){
            Log.d(TAG, "getDeviceLocation: SecurityException" + e.getMessage());
        }
    }

    private void update(double latitude, double longitude){

        String contentJson = "";
        try {
            GeneralMessage generalMessage = new GeneralMessage();
            Content content = new Content();
            content.setUsername(username);
            content.setLatitude(latitude);
            content.setLongitude(longitude);
            generalMessage.setContent(content);
            ObjectMapper mapper = new ObjectMapper();
            contentJson = mapper.writeValueAsString(generalMessage);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        client.send("/app/update/" + username, contentJson).subscribe(
                ()-> Log.d(TAG, "Message sent!"),
                error -> Log.d(TAG, "An error ocurred at sending the message!", error)
        );
    }

    @Override
    protected void onDestroy() {
        thread.interrupt();
        super.onDestroy();
        thread.interrupt();
        client.disconnect();
    }

    @Override
    protected void onStop() {
        //client.disconnect();
        thread.interrupt();
        super.onStop();
    }

    @Override
    protected void onPause() {
        thread.interrupt();
        super.onPause();

    }
}

