package com.ff_queen.admin.Models;

public class Result {
    public String id;
    public String category;
    public String game_id;
    public String game_time_id;
    public String result_date;
    public String result;
    public String date;
    public String time;
    public String status;
    public String created_at = null;
    public String updated_at = null;
    public String g_name;
    public String type;
    public String g_img;
    public String cat_name;

    public Result(String id,String result, String date, String time, String cat_name, String type, String game_time_id,String result_date) {
        this.id = id;
        this.result = result;
        this.date = date;
        this.time = time;
        this.cat_name = cat_name;
        this.type = type;
        this.game_time_id= game_time_id;
        this.result_date = result_date;
    }
}
