package com.hudhud.megalot.AppUtils.Responses;

public class NextDraw {
    private String id, price, date, dateTime, increaseValue;

    public NextDraw() {
        this.id = "";
        this.price = "";
        this.date = "";
        this.dateTime = "";
        this.increaseValue = "";
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setIncreaseValue(String increaseValue) {
        this.increaseValue = increaseValue;
    }

    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getIncreaseValue() {
        return increaseValue;
    }
}
