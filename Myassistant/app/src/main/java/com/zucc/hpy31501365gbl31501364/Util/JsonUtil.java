package com.zucc.hpy31501365gbl31501364.Util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.Account;
import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.ClockResult;
import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.Richeng;
import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.RichengResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/12 0012.
 */

public class JsonUtil {
    // 解析日程接口的数据
    public static List<RichengResult> HandleRichengResponse(String response) {
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            String jsonData = jsonArray.toString();
            Gson gson = new Gson();
            List<RichengResult> result = gson.fromJson(jsonData, new TypeToken<List<RichengResult>>(){}.getType());
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    // 解析查询全部闹钟接口的数据
    public static List<ClockResult> HandleClcokResponse(String response) {
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            String jsonData = jsonArray.toString();
            Gson gson = new Gson();
            List<ClockResult> result = gson.fromJson(jsonData, new TypeToken<List<ClockResult>>(){}.getType());
            return result;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 解析查询账单接口的数据
    public static List<Account> HandleAccountResponse(String response) {
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            String jsonData = jsonArray.toString();
            Gson gson = new Gson();
            List<Account> result = gson.fromJson(jsonData, new TypeToken<List<Account>>(){}.getType());
            return result;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
