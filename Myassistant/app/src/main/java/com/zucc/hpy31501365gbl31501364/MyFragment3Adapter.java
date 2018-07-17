package com.zucc.hpy31501365gbl31501364;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.Account;
import com.zucc.hpy31501365gbl31501364.Util.MapUtil;

import java.util.List;

/**
 * Created by Administrator on 2018/7/17 0017.
 */

public class MyFragment3Adapter  extends RecyclerView.Adapter <MyFragment3Adapter.ViewHolder> {

    private List<Account> mAccountList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View accountView;
        TextView accountTitle;
        ImageView img;
        TextView year;
        TextView month;
        TextView day;
        TextView money;

        public ViewHolder (View view) {
            super(view);
            accountView = view;
            img = (ImageView) view.findViewById(R.id.account_item_img) ;
            accountTitle = (TextView) view.findViewById(R.id.account_item_content);
            year = (TextView) view.findViewById(R.id.item_year);
            month = (TextView) view.findViewById(R.id.item_month);
            day = (TextView) view.findViewById(R.id.item_day);
            money = (TextView) view.findViewById(R.id.account_item_money);
        }
    }

    public MyFragment3Adapter(List<Account> accountList) {
        mAccountList = accountList;
    }

    @Override
    public MyFragment3Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment3_account_item, parent, false);
        final MyFragment3Adapter.ViewHolder holder = new MyFragment3Adapter.ViewHolder(view);
        holder.accountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Account account = mAccountList.get(position);
                // TODO 点击进入编辑账单页面
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyFragment3Adapter.ViewHolder holder, int position) {

        Account account = mAccountList.get(position);

        holder.img.setImageResource(MapUtil.typeMap.get(account.getAccountType()));
        holder.accountTitle.setText(account.getAccountTitle());
        holder.year.setText(account.getYear());
        holder.month.setText(account.getMonth());
        holder.day.setText(account.getDay());
        if (account.getMoneyType().equals("支出")) {
            holder.money.setText(String.valueOf(-account.getMoney()));
            holder.money.setTextColor(0xFFFF0000);
        } else {
            holder.money.setText("+" + String.valueOf(account.getMoney()));
            holder.money.setTextColor(0xFF008000);
        }
    }

    @Override
    public int getItemCount() {
        return mAccountList.size();
    }
}
