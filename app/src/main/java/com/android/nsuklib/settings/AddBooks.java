package com.android.nsuklib.settings;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.nsuklib.R;
import com.android.nsuklib.models.Book;
import com.android.nsuklib.data.MySQLiteHelper;
import com.android.nsuklib.utils.BaseActivity;

import java.util.Calendar;

public class AddBooks extends BaseActivity implements View.OnClickListener {

    private EditText mBookTitle, mBookAuthor, mBookISBN, mBookDate;
    private Button upload;
    private MySQLiteHelper db = new MySQLiteHelper(this);
    private String title, author, isbn, date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_books);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (getSupportActionBar() != null)
                (getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }

        mBookTitle = findViewById(R.id.book_title_edit);
        mBookAuthor = findViewById(R.id.book_author_edit);
        mBookISBN = findViewById(R.id.book_isbn_edit);
        mBookDate = findViewById(R.id.book_date_edit);
        mBookDate.setOnClickListener(this);

        upload = findViewById(R.id.book_upload);
        upload.setOnClickListener(this);

    }

    private boolean checkField() {
        //get text from the editText fields
        boolean valid = true;
        View focusView = null;

        title = mBookTitle.getText().toString().trim();
        author = mBookAuthor.getText().toString().trim();
        isbn = mBookISBN.getText().toString().trim();
        date = mBookDate.getText().toString();

        //gives error report if the editText field is empty
        if (TextUtils.isEmpty(title)) {
            mBookTitle.setError(getString(R.string.empty_title));
            focusView = mBookTitle;
            valid = false;
        }
        if (TextUtils.isEmpty(author)) {
            mBookAuthor.setError(getString(R.string.empty_author));
            focusView = mBookAuthor;
            valid = false;
        }
        if (TextUtils.isEmpty(isbn)){
            mBookISBN.setError("Please enter ISBN of the book");
            focusView = mBookISBN;
            valid = false;
        }
        if (!valid) {
            focusView.requestFocus();
        }
        return valid;
    }

    @Override
    public void onClick(View v) {
        //dialog popup for date selection on editText click
        if (v == mBookDate) {
            final Calendar calendar = Calendar.getInstance();
            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            mBookDate.setText(dayOfMonth + " - " + (month + 1) + " - " + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (v == upload) {
            if (checkField()) {
                 Book book = new Book(author, title, isbn, date);

                //add books to the database
                db.addBook(book);

                Toast.makeText(this, getString(R.string.success_upload), Toast.LENGTH_SHORT).show();
                mBookDate.setText("");
                mBookTitle.setText("");
                mBookISBN.setText("");
                mBookAuthor.setText("");
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
