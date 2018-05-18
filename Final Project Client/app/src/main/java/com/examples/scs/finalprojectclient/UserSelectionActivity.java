package com.examples.scs.finalprojectclient;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

import static com.examples.scs.finalprojectclient.Constants.IP_ADDRESS;
import static com.examples.scs.finalprojectclient.Constants.PORT;

public class UserSelectionActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private StompClient client;
    private String username;
    private UserArrayAdapter userArrayAdapter;
    private ListView listView;
    private ArrayList<UserDto> userDtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);
        userDtos = new ArrayList<>();
        username = getIntent().getStringExtra("StringName");
        client = Stomp.over(Stomp.ConnectionProvider.JWS, "http://" + IP_ADDRESS + PORT + "/chat/websocket");
        client.connect();
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
                            userDtos.add(userDto);
                        }
                    }
                    addUsers();
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        });


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
                ()-> Log.d("APLICATION", "Someshit worked"),
                error -> Log.d("APPLICATION", "Some shit error ocurred", error)
        );

        listView = (ListView) findViewById(R.id.userList);
        userArrayAdapter = new UserArrayAdapter(getApplicationContext(), R.layout.user_entry, username);

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

    private void addUsers(){
        for (UserDto userDto : userDtos){
            userArrayAdapter.add(userDto);
        }
    }
}

