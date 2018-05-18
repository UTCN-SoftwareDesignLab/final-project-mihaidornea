package com.examples.scs.finalprojectclient;

import android.content.Intent;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

import static com.examples.scs.finalprojectclient.Constants.IP_ADDRESS;
import static com.examples.scs.finalprojectclient.Constants.PORT;

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
        buttonSend = (Button) findViewById(R.id.send);
        listView = (ListView) findViewById(R.id.msgview);
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);

        client = Stomp.over(Stomp.ConnectionProvider.JWS, "http://" + IP_ADDRESS + PORT + "/chat/websocket");
        client.connect();

        client.topic("/broker/" + username).subscribe(message -> {
            try {
                JSONObject jsonObject = new JSONObject(message.getPayload());
                String status = jsonObject.get("status").toString();
                chatArrayAdapter.add(new ChatMessage(side, status));
            } catch (JSONException e){
                e.printStackTrace();
            }
        });


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
    private boolean sendChatMessage() {
        String contentJson = "";
        chatArrayAdapter.add(new ChatMessage(!side, chatText.getText().toString()));
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
                ()-> Log.d("APLICATION", "Someshit worked"),
                error -> Log.d("APPLICATION", "Some shit error ocurred", error)
        );
        chatText.setText("");
        return true;
    }
}

