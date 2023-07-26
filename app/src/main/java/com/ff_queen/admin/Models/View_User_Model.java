package com.ff_queen.admin.Models;

public class View_User_Model {

    String name;
    String id;
    String email;
    String phone;
    String ref_code;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRef_code() {
        return ref_code;
    }

    @Override
    public String toString() {
        return name;
    }
}
