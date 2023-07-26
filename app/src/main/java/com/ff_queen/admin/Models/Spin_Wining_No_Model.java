package com.ff_queen.admin.Models;

public class Spin_Wining_No_Model {

    String id , no;

    public Spin_Wining_No_Model(String id, String no) {
        this.id = id;
        this.no = no;
    }

    public String getId() {
        return id;
    }

    public String getNo() {
        return no;
    }

    @Override
    public String toString() {
        return no;
    }
}
