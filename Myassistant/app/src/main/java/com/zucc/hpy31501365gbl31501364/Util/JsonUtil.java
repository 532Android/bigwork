package com.zucc.hpy31501365gbl31501364.Util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.Richeng;
import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.RichengResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/12 0012.
 */

public class JsonUtil {

    // 解析日程接口的数据
    public static List<RichengResult> HandleRichengResponse(String response) {
        try{
//            Gson gson = new Gson();
//            Richeng richeng = gson.fromJson(response,Richeng.class);
//            Log.d("lookatme", richeng.getStatus());
//            List<RichengResult> result = new ArrayList<>(richeng.getRichengresult());
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
}
