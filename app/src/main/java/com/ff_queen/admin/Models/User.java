package com.ff_queen.admin.Models;

import android.content.Context;
import android.content.SharedPreferences;

public class User {
    private String user_id;

    private Context context;

    SharedPreferences sharedPreferences;


    public User(Context context) {

        this.context = context;

        sharedPreferences = context.getSharedPreferences("login_details", Context.MODE_PRIVATE);

    }

    public String getUser_id() {
        user_id = sharedPreferences.getString("user_id", "");
        return user_id;
    }


    public void setUser_id(String user_id) {
        sharedPreferences.edit().putString("user_id", user_id).commit();
        this.user_id = user_id;
    }


    public void remove() {
        sharedPreferences.edit().clear().commit();
    }

}
