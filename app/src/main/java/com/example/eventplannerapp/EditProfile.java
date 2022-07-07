package com.example.eventplannerapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {

    Button save_button;
    private FirebaseAuth mAuth;
    private EditText name_text;
    private EditText birthday_text;
    private EditText bio_text;
    private EditText email_text;
    private static int PICK_IMAGE = 123;

    private ImageView profileImage;
    Uri imagePath;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private ProgressBar progressBar;

    public EditProfile() {
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null) {
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, Login.class));
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        name_text = (EditText) findViewById(R.id.name_text);
        birthday_text = (EditText) findViewById(R.id.birthday_text);
        email_text = (EditText) findViewById(R.id.emailText);
        bio_text = (EditText) findViewById(R.id.bio_text);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        profileImage = findViewById(R.id.profile_image);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        profileImage.setOnClickListener(v -> mGetContent.launch("image/*"));

    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if(result != null) {
                        profileImage.setImageURI(result);
                    }

                }
            });

    private void userInformation() {
        String name = name_text.getText().toString().trim();
        String birthday = birthday_text.getText().toString().trim();
        String bio = bio_text.getText().toString().trim();
        String email = email_text.getText().toString().trim();
        UserInformation userinformation = new UserInformation(name, birthday, bio, email);
        FirebaseUser user = mAuth.getCurrentUser();
        databaseReference.child(user.getUid()).setValue(userinformation);
        Toast.makeText(getApplicationContext(),"User information updated",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view){
        if(view == save_button) {
            progressBar.setVisibility(View.VISIBLE);
            if(imagePath == null) {
                Drawable drawable = ResourcesCompat.getDrawable(getResources(),R.drawable.mprofile, getTheme());
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mprofile);
                userInformation();
                progressBar.setVisibility(View.GONE);
                finish();
                startActivity(new Intent(this, ProfileFragment.class));
            }
            else {
                userInformation();
                sendUserData();
                finish();
                startActivity(new Intent(this, ProfileFragment.class));
            }
        }
    }

    private void sendUserData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(mAuth.getUid());
        StorageReference imageReference = storageReference.child(mAuth.getUid()).child("Images").child("Profile Pic");
        UploadTask uploadTask = imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error: Uploading profile picture",Toast.LENGTH_LONG).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "Profile Picture Uploaded", Toast.LENGTH_LONG).show();

            }
        });

    }

}