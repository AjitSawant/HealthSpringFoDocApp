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
import com.palash.healthspring.entity.DiagnosisList;
import com.palash.healthspring.fragment.DiagnosisAddUpdateActivity;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;

import java.util.ArrayList;

public class DiagnosisListAdapter extends BaseAdapter {

    private Context context;
    private LocalSetting localSetting;
    private LayoutInflater inflater;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.DiagnosisListAdapter diagnosisListAdapter;

    private ArrayList<DiagnosisList> diagnosisListArrayList;

    public DiagnosisListAdapter(Context context, ArrayList<DiagnosisList> diagnosisListArrayList) {
        this.context = context;
        this.diagnosisListArrayList = diagnosisListArrayList;

        localSetting = new LocalSetting();
        databaseContract = new DatabaseContract(context);
        databaseAdapter = new DatabaseAdapter(databaseContract);
        diagnosisListAdapter = databaseAdapter.new DiagnosisListAdapter();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return diagnosisListArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return diagnosisListArrayList.get(position);
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
                convertView = inflater.inflate(R.layout.row_diagnosis_list, null);
                holder.row_diagnosis_list_tv_diagnosis = (TextView) convertView.findViewById(R.id.row_diagnosis_list_tv_diagnosis);
                holder.row_diagnosis_list_tv_diagnosis_date = (TextView) convertView.findViewById(R.id.row_diagnosis_list_tv_diagnosis_date);
                holder.row_diagnosis_list_tv_diagnosis_code = (TextView) convertView.findViewById(R.id.row_diagnosis_list_tv_diagnosis_code);
                holder.row_diagnosis_list_tv_diagnosis_type = (TextView) convertView.findViewById(R.id.row_diagnosis_list_tv_diagnosis_type);
                holder.row_diagnosis_list_tv_diagnosis_time = (TextView) convertView.findViewById(R.id.row_diagnosis_list_tv_diagnosis_time);
                holder.is_record_sync_tv = (TextView) convertView.findViewById(R.id.is_record_sync_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.row_diagnosis_list_tv_diagnosis.setText(diagnosisListArrayList.get(position).getDiagnosisName());

            if (diagnosisListArrayList.get(position).getDate() != null && diagnosisListArrayList.get(position).getDate().length() > 0) {
                holder.row_diagnosis_list_tv_diagnosis_date.setText(localSetting.formatDate(diagnosisListArrayList.get(position).getDate(), Constants.TIME_FORMAT, Constants.OFFLINE_DATE));
                holder.row_diagnosis_list_tv_diagnosis_time.setText(localSetting.formatDate(diagnosisListArrayList.get(position).getDate(), Constants.TIME_FORMAT, Constants.OFFLINE_TIME));
            }

            holder.row_diagnosis_list_tv_diagnosis_code.setText(diagnosisListArrayList.get(position).getCode());

            holder.row_diagnosis_list_tv_diagnosis_type.setText(diagnosisListArrayList.get(position).getDiagnosisType());

            if (diagnosisListArrayList.get(position).getDiagnosisType() != null && diagnosisListArrayList.get(position).getDiagnosisType().equals("Primary")){
                holder.row_diagnosis_list_tv_diagnosis_type.setTextColor(context.getResources().getColor(R.color.colorGreen));
            }else if (diagnosisListArrayList.get(position).getDiagnosisType() != null && diagnosisListArrayList.get(position).getDiagnosisType().equals("Secondary")){
                holder.row_diagnosis_list_tv_diagnosis_type.setTextColor(context.getResources().getColor(R.color.colorYellow));
            }else if (diagnosisListArrayList.get(position).getDiagnosisType() != null && diagnosisListArrayList.get(position).getDiagnosisType().equals("Provisional")){
                holder.row_diagnosis_list_tv_diagnosis_type.setTextColor(context.getResources().getColor(R.color.colorOrange));
            }

            if (diagnosisListArrayList.get(position).getIsSync() != null && diagnosisListArrayList.get(position).getIsSync().equals("1")) {
                holder.is_record_sync_tv.setVisibility(View.VISIBLE);
            }else {
                holder.is_record_sync_tv.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (localSetting.fragment_name.equals("PatientQueue")) {
                    if (diagnosisListArrayList.get(position).getID() != null && diagnosisListArrayList.get(position).getID().length() > 0) {
                        diagnosisListAdapter.updateCurrentNotes(diagnosisListArrayList.get(position).getID());
                    } else {
                        diagnosisListAdapter.updateUnSyncCurrentNotes(diagnosisListArrayList.get(position).get_ID());
                    }
                    Constants.backFromAddEMR = false;
                    context.startActivity(new Intent(context, DiagnosisAddUpdateActivity.class).putExtra("isUpdate", "Yes"));
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView row_diagnosis_list_tv_diagnosis;
        TextView row_diagnosis_list_tv_diagnosis_date;
        TextView row_diagnosis_list_tv_diagnosis_code;
        TextView row_diagnosis_list_tv_diagnosis_type;
        TextView row_diagnosis_list_tv_diagnosis_time;
        TextView row_diagnosis_list_tv_remark;
        TextView is_record_sync_tv;
    }
}

