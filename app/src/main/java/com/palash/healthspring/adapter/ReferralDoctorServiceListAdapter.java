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
import com.palash.healthspring.entity.ReferralDoctorPerService;
import com.palash.healthspring.fragment.ReferralAddUpdateActivity;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;

import java.util.ArrayList;

public class ReferralDoctorServiceListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;

    private DatabaseAdapter.ReferralServiceListDBAdapter referralServiceListDBAdapter;
    private ArrayList<ReferralDoctorPerService> referralDoctorPerServicearrayList;

    public ReferralDoctorServiceListAdapter(Context context, ArrayList<ReferralDoctorPerService> referralDoctorPerServicearrayList) {
        this.context = context;
        this.referralDoctorPerServicearrayList = referralDoctorPerServicearrayList;

        localSetting = new LocalSetting();
        databaseContract = new DatabaseContract(context);
        databaseAdapter = new DatabaseAdapter(databaseContract);
        referralServiceListDBAdapter = databaseAdapter.new ReferralServiceListDBAdapter();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return referralDoctorPerServicearrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return referralDoctorPerServicearrayList.get(position);
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
                convertView = inflater.inflate(R.layout.row_referral_doctor_service, null);
                holder.row_referral_service_tv_service_name = (TextView) convertView.findViewById(R.id.row_referral_service_tv_service_name);
                holder.row_referral_service_tv_doctor_name = (TextView) convertView.findViewById(R.id.row_referral_service_tv_doctor_name);
                holder.row_referral_service_tv_rate = (TextView) convertView.findViewById(R.id.row_referral_service_tv_rate);
                holder.is_record_sync_tv = (TextView) convertView.findViewById(R.id.is_record_sync_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ReferralDoctorPerService referralDoctorPerService = referralDoctorPerServicearrayList.get(position);
            if (referralDoctorPerService.getServiceName() != null && referralDoctorPerService.getServiceName().length() > 0) {
                holder.row_referral_service_tv_service_name.setText(referralDoctorPerService.getServiceName().trim());
            } else {
                holder.row_referral_service_tv_service_name.setText("-");
            }

            if (referralDoctorPerService.getReferralDoctorName() != null && referralDoctorPerService.getReferralDoctorName().length() > 0) {
                holder.row_referral_service_tv_doctor_name.setText("Dr. "+referralDoctorPerService.getReferralDoctorName().trim());
            } else {
                holder.row_referral_service_tv_doctor_name.setText("-");
            }

            if (referralDoctorPerService.getRate() != null && referralDoctorPerService.getRate().length() > 0) {
                holder.row_referral_service_tv_rate.setText("RS. "+referralDoctorPerService.getRate().trim());
            } else {
                holder.row_referral_service_tv_rate.setText("-");
            }

            if (referralDoctorPerService.getIsSync() != null && referralDoctorPerService.getIsSync().equals("1")) {
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
                    if (referralDoctorPerServicearrayList.get(position).getID() != null && referralDoctorPerServicearrayList.get(position).getID().length() > 0) {
                        referralServiceListDBAdapter.updateCurrentNotes(referralDoctorPerServicearrayList.get(position).getID());
                    } else {
                        referralServiceListDBAdapter.updateUnSyncCurrentNotes(referralDoctorPerServicearrayList.get(position).get_ID());
                    }
                    Constants.backFromAddEMR = false;
                    context.startActivity(new Intent(context, ReferralAddUpdateActivity.class).putExtra("isUpdate", "Yes"));
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView row_referral_service_tv_service_name;
        TextView row_referral_service_tv_doctor_name;
        TextView row_referral_service_tv_rate;
        TextView is_record_sync_tv;
    }
}

