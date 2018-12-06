package com.android.nsuklib.barcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.nsuklib.R;
import com.android.nsuklib.data.MySQLiteHelper;
import com.android.nsuklib.models.Transaction;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class ScanQR extends AppCompatActivity {

    private final Activity activity = this;
    private MySQLiteHelper db = new MySQLiteHelper(this);
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_reader);

        textView = findViewById(R.id.textView);

        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan a barcode");
        integrator.setBeepEnabled(false);
        integrator.setCameraId(0);
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(activity, "QR Code Scan Cancelled.", Toast.LENGTH_SHORT).show();
            }
            String content = result.getContents();
            textView.setText(content);
         //   Toast.makeText(activity, result.getContents(), Toast.LENGTH_SHORT).show();

     /*       else {

                Log.d("BARCODE RESULT: ", result.getContents());
                Log.d("author: ", data.getStringExtra("author"));

                String title = data.getStringExtra("title");
                String userName = data.getStringExtra("name");
                String author = data.getStringExtra("author");
                String isbn = data.getStringExtra("isbn");
                String pickUpDate = data.getStringExtra("pickDate");

                Intent intent = new Intent(ScanQR.this, DisplayResult.class);      */
  //              Bundle info = new Bundle();



    //            info.putString("title", title);
   //             info.putString("name", userName);
   //             info.putString("pickDate", pickUpDate);
   //             info.putString("author", author);
   //             info.putString("isbn", isbn);
   //             intent.putExtras(info);
    //            startActivity(intent);

//              nameText.setText(userName);
//              titleText.setText(title);
//              dateText.setText(pickUpDate);
//            }

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //method for checking and comparing transaction values
    public boolean checkTransValue(String name) {
        ArrayList<Transaction> transactions = new ArrayList<>(db.getAllTransactions());

        try {
            return transactions.contains(name);
        }catch (NullPointerException ex){
            ex.getMessage();
        }
       return false;
    }
}

