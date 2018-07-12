package com.zucc.hpy31501365gbl31501364.Util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Administrator on 2018/7/10 0010.
 */

public class Utill {
    private static ProgressDialog progressDialog;
    // 显示对话框
    // msg为显示的信息
    public static void showProgressDialog(Context context, String msg){
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(msg);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    // 关闭对话框
    public static void closeProgressDialog(){
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
