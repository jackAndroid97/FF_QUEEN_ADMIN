package com.ff_queen.admin.Models;

public class Money_Request_Model {

    String id;
    String user_id;
    String name;
    String amount;
    String date;
   // String type;
    String bank_name,account_no,ifsc_code,mobile,wallet;

    public Money_Request_Model(String id, String user_id, String name, String amount, String date, String bank_name, String account_no, String ifsc_code, String mobile, String wallet) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.bank_name = bank_name;
        this.account_no = account_no;
        this.ifsc_code = ifsc_code;
        this.mobile = mobile;
        this.wallet = wallet;
    }

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
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

    public String getBank_name() {
        return bank_name;
    }

    public String getAccount_no() {
        return account_no;
    }

    public String getIfsc_code() {
        return ifsc_code;
    }

    public String getMobile() {
        return mobile;
    }

    public String getWallet() {
        return wallet;
    }
}
