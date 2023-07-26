package com.ff_queen.admin.Models;

public class Category {
    public String id;
    public String category;
    public String created_at;
    public String updated_at;

    public Category(String id, String category) {
        this.id = id;
        this.category = category;
    }

    @Override
    public String toString() {
        return category;
    }
}
