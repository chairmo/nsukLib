package com.android.nsuklib.models;

public class Book {
    private int id;
    private String author;
    private String title;
    private String isbn;
    private String datePublish;


    public Book(){
    }

    public Book(String author, String title, String isbn, String datePub) {
        this.author = author;
        this.title = title;
        this.isbn = isbn;
        this.datePublish = datePub;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getDatePublish() {
        return datePublish;
    }

    public String getAuthor() {
        return author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDatePublish(String datePublish) {
        this.datePublish = datePublish;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    //override toString to enable filtering using arrayAdapter
    @Override
    public String toString() {
        return this.author;
    }
}
