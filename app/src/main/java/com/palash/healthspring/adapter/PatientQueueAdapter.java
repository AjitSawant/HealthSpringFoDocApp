package com.palash.healthspring.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.palash.healthspring.R;
import com.palash.healthspring.activity.EMRNavigationDrawerActivity;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.BookAppointment;
import com.palash.healthspring.entity.DoctorProfile;
import com.palash.healthspring.entity.PatientQueue;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;

import java.util.ArrayList;

public class PatientQueueAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private LayoutInflater inflater;
    private LocalSetting localSetting;
    private ArrayList<PatientQueue> patientQueueArrayList;
    private ArrayList<PatientQueue> patientQueueFilterlist;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapter;
    private BookAppointment bookAppointment;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private ArrayList<DoctorProfile> doctorprofilelist = null;

    public PatientQueueAdapter(Context context, ArrayList<PatientQueue> patientQueueArrayList) {
        this.context = context;
        this.patientQueueFilterlist = patientQueueArrayList;
        this.patientQueueArrayList = patientQueueArrayList;

        localSetting = new LocalSetting();
        localSetting.Init(context);
        localSetting.Load();

        bookAppointment = new BookAppointment();
        databaseContract = new DatabaseContract(context);
        databaseAdapter = new DatabaseAdapter(databaseContract);
        bookAppointmentAdapter = databaseAdapter.new BookAppointmentAdapter();
        doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();
        doctorprofilelist = doctorProfileAdapter.listAll();

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return patientQueueArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return patientQueueArrayList.get(position);
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
                convertView = inflater.inflate(R.layout.row_patient_queue, null);
                holder.row_patient_queue_iv_gender = (ImageView) convertView.findViewById(R.id.row_patient_queue_iv_gender);
                holder.row_patient_queue_tv_dept = (TextView) convertView.findViewById(R.id.row_patient_queue_tv_dept);
                holder.row_patient_queue_tv_reason = (TextView) convertView.findViewById(R.id.row_patient_queue_tv_reason);
                holder.row_patient_queue_tv_date = (TextView) convertView.findViewById(R.id.row_patient_queue_tv_date);
                holder.row_patient_queue_tv_from_time = (TextView) convertView.findViewById(R.id.row_patient_queue_tv_from_time);
                //holder.row_patient_queue_tv_to_time = (TextView) convertView.findViewById(R.id.row_patient_queue_tv_to_time);
                holder.row_patient_queue_tv_name = (TextView) convertView.findViewById(R.id.row_patient_queue_tv_name);
                holder.row_patient_queue_tv_dob = (TextView) convertView.findViewById(R.id.row_patient_queue_tv_dob);
                holder.row_patient_queue_tv_marital_status = (TextView) convertView.findViewById(R.id.row_patient_queue_tv_marital_status);
                holder.row_patient_queue_tv_contact = (TextView) convertView.findViewById(R.id.row_patient_queue_tv_contact);
                holder.row_patient_queue_tv_email = (TextView) convertView.findViewById(R.id.row_patient_queue_tv_email);
                holder.row_patient_queue_tv_bloodgroup = (TextView) convertView.findViewById(R.id.row_patient_queue_tv_bloodgroup);
                holder.row_patient_queue_tv_unit_name = (TextView) convertView.findViewById(R.id.row_patient_queue_tv_unit_name);
                holder.row_patient_queue_summary_btn = (TextView) convertView.findViewById(R.id.row_patient_queue_summary_btn);
                holder.row_patient_queue_tv_doctor_namr = (TextView) convertView.findViewById(R.id.row_patient_queue_tv_doctor_namr);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            PatientQueue patientQueue = patientQueueArrayList.get(position);

            if (patientQueue.getGender().equals("Male")) {
                holder.row_patient_queue_iv_gender.setImageDrawable(context.getResources().getDrawable(R.drawable.personmale));
            } else if (patientQueue.getGender().equals("Female")) {
                holder.row_patient_queue_iv_gender.setImageDrawable(context.getResources().getDrawable(R.drawable.personfemale));
            }

            if (patientQueue.getDepartment() != null && patientQueue.getDepartment().length() > 0) {
                holder.row_patient_queue_tv_dept.setText(patientQueue.getDepartment());
                holder.row_patient_queue_tv_dept.setVisibility(View.VISIBLE);
            } else {
                holder.row_patient_queue_tv_dept.setVisibility(View.GONE);
            }

            if (patientQueue.getVisitDescription() != null && patientQueue.getVisitDescription().length() > 0) {
                holder.row_patient_queue_tv_reason.setText(patientQueue.getVisitDescription());
                holder.row_patient_queue_tv_reason.setVisibility(View.VISIBLE);
            } else {
                holder.row_patient_queue_tv_reason.setVisibility(View.GONE);
            }

            if (patientQueue.getUnitName() != null && patientQueue.getUnitName().length() > 0) {
                holder.row_patient_queue_tv_unit_name.setText(patientQueue.getUnitName());
                holder.row_patient_queue_tv_unit_name.setVisibility(View.VISIBLE);
            } else {
                holder.row_patient_queue_tv_unit_name.setVisibility(View.GONE);
            }

            if (patientQueue.getDrName() != null && patientQueue.getDrName().length() > 0) {
                holder.row_patient_queue_tv_doctor_namr.setText(patientQueue.getDrName());
                holder.row_patient_queue_tv_doctor_namr.setVisibility(View.VISIBLE);
            } else {
                holder.row_patient_queue_tv_doctor_namr.setVisibility(View.GONE);
            }

            holder.row_patient_queue_tv_date.setText(patientQueue.getDate());

            if (patientQueue.getFromTime() != null && patientQueue.getFromTime().length() > 0) {
                holder.row_patient_queue_tv_from_time.setVisibility(View.VISIBLE);
                holder.row_patient_queue_tv_from_time.setText(localSetting.formatDate(patientQueue.getFromTime(), "HH:mm:ss", Constants.OFFLINE_TIME));
                //holder.row_patient_queue_tv_to_time.setText(localSetting.formatDate(patientQueue.getToTime(), "HH:mm:ss", Constants.OFFLINE_TIME));
            } else {
                // holder.row_patient_queue_tv_to_time.setText(localSetting.formatDate(patientQueue.getFromTime(), "HH:mm:ss", Constants.OFFLINE_TIME));
                holder.row_patient_queue_tv_from_time.setVisibility(View.INVISIBLE);
            }

            String firstName = patientQueue.getFirstName();
            String lastName = patientQueue.getLastName();
            String middleName = patientQueue.getMiddleName();

            if (middleName != null && middleName.length() > 0) {
                holder.row_patient_queue_tv_name.setText(firstName + " " + middleName + " " + lastName + " " + "(MRNO : " + patientQueue.getMRNo() + ")");
            } else {
                holder.row_patient_queue_tv_name.setText(firstName + " " + lastName + " " + "(MRNO : " + patientQueue.getMRNo() + ")");
            }

            if (patientQueue.getDateOfBirth() != null && patientQueue.getDateOfBirth().length() > 0) {
                holder.row_patient_queue_tv_dob.setVisibility(View.VISIBLE);
                holder.row_patient_queue_tv_dob.setText(patientQueue.getDateOfBirth());
            } else {
                holder.row_patient_queue_tv_dob.setVisibility(View.GONE);
            }

            if (patientQueue.getMaritalStatus() != null && patientQueue.getMaritalStatus().trim().length() > 0) {
                holder.row_patient_queue_tv_marital_status.setVisibility(View.VISIBLE);
                holder.row_patient_queue_tv_marital_status.setText(patientQueue.getMaritalStatus());
            } else {
                holder.row_patient_queue_tv_marital_status.setVisibility(View.GONE);
            }

            if (patientQueue.getContactNo1() != null && patientQueue.getContactNo1().length() > 0) {
                holder.row_patient_queue_tv_contact.setVisibility(View.VISIBLE);
                holder.row_patient_queue_tv_contact.setText(patientQueue.getContactNo1());
            } else {
                holder.row_patient_queue_tv_contact.setVisibility(View.GONE);
            }

            if (patientQueue.getEmail() != null && patientQueue.getEmail().length() > 0) {
                holder.row_patient_queue_tv_email.setVisibility(View.VISIBLE);
                holder.row_patient_queue_tv_email.setText(patientQueue.getEmail());
            } else {
                holder.row_patient_queue_tv_email.setVisibility(View.GONE);
            }

            if (patientQueue.getBloodGroup() != null && patientQueue.getBloodGroup().length() > 0) {
                holder.row_patient_queue_tv_bloodgroup.setVisibility(View.VISIBLE);
                holder.row_patient_queue_tv_bloodgroup.setText(patientQueue.getBloodGroup());
            } else {
                holder.row_patient_queue_tv_bloodgroup.setVisibility(View.GONE);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (patientQueueArrayList.get(position).getIsBilledSubmitID() != null && patientQueueArrayList.get(position).getIsBilledSubmitID().length() > 0) {
                        bookAppointment.setUnitID(patientQueueArrayList.get(position).getPatientUnitID());
                        bookAppointment.setPatientID(patientQueueArrayList.get(position).getPatientId());
                        bookAppointment.setFirstName(patientQueueArrayList.get(position).getFirstName());
                        bookAppointment.setLastName(patientQueueArrayList.get(position).getLastName());
                        bookAppointment.setMiddleName(patientQueueArrayList.get(position).getMiddleName());
                        bookAppointment.setEmailId(patientQueueArrayList.get(position).getEmail());
                        bookAppointment.setContact1(patientQueueArrayList.get(position).getContactNo1());
                        bookAppointment.setGenderID(patientQueueArrayList.get(position).getGender());
                        bookAppointment.setBloodGroupID(patientQueueArrayList.get(position).getBloodGroup());
                        bookAppointment.setDOB(patientQueueArrayList.get(position).getDateOfBirth());
                        bookAppointment.setMaritalStatusID(patientQueueArrayList.get(position).getMaritalStatus());
                        bookAppointment.setMRNo(patientQueueArrayList.get(position).getMRNo());
                        bookAppointmentAdapter.create(bookAppointment);
                        String FirstName = doctorprofilelist.get(0).getFirstName();
                        String MiddleName = doctorprofilelist.get(0).getMiddleName();
                        String LastName = doctorprofilelist.get(0).getLastName();
                        String Name;
                        if (MiddleName.trim().length() > 0) {
                            Name = FirstName + " " + MiddleName + " " + LastName;
                        } else {
                            Name = FirstName + " " + LastName;
                        }
                        bookAppointment.setDoctorID(doctorprofilelist.get(0).getDoctorID());
                        bookAppointment.setDoctorUnitID(doctorprofilelist.get(0).getUnitID());
                        bookAppointment.setDoctorName(Name);
                        bookAppointment.setSpecialization(doctorprofilelist.get(0).getSpecialization());
                        bookAppointment.setDoctorEducation(doctorprofilelist.get(0).getEducation());
                        bookAppointment.setDoctorMobileNo(doctorprofilelist.get(0).getPFNumber());
                        bookAppointment.setVisitID(patientQueueArrayList.get(position).getVisitID());
                        bookAppointment.setVisitTypeID(patientQueueArrayList.get(position).getVisitTypeID());
                        bookAppointmentAdapter.updateDoctor(bookAppointment);
                        bookAppointmentAdapter.updateVisitID(bookAppointment);

                        if (localSetting.checkUnitName(doctorprofilelist.get(0).getUnitID())) {  // for head office add/update should be hide
                            localSetting.Activityname = "VisitList";
                            localSetting.Save();
                        } else {
                            localSetting.fragment_name = "PatientQueue";
                            localSetting.Save();
                        }
                        Intent EMRIntent = new Intent(context, EMRNavigationDrawerActivity.class);
                        context.startActivity(EMRIntent);
                    } else {
                        localSetting.alertbox(context, "Billing is not generated. Bill is required for EMR.", false);
                    }
                }
            });

            holder.row_patient_queue_summary_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (patientQueueArrayList.get(position) != null && patientQueueArrayList.get(position).getID() != null && patientQueueArrayList.get(position).getID().length() > 0) {

                        String url = localSetting.returnPDFUrl("Summary", patientQueueArrayList.get(position).getPatientUnitID(), patientQueueArrayList.get(position).getPatientId(),
                                patientQueueArrayList.get(position).getPatientUnitID(), patientQueueArrayList.get(position).getVisitID(), "", "","","");

                        //context.startActivity(new Intent(context, ViewPDFActivity.class).putExtra("url", url));
                        context.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView row_patient_queue_iv_gender;
        TextView row_patient_queue_tv_dept;
        TextView row_patient_queue_tv_reason;
        TextView row_patient_queue_tv_date;
        TextView row_patient_queue_tv_from_time;
        //TextView row_patient_queue_tv_to_time;
        TextView row_patient_queue_tv_name;
        TextView row_patient_queue_tv_dob;
        TextView row_patient_queue_tv_marital_status;
        TextView row_patient_queue_tv_contact;
        TextView row_patient_queue_tv_email;
        TextView row_patient_queue_tv_bloodgroup;
        TextView row_patient_queue_tv_unit_name;
        TextView row_patient_queue_summary_btn;
        TextView row_patient_queue_tv_doctor_namr;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                patientQueueArrayList = (ArrayList<PatientQueue>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults(); // Holds the
                ArrayList<PatientQueue> FilteredArrList = new ArrayList<PatientQueue>();
                if (patientQueueFilterlist == null) {
                    patientQueueFilterlist = new ArrayList<PatientQueue>(patientQueueArrayList);
                }

                if (constraint == null || constraint.length() == 0) {
                    results.count = patientQueueFilterlist.size();
                    results.values = patientQueueFilterlist;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < patientQueueFilterlist.size(); i++) {
                        String data = patientQueueFilterlist.get(i).getFirstName() + " " + patientQueueFilterlist.get(i).getLastName();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(patientQueueFilterlist.get(i));
                        }
                    }
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}



