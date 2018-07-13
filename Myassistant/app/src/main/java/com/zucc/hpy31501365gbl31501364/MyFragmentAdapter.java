package com.zucc.hpy31501365gbl31501364;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.Richeng;
import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.RichengResult;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2018/7/12 0012.
 */

public class MyFragmentAdapter extends RecyclerView.Adapter <MyFragmentAdapter.ViewHolder>{
    private List<RichengResult> mRichengList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View eventView;
        TextView eventTitle;
        TextView startTime;
        TextView endTime;
        TextView place;
        TextView eventId;

        public ViewHolder (View view) {
            super(view);
            eventView = view;
            eventTitle = (TextView) view.findViewById(R.id.eventTitle);
            startTime = (TextView) view.findViewById(R.id.startTime);
            endTime = (TextView) view.findViewById(R.id.endTime);
            place = (TextView) view.findViewById(R.id.place);
            eventId = (TextView) view.findViewById(R.id.eventId);
        }
    }

    public MyFragmentAdapter(List<RichengResult> richengList) {
        mRichengList = richengList;
        Log.d("Adapter", "mRichengList:" + mRichengList.toString());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment1_richenglist_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.eventView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                RichengResult richeng = mRichengList.get(position);
                Toast.makeText(v.getContext(), "position: " + position + "  and  eventId: " + richeng.getEventId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), EditEventActivity.class);
                intent.putExtra("EvenId",richeng.getEventId());
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        RichengResult richeng = mRichengList.get(position);
        holder.eventTitle.setText(richeng.getEventTitle());
        holder.startTime.setText(richeng.getStartTime());
        holder.endTime.setText(richeng.getEndTime());
        holder.place.setText(richeng.getPlace());
        holder.eventId.setText(richeng.getEventId());
    }

    @Override
    public int getItemCount() {
        return mRichengList.size();
    }
}
