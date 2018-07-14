package com.zucc.hpy31501365gbl31501364;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class EditClockActivity extends AppCompatActivity {

    private EditText fordata;
    private EditText time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_clock);

        TextView tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("设置闹钟");
        Button tv_back = (Button)findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button tv_edit = (Button)findViewById(R.id.tv_edit);
        tv_edit.setText("提交");
        tv_edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//// TODO: 2018/7/14 edit
            }
        });
        Button delete = (Button)findViewById(R.id.deleteclock);
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//// TODO: 2018/7/14 delete
            }
        });
        fordata = (EditText)findViewById(R.id.settingdate);
        fordata.setInputType(InputType.TYPE_NULL);
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
        time = (EditText)findViewById(R.id.settingtime);
        time.setInputType(InputType.TYPE_NULL);
        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
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
    }
    private void showDatePickerDialog(){
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(EditClockActivity.this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                fordata.setText(year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日");
            }
        },c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void showTimePickerDialog(){
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(EditClockActivity.this, new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                time.setText(hour+"时"+minute+"分");
            }
        },c.get(Calendar.HOUR), c.get(Calendar.MINUTE),true).show();
    }
}
