package com.example.chitchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText username,email,password;
    Button btn_register;
    DatabaseReference reference;
    FirebaseAuth auth;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar=findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username=findViewById(R.id.username);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);

        btn_register=findViewById(R.id.btn_register);

        auth=FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName=username.getText().toString();
                String passWord=password.getText().toString();
                String eMail=email.getText().toString();

                if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord) || TextUtils.isEmpty(eMail)){

                    Toast.makeText(RegisterActivity.this, "Every field required", Toast.LENGTH_SHORT).show();

                }else if(passWord.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters..", Toast.LENGTH_SHORT).show();

                }
                else
                 {

                    register(email.getText().toString(), password.getText().toString(), username.getText().toString());
                }
                
            }
        });
    }
    private void register(String email,String password,String username){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser=auth.getCurrentUser();
                            String userId=firebaseUser.getUid();

                            reference= FirebaseDatabase.getInstance().getReference("Users").child(userId);

                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("id",userId);
                            hashMap.put("username",username);
                            hashMap.put("imageURL","default");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }


                                }
                            });

                        }else{
                            Toast.makeText(RegisterActivity.this, "You cant't register with this email or password", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}