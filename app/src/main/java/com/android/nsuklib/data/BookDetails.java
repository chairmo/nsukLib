package com.android.nsuklib.data;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.nsuklib.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BookDetails extends AppCompatActivity implements View.OnClickListener {

    private EditText mBookDate, mDropDate;
    private String dateStr, dropStr;
    static String pickDay = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detais_viewl);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (getSupportActionBar() != null)
                (getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }

        TextView author = findViewById(R.id.author_name);
        TextView title = findViewById(R.id.book_title);
        TextView isbn = findViewById(R.id.book_isbn);
        TextView date = findViewById(R.id.publish_date);

        mBookDate = findViewById(R.id.pickUp);
        mBookDate.setOnClickListener(this);

        mDropDate = findViewById(R.id.dropOffDate);
        mDropDate.setOnClickListener(this);

        findViewById(R.id.choosedate_button).setOnClickListener(this);

        //get intent extra from @MainActivity class and update views
        author.setText(getIntent().getStringExtra("author"));
        title.setText(getIntent().getStringExtra("title"));
        isbn.setText(getIntent().getStringExtra("isbn"));
        date.setText(getIntent().getStringExtra("date"));

    }

    //ensure EditText view for date isn't empty
    private boolean checkField() {
        //get text from the editText fields
        boolean valid = true;
        View focusView = null;

        dateStr = mBookDate.getText().toString();
        dropStr = mDropDate.getText().toString();

        //gives error report if the editText field is empty
        if (TextUtils.isEmpty(dateStr)) {
            mBookDate.setError("Please select pick up date");
            focusView = mBookDate;
            valid = false;
        }
        if (TextUtils.isEmpty(dropStr)) {
            mDropDate.setError("Please select drop off date");
            focusView = mDropDate;
            valid = false;
        }

        if (!valid) {
            focusView.requestFocus();
        }
        return valid;
    }

    private void pickUpDay() {
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                        pickDay = (dayOfMonth + " - " + (month + 1) + " - " + year);
                        SimpleDateFormat format = new SimpleDateFormat("dd - MM - yyyy", Locale.US);

                        try {
                            Date selectedDate = format.parse(pickDay);
                            Date currentDate = new Date(System.currentTimeMillis());

                            long days = 1000 * 60 * 60 * 24;
                            long diff = (selectedDate.getTime() - currentDate.getTime()) / days;

                            if (currentDate.compareTo(selectedDate) < 0 && diff < 6) {
                                mBookDate.setText(pickDay);
                            } else {
                                Toast.makeText(BookDetails.this, getText(R.string.pick_up),
                                        Toast.LENGTH_SHORT).show();

                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void dropOffDate() {
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                        String dropDay = (dayOfMonth + " - " + (month + 1) + " - " + year);
                        SimpleDateFormat format = new SimpleDateFormat("dd - MM - yyyy", Locale.US);


                        try {
                            Date selectedDate = format.parse(dropDay);
                            Date currentPick = format.parse(pickDay);
                            Date currentDate = new Date(System.currentTimeMillis());

                            long days = 1000 * 60 * 60 * 24;
                            long diff = (selectedDate.getTime() - currentPick.getTime()) / days;

                            if (pickDay.compareTo(dropDay) < 0 && diff < 4) {
                                mDropDate.setText(dropDay);
                            } else {
                                Toast.makeText(BookDetails.this, getText(R.string.text_drop_off),
                                        Toast.LENGTH_SHORT).show();

                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pickUp) {
            pickUpDay();

        }
        if (v.getId() == R.id.dropOffDate) {
            dropOffDate();
        }

        if (v.getId() == R.id.choosedate_button) {
            if (checkField()) {

                Bundle extras = getIntent().getExtras();

                String author = extras.getString("author");
                String title = extras.getString("title");
                String isbn = extras.getString("isbn");
                String date = extras.getString("date");


                //Passes All info to the next page ConfirmRequestActivity
                Intent I = new Intent(getApplicationContext(), ConfirmRequestActivity.class);
                Bundle extraInfo = new Bundle();

                extraInfo.putString("author", author);
                extraInfo.putString("title", title);
                extraInfo.putString("isbn", isbn);
                extraInfo.putString("date", date);
                extraInfo.putString("dateStr", dateStr);
                extraInfo.putString("dropStr", dropStr);

                I.putExtras(extraInfo);
                startActivity(I);

                mBookDate.setText("");
                mDropDate.setText("");

            }
        }
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
