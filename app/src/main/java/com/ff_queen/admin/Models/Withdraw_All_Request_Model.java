package com.ff_queen.admin.Models;

public class Withdraw_All_Request_Model {

    String id;
    String user_id;
    String name;
    String amount;
    String date;
    String bank_name,ac_no,ifsc,mobile,balance;


    public Withdraw_All_Request_Model(String id, String user_id, String name, String amount, String date,
                                      String bank_name,String ac_no,String ifsc,String mobile,String balance) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.bank_name = bank_name;
        this.ac_no = ac_no;
        this.ifsc = ifsc;
        this.mobile = mobile;
        this.balance = balance;


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

    public String getBank_name() {
        return bank_name;
    }

    public String getAc_no() {
        return ac_no;
    }

    public String getIfsc() {
        return ifsc;
    }

    public String getMobile() {
        return mobile;
    }

    public String getBalance() {
        return balance;
    }
}
