package com.ff_queen.admin.Models;

public class GamePlayDetailsModel {
    String game_name,category,baji,digit,amount;

    public GamePlayDetailsModel(String game_name, String category, String baji, String digit, String amount) {
        this.game_name = game_name;
        this.category = category;
        this.baji = baji;
        this.digit = digit;
        this.amount = amount;
    }

    public String getGame_name() {
        return game_name;
    }

    public String getCategory() {
        return category;
    }

    public String getBaji() {
        return baji;
    }

    public String getDigit() {
        return digit;
    }

    public String getAmount() {
        return amount;
    }
}
