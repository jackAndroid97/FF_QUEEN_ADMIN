package com.ff_queen.admin.Models;

import android.content.Context;
import android.content.SharedPreferences;

public class Admin {
    private Context context;
    public String id;
    public String name;
    public String email;
    public String password;
    public String is_super;
    public Object remember_token;
    public String created_at;
    public String updated_at;

    SharedPreferences sharedPreferences;


    public Admin(Context context) {

        this.context = context;

        sharedPreferences = context.getSharedPreferences("login_details", Context.MODE_PRIVATE);

    }

    public String getId() {
        id = sharedPreferences.getString("id", "");
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIs_super() {
        return is_super;
    }

    public void setIs_super(String is_super) {
        this.is_super = is_super;
    }

    public Object getRemember_token() {
        return remember_token;
    }

    public void setRemember_token(Object remember_token) {
        this.remember_token = remember_token;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
