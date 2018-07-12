package com.zucc.hpy31501365gbl31501364;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEventActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Button add = (Button)findViewById(R.id.add);
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
        RatingBar ratingBar = (RatingBar) findViewById(R.id.rating);
        Spinner spinner = (Spinner)findViewById(R.id.type);
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

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    rating1 = rating;
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

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String Title = title.getText().toString();
                String Type = type.toString();
                String Rating = rating1.toString();
                String ForData = fordata.getText().toString().trim();
                String Time1 = time1.getText().toString().trim();
                String Time2 = time2.getText().toString().trim();
                String Where = where.getText().toString();
                String Remark = remark.getText().toString();
                String Talk = talk.getText().toString();
                if(Title.length()==0){
                    Toast.makeText(getApplicationContext(),"标题不能为空",Toast.LENGTH_SHORT).show();
                }
                else if(ForData.length()==0){
                    Toast.makeText(getApplicationContext(),"请选择日期",Toast.LENGTH_SHORT).show();
                }
                else if(Time1.length()==0){
                    Toast.makeText(getApplicationContext(),"请选择开始时间",Toast.LENGTH_SHORT).show();
                }
                else if(Time2.length()==0){
                    Toast.makeText(getApplicationContext(),"请选择结束时间",Toast.LENGTH_SHORT).show();
                }
                else if(Time1.compareTo(Time2)>=0){
                    Toast.makeText(getApplicationContext(),"开始时间不能大于等于结束时间",Toast.LENGTH_SHORT).show();
                }
                else{


                    Toast.makeText(getApplicationContext(),"添加成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddEventActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void showDatePickerDialog(){
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                fordata.setText(year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日");
            }
        },c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePickerDialog1(){
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                time1.setText(hour+"时"+minute+"分");
            }
        },c.get(Calendar.HOUR), c.get(Calendar.MINUTE),true).show();
    }

    private void showTimePickerDialog2(){
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                time2.setText(hour+"时"+minute+"分");
            }
        },c.get(Calendar.HOUR), c.get(Calendar.MINUTE),true).show();
    }

}
