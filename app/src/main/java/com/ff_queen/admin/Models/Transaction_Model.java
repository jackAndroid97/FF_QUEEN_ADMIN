package com.ff_queen.admin.Models;

public class Transaction_Model {

    String id, game, ticket, date,ticket_number, amount, status,time,t_id;

    public Transaction_Model(String id, String game, String ticket, String date, String ticket_number, String amount, String status, String time, String t_id) {
        this.id = id;
        this.game = game;
        this.ticket = ticket;
        this.date = date;
        this.ticket_number = ticket_number;
        this.amount = amount;
        this.status = status;
        this.time = time;
        this.t_id = t_id;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getGame() {
        return game;
    }

    public String getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getTicket() {
        return ticket;
    }

    public String getTicket_number() {
        return ticket_number;
    }

    public String getTime() {
        return time;
    }

    public String getT_id() {
        return t_id;
    }
}
