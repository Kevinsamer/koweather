package com.koweather.yzc.mykoweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xk on 2017/11/6.
 */

public class Forecast {
    public Astro astro;
    public class Astro{
        //日出日落月出月落时间
        @SerializedName("mr")
        public String moonSet;
        @SerializedName("ms")
        public String moonRise;
        @SerializedName("sr")
        public String sunSet;
        @SerializedName("ss")
        public String sunRise;
    }
    public Cond cond;
    public class Cond{
        //code:天气状况代码,txt:天气状况描述
        @SerializedName("code_d")
        public String codeDay;
        @SerializedName("code_n")
        public String codeNight;
        @SerializedName("txt_d")
        public String txtDay;
        @SerializedName("txt_n")
        public String txtNight;
    }
    public String date;//预报日期
    public String hum;//相对湿度
    public String pcpn;//降水量
    public String pop;//降水概率
    public String pres;//大气压强
    public class Temperature{
        //温度  最高最低
        public String max;
        public String min;
    }
    @SerializedName("tmp")
    public Temperature temperature;

    public String uv;//紫外线指数
    public String vis;//能见度;
    public class Wind{
        public String deg;//风向360角度
        public String dir;//风向
        public String sc;//风力
        public String spd;//风速
    }
    public Wind wind;

}
