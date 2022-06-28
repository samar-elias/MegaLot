package com.hudhud.megalot.AppUtils.Responses;

public class Bracket {
    private String id, title, pointIn, pointEnd, appInstall, youtube, popUpTicket;

    public Bracket() {
        this.id = "";
        this.title = "";
        this.pointIn = "";
        this.pointEnd = "";
        this.appInstall = "";
        this.youtube = "";
        this.popUpTicket = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPointIn() {
        return pointIn;
    }

    public void setPointIn(String pointIn) {
        this.pointIn = pointIn;
    }

    public String getPointEnd() {
        return pointEnd;
    }

    public void setPointEnd(String pointEnd) {
        this.pointEnd = pointEnd;
    }

    public String getAppInstall() {
        return appInstall;
    }

    public void setAppInstall(String appInstall) {
        this.appInstall = appInstall;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getPopUpTicket() {
        return popUpTicket;
    }

    public void setPopUpTicket(String popUpTicket) {
        this.popUpTicket = popUpTicket;
    }
}
