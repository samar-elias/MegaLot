package com.hudhud.megalot.AppUtils.Responses;

public class Date {
    private String id, date;

    public Date(String date) {
        this.date = date;
    }

    public Date(String id, String date) {
        this.id = id;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }
}
