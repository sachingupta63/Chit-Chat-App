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


public class UserFragment extends Fragment {



    public UserFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private UserRecyclerAdapter userRecyclerAdapter;
    private ArrayList<User> user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user, container, false);

        recyclerView=view.findViewById(R.id.recyclreView_user);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        user=new ArrayList<>();

        readUser();


        return view;
    }

    private void readUser(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                user.clear();
                for (DataSnapshot dsp:snapshot.getChildren()){
                   User muser=dsp.getValue(User.class);
                   if (!muser.getid().equals(firebaseUser.getUid())){
                       user.add(muser);
                   }
                }
                userRecyclerAdapter=new UserRecyclerAdapter(getContext(),user);
                recyclerView.setAdapter(userRecyclerAdapter);
                userRecyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}