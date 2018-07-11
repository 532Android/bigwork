package com.zucc.hpy31501365gbl31501364;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.zucc.hpy31501365gbl31501364.Util.HttpUtil;
import com.zucc.hpy31501365gbl31501364.Util.Utill;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;

import static java.lang.Character.getType;

/**
 * Created by L-Jere on 2018/7/10.
 */

public class logon extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private SharedPreferences pre;
    private final String URL = "http://10.0.2.2:3000/users/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logon);
        Button Logon = (Button) findViewById(R.id.logon);
        final Button Sign = (Button) findViewById(R.id.sign);
        final EditText Username = (EditText) findViewById(R.id.username);
        final EditText Password = (EditText) findViewById(R.id.password);
        final CheckBox Remember = (CheckBox) findViewById(R.id.rember);
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        pre = getSharedPreferences("data", MODE_PRIVATE);
        boolean isRemember = pref.getBoolean("remember", false);
        if (isRemember) {
            String U = pre.getString("username", "");
            String P = pre.getString("password", "");
            Username.setText(U);
            Password.setText(P);
            Remember.setChecked(true);
        }
        Logon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String U = Username.getText().toString();
                final String P = Password.getText().toString();
//                SharedPreferences pre = getSharedPreferences("data", MODE_PRIVATE);
                // TODO 发请求到后台
                RequestBody requestBody = new FormBody.Builder()
                        .add("userId", U)
                        .add("userPwd", P)
                        .build();
                HttpUtil.postOkHttpRequest(URL, requestBody, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            final String status = jsonObject.getString("status");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (status.equals("1")) {
                                       // TODO 判断后台数据是否和缓存数据一致
                                        Toast.makeText(logon.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(logon.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("logon", responseData);
                    }
                });
//                if(U.equals(pre.getString("username", ""))&&P.equals(pre.getString("password", ""))){
//                    editor = pref.edit();
//                    if (Remember.isChecked()) {
//                        editor.putBoolean("remember", true);
//                    }
//                    else{
//                        editor.clear();
//                    }
//                    editor.commit();
//                    Intent intent = new Intent(logon.this,MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//                else{
//                    Toast.makeText(getApplicationContext(),"帐号或密码错误",Toast.LENGTH_SHORT).show();
//                }
            }
        });
        Sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(logon.this, sign_up.class);
                startActivity(intent);
            }
        });
    }
}
