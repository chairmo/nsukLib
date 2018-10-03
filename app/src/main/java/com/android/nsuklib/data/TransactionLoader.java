package com.android.nsuklib.data;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.nsuklib.logic.MyLogger;
import com.android.nsuklib.models.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionLoader extends android.support.v4.content.AsyncTaskLoader<List<Transaction>> {

    public TransactionLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
       MyLogger.d("onStartLoading", "StartLoading..... ");
      forceLoad();
    }

    @Nullable
    @Override
    public List<Transaction> loadInBackground() {
        MySQLiteHelper db = new MySQLiteHelper(getContext());

        return new ArrayList<>(db.getAllTransactions());
    }
}
