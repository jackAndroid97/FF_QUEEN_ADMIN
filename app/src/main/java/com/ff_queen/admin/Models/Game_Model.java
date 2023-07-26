package com.ff_queen.admin.Models;

import androidx.annotation.NonNull;

public class Game_Model {

    String id, game_name, status, image;

    public Game_Model(String id, String game_name, String status, String image) {
        this.id = id;
        this.game_name = game_name;
        this.status = status;
        this.image = image;
    }

    public Game_Model(String id, String game_name, String image) {
        this.id = id;
        this.game_name = game_name;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getGame_name() {
        return game_name;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }

    @NonNull
    @Override
    public String toString() {
        return game_name;
    }
}
