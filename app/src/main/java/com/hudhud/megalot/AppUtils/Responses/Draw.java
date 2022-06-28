package com.hudhud.megalot.AppUtils.Responses;

import java.util.ArrayList;

public class Draw {
    private String id, prize, increaseValue, date, dateTime, myPrize, no1, no2, no3, no4, no5, no6;
    private BigWinner bigWinner;
    private ArrayList<DrawResult> drawResults;

    public Draw() {
        this.id = "";
        this.prize = "";
        this.increaseValue = "";
        this.date = "";
        this.dateTime = "";
        this.myPrize = "";
        this.no1 = "";
        this.no2 = "";
        this.no3 = "";
        this.no4 = "";
        this.no5 = "";
        this.no6 = "";
        this.bigWinner = new BigWinner();
        this.drawResults = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getIncreaseValue() {
        return increaseValue;
    }

    public void setIncreaseValue(String increaseValue) {
        this.increaseValue = increaseValue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMyPrize() {
        return myPrize;
    }

    public void setMyPrize(String myPrize) {
        this.myPrize = myPrize;
    }

    public String getNo1() {
        return no1;
    }

    public void setNo1(String no1) {
        this.no1 = no1;
    }

    public String getNo2() {
        return no2;
    }

    public void setNo2(String no2) {
        this.no2 = no2;
    }

    public String getNo3() {
        return no3;
    }

    public void setNo3(String no3) {
        this.no3 = no3;
    }

    public String getNo4() {
        return no4;
    }

    public void setNo4(String no4) {
        this.no4 = no4;
    }

    public String getNo5() {
        return no5;
    }

    public void setNo5(String no5) {
        this.no5 = no5;
    }

    public String getNo6() {
        return no6;
    }

    public void setNo6(String no6) {
        this.no6 = no6;
    }

    public BigWinner getBigWinner() {
        return bigWinner;
    }

    public void setBigWinner(BigWinner bigWinner) {
        this.bigWinner = bigWinner;
    }

    public ArrayList<DrawResult> getDrawResults() {
        return drawResults;
    }

    public void setDrawResults(ArrayList<DrawResult> drawResults) {
        this.drawResults = drawResults;
    }
}
