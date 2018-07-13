package com.zucc.hpy31501365gbl31501364;

import android.content.Intent;
import android.content.SharedPreferences;
import android.sax.StartElementListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.RichengResult;
import com.zucc.hpy31501365gbl31501364.Util.HttpUtil;
import com.zucc.hpy31501365gbl31501364.Util.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SelectEventActivity extends AppCompatActivity {
    private final String URL = "http://10.0.2.2:3000/richengs/";
    private SharedPreferences pre;
    private List<String> dataList;
    private ArrayAdapter<String> adapter;
    private RecyclerView mRecyclerView;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_event);
        mRecyclerView = (RecyclerView)findViewById(R.id.richeng_List2);
        TextView tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("日程查询");
        Button tv_back = (Button)findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectEventActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        dataList = new ArrayList<String>();
        dataList.add("日期(降序)");
        dataList.add("日期(升序)");
        dataList.add("重要性");
        dataList.add("类别");
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
        pre = getSharedPreferences("data", MODE_PRIVATE);
        String userId = pre.getString("username", "");
        RequestBody requestBody = new FormBody.Builder()
                .add("userId", userId)
                .add("sort", String.valueOf(1))
                .build();
        queryFromServer(URL + "searchEvent", requestBody);
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
                                SelectEventActivityAdapter adapter = new SelectEventActivityAdapter(result);
                                mRecyclerView.setAdapter(adapter);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(SelectEventActivity.this);
                                mRecyclerView.setLayoutManager(layoutManager);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
