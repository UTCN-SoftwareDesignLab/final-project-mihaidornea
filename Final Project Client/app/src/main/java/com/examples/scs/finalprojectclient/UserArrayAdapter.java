package com.examples.scs.finalprojectclient;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mihaidornea on 5/16/2018.
 */

public class UserArrayAdapter extends ArrayAdapter<UserDto>{

    private Button user;
    private List<UserDto> userList = new ArrayList<>();
    private Context context;
    private String username;

    @Override
    public void add(UserDto object){
        userList.add(object);
        super.add(object);
    }

    public UserArrayAdapter(Context context, int textViewResourceId, String username){
        super(context, textViewResourceId);
        this.context = context;
        this.username = username;
    }

    public int getCount(){
        return this.userList.size();
    }

    public UserDto getItem(int index){
        return this.userList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        UserDto userDto = getItem(position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.user_entry, parent, false);
        user = row.findViewById(R.id.userButton);
        user.setClickable(true);
        user.setText(userDto.getFirstName() + " " + userDto.getLastName());
        user.setOnClickListener(view -> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("StringUsername", username);
            intent.putExtra("StringToUsername", userDto.getUsername());
            context.startActivity(intent);
        });
        return row;
    }

}
