package com.android.nsuklib.login;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.nsuklib.R;
import com.android.nsuklib.models.SharedPrefManager;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends AppCompatActivity {

    private Context mContext = this;
    private CircleImageView circleImageView;
    private TextView nameView;
    private TextView emailView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (getSupportActionBar() != null)
                (getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }

        circleImageView = findViewById(R.id.circleImageView);
        nameView = findViewById(R.id.family_name);
        emailView = findViewById(R.id.nav_email);

        //display profile data and configure signing activity
        displayProfile();

    }

    private void displayProfile() {
        SharedPrefManager manager = new SharedPrefManager(mContext);
        String name = manager.getName();
        String email = manager.getUserEmail();

 /*       Uri photo = null;
        if (manager.getPhoto() != null){
           photo = Uri.parse(manager.getPhoto());
        }
*/
        String uri = manager.getPhoto();

        if (uri != null) {
            Uri photo = Uri.parse(uri);
            Picasso.with(mContext).load(photo).placeholder(R.drawable.some_people)
                    .resize(100, 100)
                    .centerCrop().into(circleImageView);
        }
        nameView.setText(name);
        emailView.setText(email);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}



