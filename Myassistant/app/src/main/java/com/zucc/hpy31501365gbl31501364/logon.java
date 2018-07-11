package com.zucc.hpy31501365gbl31501364;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by L-Jere on 2018/7/10.
 */

public class logon extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences pre;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logon);

        Button Logon = (Button)findViewById(R.id.logon);
        final Button Sign = (Button)findViewById(R.id.sign);
        final EditText Username = (EditText)findViewById(R.id.username);
        final EditText Password = (EditText)findViewById(R.id.password);
        final CheckBox Remember = (CheckBox)findViewById(R.id.rember);
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        pre = getSharedPreferences("data", MODE_PRIVATE);
        boolean isRemember = pref.getBoolean("remember", false);
        if(isRemember){
            String U = pre.getString("username", "");
            String P = pre.getString("password", "");
            Username.setText(U);
            Password.setText(P);
            Remember.setChecked(true);
        }
        Logon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String U = Username.getText().toString();
                String P = Password.getText().toString();
                // TODO 发请求到后台
                if(U.equals(pre.getString("username", ""))&&P.equals(pre.getString("password", ""))){
                    editor = pref.edit();
                    if (Remember.isChecked()) {
                        editor.putBoolean("remember", true);
                    }
                    else{
                        editor.clear();
                    }
                    editor.commit();
                    Intent intent = new Intent(logon.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"帐号或密码错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Sign.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(logon.this,sign_up.class);
                startActivity(intent);
            }
        });
    }
}
