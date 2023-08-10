package com.ff_queen.admin.Models;

public class BidHistoryModel {
    String date,time,game_name,digit,amt,name,category,baji,mobile;

    public BidHistoryModel(String date, String time, String game_name, String digit, String amt, String name,
                           String category,String baji,String mobile) {
        this.date = date;
        this.time = time;
        this.game_name = game_name;
        this.digit = digit;
        this.amt = amt;
        this.name = name;
        this.category = category;
        this.baji = baji;
        this.mobile = mobile;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getDigit() {
        return digit;
    }

    public void setDigit(String digit) {
        this.digit = digit;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBaji() {
        return baji;
    }

    public void setBaji(String baji) {
        this.baji = baji;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
