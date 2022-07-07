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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class ForgotUser1 extends AppCompatActivity implements View.OnClickListener {

    private TextView return_text;
    private EditText email_text;
    private Button sendEmail_button;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_user1);

        mAuth = FirebaseAuth.getInstance();
        return_text = (TextView) findViewById(R.id.return_text);
        return_text.setOnClickListener(this);
        sendEmail_button = (Button) findViewById(R.id.sendEmail_button);
        sendEmail_button.setOnClickListener(this);
        email_text = (EditText) findViewById(R.id.email_text);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.return_text:
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.sendEmail_button:
                sendEmail();
                break;
        }
    }

    private void sendEmail() {
        String email = email_text.getText().toString().trim();

        if (email.isEmpty()) {
            email_text.setText("Email is required");
            email_text.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_text.setError("Please provide valid email");
            email_text.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        boolean check = !task.getResult().getSignInMethods().isEmpty();
                        if(!check) { // Email not in database
                            Toast.makeText(getApplicationContext(), "That email is not associated with an account", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                            finishSend();
                        }
                    }
                });
    }

    private void finishSend() {
        String email = email_text.getText().toString().trim();
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Email sent", Toast.LENGTH_LONG ).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Error: Email not sent.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

}