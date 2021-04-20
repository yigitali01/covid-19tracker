package com.example.covid19tracker.model;

public class USDistrictModel {

    private String province;
    private String confirmed;
    private String death;

    public USDistrictModel(String province, String confirmed, String death) {
        this.province = province;
        this.confirmed = confirmed;
        this.death = death;
    }


    public String getProvince() {
        return province;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public String getDeath() {
        return death;
    }

}
