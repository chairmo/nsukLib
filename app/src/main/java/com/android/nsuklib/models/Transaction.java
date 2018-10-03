package com.android.nsuklib.models;

public class Transaction {
    private String authorTrans;
    private String titleTrans;
    private String isbnTrans;
    private String timeTrans;
    private String pickUpDate;
    private String dropOffDate;
    private int id;
    private String dateTrans;
    private String userName;


    public Transaction() {
    }

    public Transaction(String user, String author, String title, String isbn, String dateT, String timeT,
                       String pickD, String dropD) {
        userName = user;
        authorTrans = author;
        titleTrans = title;
        isbnTrans = isbn;
        dropOffDate = dropD;
        pickUpDate = pickD;
        timeTrans = timeT;
        dateTrans = dateT;
    }


    @Override
    public String toString() {
        return this.getPickUpDate();
    }

    /*********************************GETTERS********************************************/

    public String getUserName() {
        return userName;
    }

    public String getAuthorTrans() {
        return authorTrans;
    }

    public String getIsbnTrans() {
        return isbnTrans;
    }

    public String getTitleTrans() {
        return titleTrans;
    }

    public String getDateTrans() {
        return dateTrans;
    }

    public String getTimeTrans() {
        return timeTrans;
    }

    public String getPickUpDate() {
        return pickUpDate;
    }

    public String getDropOffDate() {
        return dropOffDate;
    }

    public int getId() {
        return id;
    }

    /***********************************SETTERS*************************************/

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAuthorTrans(String authorTrans) {
        this.authorTrans = authorTrans;
    }

    public void setIsbnTrans(String isbnTrans) {
        this.isbnTrans = isbnTrans;
    }

    public void setTitleTrans(String titleTrans) {
        this.titleTrans = titleTrans;
    }

    public void setDateTrans(String dateTrans) {
        this.dateTrans = dateTrans;
    }

    public void setTimeTrans(String timeTrans) {
        this.timeTrans = timeTrans;
    }

    public void setPickUpDate(String pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public void setDropOffDate(String dropOffDate) {
        this.dropOffDate = dropOffDate;
    }

    public void setId(int id) {
        this.id = id;
    }

}
