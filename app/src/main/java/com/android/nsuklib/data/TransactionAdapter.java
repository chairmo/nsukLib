package com.android.nsuklib.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.nsuklib.R;
import com.android.nsuklib.logic.MyLogger;
import com.android.nsuklib.models.Transaction;

import java.util.List;


public class TransactionAdapter extends ArrayAdapter<Transaction> {

    private static final String TAG = TransactionAdapter.class.getSimpleName();

    public TransactionAdapter(@NonNull Context context, List<Transaction> transactions) {
        super(context, 0, transactions);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        //check for existing list item
        if (convertView == null) {
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.transaction_list,
                    parent, false);

           viewHolder = new ViewHolder(convertView);
           convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Transaction transaction = getItem(position);
        Log.i(TAG, viewHolder.user.getText().toString());
        //find the reference to the textView id to display
        try {
            if (transaction != null) {
                viewHolder.id.setText(String.valueOf(transaction.getId()));
                viewHolder.transTime.setText(transaction.getTimeTrans());
                viewHolder.transDate.setText(transaction.getDateTrans());
                viewHolder.user.setText(transaction.getUserName());
                viewHolder.transTitle.setText(transaction.getTitleTrans());
                viewHolder.transAuthor.setText(transaction.getAuthorTrans());
                viewHolder.transISBN.setText(transaction.getIsbnTrans());
                viewHolder.transPickup.setText(transaction.getPickUpDate());
                viewHolder.transReturn.setText(transaction.getDropOffDate());
            }
        }catch (Exception ex){
            ex.printStackTrace();
            MyLogger.d(TAG,  ex.toString());
        }

        return convertView;
    }

    public class ViewHolder {

        TextView id, transDate, transTime, user,
                transAuthor, transTitle, transISBN, transPickup, transReturn;
       ViewHolder(View view){
            id =  view.findViewById(R.id.transaction_id);
            transDate =  view.findViewById(R.id.transaction_date);
            transTime =  view.findViewById(R.id.transaction_time);
            user =  view.findViewById(R.id.user_name);
            transAuthor =  view.findViewById(R.id.book_author);
            transTitle =  view.findViewById(R.id.book_title);
            transISBN =  view.findViewById(R.id.book_isbn);
            transPickup = view.findViewById(R.id.pick_up_date);
            transReturn = view.findViewById(R.id.return_date);

        }
    }
}