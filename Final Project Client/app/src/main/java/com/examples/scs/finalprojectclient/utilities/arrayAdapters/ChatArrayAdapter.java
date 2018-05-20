package com.examples.scs.finalprojectclient.utilities.arrayAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.examples.scs.finalprojectclient.messages.ChatMessage;
import com.examples.scs.finalprojectclient.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mihaidornea on 5/16/2018.
 */

public class ChatArrayAdapter extends ArrayAdapter<ChatMessage>{

    private TextView chatText;
    private List<ChatMessage> chatMessageList = new ArrayList<>();
    private Context context;

    @Override
    public void add(ChatMessage object){
        chatMessageList.add(object);
        super.add(object);
        super.notifyDataSetChanged();
    }

    public ChatArrayAdapter(Context context, int textViewResourceId){
        super(context, textViewResourceId);
        this.context = context;
    }

    public int getCount(){
        return this.chatMessageList.size();
    }

    public ChatMessage getItem(int index){
        return this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ChatMessage chatMessageObj = getItem(position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (chatMessageObj.left) {
            row = inflater.inflate(R.layout.right, parent, false);
        } else {
            row = inflater.inflate(R.layout.left, parent, false);
        }
        chatText = row.findViewById(R.id.msgr);
        chatText.setText(chatMessageObj.message);
        return row;
    }

}
