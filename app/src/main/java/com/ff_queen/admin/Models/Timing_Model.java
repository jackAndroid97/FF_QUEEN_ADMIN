package com.ff_queen.admin.Models;

public class Timing_Model {

    public String id, time, start_time, end_time, status, baji,date_status, name,time_id,a_status;
    public String image;

    public Timing_Model(String id, String time, String status,String start_time,String end_time, String baji, String date_status,String time_id,String a_status) {
        this.id = id;
        this.time = time;
        this.status = status;
        this.start_time = start_time;
        this.end_time = end_time;
        this.baji = baji;
        this.date_status = date_status;
        this.time_id = time_id;
        this.a_status = a_status;
    }

    public Timing_Model(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }


    public String getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public String  getImage() {
        return image;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getBaji() {
        return baji;
    }

    public String getDate_status() {
        return date_status;
    }

    @Override
    public String toString() {
        return  start_time;
    }

    public String getName() {
        return name;
    }

    public String getTime_id() {
        return time_id;
    }

    public String getA_status() {
        return a_status;
    }
}

