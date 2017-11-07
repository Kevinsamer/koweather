package com.koweather.yzc.mykoweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xk on 2017/11/6.
 */

public class Now {
    public class Cond{
        //天气状况
        public String code;
        public String txt;
    }
    public Cond cond;
    public String fl;//体感温度，单位摄氏度
    public String hum;//相对湿度
    public String pcpn;//降水量
    public String pres;//大气压强
    /**
     * 温度
     */
    public String tmp;
    public String vis;//能见度，单位公里
    public class Wind{
        public String deg;//风向360角度
        public String dir;//风向
        public String sc;//风力
        @SerializedName("spd")
        public String speed;
    }
    public Wind wind;
}
