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

import com.shashank.sony.fancytoastlib.FancyToast;
import com.zucc.hpy31501365gbl31501364.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by L-Jere on 2018/7/10.
 */

public class logon extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences pre;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor editors;
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
                                        if(U.equals(pre.getString("username", ""))&&P.equals(pre.getString("password", ""))){
                                            editors = pref.edit();
                                            editor = pre.edit();
                                            if (Remember.isChecked()) {
                                                editors.putBoolean("remember", true);
                                            }
                                            else{
                                                editors.putBoolean("remember", false);
                                            }
                                            editor.commit();
                                            editors.commit();
                                        }
                                        else{
                                            editors = pref.edit();
                                            editor = pre.edit();
                                            if (Remember.isChecked()) {
                                                editors.putBoolean("remember", true);
                                                editor.putString("username",U);
                                                editor.putString("password",P);
                                            }
                                            else{
                                                editors.putBoolean("remember", false);
                                                editor.putString("username",U);
                                                editor.putString("password",P);
                                            }
                                            editor.commit();
                                            editors.commit();
                                        }

                                        FancyToast.makeText(logon.this, "登录成功", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                        Intent intent = new Intent(logon.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        FancyToast.makeText(logon.this, "账号或密码错误", FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                                    }
                                }
                            });
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("logon", responseData);
                    }
                });
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
