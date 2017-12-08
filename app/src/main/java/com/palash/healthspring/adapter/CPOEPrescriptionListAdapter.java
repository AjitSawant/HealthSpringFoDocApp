package com.palash.healthspring.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.palash.healthspring.R;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.CPOEPrescription;
import com.palash.healthspring.fragment.CPOEPrescriptionAddUpdateActivity;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;

import java.util.ArrayList;

/**
 * Created by manishas on 22/7/2016.
 */
public class CPOEPrescriptionListAdapter extends BaseAdapter {

    private Context context;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.CPOEMedicineAdapter cpoeMedicineAdapter;

    private LayoutInflater inflater;
    private ArrayList<CPOEPrescription> cpoeMedicineArrayList;
    private LocalSetting localSetting;

    public CPOEPrescriptionListAdapter(Context context, ArrayList<CPOEPrescription> cpoeMedicineArrayList) {
        this.context = context;
        this.cpoeMedicineArrayList = cpoeMedicineArrayList;

        localSetting = new LocalSetting();
        databaseContract = new DatabaseContract(context);
        databaseAdapter = new DatabaseAdapter(databaseContract);
        cpoeMedicineAdapter = databaseAdapter.new CPOEMedicineAdapter();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cpoeMedicineArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return cpoeMedicineArrayList.get(position);
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
                convertView = inflater.inflate(R.layout.row_current_medication, null);

                holder.row_current_medication_tv_drug_name = (EditText) convertView.findViewById(R.id.row_current_medication_tv_drug_name);
                holder.row_current_medication_tv_date = (EditText) convertView.findViewById(R.id.row_current_medication_tv_date);
                holder.row_current_medication_tv_days = (TextView) convertView.findViewById(R.id.row_current_medication_tv_days);
                holder.row_current_medication_tv_dose = (TextView) convertView.findViewById(R.id.row_current_medication_tv_dose);
                holder.row_current_medication_tv_reason = (TextView) convertView.findViewById(R.id.row_current_medication_tv_reason);
                holder.row_current_medication_tv_route = (TextView) convertView.findViewById(R.id.row_current_medication_tv_route);
                holder.row_current_medication_tv_frequency = (TextView) convertView.findViewById(R.id.row_current_medication_tv_frequency);
                holder.row_current_medication_tv_qty = (TextView) convertView.findViewById(R.id.row_current_medication_tv_qty);
                holder.is_record_sync_tv = (TextView) convertView.findViewById(R.id.is_record_sync_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            CPOEPrescription elCPOEPrescription = cpoeMedicineArrayList.get(position);

            if (elCPOEPrescription.getItemName() != null && elCPOEPrescription.getItemName().length() > 0) {
                holder.row_current_medication_tv_drug_name.setText(elCPOEPrescription.getItemName());
            } else {
                holder.row_current_medication_tv_drug_name.setText("-");
            }

            if (elCPOEPrescription.getDays() != null && elCPOEPrescription.getDays().length() > 0) {
                holder.row_current_medication_tv_days.setText(elCPOEPrescription.getDays());
            } else {
                holder.row_current_medication_tv_days.setText("-");
            }

            if (elCPOEPrescription.getDose() != null && elCPOEPrescription.getDose().length() > 0) {
                holder.row_current_medication_tv_dose.setText(elCPOEPrescription.getDose());
            } else {
                holder.row_current_medication_tv_dose.setText("-");
            }

            if (elCPOEPrescription.getReason() != null && elCPOEPrescription.getReason().length() > 0) {
                holder.row_current_medication_tv_reason.setText(elCPOEPrescription.getReason());
            } else {
                holder.row_current_medication_tv_reason.setText("-");
            }

            if (elCPOEPrescription.getRoute() != null && elCPOEPrescription.getRoute().length() > 0 && !elCPOEPrescription.getRoute().equals("--Select--")) {
                holder.row_current_medication_tv_route.setText(elCPOEPrescription.getRoute());
            } else {
                holder.row_current_medication_tv_route.setText("-");
            }

            if (elCPOEPrescription.getFrequency() != null && elCPOEPrescription.getFrequency().length() > 0) {
                holder.row_current_medication_tv_frequency.setText(elCPOEPrescription.getFrequency());
            } else {
                holder.row_current_medication_tv_frequency.setText("-");
            }

            if (elCPOEPrescription.getQuantity() != null && elCPOEPrescription.getQuantity().length() > 0) {
                holder.row_current_medication_tv_qty.setText(elCPOEPrescription.getQuantity());
            } else {
                holder.row_current_medication_tv_qty.setText("-");
            }

            if (elCPOEPrescription.getDate() != null && elCPOEPrescription.getDate().length() > 0) {
                holder.row_current_medication_tv_date.setText(localSetting.formatDate(elCPOEPrescription.getDate(), Constants.TIME_FORMAT, Constants.OFFLINE_DATE)
                        + " " + localSetting.formatDate(elCPOEPrescription.getDate(), Constants.TIME_FORMAT, Constants.OFFLINE_TIME));
            }

            if (elCPOEPrescription.getIsSync() != null && elCPOEPrescription.getIsSync().equals("1")) {
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
                    if (cpoeMedicineArrayList.get(position).getID() != null && cpoeMedicineArrayList.get(position).getID().length() > 0) {
                        cpoeMedicineAdapter.updateCurrentNotes(cpoeMedicineArrayList.get(position).getID());
                    } else {
                        cpoeMedicineAdapter.updateUnSyncCurrentNotes(cpoeMedicineArrayList.get(position).get_ID());
                    }
                    context.startActivity(new Intent(context, CPOEPrescriptionAddUpdateActivity.class).putExtra("isUpdate", "Yes"));
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        EditText row_current_medication_tv_drug_name;
        EditText row_current_medication_tv_date;
        TextView row_current_medication_tv_days;
        TextView row_current_medication_tv_dose;
        TextView row_current_medication_tv_reason;
        TextView row_current_medication_tv_route;
        TextView row_current_medication_tv_frequency;
        TextView row_current_medication_tv_qty;
        TextView is_record_sync_tv;
    }
}

