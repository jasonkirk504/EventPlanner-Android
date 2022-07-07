package com.example.eventplannerapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {

    private FirebaseDatabase firebaseDatabase;
    private ImageView profilePic;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;
    private TextView name_text;
    private TextView birthday_text;
    private TextView bio_text;
    private TextView email_text;
    private FirebaseAuth mAuth;
    private Button pButton;
    private TextView profile_text;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        profile_text = view.findViewById(R.id.profile_text);
        name_text = view.findViewById(R.id.pName_text);
        birthday_text = view.findViewById(R.id.pBirthday_text);
        bio_text = view.findViewById(R.id.pBio_text);
        email_text = view.findViewById(R.id.pEmail_text);
        pButton = view.findViewById(R.id.pSave_button);
        profilePic = view.findViewById(R.id.profilePic);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(mAuth.getUid());
        StorageReference storageReference = firebaseStorage.getReference();
        // Get the image stored on Firebase via "User id/Images/Profile Pic.jpg".
        storageReference.child(mAuth.getUid()).child("Images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Using "Picasso" (http://square.github.io/picasso/) after adding the dependency in the Gradle.
                // ".fit().centerInside()" fits the entire image into the specified area.
                // Finally, add "READ" and "WRITE" external storage permissions in the Manifest.
                Picasso.get().load(uri).fit().centerInside().into(profilePic);
            }
        });
        final FirebaseUser user = mAuth.getCurrentUser();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                UserInformation userProfile = dataSnapshot.child(user.getUid()).getValue(UserInformation.class);
                profile_text.setText(userProfile.getName());
                email_text.setText(userProfile.getEmail());
                bio_text.setText(userProfile.getBio());
                birthday_text.setText(userProfile.getBirthday());
            }
            @Override
            public void onCancelled( DatabaseError databaseError) {
                Toast.makeText(view.getContext(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), EditProfile.class));
            }
        });

    }
}