package com.android.nsuklib.settings;

import android.app.SearchManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.nsuklib.R;
import com.android.nsuklib.data.TransactionAdapter;
import com.android.nsuklib.data.TransactionLoader;
import com.android.nsuklib.logic.MyLogger;
import com.android.nsuklib.models.Transaction;
import com.android.nsuklib.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class GenTransactions extends AppCompatActivity  implements
        android.support.v4.app.LoaderManager.LoaderCallbacks<List<Transaction>>{
    private ArrayList<Transaction> transactionList;
    private TransactionAdapter transactionAdapter;
    private static final int LOADER_ID = 1;

//   private MySQLiteHelper db = new MySQLiteHelper(this);
//   private SharedPrefManager manager = new SharedPrefManager(this);
//   private int reservation = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (getSupportActionBar() != null)
                (getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }

        //get reference to the Connectivity manager to check state of network
        if (new Utils(this).isNetworkAvailable()) {
            //get reference to loaderManager
            android.support.v4.app.LoaderManager manager = getSupportLoaderManager();
           manager.initLoader(LOADER_ID, null, this).forceLoad();
        } else {
            //display error
            MyLogger.d("onCreate: ", "no network connection");
        }
        ListView view = findViewById(R.id.listView_item);
        transactionList = new ArrayList<>();
        transactionAdapter = new TransactionAdapter(this,transactionList);

        view.setAdapter(transactionAdapter);
        view.setTextFilterEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trans_menu, menu);

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

                transactionAdapter.getFilter().filter(newText);
                return true;
            }
        });

        return true;
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @NonNull
    @Override
    public Loader<List<Transaction>> onCreateLoader(int id, @Nullable Bundle args) {
        return new TransactionLoader(getApplicationContext());
    }


    @Override
    public void onLoadFinished(@NonNull Loader<List<Transaction>> loader, List<Transaction> data) {
        transactionAdapter.clear();
        if (data != null && !data.isEmpty())
            transactionAdapter.addAll(data);
    }


    @Override
    public void onLoaderReset(@NonNull Loader<List<Transaction>> loader) {

    }
}
