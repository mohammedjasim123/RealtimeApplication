package com.example.realtimeapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText email1,phone,pass1,cnfirm;
    FirebaseAuth mauth;
    FirebaseDatabase database;
    Button register;

    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email1 = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        pass1 = findViewById(R.id.password);
        cnfirm = findViewById(R.id.confirm);
        login = findViewById(R.id.tvlogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            }
        });

        mauth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createuser();
            }
        });

    }

    private void createuser() {

        String Email = email1.getText().toString();
        String Phone = phone.getText().toString();
        String Pass = pass1.getText().toString();
        String Cnfm = cnfirm.getText().toString();

        if (TextUtils.isEmpty(Email)){
            email1.setError("Email cannot be empty");
            email1.requestFocus();
        } else if (TextUtils.isEmpty(Phone)) {
            phone.setError("Phone number cannot be empty");
            phone.requestFocus();
        } else if (TextUtils.isEmpty(Pass)){
            pass1.setError("Password cannot be empty");
            pass1.requestFocus();
        } else if (TextUtils.isEmpty(Cnfm)){
            cnfirm.setError("Cannot be empty");
            cnfirm.requestFocus();
        } else if (TextUtils.equals(Cnfm,Pass)){

            //Authentication
            mauth.createUserWithEmailAndPassword(Email,Pass)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(RegisterActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "User Failed To Create", Toast.LENGTH_SHORT).show();
                        }
                    });

            //store to realtime database

            Map<String, Object> details = new HashMap<>();
            details.put("Email",Email);
            details.put("Phone",Phone);
            details.put("Pass",Pass);
            DatabaseReference reference = database.getReference().child("User");
            reference.setValue(details)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(RegisterActivity.this, "Successfully inserted to realtime database", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Insertion Failed", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            cnfirm.setError("Password does not match");
            cnfirm.requestFocus();
        }


    }
}