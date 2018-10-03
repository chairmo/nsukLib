package com.android.nsuklib.barcode;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.nsuklib.R;
import com.android.nsuklib.logic.MainActivity;
import com.android.nsuklib.models.SharedPrefManager;
import com.android.nsuklib.utils.Utils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GenerateQR extends AppCompatActivity implements View.OnClickListener{
    private ImageView qrImage;
    private String qrString;
    private Button saveQr;
    private Context mContext = this;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_code);

        if (Build.VERSION.SDK_INT >= 23){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        qrImage = findViewById(R.id.qrImage);
        findViewById(R.id.generate_qr).setOnClickListener(this);

        saveQr = findViewById(R.id.save_qr);
        saveQr.setVisibility(View.INVISIBLE);
        saveQr.setOnClickListener(this);


        //get intent extra passed and covert it to string
        Bundle extras = getIntent().getExtras();
        String author = extras.getString("author");
        String title = extras.getString("title");
        String isbn = extras.getString("isbn");
        String date = extras.getString("date");
        String pickDate = extras.getString("dateStr");

        //initialize SharedPredManager class to get name, email and photo url
        SharedPrefManager manager = new SharedPrefManager(mContext);
        String name = manager.getName();
        String email = manager.getUserEmail();
        String photo = manager.getPhoto();

        qrString = "User Photo: " + photo + "\n" + "User Name: " + name + "\n" + "Email Address: " +
                email + "\n" + "Book Author: " + author + "\n" + "Book Title: " + title + "\n" +
                "Book ISBN: " + isbn + "Publication Date: " + date + "\n" +
                "Requested Pick up date: " + pickDate;

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.generate_qr) {

            //generate barcode image and update save button to visible
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try{
                BitMatrix bitMatrix = multiFormatWriter.encode(qrString, BarcodeFormat.QR_CODE,
                        256,256);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                qrImage.setImageBitmap(bitmap);
                saveQr.setVisibility(View.VISIBLE);
            }catch(WriterException e){
                e.printStackTrace();
            }

        }

        //save the bitmap in the phone storage
        if (v.getId() == R.id.save_qr) {
            qrImage.buildDrawingCache();
            Bitmap bmp = qrImage.getDrawingCache();
            new Utils(this);
            Utils.saveImage(getApplicationContext(), bmp);
            Toast.makeText(mContext, "QR Code Saved Successfully!", Toast.LENGTH_SHORT).show();
            Log.d("BITMAP: ", "save bitmap: " + bmp.getByteCount());
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

    }
}


