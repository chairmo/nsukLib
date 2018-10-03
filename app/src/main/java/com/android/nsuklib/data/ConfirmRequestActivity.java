package com.android.nsuklib.data;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.nsuklib.R;
import com.android.nsuklib.barcode.GenerateQR;
import com.android.nsuklib.models.SharedPrefManager;
import com.android.nsuklib.models.Transaction;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfirmRequestActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mUsername, mEmail, mAuthor, mTitle, mISBN, mDate, mPickDate, mDropDate;
    private CircleImageView imageView;
    private Context mContext = this;

    private String author, title, isbn, date, pickDate, dropDate, user;
    private SharedPrefManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_request);

        mAuthor = findViewById(R.id.author_name);
        mDate = findViewById(R.id.publish_date);
        mEmail = findViewById(R.id.nav_email);
        mUsername = findViewById(R.id.family_name);
        mISBN = findViewById(R.id.book_isbn);
        mTitle = findViewById(R.id.book_title);
        mPickDate = findViewById(R.id.pick_up_date);
        imageView = findViewById(R.id.circleImageView);
        mDropDate = findViewById(R.id.drop_off_date);

        findViewById(R.id.proceed_button).setOnClickListener(this);

        manager = new SharedPrefManager(mContext);

        //     Bundle extras = getIntent().getExtras();
        displayProfile();
    }

    private void displayProfile() {
        //get intent extra and update views
        Bundle extras = getIntent().getExtras();
        pickDate = extras.getString("dateStr");
        title = extras.getString("title");
        isbn = extras.getString("isbn");
        author = extras.getString("author");
        date = extras.getString("date");
        dropDate = extras.getString("dropStr");

        // update book details and booking views
        mDropDate.setText(dropDate);
        mPickDate.setText(pickDate);
        mTitle.setText(title);
        mISBN.setText(isbn);
        mDate.setText(date);
        mAuthor.setText(author);

        //update profile view

        String uri = manager.getPhoto();

        //check if there's photo and update view
        if (uri != null) {
            Uri photo = Uri.parse(uri);
            Picasso.with(mContext).load(photo).placeholder(R.drawable.some_people)
                    .resize(100, 100)
                    .centerCrop().into(imageView);
        }
        //update name and email views
        user = manager.getName();
        if (user != null)
            mUsername.setText(user);
        mEmail.setText(manager.getUserEmail());
    }

    //set in the transaction parameters
    private void updateTransaction() {
        MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());

        // Transaction date and time
        DateFormat df = new SimpleDateFormat("HH:mm", Locale.UK); //format time
        String timeTrans = df.format(Calendar.getInstance().getTime());
        DateFormat df1 = new SimpleDateFormat("dd - MM - yyyy", Locale.getDefault());//format date
        String dateTrans = df1.format(Calendar.getInstance().getTime());


        Transaction transaction = new Transaction(user, author, title, isbn, dateTrans, timeTrans,
                pickDate, dropDate);
        db.addTransaction(transaction);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.proceed_button) {

            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Do you wish to book your reservation ? ");
            dlgAlert.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            updateTransaction();
                            Toast.makeText(mContext, "Congratulations! Your Reservation is Successful.",
                                    Toast.LENGTH_SHORT).show();
                            //Passes All info to the next page GenerateQR
                            Intent Intent = new Intent(getApplicationContext(), GenerateQR.class);

                            Bundle extraInfo = new Bundle();

                            extraInfo.putString("author", author);
                            extraInfo.putString("title", title);
                            extraInfo.putString("isbn", isbn);
                            extraInfo.putString("date", date);
                            extraInfo.putString("dateStr", pickDate);

                            Intent.putExtras(extraInfo);
                            startActivity(Intent);
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();


        }
    }


}


