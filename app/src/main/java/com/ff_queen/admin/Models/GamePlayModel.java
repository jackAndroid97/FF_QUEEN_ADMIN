package com.ff_queen.admin.Models;

public class GamePlayModel {
    String date,amt,name,mobile,u_id;

    public GamePlayModel(String date, String amt, String name, String mobile,String u_id) {
        this.date = date;
        this.amt = amt;
        this.name = name;
        this.mobile = mobile;
        this.u_id = u_id;
    }

    public String getDate() {
        return date;
    }

    public String getAmt() {
        return amt;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getU_id() {
        return u_id;
    }
}
