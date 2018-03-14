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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.palash.healthspringapp.entity.DoctorProfile;
import com.palash.healthspringapp.entity.ELDoctorMaster;
import com.palash.healthspringapp.entity.ELPatientCategory;
import com.palash.healthspringapp.entity.ELVisitType;
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

import cn.pedant.SweetAlert.SweetAlertDialog;
import fr.ganfra.materialspinner.MaterialSpinner;


public class VisitBookActivity extends AppCompatActivity {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapter;
    private DatabaseAdapter.VisitTypeMasterAdapter visitTypeMasterAdapterDB;
    private DatabaseAdapter.DepartmentAdapter departmentAdapterBD;

    private Visit visit;
    private ArrayList<DoctorProfile> doctorProfileArrayList;
    private ArrayList<BookAppointment> bookAppointmentArrayList;
    private ArrayList<ELVisitType> elVisitTypeArrayList;
    private ArrayList<Department> departmentslist;
    private ArrayList<ELDoctorMaster> elDoctorMasterArrayList;

    private TextView visit_book_edt_patient_name;
    private TextView visit_book_edt_patient_contact;
    private TextView visit_book_edt_patient_mail;
    private TextView visit_book_edt_patient_gender;
    private EditText visit_book_edt_date;
    private EditText visit_book_edt_time;
    private MaterialSpinner visit_book_spinner_visit_type;
    private MaterialSpinner visit_book_spinner_department;
    private MaterialSpinner visit_book_spinner_doctor;
    private EditText visit_book_remark_edt;
    private EditText visit_book_reference_doctor_edt;
    private LinearLayout layout_visit_book_spinner_department;
    private LinearLayout layout_visit_book_spinner_doctor;

    private SpinnerAdapter.VisitTypeListAdapter visitTypeListAdapter;
    private SpinnerAdapter.DepartmentAdapter departmentAdapter;
    private SpinnerAdapter.DoctorNameListAdapter doctorNameListAdapter;

    private String CurrentDate;
    private String SelectedVisitTypeID = "";
    private String SelectedServiceID = "";
    private String SelectedDepartmentID = "0";
    private String SelectedDoctorID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_book);
        InitSetting();
        InitView();
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
            databaseContract = new DatabaseContract(context);
            databaseAdapter = new DatabaseAdapter(databaseContract);
            doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();
            bookAppointmentAdapter = databaseAdapter.new BookAppointmentAdapter();
            visitTypeMasterAdapterDB = databaseAdapter.new VisitTypeMasterAdapter();
            departmentAdapterBD = databaseAdapter.new DepartmentAdapter();

            bookAppointmentArrayList = bookAppointmentAdapter.listLast();
            doctorProfileArrayList = doctorProfileAdapter.listAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView() {
        try {
            visit_book_edt_patient_name = (TextView) findViewById(R.id.visit_book_edt_patient_name);
            visit_book_edt_patient_contact = (TextView) findViewById(R.id.visit_book_edt_patient_contact);
            visit_book_edt_patient_mail = (TextView) findViewById(R.id.visit_book_edt_patient_mail);
            visit_book_edt_patient_gender = (TextView) findViewById(R.id.visit_book_edt_patient_gender);
            visit_book_edt_date = (EditText) findViewById(R.id.visit_book_edt_date);
            visit_book_edt_time = (EditText) findViewById(R.id.visit_book_edt_time);
            visit_book_spinner_visit_type = (MaterialSpinner) findViewById(R.id.visit_book_spinner_visit_type);
            visit_book_spinner_department = (MaterialSpinner) findViewById(R.id.visit_book_spinner_department);
            visit_book_spinner_doctor = (MaterialSpinner) findViewById(R.id.visit_book_spinner_doctor);
            visit_book_remark_edt = (EditText) findViewById(R.id.visit_book_remark_edt);
            visit_book_reference_doctor_edt = (EditText) findViewById(R.id.visit_book_reference_doctor_edt);

            layout_visit_book_spinner_department = (LinearLayout) findViewById(R.id.layout_visit_book_spinner_department);
            layout_visit_book_spinner_doctor = (LinearLayout) findViewById(R.id.layout_visit_book_spinner_doctor);

            Bindview();
            disableView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void isLoginAsDoctor() {
        if (doctorProfileArrayList != null && doctorProfileArrayList.size() > 0) {
            if (LocalSetting.isFrontOfficeUser(doctorProfileArrayList.get(0))) {
                layout_visit_book_spinner_department.setVisibility(View.VISIBLE);
                layout_visit_book_spinner_doctor.setVisibility(View.VISIBLE);
            } else {
                layout_visit_book_spinner_doctor.setVisibility(View.GONE);

                //SelectedDepartmentID = doctorProfileArrayList.get(0).getDepartmentID();
                SelectedDoctorID = doctorProfileArrayList.get(0).getDoctorID();
            }
        }
    }

    private void InitAdapter() {
        try {
            elVisitTypeArrayList = visitTypeMasterAdapterDB.listAll();
            if (doctorProfileArrayList != null && doctorProfileArrayList.size() > 0) {
                departmentslist = departmentAdapterBD.listAll(doctorProfileArrayList.get(0).getUnitID());
            }

            if (elVisitTypeArrayList != null && elVisitTypeArrayList.size() > 0) {
                visitTypeListAdapter = new SpinnerAdapter.VisitTypeListAdapter(context, elVisitTypeArrayList);
                visit_book_spinner_visit_type.setAdapter(visitTypeListAdapter);
                visitTypeListAdapter.notifyDataSetChanged();
                visit_book_spinner_visit_type.setSelection(1);

                visit_book_spinner_visit_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int sVisitTypePos = visit_book_spinner_visit_type.getSelectedItemPosition();
                        if (sVisitTypePos == 0) {
                            SelectedVisitTypeID = "0";
                            SelectedServiceID = "0";
                        } else if (sVisitTypePos > 0) {
                            sVisitTypePos = sVisitTypePos - 1;
                            SelectedVisitTypeID = elVisitTypeArrayList.get(sVisitTypePos).getID();
                            SelectedServiceID = elVisitTypeArrayList.get(sVisitTypePos).getServiceID();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                visitTypeListAdapter = new SpinnerAdapter.VisitTypeListAdapter(context, elVisitTypeArrayList);
                visit_book_spinner_visit_type.setAdapter(visitTypeListAdapter);
            }

            if (departmentslist != null && departmentslist.size() > 0) {
                departmentAdapter = new SpinnerAdapter.DepartmentAdapter(context, departmentslist);
                visit_book_spinner_department.setAdapter(departmentAdapter);
                departmentAdapter.notifyDataSetChanged();
                visit_book_spinner_department.setSelection(1);

                visit_book_spinner_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int sVisitTypePos = visit_book_spinner_department.getSelectedItemPosition();
                        if (sVisitTypePos == 0) {
                            SelectedDepartmentID = "0";
                        } else if (sVisitTypePos > 0) {
                            sVisitTypePos = sVisitTypePos - 1;
                            SelectedDepartmentID = departmentslist.get(sVisitTypePos).getID();
                        }
                        if (!SelectedDepartmentID.equals("0")) {
                            new VisitDoctorScheduleTask().execute();
                        } else {
                            elDoctorMasterArrayList = new ArrayList<ELDoctorMaster>();
                            doctorNameListAdapter = new SpinnerAdapter.DoctorNameListAdapter(context, elDoctorMasterArrayList);
                            visit_book_spinner_doctor.setAdapter(doctorNameListAdapter);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                departmentAdapter = new SpinnerAdapter.DepartmentAdapter(context, departmentslist);
                visit_book_spinner_department.setAdapter(departmentAdapter);
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

            if (bookAppointmentArrayList.get(0).getGenderID() != null && bookAppointmentArrayList.get(0).getGenderID().length() > 0) {
                if (bookAppointmentArrayList.get(0).getGenderID().equals("1") || bookAppointmentArrayList.get(0).getGenderID().equals("True")) {
                    visit_book_edt_patient_gender.setText("Male");
                } else if (bookAppointmentArrayList.get(0).getGenderID().equals("2")) {
                    visit_book_edt_patient_gender.setText("Female");
                } else if (bookAppointmentArrayList.get(0).getGenderID().equals("3")) {
                    visit_book_edt_patient_gender.setText("Other");
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
            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("Do you really want to book visit?")
                    .setConfirmText("Yes")
                    .setCancelText("No")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            if (localSetting.isNetworkAvailable(context)) {
                                visit = new Visit();
                                visit.setUnitID(bookAppointmentArrayList.get(0).getDoctorUnitID());
                                visit.setPatientID(bookAppointmentArrayList.get(0).getPatientID());
                                visit.setPatientUnitID(bookAppointmentArrayList.get(0).getUnitID());
                                visit.setVisitTypeID(SelectedVisitTypeID);
                                visit.setVisitTypeServiceID(SelectedServiceID);
                                visit.setDepartmentID(SelectedDepartmentID);
                                visit.setDoctorID(SelectedDoctorID);
                                visit.setReferredDoctor(visit_book_reference_doctor_edt.getText().toString());
                                visit.setComplaints(visit_book_remark_edt.getText().toString());
                                visit.setAddedDateTime(CurrentDate);
                                visit.setVisitDateTime(CurrentDate);
                                visit.setAddedBy(bookAppointmentArrayList.get(0).getID());
                                new VisitBookTask().execute();
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setCancelClickListener(null)
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
        if (SelectedVisitTypeID.equals("0")) {
            Validate.Msgshow(context, "Please select visit type.");
            return false;
        } else if (SelectedDepartmentID.equals("0")) {
            Validate.Msgshow(context, "Please select department.");
            return false;
        } else if (SelectedDoctorID.equals("0")) {
            Validate.Msgshow(context, "Please select doctor.");
            return false;
        }

        return true;
    }

    public class VisitDoctorScheduleTask extends AsyncTask<Void, Void, String> {
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
                        visit_book_spinner_doctor.setAdapter(doctorNameListAdapter);
                        doctorNameListAdapter.notifyDataSetChanged();

                        visit_book_spinner_doctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                int sCategoryL3Pos = visit_book_spinner_doctor.getSelectedItemPosition();
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
                        doctorNameListAdapter = new SpinnerAdapter.DoctorNameListAdapter(context, elDoctorMasterArrayList);
                        visit_book_spinner_doctor.setAdapter(doctorNameListAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (responseCode == Constants.HTTP_NO_RECORD_FOUND_OK_204) {
                SelectedDoctorID = "0";
                doctorNameListAdapter = new SpinnerAdapter.DoctorNameListAdapter(context, elDoctorMasterArrayList);
                visit_book_spinner_doctor.setAdapter(doctorNameListAdapter);
            } else {
                localSetting.alertbox(context, localSetting.handleError(responseCode), false);
            }
            super.onPostExecute(result);
        }
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
                serviceConsumer = new WebServiceConsumer(context, null, null, null);
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
                localSetting.hideDialog(progressDialog);
                if (responseCode == Constants.HTTP_OK_200) {
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getResources().getString(R.string.app_name))
                            .setContentText("Visit booked successfully.")
                            .setConfirmText("Go to Queue")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    startActivity(new Intent(context, PatientQueueActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    finish();
                                }
                            })
                            .setCancelClickListener(null)
                            .show();
                } else {
                    localSetting.showErrorAlert(context, context.getResources().getString(R.string.opps_alert), localSetting.handleError(responseCode));
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