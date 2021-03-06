package com.zucc.hpy31501365gbl31501364;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;
import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.ClockResult;
import com.zucc.hpy31501365gbl31501364.Util.HttpUtil;
import com.zucc.hpy31501365gbl31501364.Util.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by L-Jere on 2018/7/11.
 */

public class MyFragment2 extends Fragment {

    private RecyclerView mRecyclerView;
    private SharedPreferences pre;
    private SharedPreferences prec;
    private SharedPreferences.Editor editorc;
    private final String URL = "http://10.0.2.2:3000/richengs/";
    private int clockcount = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment2,container,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.lock_list);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));
        pre = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        prec = getActivity().getSharedPreferences("clock", MODE_PRIVATE);
        String userId = pre.getString("username", "");
        TextView tv_title = (TextView)view.findViewById(R.id.tv_title);
        tv_title.setText("闹钟");
        Button tv_back = (Button)view.findViewById(R.id.tv_back);
        tv_back.setBackgroundColor(Color.parseColor("#00b7c4c4"));
        tv_back.setText("注销");
        tv_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), logon.class);
                startActivity(intent);
            }
        });
        Button tv_edit = (Button)view.findViewById(R.id.tv_edit);
        tv_edit.setText("添加");
        tv_edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectEventActivity.class);
                intent.putExtra("isClock", true);
                startActivity(intent);
            }
        });
        getFromServer(URL + "findAllClock?userId=" + userId);
        return view;
    }

    private void getFromServer(String address) {
        HttpUtil.getOkHttpRequest(address, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Fragment2getFrom", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    final String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        final List<ClockResult> result = JsonUtil.HandleClcokResponse(responseData);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyFragment2Adapter adapter = new MyFragment2Adapter(result);
                                mRecyclerView.setAdapter(adapter);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                mRecyclerView.setLayoutManager(layoutManager);
                            }
                        });
                    } else if (status.equals("3002")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FancyToast.makeText(getActivity(),"你还没有设置闹钟哦",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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

    @Override
    public void onResume() {
        super.onResume();
        pre = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String userId = pre.getString("username", "");
        getFromServer(URL + "findAllClock?userId=" + userId);
    }
}
