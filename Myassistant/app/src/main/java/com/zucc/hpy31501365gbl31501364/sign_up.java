package com.zucc.hpy31501365gbl31501364;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zucc.hpy31501365gbl31501364.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

/**
 * Created by L-Jere on 2018/7/10.
 */

public class sign_up extends AppCompatActivity {

    private final String URL = "http://10.0.2.2:3000/users/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        final EditText Name = (EditText)findViewById(R.id.name);
        final EditText Username = (EditText)findViewById(R.id.username);
        final EditText Password1 = (EditText)findViewById(R.id.password1);
        final EditText Password2 = (EditText)findViewById(R.id.password2);
        Button Sign = (Button) findViewById(R.id.sign);
        Sign.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String N = Name.getText().toString();
                String Un = Username.getText().toString();
                String P1 = Password1.getText().toString();
                String P2 = Password2.getText().toString();
                if(N.length()==0){
                    Toast.makeText(getApplicationContext(),"昵称不能为空",Toast.LENGTH_SHORT).show();
                }
                else if(Un.length()==0){
                    Toast.makeText(getApplicationContext(),"帐号不能为空",Toast.LENGTH_SHORT).show();
                }
                else if(P1.length()==0){
                    Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                }
                else if(P2.length()==0){
                    Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                }
                else if(!P1.equals(P2)){
                    Toast.makeText(getApplicationContext(),"两次密码不相等",Toast.LENGTH_SHORT).show();
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
                                                    Toast.makeText(getApplicationContext(),"用户已存在",Toast.LENGTH_SHORT).show();
                                                }
                                                else if(status.equals("1")){
                                                    Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(sign_up.this, logon.class);
                                                    startActivity(intent);
                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(),"注册信息有误",Toast.LENGTH_SHORT).show();
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