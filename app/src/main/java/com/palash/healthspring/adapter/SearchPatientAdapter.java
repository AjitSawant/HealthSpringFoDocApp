package com.palash.healthspring.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.buzzbox.mob.android.scheduler.SchedulerManager;
import com.palash.healthspring.R;
import com.palash.healthspring.activity.TimeSlotActivity;
import com.palash.healthspring.activity.VisitListActivity;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.BookAppointment;
import com.palash.healthspring.entity.DoctorProfile;
import com.palash.healthspring.entity.Flag;
import com.palash.healthspring.entity.Patient;
import com.palash.healthspring.task.MasterTask;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;

import java.util.ArrayList;

/**
 * Created by manishas on 5/19/2016.
 */
public class SearchPatientAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private LayoutInflater inflater;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.MasterFlagAdapter masterFlagAdapter;

    private Flag flag;
    private DoctorProfile elDoctorProfile;
    private BookAppointment bookAppointment;

    private ArrayList<Patient> patientFilterlist; // Original Values
    private ArrayList<Patient> patientlist; // Values to be displayed
    private ArrayList<DoctorProfile> doctorprofilelist = null;

    public SearchPatientAdapter(Context context, ArrayList<Patient> patientlist) {
        this.context = context;
        this.patientFilterlist = patientlist;
        this.patientlist = patientlist;

        localSetting = new LocalSetting();
        localSetting.Init(context);
        localSetting.Load();

        bookAppointment = new BookAppointment();
        elDoctorProfile = new DoctorProfile();
        databaseContract = new DatabaseContract(context);
        databaseAdapter = new DatabaseAdapter(databaseContract);
        bookAppointmentAdapter = databaseAdapter.new BookAppointmentAdapter();
        doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();
        masterFlagAdapter = databaseAdapter.new MasterFlagAdapter();

        doctorprofilelist = doctorProfileAdapter.listAll();
        if (doctorprofilelist != null && doctorprofilelist.size() > 0) {
            elDoctorProfile = doctorprofilelist.get(0);
        }
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return patientlist.size();
    }

    @Override
    public Patient getItem(int position) {
        return patientlist.get(position);
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
                convertView = inflater.inflate(R.layout.row_list_patient, null);
                holder.patient_img = (ImageView) convertView.findViewById(R.id.row_search_patient_image);
                holder.patient_name = (TextView) convertView.findViewById(R.id.row_search_patient_name_txt);
                holder.patient_mrno = (TextView) convertView.findViewById(R.id.row_search_patient_mrno_txt);
                holder.patient_row_bnt_book = (TextView) convertView.findViewById(R.id.patient_row_bnt_book);
                holder.patient_row_bnt_visit = (TextView) convertView.findViewById(R.id.patient_row_bnt_visit);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Patient elPatient = getItem(position);

            if (elPatient.getMiddleName() != null && elPatient.getMiddleName().length() != 0) {
                holder.patient_name.setText(elPatient.getFirstName() + " " + elPatient.getMiddleName() + " " + elPatient.getLastName());
            } else {
                holder.patient_name.setText(elPatient.getFirstName() + " " + elPatient.getLastName());
            }

            if (elPatient.getGender().equalsIgnoreCase("Male")) {
                holder.patient_img.setImageResource(R.drawable.personmale);
            } else {
                holder.patient_img.setImageResource(R.drawable.personfemale);
            }

            holder.patient_mrno.setText(elPatient.getAge() + " Yrs." + " " + "M.R.No:" + elPatient.getMRNo());

            holder.patient_row_bnt_book.getId();
            holder.patient_row_bnt_book.setTag(position);

            holder.patient_row_bnt_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Patient elPatient = patientlist.get(position);

                    bookAppointment.setID(elDoctorProfile.getID());
                    bookAppointment.setUnitID(elPatient.getUnitID());
                    bookAppointment.setPatientID(elPatient.getID());
                    bookAppointment.setFirstName(elPatient.getFirstName());
                    bookAppointment.setLastName(elPatient.getLastName());
                    bookAppointment.setMiddleName(elPatient.getMiddleName());
                    bookAppointment.setEmailId(elPatient.getEmail());
                    bookAppointment.setContact1(elPatient.getContactNo1());
                    bookAppointment.setGenderID(elPatient.getGenderID());
                    bookAppointment.setBloodGroupID(elPatient.getBloodGroupID());
                    bookAppointment.setDOB(elPatient.getDateOfBirth());
                    bookAppointment.setMaritalStatusID(elPatient.getMaritalStatusID());
                    bookAppointmentAdapter.create(bookAppointment);

                    String FirstName = elDoctorProfile.getFirstName();
                    String MiddleName = elDoctorProfile.getMiddleName();
                    String LastName = elDoctorProfile.getLastName();
                    String Name = FirstName + " " + LastName;
                    if (MiddleName != null && MiddleName.trim().length() > 0) {
                        Name = FirstName + " " + MiddleName + " " + LastName;
                    }

                    bookAppointment.setDoctorID(elDoctorProfile.getDoctorID());
                    bookAppointment.setDoctorUnitID(elDoctorProfile.getUnitID());
                    bookAppointment.setDoctorName(Name);
                    bookAppointment.setSpecialization(elDoctorProfile.getSpecialization());
                    bookAppointment.setDoctorEducation(elDoctorProfile.getEducation());
                    bookAppointment.setDoctorMobileNo(elDoctorProfile.getPFNumber());
                    bookAppointmentAdapter.updateDoctor(bookAppointment);
                    localSetting.Activityname = "PatientList";
                    localSetting.Save();

                    context.startActivity(new Intent(context, TimeSlotActivity.class));
                    Constants.refreshPatient = false;
                }
            });

            holder.patient_row_bnt_visit.getId();
            holder.patient_row_bnt_visit.setTag(position);
            holder.patient_row_bnt_visit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Patient elPatient = patientlist.get(position);

                    bookAppointment.setID(elDoctorProfile.getID());
                    bookAppointment.setUnitID(elPatient.getUnitID());
                    bookAppointment.setPatientID(elPatient.getID());
                    bookAppointment.setFirstName(elPatient.getFirstName());
                    bookAppointment.setLastName(elPatient.getLastName());
                    bookAppointment.setMiddleName(elPatient.getMiddleName());
                    bookAppointment.setEmailId(elPatient.getEmail());
                    bookAppointment.setContact1(elPatient.getContactNo1());
                    bookAppointment.setGenderID(elPatient.getGender());
                    bookAppointment.setBloodGroupID(elPatient.getBloodGroup());
                    bookAppointment.setDOB(elPatient.getDateOfBirth());
                    bookAppointment.setMaritalStatusID(elPatient.getMaritalStatus());
                    bookAppointmentAdapter.create(bookAppointment);

                    String FirstName = elDoctorProfile.getFirstName();
                    String MiddleName = elDoctorProfile.getMiddleName();
                    String LastName = elDoctorProfile.getLastName();
                    String Name;
                    if (MiddleName != null && MiddleName.trim().length() > 0) {
                        Name = FirstName + " " + MiddleName + " " + LastName;
                    } else {
                        Name = FirstName + " " + LastName;
                    }

                    bookAppointment.setDoctorID(elDoctorProfile.getDoctorID());
                    bookAppointment.setDoctorUnitID(elDoctorProfile.getUnitID());
                    bookAppointment.setDoctorName(Name);
                    bookAppointment.setSpecialization(elDoctorProfile.getSpecialization());
                    bookAppointment.setDoctorEducation(elDoctorProfile.getEducation());
                    bookAppointment.setDoctorMobileNo(elDoctorProfile.getPFNumber());
                    bookAppointmentAdapter.updateDoctor(bookAppointment);

                    context.startActivity(new Intent(context, VisitListActivity.class));
                    Constants.refreshPatient = false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView patient_img;
        TextView patient_name;
        TextView patient_mrno;
        TextView patient_row_bnt_book;
        TextView patient_row_bnt_visit;
    }

    private void MasterFlagTask() {
        flag = new Flag();
        flag.setFlag(Constants.BOOK_APPOINTMENT_TASK);
        masterFlagAdapter.updateFalg(flag);
        SchedulerManager.getInstance().runNow(context, MasterTask.class, 1);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                patientlist = (ArrayList<Patient>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Patient> FilteredArrList = new ArrayList<Patient>();
                if (patientFilterlist == null) {
                    patientFilterlist = new ArrayList<Patient>(patientlist);
                }

                if (constraint == null || constraint.length() == 0) {
                    results.count = patientFilterlist.size();
                    results.values = patientFilterlist;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < patientFilterlist.size(); i++) {
                        String data ;
                        if (patientFilterlist.get(i).getMiddleName() != null && patientFilterlist.get(i).getMiddleName().trim().length() > 0) {
                            data = patientFilterlist.get(i).getFirstName() + " " + patientFilterlist.get(i).getMiddleName() + " " + patientFilterlist.get(i).getLastName();
                        } else {
                            data = patientFilterlist.get(i).getFirstName() + " " + patientFilterlist.get(i).getLastName();
                        }
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(patientFilterlist.get(i));
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



