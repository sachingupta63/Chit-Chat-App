package com.example.chitchatapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chitchatapp.Adapters.UserRecyclerAdapter;
import com.example.chitchatapp.Model.Chat;
import com.example.chitchatapp.Model.User;
import com.example.chitchatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserRecyclerAdapter userRecyclerAdapter;
    private ArrayList<User> mUser;
    private ArrayList<String> userList;
    private HashSet<String> hashSet;

    FirebaseUser firebaseUser;
    DatabaseReference reference;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView=view.findViewById(R.id.chat_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        userList=new ArrayList<>();

        reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Chat chat=snapshot1.getValue(Chat.class);

                    if (chat.getSender().equals(firebaseUser.getUid())){
                        userList.add(chat.getReceiver());
                    }
                    if (chat.getReceiver().equals(firebaseUser.getUid())){
                        userList.add(chat.getSender());
                    }
                    hashSet=new HashSet<>(userList);
                }
               readChat();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        return view;
    }

    private void readChat(){
        mUser=new ArrayList<>();

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mUser.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    User user=snapshot1.getValue(User.class);

                    for (String id:hashSet){
                        if (user.getid().equals(id)){
                            mUser.add(user);
                        }
                    }

                   /* for (String in: userList){
                        if (user.getid().equals(in)){
                            if (mUser.size() != 0){
                                for (User user1: mUser){
                                    if(!user.getid().equals(user1.getid())){
                                        mUser.add(user);
                                    }
                                }
                            }else {
                                mUser.add(user);
                            }
                        }
                    }*/
                }

                userRecyclerAdapter=new UserRecyclerAdapter(getContext(),mUser);
                recyclerView.setAdapter(userRecyclerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }
}