package com.ff_queen.admin.Models;

public class User_Model {

    String id,name, email, phone, wallet,status,pass,bank_name,upi,ifsc,account_no;

    public User_Model(String id,String name, String email, String phone,String wallet,String status,String bank_name,String pass,String upi,String ifsc,String account_no) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.wallet = wallet;
        this.status = status;
        this.bank_name = bank_name;
        this.pass = pass;
        this.upi = upi;
        this.ifsc = ifsc;
        this.account_no = account_no;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getWallet() {
        return wallet;
    }

    public String getStatus() {
        return status;
    }

    public String getPass() {
        return pass;
    }

    public String getBank_name() {
        return bank_name;
    }

    public String getUpi() {
        return upi;
    }

    public String getIfsc() {
        return ifsc;
    }

    public String getAccount_no() {
        return account_no;
    }
}
