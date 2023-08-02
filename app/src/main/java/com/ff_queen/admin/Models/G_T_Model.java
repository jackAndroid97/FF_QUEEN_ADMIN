package com.ff_queen.admin.Models;

import androidx.annotation.NonNull;

public class G_T_Model {
    String id,time,count;

    public G_T_Model(String id, String time,String count) {
        this.id = id;
        this.time = time;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @NonNull
    @Override
    public String toString() {
        return time;
    }
}
