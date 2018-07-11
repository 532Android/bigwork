package com.zucc.hpy31501365gbl31501364;

import android.os.AsyncTask;

import com.zucc.hpy31501365gbl31501364.Util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/7/10 0010.
 */

public class LogonTask extends AsyncTask<String, Integer, String> {
    private ICallback mCallback;
    public LogonTask(ICallback cb) {
        this.mCallback = cb;
    }

    @Override
    protected void onPostExecute(String s) {

        super.onPostExecute(s);

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    @Override
    protected String doInBackground(String... urls) {

        return null;
    }

    @Override
    protected  void onPreExecute () {

    }

    private String getContent (String address, RequestBody requestBody) {
//        static String result = "";
        HttpUtil.postOkHttpRequest(address, requestBody, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
//                result = responseData;
            }
        });
        return "";
    }
}
