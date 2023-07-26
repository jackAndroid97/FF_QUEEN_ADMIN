package com.ff_queen.admin.Models;

public class Withdraw_All_Request_Model {

    String id;
    String user_id;
    String name;
    String amount;
    String date;
    String type;
    String retailer_id;
    String super_dis_id;
    String distributor_id;


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

    public String getRetailer_id() {
        return retailer_id;
    }

    public String getSuper_dis_id() {
        return super_dis_id;
    }

    public String getDistributor_id() {
        return distributor_id;
    }
}
