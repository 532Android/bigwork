package com.zucc.hpy31501365gbl31501364;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class sign_up extends AppCompatActivity {

    private final String URL = "http://10.0.2.2:3000/users/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        TextView tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("注册");
        Button tv_back = (Button)findViewById(R.id.tv_back);
        final EditText Name = (EditText)findViewById(R.id.name);
        final EditText Username = (EditText)findViewById(R.id.username);
        final EditText Password1 = (EditText)findViewById(R.id.password1);
        final EditText Password2 = (EditText)findViewById(R.id.password2);
        Button Sign = (Button) findViewById(R.id.sign);

        tv_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sign_up.this, logon.class);
                startActivity(intent);
            }
        });

        Sign.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String N = Name.getText().toString();
                String Un = Username.getText().toString();
                String P1 = Password1.getText().toString();
                String P2 = Password2.getText().toString();
                if(N.length()==0){
                    FancyToast.makeText(getApplicationContext(),"昵称不能为空", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
                else if(Un.length()==0){
                    FancyToast.makeText(getApplicationContext(),"帐号不能为空",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
                else if(P1.length()==0){
                    FancyToast.makeText(getApplicationContext(),"密码不能为空",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
                else if(P2.length()==0){
                    FancyToast.makeText(getApplicationContext(),"密码不能为空",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
                else if(!P1.equals(P2)){
                    FancyToast.makeText(getApplicationContext(),"两次密码不相等",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
                else{
                    RequestBody requestBody = new FormBody.Builder()
                            .add("userId",Un)
                            .add("userPwd",P1)
                            .add("userName",N)
                            .build();
                    HttpUtil.postOkHttpRequest(URL, requestBody, new okhttp3.Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                }
                                @Override
                                public void onResponse(Call call, Response response) throws IOException{
                                    String responseData = response.body().string();
                                    try{
                                        JSONObject jsonObject = new JSONObject(responseData);
                                        final String status = jsonObject.getString("status");
                                        runOnUiThread(new Runnable(){
                                            @Override
                                            public void run() {
                                                if (status.equals("1000")) {
                                                    FancyToast.makeText(getApplicationContext(),"用户已存在",FancyToast.LENGTH_SHORT, FancyToast.WARNING, true).show();
                                                }
                                                else if(status.equals("1")){
                                                    FancyToast.makeText(getApplicationContext(),"注册成功",FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                                    Intent intent = new Intent(sign_up.this, logon.class);
                                                    startActivity(intent);
                                                }
                                                else{
                                                    FancyToast.makeText(getApplicationContext(),"注册信息有误",FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
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
    }
}