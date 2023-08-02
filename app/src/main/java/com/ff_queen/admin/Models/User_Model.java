package com.ff_queen.admin.Models;

public class User_Model {

    String id,name, email, phone, wallet,status;

    public User_Model(String id,String name, String email, String phone,String wallet,String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.wallet = wallet;
        this.status = status;
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
}
