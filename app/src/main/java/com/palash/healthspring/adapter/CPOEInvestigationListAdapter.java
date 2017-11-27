package com.palash.healthspring.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.palash.healthspring.R;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.CPOEService;
import com.palash.healthspring.fragment.CPOEInvestigationAddUpdateActivity;
import com.palash.healthspring.utilities.LocalSetting;

import java.util.ArrayList;

/**
 * Created by manishas on 22/7/2016.
 */
public class CPOEInvestigationListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private LocalSetting localSetting;

    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.CPOEServiceAdapter cpoeServiceAdapter;

    private ArrayList<CPOEService> cpoeServiceArrayList;

    public CPOEInvestigationListAdapter(Context context, ArrayList<CPOEService> cpoeServiceArrayList) {
        this.context = context;
        this.cpoeServiceArrayList = cpoeServiceArrayList;

        localSetting = new LocalSetting();
        databaseContract = new DatabaseContract(context);
        databaseAdapter = new DatabaseAdapter(databaseContract);
        cpoeServiceAdapter = databaseAdapter.new CPOEServiceAdapter();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cpoeServiceArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return cpoeServiceArrayList.get(position);
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
                convertView = inflater.inflate(R.layout.row_cpoe_service, null);
                holder.row_cpoeservice_tv_service_name = (TextView) convertView.findViewById(R.id.row_cpoeservice_tv_service_name);
                holder.row_cpoeservice_tv_priority = (TextView) convertView.findViewById(R.id.row_cpoeservice_tv_priority);
                holder.row_cpoeservice_tv_rate = (TextView) convertView.findViewById(R.id.row_cpoeservice_tv_rate);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            CPOEService elCPOEService = cpoeServiceArrayList.get(position);

            if (elCPOEService.getServiceName() != null && elCPOEService.getServiceName().length() > 0) {
                holder.row_cpoeservice_tv_service_name.setText(elCPOEService.getServiceName().trim());
            } else {
                holder.row_cpoeservice_tv_service_name.setText("-");
            }

            if (elCPOEService.getPriorityDescription() != null && elCPOEService.getPriorityDescription().length() > 0) {
                holder.row_cpoeservice_tv_priority.setText(elCPOEService.getPriorityDescription().trim());
            } else {
                holder.row_cpoeservice_tv_priority.setText("-");
            }

            if (elCPOEService.getRate() != null && elCPOEService.getRate().length() > 0) {
                holder.row_cpoeservice_tv_rate.setText("RS. " + elCPOEService.getRate().trim());
            } else {
                holder.row_cpoeservice_tv_rate.setText("-");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (localSetting.fragment_name.equals("PatientQueue")) {
                    if (cpoeServiceArrayList.get(position).getID() != null && cpoeServiceArrayList.get(position).getID().length() > 0) {
                        cpoeServiceAdapter.updateCurrentNotes(cpoeServiceArrayList.get(position).getID());
                    } else {
                        cpoeServiceAdapter.updateUnSyncCurrentNotes(cpoeServiceArrayList.get(position).get_ID());
                    }
                    context.startActivity(new Intent(context, CPOEInvestigationAddUpdateActivity.class).putExtra("isUpdate", "Yes"));
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView row_cpoeservice_tv_service_name;
        TextView row_cpoeservice_tv_priority;
        TextView row_cpoeservice_tv_rate;
    }
}
