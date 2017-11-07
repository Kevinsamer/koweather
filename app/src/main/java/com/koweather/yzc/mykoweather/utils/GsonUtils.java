package com.koweather.yzc.mykoweather.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.koweather.yzc.mykoweather.db.City;
import com.koweather.yzc.mykoweather.db.County;
import com.koweather.yzc.mykoweather.db.Province;
import com.koweather.yzc.mykoweather.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Gson处理Json数据
 * Created by xk on 2017/11/3.
 */

public class GsonUtils {
    private static Gson gson = new Gson();

    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray provinces = new JSONArray(response);
                for (int i = 0; i < provinces.length(); i++) {
                    JSONObject jsonObject = provinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProviceName(jsonObject.getString("name"));
                    province.setProvinceCode(jsonObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCityResponse(String response, int proviceID) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    City city = new City();
                    city.setProvinceID(proviceID);
                    city.setCityName(jsonObject.getString("name"));
                    city.setCityCode(jsonObject.getInt("id"));
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCountyResponse(String response, int cityID) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    County county = new County();
                    county.setCityID(cityID);
                    county.setCountyName(jsonObject.getString("name"));
                    county.setWeatherID(jsonObject.getString("weather_id"));
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.get(0).toString();
            Gson gson = new Gson();
            return gson.fromJson(weatherContent,Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
