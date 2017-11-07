package com.koweather.yzc.mykoweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xk on 2017/11/6.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;
    @SerializedName("cnty")
    public String countryName;
    @SerializedName("id")
    public String weatherId;

    public String lon;//纬度
    public String lat;//经度

    public Update update;
    public class Update{
        @SerializedName("loc")
        public String updateTime;
        @SerializedName("utc")
        public String utcUpdateTime;//utc格式的更新时间
    }
}
