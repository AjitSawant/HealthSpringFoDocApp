package com.palash.healthspring.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.palash.healthspring.R;
import com.palash.healthspring.activity.CancelAppointmentActivity;
import com.palash.healthspring.activity.PatientQueueActivity;
import com.palash.healthspring.activity.TimeSlotActivity;
import com.palash.healthspring.api.JsonObjectMapper;
import com.palash.healthspring.api.WebServiceConsumer;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.Appointment;
import com.palash.healthspring.entity.AppointmentReason;
import com.palash.healthspring.entity.BookAppointment;
import com.palash.healthspring.entity.DoctorProfile;
import com.palash.healthspring.entity.Flag;
import com.palash.healthspring.entity.Visit;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;
import com.palash.healthspring.utilities.TransparentProgressDialog;
import com.squareup.okhttp.Response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AppointmentExpandListAdapter extends BaseExpandableListAdapter {

    private static final String TAG = AppointmentExpandListAdapter.class.getSimpleName();

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapter;
    private DatabaseAdapter.AppointmentReasonAdapter appointmentReasonAdapterDB;

    private String jSonData = "";
    private JsonObjectMapper objMapper = null;

    private Flag flag;
    private BookAppointment bookAppointment;
    private ArrayList<AppointmentReason> appointmentReasonsList;
    private ArrayList<DoctorProfile> doctorprofilelist = null;

    private HashMap<String, ArrayList<Appointment>> _listDataChild;
    private List<String> _listDataHeader; // header titles

    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat df = new SimpleDateFormat(Constants.TIME_FORMAT, Locale.US);

    public AppointmentExpandListAdapter(Context context, List<String> listDataHeader, HashMap<String, ArrayList<Appointment>> listDataChild) {
        this.context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listDataChild;

        localSetting = new LocalSetting();
        localSetting.Init(context);
        localSetting.Load();

        databaseContract = new DatabaseContract(context);
        databaseAdapter = new DatabaseAdapter(databaseContract);
        bookAppointmentAdapter = databaseAdapter.new BookAppointmentAdapter();
        doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();
        appointmentReasonAdapterDB = databaseAdapter.new AppointmentReasonAdapter();

        doctorprofilelist = doctorProfileAdapter.listAll();
        bookAppointment = new BookAppointment();
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
        try {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.row_appointment_list_header, null);
            }

            TextView heading = (TextView) convertView.findViewById(R.id.tvAppointmentListHeader);
            heading.setText(headerTitle);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        try {
            Log.d(TAG, "Group position" + groupPosition);
            final Appointment appointment = (Appointment) getChild(groupPosition, childPosition);
            if (view == null) {
                LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.row_appointment_list_child, null);
            }
            ImageView row_appointment_iv_gender = (ImageView) view.findViewById(R.id.row_appointment_iv_gender);
            TextView row_appointment_tv_dept = (TextView) view.findViewById(R.id.row_appointment_tv_dept);
            TextView row_appointment_tv_reason = (TextView) view.findViewById(R.id.row_appointment_tv_reason);
            TextView row_appointment_tv_date = (TextView) view.findViewById(R.id.row_appointment_tv_date);
            TextView row_appointment_tv_from_time = (TextView) view.findViewById(R.id.row_appointment_tv_from_time);
            TextView row_appointment_tv_to_time = (TextView) view.findViewById(R.id.row_appointment_tv_to_time);
            TextView row_appointment_tv_name = (TextView) view.findViewById(R.id.row_appointment_tv_name);
            TextView row_appointment_tv_dob = (TextView) view.findViewById(R.id.row_appointment_tv_dob);
            TextView row_appointment_tv_marital_status = (TextView) view.findViewById(R.id.row_appointment_tv_marital_status);
            TextView row_appointment_tv_contact = (TextView) view.findViewById(R.id.row_appointment_tv_contact);
            TextView row_appointment_tv_email = (TextView) view.findViewById(R.id.row_appointment_tv_email);
            TextView row_appointment_bnt_reschedual = (TextView) view.findViewById(R.id.row_appointment_bnt_reschedual);
            TextView row_appointment_bnt_visit = (TextView) view.findViewById(R.id.row_appointment_bnt_visit);
            TextView row_appointment_bnt_cancle = (TextView) view.findViewById(R.id.row_appointment_bnt_cancle);

            if (appointment.getGender().equals("Male")) {
                row_appointment_iv_gender.setImageDrawable(context.getResources().getDrawable(R.drawable.personmale));
            } else if (appointment.getGender().equals("Female")) {
                row_appointment_iv_gender.setImageDrawable(context.getResources().getDrawable(R.drawable.personfemale));
            }

            if (appointment.getAppointmentReason() != null && appointment.getAppointmentReason().length() > 0) {
                row_appointment_tv_reason.setText(appointment.getAppointmentReason());
                row_appointment_tv_reason.setVisibility(View.VISIBLE);
            } else {
                row_appointment_tv_reason.setVisibility(View.GONE);
            }

            if (appointment.getDepartment() != null && appointment.getDepartment().length() > 0) {
                row_appointment_tv_dept.setText(appointment.getDepartment());
                row_appointment_tv_dept.setVisibility(View.VISIBLE);
            } else {
                row_appointment_tv_dept.setVisibility(View.GONE);
            }

            if (appointment.getAppointmentDate() != null && appointment.getAppointmentDate().length() > 0) {
                row_appointment_tv_date.setText(appointment.getAppointmentDate());
                row_appointment_tv_date.setVisibility(View.VISIBLE);
            } else {
                row_appointment_tv_date.setVisibility(View.GONE);
            }

            if (appointment.getFromTime() != null && appointment.getFromTime().length() > 0) {
                row_appointment_tv_from_time.setText(localSetting.formatDate(appointment.getFromTime(), "HH:mm:ss", Constants.OFFLINE_TIME));
                row_appointment_tv_from_time.setVisibility(View.VISIBLE);
            } else {
                row_appointment_tv_from_time.setVisibility(View.GONE);
            }

            if (appointment.getToTime() != null && appointment.getToTime().length() > 0) {
                row_appointment_tv_to_time.setText(localSetting.formatDate(appointment.getToTime(), "HH:mm:ss", Constants.OFFLINE_TIME));
                row_appointment_tv_to_time.setVisibility(View.VISIBLE);
            } else {
                row_appointment_tv_to_time.setVisibility(View.GONE);
            }

            if (appointment.getDOB() != null && appointment.getDOB().length() > 0 && appointment.getDOB().length() > 3) {
                row_appointment_tv_dob.setText(appointment.getDOB());
                row_appointment_tv_dob.setVisibility(View.VISIBLE);
            } else {
                row_appointment_tv_dob.setVisibility(View.GONE);
            }

            String firstName = appointment.getFirstName();
            String lastName = appointment.getLastName();
            String middleName = appointment.getMiddleName();
            if (middleName.length() > 0) {
                row_appointment_tv_name.setText(firstName + " " + middleName + " " + lastName);
            } else {
                row_appointment_tv_name.setText(firstName + " " + lastName);
            }

            if (appointment.getContact1() != null && appointment.getContact1().length() > 0 && appointment.getContact1().length() >= 10) {
                row_appointment_tv_contact.setText(appointment.getContact1());
                row_appointment_tv_contact.setVisibility(View.VISIBLE);
            } else {
                row_appointment_tv_contact.setVisibility(View.GONE);
            }

            if (appointment.getMaritalStatus() != null && appointment.getMaritalStatus().length() > 0) {
                row_appointment_tv_marital_status.setText(appointment.getMaritalStatus());
                row_appointment_tv_marital_status.setVisibility(View.VISIBLE);
            } else {
                row_appointment_tv_marital_status.setVisibility(View.GONE);
            }

            if (appointment.getEmailId() != null && appointment.getEmailId().length() > 0) {
                row_appointment_tv_email.setText(appointment.getEmailId());
                row_appointment_tv_email.setVisibility(View.VISIBLE);
            } else {
                row_appointment_tv_email.setVisibility(View.GONE);
            }

            String CurrentDate = new SimpleDateFormat(Constants.PATIENT_QUEUE_DATE, Locale.getDefault()).format(new Date());
            if (CurrentDate.equals(appointment.getAppointmentDate())) {
                row_appointment_bnt_reschedual.setVisibility(View.VISIBLE);
                row_appointment_bnt_visit.setVisibility(View.VISIBLE);
                row_appointment_bnt_cancle.setVisibility(View.VISIBLE);
            } else {
                row_appointment_bnt_reschedual.setVisibility(View.VISIBLE);
                row_appointment_bnt_visit.setVisibility(View.GONE);
                row_appointment_bnt_cancle.setVisibility(View.VISIBLE);
            }

            // schedule and cancel button hide                        // commented by Ajit
            row_appointment_bnt_reschedual.setVisibility(View.GONE);
            row_appointment_bnt_cancle.setVisibility(View.GONE);
            row_appointment_bnt_visit.setVisibility(View.GONE);

            row_appointment_bnt_reschedual.getId();
            row_appointment_bnt_reschedual.setTag(appointment);
            row_appointment_bnt_reschedual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle(context.getResources().getString(R.string.app_name))
                            .setMessage("Do you really want to reschedule this appointment?")
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    bookAppointment.setAppointmentId(appointment.getID());
                                    bookAppointment.setUnitID(appointment.getUnitID());
                                    bookAppointment.setPatientID(appointment.getPatientID());
                                    bookAppointment.setFirstName(appointment.getFirstName());
                                    bookAppointment.setLastName(appointment.getLastName());
                                    bookAppointment.setMiddleName(appointment.getMiddleName());
                                    bookAppointment.setEmailId(appointment.getEmailId());
                                    bookAppointment.setContact1(appointment.getContact1());
                                    bookAppointment.setGenderID(appointment.getGenderID());
                                    bookAppointment.setBloodGroupID(appointment.getBloodGroupID());
                                    bookAppointment.setDOB(appointment.getDOB());
                                    bookAppointment.setMaritalStatusID(appointment.getMaritalStatusID());
                                    bookAppointmentAdapter.create(bookAppointment);
                                    String FirstName = doctorprofilelist.get(0).getFirstName();
                                    String MiddleName = doctorprofilelist.get(0).getMiddleName();
                                    String LastName = doctorprofilelist.get(0).getLastName();
                                    String Name = FirstName + " " + LastName;
                                    if (MiddleName.trim().length() > 0) {
                                        Name = FirstName + " " + MiddleName + " " + LastName;
                                    }
                                    bookAppointment.setID(doctorprofilelist.get(0).getID());
                                    bookAppointment.setDoctorID(doctorprofilelist.get(0).getDoctorID());
                                    bookAppointment.setDoctorName(Name);
                                    bookAppointment.setSpecialization(doctorprofilelist.get(0).getSpecialization());
                                    bookAppointment.setDoctorEducation(doctorprofilelist.get(0).getEducation());
                                    bookAppointment.setDoctorMobileNo(doctorprofilelist.get(0).getPFNumber());
                                    bookAppointmentAdapter.updateDoctor(bookAppointment);

                                    bookAppointment.setAppointmentReasonID(appointment.getAppointmentReasonID());
                                    bookAppointment.setDepartmentID(appointment.getDepartmentID());
                                    bookAppointment.setComplaintId(appointment.getComplaintId());
                                    bookAppointment.setRemark(appointment.getRemark());
                                    bookAppointmentAdapter.updateAppointment(bookAppointment);
                                    localSetting.Activityname = "RescheduleAppointment";
                                    localSetting.Save();
                                    Intent doctorlist_intent = new Intent(context, TimeSlotActivity.class);
                                    context.startActivity(doctorlist_intent);
                                    doctorlist_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    ((Activity) context).finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.mipmap.ic_launcher)
                            .show();
                }
            });
            row_appointment_bnt_visit.getId();
            row_appointment_bnt_visit.setTag(appointment);
            row_appointment_bnt_visit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        new AlertDialog.Builder(context)
                                .setTitle(context.getResources().getString(R.string.app_name))
                                .setMessage("Do you really want to mark this appointment as visit?")
                                .setCancelable(false)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Visit visit = new Visit();
                                        visit.setAppointmentId(appointment.getID());
                                        visit.setUnitID(appointment.getUnitID());
                                        visit.setDate(df.format(c.getTime()));
                                        visit.setPatientID(appointment.getPatientID());
                                        visit.setPatientUnitID(appointment.getPatientUnitID());
                                        visit.setVisitTypeID(appointment.getAppointmentReasonID());
                                        visit.setDepartmentID(appointment.getDepartmentID());
                                        visit.setDoctorID(appointment.getDoctorID());
                                        visit.setComplaints(appointment.getComplaint());
                                        visit.setReferredDoctorID(doctorprofilelist.get(0).getDoctorID());
                                        visit.setVisitDateTime(df.format(c.getTime()));
                                        String name = "Dr." + doctorprofilelist.get(0).getFirstName() + " " + doctorprofilelist.get(0).getMiddleName() + " " + doctorprofilelist.get(0).getLastName();
                                        visit.setReferredDoctor(name);
                                        visit.setAddedBy(doctorprofilelist.get(0).getID());
                                        appointmentReasonsList = appointmentReasonAdapterDB.listVisitTypeServiceID(appointment.getAppointmentReasonID());
                                        visit.setVisitTypeServiceID(appointmentReasonsList.get(0).getServiceID());
                                        objMapper = new JsonObjectMapper();
                                        jSonData = objMapper.unMap(visit);
                                        new VisitAppointmentTask().execute();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(R.mipmap.ic_launcher)
                                .show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            row_appointment_bnt_cancle.getId();
            row_appointment_bnt_cancle.setTag(appointment);
            row_appointment_bnt_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle(context.getResources().getString(R.string.app_name))
                            .setMessage("Do you really want to cancel this appointment?")
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(context, CancelAppointmentActivity.class);
                                    intent.putExtra("UnitID", appointment.getUnitID());
                                    intent.putExtra("AppoinmentID", appointment.getID());
                                    context.startActivity(intent);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    ((Activity) context).finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.mipmap.ic_launcher)
                            .show();
                }
            });
            return view;
        } catch (Exception ex) {
            return view;
        }
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class VisitAppointmentTask extends AsyncTask<Void, Void, String> {

        private int responseCode = 0;
        private TransparentProgressDialog progressDialog = null;
        private WebServiceConsumer serviceConsumer = null;
        private Response response = null;

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String responseString = "";
            try {
                serviceConsumer = new WebServiceConsumer(context, null, null);
                response = serviceConsumer.POST(Constants.VISIT_MARK_APPOINTMENT_URL, jSonData);
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    Log.d(Constants.TAG, "Response code:" + responseCode);
                    Log.d(Constants.TAG, "Response string:" + responseString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (responseCode == Constants.HTTP_OK_200) {
                    localSetting.hideDialog(progressDialog);
                    new AlertDialog
                            .Builder(context)
                            .setTitle(context.getResources().getString(R.string.app_name))
                            .setMessage("Visit marked successfully.")
                            .setCancelable(false)
                            .setPositiveButton("Go to Patient Queue", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(context, PatientQueueActivity.class);
                                    context.startActivity(intent);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    ((Activity) context).finish();
                                }
                            })
                            .setIcon(R.mipmap.ic_launcher)
                            .show();
                } else {
                    localSetting.hideDialog(progressDialog);
                    localSetting.alertbox(context, localSetting.handleError(responseCode), false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }
}
