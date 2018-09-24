package com.android.nsuklib.logic;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.nsuklib.R;
import com.android.nsuklib.barcode.ScanQR;
import com.android.nsuklib.data.BookAdapter;
import com.android.nsuklib.data.BookDetails;
import com.android.nsuklib.data.BookLoader;
import com.android.nsuklib.login.LoginActivity;
import com.android.nsuklib.login.Profile;
import com.android.nsuklib.models.Book;
import com.android.nsuklib.models.SharedPrefManager;
import com.android.nsuklib.utils.AboutApp;
import com.android.nsuklib.utils.BaseActivity;
import com.android.nsuklib.utils.HelpActivity;
import com.android.nsuklib.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<List<Book>>,
        GoogleApiClient.OnConnectionFailedListener {


    public static final int PHOTO = 101;
    private BookAdapter bookAdapter;
    private static final int LOADER_ID = 1;

    Context mContext = this;

    SharedPrefManager sharedPrefManager;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private CircleImageView circleImageView;
    private TextView mFullNameTextView;
    private TextView mEmailTextView;
    private NavigationView navigationView;

    private ArrayList<Book> bookArrayList;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mAuth = FirebaseAuth.getInstance();

        //navigation drawer action
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //get navigation views
        getViews();
        //display the profile information
        userDetails();
        displayProfile();

        //initialize the adapter on the listView
        initListOfBooks();

        //get reference to the Connectivity manager to check state of network
        if (new Utils(this).isNetworkAvailable()) {
            //get reference to loaderManager
            LoaderManager manager = getSupportLoaderManager();
            manager.initLoader(LOADER_ID, null, this).forceLoad();
        } else {
            //display error
            MyLogger.d("onCreate: ", "no network connection");
        }
    }

    //initialize bookAdapter, listView and sent intent extra to Profile Activity
    private void initListOfBooks() {
        //initialize the BookAdapter class
        bookArrayList = new ArrayList<>();
        bookAdapter = new BookAdapter(this, bookArrayList);

        //find a reference to a listView item in the layout
        ListView listBook = findViewById(R.id.listView_item);

        //set the adapter
        listBook.setAdapter(bookAdapter);
        listBook.setTextFilterEnabled(true);

        listBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //     Book book = bookArrayList.get(position);
                Book book = (Book) parent.getAdapter().getItem(position);

                String author = book.getAuthor();
                String title = book.getTitle();
                String isbn = book.getIsbn();
                String date = book.getDatePublish();

                //launch BookDetails activity with the passed id
                Intent intent = new Intent(getApplicationContext(), BookDetails.class);
                intent.putExtra("author", author);
                intent.putExtra("title", title);
                intent.putExtra("isbn", isbn);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

    }

    private void getViews() {
        //getting navigation header views
        circleImageView = navigationView.getHeaderView(0).findViewById(R.id.image_holder);
        mFullNameTextView = navigationView.getHeaderView(0).findViewById(R.id.name_holder);
        mEmailTextView = navigationView.getHeaderView(0).findViewById(R.id.email_holder);
    }

    //helper method for storing data in sharedPreference and display in CircleImageView, TextView
    public void displayProfile() {
        // create an object of sharedPreferenceManager and get stored user data
        //       sharedPrefManager = new SharedPrefManager(mContext);
        String mUsername = sharedPrefManager.getName();
        String mEmail = sharedPrefManager.getUserEmail();

//        String uri = sharedPrefManager.getPhoto();
//        Uri mPhotoUri = Uri.parse(uri);

        Uri mPhotoUri = null;
        if (sharedPrefManager.getPhoto() != null) {
            mPhotoUri = Uri.parse(sharedPrefManager.getPhoto());
        }

        //Set data gotten from SharedPreference to the Navigation Header view
        mFullNameTextView.setText(mUsername);
        mEmailTextView.setText(mEmail);

        Picasso.with(mContext)
                .load(mPhotoUri)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon).resize(90, 90).centerCrop()
                .into(circleImageView);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);

        MenuItem search = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) search.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //     bookAdapter.filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                bookAdapter.getFilter().filter(newText);
                return true;
            }
        });

        return true;

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.user_profile) {
            startActivity(new Intent(getApplicationContext(), Profile.class));
            // Handle the setting action
        }
/*        else if (id == R.id.settings) {
            showAlert("Sorry! You don't have Admin Privilege to view this page.");

        }  */
        else if (id == R.id.barcode) {
            startActivity(new Intent(getApplicationContext(), ScanQR.class));

        } else if (id == R.id.nav_share) {
            //         shareImage();
            images();

        } else if (id == R.id.nav_help) {
            startActivity(new Intent(getApplicationContext(), HelpActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(getApplicationContext(), AboutApp.class));
        } else if (id == R.id.sign_out) {
            signOut();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        mAuth.signOut();
        //go to login page
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    //launch gallery
    private void images() {
        Intent mediaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //    mediaIntent.setType("images/*");
        startActivityForResult(mediaIntent, PHOTO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO && resultCode == Activity.RESULT_OK) {
            Uri imagesUri = data.getData();
            if (imagesUri != null) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"email"});
                intent.putExtra(Intent.EXTRA_TEXT, "nsukLib");
                intent.setType("images/*");
                intent.putExtra(Intent.EXTRA_STREAM, imagesUri);
                startActivity(Intent.createChooser(intent, "Send to....."));
            }
        }
    }

    private void shareImage() {
        Uri uri = Uri.parse(new File(Environment.getExternalStorageDirectory().getPath()).toString());
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/jpeg");
        startActivity(intent);
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(MainActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        bookAdapter.clear();
        if (books != null && !books.isEmpty())
            bookAdapter.addAll(books);

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        Log.d("onLoadReset", "unchecked call error ");
        bookAdapter.clear();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //getting user details
    public void userDetails() {
        FirebaseUser user = mAuth.getCurrentUser();
        sharedPrefManager = new SharedPrefManager(mContext);
        if (user != null) {
            Uri photo = user.getPhotoUrl();
            String foto = photo != null ? photo.toString() : null;
            sharedPrefManager.savePhoto(mContext, foto);


            sharedPrefManager.saveName(mContext, user.getDisplayName());
            sharedPrefManager.saveEmail(mContext, user.getEmail());
        }
    }
}