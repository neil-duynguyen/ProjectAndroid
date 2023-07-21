package com.example.projectandroid.providerFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.projectandroid.R;
import com.example.projectandroid.adapters.ChatAdapter;
import com.example.projectandroid.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class ChatFragmentProvider extends Fragment {

    private ListView listView;
    private EditText editTextMessage;
    private Button btnSend;
    private DatabaseReference databaseReference;
    private List<Message> messages;
    private String currentUserID;
    private ChatAdapter chatAdapter;

    public ChatFragmentProvider() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("messages");
        messages = new ArrayList<>();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserID = currentUser.getEmail();
        } else {
            // Handle the case when the user is not signed in or authentication fails
            currentUserID = null;
        }
        chatAdapter = new ChatAdapter(messages, currentUserID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_provider, container, false);
        listView = view.findViewById(R.id.list_viewp);
        listView.setAdapter(chatAdapter);
        editTextMessage = view.findViewById(R.id.edit_text_messagep);
        btnSend = view.findViewById(R.id.btn_sendp);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Listen for new messages in the Firebase Database
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Message message = dataSnapshot.getValue(Message.class);
                messages.add(message);
                chatAdapter.notifyDataSetChanged();
                listView.smoothScrollToPosition(messages.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        // Send button click listener
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString();
                if (!message.isEmpty()) {
                    sendMessage(message);
                    editTextMessage.setText("");
                }
            }
        });
    }

    private void sendMessage(String message) {
        if (currentUserID != null) {
            // Push the new message to the Firebase Database
            String key = databaseReference.push().getKey();
            Message newMessage = new Message(currentUserID, message);
            databaseReference.child(key).setValue(newMessage);
        } else {
            // Handle the case when the user is not signed in or authentication fails
        }
    }
}