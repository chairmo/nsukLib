package com.android.nsuklib.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.nsuklib.R;
import com.android.nsuklib.barcode.ScanQR;
import com.android.nsuklib.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        mAuth = FirebaseAuth.getInstance();
        //add items to the arrayList for display on the listView
        final ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Add a book");
        arrayList.add("Remove a book");
        arrayList.add("Transactions");
        arrayList.add("Barcode Scanner");
        arrayList.add("Logout");

        //populate the view with adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, arrayList);

        ListView listView = findViewById(R.id.listView_item);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get item id position when clicked
                if (position == 0){
                    startActivity(new Intent(getApplicationContext(), AddBooks.class));
                }if (position == 1){
                    startActivity(new Intent(getApplicationContext(), DeleteBooks.class));
                }if (position == 2){
                    startActivity(new Intent(getApplicationContext(), GenTransactions.class));
                }if (position == 2){
                    startActivity(new Intent(getApplicationContext(), GenTransactions.class));
                }if (position == 3){
                    startActivity(new Intent(getApplicationContext(), ScanQR.class));
                }
                if (position == 4) {
                   signOut();
                }

            }
        });
    }

    private void signOut() {
        mAuth.signOut();
        //go to login page
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
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
