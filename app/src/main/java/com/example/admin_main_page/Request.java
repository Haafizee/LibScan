package com.example.admin_main_page;

public class Request
{
    String bookID;
    String userID;
    String requestID;

    public Request()
    {
    }

    public String getBookID()
    {
        return bookID;
    }

    public void setBookID(String bookID)
    {
        this.bookID = bookID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Request(String bookID, String userID, String requestID) {
        this.bookID = bookID;
        this.userID = userID;
        this.requestID = requestID;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }
}
