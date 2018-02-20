package com.palash.healthspringapp.activity;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buzzbox.mob.android.scheduler.SchedulerManager;
import com.palash.healthspringapp.R;
import com.palash.healthspringapp.adapter.SpinnerAdapter;
import com.palash.healthspringapp.api.JsonObjectMapper;
import com.palash.healthspringapp.api.WebServiceConsumer;
import com.palash.healthspringapp.database.DatabaseAdapter;
import com.palash.healthspringapp.database.DatabaseContract;
import com.palash.healthspringapp.entity.AppointmentReason;
import com.palash.healthspringapp.entity.BookAppointment;
import com.palash.healthspringapp.entity.Complaint;
import com.palash.healthspringapp.entity.Department;
import com.palash.healthspringapp.entity.Flag;
import com.palash.healthspringapp.entity.Visit;
import com.palash.healthspringapp.task.MasterTask;
import com.palash.healthspringapp.utilities.Constants;
import com.palash.healthspringapp.utilities.LocalSetting;
import com.palash.healthspringapp.utilities.TransparentProgressDialog;
import com.palash.healthspringapp.utilities.Validate;
import com.squareup.okhttp.Response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;


public class VisitBookActivity extends AppCompatActivity {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapter;
    private DatabaseAdapter.AppointmentReasonAdapter appointmentReasonAdapterDB;
    private DatabaseAdapter.DepartmentAdapter departmentAdapterBD;
    private DatabaseAdapter.ComplaintAdapter complaintAdapterBD;
    private DatabaseAdapter.MasterFlagAdapter masterFlagAdapter;

    private Flag masterflag;
    private Visit visit;
    private ArrayList<BookAppointment> bookAppointmentArrayList;
    private ArrayList<AppointmentReason> appointmentReasonslist;
    private ArrayList<Department> departmentslist;
    private ArrayList<Complaint> complaintslist;

    private TextView visit_book_edt_patient_name;
    private TextView visit_book_edt_patient_contact;
    private TextView visit_book_edt_patient_mail;
    private TextView visit_book_edt_doctor_name;
    private TextView visit_book_edt_patient_gender;
    private TextView visit_book_edt_doctor_spcilization;
    private TextView visit_book_edt_doctor_education;
    private TextView visit_book_edt_doctor_contactno;
    private EditText visit_book_edt_date;
    private EditText visit_book_edt_time;
    private LinearLayout visit_book_edt_doctor_mobile_layout;

    private MaterialSpinner visit_book_spinner_appointmetreason;
    private MaterialSpinner visit_book_spinner_department;
    private MaterialSpinner visit_book_spinner_complaint;
    private SpinnerAdapter.AppointmentReasonAdapter appointmentReasonAdapter;
    private SpinnerAdapter.DepartmentAdapter departmentAdapter;
    private SpinnerAdapter.ComplaintAdapter complaintAdapter;

    private String CurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_book);
        InitSetting();
        InitView();
        InitAdapter();
    }

    private void InitSetting() {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            context = this;
            localSetting = new LocalSetting();
            databaseContract = new DatabaseContract(context);
            databaseAdapter = new DatabaseAdapter(databaseContract);
            masterFlagAdapter = databaseAdapter.new MasterFlagAdapter();
            appointmentReasonAdapterDB = databaseAdapter.new AppointmentReasonAdapter();
            departmentAdapterBD = databaseAdapter.new DepartmentAdapter();
            complaintAdapterBD = databaseAdapter.new ComplaintAdapter();
            bookAppointmentAdapter = databaseAdapter.new BookAppointmentAdapter();
            bookAppointmentArrayList = bookAppointmentAdapter.listLast();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView() {
        try {
            visit_book_edt_patient_name = (TextView) findViewById(R.id.visit_book_edt_patient_name);
            visit_book_edt_patient_contact = (TextView) findViewById(R.id.visit_book_edt_patient_contact);
            visit_book_edt_patient_mail = (TextView) findViewById(R.id.visit_book_edt_patient_mail);
            visit_book_edt_doctor_name = (TextView) findViewById(R.id.visit_book_edt_doctor_name);
            visit_book_edt_patient_gender = (TextView) findViewById(R.id.visit_book_edt_patient_gender);
            visit_book_edt_doctor_spcilization = (TextView) findViewById(R.id.visit_book_edt_doctor_spcilization);
            visit_book_edt_doctor_education = (TextView) findViewById(R.id.visit_book_edt_doctor_education);
            visit_book_edt_doctor_contactno = (TextView) findViewById(R.id.visit_book_edt_doctor_contactno);
            visit_book_edt_doctor_mobile_layout = (LinearLayout) findViewById(R.id.visit_book_edt_doctor_mobile_layout);
            visit_book_edt_date = (EditText) findViewById(R.id.visit_book_edt_date);
            visit_book_edt_time = (EditText) findViewById(R.id.visit_book_edt_time);
            visit_book_spinner_department = (MaterialSpinner) findViewById(R.id.visit_book_spinner_department);
            visit_book_spinner_appointmetreason = (MaterialSpinner) findViewById(R.id.visit_book_spinner_appointmetreason);
            visit_book_spinner_complaint = (MaterialSpinner) findViewById(R.id.visit_book_spinner_complaint);

            visit_book_spinner_complaint.setVisibility(View.GONE);
            Bindview();
            disableView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitAdapter() {
        try {
            appointmentReasonslist = appointmentReasonAdapterDB.listAll();
            departmentslist = departmentAdapterBD.listAll();
            complaintslist = complaintAdapterBD.listAll();

            if (appointmentReasonslist != null && appointmentReasonslist.size() > 0) {
                appointmentReasonAdapter = new SpinnerAdapter.AppointmentReasonAdapter(context, appointmentReasonslist);
                visit_book_spinner_appointmetreason.setAdapter(appointmentReasonAdapter);
                appointmentReasonAdapter.notifyDataSetChanged();
            }
            if (departmentslist != null && departmentslist.size() > 0) {
                departmentAdapter = new SpinnerAdapter.DepartmentAdapter(context, departmentslist);
                visit_book_spinner_department.setAdapter(departmentAdapter);
                departmentAdapter.notifyDataSetChanged();
            }
            if (complaintslist != null && complaintslist.size() > 0) {
                complaintAdapter = new SpinnerAdapter.ComplaintAdapter(context, complaintslist);
                visit_book_spinner_complaint.setAdapter(complaintAdapter);
                complaintAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disableView() {
        try {
            visit_book_edt_patient_name.setEnabled(false);
            visit_book_edt_patient_contact.setEnabled(false);
            visit_book_edt_patient_mail.setEnabled(false);
            visit_book_edt_doctor_name.setEnabled(false);
            visit_book_edt_doctor_spcilization.setEnabled(false);
            visit_book_edt_doctor_education.setEnabled(false);
            visit_book_edt_doctor_contactno.setEnabled(false);
            visit_book_edt_date.setEnabled(false);
            visit_book_edt_time.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Bindview() {
        if (bookAppointmentArrayList != null) {
            String FirstName = bookAppointmentArrayList.get(0).getFirstName();
            String MiddleName = bookAppointmentArrayList.get(0).getMiddleName();
            String LastName = bookAppointmentArrayList.get(0).getLastName();
            String PatientName;
            if (MiddleName.trim().length() > 0) {
                PatientName = FirstName + " " + MiddleName + " " + LastName;
            } else {
                PatientName = FirstName + " " + LastName;
            }
            visit_book_edt_patient_name.setText(PatientName);
            visit_book_edt_patient_contact.setText(bookAppointmentArrayList.get(0).getContact1());
            visit_book_edt_patient_mail.setText(bookAppointmentArrayList.get(0).getEmailId());
            visit_book_edt_doctor_name.setText("Dr. " + bookAppointmentArrayList.get(0).getDoctorName());
            visit_book_edt_doctor_spcilization.setText(bookAppointmentArrayList.get(0).getSpecialization());
            visit_book_edt_doctor_education.setText(bookAppointmentArrayList.get(0).getDoctorEducation());
            visit_book_edt_doctor_contactno.setText(bookAppointmentArrayList.get(0).getDoctorMobileNo());

            if (bookAppointmentArrayList.get(0).getDoctorMobileNo() != null && bookAppointmentArrayList.get(0).getDoctorMobileNo().trim().length() > 0) {
                visit_book_edt_doctor_contactno.setText(bookAppointmentArrayList.get(0).getDoctorMobileNo());
                visit_book_edt_doctor_mobile_layout.setVisibility(View.VISIBLE);
            } else {
                visit_book_edt_doctor_mobile_layout.setVisibility(View.GONE);
            }

            if (bookAppointmentArrayList.get(0).getGenderID() != null && bookAppointmentArrayList.get(0).getGenderID().trim().length() > 0) {
                if (bookAppointmentArrayList.get(0).getGenderID().equals("1")) {
                    visit_book_edt_patient_gender.setText("Male");
                } else {
                    visit_book_edt_patient_gender.setText("Female");
                }
            }

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
            String date = df.format(c.getTime());
            String[] splited = date.split("\\s+");
            visit_book_edt_date.setText(splited[0]);
            visit_book_edt_time.setText(splited[1] + " " + splited[2]);
            SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIME_FORMAT, Locale.US);
            CurrentDate = dateFormat.format(c.getTime());
        }
    }

    private void VisitBookBindView() {
        try {
            new AlertDialog
                    .Builder(context)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Do you really want to book visit?")
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            visit = new Visit();
                            int Posappointmnetreason = visit_book_spinner_appointmetreason.getSelectedItemPosition();
                            if (Posappointmnetreason > 0) {
                                Posappointmnetreason = Posappointmnetreason - 1;
                            }
                            int Posdepartment = visit_book_spinner_department.getSelectedItemPosition();
                            if (Posdepartment > 0) {
                                Posdepartment = Posdepartment - 1;
                            }
//                            int Poscomplaint = visit_book_spinner_complaint.getSelectedItemPosition();
//                            if (Poscomplaint > 0) {
//                                Poscomplaint = Poscomplaint - 1;
//                            }
                            visit.setUnitID(bookAppointmentArrayList.get(0).getDoctorUnitID());
                            visit.setDate(CurrentDate);
                            visit.setPatientID(bookAppointmentArrayList.get(0).getPatientID());
                            visit.setPatientUnitID(bookAppointmentArrayList.get(0).getUnitID());
                            visit.setDoctorID(bookAppointmentArrayList.get(0).getDoctorID());
                            visit.setVisitTypeID(appointmentReasonslist.get(Posappointmnetreason).getID());
                            // visit.setComplaints(complaintslist.get(Poscomplaint).getDescription());
                            visit.setDepartmentID(departmentslist.get(Posdepartment).getID());
                            visit.setReferredDoctorID(bookAppointmentArrayList.get(0).getDoctorID());
                            visit.setReferredDoctor("Dr. " + bookAppointmentArrayList.get(0).getDoctorName());
                            visit.setAddedBy(bookAppointmentArrayList.get(0).getID());
                            visit.setVisitTypeServiceID(appointmentReasonslist.get(Posappointmnetreason).getServiceID());
                            visit.setVisitDateTime(CurrentDate);
                            new VisitBookTask().execute();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(R.mipmap.ic_launcher)
                    .show();
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
                    VisitBookBindView();
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean validateControls() {
        if (departmentslist == null || departmentslist.size() == 0 || !Validate.isValidOption(visit_book_spinner_department.getSelectedItemPosition())) {
            Validate.Msgshow(context, "Please select department.");
            return false;
        } else if (appointmentReasonslist == null || appointmentReasonslist.size() == 0 || !Validate.isValidOption(visit_book_spinner_appointmetreason.getSelectedItemPosition())) {
            Validate.Msgshow(context, "Please select appointment reason.");
            return false;
        }
//        else if (!Validate.isValidOption(visit_book_spinner_complaint.getSelectedItemPosition())) {
//            Validate.Msgshow(context, "Please select complaint.");
//            return false;
//        }

        return true;
    }

    public class VisitBookTask extends AsyncTask<Void, Void, String> {
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
                jSonData = objMapper.unMap(visit);
                serviceConsumer = new WebServiceConsumer(context, null, null);
                response = serviceConsumer.POST(Constants.VISIT_BOOK_URL, jSonData);
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
                            .setMessage("Visit booked successfully.")
                            .setCancelable(true)
                            .setPositiveButton("Go to Patient Queue", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(context, PatientQueueActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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

    @Override
    protected void onResume() {
        super.onResume();
    }
}