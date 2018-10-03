package com.android.nsuklib.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.nsuklib.logic.MyLogger;
import com.android.nsuklib.models.Book;
import com.android.nsuklib.models.Transaction;

import java.util.ArrayList;

public class MySQLiteHelper extends SQLiteOpenHelper {


    // Database Name - BookDB
    private static final String DATABASE_NAME = "BookDB";

    // Table Name - books
    private static final String TABLE_BOOKS = "books";
    private static final String TABLE_TRANSACTIONS = "transactions";

    // Columns Names of books Table
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_ISBN = "isbn";
    private static final String KEY_DATE_PUB = "published";

    //Columns Names of transaction table
    private static final String KEY_NUMBER = "number";
    private static final String KEY_RETURN_DATE = "dropOffDate";
    private static final String KEY_PICKUP_DATE = "pickupDate";
    private static final String KEY_DATE_TRANS = "dateTrans";
    private static final String KEY_TIME_TRANS = "timeTrans";
    private static final String KEY_TITLE_TRANS = "titleTrans";
    private static final String KEY_AUTHOR_TRANS = "authorTrans";
    private static final String KEY_ISBN_TRANS = "isbnTrans";
    private static final String KEY_USERNAME = "userName";


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Log TAG for debugging purpose
    private static final String TAG = "SQLiteDebugLog";

    // Constructor
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOK_TABLE = "CREATE TABLE books ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "author TEXT, " + "title TEXT, " + "isbn TEXT UNIQUE, " + "published TEXT " + ")";


        String CREATE_TRANSACTION_TABLE = "CREATE TABLE transactions ( " +
                "number INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "authorTrans TEXT, " + "titleTrans TEXT, " + "isbnTrans TEXT, " + "dateTrans TEXT, " +
                "timeTrans TEXT, " + "pickupDate TEXT, " + "dropOffDate TEXT, " + "userName TEXT " + ")";

        // execute an SQL statement to create the table
        db.execSQL(CREATE_BOOK_TABLE);
        db.execSQL(CREATE_TRANSACTION_TABLE);
    }

    // onUpdate() is invoked when you upgrade the database scheme.

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS books");
        //Droff older transaction table if existed
        db.execSQL("DROP TABLE IF EXISTS transactions");

        // create fresh books and users table
        this.onCreate(db);
    }

    //******************************METHODS FOR BOOK*******************************************

    public void addBook(Book book) {
       MyLogger.d(TAG, "addBook() - " + book.toString());
        //  get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        //  create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_AUTHOR, book.getAuthor()); // get author
        values.put(KEY_TITLE, book.getTitle()); // get title
        values.put(KEY_ISBN, book.getIsbn()); // get isbn
        values.put(KEY_DATE_PUB, book.getDatePublish());

        //  insert
        db.insert(TABLE_BOOKS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close - release the reference of writable DB
        db.close();
    }

    // Get all books from the database
    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> books = new ArrayList<>();

        //  build the query
        String query = "SELECT  * FROM " + TABLE_BOOKS;

        //  get reference to writable DB
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        //  go over each row, build book and add it to list
        Book book = null;
        if (cursor.moveToFirst()) {
            do {
                book = new Book();
                book.setId(Integer.parseInt(cursor.getString(0)));

                System.out.println("TESTid: " + Integer.parseInt(cursor.getString(0)));

                book.setAuthor(cursor.getString(1));
                book.setTitle(cursor.getString(2));
                book.setIsbn(cursor.getString(3));
                book.setDatePublish(cursor.getString(4));

                //print the get book details
                System.out.println("set author: " + cursor.getString(1));
                System.out.println("set title: " + cursor.getString(2));
                System.out.println("set isbn: " + cursor.getString(3));
                //        System.out.println("set date: " + cursor.getString(4));
                // Add book to books
                books.add(book);
            } while (cursor.moveToNext());
        }

        MyLogger.d(TAG, "getAllBooks() - " + books.toString());

        // return books
        return books;
    }

    // Updating single book
    public int updateBook(Book book) {

        //  get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        //  create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("title", book.getTitle()); // get title
        values.put("author", book.getAuthor()); // get author
        values.put("isbn", book.getIsbn()); // get author

        //  updating row
        int i = db.update(TABLE_BOOKS, //table
                values, // column/value
                KEY_ID + " = ?", // selections
                new String[]{String.valueOf(book.getId())}); //selection args
        //  close
        db.close();
        return i;
    }


    public void deleteBook(String isbn) throws SQLException {
        //  get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        String[] whereArgs = new String[]{isbn};
        db.delete(TABLE_BOOKS, KEY_ISBN + "=?", whereArgs);
    }

    /*******************************METHOD FOR TRANSACTION TABLE*****************************************/
    public void addTransaction(Transaction transaction) {
     MyLogger.d(TAG, "addTransaction() - " + transaction.toString());
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();

        values.put(KEY_AUTHOR_TRANS, transaction.getAuthorTrans());
        values.put(KEY_TITLE_TRANS, transaction.getTitleTrans());
        values.put(KEY_ISBN_TRANS, transaction.getIsbnTrans());
        values.put(KEY_DATE_TRANS, transaction.getDateTrans());
        values.put(KEY_TIME_TRANS, transaction.getTimeTrans());
        values.put(KEY_PICKUP_DATE, transaction.getPickUpDate());
        values.put(KEY_RETURN_DATE, transaction.getDropOffDate());
        values.put(KEY_USERNAME, transaction.getUserName());

        // insert to table
        db.insert(TABLE_TRANSACTIONS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // close - release the reference of writable DB
        db.close();
    }

    // Get all transactions from the database
    public ArrayList<Transaction> getAllTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        //  build the query
        String query = "SELECT  * FROM " + TABLE_TRANSACTIONS;

        //  get reference to writable DB
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        //  go over each row, build book and add it to list
        Transaction transaction = null;
        if (cursor.moveToFirst()) {
            do {
                transaction = new Transaction();

                transaction.setId(Integer.parseInt(cursor.getString(0)));
                transaction.setAuthorTrans(cursor.getString(1));
                transaction.setTitleTrans(cursor.getString(2));
                transaction.setIsbnTrans(cursor.getString(3));
                transaction.setDateTrans(cursor.getString(4));
                transaction.setTimeTrans(cursor.getString(5));
                transaction.setPickUpDate(cursor.getString(6));
                transaction.setDropOffDate(cursor.getString(7));
                transaction.setUserName(cursor.getString(8));

                // Add book to books
                transactions.add(transaction);
            } while (cursor.moveToNext());
        }

        MyLogger.d(TAG, "getAllTransactions() - " + transactions.toString());

        // return books
        return transactions;
    }

}

