package com.zucc.hpy31501365gbl31501364;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by L-Jere on 2018/7/16.
 */

public class MyReceiver extends BroadcastReceiver {
    private SharedPreferences prec;
    private SharedPreferences.Editor editorc;
    @Override
    public void onReceive(final Context context, Intent intent)
    {
        prec =context.getSharedPreferences("clock", MODE_PRIVATE);
        Intent newIntent = new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
        String msg = intent.getStringExtra("msg");
        msg = "您设定的日程" + msg + "到提醒时间了";
        Toast toast = Toast.makeText(context,msg,Toast.LENGTH_LONG);
        toast.show();
    }
}
