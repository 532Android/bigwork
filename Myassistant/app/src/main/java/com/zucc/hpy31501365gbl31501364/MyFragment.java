package com.zucc.hpy31501365gbl31501364;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.RichengResult;
import com.zucc.hpy31501365gbl31501364.Util.HttpUtil;
import com.zucc.hpy31501365gbl31501364.Util.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by L-Jere on 2018/7/11.
 */

public class MyFragment extends Fragment {

    private CalendarView mCanlendarView;
    private RecyclerView mRecyclerView;
    private SharedPreferences pre;
    private final String URL = "http://10.0.2.2:3000/richengs/";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment1,container,false);
        mCanlendarView = (CalendarView) view.findViewById(R.id.calendarView);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.richeng_List);
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

        mCanlendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int nian, int yue, int ri) {
                pre = getActivity().getSharedPreferences("data", MODE_PRIVATE);
                String userId = pre.getString("username", "");
                String time = String.valueOf(nian) + "年" + String.valueOf(yue+1) + "月" + String.valueOf(ri) + "日";
                Log.d("Date", time);
                RequestBody requestBody = new FormBody.Builder()
                        .add("userId", userId)
                        .add("eventDate", time)
                        .build();
                queryFromServer(URL + "searchEvent", requestBody);
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
    private void queryFromServer(String address, RequestBody requestBody){
        HttpUtil.postOkHttpRequest(address, requestBody, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    final String status = jsonObject.getString("status");
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
                                mRecyclerView.setVisibility(View.GONE);
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
        pre = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String userId = pre.getString("username", "");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String time = String.valueOf(year) + "年" + String.valueOf(month) + "月" + String.valueOf(day) + "日";
        Log.d("MyFragment", userId);
        RequestBody requestBody = new FormBody.Builder()
                .add("userId", userId)
                .add("eventDate", time)
                .build();
        queryFromServer(URL + "searchEvent", requestBody);
    }
}
