package com.koweather.yzc.mykoweather.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.koweather.yzc.mykoweather.R;
import com.koweather.yzc.mykoweather.db.City;
import com.koweather.yzc.mykoweather.db.County;
import com.koweather.yzc.mykoweather.db.Province;
import com.koweather.yzc.mykoweather.utils.GsonUtils;
import com.koweather.yzc.mykoweather.utils.HttpUtils;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by xk on 2017/11/3.
 */

public class ChooseAreaFragment extends Fragment {
    private TextView titleTextView;
    private ListView listView;
    private Button backButton;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();//存放listview的内容
    private int currentLevel;//当前选中的级别
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private List<Province> provinces;//省份表
    private List<City> cities;//城市表
    private List<County> counties;//县级表
    private Province currentProvince;//当前选中的省份
    private City currentCity;//当前选中的城市
    private County currentCounty;//当前选中的县
    private ProgressDialog progressDialog;
    private View view;
    private static String weatherURL = "http://guolin.tech/api/weather?cityid=";
    private static String key = "7a6c0c69b869474da3c3471de2bcf82c";//这条key使用次数1000次/天
    private int ChangeContent = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.choose_area, container, false);
        backButton = view.findViewById(R.id.title_back);
        backButton.setVisibility(View.GONE);
        titleTextView = view.findViewById(R.id.title_text);
        listView = view.findViewById(R.id.listview);
        adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    currentProvince = provinces.get(position);
                    Log.d("province", currentProvince.getProviceName());
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    currentCity = cities.get(position);
                    queryCounties();
                } else if (currentLevel == LEVEL_COUNTY) {
                    //获取天气数据
                    currentCounty = counties.get(position);
                    getWeatherInfo();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY) {
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    queryProvinces();
                }
            }
        });
        queryProvinces();

    }

    /**
     * 获取天气数据
     */
    private void getWeatherInfo() {
        showProgressdialog();
        HttpUtils.sendOKHttpRequest(weatherURL + currentCounty.getWeatherID() + "&key=" + key, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressdialog();
                        Snackbar.make(view,"获取天气数据失败",Snackbar.LENGTH_SHORT).setAction("重试", new View.OnClickListener() {
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
                final String responseText = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressdialog();
                        Toast.makeText(view.getContext(),responseText,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 根据选中的城市查询所有县区级地区
     */
    private void queryCounties() {
        titleTextView.setText(currentCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        counties = DataSupport.where("cityid = ?", String.valueOf(currentCity.getId())).find(County.class);
        if (counties.size() > 0) {
            dataList.clear();
            for (int i = 0; i < counties.size(); i++) {
                dataList.add(counties.get(i).getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            String url = "http://guolin.tech/api/china/" + currentProvince.getProvinceCode() + "/" + currentCity.getCityCode();
            getDataFromInternet(url, "county");
        }
    }

    /**
     * 根据选中的省份查询所有城市,优先查询数据库，没有再从服务器查询
     */
    private void queryCities() {
        titleTextView.setText(currentProvince.getProviceName());
        backButton.setVisibility(View.VISIBLE);
        cities = DataSupport.where("provinceID = ?", String.valueOf(currentProvince.getId())).find(City.class);
        //Log.d("cities.size",cities.get(0).getCityName()+"");
        if (cities.size() > 0) {
            dataList.clear();
            for (int i = 0; i < cities.size(); i++) {
                dataList.add(cities.get(i).getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            String url = "http://guolin.tech/api/china/" + currentProvince.getProvinceCode();
            getDataFromInternet(url, "city");
        }
    }

    /**
     * 查询所有省份,优先查询数据库，没有数据再从服务器查询
     */
    private void queryProvinces() {
        titleTextView.setText("中国");
        backButton.setVisibility(View.GONE);
        provinces = DataSupport.findAll(Province.class);
        if (provinces.size() > 0) {
            //本地有数据，将数据存入dataList
            dataList.clear();
            for (int i = 0; i < provinces.size(); i++) {
                dataList.add(provinces.get(i).getProviceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            String url = "http://guolin.tech/api/china/";
            getDataFromInternet(url, "province");
        }

    }

    /**
     * 从服务器查询省份城市和县城的数据
     *
     * @param url  查询地址
     * @param type 查询类型（省份，城市，县）
     */
    private void getDataFromInternet(final String url, final String type) {
        showProgressdialog();
        HttpUtils.sendOKHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressdialog();
                        Snackbar.make(view, "加载失败", Snackbar.LENGTH_SHORT).setAction("重新加载", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getDataFromInternet(url, type);
                            }
                        }).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if (type.equals("province")) {
                    result = GsonUtils.handleProvinceResponse(responseText);
                } else if (type.equals("city")) {
                    result = GsonUtils.handleCityResponse(responseText, currentProvince.getId());
                } else if (type.equals("county")) {
                    result = GsonUtils.handleCountyResponse(responseText, currentCity.getId());
                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressdialog();
                            if (type.equals("province")) {
                                queryProvinces();
                            } else if (type.equals("city")) {
                                queryCities();
                            } else if (type.equals("county")) {
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });

    }

    /**
     * 显示加载进度对话框
     */
    private void showProgressdialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭加载进度对话框
     */
    private void closeProgressdialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
