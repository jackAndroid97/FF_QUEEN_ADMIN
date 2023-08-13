package com.ff_queen.admin.Models;

public class Child_Result {

    String result_digit, single_digit, patti_digit, start_date;

    public Child_Result(String single_digit, String patti_digit, String start_date) {
        this.single_digit = single_digit;
        this.patti_digit = patti_digit;
        this.start_date = start_date;
    }

    public Child_Result(String result_digit) {
        this.result_digit = result_digit;
    }

    public String getResult_digit() {
        return result_digit;
    }

    public String getSingle_digit() {
        return single_digit;
    }

    public String getPatti_digit() {
        return patti_digit;
    }

    public String getStart_date() {
        return start_date;
    }


}
