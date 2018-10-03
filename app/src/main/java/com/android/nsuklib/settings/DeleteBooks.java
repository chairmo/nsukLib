package com.android.nsuklib.settings;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.nsuklib.R;
import com.android.nsuklib.data.MySQLiteHelper;

public class DeleteBooks extends AppCompatActivity implements View.OnClickListener {
    private EditText mISBN;
    private Button delete;
    MySQLiteHelper db = new MySQLiteHelper(this);
    private String isbn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_books);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (getSupportActionBar() != null)
                (getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }

        mISBN = findViewById(R.id.isbn);
        delete = findViewById(R.id.delete);
        delete.setOnClickListener(this);

    }

    private boolean isMatch() {
        boolean valid = true;
        View focusView = null;
        isbn = mISBN.getText().toString().toUpperCase().trim();
        if (TextUtils.isEmpty(isbn)) {
            mISBN.setError("ISBN field cannot be empty");
            focusView = mISBN;
            valid = false;
        }
        if (!valid) {
            focusView.requestFocus();
        }

        return valid;
    }



    //show confirmation before deleting pet from the database
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_a_book);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                db.deleteBook(isbn);
                mISBN.setText("");
                Toast.makeText(DeleteBooks.this, "Book with ISBN: " + isbn +
                        " successfully deleted", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.cancel_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == delete) {
            if (isMatch()) {
                showDeleteConfirmationDialog();

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
