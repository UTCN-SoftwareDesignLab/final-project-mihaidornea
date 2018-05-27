package com.examples.scs.finalprojectclient.activities;

import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.examples.scs.finalprojectclient.utilities.arrayAdapters.ChatArrayAdapter;
import com.examples.scs.finalprojectclient.messages.ChatMessage;
import com.examples.scs.finalprojectclient.messages.Content;
import com.examples.scs.finalprojectclient.messages.GeneralMessage;
import com.examples.scs.finalprojectclient.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompHeader;
import ua.naiksoftware.stomp.client.StompClient;

import static com.examples.scs.finalprojectclient.utilities.Constants.IP_ADDRESS;
import static com.examples.scs.finalprojectclient.utilities.Constants.PORT;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private StompClient client;
    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private String username;
    private String toUsername;
    private EditText chatText;
    private Button buttonSend;
    private boolean side = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = getIntent().getStringExtra("StringUsername");
        toUsername = getIntent().getStringExtra("StringToUsername");


        buttonSend = findViewById(R.id.send);
        listView = findViewById(R.id.msgview);
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);

        client = ((ChatingApplication) getApplication()).getClient();
        SharedPreferences prefs = getSharedPreferences("", MODE_PRIVATE);
        username = prefs.getString("username", null);
        String password = prefs.getString("password", null);
        List<StompHeader> stompHeaders = new ArrayList<>();
        stompHeaders.add(new StompHeader("USERNAME_HEADER", username));
        stompHeaders.add(new StompHeader("PASSWORD_HEADER", password));

        client.topic("/broker/" + username, stompHeaders).subscribe(message -> {
            try {
                JSONObject jsonObject = new JSONObject(message.getPayload());
                String status = jsonObject.get("status").toString();
                try {
                    JSONArray messageArray = jsonObject.getJSONArray("messages");
                    if (messageArray != null) {
                        for (int i = 0; i < messageArray.length(); i++) {
                            JSONObject im = (JSONObject) messageArray.get(i);
                            if (im != null) {
                                runOnUiThread(() -> {
                                    try {
                                        boolean right = true;
                                        if (im.getString("fromUsername").equals(username)) {
                                            right = true;
                                            chatArrayAdapter.add(new ChatMessage(right, im.getString("message")));
                                        } else if (im.getString("toUsername").equals(username)) {
                                            right = false;
                                            chatArrayAdapter.add(new ChatMessage(right, im.getString("message")));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        }
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        });
        getMessages();


        listView.setAdapter(chatArrayAdapter);
        chatText = (EditText) findViewById(R.id.msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return (keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER) && sendChatMessage();
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
    }

    private void getMessages(){
        String contentJson = "";
        try {
            GeneralMessage generalMessage = new GeneralMessage();
            Content content = new Content();
            content.setUsername(toUsername);
            generalMessage.setContent(content);
            ObjectMapper mapper = new ObjectMapper();
            contentJson = mapper.writeValueAsString(generalMessage);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        client.send("/app/getIM/" + username, contentJson).subscribe(
                ()-> Log.d(TAG, "Message sent!"),
                error -> Log.d(TAG, "An error ocurred at sending the message!", error)
        );
    }

    private boolean sendChatMessage() {
        String contentJson = "";
        runOnUiThread(() -> chatArrayAdapter.add(new ChatMessage(!side, chatText.getText().toString())));
        try {
            GeneralMessage generalMessage = new GeneralMessage();
            Content content = new Content();
            content.setMessage(chatText.getText().toString());
            content.setUsername(username);
            generalMessage.setContent(content);
            ObjectMapper mapper = new ObjectMapper();
            contentJson = mapper.writeValueAsString(generalMessage);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        client.send("/app/im/" + toUsername, contentJson).subscribe(
                ()-> Log.d(TAG, "Message sent!"),
                error -> Log.d(TAG, "An error ocurred!", error)
        );
        chatText.setText("");
        return true;
    }
}

