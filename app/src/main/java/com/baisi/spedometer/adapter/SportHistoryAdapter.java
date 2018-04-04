package com.baisi.spedometer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baisi.spedometer.R;
import com.baisi.spedometer.step.bean.SportRecordData;
import com.baisi.spedometer.step.bean.SportRecordDataNormal;
import com.baisi.spedometer.step.utils.StepConversion;

import java.util.Date;
import java.util.List;

/**
 * Created by hanwenmao on 2018/1/12.
 */

public class SportHistoryAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<SportRecordDataNormal> sportRecordDataList;
    private  final int TITLE = 0, CONTENT = 1;

    public SportHistoryAdapter(Context context) {
        this.context = context;
    }

    public SportHistoryAdapter(Context context, List<SportRecordDataNormal> sportRecordDataList) {
        this.context = context;
        this.sportRecordDataList = sportRecordDataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater viewInflate = LayoutInflater.from(context);
        if (viewType == 0) {
            View view = viewInflate.inflate(R.layout.item_title_sport_history, null);
            RecyclerView.ViewHolder titleHolder = new TitleHolder(view);
            return titleHolder;
        } else {
            View view = viewInflate.inflate(R.layout.item_cotent_sport_history, null);
            RecyclerView.ViewHolder contentHolder = new ContentHolder(view);
            return contentHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SportRecordDataNormal sportRecordDataNormal = sportRecordDataList.get(position);
        switch (holder.getItemViewType()) {
            case TITLE:
                TitleHolder titleHolder =(TitleHolder) holder;
                Date date = sportRecordDataNormal.getSprotDate();

                titleHolder.date.setText(StepConversion.getSimpleDateYYMM(date));
                titleHolder.milesAll.setText(String.valueOf(sportRecordDataNormal.getMiles()));
                titleHolder.act_times.setText(sportRecordDataNormal.getSport_times()+"");
                break;
            case CONTENT:
                ContentHolder contentHolder =(ContentHolder)holder ;
                contentHolder.date.setText(sportRecordDataNormal.getSimpleDate());
                contentHolder.miles.setText(String.valueOf(sportRecordDataNormal.getMiles()));
                contentHolder.sport_time_long.setText(StepConversion.stringForTime(sportRecordDataNormal.getSportLong()));
                contentHolder.speed.setText(String.valueOf(sportRecordDataNormal.getMilesNeedTime()));
                contentHolder.step.setText(String.valueOf(sportRecordDataNormal.getSteps()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return sportRecordDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        if (sportRecordDataList.get(position).isIstitle()) {
            return TITLE;
        } else {
            return CONTENT;
        }
    }

    class TitleHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView act_times;
        TextView milesAll;

        public TitleHolder(View itemView) {
            super(itemView);
            act_times = itemView.findViewById(R.id.act_times);
            date = itemView.findViewById(R.id.date);
            milesAll = itemView.findViewById(R.id.miles);
        }
    }

    class ContentHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView miles;
        TextView sport_time_long;
        TextView speed;
        TextView step;

        public ContentHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            miles = itemView.findViewById(R.id.miles);
            sport_time_long = itemView.findViewById(R.id.sport_time_long);
            speed = itemView.findViewById(R.id.speed);
            step = itemView.findViewById(R.id.step);
        }
    }
}
