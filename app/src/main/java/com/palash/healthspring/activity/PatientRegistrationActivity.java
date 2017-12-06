package com.palash.healthspring.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.buzzbox.mob.android.scheduler.SchedulerManager;
import com.palash.healthspring.R;
import com.palash.healthspring.adapter.SpinnerAdapter;
import com.palash.healthspring.api.JsonObjectMapper;
import com.palash.healthspring.api.WebServiceConsumer;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.BloodGroup;
import com.palash.healthspring.entity.DoctorProfile;
import com.palash.healthspring.entity.Flag;
import com.palash.healthspring.entity.Gender;
import com.palash.healthspring.entity.MaritalStatus;
import com.palash.healthspring.entity.Patient;
import com.palash.healthspring.entity.Prefix;
import com.palash.healthspring.task.MasterTask;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;
import com.palash.healthspring.utilities.TransparentProgressDialog;
import com.palash.healthspring.utilities.Validate;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by manishas on 5/20/2016.
 */
public class PatientRegistrationActivity extends AppCompatActivity {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    //private DatabaseAdapter.PrefixAdapter prefixAdapter;
    //private DatabaseAdapter.GenderAdapter genderAdapter;
    //private DatabaseAdapter.MaritalStatusAdapter maritalStatusAdapter;
    //private DatabaseAdapter.BloodGroupAdapter bloodGroupAdapter;
    private DatabaseAdapter.MasterFlagAdapter masterFlagAdapter;

    private Patient patient;
    private Flag flag;
    private ArrayList<DoctorProfile> listProfile;
    private ArrayList<Prefix> listPrefix;
    private ArrayList<Gender> listGender;
    private ArrayList<MaritalStatus> listMaritalStatus;
    private ArrayList<BloodGroup> listbloodGroups;

    private MaterialSpinner patient_reg_spinner_perfix;
    private MaterialSpinner patient_reg_spinner_gender;
    private MaterialSpinner patient_reg_spinner_maritalstatus;
    private MaterialSpinner patient_reg_spinner_bloodgroup;
    private EditText patient_reg_edt_fname;
    private EditText patient_reg_edt_mname;
    private EditText patient_reg_edt_lname;
    private EditText patient_reg_edt_dob;
    private EditText patient_reg_edt_education;
    private EditText patient_reg_edt_email;
    private EditText patient_reg_edt_mobile;

    private SpinnerAdapter.GenderAdapter getGenderArrayAdapter;
    private SpinnerAdapter.PerfixAdapter getPerfixArrayAdapter;
    private SpinnerAdapter.MaritalStatusAdapter getMaritalStatusArrayAdapter;
    private SpinnerAdapter.BloodGroupAdapter getBloodGroupArrayAdapter;

    private String jSonData = "";
    private DatePickerDialog.OnDateSetListener dateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);
        InitSetting();
        MasterFlagTask();
        InitView();
    }

    private void InitSetting() {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            context = PatientRegistrationActivity.this;
            localSetting = new LocalSetting();
            databaseContract = new DatabaseContract(context);
            databaseAdapter = new DatabaseAdapter(databaseContract);
            masterFlagAdapter = databaseAdapter.new MasterFlagAdapter();
            doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();
            //genderAdapter = databaseAdapter.new GenderAdapter();
            //maritalStatusAdapter = databaseAdapter.new MaritalStatusAdapter();
            //prefixAdapter = databaseAdapter.new PrefixAdapter();
            //bloodGroupAdapter = databaseAdapter.new BloodGroupAdapter();
            listProfile = doctorProfileAdapter.listAll();
            //listGender = genderAdapter.listAll();
            //listMaritalStatus = maritalStatusAdapter.listAll();
            //listPrefix = prefixAdapter.listAll();
            //listbloodGroups = bloodGroupAdapter.listAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView() {
        try {
            patient_reg_edt_fname = (EditText) findViewById(R.id.patient_reg_edt_fname);
            patient_reg_edt_mname = (EditText) findViewById(R.id.patient_reg_edt_mname);
            patient_reg_edt_lname = (EditText) findViewById(R.id.patient_reg_edt_lname);
            patient_reg_edt_dob = (EditText) findViewById(R.id.patient_reg_edt_dob);
            patient_reg_edt_education = (EditText) findViewById(R.id.patient_reg_edt_education);
            patient_reg_edt_email = (EditText) findViewById(R.id.patient_reg_email);
            patient_reg_edt_mobile = (EditText) findViewById(R.id.patient_reg_mobile);
            patient_reg_spinner_perfix = (MaterialSpinner) findViewById(R.id.patient_reg_spinnerperfix);
            patient_reg_spinner_gender = (MaterialSpinner) findViewById(R.id.patient_reg_spinner_gender);
            patient_reg_spinner_maritalstatus = (MaterialSpinner) findViewById(R.id.patient_reg_spinner_maritalstatus);
            patient_reg_spinner_bloodgroup = (MaterialSpinner) findViewById(R.id.patient_reg_spinner_bloodgroup);
            patient_reg_edt_dob.setFocusable(false);
            patient_reg_edt_dob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog dialog = new DatePickerDialog(context, dateListener, Calendar.getInstance().get(Calendar.YEAR),
                            Calendar.getInstance().get(Calendar.MONTH),
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    dialog.getDatePicker().setMaxDate(new Date().getTime());
                    dialog.show();
                }
            });
            dateListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    patient_reg_edt_dob.setText(day + "/" + (month + 1) + "/" + year);
                }
            };
            InitAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Constants.refreshPatient = false;
        finish();
        super.onBackPressed();
    }

    private void MasterFlagTask() {
        flag = masterFlagAdapter.listCurrent();
        flag.setFlag(Constants.PATIENT_REGISTRATION_TASK);
        masterFlagAdapter.create(flag);
        SchedulerManager.getInstance().runNow(context, MasterTask.class, 1);
    }

    private void InitAdapter() {
        try {
            if (listGender != null && listGender.size() > 0) {
                getGenderArrayAdapter = new SpinnerAdapter.GenderAdapter(context, listGender);
                patient_reg_spinner_gender.setAdapter(getGenderArrayAdapter);
                getGenderArrayAdapter.notifyDataSetChanged();
            }

            if (listPrefix != null && listPrefix.size() > 0) {
                getPerfixArrayAdapter = new SpinnerAdapter.PerfixAdapter(context, listPrefix);
                patient_reg_spinner_perfix.setAdapter(getPerfixArrayAdapter);
                getPerfixArrayAdapter.notifyDataSetChanged();
            }

            if (listMaritalStatus != null && listMaritalStatus.size() > 0) {
                getMaritalStatusArrayAdapter = new SpinnerAdapter.MaritalStatusAdapter(context, listMaritalStatus);
                patient_reg_spinner_maritalstatus.setAdapter(getMaritalStatusArrayAdapter);
                getMaritalStatusArrayAdapter.notifyDataSetChanged();
            }

            if (listbloodGroups != null && listbloodGroups.size() > 0) {
                getBloodGroupArrayAdapter = new SpinnerAdapter.BloodGroupAdapter(context, listbloodGroups);
                patient_reg_spinner_bloodgroup.setAdapter(getBloodGroupArrayAdapter);
                getBloodGroupArrayAdapter.notifyDataSetChanged();
            }
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
                    PatientRegistration();
                }
                return true;
            case android.R.id.home:
                Constants.refreshPatient = false;
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean validateControls() {
        if (!Validate.isValidOption(patient_reg_spinner_perfix.getSelectedItemPosition())) {
            Validate.Msgshow(context, "Please select prefix.");
            return false;
        } else if (!Validate.hasTextIn(patient_reg_edt_fname.getText().toString())) {
            Validate.Msgshow(context, "Please enter first name.");
            return false;
        } else if (!Validate.hasTextIn(patient_reg_edt_mname.getText().toString())) {
            Validate.Msgshow(context, "Please enter middle name.");
            return false;
        } else if (!Validate.hasTextIn(patient_reg_edt_lname.getText().toString())) {
            Validate.Msgshow(context, "Please enter last name.");
            return false;
        } else if (!Validate.hasTextIn(patient_reg_edt_dob.getText().toString())) {
            Validate.Msgshow(context, "Please enter birth date.");
            return false;
        } else if (!Validate.hasTextIn(patient_reg_edt_education.getText().toString())) {
            Validate.Msgshow(context, "Please enter education.");
            return false;
        } else if (!Validate.isValidOption(patient_reg_spinner_gender.getSelectedItemPosition())) {
            Validate.Msgshow(context, "Please select Gender.");
            return false;
        } else if (!Validate.isValidOption(patient_reg_spinner_maritalstatus.getSelectedItemPosition())) {
            Validate.Msgshow(context, "Please select Marital Status.");
            return false;
        } else if (!Validate.isValidOption(patient_reg_spinner_bloodgroup.getSelectedItemPosition())) {
            Validate.Msgshow(context, "Please select Blood Group.");
            return false;
        } else if (!Validate.hasTextIn(patient_reg_edt_email.getText().toString())) {
            Validate.Msgshow(context, "Please enter email address.");
            return false;
        } else if (!Validate.isValidEmail(patient_reg_edt_email.getText().toString())) {
            Validate.Msgshow(context, "Invalid email address.");
            return false;
        } else if (!Validate.hasTextIn(patient_reg_edt_mobile.getText().toString())) {
            Validate.Msgshow(context, "Please enter Mobile number.");
            return false;
        } else if (patient_reg_edt_mobile.getText().toString().trim().length() != 10) {
            Validate.Msgshow(context, "Mobile number length must be 10 digit.");
            return false;
        }
        return true;
    }

    private void Clear() {
        patient_reg_edt_fname.setText(null);
        patient_reg_edt_mname.setText(null);
        patient_reg_edt_lname.setText(null);
        patient_reg_edt_dob.setText(null);
        patient_reg_edt_email.setText(null);
        patient_reg_edt_mobile.setText(null);
        patient_reg_edt_education.setText(null);
        patient_reg_spinner_gender.setSelection(0);
        patient_reg_spinner_perfix.setSelection(0);
        patient_reg_spinner_maritalstatus.setSelection(0);
        patient_reg_spinner_perfix.requestFocus();
    }

    private void PatientRegistration() {
        try {
            patient = new Patient();
            patient.setUnitID(listProfile.get(0).getUnitID());
            patient.setFirstName(patient_reg_edt_fname.getText().toString());
            patient.setMiddleName(patient_reg_edt_mname.getText().toString());
            patient.setLastName(patient_reg_edt_lname.getText().toString());
            patient.setEducation(patient_reg_edt_education.getText().toString().trim());
            patient.setDateOfBirth(patient_reg_edt_dob.getText().toString().trim());
            patient.setEmail(patient_reg_edt_email.getText().toString().trim());
            patient.setContactNo1(patient_reg_edt_mobile.getText().toString().trim());
            int Posprefix = patient_reg_spinner_perfix.getSelectedItemPosition();
            if (Posprefix > 0) {
                Posprefix = Posprefix - 1;
            }
            patient.setPrefixId(listPrefix.get(Posprefix).getID());
            int Posgender = patient_reg_spinner_gender.getSelectedItemPosition();
            if (Posgender > 0) {
                Posgender = Posgender - 1;
            }
            patient.setGenderID(listGender.get(Posgender).getID());
            int Posmarital = patient_reg_spinner_maritalstatus.getSelectedItemPosition();
            if (Posmarital > 0) {
                Posmarital = Posmarital - 1;
            }
            patient.setMaritalStatusID(listMaritalStatus.get(Posmarital).getID());
            int Posbloodgroup = patient_reg_spinner_bloodgroup.getSelectedItemPosition();
            if (Posbloodgroup > 0) {
                Posbloodgroup = Posbloodgroup - 1;
            }
            patient.setBloodGroupID(listbloodGroups.get(Posbloodgroup).getID());
            new PatientRegistrationTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class PatientRegistrationTask extends AsyncTask<Void, Void, String> {

        private int responseCode = 0;
        private TransparentProgressDialog progressDialog = null;
        private JsonObjectMapper objMapper = null;
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
                objMapper = new JsonObjectMapper();
                jSonData = objMapper.unMap(patient);
                serviceConsumer = new WebServiceConsumer(context, null, null);
                response = serviceConsumer.POST(Constants.PATIENT_REGISTRATION_URL, jSonData);
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
                if (responseCode == Constants.HTTP_CREATED_201) {
                    localSetting.hideDialog(progressDialog);
                    new AlertDialog
                            .Builder(context)
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage("Patient registered successfully.")
                            .setCancelable(true)
                            .setPositiveButton("Go to Patient List", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    /*flag = flagAdapter.listCurrent();
                                    flag.setFlag(Constants.PATIENT_LIST_TASK);
                                    flagAdapter.create(flag);
                                    SchedulerManager.getInstance().runNow(context, SynchronizationTask.class, 1);*/
                                    Clear();
                                    Constants.refreshPatient = true;
                                    finish();
                                    //startActivity(new Intent(context, PatientListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    //startActivity(new Intent(context, DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
}
