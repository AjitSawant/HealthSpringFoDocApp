package com.palash.healthspringfoapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.palash.healthspringfoapp.R;
import com.palash.healthspringfoapp.database.DatabaseAdapter;
import com.palash.healthspringfoapp.database.DatabaseContract;
import com.palash.healthspringfoapp.entity.Vital;
import com.palash.healthspringfoapp.entity.VitalsList;
import com.palash.healthspringfoapp.utilities.Constants;
import com.palash.healthspringfoapp.utilities.LocalSetting;

import java.util.ArrayList;

public class VitalsListAdapter extends BaseAdapter {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.VitalAdapter vitalAdapterDB;
    private DatabaseAdapter.VitalsListAdapter vitalListAdapterDB;

    private ArrayList<VitalsList> vitalsListArrayList;
    private ArrayList<Vital> vitalArrayList;

    private LayoutInflater inflater;
    private double minvalue = 0;
    private double maxvalue = 0;
    private double value = 0;

    public VitalsListAdapter(Context context, ArrayList<VitalsList> vitalsListArrayList) {
        this.context = context;
        this.vitalsListArrayList = vitalsListArrayList;

        localSetting = new LocalSetting();
        databaseContract = new DatabaseContract(context);
        databaseAdapter = new DatabaseAdapter(databaseContract);
        vitalAdapterDB = databaseAdapter.new VitalAdapter();
        vitalListAdapterDB = databaseAdapter.new VitalsListAdapter();

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return vitalsListArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return vitalsListArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        try {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.row_vitals_list, null);
                holder.row_vitals_list_tv_vitals_name = (TextView) convertView.findViewById(R.id.row_vitals_list_tv_vitals_name);
                holder.row_vitals_list_tv_date = (TextView) convertView.findViewById(R.id.row_vitals_list_tv_date);
                holder.row_vitals_list_tv_value = (TextView) convertView.findViewById(R.id.row_vitals_list_tv_value);
                holder.row_vitals_list_tv_units = (TextView) convertView.findViewById(R.id.row_vitals_list_tv_units);
                holder.row_vitals_list_tv_range = (TextView) convertView.findViewById(R.id.row_vitals_list_tv_range);
                holder.is_record_sync_tv = (TextView) convertView.findViewById(R.id.is_record_sync_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            VitalsList vitalsList = vitalsListArrayList.get(position);

            holder.row_vitals_list_tv_vitals_name.setText(vitalsList.getVitalsDecription());

            holder.row_vitals_list_tv_units.setText(vitalsList.getUnit());

            holder.row_vitals_list_tv_value.setText(vitalsList.getValue());

            if (vitalsList.getIsSync() != null && vitalsList.getIsSync().equals("1")) {
                holder.is_record_sync_tv.setVisibility(View.VISIBLE);
            }else {
                holder.is_record_sync_tv.setVisibility(View.GONE);
            }

            vitalArrayList = vitalAdapterDB.listAll(vitalsList.getVitalsDecription());
            if (vitalArrayList != null && vitalArrayList.size() > 0) {
                holder.row_vitals_list_tv_range.setText(vitalArrayList.get(0).getMinValue() + "-" + vitalArrayList.get(0).getMaxValue());
                try {
                    value = Double.parseDouble(vitalsListArrayList.get(position).getValue());
                    minvalue = Double.parseDouble(vitalArrayList.get(0).getMinValue());
                    maxvalue = Double.parseDouble(vitalArrayList.get(0).getMaxValue());
                    if (value > maxvalue || value < minvalue) {
                        holder.row_vitals_list_tv_value.setTextColor(Color.RED);
                    } else {
                        holder.row_vitals_list_tv_value.setTextColor(Color.GREEN);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            if (vitalsList.getDate() != null && vitalsList.getDate().length() > 0 && vitalsList.getTime() != null && vitalsList.getTime().length() > 0) {
                holder.row_vitals_list_tv_date.setText(localSetting.formatDate(vitalsList.getDate(), Constants.TIME_FORMAT, Constants.OFFLINE_DATE)
                        + " " + localSetting.formatDate(vitalsList.getTime(), Constants.TIME_FORMAT, Constants.OFFLINE_TIME));
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* if (localSetting.fragment_name.equals("PatientQueue")) {
                        if (vitalsListArrayList.get(position).getID() != null && vitalsListArrayList.get(position).getID().length() > 0) {
                            vitalListAdapterDB.updateCurrentNotes(vitalsListArrayList.get(position).getID());
                        } else {
                            vitalListAdapterDB.updateUnSyncCurrentNotes(vitalsListArrayList.get(position).get_ID());
                        }
                        context.startActivity(new Intent(context, VitalsListAddUpdateActivity.class).putExtra("isUpdate", "Yes"));
                    }*/
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class ViewHolder {
        TextView row_vitals_list_tv_vitals_name;
        TextView row_vitals_list_tv_date;
        TextView row_vitals_list_tv_value;
        TextView row_vitals_list_tv_units;
        TextView row_vitals_list_tv_range;
        TextView is_record_sync_tv;
    }
}

