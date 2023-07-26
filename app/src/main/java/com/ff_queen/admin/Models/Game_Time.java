package com.ff_queen.admin.Models;

public class Game_Time {
    public String id;
    public String game_id;
    public String start_time;
    public String end_time;
    public String end_time_spcl;
    public String created_at;
    public String updated_at;
    public String status;
    public String countdown_time;
    public float count;
    private String date_status;
    private String now_time;


    // Getter Methods

    public String getId() {
        return id;
    }

    public String getGame_id() {
        return game_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getEnd_time_spcl() {
        return end_time_spcl;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getStatus() {
        return status;
    }

    public String getCountdown_time() {
        return countdown_time;
    }

    public float getCount() {
        return count;
    }

    public String getDate_status() {
        return date_status;
    }

    public String getNow_time() {
        return now_time;
    }

    // Setter Methods

    public void setId(String id) {
        this.id = id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setEnd_time_spcl(String end_time_spcl) {
        this.end_time_spcl = end_time_spcl;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCountdown_time(String countdown_time) {
        this.countdown_time = countdown_time;
    }

    public void setCount(float count) {
        this.count = count;
    }

    public void setDate_status(String date_status) {
        this.date_status = date_status;
    }

    public void setNow_time(String now_time) {
        this.now_time = now_time;
    }
}
