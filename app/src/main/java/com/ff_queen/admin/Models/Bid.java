package com.ff_queen.admin.Models;

public class Bid {
    public String id;
    public String user_id;
    public String game_id;
    public String start_time;
    public String baji;
    public String digit;
    public String rupees;
    public String category;
    public String date;
    public String time;
    public String win;
    public String created_at;
    public String updated_at;
    public String g_name;
    public String type;
    public String g_img;
    public String cat_name;

    public Bid(String id, String g_img, String g_name) {
        this.id = id;
        this.g_img= g_img;
        this.g_name = g_name;
    }
}
