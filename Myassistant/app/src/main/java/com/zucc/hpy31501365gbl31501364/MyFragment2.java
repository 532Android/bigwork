package com.zucc.hpy31501365gbl31501364;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.ClockResult;
import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.RichengResult;
import com.zucc.hpy31501365gbl31501364.Util.HttpUtil;
import com.zucc.hpy31501365gbl31501364.Util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.zucc.hpy31501365gbl31501364.MyApplication.getContext;

/**
 * Created by L-Jere on 2018/7/11.
 */

public class MyFragment2 extends Fragment {

    private RecyclerView mRecyclerView;
    private SharedPreferences pre;
    private SharedPreferences prec;
    private SharedPreferences.Editor editorc;
    private final String URL = "http://10.0.2.2:3000/richengs/";

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
                                Toast.makeText(getActivity(), "你还没有设置闹钟哦！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        String clockdate = prec.getString("clockdate","");
        String clocktime = prec.getString("clocktime", "");
        String ClockId = prec.getString("clockid","");
        String clocktitle = prec.getString("clocktitle","");
        Boolean clockon = prec.getBoolean("on", Boolean.parseBoolean(""));
        String ClockId2 = "";
        if(ClockId != null && !"".equals(ClockId)) {
            for (int i = 0; i < ClockId.length(); i++) {
                if (ClockId.charAt(i) >= 48 && ClockId.charAt(i) <= 57) {
                    if(ClockId2.length()<9) {
                        ClockId2 += ClockId.charAt(i);
                    }
                }
            }
        }
        int clockid = Integer.parseInt(ClockId2.trim());
        String nian = clockdate.substring(0,4);
        int k = clockdate.indexOf("月",5);
        int j = clockdate.indexOf("日",k+1);
        String yue = clockdate.substring(5,k);
        String ri = clockdate.substring(k+1,j);
        if(yue.length()==1){
            yue="0"+yue;
        }
        if(ri.length()==1){
            ri="0"+ri;
        }
        int yyue =  Integer.parseInt(yue);
        int rri =  Integer.parseInt(ri);
        String endDate = nian + "-" + yue + "-" + ri;
        int l = clocktime.indexOf("时",0);
        int m = clocktime.indexOf("分",l+1);
        String shi = clocktime.substring(0,l);
        String feng = clocktime.substring(l+1,m);
        if(shi.length()==1){
            shi="0"+shi;
        }
        if(feng.length()==1){
            feng="0"+feng;
        }
        int sshi = Integer.parseInt(shi);
        int ffeng = Integer.parseInt(feng);
        Time t=new Time("GMT+8");
        t.setToNow();
        int year = t.year;
        int month = t.month+1;
        int day = t.monthDay;
        String startDate = year + "-" + month + "-" + day;
        Intent intentc = new Intent(getActivity(), MyReceiver.class);
        intentc.putExtra("msg", clocktitle);
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), clockid, intentc, 0);
        AlarmManager aManager = (AlarmManager) getActivity().getSystemService(Service.ALARM_SERVICE);
        if(clockon) {
            Notification notification = new NotificationCompat.Builder(getActivity())
                    .setContentText("testtest")
                    .build();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            calendar.set(Calendar.HOUR_OF_DAY, sshi);
            calendar.set(Calendar.MINUTE, ffeng);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            if(yyue>month){
                calendar.add(Calendar.DAY_OF_MONTH, getdate(toDate(startDate),toDate(endDate)));
            }
            aManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
            editorc = prec.edit();
            editorc.putBoolean("on",false);
            editorc.commit();
        }
        else{
            aManager.cancel(pi);
        }
    }

    public Date toDate(String str) {
        Date date;
        try {
            java.text.SimpleDateFormat formatter = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            date = formatter.parse(str);
            return date;
        } catch (Exception e) {

        }
        return null;
    }

        public static int getdate(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
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
