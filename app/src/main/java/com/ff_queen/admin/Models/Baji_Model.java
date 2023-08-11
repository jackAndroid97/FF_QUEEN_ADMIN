package com.ff_queen.admin.Models;

import androidx.annotation.NonNull;

public class Baji_Model {
    String name,id,baji;

    public Baji_Model(String name, String id, String baji) {
        this.id = id;
        this.name = name;
        this.baji = baji;

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public String getBaji() {
        return baji;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
