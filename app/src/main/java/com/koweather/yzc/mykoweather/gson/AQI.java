package com.koweather.yzc.mykoweather.gson;

/**
 * Created by xk on 2017/11/6.
 */

public class AQI {
    public AQICity city;
    public class AQICity{
        public String aqi;//空气污染指数
        public String co;//一氧化碳
        public String no2;//二氧化氮
        public String o3;//臭氧
        public String pm10;
        public String pm25;
        public String qlty;//空气质量，取值范围:优，良，轻度污染，中度污染，重度污染，严重污染
        public String so2;//二氧化硫
    }
}
