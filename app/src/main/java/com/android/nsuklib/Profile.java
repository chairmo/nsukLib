package com.android.nsuklib;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        Button mLogOut = (Button) findViewById(R.id.buttonLogOut);
        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(Profile.this, LoginActivity.class));
                }
            }
        };
        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mListener);
    }

    private void updateUI(FirebaseUser currentUser) {

        TextView yourName = (TextView) findViewById(R.id.nav_name);
        TextView yourEmail = (TextView) findViewById(R.id.nav_email);
        CircleImageView civ = (CircleImageView) findViewById(R.id.circleImageView);

        if (currentUser.getDisplayName() == null) {
            yourName.setText("");
        } else {
            yourName.setText(currentUser.getDisplayName());
        }
        if (currentUser.getEmail() == null) {
            yourEmail.setText("");
        } else {
            yourEmail.setText(currentUser.getEmail());
        }
        try {
            if (currentUser.getPhotoUrl().toString() == null) {
                Picasso.with(this).load(R.drawable.some_people).into(civ);
            } else {
                Picasso.with(this).load(currentUser.getPhotoUrl()).into(civ);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



