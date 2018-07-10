package com.zucc.hpy31501365gbl31501364;

import android.os.AsyncTask;

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
    protected String doInBackground(String... strings) {
        return null;
    }

    @Override
    protected  void onPreExecute () {

    }
}
