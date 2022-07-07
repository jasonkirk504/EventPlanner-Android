package com.example.eventplannerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private TextView forgot;
    private Button login_button;
    //Input Texts
    private EditText email_text, password_text;
    //Firebase Instance
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        register = (TextView) findViewById(R.id.register_text);
        forgot = (TextView) findViewById(R.id.forgot_text);
        forgot.setOnClickListener(this);
        register.setOnClickListener(this);
        login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(this);

        //User credentials
        email_text = (EditText) findViewById(R.id.email_text);
        password_text = (EditText) findViewById(R.id.password_text);

        //Firebase Instance
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_text:
                startActivity(new Intent(this, RegisterUser.class));
                break;

            case R.id.forgot_text:
                startActivity(new Intent(this, ForgotUser1.class));
                break;

            case R.id.login_button:
                login_user();
                break;

        }
    }

    private void login_user() {
        String email = email_text.getText().toString().trim();
        String password = password_text.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            login2();

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Login failed, try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void login2() {
        startActivity(new Intent(this, MainActivity.class));
    }

}


