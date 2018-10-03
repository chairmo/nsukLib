package com.android.nsuklib.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.nsuklib.R;
import com.android.nsuklib.models.Book;

import java.util.List;

/**
 * Created by chairmo on 5/24/2018.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    //public constructor
    public BookAdapter(@NonNull Context context, List<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //check for existing list item
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.book_list,
                    parent, false);
        }
        //find the book position in the list of books
        Book book = getItem(position);

        //find the textView with author name and book title
        TextView author = view.findViewById(R.id.author_name);
        author.setText(book.getAuthor());

        TextView title = view.findViewById(R.id.book_title);
        title.setText(book.getTitle());

        return view;
    }
}


