package com.hudhud.megalot.AppUtils.Responses;

public class Intro {
    private String id, title, description, image, status;

    public Intro(String id, String title, String description, String image, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.status = status;
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

    public String getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }
}
