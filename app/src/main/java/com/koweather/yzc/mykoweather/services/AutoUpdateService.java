package com.koweather.yzc.mykoweather.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.koweather.yzc.mykoweather.WeatherActivity;

/**
 * Created by xk on 2017/11/8.
 */

public class AutoUpdateService extends Service {
    private static String weatherId;
    //private GetWeatherId mWeatherId = new GetWeatherId();
    public class GetWeatherId extends Binder{
        public void getWeatherId(String weatherId){
            AutoUpdateService.weatherId = weatherId;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        weatherId = intent.getStringExtra("weatherid");

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("weatherid in service",weatherId);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新天气信息
     */
    private void updateWeather(){
        if(weatherId != null){

        }
    }

    /**
     * 更新每日一图
     */
    private void updatePingPic(){

    }
}
