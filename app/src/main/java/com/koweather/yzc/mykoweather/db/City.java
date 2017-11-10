package com.koweather.yzc.mykoweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by xk on 2017/11/3.
 */

public class City extends DataSupport {
    private int id;
    private String cityName;
    private int cityCode;
    private int provinceID;//通过ID关联省级表

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(int provinceID) {
        this.provinceID = provinceID;
    }

    public City(int cityID, String cityName, int cityCode, int proviceID) {
        this.id = cityID;
        this.cityName = cityName;
        this.cityCode = cityCode;
        this.provinceID = proviceID;
    }

    public City() {
    }

}
