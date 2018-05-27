package com.examples.scs.finalprojectclient.utilities.arrayAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.examples.scs.finalprojectclient.R;
import com.examples.scs.finalprojectclient.activities.MainActivity;
import com.examples.scs.finalprojectclient.activities.UserSelectionActivity;
import com.examples.scs.finalprojectclient.utilities.UserDto;

import java.util.ArrayList;
import java.util.Collections;
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
        boolean exists = false;
        for(UserDto userDto : userList){
            if (userDto.getUsername().equals(object.getUsername())){
                exists = true;
            }
        }
        if (exists){
            exists = false;
        } else {
            userList.add(object);
            super.add(object);
        }
    }

    @Override
    public void addAll(UserDto... items) {
        Collections.addAll(userList, items);
        super.addAll(items);
    }

    @Override
    public void remove(@Nullable UserDto object) {
        super.remove(object);
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
            ((UserSelectionActivity) context).stopThread();
            context.startActivity(intent);
        });
        return row;
    }

}
