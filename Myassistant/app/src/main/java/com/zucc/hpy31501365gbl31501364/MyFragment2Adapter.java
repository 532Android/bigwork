package com.zucc.hpy31501365gbl31501364;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Intent;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.RichengResult;

import java.util.List;


/**
 * Created by Administrator on 2018/7/12 0012.
 */

public class MyFragment2Adapter extends RecyclerView.Adapter <MyFragment2Adapter.ViewHolder>{
    private List<RichengResult> mlockList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View eventView;
        TextView eventdate;
        TextView eventTitle;
        TextView startTime;
        TextView eventId;
        Switch mswitch;
        AlarmManager aManager;

        public ViewHolder (View view) {
            super(view);
            eventView = view;
            eventTitle = (TextView) view.findViewById(R.id.title);
            startTime = (TextView) view.findViewById(R.id.starttime);
            eventdate = (TextView) view.findViewById(R.id.eventdata);
            eventId = (TextView) view.findViewById(R.id.eventid);
            mswitch = (Switch) view.findViewById(R.id.starttime);
//            aManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
        }
    }

    public MyFragment2Adapter(List<RichengResult> lockList) {
        mlockList = lockList;
        Log.d("Adapter", "mRichengList:" + mlockList.toString());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment2_clocklist, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.eventView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                RichengResult lock = mlockList.get(position);
                Intent intent = new Intent(v.getContext(), EditClockActivity.class);
                intent.putExtra("EvenId",lock.getEventId());
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        RichengResult lock = mlockList.get(position);
        holder.eventdate.setText(lock.getEventDate());
        holder.eventTitle.setText(lock.getEventTitle());
        holder.startTime.setText(lock.getStartTime());
        holder.eventId.setText(lock.getEventId());
        holder.mswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    holder.eventdate.setTextColor(Color.parseColor("#99000000"));
                    holder.eventTitle.setTextColor(Color.parseColor("#99000000"));
                    holder.startTime.setTextColor(Color.parseColor("#99000000"));
                }else {
                    holder.eventdate.setTextColor(Color.parseColor("#50000000"));
                    holder.eventTitle.setTextColor(Color.parseColor("#50000000"));
                    holder.startTime.setTextColor(Color.parseColor("#50000000"));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlockList.size();
    }
}
