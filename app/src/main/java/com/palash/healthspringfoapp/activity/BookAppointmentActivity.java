package com.palash.healthspringfoapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buzzbox.mob.android.scheduler.SchedulerManager;
import com.palash.healthspringfoapp.R;
import com.palash.healthspringfoapp.adapter.SpinnerAdapter;
import com.palash.healthspringfoapp.api.JsonObjectMapper;
import com.palash.healthspringfoapp.api.WebServiceConsumer;
import com.palash.healthspringfoapp.database.DatabaseAdapter;
import com.palash.healthspringfoapp.database.DatabaseContract;
import com.palash.healthspringfoapp.entity.AppointmentReason;
import com.palash.healthspringfoapp.entity.BookAppointment;
import com.palash.healthspringfoapp.entity.Department;
import com.palash.healthspringfoapp.entity.DoctorProfile;
import com.palash.healthspringfoapp.entity.ELDoctorMaster;
import com.palash.healthspringfoapp.entity.Flag;
import com.palash.healthspringfoapp.task.SynchronizationTask;
import com.palash.healthspringfoapp.utilities.Constants;
import com.palash.healthspringfoapp.utilities.LocalSetting;
import com.palash.healthspringfoapp.utilities.TransparentProgressDialog;
import com.palash.healthspringfoapp.utilities.Validate;
import com.squareup.okhttp.Response;

import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;


public class BookAppointmentActivity extends AppCompatActivity {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapter;
    private DatabaseAdapter.DepartmentAdapter departmentAdapterBD;
    private DatabaseAdapter.AppointmentReasonAdapter appointmentReasonAdapterDB;

    private TextView appointment_edt_patient_name;
    private TextView appointment_edt_patient_gender;
    private TextView appointment_edt_patient_contact;
    private TextView appointment_edt_patient_mail;
    private EditText appointment_edt_date;
    private EditText appointment_edt_time;
    private EditText appointment_edt_remark;
    private EditText appointment_edt_reschedule_reason;
    private MaterialSpinner appointment_spinner_department;
    private MaterialSpinner appointment_book_spinner_doctor;
    private MaterialSpinner appointment_spinner_appointmetreason;
    private LinearLayout layout_appointment_book_spinner_department;
    private LinearLayout layout_appointment_book_spinner_doctor;

    private SpinnerAdapter.AppointmentReasonAdapter appointmentReasonAdapter;
    private SpinnerAdapter.DepartmentAdapter departmentAdapter;
    private SpinnerAdapter.DoctorNameListAdapter doctorNameListAdapter;

    private BookAppointment bookAppointment;

    private ArrayList<DoctorProfile> doctorProfileArrayList;
    private ArrayList<Department> departmentslist;
    private ArrayList<ELDoctorMaster> elDoctorMasterArrayList;
    private ArrayList<BookAppointment> bookAppointmentArrayList;
    private ArrayList<AppointmentReason> appointmentReasonslist;

    private String SelectedDepartmentID = "0";
    private String SelectedDoctorID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        InitSetting();
        InitView();
        disableView();
        InitAdapter();
        isLoginAsDoctor();
    }

    private void InitSetting() {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            context = this;
            localSetting = new LocalSetting();
            localSetting.Init(context);
            localSetting.Load();
            databaseContract = new DatabaseContract(context);
            databaseAdapter = new DatabaseAdapter(databaseContract);
            doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();
            departmentAdapterBD = databaseAdapter.new DepartmentAdapter();
            appointmentReasonAdapterDB = databaseAdapter.new AppointmentReasonAdapter();
            bookAppointmentAdapter = databaseAdapter.new BookAppointmentAdapter();

            doctorProfileArrayList = doctorProfileAdapter.listAll();
            appointmentReasonslist = appointmentReasonAdapterDB.listAll();
            if (doctorProfileArrayList != null && doctorProfileArrayList.size() > 0) {
                departmentslist = departmentAdapterBD.listAll(doctorProfileArrayList.get(0).getUnitID());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView() {
        try {
            appointment_edt_patient_name = (TextView) findViewById(R.id.appointment_edt_patient_name);
            appointment_edt_patient_gender = (TextView) findViewById(R.id.appointment_edt_patient_gender);
            appointment_edt_patient_contact = (TextView) findViewById(R.id.appointment_edt_patient_contact);
            appointment_edt_patient_mail = (TextView) findViewById(R.id.appointment_edt_patient_mail);
            appointment_spinner_department = (MaterialSpinner) findViewById(R.id.appointment_spinner_department);
            appointment_book_spinner_doctor = (MaterialSpinner) findViewById(R.id.appointment_book_spinner_doctor);
            layout_appointment_book_spinner_department = (LinearLayout) findViewById(R.id.layout_appointment_book_spinner_department);
            layout_appointment_book_spinner_doctor = (LinearLayout) findViewById(R.id.layout_appointment_book_spinner_doctor);
            appointment_edt_date = (EditText) findViewById(R.id.appointment_edt_date);
            appointment_edt_time = (EditText) findViewById(R.id.appointment_edt_time);
            appointment_edt_remark = (EditText) findViewById(R.id.appointment_edt_remark);
            appointment_edt_reschedule_reason = (EditText) findViewById(R.id.appointment_edt_reschedule_reason);
            appointment_spinner_appointmetreason = (MaterialSpinner) findViewById(R.id.appointment_spinner_appointmetreason);

            if (localSetting.Activityname.equals("PatientList")) {
                setTitle(R.string.dashboard_Appointment);
                appointment_edt_reschedule_reason.setVisibility(View.GONE);
            } else if (localSetting.Activityname.equals("RescheduleAppointment")) {
                appointment_edt_reschedule_reason.setVisibility(View.VISIBLE);
                setTitle("Reschedule Appointment");
            }

            BindView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitAdapter() {
        try {

            if (departmentslist != null && departmentslist.size() > 0) {
                departmentAdapter = new SpinnerAdapter.DepartmentAdapter(context, departmentslist);
                appointment_spinner_department.setAdapter(departmentAdapter);
                departmentAdapter.notifyDataSetChanged();
                appointment_spinner_department.setSelection(1);

                appointment_spinner_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int sVisitTypePos = appointment_spinner_department.getSelectedItemPosition();
                        if (sVisitTypePos == 0) {
                            SelectedDepartmentID = "0";
                        } else if (sVisitTypePos > 0) {
                            sVisitTypePos = sVisitTypePos - 1;
                            SelectedDepartmentID = departmentslist.get(sVisitTypePos).getID();
                        }
                        if (doctorProfileArrayList != null && doctorProfileArrayList.size() > 0) {
                            if (!SelectedDepartmentID.equals("0") && doctorProfileArrayList.get(0).getIsFrontOfficeUser().equals("1")) {
                                new AppointmentDoctorScheduleTask().execute();
                            } else {
                                elDoctorMasterArrayList = new ArrayList<ELDoctorMaster>();
                                doctorNameListAdapter = new SpinnerAdapter.DoctorNameListAdapter(context, elDoctorMasterArrayList);
                                appointment_book_spinner_doctor.setAdapter(doctorNameListAdapter);
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                departmentAdapter = new SpinnerAdapter.DepartmentAdapter(context, departmentslist);
                appointment_spinner_department.setAdapter(departmentAdapter);
            }

            if (appointmentReasonslist != null && appointmentReasonslist.size() > 0) {
                appointmentReasonAdapter = new SpinnerAdapter.AppointmentReasonAdapter(context, appointmentReasonslist);
                appointment_spinner_appointmetreason.setAdapter(appointmentReasonAdapter);
                appointmentReasonAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void isLoginAsDoctor() {
        if (doctorProfileArrayList != null && doctorProfileArrayList.size() > 0) {
            if (LocalSetting.isFrontOfficeUser(doctorProfileArrayList.get(0))) {
                layout_appointment_book_spinner_department.setVisibility(View.VISIBLE);
                layout_appointment_book_spinner_doctor.setVisibility(View.VISIBLE);
            } else {
                layout_appointment_book_spinner_doctor.setVisibility(View.GONE);

                //SelectedDepartmentID = doctorProfileArrayList.get(0).getDepartmentID();
                SelectedDoctorID = doctorProfileArrayList.get(0).getDoctorID();
            }
        }
    }

    private boolean validateControls() {
        if (departmentslist == null || departmentslist.size() == 0 || !Validate.isValidOption(appointment_spinner_department.getSelectedItemPosition())) {
            Validate.Msgshow(context, "Please select department.");
            return false;
        } else if (appointmentReasonslist == null || appointmentReasonslist.size() == 0 || !Validate.isValidOption(appointment_spinner_appointmetreason.getSelectedItemPosition())) {
            Validate.Msgshow(context, "Please select appointment reason.");
            return false;
        } else if (localSetting.Activityname.equals("RescheduleAppointment")) {
            if (!Validate.hasTextIn(appointment_edt_reschedule_reason.getText().toString())) {
                Validate.Msgshow(context, "Please enter reschedule reason.");
                return false;
            }
        }
        return true;
    }

    private void disableView() {
        try {
            appointment_edt_patient_name.setEnabled(false);
            appointment_edt_patient_contact.setEnabled(false);
            appointment_edt_patient_mail.setEnabled(false);
            //appointment_edt_doctor_name.setEnabled(false);
            //appointment_edt_doctor_spcilization.setEnabled(false);
            //appointment_edt_doctor_education.setEnabled(false);
            //appointment_edt_doctor_contactno.setEnabled(false);
            appointment_edt_date.setEnabled(false);
            appointment_edt_time.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void BindView() {
        try {
            bookAppointmentArrayList = bookAppointmentAdapter.listLast();
            if (bookAppointmentArrayList != null && bookAppointmentArrayList.size() > 0) {
                String FirstName = bookAppointmentArrayList.get(0).getFirstName();
                String MiddleName = bookAppointmentArrayList.get(0).getMiddleName();
                String LastName = bookAppointmentArrayList.get(0).getLastName();
                String Name;
                if (MiddleName != null && MiddleName.length() > 0) {
                    Name = FirstName + " " + MiddleName + " " + LastName;
                } else {
                    Name = FirstName + " " + LastName;
                }
                appointment_edt_patient_name.setText(Name);
                appointment_edt_patient_contact.setText(bookAppointmentArrayList.get(0).getContact1());
                appointment_edt_patient_mail.setText(bookAppointmentArrayList.get(0).getEmailId());
                appointment_edt_time.setText(bookAppointmentArrayList.get(0).getFromTime() + " - " + bookAppointmentArrayList.get(0).getToTime());
                appointment_edt_date.setText(bookAppointmentArrayList.get(0).getAppointmentDate());
                appointment_edt_remark.setText(bookAppointmentArrayList.get(0).getRemark());

                if (bookAppointmentArrayList.get(0).getGenderID() != null && bookAppointmentArrayList.get(0).getGenderID().trim().length() > 0) {
                    if (bookAppointmentArrayList.get(0).getGenderID().equals("1")) {
                        appointment_edt_patient_gender.setText("Male");
                    } else {
                        appointment_edt_patient_gender.setText("Female");
                    }
                }
                try {
                    if (appointmentReasonslist != null && appointmentReasonslist.size() > 0) {
                        appointment_spinner_appointmetreason.setSelection(Integer.parseInt(bookAppointmentArrayList.get(0).getAppointmentReasonID()));
                    }
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void BookAppointmentBindView() {
        try {
            new AlertDialog
                    .Builder(context)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Do you really want to book appointment?")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            bookAppointment = new BookAppointment();
                            int Posappointmnetreason = appointment_spinner_appointmetreason.getSelectedItemPosition();
                            if (Posappointmnetreason > 0) {
                                Posappointmnetreason = Posappointmnetreason - 1;
                            }

                            int Posdepartment = appointment_spinner_department.getSelectedItemPosition();
                            if (Posdepartment > 0) {
                                Posdepartment = Posdepartment - 1;
                            }

                            /*int Poscomplaint = appointment_spinner_complaint.getSelectedItemPosition();
                            if (Poscomplaint > 0) {
                                Poscomplaint = Poscomplaint - 1;
                            }*/
                            bookAppointment.setAppointmentReasonID(appointmentReasonslist.get(Posappointmnetreason).getID());
                            bookAppointment.setRemark(appointment_edt_remark.getText().toString());
                            //bookAppointment.setComplaintId(complaintslist.get(Poscomplaint).getID());
                            bookAppointment.setDepartmentID(departmentslist.get(Posdepartment).getID());
                            bookAppointmentAdapter.updateAppointment(bookAppointment);
                            bookAppointmentArrayList = bookAppointmentAdapter.listLast();

                            bookAppointment = new BookAppointment();
                            bookAppointment.setDoctorUnitID(bookAppointmentArrayList.get(0).getDoctorUnitID());
                            bookAppointment.setDoctorID(bookAppointmentArrayList.get(0).getDoctorID());
                            bookAppointment.setUnitID(bookAppointmentArrayList.get(0).getUnitID());
                            bookAppointment.setPatientID(bookAppointmentArrayList.get(0).getPatientID());
                            bookAppointment.setFirstName(bookAppointmentArrayList.get(0).getFirstName());
                            bookAppointment.setMiddleName(bookAppointmentArrayList.get(0).getMiddleName());
                            bookAppointment.setLastName(bookAppointmentArrayList.get(0).getLastName());
                            bookAppointment.setGenderID(bookAppointmentArrayList.get(0).getGenderID());
                            bookAppointment.setDOB(bookAppointmentArrayList.get(0).getDOB());
                            bookAppointment.setMaritalStatusID(bookAppointmentArrayList.get(0).getMaritalStatusID());
                            bookAppointment.setBloodGroupID(bookAppointmentArrayList.get(0).getBloodGroupID());
                            bookAppointment.setContact1(bookAppointmentArrayList.get(0).getContact1());
                            bookAppointment.setEmailId(bookAppointmentArrayList.get(0).getEmailId());

                            //bookAppointment.setComplaintId(bookAppointmentArrayList.get(0).getComplaintId());
                            bookAppointment.setAppointmentDate(bookAppointmentArrayList.get(0).getAppointmentDate());
                            bookAppointment.setFromTime(bookAppointmentArrayList.get(0).getFromTime());
                            bookAppointment.setToTime(bookAppointmentArrayList.get(0).getToTime());
                            bookAppointment.setRemark(bookAppointmentArrayList.get(0).getRemark());
                            bookAppointment.setAppointmentReasonID(bookAppointmentArrayList.get(0).getAppointmentReasonID());
                            bookAppointment.setDepartmentID(bookAppointmentArrayList.get(0).getDepartmentID());
                            bookAppointment.setAddedBy(doctorProfileArrayList.get(0).getID());
                           /* bookAppointment.setRemark(bookAppointmentArrayList.get(0).getRemark());
                            bookAppointment.setComplaintId(bookAppointmentArrayList.get(0).getComplaintId());
                           */
                            new BookAppointmentTask().execute();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(R.mipmap.ic_launcher)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void RescheduleAppointmentBindView() {
        try {
            bookAppointment = new BookAppointment();
            int Posappointmnetreason = appointment_spinner_appointmetreason.getSelectedItemPosition();
            if (Posappointmnetreason > 0) {
                Posappointmnetreason = Posappointmnetreason - 1;
            }

            int Posdepartment = appointment_spinner_department.getSelectedItemPosition();
            if (Posdepartment > 0) {
                Posdepartment = Posdepartment - 1;
            }

            /*int Poscomplaint = appointment_spinner_complaint.getSelectedItemPosition();
            if (Poscomplaint > 0) {
                Poscomplaint = Poscomplaint - 1;
            }*/

            bookAppointment.setAppointmentReasonID(appointmentReasonslist.get(Posappointmnetreason).getID());
            bookAppointment.setRemark(appointment_edt_remark.getText().toString());
            bookAppointment.setReSchedulingReason(appointment_edt_reschedule_reason.getText().toString());
            //bookAppointment.setComplaintId(complaintslist.get(Poscomplaint).getID());
            bookAppointment.setDepartmentID(departmentslist.get(Posdepartment).getID());
            bookAppointmentAdapter.updateAppointment(bookAppointment);
            bookAppointmentArrayList = bookAppointmentAdapter.listLast();

            bookAppointment = new BookAppointment();
            bookAppointment.setAppointmentId(bookAppointmentArrayList.get(0).getAppointmentId());
            bookAppointment.setUnitID(bookAppointmentArrayList.get(0).getUnitID());
            bookAppointment.setComplaintId(bookAppointmentArrayList.get(0).getComplaintId());
            bookAppointment.setAppointmentDate(bookAppointmentArrayList.get(0).getAppointmentDate());
            bookAppointment.setFromTime(bookAppointmentArrayList.get(0).getFromTime());
            bookAppointment.setToTime(bookAppointmentArrayList.get(0).getToTime());
            bookAppointment.setRemark(bookAppointmentArrayList.get(0).getRemark());
            bookAppointment.setReSchedulingReason(bookAppointmentArrayList.get(0).getReSchedulingReason());
            bookAppointment.setAppointmentReasonID(bookAppointmentArrayList.get(0).getAppointmentReasonID());
            bookAppointment.setRemark(bookAppointmentArrayList.get(0).getRemark());
            //bookAppointment.setComplaintId(bookAppointmentArrayList.get(0).getComplaintId());
            bookAppointment.setDepartmentID(bookAppointmentArrayList.get(0).getDepartmentID());

            new RescheduleAppointmentTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.menu_toolbar_save).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toolbar_save:
                if (validateControls()) {
                    if (localSetting.Activityname.equals("PatientList")) {
                        //BookAppointmentBindView();
                    } else if (localSetting.Activityname.equals("RescheduleAppointment")) {
                        //RescheduleAppointmentBindView();
                    }
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class BookAppointmentTask extends AsyncTask<Void, Void, String> {
        private int responseCode = 0;
        private TransparentProgressDialog progressDialog = null;
        private JsonObjectMapper objMapper = null;
        private WebServiceConsumer serviceConsumer = null;
        private Response response = null;
        private String jSonData = "";

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
                objMapper = new JsonObjectMapper();
                jSonData = objMapper.unMap(bookAppointment);
                serviceConsumer = new WebServiceConsumer(context, null, null, null);
                response = serviceConsumer.POST(Constants.BOOK_APPOINTMENT_URL, jSonData);
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
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage("Appointment booked successfully.")
                            .setCancelable(false)
                            .setPositiveButton("Go to Appointment", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SchedulerManager.getInstance().runNow(context, SynchronizationTask.class, 1);
                                    startActivity(new Intent(context, AppointmentListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    finish();
                                }
                            })
                            .setIcon(R.mipmap.ic_launcher).show();
                } else if (responseCode == Constants.HTTP_AMBIGUOUS_300) {
                    localSetting.hideDialog(progressDialog);
                    new AlertDialog
                            .Builder(context)
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage("Appointment booking failed. Selected slot is already booked. Please try another slot.")
                            .setCancelable(false)
                            .setPositiveButton("Go back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setIcon(R.mipmap.ic_launcher).show();
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

    public class RescheduleAppointmentTask extends AsyncTask<Void, Void, String> {
        private int responseCode = 0;
        private TransparentProgressDialog progressDialog = null;
        private JsonObjectMapper objMapper = null;
        private WebServiceConsumer serviceConsumer = null;
        private Response response = null;
        private String jSonData = "";

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
                objMapper = new JsonObjectMapper();
                jSonData = objMapper.unMap(bookAppointment);
                serviceConsumer = new WebServiceConsumer(context, null, null, null);
                response = serviceConsumer.POST(Constants.RECHEDUAL_APPOINTMENT_URL, jSonData);
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
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage("Appointment rescheduled successfully.")
                            .setCancelable(false)
                            .setPositiveButton("Go to Appointment", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //startActivity(new Intent(context, AppointmentListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    finish();
                                }
                            })
                            .setIcon(R.mipmap.ic_launcher).show();
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

    public class AppointmentDoctorScheduleTask extends AsyncTask<Void, Void, String> {
        private JsonObjectMapper objectMapper;
        private String responseString = "";
        private int responseCode = 0;
        private TransparentProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                WebServiceConsumer serviceConsumer = new WebServiceConsumer(context, null, null, null);
                Response response = serviceConsumer.GET(Constants.VISIT_SCHEDULE_DOCTOR_NAME_URL + doctorProfileArrayList.get(0).getUnitID() + "&DepartmentID=" + SelectedDepartmentID);
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
            localSetting.hideDialog(progressDialog);
            if (responseCode == Constants.HTTP_OK_200) {
                try {
                    objectMapper = new JsonObjectMapper();
                    elDoctorMasterArrayList = objectMapper.map(responseString, ELDoctorMaster.class);

                    //  --------------- Patient category L3 drop down  -------------------------
                    if (elDoctorMasterArrayList != null && elDoctorMasterArrayList.size() > 0) {
                        doctorNameListAdapter = new SpinnerAdapter.DoctorNameListAdapter(context, elDoctorMasterArrayList);
                        appointment_book_spinner_doctor.setAdapter(doctorNameListAdapter);
                        doctorNameListAdapter.notifyDataSetChanged();

                        appointment_book_spinner_doctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                int sCategoryL3Pos = appointment_book_spinner_doctor.getSelectedItemPosition();
                                if (sCategoryL3Pos == 0) {
                                    SelectedDoctorID = "0";
                                } else if (sCategoryL3Pos > 0) {
                                    sCategoryL3Pos = sCategoryL3Pos - 1;
                                    SelectedDoctorID = elDoctorMasterArrayList.get(sCategoryL3Pos).getDoctorID();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    } else {
                        SelectedDoctorID = "0";
                        elDoctorMasterArrayList = new ArrayList<>();
                        doctorNameListAdapter = new SpinnerAdapter.DoctorNameListAdapter(context, elDoctorMasterArrayList);
                        appointment_book_spinner_doctor.setAdapter(doctorNameListAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (responseCode == Constants.HTTP_NO_RECORD_FOUND_OK_204) {
                SelectedDoctorID = "0";
                elDoctorMasterArrayList = new ArrayList<>();
                doctorNameListAdapter = new SpinnerAdapter.DoctorNameListAdapter(context, elDoctorMasterArrayList);
                appointment_book_spinner_doctor.setAdapter(doctorNameListAdapter);
            } else {
                localSetting.alertbox(context, localSetting.handleError(responseCode), false);
            }
            super.onPostExecute(result);
        }
    }
}
