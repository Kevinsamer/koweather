package com.koweather.yzc.mykoweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by xk on 2017/11/3.
 */

public class Province extends DataSupport {

    private int id;
    private String proviceName;
    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProviceName() {
        return proviceName;
    }

    public void setProviceName(String proviceName) {
        this.proviceName = proviceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public Province(int provinceID, String provice, int provinceCode) {
        this.id = provinceID;
        this.proviceName = provice;
        this.provinceCode = provinceCode;
    }

    public Province() {
    }
}
