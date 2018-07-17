package com.zucc.hpy31501365gbl31501364.Util;

import com.zucc.hpy31501365gbl31501364.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/17 0017.
 */

public class MapUtil {
    public static Map<String, Integer> typeMap = new HashMap<>();

    static {
        typeMap.put("娱乐", R.drawable.money_in);
        typeMap.put("餐饮", R.drawable.money_out);
    }
    public MapUtil (){

        System.out.println("*************************"+typeMap);
    }


}
