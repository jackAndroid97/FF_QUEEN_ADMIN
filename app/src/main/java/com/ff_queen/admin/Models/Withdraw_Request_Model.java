package com.ff_queen.admin.Models;

import com.google.gson.annotations.SerializedName;

public class Withdraw_Request_Model {

    String id;
    String user_id;
    String name;
    @SerializedName("ammount")
    String amount;
    String date;
    String type;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getType() {
        return type;
    }
}
