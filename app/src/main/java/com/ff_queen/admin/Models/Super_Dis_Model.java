package com.ff_queen.admin.Models;

public class Super_Dis_Model {

    String id, name;

    public Super_Dis_Model(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
