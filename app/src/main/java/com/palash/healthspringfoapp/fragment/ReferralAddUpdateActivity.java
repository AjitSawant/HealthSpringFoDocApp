package com.palash.healthspringfoapp.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.buzzbox.mob.android.scheduler.SchedulerManager;
import com.palash.healthspringfoapp.R;
import com.palash.healthspringfoapp.activity.EMRNavigationDrawerActivity;
import com.palash.healthspringfoapp.adapter.SpinnerAdapter;
import com.palash.healthspringfoapp.api.JsonObjectMapper;
import com.palash.healthspringfoapp.api.WebServiceConsumer;
import com.palash.healthspringfoapp.database.DatabaseAdapter;
import com.palash.healthspringfoapp.database.DatabaseContract;
import com.palash.healthspringfoapp.entity.BookAppointment;
import com.palash.healthspringfoapp.entity.Department;
import com.palash.healthspringfoapp.entity.DoctorProfile;
import com.palash.healthspringfoapp.entity.Flag;
import com.palash.healthspringfoapp.entity.ReferralDoctorPerService;
import com.palash.healthspringfoapp.entity.ServiceName;
import com.palash.healthspringfoapp.task.MasterTask;
import com.palash.healthspringfoapp.utilities.Constants;
import com.palash.healthspringfoapp.utilities.LocalSetting;
import com.palash.healthspringfoapp.utilities.TransparentProgressDialog;
import com.palash.healthspringfoapp.utilities.Validate;
import com.squareup.okhttp.Response;

import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;

public class ReferralAddUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.MasterFlagAdapter masterFlagAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapterDB;
    private DatabaseAdapter.CPOEServiceAdapter cpoeServiceAdapterDB;
    private DatabaseAdapter.DepartmentAdapter departmentAdapterDB;
    private DatabaseAdapter.ReferralServiceListDBAdapter referralServiceListDBAdapter;

    private ReferralDoctorPerService elReferralDoctorPerService;
    private Flag masterflag;
    private ArrayList<DoctorProfile> doctorProfileList;
    private ArrayList<BookAppointment> bookAppointmentArrayList;
    private ArrayList<ServiceName> referralServiceNameArrayList;
    private ArrayList<Department> departmentArrayList;
    private ArrayList<ReferralDoctorPerService> referralDoctorPerServiceArrayList = new ArrayList<>();

    private SpinnerAdapter.ServiceNameAdapter serviceNameAdapter;
    private SpinnerAdapter.DepartmentAdapter departmentAdapter;
    private SpinnerAdapter.ReferralDoctorPerServiceAdapter referralDoctorPerServiceAdapter;

    private MaterialSpinner referral_spinner_department;
    private MaterialSpinner referral_spinner_doctor;
    private AutoCompleteTextView referral_edt_name;
    private EditText referral_edt_rate;
    private TextView service_name_spinner_search_btn;
    private TextInputLayout no_doctor_alert;

    private String UnitID = "";
    private String serviceID = "";
    private String serviceName = "";
    private String DeptID = "";
    private String DeptName = "";
    private String ReferralDoctorID = "";
    private String ReferralDoctorName = "";
    private String isUpdate = "";
    private String SearchText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_add_update);
        InitSetting();
        MasterFlagTask();
        InitiView();
        SetSpinnerAdapter();

        isUpdate = getIntent().getStringExtra("isUpdate");
        if (isUpdate.equals("Yes")) {
            LoadUpdateRecordData();
        }
    }

    private void InitSetting() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        context = this;
        localSetting = new LocalSetting();
        databaseContract = new DatabaseContract(context);
        databaseAdapter = new DatabaseAdapter(databaseContract);
        masterFlagAdapter = databaseAdapter.new MasterFlagAdapter();
        doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();
        bookAppointmentAdapterDB = databaseAdapter.new BookAppointmentAdapter();
        bookAppointmentArrayList = bookAppointmentAdapterDB.listLast();
        cpoeServiceAdapterDB = databaseAdapter.new CPOEServiceAdapter();
        departmentAdapterDB = databaseAdapter.new DepartmentAdapter();
        referralServiceListDBAdapter = databaseAdapter.new ReferralServiceListDBAdapter();

        elReferralDoctorPerService = new ReferralDoctorPerService();
        doctorProfileList = doctorProfileAdapter.listAll();
        if (doctorProfileList != null && doctorProfileList.size() > 0) {
            UnitID = doctorProfileAdapter.listAll().get(0).getUnitID();
        }
    }

    private void MasterFlagTask() {
        masterflag = masterFlagAdapter.listCurrent();
        masterflag.setFlag(Constants.EMR_DEPARTMENT_MASTER_TASK);
        masterFlagAdapter.create(masterflag);
        SchedulerManager.getInstance().runNow(context, MasterTask.class, 1);
    }

    private void LoadUpdateRecordData() {
        referralDoctorPerServiceArrayList = referralServiceListDBAdapter.CurrentUpdateNotes();
        if (referralDoctorPerServiceArrayList != null && referralDoctorPerServiceArrayList.size() > 0) {
            elReferralDoctorPerService = referralDoctorPerServiceArrayList.get(0);
            serviceID = elReferralDoctorPerService.getServiceID();
            serviceName = elReferralDoctorPerService.getServiceName();
            referral_edt_name.setText(elReferralDoctorPerService.getServiceName());
            referral_edt_rate.setText(elReferralDoctorPerService.getRate());

            referralServiceListDBAdapter.removeCurrentUpdateNotes();
        }
    }

    private void InitiView() {
        referral_edt_name = (AutoCompleteTextView) findViewById(R.id.referral_edt_name);
        referral_edt_rate = (EditText) findViewById(R.id.referral_edt_rate);
        referral_spinner_department = (MaterialSpinner) findViewById(R.id.referral_spinner_department);
        referral_spinner_doctor = (MaterialSpinner) findViewById(R.id.referral_spinner_doctor);
        service_name_spinner_search_btn = (TextView) findViewById(R.id.service_name_spinner_search_btn);
        no_doctor_alert = (TextInputLayout) findViewById(R.id.no_doctor_alert);
        no_doctor_alert.setVisibility(View.GONE);
        referral_spinner_doctor.setVisibility(View.GONE);

        service_name_spinner_search_btn.setOnClickListener(this);
    }

    private void SetSpinnerAdapter() {
        try {
            if (doctorProfileList != null && doctorProfileList.size() > 0) {
                departmentArrayList = departmentAdapterDB.listAll(doctorProfileAdapter.listAll().get(0).getUnitID());
            }

            referralServiceNameArrayList = EMRNavigationDrawerActivity.referralServiceNameArrayList;
            if (referralServiceNameArrayList != null && referralServiceNameArrayList.size() > 0) {

                serviceNameAdapter = new SpinnerAdapter.ServiceNameAdapter(context, referralServiceNameArrayList);
                referral_edt_name.setAdapter(serviceNameAdapter);
                referral_edt_name.setThreshold(1);
                serviceNameAdapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        referral_edt_name.showDropDown();
                    }
                }, 1000);

                referral_edt_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (referralServiceNameArrayList != null && referralServiceNameArrayList.size() > 0) {
                            serviceNameAdapter.getFilter().filter(s.toString());
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                referral_edt_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d(Constants.TAG + ":" + "ServicePosition : ", "" + position);
                        Log.d(Constants.TAG + ":" + "ServiceID : ", "" + referralServiceNameArrayList.get(position).getID());
                        Log.d(Constants.TAG + ":" + "ServiceName : ", "" + referralServiceNameArrayList.get(position).getDescription());
                        Log.d(Constants.TAG + ":" + "ServiceRate : ", "" + referralServiceNameArrayList.get(position).getBaseServiceRate());

                        serviceID = referralServiceNameArrayList.get(position).getID();
                        serviceName = referralServiceNameArrayList.get(position).getDescription();
                        referral_edt_name.setText(serviceName);
                        referral_edt_rate.setText(referralServiceNameArrayList.get(position).getBaseServiceRate());
                    }
                });
            }

            if (departmentArrayList != null && departmentArrayList.size() > 0) {
                departmentAdapter = new SpinnerAdapter.DepartmentAdapter(context, departmentArrayList);
                referral_spinner_department.setAdapter(departmentAdapter);
                departmentAdapter.notifyDataSetChanged();
                referral_spinner_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        int position = referral_spinner_department.getSelectedItemPosition();
                        if (position > 0) {
                            position = position - 1;
                        }

                        if (!serviceID.equals("")) {
                            if (departmentArrayList != null && departmentArrayList.size() > 0) {
                                Log.d(Constants.TAG + "" + "DeptPosition : ", "" + position);
                                Log.d(Constants.TAG + "" + "DeptID : ", "" + departmentArrayList.get(position).getID());
                                Log.d(Constants.TAG + "" + "DeptName : ", "" + departmentArrayList.get(position).getDescription());

                                DeptID = departmentArrayList.get(position).getID();
                                DeptName = departmentArrayList.get(position).getDescription();

                                if (localSetting.isNetworkAvailable(context)) {
                                    new GetReferralDoctorListTask().execute();
                                } else {
                                    Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(context, "Please select service", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });
            }else{
                departmentAdapter = new SpinnerAdapter.DepartmentAdapter(context, departmentArrayList);
                referral_spinner_department.setAdapter(departmentAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateControls() {
        if (serviceName.equals("") || referral_edt_name.getText().toString().equals("null") || referral_edt_name.getText().toString().trim().equals("")
                || referral_edt_name.getText().toString().trim().length() == 0) {
            Validate.Msgshow(context, "Please enter service name.");
            return false;
        } else if (referral_edt_rate.getText().toString().equals("null") || referral_edt_rate.getText().toString().trim().equals("")
                || referral_edt_rate.getText().toString().trim().length() == 0) {
            Validate.Msgshow(context, "Please enter service name.");
            return false;
        } else if (DeptID.equals("") || !Validate.isValidOption(referral_spinner_department.getSelectedItemPosition())) {
            Validate.Msgshow(context, "Please select department.");
            return false;
        } else if (ReferralDoctorID.equals("") || !Validate.isValidOption(referral_spinner_doctor.getSelectedItemPosition())) {
            Validate.Msgshow(context, "Please select referral doctor.");
            return false;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.menu_toolbar_save).setVisible(true);
        menu.findItem(R.id.menu_toolbar_cancle).setVisible(true);
        menu.findItem(R.id.menu_toolbar_refresh).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    private void Clear() {
        referral_edt_name.setText("");
        referral_edt_rate.setText("");
        referral_spinner_department.setSelection(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.service_name_spinner_search_btn:
                if (referral_edt_name.getText().toString() != null && referral_edt_name.getText().toString().length() > 0) {
                    SearchText = referral_edt_name.getText().toString();
                    if (localSetting.isNetworkAvailable(context)) {
                        new GetServiceNameListTask().execute();
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Constants.backFromAddEMR = true;
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toolbar_save:
                if (validateControls()) {
                    if (isUpdate.equals("Yes")) {
                        ReferralDoctorServiceUpdateBindView();
                    } else {
                        ReferralDoctorServiceAddBindView();
                    }
                }
                return true;
            case R.id.menu_toolbar_cancle:
                Clear();
                return true;
            case R.id.menu_toolbar_refresh:
                new GetReferralDoctorListTask().execute();
                return true;
            case android.R.id.home:
                Constants.backFromAddEMR = true;
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void ReferralDoctorServiceAddBindView() {
        try {
            new AlertDialog
                    .Builder(context)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Do you really want to add  referral doctor service?")
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            elReferralDoctorPerService = new ReferralDoctorPerService();
                            elReferralDoctorPerService.setUnitID(bookAppointmentArrayList.get(0).getDoctorUnitID());
                            elReferralDoctorPerService.setPatientID(bookAppointmentArrayList.get(0).getPatientID());
                            elReferralDoctorPerService.setPatientUnitID(bookAppointmentArrayList.get(0).getUnitID());
                            elReferralDoctorPerService.setVisitId(bookAppointmentArrayList.get(0).getVisitID());
                            elReferralDoctorPerService.setDoctorID(bookAppointmentArrayList.get(0).getDoctorID());
                            elReferralDoctorPerService.setServiceID(serviceID);
                            elReferralDoctorPerService.setServiceName(serviceName);
                            elReferralDoctorPerService.setReferralDoctorID(ReferralDoctorID);
                            elReferralDoctorPerService.setReferralDoctorName(ReferralDoctorName);
                            elReferralDoctorPerService.setRate(referral_edt_rate.getText().toString());
                            elReferralDoctorPerService.setIsSync("1");
                            callToWebservice();
                            //new ReferralDoctorAddUpdateTask().execute();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(R.mipmap.ic_launcher)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ReferralDoctorServiceUpdateBindView() {
        try {
            new AlertDialog
                    .Builder(context)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Do you really want to update referral doctor service?")
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            elReferralDoctorPerService.setServiceID(serviceID);
                            elReferralDoctorPerService.setServiceName(serviceName);
                            elReferralDoctorPerService.setReferralDoctorID(ReferralDoctorID);
                            elReferralDoctorPerService.setReferralDoctorName(ReferralDoctorName);
                            elReferralDoctorPerService.setRate(referral_edt_rate.getText().toString());
                            elReferralDoctorPerService.setIsSync("1");
                            callToWebservice();
                            //new ReferralDoctorAddUpdateTask().execute();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(R.mipmap.ic_launcher)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callToWebservice() {
        if (localSetting.isNetworkAvailable(context)) {
            new ReferralDoctorAddUpdateTask().execute();
        } else {
            new AlertDialog
                    .Builder(context)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage(context.getResources().getString(R.string.offline_net_alert))
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (isUpdate.equals("Yes")) {
                                referralServiceListDBAdapter.updateUnSync(elReferralDoctorPerService);
                            } else {
                                referralServiceListDBAdapter.createUnSync(elReferralDoctorPerService);
                            }
                            Clear();
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(R.mipmap.ic_launcher)
                    .show();
        }
    }

    public class GetServiceNameListTask extends AsyncTask<Void, Void, String> {
        private int responseCode = 0;
        private TransparentProgressDialog progressDialog = null;
        private JsonObjectMapper objMapper = null;
        private WebServiceConsumer serviceConsumer = null;
        private Response response = null;
        private String responseMSG = "";
        private String responseString = "";

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                objMapper = new JsonObjectMapper();
                serviceConsumer = new WebServiceConsumer(context, null, null, null);
                SearchText = SearchText.replaceAll(" ", "_");
                response = serviceConsumer.GET(Constants.SERVICENAME_URL + "?SearchText=" + SearchText);
                if (response != null) {
                    responseCode = response.code();
                    responseMSG = response.message().toString();
                    responseString = response.body().string();
                    Log.d(Constants.TAG, "Response code:" + responseCode);
                    Log.d(Constants.TAG, "Response MSG:" + responseMSG);
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
                    EMRNavigationDrawerActivity.referralServiceNameArrayList = objMapper.map(responseString, ServiceName.class);
                    SetSpinnerAdapter();
                } else if (responseCode == Constants.HTTP_NO_RECORD_FOUND_OK_204) {
                    localSetting.alertbox(context, "No data available for given search. Please try again", false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }

    public class GetReferralDoctorListTask extends AsyncTask<Void, Void, String> {
        private int responseCode = 0;
        private TransparentProgressDialog progressDialog = null;
        private JsonObjectMapper objMapper = null;
        private WebServiceConsumer serviceConsumer = null;
        private Response response = null;
        private String responseMSG = "";
        private String responseString = "";

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                objMapper = new JsonObjectMapper();
                serviceConsumer = new WebServiceConsumer(context, null, null, null);
                response = serviceConsumer.GET(Constants.REFERRAL_DOCTOR_LIST_MASTER_PER_SERVICE_URL + "?UnitID=" + UnitID + "&DepartmentID=" + DeptID + "&ServiceID=" + serviceID);
                if (response != null) {
                    responseCode = response.code();
                    responseMSG = response.message().toString();
                    responseString = response.body().string();
                    Log.d(Constants.TAG, "Response code:" + responseCode);
                    Log.d(Constants.TAG, "Response MSG:" + responseMSG);
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
                    referralDoctorPerServiceArrayList = objMapper.map(responseString, ReferralDoctorPerService.class);
                    referralDoctorPerServiceAdapter = new SpinnerAdapter.ReferralDoctorPerServiceAdapter(context, referralDoctorPerServiceArrayList);
                    referral_spinner_doctor.setAdapter(referralDoctorPerServiceAdapter);
                    referralDoctorPerServiceAdapter.notifyDataSetChanged();
                    no_doctor_alert.setVisibility(View.GONE);
                    referral_spinner_doctor.setVisibility(View.VISIBLE);

                    referral_spinner_doctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                   int arg2, long arg3) {
                            int position = referral_spinner_doctor.getSelectedItemPosition();
                            if (position > 0) {
                                position = position - 1;
                            }
                            Log.d(Constants.TAG + "" + "Position : ", "" + position);
                            Log.d(Constants.TAG + "" + "ReferralDoctorID : ", "" + referralDoctorPerServiceArrayList.get(position).getReferralDoctorID());
                            Log.d(Constants.TAG + "" + "ReferralDoctorName : ", "" + referralDoctorPerServiceArrayList.get(position).getReferralDoctorName());

                            ReferralDoctorID = referralDoctorPerServiceArrayList.get(position).getReferralDoctorID();
                            ReferralDoctorName = referralDoctorPerServiceArrayList.get(position).getReferralDoctorName();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });
                } else if (responseCode == Constants.HTTP_NO_RECORD_FOUND_OK_204) {
                    no_doctor_alert.setVisibility(View.VISIBLE);
                    referral_spinner_doctor.setVisibility(View.GONE);
                    ReferralDoctorID = "";
                    ReferralDoctorName = "";
                    referralDoctorPerServiceArrayList = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }

    public class ReferralDoctorAddUpdateTask extends AsyncTask<Void, Void, String> {

        private int responseCode = 0;
        private TransparentProgressDialog progressDialog = null;
        private JsonObjectMapper objMapper = null;
        private WebServiceConsumer serviceConsumer = null;
        private Response response = null;
        private String jSonData = "";
        private String responseMSG = "";
        private String responseString = "";

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                objMapper = new JsonObjectMapper();
                jSonData = objMapper.unMap(elReferralDoctorPerService);
                serviceConsumer = new WebServiceConsumer(context, null, null, null);
                response = serviceConsumer.POST(Constants.REFERRAL_DOCTOR_ADD_UPDATE_PER_SERVICE_URL, jSonData);
                if (response != null) {
                    responseCode = response.code();
                    responseMSG = response.message().toString();
                    responseString = response.body().string();
                    Log.d(Constants.TAG, "Response code:" + responseCode);
                    Log.d(Constants.TAG, "Response MSG:" + responseMSG);
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
                if (responseCode == Constants.HTTP_CREATED_201 && responseMSG.equals("Created")) {
                    ArrayList<ReferralDoctorPerService> referralServiceList = objMapper.map(responseString, ReferralDoctorPerService.class);
                    if (referralServiceList != null && referralServiceList.size() > 0) {
                        for (int index = 0; index < referralServiceList.size(); index++) {
                            referralServiceListDBAdapter.create(referralServiceList.get(index));
                        }
                    }
                    Toast.makeText(context, "Referral Service added successfully.", Toast.LENGTH_SHORT).show();
                    Clear();
                } else if (responseCode == Constants.HTTP_OK_200 && responseMSG.equals("OK")) {
                    ArrayList<ReferralDoctorPerService> referralServiceList = objMapper.map(responseString, ReferralDoctorPerService.class);
                    if (referralServiceList != null && referralServiceList.size() > 0) {
                        for (int index = 0; index < referralServiceList.size(); index++) {
                            referralServiceListDBAdapter.create(referralServiceList.get(index));
                        }
                    }
                    Toast.makeText(context, "Referral Service updated successfully.", Toast.LENGTH_SHORT).show();
                    Clear();
                } else {
                    //localSetting.alertbox(context, localSetting.handleError(responseCode), false);
                    new AlertDialog
                            .Builder(context)
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage(context.getResources().getString(R.string.offline_alert))
                            .setCancelable(true)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (isUpdate.equals("Yes")) {
                                        referralServiceListDBAdapter.updateUnSync(elReferralDoctorPerService);
                                    } else {
                                        referralServiceListDBAdapter.createUnSync(elReferralDoctorPerService);
                                    }
                                    Clear();
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.mipmap.ic_launcher)
                            .show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }
}
