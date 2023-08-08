package com.ff_queen.admin.Models;

public class BitNumberModel {
    String digit,total_no_wise_rs;

    public BitNumberModel(String digit, String total_no_wise_rs) {

        this.digit = digit;
        this.total_no_wise_rs = total_no_wise_rs;

    }



    public String getDigit() {
        return digit;
    }

    public String getTotal_no_wise_rs() {
        return total_no_wise_rs;
    }



    public void setDigit(String digit) {
        this.digit = digit;
    }

    public void setTotal_no_wise_rs(String total_no_wise_rs) {
        this.total_no_wise_rs = total_no_wise_rs;
    }
}
