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
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.ClockResult;
import com.zucc.hpy31501365gbl31501364.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * Created by Administrator on 2018/7/12 0012.
 */

public class MyFragment2Adapter extends RecyclerView.Adapter <MyFragment2Adapter.ViewHolder> {
    private List<ClockResult> mClockList;
    private SharedPreferences pre;
    private SharedPreferences prec;
    private SharedPreferences.Editor editorc;
    private String starttime;
    private String clocktitle;
    private String startdate;
    private String cid;
    private String eid;
    private int clockcount = 0;
    private final String URL = "http://10.0.2.2:3000/richengs/";

    static class ViewHolder extends RecyclerView.ViewHolder {
        View eventView;
        TextView eventdate;
        TextView eventTitle;
        TextView startTime;
        TextView eventId;
        Switch mswitch;
        TextView clockid;

        public ViewHolder(View view) {
            super(view);
            eventView = view;
            eventTitle = (TextView) view.findViewById(R.id.title);
            startTime = (TextView) view.findViewById(R.id.start);
            eventdate = (TextView) view.findViewById(R.id.eventdata);
            eventId = (TextView) view.findViewById(R.id.eventid);
            mswitch = (Switch) view.findViewById(R.id.starttime);
            clockid = (TextView) view.findViewById(R.id.clockid);
        }
    }

    public MyFragment2Adapter(List<ClockResult> clockList) {
        mClockList = clockList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment2_clocklist, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.eventView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ClockResult clock = mClockList.get(position);
                Intent intent = new Intent(v.getContext(), EditClockActivity.class);
                intent.putExtra("EvenId", clock.getEventId());
                intent.putExtra("ClockId", clock.getClockId());
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        ClockResult clock = mClockList.get(position);
        holder.eventdate.setText(clock.getAlertDate());
        holder.eventTitle.setText(clock.getEventTitle());
        holder.startTime.setText(clock.getAlertTime());
        holder.eventId.setText(clock.getEventId());
        holder.clockid.setText(clock.getClockId());
        pre = MyApplication.getContext().getSharedPreferences("data", MODE_PRIVATE);
        prec = MyApplication.getContext().getSharedPreferences("clock", MODE_PRIVATE);
        final String userId = pre.getString("username", "");
        holder.mswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                starttime = holder.startTime.getText().toString();
                clocktitle = holder.eventTitle.getText().toString();
                startdate = holder.eventdate.getText().toString();
                cid = holder.clockid.getText().toString();
                eid = holder.eventId.getText().toString();
                String ClockId2 = "";
                if (cid != null && !"".equals(cid)) {
                    for (int i = 0; i < cid.length(); i++) {
                        if (cid.charAt(i) >= 48 && cid.charAt(i) <= 57) {
                            if (ClockId2.length() < 9) {
                                ClockId2 += cid.charAt(i);
                            }
                        }
                    }
                }
                int clockid = Integer.parseInt(ClockId2.trim());
                String nian = startdate.substring(0, 4);
                int k = startdate.indexOf("月", 5);
                int j = startdate.indexOf("日", k + 1);
                String yue = startdate.substring(5, k);
                String ri = startdate.substring(k + 1, j);
                if (yue.length() == 1) {
                    yue = "0" + yue;
                }
                if (ri.length() == 1) {
                    ri = "0" + ri;
                }
                int yyue = Integer.parseInt(yue);
                int rri = Integer.parseInt(ri);
                String endDate = nian + "-" + yue + "-" + ri;
                int l = starttime.indexOf("时", 0);
                int m = starttime.indexOf("分", l + 1);
                String shi = starttime.substring(0, l);
                String feng = starttime.substring(l + 1, m);
                if (shi.length() == 1) {
                    shi = "0" + shi;
                }
                if (feng.length() == 1) {
                    feng = "0" + feng;
                }
                int sshi = Integer.parseInt(shi);
                int ffeng = Integer.parseInt(feng);
                Time t = new Time("GMT+8");
                t.setToNow();
                int year = t.year;
                int month = t.month + 1;
                int day = t.monthDay;
                int hour = t.hour+8;
                int minute = t.minute;
                String startDate = year + "-" + month + "-" + day;
                Intent intentc = new Intent(MainActivity.getMainActivity(), MyReceiver.class);
                intentc.putExtra("msg", clocktitle);
                PendingIntent pi = PendingIntent.getBroadcast(MainActivity.getMainActivity(), clockid, intentc, 0);
                AlarmManager aManager = (AlarmManager) MainActivity.getMainActivity().getSystemService(Service.ALARM_SERVICE);
                NotificationManager notificationManager = (NotificationManager) MainActivity.getMainActivity().getSystemService(NOTIFICATION_SERVICE);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                calendar.set(Calendar.HOUR_OF_DAY, sshi);
                calendar.set(Calendar.MINUTE, ffeng);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                if (isChecked) {
                    holder.eventdate.setTextColor(Color.parseColor("#99000000"));
                    holder.eventTitle.setTextColor(Color.parseColor("#99000000"));
                    holder.startTime.setTextColor(Color.parseColor("#99000000"));
                    editorc = prec.edit();
                    editorc.putString("clocktime", starttime);
                    editorc.putString("clocktitle", clocktitle);
                    editorc.putString("clockdate", startdate);
                    editorc.putString("clockid", cid);
                    editorc.putString("eventid", eid);
                    editorc.putBoolean("on", true);
                    editorc.commit();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("userId", userId)
                            .add("eventId", eid)
                            .add("clockId", cid)
                            .add("isOpen", "true")
                            .build();
                    HttpUtil.postOkHttpRequest(URL + "openClock", requestBody, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(responseData);
                                final String status = jsonObject.getString("status");
                                MainActivity.getMainActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (status.equals("1")) {

                                        } else {
                                            Toast.makeText(MainActivity.getMainActivity(), "闹钟设置失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    if (yyue > month) {
                        calendar.add(Calendar.DAY_OF_MONTH, getdate(toDate(startDate), toDate(endDate)));
                        clockcount = 0;
                    } else if (yyue < month) {
                        editorc = prec.edit();
                        editorc.putBoolean("on", false);
                        editorc.commit();
                        clockcount = 0;
                    } else {
                        if (rri < day) {
                            editorc = prec.edit();
                            editorc.putBoolean("on", false);
                            editorc.commit();
                            clockcount = 0;
                        } else if (rri > day) {
                            calendar.add(Calendar.DAY_OF_MONTH, getdate(toDate(startDate), toDate(endDate)));
                            clockcount = 0;
                        } else if (rri == day) {
                            if (sshi < hour) {
                                editorc = prec.edit();
                                editorc.putBoolean("on", false);
                                editorc.commit();
                                clockcount = 0;
                            } else if(sshi > hour){
                                clockcount = 1;
                            } else if(sshi == hour){
                              if(ffeng <= minute){
                                  editorc = prec.edit();
                                  editorc.putBoolean("on", false);
                                  editorc.commit();
                                  clockcount = 0;
                              }else{
                                  clockcount = 1;
                              }
                            }
                        }
                    }
                    if (clockcount == 1) {
                        clockcount = 0;
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.getMainActivity());
                        mBuilder.setContentTitle(clocktitle)
                                .setContentText("你设定闹钟的日程到时间了")
                                .setTicker("你看不见我看不见我")
                                .setLargeIcon(BitmapFactory.decodeResource(MainActivity.getMainActivity().getResources(), R.mipmap.logo))
                                .setSmallIcon(R.drawable.clock)
                                .setWhen(calendar.getTimeInMillis())
                                .setSound(Uri.parse("android.resource://com.zucc.hpy31501365gbl31501364/" + R.raw.music1))
                                .setPriority(Notification.PRIORITY_MAX)
                                .setOngoing(true);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel("" + clockid, "闹钟", NotificationManager
                                    .IMPORTANCE_DEFAULT);
                            mBuilder.setChannelId("" + clockid);
                            notificationManager.createNotificationChannel(channel);
                        }
                        Notification notification = mBuilder.build();
                        notification.flags = Notification.FLAG_AUTO_CANCEL;
                        aManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
                        mBuilder.setContentIntent(pi);
                        notificationManager.notify(clockid, mBuilder.build());
                    }
                    else {
                        aManager.cancel(pi);
                    }
                } else {
                    holder.eventdate.setTextColor(Color.parseColor("#50000000"));
                    holder.eventTitle.setTextColor(Color.parseColor("#50000000"));
                    holder.startTime.setTextColor(Color.parseColor("#50000000"));
                    editorc = prec.edit();
                    editorc.putString("clocktime", starttime);
                    editorc.putString("clocktitle", clocktitle);
                    editorc.putString("clockdate", startdate);
                    editorc.putString("clockid", cid);
                    editorc.putString("eventid", eid);
                    editorc.putBoolean("on", false);
                    editorc.commit();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("userId", userId)
                            .add("eventId", eid)
                            .add("clockId", cid)
                            .add("isOpen", "false")
                            .build();
                    HttpUtil.postOkHttpRequest(URL + "openClock", requestBody, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(responseData);
                                final String status = jsonObject.getString("status");
                                MainActivity.getMainActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (status.equals("1")) {

                                        } else {
                                            Toast.makeText(MainActivity.getMainActivity(), "闹钟设置失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    aManager.cancel(pi);
                }
            }
        });
        holder.mswitch.setChecked(clock.getIsOpen());
    }

    @Override
    public int getItemCount() {
        return mClockList.size();
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

}

