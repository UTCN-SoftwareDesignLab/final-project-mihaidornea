package com.examples.scs.finalprojectclient.activities;

import android.app.Application;
import android.content.SharedPreferences;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

import static com.examples.scs.finalprojectclient.utilities.Constants.IP_ADDRESS;
import static com.examples.scs.finalprojectclient.utilities.Constants.PORT;

/**
 * Created by mihaidornea on 5/26/2018.
 */

public class ChatingApplication extends Application {

    private StompClient client;

    @Override
    public void onCreate() {
        super.onCreate();
        int count = 0;
        client = Stomp.over(Stomp.ConnectionProvider.JWS, "http://" + IP_ADDRESS + PORT + "/chat/websocket");
        client.connect();
        while(!client.isConnected()){
            try {
                Thread.sleep(1);
                count++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (count > 2000)
                break;
            if (client.isConnected())
                break;
        }
    }

    public StompClient getClient() {
        return client;
    }
}
