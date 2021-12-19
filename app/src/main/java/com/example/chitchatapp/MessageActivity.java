package com.example.chitchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chitchatapp.Adapters.MessageAdapter;
import com.example.chitchatapp.Model.Chat;
import com.example.chitchatapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;
    ImageButton btn_send;
    EditText text_send;
    RecyclerView message_recyclerview;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    Intent intent;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar=findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MessageActivity.this,MainActivity.class));
            }
        });

        message_recyclerview=findViewById(R.id.message_recyclerview);
        message_recyclerview.setHasFixedSize(true);
        message_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        profile_image=findViewById(R.id.message_profile_image);
        username=findViewById(R.id.message_user_username);
        btn_send=findViewById(R.id.btn_send);
        text_send=findViewById(R.id.edit_message);

        intent=getIntent();

        String userid=intent.getStringExtra("userID");

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=text_send.getText().toString();
                if(!msg.equals("")) {
                    sendMessage(firebaseUser.getUid(),userid,msg);
                }else{
                    Toast.makeText(MessageActivity.this, "Enter the message to send...", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                User user=snapshot.getValue(User.class);
                username.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(profile_image);
                }

                readMessage(firebaseUser.getUid(),userid,user.getImageURL());

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
    private void sendMessage(String sender,String receiver,String message){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        reference.child("Chats").push().setValue(hashMap);

    }

    private void readMessage(String myId,String userid, String imageUrl){
        mChat=new ArrayList<>();

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Chats");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mChat.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Chat chat=snapshot1.getValue(Chat.class);
                    if (chat.getReceiver().equals(myId) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(myId)){
                        mChat.add(chat);
                    }
                }
                messageAdapter=new MessageAdapter(MessageActivity.this,mChat,imageUrl);
                message_recyclerview.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}