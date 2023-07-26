package com.ff_queen.admin.Models;

public class Koyel_Result_Model {

    String date;
    String morning_result;
    String day_result;
    String evening_result;
    String night_result;

    public void setDate(String date) {
        this.date = date;
    }

    public void setMorning_result(String morning_result) {
        this.morning_result = morning_result;
    }

    public void setDay_result(String day_result) {
        this.day_result = day_result;
    }

    public void setEvening_result(String evening_result) {
        this.evening_result = evening_result;
    }

    public String getDate() {
        return date;
    }

    public String getMorning_result() {
        return morning_result;
    }

    public String getDay_result() {
        return day_result;
    }

    public String getEvening_result() {
        return evening_result;
    }

    public String getNight_result() {
        return night_result;
    }

    public void setNight_result(String night_result) {
        this.night_result = night_result;
    }
}
