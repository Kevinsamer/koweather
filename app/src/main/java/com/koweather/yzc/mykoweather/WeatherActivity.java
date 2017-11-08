package com.koweather.yzc.mykoweather;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.koweather.yzc.mykoweather.fragment.ChooseAreaFragment;
import com.koweather.yzc.mykoweather.gson.Forecast;
import com.koweather.yzc.mykoweather.gson.Weather;
import com.koweather.yzc.mykoweather.services.AutoUpdateService;
import com.koweather.yzc.mykoweather.utils.GsonUtils;
import com.koweather.yzc.mykoweather.utils.HttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class WeatherActivity extends AppCompatActivity {
    private ScrollView weatherLayout;
    private TextView titleText;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText, pm25Text, comfortText, carWashText, sportText;
    private ImageView chooseAreaImg,weatherBg;
    private static String weatherURL = "http://guolin.tech/api/weather?cityid=";
    private static String key = "7a6c0c69b869474da3c3471de2bcf82c";//这条key使用次数1000次/天
    private static String binPicUrl = "http://guolin.tech/api/bing_pic";
    public String weatherId;
    public SwipeRefreshLayout swipeRefreshLayout;
    public DrawerLayout drawerLayout;
    private AutoUpdateService.GetWeatherId getWeatherId;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            getWeatherId = (AutoUpdateService.GetWeatherId) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21){
            View view = getWindow().getDecorView();
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        //初始化控件
        weatherLayout = findViewById(R.id.weather_layout);
        titleText = findViewById(R.id.title_city);
        titleUpdateTime = findViewById(R.id.title_update_time);
        degreeText = findViewById(R.id.now_degree_text);
        weatherInfoText = findViewById(R.id.now_weather_info);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        comfortText = findViewById(R.id.comfort_text);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);
        chooseAreaImg = findViewById(R.id.choose_area_img);
        weatherBg = findViewById(R.id.weather_bg);
        swipeRefreshLayout = findViewById(R.id.refresh_weather);
        swipeRefreshLayout.setColorSchemeResources(new int[]{R.color.colorPrimary,R.color.white,R.color.colorAccent});
        drawerLayout = findViewById(R.id.draw_layout);

        chooseAreaImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        titleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (weatherId != null){
                    getWeatherInfo(weatherId);
                }
            }
        });
        weatherId = getIntent().getStringExtra("weatherid");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherContent = prefs.getString(weatherId, null);
        //Log.d("read weatherid",weatherId);
        if (weatherContent != null) {
            //有缓存时直接解析
            Weather weather = GsonUtils.handleWeatherResponse(weatherContent);
            setWeatherInfo(weather);
        } else {
            //无缓存是去服务器查询天气数据
            weatherLayout.setVisibility(View.INVISIBLE);
            getWeatherInfo(weatherId);
        }
        getBingPic();
//        swipeRefreshLayout.setVisibility(View.INVISIBLE);
//        weatherBg.setVisibility(View.INVISIBLE);
    }

    private void getBingPic(){
        HttpUtils.sendOKHttpRequest(binPicUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String pic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",pic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(pic).into(weatherBg);
                    }
                });
            }
        });
    }

    public void getWeatherInfo(final String weatherId) {
        String url = weatherURL + weatherId + "&key=" + key;
        HttpUtils.sendOKHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //获取失败
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(weatherLayout, "获取天气数据失败", Snackbar.LENGTH_SHORT).setAction("重新获取",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getWeatherInfo(weatherId);
                                    }
                                }).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseContent = response.body().string();
                //Log.d("weatherContent",responseContent);此处已经获取到weather的json数据
                final Weather weather = GsonUtils.handleWeatherResponse(responseContent);
                //Log.d("weather","status="+weather.status+"cityName:"+weather.basic.cityName);


                if (weather != null && weather.status.equals("ok")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences.Editor editor = PreferenceManager
                                    .getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString(weatherId,responseContent);
                            editor.apply();
                            //Log.d("write weatherid",weatherId);
                            setWeatherInfo(weather);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });

                    Intent bindIntent = new Intent(WeatherActivity.this,AutoUpdateService.class);
                    bindService(bindIntent,connection,BIND_AUTO_CREATE);
//                    Intent intent = new Intent(WeatherActivity.this,AutoUpdateService.class);
//                    intent.putExtra("weatherid",weatherId);
//                    startService(intent);
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(weatherLayout, "获取天气数据失败", Snackbar.LENGTH_SHORT).setAction("重新获取",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            getWeatherInfo(weatherId);
                                        }
                                    }).show();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });

                }
            }
        });
        getBingPic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    private void setWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.tmp+"℃";
        String weatherInfo = weather.now.cond.txt;
        titleText.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecasts) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = view.findViewById(R.id.data_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.cond.txtDay);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度：" + weather.suggestion.comf.txt;
        String carWash = "洗车指数：" + weather.suggestion.cw.txt;
        String sport = "运行建议：" + weather.suggestion.sport.txt;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);

    }
}
