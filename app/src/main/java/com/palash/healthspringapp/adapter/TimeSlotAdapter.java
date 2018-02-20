package com.palash.healthspringapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.palash.healthspringapp.R;
import com.palash.healthspringapp.entity.TimeSlot;

import java.util.ArrayList;

public class TimeSlotAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<TimeSlot> timeSlotlist; // Values to be displayed

    public TimeSlotAdapter(Context context, ArrayList<TimeSlot> timeSlotlist) {
        this.context = context;
        this.timeSlotlist = timeSlotlist;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return timeSlotlist.size();
    }

    @Override
    public TimeSlot getItem(int position) {
        return timeSlotlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        try {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.row_time_slot, null);
                holder.row_tvTimeslot_fromslot = (TextView) convertView.findViewById(R.id.row_tvTimeslot_fromslot);
                holder.row_tvTimeslot_toslot = (TextView) convertView.findViewById(R.id.row_tvTimeslot_toslot);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.row_tvTimeslot_fromslot.setText(timeSlotlist.get(position).getFromSlot()+" - "+timeSlotlist.get(position).getToSlot());
            holder.row_tvTimeslot_toslot.setText(timeSlotlist.get(position).getToSlot());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class ViewHolder {
        TextView row_tvTimeslot_fromslot;
        TextView row_tvTimeslot_toslot;
    }
}



