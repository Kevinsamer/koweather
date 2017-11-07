package com.koweather.yzc.mykoweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.koweather.yzc.mykoweather.fragment.ChooseAreaFragment;
import com.koweather.yzc.mykoweather.gson.Forecast;
import com.koweather.yzc.mykoweather.gson.Weather;
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
    private ImageView chooseAreaImg;
    private static String weatherURL = "http://guolin.tech/api/weather?cityid=";
    private static String key = "7a6c0c69b869474da3c3471de2bcf82c";//这条key使用次数1000次/天
    private String weatherId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        weatherId = getIntent().getStringExtra("weatherid");
        weatherLayout.setVisibility(View.INVISIBLE);
        getWeatherInfo();

        titleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        String weatherContent = prefs.getString("weather", null);
//        if (weatherContent != null) {
//            //有缓存时直接解析
//            Weather weather = GsonUtils.handleWeatherResponse(weatherContent);
//
//        } else {
//            //无缓存是去服务器查询天气数据
//        }
    }

    private void getWeatherInfo() {
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
                                        getWeatherInfo();
                                    }
                                }).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseContent = response.body().string();
                //Log.d("weatherContent",responseContent);此处已经获取到weather的json数据
                final Weather weather = GsonUtils.handleWeatherResponse(responseContent);
                //Log.d("weather","status="+weather.status+"cityName:"+weather.basic.cityName);


                if (weather != null && weather.status.equals("ok")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setWeatherInfo(weather);
                        }
                    });

                }else {
                    Snackbar.make(weatherLayout, "获取天气数据失败", Snackbar.LENGTH_SHORT).setAction("重新获取",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getWeatherInfo();
                                }
                            }).show();
                }
            }
        });

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

    }
}
