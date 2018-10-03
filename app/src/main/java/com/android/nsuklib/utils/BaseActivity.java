package com.android.nsuklib.utils;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.android.nsuklib.R;

import java.util.regex.Pattern;

public class BaseActivity extends AppCompatActivity {


    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9+._%-+]{1,256}" +
            "@" + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "." + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");


    //  Create an alert dialog to show in case registration failed
    public void showErrorDialog(String message) {
        new AlertDialog.Builder(this).setTitle("Oops!").setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    public void showSuccessDialog(String message) {
        new AlertDialog.Builder(this).setTitle("Congratulations! ")
                .setMessage(message).setPositiveButton(android.R.string.ok, null)
                .setIcon(R.drawable.success).show();
    }

    public void showAlert(String alert) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(alert);
 //       dlgAlert.setTitle("nsukLib Library System");
        dlgAlert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }
}
