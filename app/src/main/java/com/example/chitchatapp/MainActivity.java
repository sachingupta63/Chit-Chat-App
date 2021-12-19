package com.example.chitchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.chitchatapp.Adapters.ViewPagerAdapter;
import com.example.chitchatapp.Model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView image;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        image=findViewById(R.id.profile_image);
        username=findViewById(R.id.user_username);

        TabLayout tabLayout=findViewById(R.id.tablayout);
        ViewPager viewPager=findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                User user=snapshot.getValue(User.class);
                username.setText(user.getUsername());

                if(user.getImageURL().equals("default")) {
                    image.setImageResource(R.mipmap.ic_launcher);
                }
                else{
                    Glide.with(MainActivity.this).load(user.getImageURL()).into(image);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this,"Error in datbase",Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

       switch(item.getItemId()){
           case R.id.logout:
               FirebaseAuth.getInstance().signOut();
               startActivity(new Intent(MainActivity.this,StartActivity.class));
               finish();
               return true;
       }
       return  false;
    }
}