package com.zucc.hpy31501365gbl31501364;

import android.app.AlarmManager;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.ClockResult;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Administrator on 2018/7/12 0012.
 */

public class MyFragment2Adapter extends RecyclerView.Adapter <MyFragment2Adapter.ViewHolder>{
    private List<ClockResult> mClockList;
    private SharedPreferences prec;
    private SharedPreferences.Editor editorc;
    private String starttime;
    private String clocktitle;
    private String startdate;
    private String cid;
    static class ViewHolder extends RecyclerView.ViewHolder {
        View eventView;
        TextView eventdate;
        TextView eventTitle;
        TextView startTime;
        TextView eventId;
        Switch mswitch;
        TextView clockid;

        public ViewHolder (View view) {
            super(view);
            eventView = view;
            eventTitle = (TextView) view.findViewById(R.id.title);
            startTime = (TextView) view.findViewById(R.id.start);
            eventdate = (TextView) view.findViewById(R.id.eventdata);
            eventId = (TextView) view.findViewById(R.id.eventid);
            mswitch = (Switch) view.findViewById(R.id.starttime);
            clockid = (TextView)view.findViewById(R.id.clockid);
        }
    }

    public MyFragment2Adapter(List<ClockResult> clockList) {
        mClockList = clockList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment2_clocklist, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.eventView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ClockResult clock = mClockList.get(position);
                Intent intent = new Intent(v.getContext(), EditClockActivity.class);
                intent.putExtra("EvenId",clock.getEventId());
                intent.putExtra("ClockId", clock.getClockId());
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        ClockResult clock = mClockList.get(position);
        holder.eventdate.setText(clock.getAlertDate());
        holder.eventTitle.setText(clock.getEventTitle());
        holder.startTime.setText(clock.getAlertTime());
        holder.eventId.setText(clock.getEventId());
        holder.clockid.setText(clock.getClockId());
        holder.mswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                starttime = holder.startTime.getText().toString();
                clocktitle = holder.eventTitle.getText().toString();
                startdate = holder.eventdate.getText().toString();
                cid = holder.clockid.getText().toString();
                prec = MyApplication.getContext().getSharedPreferences("clock", MODE_PRIVATE);
                if (isChecked){
                    holder.eventdate.setTextColor(Color.parseColor("#99000000"));
                    holder.eventTitle.setTextColor(Color.parseColor("#99000000"));
                    holder.startTime.setTextColor(Color.parseColor("#99000000"));
                    editorc = prec.edit();
                    editorc.putString("clocktime",starttime);
                    editorc.putString("clocktitle",clocktitle);
                    editorc.putString("clockdate",startdate);
                    editorc.putString("clockid",cid);
                    editorc.putBoolean("on",true);
                    editorc.commit();
                }else {
                    holder.eventdate.setTextColor(Color.parseColor("#50000000"));
                    holder.eventTitle.setTextColor(Color.parseColor("#50000000"));
                    holder.startTime.setTextColor(Color.parseColor("#50000000"));
                    editorc = prec.edit();
                    editorc.putString("clocktime",starttime);
                    editorc.putString("clocktitle",clocktitle);
                    editorc.putString("clockdate",startdate);
                    editorc.putString("clockid",cid);
                    editorc.putBoolean("on",false);
                    editorc.commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mClockList.size();
    }
}
