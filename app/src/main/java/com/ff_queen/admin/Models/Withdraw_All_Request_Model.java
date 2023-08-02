package com.ff_queen.admin.Models;

public class Withdraw_All_Request_Model {

    String id;
    String user_id;
    String name;
    String amount;
    String date;



    public Withdraw_All_Request_Model(String id, String user_id, String name, String amount, String date) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.amount = amount;
        this.date = date;


    }

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

}
