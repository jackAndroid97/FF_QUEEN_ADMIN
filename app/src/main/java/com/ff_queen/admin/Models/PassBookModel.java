package com.ff_queen.admin.Models;

public class PassBookModel {
    String date,time,desc,pre_bal,amt,av_bal;

    public PassBookModel(String date, String time, String desc, String pre_bal, String amt, String av_bal) {
        this.date = date;
        this.time = time;
        this.desc = desc;
        this.pre_bal = pre_bal;
        this.amt = amt;
        this.av_bal = av_bal;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPre_bal() {
        return pre_bal;
    }

    public void setPre_bal(String pre_bal) {
        this.pre_bal = pre_bal;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getAv_bal() {
        return av_bal;
    }

    public void setAv_bal(String av_bal) {
        this.av_bal = av_bal;
    }
}
