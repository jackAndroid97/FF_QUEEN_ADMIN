package com.ff_queen.admin.Models;

import java.util.ArrayList;
import java.util.List;

public class Result_Model {

    String date;
    List<Child_Result> single_models = new ArrayList<>();
//    List<Child_Result> patti_models = new ArrayList<>();

    public Result_Model(String date, List<Child_Result> single_models/*, List<Child_Result> patti_models*/) {
        this.date = date;
        this.single_models = single_models;
//        this.patti_models = patti_models;
    }

    public String getDate() {
        return date;
    }

    public List<Child_Result> getSingle_models() {
        return single_models;
    }

    /*public List<Child_Result> getPatti_models() {
        return patti_models;
    }*/

}
