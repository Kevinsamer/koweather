package com.koweather.yzc.mykoweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xk on 2017/11/6.
 */

public class Weather {
    public String status;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    public Basic basic;
    @SerializedName("daily_forecast")
    public List<Forecast> forecasts;
}
