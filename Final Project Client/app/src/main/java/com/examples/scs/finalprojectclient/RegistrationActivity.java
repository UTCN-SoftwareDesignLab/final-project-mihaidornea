package com.examples.scs.finalprojectclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

import static com.examples.scs.finalprojectclient.Constants.IP_ADDRESS;
import static com.examples.scs.finalprojectclient.Constants.PORT;

/**
 * Created by mihaidornea on 5/17/2018.
 */

public class RegistrationActivity extends AppCompatActivity {

    private StompClient client;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private Button registrationButton;
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        textView = findViewById(R.id.registrationMessage);
        usernameEditText = findViewById(R.id.registrationUsername);
        passwordEditText = findViewById(R.id.registrationPassword);
        firstNameEditText = findViewById(R.id.registrationFirstName);
        lastNameEditText = findViewById(R.id.registrationLastName);
        registrationButton = findViewById(R.id.registerAccountButton);

        client = Stomp.over(Stomp.ConnectionProvider.JWS, "http://" + IP_ADDRESS + PORT + "/chat/websocket");
        client.connect();

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                client.topic("/broker/" + username).subscribe(message -> {
                    try {
                        JSONObject jsonObject = new JSONObject(message.getPayload());
                        String status = jsonObject.get("status").toString();
                        textView.setText(status);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                });

                String contentJson = "";

                try {
                    GeneralMessage generalMessage = new GeneralMessage();
                    Content content = new Content();
                    content.setUsername(username);
                    content.setPassword(password);
                    content.setFirstName(firstName);
                    content.setLastName(lastName);
                    generalMessage.setContent(content);
                    ObjectMapper mapper = new ObjectMapper();
                    contentJson = mapper.writeValueAsString(generalMessage);
                }catch (JsonProcessingException e){
                    e.printStackTrace();
                }
                client.send("/app/register/" + username, contentJson).subscribe(
                        ()-> Log.d("APLICATION", "Someshit worked"),
                        error -> Log.d("APPLICATION", "Some shit error ocurred", error)
                );
            }
        });

    }
}
