package com.hudhud.megalot.AppUtils.Responses;

public class DataBottom {
    private String id, title, description;

    public DataBottom(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
