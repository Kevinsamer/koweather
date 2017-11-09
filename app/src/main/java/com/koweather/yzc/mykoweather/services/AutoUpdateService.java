package com.koweather.yzc.mykoweather.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.koweather.yzc.mykoweather.gson.Weather;
import com.koweather.yzc.mykoweather.utils.GsonUtils;
import com.koweather.yzc.mykoweather.utils.HttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by xk on 2017/11/8.
 */

public class AutoUpdateService extends Service {
    private static String weatherId;
    private static String weatherURL = "http://guolin.tech/api/weather?cityid=";
    private static String key = "7a6c0c69b869474da3c3471de2bcf82c";//这条key使用次数1000次/天
    private static String binPicUrl = "http://guolin.tech/api/bing_pic";
    private GetWeatherIdBinder mWeatherIdBinder = new GetWeatherIdBinder();
    public class GetWeatherIdBinder extends Binder{
        public void getWeatherId(String weatherId){
            AutoUpdateService.weatherId = weatherId;
            //Log.d("weatherid in service",weatherId);
            updatePingPic();
            updateWeather();
            AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
            int anHour = 8 * 60 * 60 * 1000; // 这是8小时的毫秒数
            long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
            Intent i = new Intent(AutoUpdateService.this, AutoUpdateService.class);
            PendingIntent pi = PendingIntent.getService(AutoUpdateService.this, 0, i, 0);
            manager.cancel(pi);
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mWeatherIdBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新天气信息
     */
    private void updateWeather(){
        if(weatherId != null){
            HttpUtils.sendOKHttpRequest(weatherURL + weatherId + key, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    if (!TextUtils.isEmpty(responseText)){
                        Weather weather = GsonUtils.handleWeatherResponse(responseText);
                        if(weather != null && weather.status.equals("ok")) {
                            SharedPreferences.Editor editor = PreferenceManager
                                    .getDefaultSharedPreferences(AutoUpdateService.this).edit();
                            editor.putString("weatherid",responseText);
                            editor.apply();
                        }
                    }
                }
            });
        }
    }

    /**
     * 更新每日一图
     */
    private void updatePingPic(){
        HttpUtils.sendOKHttpRequest(binPicUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                if (!TextUtils.isEmpty(responseText)){
                    SharedPreferences.Editor editor = PreferenceManager
                            .getDefaultSharedPreferences(AutoUpdateService.this).edit();
                    editor.putString("pic",responseText);
                    editor.apply();
                }
            }
        });
    }
}
