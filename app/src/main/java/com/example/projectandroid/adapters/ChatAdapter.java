package com.example.projectandroid.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.view.Gravity;
import com.example.projectandroid.R;
import com.example.projectandroid.model.Message;
import android.util.Log;

import java.util.List;

public class ChatAdapter extends BaseAdapter {

    private List<Message> messages;
    private String currentUserID;

    public ChatAdapter(List<Message> messages, String currentUserID) {
        this.messages = messages;
        this.currentUserID = currentUserID;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = messages.get(position);

        if (message.getUsername().equals(currentUserID)) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
        } else {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
        }

        TextView tvUsername = convertView.findViewById(R.id.tv_username);
        TextView tvMessage = convertView.findViewById(R.id.tv_message);

        // Set email và tin nhắn
        tvUsername.setText(message.getUsername());
        tvMessage.setText(message.getContent());

        return convertView;
    }
}


