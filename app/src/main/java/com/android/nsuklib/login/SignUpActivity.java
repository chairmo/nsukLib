package com.android.nsuklib.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.nsuklib.R;
import com.android.nsuklib.logic.MyLogger;
import com.android.nsuklib.utils.BaseActivity;
import com.android.nsuklib.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "SignUpActivity";

    // UI references.
    private AutoCompleteTextView mEmailView;
    //    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;

    private ProgressBar progressBar;
    // Firebase instance variables
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEmailView = findViewById(R.id.register_email);
        mPasswordView = findViewById(R.id.register_password);
        mConfirmPasswordView = findViewById(R.id.register_confirm_password);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);

        //clickListener button
        findViewById(R.id.register_sign_up_button).setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private boolean validateForm() {

        boolean valid = true;
        View focusView = null;

        String password = mPasswordView.getText().toString();
      //  String conPassword = mConfirmPasswordView.getText().toString();
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            valid = false;
        } else {
            mPasswordView.setError(null);
        }

        // Check for a valid email address.
        String email = mEmailView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            valid = false;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            valid = false;
        } else {
            mEmailView.setError(null);
        }

        if (!valid) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        return valid;
    }

    private boolean isEmailValid(String email) {
        //  Add regex here
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        // Add regex for at least 8 characters including lower case, upper case, digit and special character
        return password.equals(mConfirmPasswordView.getText().toString()) && password.length() >= 6;
    }

    //  Create a Firebase user
    private void createFirebaseUser(String email, String password) {

        MyLogger.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        updateUI();


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            MyLogger.d(TAG + "createUserWithEmail: failure", task.getException().toString());
                            showErrorDialog("Registration attempt failed, try again.");
                        } else {
                            showSuccessDialog("Your Account Creation is Successful.");
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        }
                        stopUI();
                    }
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent logIn = new Intent(SignUpActivity.this, LoginActivity.class);
        logIn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(logIn);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Utils utils = new Utils(this);
        int id = v.getId();
        if (id == R.id.register_sign_up_button) {
            if (utils.isNetworkAvailable()) {
                createFirebaseUser(mEmailView.getText().toString(), mPasswordView.getText().toString());
            }
        } else {
            Toast.makeText(SignUpActivity.this, "Oops! no internet connection!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI() {
        progressBar.setVisibility(View.VISIBLE);
        findViewById(R.id.imageView).setVisibility(View.INVISIBLE);
        findViewById(R.id.register_email).setVisibility(View.INVISIBLE);
        findViewById(R.id.register_password).setVisibility(View.INVISIBLE);
        findViewById(R.id.register_confirm_password).setVisibility(View.INVISIBLE);
        findViewById(R.id.register_sign_up_button).setVisibility(View.INVISIBLE);
    }

    private void stopUI() {
        progressBar.setVisibility(View.INVISIBLE);
        findViewById(R.id.imageView).setVisibility(View.VISIBLE);
        findViewById(R.id.register_email).setVisibility(View.VISIBLE);
        findViewById(R.id.register_password).setVisibility(View.VISIBLE);
        findViewById(R.id.register_confirm_password).setVisibility(View.VISIBLE);
        findViewById(R.id.register_sign_up_button).setVisibility(View.VISIBLE);
    }
}
