package com.zucc.hpy31501365gbl31501364;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;
import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.RichengResult;
import com.zucc.hpy31501365gbl31501364.Util.HttpUtil;
import com.zucc.hpy31501365gbl31501364.Util.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by L-Jere on 2018/7/13.
 */

public class EditEventActivity extends AppCompatActivity {

    private final String URL = "http://10.0.2.2:3000/richengs/";
    private SharedPreferences pre;
    private List<String> dataList;
    private ArrayAdapter<String> adapter;
    private EditText fordata;
    private EditText time1;
    private EditText time2;
    private EditText title;
    private EditText where;
    private EditText remark;
    private EditText talk;
    private String type;
    private Float rating1=1f;
    private RatingBar ratingBar;
    private Spinner spinner;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        TextView tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("编辑日程");
        Button tv_back = (Button)findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditEventActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        pre = getSharedPreferences("data", MODE_PRIVATE);
        title = (EditText)findViewById(R.id.title);
        where = (EditText)findViewById(R.id.where);
        remark = (EditText)findViewById(R.id.remark);
        talk = (EditText)findViewById(R.id.talk);
        fordata = (EditText)findViewById(R.id.data);
        time1 = (EditText)findViewById(R.id.time1);
        time2 = (EditText)findViewById(R.id.time2);
        fordata.setInputType(InputType.TYPE_NULL);
        time1.setInputType(InputType.TYPE_NULL);
        time2.setInputType(InputType.TYPE_NULL);
        ratingBar= (RatingBar) findViewById(R.id.rating);
        spinner = (Spinner)findViewById(R.id.type);

        Intent intent = getIntent();
        final String EvenId = intent.getStringExtra("EvenId");
        String userId = pre.getString("username", "");
        RequestBody requestBody = new FormBody.Builder()
                .add("userId", userId)
                .add("eventId", EvenId)
                .build();
        queryFromServer(URL + "searchEvent", requestBody);

        dataList = new ArrayList<String>();
        dataList.add("工作");
        dataList.add("家庭");
        dataList.add("旅行");
        dataList.add("娱乐");
        dataList.add("纪念日");
        dataList.add("其他");
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dataList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type=adapter.getItem(position);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        fordata.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
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

        time1.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showTimePickerDialog1();
                }
            }
        });
        time1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePickerDialog1();
            }
        });

        time2.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showTimePickerDialog2();
                }
            }
        });
        time2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePickerDialog2();
            }
        });

        Button tv_edit = (Button)findViewById(R.id.tv_edit);
        tv_edit.setText("提交");
        tv_edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String Title = title.getText().toString();
                String Type = type.toString();
                String Rating = rating1.toString();
                String ForData = fordata.getText().toString().trim();
                String nian = ForData.substring(0,4);
                int k = ForData.indexOf("月",5);
                int j = ForData.indexOf("日",k+1);
                String yue = ForData.substring(5,k);
                String ri = ForData.substring(k+1,j);
                if(yue.length()==1){
                    yue="0"+yue;
                }
                if(ri.length()==1){
                    ri="0"+ri;
                }
                String date = nian + "年" + yue + "月" + ri + "日";
                String Time1 = time1.getText().toString().trim();
                int l = Time1.indexOf("时",0);
                int m = Time1.indexOf("分",l+1);
                String shi = Time1.substring(0,l);
                String feng = Time1.substring(l+1,m);
                if(shi.length()==1){
                    shi="0"+shi;
                }
                if(feng.length()==1){
                    feng="0"+feng;
                }
                String ttime = shi + "时" + feng + "分";
                String Time2 = time2.getText().toString().trim();
                int l2 = Time2.indexOf("时",0);
                int m2 = Time2.indexOf("分",l2+1);
                String sshi = Time2.substring(0,l2);
                String ffeng = Time2.substring(l2+1,m2);
                if(sshi.length()==1){
                    sshi="0"+sshi;
                }
                if(ffeng.length()==1){
                    ffeng="0"+ffeng;
                }
                String tttime = sshi + "时" + ffeng + "分";
                String Where = where.getText().toString();
                String Remark = remark.getText().toString();
                String Talk = talk.getText().toString();
                if(Title.length()==0){
                    FancyToast.makeText(getApplicationContext(),"标题不能为空",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
                else if(Rating.length()==0){
                    FancyToast.makeText(getApplicationContext(),"请选择重要性",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
                else if(ForData.length()==0){
                    FancyToast.makeText(getApplicationContext(),"请选择日期",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
                else if(Time1.length()==0){
                    FancyToast.makeText(getApplicationContext(),"请选择开始时间",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
                else if(Time2.length()==0){
                    FancyToast.makeText(getApplicationContext(),"请选择结束时间",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
                else if(ttime.compareTo(tttime)>=0){
                    FancyToast.makeText(getApplicationContext(),"开始时间不能大于等于结束时间",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
                else{
                    RequestBody requestBody = new FormBody.Builder()
                            .add("eventId",EvenId)
                            .add("eventTitle",Title)
                            .add("eventDate",date)
                            .add("eventType",Type)
                            .add("startTime",ttime)
                            .add("endTime",tttime)
                            .add("priority",Rating)
                            .add("place",Where)
                            .add("beizhu",Remark)
                            .add("liuyan",Talk)
                            .build();
                    HttpUtil.postOkHttpRequest(URL + "editEvent", requestBody, new okhttp3.Callback(){
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
                                            Intent intent = new Intent(EditEventActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                        else{
                                            FancyToast.makeText(getApplicationContext(),"所修改行程有误",FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
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

        Button delete = (Button)findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                RequestBody requestBody = new FormBody.Builder()
                        .add("eventId",EvenId)
                        .build();
                HttpUtil.postOkHttpRequest(URL + "deleteEvent", requestBody, new okhttp3.Callback(){
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
                                        Intent intent = new Intent(EditEventActivity.this, MainActivity.class);
                                        startActivity(intent);
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

    }
    private void showDatePickerDialog(){
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(EditEventActivity.this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                fordata.setText(year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日");
            }
        },c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePickerDialog1(){
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(EditEventActivity.this, new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                time1.setText(hour+"时"+minute+"分");
            }
        },c.get(Calendar.HOUR), c.get(Calendar.MINUTE),true).show();
    }

    private void showTimePickerDialog2(){
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(EditEventActivity.this, new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                time2.setText(hour+"时"+minute+"分");
            }
        },c.get(Calendar.HOUR), c.get(Calendar.MINUTE),true).show();
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
                            final List<RichengResult> result = JsonUtil.HandleRichengResponse(responseData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    title.setText(result.get(0).getEventTitle());
                                    spinner.setSelection(checkChoosed(result.get(0).getEventType()));
                                    fordata.setText(result.get(0).getEventDate());
                                    time1.setText(result.get(0).getStartTime());
                                    time2.setText(result.get(0).getEndTime());
                                    ratingBar.setRating(Float.parseFloat(result.get(0).getPriority()));
                                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                        @Override
                                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                            rating1 = rating;
                                        }
                                    });
                                    where.setText(result.get(0).getPlace());
                                    remark.setText(result.get(0).getBeizhu());
                                    talk.setText(result.get(0).getLiuyan());
                                }
                            });

                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private int checkChoosed (String choosed) {
        int result;
        switch (choosed) {
            case "工作":
                result = 0;
                break;
            case "家庭":
                result = 1;
                break;
            case "旅行":
                result = 2;
                break;
            case "娱乐":
                result = 3;
                break;
            case "纪念日":
                result = 4;
                break;
            default:
                result = 5;
                break;
        }
        return result;
    }

}
