package com.koweather.yzc.mykoweather.utils;


import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * okHttp请求
 * Created by xk on 2017/11/3.
 */

public class HttpUtils {

    public static void sendOKHttpRequest(String url ,okhttp3.Callback callback)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
}
