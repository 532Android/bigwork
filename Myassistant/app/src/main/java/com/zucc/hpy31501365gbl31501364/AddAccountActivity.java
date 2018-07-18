package com.zucc.hpy31501365gbl31501364;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.Account;
import com.zucc.hpy31501365gbl31501364.Util.HttpUtil;
import com.zucc.hpy31501365gbl31501364.Util.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddAccountActivity extends AppCompatActivity {

    private final String URL = "http://10.0.2.2:3000/accounts/";
    private SharedPreferences pre;
    private EditText accountTitle;
    private RadioGroup moneyType;
    private Spinner accountType;
    private EditText accountDate;
    private EditText accountMoney;
    private EditText accountBeizhu;
    private List<String> dataListIn;
    private ArrayAdapter<String> adapterIn;
    private List<String> dataListOut;
    private ArrayAdapter<String> adapterOut;
    private List<String> dataListDefault;
    private ArrayAdapter<String> adapterDefault;
    private String aTitle;
    private String mType = "请先选择收入或支出";
    private String aDate;
    private String aMoney;
    private String aType;
    private String aBeizhu;
    private String userId;
    private Boolean flag;
    private String accountId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        Intent intent = getIntent();
        flag = intent.getBooleanExtra("isEdit", false);
        pre = getSharedPreferences("data", MODE_PRIVATE);
        userId = pre.getString("username", "");
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("添加账单");
        Button tv_back = (Button) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button tv_edit = (Button) findViewById(R.id.tv_edit);
        tv_edit.setText("提交");
        Button deleteAccount = (Button) findViewById(R.id.delete_account);
        accountTitle = (EditText) findViewById(R.id.add_account_title);
        accountType = (Spinner) findViewById(R.id.add_acccount_type);
        initSpinner();
        accountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mType.equals("收入")) {
                    aType = adapterIn.getItem(position).toString();
                } else if (mType.equals("支出")) {
                    aType = adapterOut.getItem(position).toString();
                } else {
                    aType = adapterDefault.getItem(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        accountDate = (EditText) findViewById(R.id.add_acccount_date);
        accountDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog();
                }
            }
        });
        accountDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        accountMoney = (EditText) findViewById(R.id.add_acccount_money);
        accountBeizhu = (EditText) findViewById(R.id.add_acccount_beizhu);
        moneyType = (RadioGroup) findViewById(R.id.chooseType);
        moneyType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                mType = radioButton.getText().toString();
                if (mType.equals("收入")) {
                    accountType.setAdapter(adapterIn);
                } else {
                    accountType.setAdapter(adapterOut);
                }
            }
        });
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPostData();
                checkDataAndSend();
            }
        });
        if (flag) {
            tv_title.setText("编辑账单");
            accountId = intent.getStringExtra("accountId");
            deleteAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteFromServer("deleteAccount");
                }
            });
            queryFromServer("searchAccount");
        } else {
            deleteAccount.setVisibility(View.GONE);
        }
    }

    private void initSpinner() {
        dataListDefault = new ArrayList<String>();
        dataListDefault.add("请先选择收入或支出");
        adapterDefault = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataListDefault);
        adapterDefault.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountType.setAdapter(adapterDefault);
        dataListIn = new ArrayList<String>();
        dataListIn.add("工资");
        dataListIn.add("理财");
        dataListIn.add("其他");
        adapterIn = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataListIn);
        adapterIn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataListOut = new ArrayList<String>();
        dataListOut.add("购物");
        dataListOut.add("餐饮");
        dataListOut.add("交通");
        dataListOut.add("娱乐");
        dataListOut.add("礼物");
        dataListOut.add("其他");
        adapterOut = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataListOut);
        adapterOut.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void initPostData() {
        aTitle = accountTitle.getText().toString();
        aDate = accountDate.getText().toString();
        aMoney = accountMoney.getText().toString();
        aBeizhu = accountBeizhu.getText().toString();
    }

    private void checkDataAndSend() {
        if (aTitle.length() == 0) {
            Toast.makeText(getApplicationContext(), "请输入内容！", Toast.LENGTH_SHORT).show();
        } else if (mType.length() == 0) {
            Toast.makeText(getApplicationContext(), "请先选择收入或支出", Toast.LENGTH_SHORT).show();
        } else if (aType.length() == 0) {
            Toast.makeText(getApplicationContext(), "请选择类型", Toast.LENGTH_SHORT).show();
        } else if (aDate.length() == 0) {
            Toast.makeText(getApplicationContext(), "请选择日期", Toast.LENGTH_SHORT).show();
        } else if (aMoney.length() == 0) {
            Toast.makeText(getApplicationContext(), "请输入金额", Toast.LENGTH_SHORT).show();
        } else {
            if (aType.equals("请先选择收入或支出")) {
                Toast.makeText(getApplicationContext(), "请先选择收入或支出", Toast.LENGTH_SHORT).show();
            } else {
                if (flag) {
                    addToServer("editAccount");
                } else {
                    addToServer("addAccount");
                }
            }
        }
    }

    private void queryFromServer (String address) {
        RequestBody requestBody = new FormBody.Builder()
                .add("userId", userId)
                .add("accountId", accountId)
                .build();
        HttpUtil.postOkHttpRequest(URL + address, requestBody, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    final String status = jsonObject.getString("status");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (status.equals("1")) {
                                final List<Account> account = JsonUtil.HandleAccountResponse(responseData);
                                accountTitle.setText(account.get(0).getAccountTitle());
                                if (account.get(0).getMoneyType().equals("收入")) {
                                    RadioButton moneyIn = (RadioButton) moneyType.findViewById(R.id.btn_in);
                                    moneyIn.setChecked(true);
                                } else {
                                    RadioButton moneyOut = (RadioButton) moneyType.findViewById(R.id.btn_out);
                                    moneyOut.setChecked(true);
                                }
                                accountType.setSelection(checkChoosed(mType, account.get(0).getAccountType()));
                                String accDate = account.get(0).getYear() + "年" + account.get(0).getMonth() + "月" + account.get(0).getDay() + "日";
                                accountDate.setText(accDate);
                                accountMoney.setText(String.valueOf(account.get(0).getMoney()));
                                accountBeizhu.setText(account.get(0).getBeizhu());
                            } else {
                                Toast.makeText(getApplicationContext(),"查询数据失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addToServer(String address) {
        String nian = aDate.substring(0, 4);
        int k = aDate.indexOf("月", 5);
        int j = aDate.indexOf("日", k + 1);
        String yue = aDate.substring(5, k);
        String ri = aDate.substring(k + 1, j);
        if (yue.length() == 1) {
            yue = "0" + yue;
        }
        if (ri.length() == 1) {
            ri = "0" + ri;
        }
        RequestBody requestBody = new FormBody.Builder()
                .add("userId", userId)
                .add("accountTitle", aTitle)
                .add("accountType", aType)
                .add("year", nian)
                .add("month", yue)
                .add("day", ri)
                .add("moneyType", mType)
                .add("money", aMoney)
                .add("beizhu", aBeizhu)
                .add("accountId", accountId)
                .build();
        HttpUtil.postOkHttpRequest(URL + address, requestBody, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    final String status = jsonObject.getString("status");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (flag) {
                                if (status.equals("1")) {
                                    Toast.makeText(getApplicationContext(), "编辑成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "编辑账单失败", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if (status.equals("1")) {
                                    Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "添加账单失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void deleteFromServer(String address) {
        RequestBody requestBody = new FormBody.Builder()
                .add("userId", userId)
                .add("accountId", accountId)
                .build();
        HttpUtil.postOkHttpRequest(URL + address, requestBody, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    final String status = jsonObject.getString("status");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                if (status.equals("1")) {
                                    Toast.makeText(getApplicationContext(), "删除账单成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "删除账单失败", Toast.LENGTH_SHORT).show();
                                }
                            }

                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(AddAccountActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                accountDate.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private int checkChoosed(String moneyType, String accountType) {
        int result;
        if (moneyType.equals("收入")) {
            switch (accountType) {
                case "工资":
                    result = 0;
                    break;
                case "理财":
                    result = 1;
                    break;
                default:
                    result = 2;
                    break;
            }
        } else {
            switch (accountType) {
                case "购物":
                    result = 0;
                    break;
                case "餐饮":
                    result = 1;
                    break;
                case "交通":
                    result = 2;
                    break;
                case "娱乐":
                    result = 3;
                    break;
                case "礼物":
                    result = 4;
                    break;
                default:
                    result = 5;
                    break;
            }
        }
        return result;
    }
}
