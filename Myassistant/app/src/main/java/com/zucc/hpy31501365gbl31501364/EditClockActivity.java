package com.zucc.hpy31501365gbl31501364;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.ClockResult;
import com.zucc.hpy31501365gbl31501364.Util.HttpUtil;
import com.zucc.hpy31501365gbl31501364.Util.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditClockActivity extends AppCompatActivity {

    private final String URL = "http://10.0.2.2:3000/richengs/";
    private SharedPreferences pre;
    private EditText fordata;
    private EditText time;
    private TextView eventDate;
    private TextView startTime;
    private TextView endTime;
    private TextView eventTitle;
    private Spinner song;
    private Button listen;
    private MediaPlayer mp;
    private List<String> dataList;
    private ArrayAdapter<String> adapter;
    private int songNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_clock);
        eventDate = (TextView) findViewById(R.id.eventdate);
        startTime = (TextView) findViewById(R.id.starttime);
        endTime = (TextView) findViewById(R.id.endtime);
        eventTitle = (TextView) findViewById(R.id.title);
        listen = (Button)findViewById(R.id.listen);
        song = (Spinner)findViewById(R.id.song);
        dataList = new ArrayList<String>();
        dataList.add("白羊-某某某");
        dataList.add("体面-某某某");
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dataList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        song.setAdapter(adapter);
        song.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                songNum = position;
                switch (position){
                    case 0:
                        mp=MediaPlayer.create(EditClockActivity.this, R.raw.music1);
                        break;
                    case 1:
                        mp=MediaPlayer.create(EditClockActivity.this, R.raw.music2);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("设置闹钟");
        Button tv_back = (Button) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fordata = (EditText) findViewById(R.id.settingdate);
        fordata.setInputType(InputType.TYPE_NULL);
        fordata.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog();
                }
            }
        });
        fordata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        time = (EditText) findViewById(R.id.settingtime);
        time.setInputType(InputType.TYPE_NULL);
        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showTimePickerDialog();
                }
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        Intent intent = getIntent();
        final String eventId = intent.getStringExtra("EvenId");
        final String clockId = intent.getStringExtra("ClockId");
        pre = getSharedPreferences("data", MODE_PRIVATE);
        final String userId = pre.getString("username", "");
        RequestBody requestBody = new FormBody.Builder()
                .add("userId", userId)
                .add("eventId", eventId)
                .add("clockId", clockId)
                .build();
        queryFromServer(URL + "searchClock", requestBody);

        Button tv_edit = (Button) findViewById(R.id.tv_edit);
        tv_edit.setText("提交");
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String choosedSong = "" + songNum;
                String editdate = fordata.getText().toString();
                String nian = editdate.substring(0,4);
                int k = editdate.indexOf("月",5);
                int j = editdate.indexOf("日",k+1);
                String yue = editdate.substring(5,k);
                String ri = editdate.substring(k+1,j);
                if(yue.length()==1){
                    yue="0"+yue;
                }
                if(ri.length()==1){
                    ri="0"+ri;
                }
                String date = nian + "年" + yue + "月" + ri + "日";
                String edittime = time.getText().toString();
                int l = edittime.indexOf("时",0);
                int m = edittime.indexOf("分",l+1);
                String shi = edittime.substring(0,l);
                String feng = edittime.substring(l+1,m);
                if(shi.length()==1){
                    shi="0"+shi;
                }
                if(feng.length()==1){
                    feng="0"+feng;
                }
                String ttime = shi + "时" + feng + "分";
                if(date.compareTo(eventDate.getText().toString())>0){
                    FancyToast.makeText(getApplicationContext(),"提醒日期不能晚于该日程日期",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
                else if(date.compareTo(eventDate.getText().toString())==0&&ttime.compareTo(startTime.getText().toString())>0){
                    FancyToast.makeText(getApplicationContext(),"提醒事件不能晚于该日程开始时间",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
                else{
                    RequestBody requestBody = new FormBody.Builder()
                            .add("userId", userId)
                            .add("eventId", eventId)
                            .add("clockId", clockId)
                            .add("alertDate",date)
                            .add("alertTime",ttime)
                            .add("choosedSong",choosedSong)
                            .build();
                    HttpUtil.postOkHttpRequest(URL + "editClock", requestBody, new okhttp3.Callback(){
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            try{
                                JSONObject jsonObject = new JSONObject(responseData);
                                final String status = jsonObject.getString("status");
                                runOnUiThread(new Runnable(){
                                    @Override
                                    public void run() {
                                        if (status.equals("1")) {
                                            FancyToast.makeText(getApplicationContext(),"修改成功",FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                            finish();
                                        }
                                        else{
                                            FancyToast.makeText(getApplicationContext(),"所修改闹钟有误",FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                                        }
                                    }
                                });
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
        Button delete = (Button) findViewById(R.id.deleteclock);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestBody requestBody = new FormBody.Builder()
                        .add("userId", userId)
                        .add("eventId", eventId)
                        .add("clockId", clockId)
                        .build();
                HttpUtil.postOkHttpRequest(URL + "deleteClock", requestBody, new okhttp3.Callback(){
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        try{
                            JSONObject jsonObject = new JSONObject(responseData);
                            final String status = jsonObject.getString("status");
                            runOnUiThread(new Runnable(){
                                @Override
                                public void run() {
                                    if (status.equals("1")) {
                                        FancyToast.makeText(getApplicationContext(),"删除成功",FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                        finish();
                                    }
                                    else{
                                        FancyToast.makeText(getApplicationContext(),"删除过程有误",FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                                    }
                                }
                            });
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        listen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(listen.getText().toString().trim().equals("试听")){
                    try {
                        mp.prepare();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mp.start();
                    listen.setText("暂停");
                }
                else if(listen.getText().toString().trim().equals("暂停")){
                    mp.pause();
                    listen.setText("试听");
                }
            }
        });
    }

    private void queryFromServer(String address, RequestBody requestBody) {
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
                        final String jsonData = jsonObject.getString("result");
                        Gson gson = new Gson();
                        final ClockResult clock = gson.fromJson(jsonData, ClockResult.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                eventDate.setText(clock.getEventDate());
                                startTime.setText(clock.getStartTime());
                                endTime.setText(clock.getEndTime());
                                eventTitle.setText(clock.getEventTitle());
                                fordata.setText(clock.getAlertDate());
                                time.setText(clock.getAlertTime());
                                song.setSelection(Integer.parseInt(clock.getChoosedSong()));
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    });
}

    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(EditClockActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                fordata.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePickerDialog() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(EditClockActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                time.setText(hour + "时" + minute + "分");
            }
        }, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), true).show();
    }
    protected void onDestroy() {
        if(mp.isPlaying()){
            mp.stop();
        }
        mp.release();
        super.onDestroy();
    }
}
