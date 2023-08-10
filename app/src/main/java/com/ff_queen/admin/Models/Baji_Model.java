package com.ff_queen.admin.Models;

import androidx.annotation.NonNull;

public class Baji_Model {
    String name,id;

    public Baji_Model(String name, String id) {
        this.id = id;
        this.name = name;

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }



    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
