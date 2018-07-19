package com.zucc.hpy31501365gbl31501364;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.Account;
import com.zucc.hpy31501365gbl31501364.Util.HttpUtil;
import com.zucc.hpy31501365gbl31501364.Util.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AccountGraphActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    double sumOut = 0;
    double sumIn = 0;
    double typeBuy = 0;
    double typeEat = 0;
    double typeTraffic = 0;
    double typeYule = 0;
    double typeGift = 0;
    double typeOutOther = 0;
    double typeInOther = 0;
    double typeLicai = 0;
    double typeInMoney = 0;
    private RecyclerView recyclerView;
    private PieChart mPieChart;
    private ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
    private SharedPreferences pre;
    private String URL = "http://10.0.2.2:3000/accounts/";
    private List<Account> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_graph);
        recyclerView = (RecyclerView) findViewById(R.id.account_graph_list);
        pre = getSharedPreferences("data", MODE_PRIVATE);
        String userId = pre.getString("username", "");
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        if (month.length() == 1) {
            month = "0" + month;
        }
        RequestBody requestBody = new FormBody.Builder()
                .add("userId", userId)
                .add("year", String.valueOf(year))
                .add("month", month)
                .build();
        queryFromServer(URL + "searchAccount", requestBody);
    }

    private void queryFromServer(String address, RequestBody requestBody) {
        HttpUtil.postOkHttpRequest(address, requestBody, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("AccountqueryFrom", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    final String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        result = JsonUtil.HandleAccountResponse(responseData);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (Account account : result) {
                                    if (account.getMoneyType().equals("支出")) {
                                        sumOut += account.getMoney();
                                    } else {
                                        sumIn += account.getMoney();
                                    }
                                    sumAccountTypeMoney(account);
                                }
                                initView();
                                MyFragment3Adapter adapter = new MyFragment3Adapter(result);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setVisibility(View.VISIBLE);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(AccountGraphActivity.this);
                                recyclerView.setLayoutManager(layoutManager);
                            }
                        });
                    } else if (status.equals("4001")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AccountGraphActivity.this, "该月还没有任何账单哦！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void postFromServer(String address, RequestBody requestBody) {
        HttpUtil.postOkHttpRequest(address, requestBody, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("AccountqueryFrom", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    final String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        final List<Account> result = JsonUtil.HandleAccountResponse(responseData);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyFragment3Adapter adapter = new MyFragment3Adapter(result);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setVisibility(View.VISIBLE);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(AccountGraphActivity.this);
                                recyclerView.setLayoutManager(layoutManager);
                            }
                        });
                    } else if (status.equals("4001")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AccountGraphActivity.this, "该月还没有任何账单哦！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sumAccountTypeMoney (Account account) {
        switch (account.getAccountType()) {
            case "工资":
                typeInMoney += account.getMoney();
                break;
            case "理财":
                typeLicai += account.getMoney();
                break;
            case "购物":
                typeBuy += account.getMoney();
                break;
            case "餐饮":
                typeEat += account.getMoney();
                break;
            case "交通":
                typeTraffic += account.getMoney();
                break;
            case "娱乐":
                typeYule += account.getMoney();
                break;
            case "礼物":
                typeGift += account.getMoney();
                break;
            case "其他":
                if (account.getMoneyType().equals("支出")) {
                    typeOutOther += account.getMoney();
                } else {
                    typeInOther += account.getMoney();
                }
                break;
        }
    }

    private void initPieEntry(Double money, String text) {
        if (money != 0) {
            float sum = new Double(money/sumOut).floatValue();
            entries.add(new PieEntry(sum, text));
        }
    }

    //初始化View
    private void initView() {

        //饼状图
        mPieChart = (PieChart) findViewById(R.id.mPieChart);
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5, 10, 5, 5);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        //设置中间文件
        mPieChart.setCenterText(generateCenterSpannableText());

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);

        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(0);
        // 触摸旋转
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);

        //变化监听
        mPieChart.setOnChartValueSelectedListener(this);

        //模拟数据
        initPieEntry(typeBuy, "购物");
        initPieEntry(typeEat, "餐饮");
        initPieEntry(typeTraffic, "交通");
        initPieEntry(typeYule, "娱乐");
        initPieEntry(typeOutOther, "其他");
        initPieEntry(typeGift, "礼物");
//        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
//        entries.add(new PieEntry(4, "优秀"));
//        entries.add(new PieEntry(2, "满分"));
//        entries.add(new PieEntry(3, "及格"));
//        entries.add(new PieEntry(1, "不及格"));
//        entries.add(new PieEntry(0, "xixi"));

        //设置数据
        setData(entries);

        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
//
//        Legend l = mPieChart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        l.setDrawInside(false);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(0f);
//        l.setYOffset(0f);

        // 输入标签样式
        mPieChart.setEntryLabelColor(Color.DKGRAY);
        mPieChart.setEntryLabelTextSize(12f);
    }

    //设置中间文字
    private SpannableString generateCenterSpannableText() {
        //原文：MPAndroidChart\ndeveloped by Philipp Jahoda
        SpannableString s = new SpannableString("总支出：\n" + String.valueOf(sumOut) + "元");
//        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
//         s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
//         s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
//         s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    //设置数据
    private void setData(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.DKGRAY);
        mPieChart.setData(data);
        mPieChart.highlightValues(null);
        //刷新
        mPieChart.invalidate();
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        String aType = ((PieEntry)e).getLabel();
        List<Account> rest = new ArrayList<>();
        for (Account item : result) {
            if (aType.equals(item.getAccountType())) {
                rest.add(item);
            }
        }
        MyFragment3Adapter adapter = new MyFragment3Adapter(rest);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AccountGraphActivity.this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onNothingSelected() {
        MyFragment3Adapter adapter = new MyFragment3Adapter(result);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AccountGraphActivity.this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        pre = getSharedPreferences("data", MODE_PRIVATE);
//        String userId = pre.getString("username", "");
//        Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
//        if (month.length() == 1) {
//            month = "0" + month;
//        }
//        RequestBody requestBody = new FormBody.Builder()
//                .add("userId", userId)
//                .add("year", String.valueOf(year))
//                .add("month", month)
//                .build();
//        queryFromServer(URL + "searchAccount", requestBody);
    }
}
