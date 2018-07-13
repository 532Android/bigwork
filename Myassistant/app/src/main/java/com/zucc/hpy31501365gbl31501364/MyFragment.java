package com.zucc.hpy31501365gbl31501364;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.haibin.calendarview.CalendarView;
import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.RichengResult;
import com.zucc.hpy31501365gbl31501364.Util.HttpUtil;
import com.zucc.hpy31501365gbl31501364.Util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by L-Jere on 2018/7/11.
 */

public class MyFragment extends Fragment implements
        CalendarView.OnDateSelectedListener,
        CalendarView.OnDateChangeListener {

    private com.haibin.calendarview.CalendarView mCalendarView;
    private TextView mTextMonthDay;

    private TextView mTextYear;

    private TextView mTextLunar;
    private List<com.haibin.calendarview.Calendar> schemes = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private SharedPreferences pre;
    private final String URL = "http://10.0.2.2:3000/richengs/";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);
        mCalendarView = (com.haibin.calendarview.CalendarView) view.findViewById(R.id.calendarView);
        mTextMonthDay = (TextView) view.findViewById(R.id.tv_month_day);
        mTextYear = (TextView) view.findViewById(R.id.tv_year);
        mTextLunar = (TextView) view.findViewById(R.id.tv_lunar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.richeng_List);
        mCalendarView.setOnDateSelectedListener(this);
        pre = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String userId = pre.getString("username", "");
        getFromServer(URL + "findAllEvent?userId=" + userId);

        FloatingActionButton addevents = (FloatingActionButton)view.findViewById(R.id.Addeventbutton);
        TextView tv_title = (TextView)view.findViewById(R.id.tv_title);
        tv_title.setText("日程管理");
        Button tv_back = (Button)view.findViewById(R.id.tv_back);
        tv_back.setBackgroundColor(Color.parseColor("#00b7c4c4"));
        tv_back.setText("注销");
        Button tv_edit = (Button)view.findViewById(R.id.tv_edit);
        tv_edit.setText("查询");

        tv_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), logon.class);
                startActivity(intent);
            }
        });

        tv_edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectEventActivity.class);
                startActivity(intent);
            }
        });
        addevents.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), AddEventActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void getFromServer(String address) {
        HttpUtil.getOkHttpRequest(address, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getActivity(), "请求出问题了！", Toast.LENGTH_SHORT).show();
                Log.e("getFrom", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    final String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        JSONArray rDate = jsonObject.getJSONArray("result");
                        JSONArray count = jsonObject.getJSONArray("count");
                        int color[] = {0xFF40db25,0xFFe69138,0xFFdf1356,0xFFedc56d,0xFFaacc44,0xFFbc13f0,0xFF13acf0};
                        Random rand = new Random();
                        for (int i = 0; i < rDate.length(); i++) {
                            String strDate = rDate.get(i).toString();
                            int nian = Integer.parseInt(strDate.substring(0,4));
                            int k = strDate.indexOf("月",5);
                            int j = strDate.indexOf("日",k+1);
                            int yue = Integer.parseInt(strDate.substring(5,k));
                            int ri = Integer.parseInt(strDate.substring(k+1,j));
                            schemes.add(getSchemeCalendar(nian, yue, ri, color[rand.nextInt(7)], count.get(i).toString()));
                        }
                        mCalendarView.setSchemeDate(schemes);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void queryFromServer(String address, RequestBody requestBody) {
        HttpUtil.postOkHttpRequest(address, requestBody, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getActivity(), "请求出问题了！", Toast.LENGTH_SHORT).show();
                Log.e("queryFrom", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    final String status = jsonObject.getString("status");
                    Log.d("MyFragment", status);
                    if (status.equals("1")) {
                        final List<RichengResult> result = JsonUtil.HandleRichengResponse(responseData);
                        for (RichengResult key : result) {
                            Log.d("MyFragment", key.getEventTitle());
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyFragmentAdapter adapter = new MyFragmentAdapter(result);
                                mRecyclerView.setAdapter(adapter);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                mRecyclerView.setLayoutManager(layoutManager);

                            }
                        });
                    } else if (status.equals("2001")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.setVisibility(View.INVISIBLE);
                                Toast.makeText(getActivity(), "该天还没有日程计划哦！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateChange(com.haibin.calendarview.Calendar calendar) {
        mTextMonthDay.setText(String.valueOf(calendar.getMonth()) + "月" + String.valueOf(calendar.getDay()) + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        pre = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String userId = pre.getString("username", "");
        String yue = String.valueOf(calendar.getMonth());
        String ri = String.valueOf(calendar.getDay());
        if(yue.length()==1){
            yue="0"+yue;
        }
        if(ri.length()==1){
            ri="0"+ri;
        }
        String time = String.valueOf(calendar.getYear()) + "年" + yue + "月" + ri + "日";
        RequestBody requestBody = new FormBody.Builder()
                .add("userId", userId)
                .add("eventDate", time)
                .build();
        queryFromServer(URL + "searchEvent", requestBody);
    }

    @Override
    public void onDateSelected(com.haibin.calendarview.Calendar calendar) {
        onDateChange(calendar);
    }


    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }

    private com.haibin.calendarview.Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        com.haibin.calendarview.Calendar calendar = new com.haibin.calendarview.Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }
}
