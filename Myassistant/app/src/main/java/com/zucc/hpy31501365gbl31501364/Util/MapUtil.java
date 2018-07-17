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
        typeMap.put("娱乐", R.drawable.yule);
        typeMap.put("餐饮", R.drawable.eat);
        typeMap.put("购物", R.drawable.buy);
        typeMap.put("理财", R.drawable.licai);
        typeMap.put("交通", R.drawable.traffic);
        typeMap.put("其他", R.drawable.other);
        typeMap.put("礼物", R.drawable.gift);
        typeMap.put("工资", R.drawable.money_in);
    }
    public MapUtil (){

        System.out.println("*************************"+typeMap);
    }


}
