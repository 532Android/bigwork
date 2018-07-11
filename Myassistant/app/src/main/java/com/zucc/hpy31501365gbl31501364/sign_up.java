package com.zucc.hpy31501365gbl31501364;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by L-Jere on 2018/7/10.
 */

public class sign_up extends AppCompatActivity {

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
                    // TODO 注册信息传到后台
                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.putString("name", N);
                    editor.putString("username",Un);
                    editor.putString("password",P1);
                    editor.commit();
                    finish();
                    Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(sign_up.this,logon.class);
                    startActivity(intent);
                }
            }
        });
    }
}
