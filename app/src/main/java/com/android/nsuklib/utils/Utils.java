package com.android.nsuklib.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Utils {
    // public static final String USER_KEY = "users";

    private Context mContext = null;

    /**
     * Public constructor that takes mContext for later use
     */
    public Utils(Context con) {
        mContext = con;
    }


    //This is a method to Check if the device internet connection is currently on
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void saveImage(Context context, Bitmap bitmap) {

        File storage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(storage, "QR_" +
                (int) System.currentTimeMillis() + ".jpg");
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            scanFile(context, Uri.fromFile(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private static void scanFile(Context context, Uri imageUri) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(imageUri);
        context.sendBroadcast(intent);
    }

    public static void DeleteFile(String fileName) {

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + "/QRCodes");
        myDir.mkdirs();

        String fname = fileName;
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            file.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sharePost(Context context, LinearLayout view) {
        try {
//            Bitmap b = ((BitmapDrawable) view.getDrawable()).getBitmap();
            Bitmap b = loadBitmapFromView(view);
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    b, "Title", null);
            Uri imageUri = Uri.parse(path);
            share.putExtra(Intent.EXTRA_STREAM, imageUri);
//            share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=traveldestination.evonative.com.traveldestination");
            context.startActivity(Intent.createChooser(share, "Share Via"));
        } catch (Exception e) {
            Toast.makeText(context, "CHECK PERMISSION FROM SETTINGS", Toast.LENGTH_LONG).show();
        }
    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap bitmap;
        v.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static void shareFile(Context context) {
        File sdDir = Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES);
        File file = new File(sdDir, "QR_");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (file.exists()) {
            intent.setDataAndType(Uri.withAppendedPath(Uri.fromFile(file),
                    "/QR_"), "image/*");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "No existing file in the storage!", Toast.LENGTH_SHORT).show();
        }
    }

}
