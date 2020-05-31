package com.example.travelapp;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText fname , mail , password , passwrod2;
    TextView sin;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    ImageView sback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        sback = (ImageView)findViewById(R.id.sback);
        sin = findViewById(R.id.sin);
        fname = findViewById(R.id.fname);
        mail = findViewById(R.id.mail);
        password = findViewById(R.id.password);
        passwrod2 = findViewById(R.id.password2);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        sin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String s_fname = fname.getText().toString().trim();
                final String s_email = mail.getText().toString().trim();
                final String s_password = password.getText().toString().trim();
                final String s_password2 = passwrod2.getText().toString().trim();
                if(!s_password.equals(s_password2)){
                    passwrod2.setError("Please set valid Password");
                    return;
                }
                if(TextUtils.isEmpty(s_fname)){
                    fname.setError("Name is required");
                    return;
                }
                if(TextUtils.isEmpty(s_email)){
                    mail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(s_password)) {
                    password.setError("Password is Required");
                    return;
                }
                if(TextUtils.isEmpty(s_password2)){
                    passwrod2.setError("Please Confirm your password");
                    return;
                }
                if(s_password.length()<6){
                    password.setError("Password must be >= 6 characters");
                }
                System.out.println("Password:"+s_password);
                fAuth.createUserWithEmailAndPassword(s_email,s_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Signup.this, "User Created.", Toast.LENGTH_SHORT).show();
                            userId = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userId);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fName",s_fname);
                            user.put("email",s_email);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userId);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),Signin.class));

                        }else {
                            Toast.makeText(Signup.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
        sback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(Signup.this, Login.class);
                startActivity(it);

            }
        });
    }
}
