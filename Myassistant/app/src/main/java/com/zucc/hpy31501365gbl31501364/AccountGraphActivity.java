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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import com.shashank.sony.fancytoastlib.FancyToast;
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

    private double sumOut;
    private double sumIn;
    private double typeBuy;
    private double typeEat;
    private double typeTraffic;
    private double typeYule;
    private double typeGift;
    private double typeOutOther;
    private double typeInOther;
    private double typeLicai;
    private double typeInMoney;
    private RecyclerView recyclerView;
    private RadioGroup moneyType;
    private PieChart mPieChart;
    private ArrayList<PieEntry> entries;
    private ArrayList<PieEntry> entriesIn;
    private SharedPreferences pre;
    private String URL = "http://10.0.2.2:3000/accounts/";
    private List<Account> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_graph);
        recyclerView = (RecyclerView) findViewById(R.id.account_graph_list);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("月账单图表");
        Button tv_back = (Button) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pre = getSharedPreferences("data", MODE_PRIVATE);
        final String userId = pre.getString("username", "");
        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        if (month.length() == 1) {
            month = "0" + month;
        }
        final String yue = month;
        moneyType = (RadioGroup) findViewById(R.id.graph_type);
        moneyType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                sumOut = 0;
                sumIn = 0;
                typeBuy = 0;
                typeEat = 0;
                typeTraffic = 0;
                typeYule = 0;
                typeGift = 0;
                typeOutOther = 0;
                typeInOther = 0;
                typeLicai = 0;
                typeInMoney = 0;
                entries = new ArrayList<PieEntry>();
                entriesIn = new ArrayList<PieEntry>();
                result = null;
                RequestBody requestBody = new FormBody.Builder()
                        .add("userId", userId)
                        .add("year", String.valueOf(year))
                        .add("moneyType", radioButton.getText().toString())
                        .add("month", yue)
                        .build();
                queryFromServer(URL + "searchAccount", requestBody, radioButton.getText().toString());
            }
        });
        RadioButton radioButton = (RadioButton) findViewById(R.id.graph_out);
    }

    private void queryFromServer(String address, RequestBody requestBody, final String moneyType) {
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
                                DecimalFormat df = new DecimalFormat("#.00");
                                for (Account account : result) {
                                    if (account.getMoneyType().equals("支出")) {
                                        sumOut += Double.valueOf(df.format(account.getMoney()));
                                    } else {
                                        sumIn += Double.valueOf(df.format(account.getMoney()));
                                    }
                                    sumAccountTypeMoney(account);
                                }
                                sumOut = Double.valueOf(df.format(sumOut));
                                sumIn = Double.valueOf(df.format(sumIn));
                                initView(moneyType);
                                MyFragment3Adapter adapter = new MyFragment3Adapter(result);
                                recyclerView.setAdapter(adapter);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(AccountGraphActivity.this);
                                recyclerView.setLayoutManager(layoutManager);
                            }
                        });
                    } else if (status.equals("4001")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FancyToast.makeText(AccountGraphActivity.this, "该月还没有任何账单哦！", FancyToast.LENGTH_SHORT, FancyToast.WARNING, true).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sumAccountTypeMoney(Account account) {
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

    private void initOutPieEntry(Double money, String text) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (money != 0) {
            float sum = new Double(money / sumOut).floatValue();
            entries.add(new PieEntry(sum, text));
        }
    }
    private void initInPieEntry(Double money, String text) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (money != 0) {
            float sum = new Double(money / sumIn).floatValue();
            entriesIn.add(new PieEntry(sum, text));
        }
    }

    //初始化View
    private void initView(String moneyType) {

        //饼状图
        mPieChart = (PieChart) findViewById(R.id.mPieChart);
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5, 10, 5, 5);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        //设置中间文件
        mPieChart.setCenterText(generateCenterSpannableText(moneyType));

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

        //数据
        if (moneyType.equals("支出")) {
            initOutPieEntry(typeBuy, "购物");
            initOutPieEntry(typeEat, "餐饮");
            initOutPieEntry(typeTraffic, "交通");
            initOutPieEntry(typeYule, "娱乐");
            initOutPieEntry(typeOutOther, "其他");
            initOutPieEntry(typeGift, "礼物");
            //设置数据
            setData(entries);
        } else {
            initInPieEntry(typeInMoney, "工资");
            initInPieEntry(typeLicai, "理财");
            initInPieEntry(typeInOther, "其他");
            //设置数据
            setData(entriesIn);
        }


        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        // 输入标签样式
        mPieChart.setEntryLabelColor(Color.DKGRAY);
        mPieChart.setEntryLabelTextSize(12f);
    }

    //设置中间文字
    private SpannableString generateCenterSpannableText(String moneyType) {
        SpannableString s;
        if (moneyType.equals("支出")) {
            s = new SpannableString("总支出：\n" + String.valueOf(sumOut) + "元");
        } else {
            s = new SpannableString("总收入：\n" + String.valueOf(sumIn) + "元");
        }

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
        String aType = ((PieEntry) e).getLabel();
        List<Account> rest = new ArrayList<>();
        for (Account item : result) {
            if (aType.equals(item.getAccountType())) {
                rest.add(item);
            }
        }
        MyFragment3Adapter adapter = new MyFragment3Adapter(rest);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AccountGraphActivity.this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onNothingSelected() {
        MyFragment3Adapter adapter = new MyFragment3Adapter(result);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AccountGraphActivity.this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pre = getSharedPreferences("data", MODE_PRIVATE);
        final String userId = pre.getString("username", "");
        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        if (month.length() == 1) {
            month = "0" + month;
        }
        final String yue = month;
        sumOut = 0;
        sumIn = 0;
        typeBuy = 0;
        typeEat = 0;
        typeTraffic = 0;
        typeYule = 0;
        typeGift = 0;
        typeOutOther = 0;
        typeInOther = 0;
        typeLicai = 0;
        typeInMoney = 0;
        entries = new ArrayList<PieEntry>();
        entriesIn = new ArrayList<PieEntry>();
        result = null;
        RequestBody requestBody = new FormBody.Builder()
                .add("userId", userId)
                .add("year", String.valueOf(year))
                .add("moneyType", "支出")
                .add("month", yue)
                .build();
        queryFromServer(URL + "searchAccount", requestBody,"支出");
    }
}
