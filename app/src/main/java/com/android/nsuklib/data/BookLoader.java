package com.android.nsuklib.data;

import android.content.Context;
import android.util.Log;

import com.android.nsuklib.models.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chairmo on 5/28/2018.
 */

public class BookLoader extends android.support.v4.content.AsyncTaskLoader<List<Book>> {

    public BookLoader(Context context){
        super(context);
    }

    @Override
    protected void onStartLoading() {
        Log.d("onStartLoading", "StartLoading..... ");
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        MySQLiteHelper db = new MySQLiteHelper(getContext());

        return new ArrayList<>(db.getAllBooks());

    }
}
