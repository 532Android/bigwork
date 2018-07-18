package com.zucc.hpy31501365gbl31501364;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;


/**
 * Created by L-Jere on 2018/7/16.
 */

public class MyReceiver extends BroadcastReceiver {
    private MediaPlayer mp;
    @Override
    public void onReceive(final Context context, Intent intent)
    {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
//        dialogBuilder.setTitle("提示");
//        dialogBuilder.setMessage("这是在BroadcastReceiver弹出的对话框。");
//        dialogBuilder.setCancelable(false);
//        dialogBuilder.setPositiveButton("确定", null);
//        AlertDialog alertDialog = dialogBuilder.create();
//        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        alertDialog.show();

        String msg = intent.getStringExtra("msg");
        msg = "您设定的日程" + msg + "到提醒时间了";
        mp=MediaPlayer.create(context, R.raw.music1);
        Toast toast = Toast.makeText(context,msg,Toast.LENGTH_LONG);
        View view = toast.getView();
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewDetachedFromWindow(View v) {
                try {
                    mp.prepare();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.start();
            }
            @Override
            public void onViewAttachedToWindow(View v) {
                mp.pause();
            }
        });
        toast.show();
    }
}
