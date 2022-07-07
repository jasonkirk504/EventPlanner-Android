package com.example.eventplannerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private TextView return_text;
    private EditText email_text, fullname_text, password_text;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();
        //TextView
        return_text = (TextView) findViewById(R.id.return_text);
        return_text.setOnClickListener(this);
        //Button
        registerButton = (Button) findViewById(R.id.register_user_button);
        registerButton.setOnClickListener(this);
        //EditTexts
        fullname_text = (EditText) findViewById(R.id.fullname_text);
        email_text = (EditText) findViewById(R.id.email_text);
        password_text = (EditText) findViewById(R.id.password_text);
        //Progress Bar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.return_text:
                startActivity(new Intent(this,Login.class));
                break;
            case R.id.register_user_button:
                registerUser();
                break;
        }

    }

    private void registerUser() {
        String email = email_text.getText().toString().trim();
        String password = password_text.getText().toString().trim();
        String fullname = fullname_text.getText().toString().trim();

        if(fullname.isEmpty()) {
            fullname_text.setError("Full Name is required");
            fullname_text.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            password_text.setError("Password is required");
            password_text.requestFocus();
            return;
        }

        if(email.isEmpty()) {
            email_text.setError("Email is required");
            email_text.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_text.setError("Please provide valid email");
            email_text.requestFocus();
            return;
        }

        if(password.length() < 6) {
            password_text.setError("Password must be at least 6 characters");
            password_text.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            User user = new User(fullname, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(RegisterUser.this, "User has been registered successfully",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        finish();

                                    }else{
                                        Toast.makeText(RegisterUser.this,"Failed to Register, Try Again.", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });


                        }else{
                            Toast.makeText(RegisterUser.this, "Failed to Register, Try Again.",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }
}