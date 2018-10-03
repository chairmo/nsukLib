package com.android.nsuklib.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.nsuklib.R;
import com.android.nsuklib.logic.MyLogger;
import com.android.nsuklib.settings.SettingsActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class AdminLogin extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "AdminLogin";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (getSupportActionBar() != null)
                (getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }

        progressBar = findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.INVISIBLE);


        findViewById(R.id.googleBtn).setOnClickListener(this);
        //configure google signing action
        configureSingIn();

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(AdminLogin.this, SettingsActivity.class));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.googleBtn) {
            signIn();
        }
    }

    // This method configures Google SignIn
    private void signIn() {
        updateUI();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Configure Google Sign In helper method
    private void configureSingIn() {
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(AdminLogin.this.getResources().getString(R.string.web_clied_id))
                        .requestEmail()
                        .build();

        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    // This IS the method where the result of clicking the signIn button will be handled
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, save Token and a state then authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();

                String idToken = account.getIdToken();

                AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                firebaseAuthWithGoogle(credential);
            } else {
                // Google Sign In failed, update UI appropriately
               MyLogger.e(TAG, "Login Unsuccessful. ");
                Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    //After a successful sign into Google, this method now authenticates the user with Firebase
    private void firebaseAuthWithGoogle(AuthCredential credential) {
        updateUI();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        MyLogger.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            MyLogger.d(TAG, "signInWithCredential" + task.getException().getMessage());
                            task.getException().printStackTrace();
                            Toast.makeText(AdminLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //
                            Toast.makeText(AdminLogin.this, "Login successful",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AdminLogin.this, SettingsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        stopUI();
                    }
                });
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void updateUI() {
        progressBar.setVisibility(View.VISIBLE);
        findViewById(R.id.googleBtn).setVisibility(View.INVISIBLE);
    }

    private void stopUI() {
        progressBar.setVisibility(View.INVISIBLE);
        findViewById(R.id.googleBtn).setVisibility(View.VISIBLE);
    }

}
