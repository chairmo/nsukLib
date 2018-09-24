package com.android.nsuklib.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.nsuklib.R;
import com.android.nsuklib.logic.MainActivity;
import com.android.nsuklib.logic.MyLogger;
import com.android.nsuklib.utils.BaseActivity;
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


    public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 9001;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
 //   public SharedPrefManager sharedPrefManager;
    private ProgressBar progressBar;

    private final Context mContext = this;


    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.INVISIBLE);


        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == R.integer.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainActivity);
                }
            }
        };

        //clickListener buttons
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.googleBtn).setOnClickListener(this);
        findViewById(R.id.signUpBtn).setOnClickListener(this);
        findViewById(R.id.admin_sign_in_button).setOnClickListener(this);

        //configure google signing action
        configureSingIn();

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }


    //method to signing existing user, which takes in email and password. And validates them
    private void attemptLogin() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (email.equals("") || password.equals(""))
            return;
        else {
            Toast.makeText(this, "Login in process...", Toast.LENGTH_SHORT).show();
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            stopUI();
                            showErrorDialog("There was a problem signing in.");
                        } else {
                            String str = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
                            MyLogger.d("Firebase", str);
                            finish();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                    }
                });
    }

    /* *************************************GOOGLE SIGNING ACTIVITY*******************************************/
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
                        .requestIdToken(LoginActivity.this.getResources().getString(R.string.web_clied_id))
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
                            MyLogger.i(TAG, "signInWithCredential" + task.getException().getMessage());
                            task.getException().printStackTrace();
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //
                            Toast.makeText(LoginActivity.this, "Login successful",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                       stopUI();
                    }
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void openSignUp() {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    private void adminLogin(){
        startActivity(new Intent(LoginActivity.this, AdminLogin.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.email_sign_in_button) {
            updateUI();
            attemptLogin();

        } else if (id == R.id.googleBtn ) {
            signIn();
        } else if (id == R.id.signUpBtn) {
            openSignUp();
        } else if ( id == R.id.admin_sign_in_button){
            adminLogin();
        }
    }
    private void updateUI() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void stopUI() {
        progressBar.setVisibility(View.INVISIBLE);
    }

}
