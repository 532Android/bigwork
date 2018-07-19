package com.zucc.hpy31501365gbl31501364;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.joaquimley.faboptions.FabOptions;
import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.Account;
import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.RichengResult;
import com.zucc.hpy31501365gbl31501364.Util.HttpUtil;
import com.zucc.hpy31501365gbl31501364.Util.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by L-Jere on 2018/7/11.
 */

public class MyFragment3 extends Fragment{
    private TextView outMoney;
    private TextView inMoney;
    private TextView restMoney;
    private TextView account_title;
    private RecyclerView recyclerView;
    private SharedPreferences pre;
    private final String URL = "http://10.0.2.2:3000/accounts/";

    @Override
    public void onResume() {
        super.onResume();
        pre = getActivity().getSharedPreferences("data", MODE_PRIVATE);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment3,container,false);
        outMoney = view.findViewById(R.id.outMoney);
        inMoney = view.findViewById(R.id.inMoney);
        restMoney = view.findViewById(R.id.restMoney);
        FabOptions fabOptions = (FabOptions) view.findViewById(R.id.fab_options);
        fabOptions.setButtonsMenu(R.menu.fab_menu);
        fabOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.faboptions_add:
                        Intent intent = new Intent(getActivity(), AddAccountActivity.class);
                        getActivity().startActivity(intent);
                        break;
                    case R.id.faboptions_graph:
                        Intent intentToGraph = new Intent(getActivity(), AccountGraphActivity.class);
                        getActivity().startActivity(intentToGraph);
                        break;
                }
            }
        });
        account_title = (TextView)view.findViewById(R.id.account_title);
        recyclerView = (RecyclerView) view.findViewById(R.id.account_list);
        account_title.setText("本月账单明细");
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                        final List<Account> result = JsonUtil.HandleAccountResponse(responseData);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                double sumOut = 0;
                                double sumIn = 0;
                                DecimalFormat df = new DecimalFormat("#.00");
                                for (Account account : result) {
                                    if (account.getMoneyType().equals("支出")) {
                                        sumOut += Double.valueOf(df.format(account.getMoney()));
                                    } else {
                                        sumIn += Double.valueOf(df.format(account.getMoney()));
                                    }
                                }
                                inMoney.setText(String.valueOf(sumIn));
                                outMoney.setText(String.valueOf(sumOut));
                                restMoney.setText(df.format(sumIn - sumOut));
                                MyFragment3Adapter adapter = new MyFragment3Adapter(result);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setVisibility(View.VISIBLE);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(layoutManager);

                            }
                        });
                    } else if (status.equals("4001")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setVisibility(View.INVISIBLE);
                                Toast.makeText(getActivity(), "该月还没有任何账单哦！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
