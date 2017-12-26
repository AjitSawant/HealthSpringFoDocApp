package com.palash.healthspring.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palash.healthspring.R;
import com.palash.healthspring.activity.EMRNavigationDrawerActivity;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.BookAppointment;
import com.palash.healthspring.entity.DoctorProfile;
import com.palash.healthspring.entity.VisitList;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;

import java.util.ArrayList;

public class VisitListAdapter extends BaseAdapter {

    private Context context;
    private LocalSetting localSetting;
    private LayoutInflater inflater;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;

    private BookAppointment bookAppointment;

    private ArrayList<VisitList> visitListArrayList;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapter;

    public VisitListAdapter(Context context, ArrayList<VisitList> visitListArrayList) {
        this.context = context;
        this.visitListArrayList = visitListArrayList;

        localSetting = new LocalSetting();
        localSetting.Init(context);
        localSetting.Load();

        bookAppointment = new BookAppointment();

        databaseContract = new DatabaseContract(context);
        databaseAdapter = new DatabaseAdapter(databaseContract);
        bookAppointmentAdapter = databaseAdapter.new BookAppointmentAdapter();

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return visitListArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return visitListArrayList.get(position);
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
                convertView = inflater.inflate(R.layout.row_visit_list, null);
                holder.row_visit_list_tv_reason = (TextView) convertView.findViewById(R.id.row_visit_list_tv_reason);
                holder.row_visit_list_tv_date = (EditText) convertView.findViewById(R.id.row_visit_list_tv_date);
                holder.row_visit_list_tv_time = (EditText) convertView.findViewById(R.id.row_visit_list_tv_time);
                holder.row_visit_list_tv_name = (TextView) convertView.findViewById(R.id.row_visit_list_tv_name);
                holder.row_visit_list_tv_department = (TextView) convertView.findViewById(R.id.row_visit_list_tv_department);
                holder.row_visit_list_tv_complatient = (TextView) convertView.findViewById(R.id.row_visit_list_tv_complatient);
                holder.layout_row_visit_list_tv_department = (LinearLayout) convertView.findViewById(R.id.layout_row_visit_list_tv_department);
                holder.layout_row_visit_list_tv_reason = (LinearLayout) convertView.findViewById(R.id.layout_row_visit_list_tv_reason);
                holder.layout_row_visit_list_tv_complatient = (LinearLayout) convertView.findViewById(R.id.layout_row_visit_list_tv_complatient);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            VisitList visitList = visitListArrayList.get(position);

            holder.row_visit_list_tv_date.setText(visitList.getAppointmentDate());

            if (visitList.getFromTime() != null && visitList.getFromTime().length() > 0 && visitList.getToTime() != null && visitList.getToTime().length() > 0) {
                holder.row_visit_list_tv_time.setText(localSetting.formatDate(visitList.getFromTime(), "HH:mm:ss", Constants.OFFLINE_TIME)
                        + " - " + localSetting.formatDate(visitList.getToTime(), "HH:mm:ss", Constants.OFFLINE_TIME));
            } else {
                holder.row_visit_list_tv_time.setText(localSetting.formatDate(visitList.getTime(), "HH:mm:ss", Constants.OFFLINE_TIME));
            }

            String firstName = visitList.getFirstName();
            String lastName = visitList.getLastName();
            String middleName = visitList.getMiddleName();

            if (middleName != null && middleName.length() > 0) {
                holder.row_visit_list_tv_name.setText(firstName + " " + middleName + " " + lastName);
            } else {
                holder.row_visit_list_tv_name.setText(firstName + " " + lastName);
            }

            if (visitList.getDepartment() != null && visitList.getDepartment().length() > 0) {
                holder.layout_row_visit_list_tv_department.setVisibility(View.VISIBLE);
                holder.row_visit_list_tv_department.setText(visitList.getDepartment().trim());
            } else {
                holder.layout_row_visit_list_tv_department.setVisibility(View.GONE);
            }

            if (visitList.getVisitDescription() != null && visitList.getVisitDescription().length() > 0) {
                holder.layout_row_visit_list_tv_reason.setVisibility(View.VISIBLE);
                holder.row_visit_list_tv_reason.setText(visitList.getVisitDescription());
            } else {
                holder.layout_row_visit_list_tv_reason.setVisibility(View.GONE);
            }

            if (visitList.getComplaints() != null && visitList.getComplaints().length() > 0) {
                holder.layout_row_visit_list_tv_complatient.setVisibility(View.VISIBLE);
                holder.row_visit_list_tv_complatient.setText(visitList.getComplaints().trim());
            } else {
                holder.layout_row_visit_list_tv_complatient.setVisibility(View.GONE);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bookAppointment.setVisitID(visitListArrayList.get(position).getID());
                    bookAppointmentAdapter.updateVisitID(bookAppointment);
                    localSetting.fragment_name = "VisitList";
                    //localSetting.fragment_name = "PatientQueue";
                    localSetting.Save();
                    context.startActivity(new Intent(context, EMRNavigationDrawerActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class ViewHolder {
        TextView row_visit_list_tv_reason;
        EditText row_visit_list_tv_date;
        EditText row_visit_list_tv_time;
        TextView row_visit_list_tv_name;
        TextView row_visit_list_tv_department;
        TextView row_visit_list_tv_complatient;
        LinearLayout layout_row_visit_list_tv_department;
        LinearLayout layout_row_visit_list_tv_reason;
        LinearLayout layout_row_visit_list_tv_complatient;
    }
}

