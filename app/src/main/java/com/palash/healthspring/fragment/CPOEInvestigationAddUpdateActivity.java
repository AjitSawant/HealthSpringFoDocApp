package com.palash.healthspring.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import com.palash.healthspring.R;
import com.palash.healthspring.activity.EMRNavigationDrawerActivity;
import com.palash.healthspring.adapter.SpinnerAdapter;
import com.palash.healthspring.api.JsonObjectMapper;
import com.palash.healthspring.api.WebServiceConsumer;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.BookAppointment;
import com.palash.healthspring.entity.CPOEService;
import com.palash.healthspring.entity.Department;
import com.palash.healthspring.entity.Flag;
import com.palash.healthspring.entity.Priority;
import com.palash.healthspring.entity.ServiceName;
import com.palash.healthspring.task.MasterTask;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;
import com.palash.healthspring.utilities.TransparentProgressDialog;
import com.palash.healthspring.utilities.Validate;
import com.squareup.okhttp.Response;

import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;


public class CPOEInvestigationAddUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.MasterFlagAdapter masterFlagAdapter;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapterDB;
    private DatabaseAdapter.CPOEServiceAdapter cpoeServiceAdapterDB;
    private DatabaseAdapter.PriorityAdapter priorityAdapterDB;
    private DatabaseAdapter.DepartmentAdapter departmentAdapterDB;

    private CPOEService cpoeService;
    private Flag masterflag;
    private ArrayList<BookAppointment> bookAppointmentArrayList;
    private ArrayList<CPOEService> cpoeServiceArrayList;
    private ArrayList<ServiceName> serviceNameArrayList;
    private ArrayList<Priority> priorityArrayList;
    private ArrayList<Department> departmentArrayList;

    private SpinnerAdapter.PriorityAdapter priorityAdapter;
    private SpinnerAdapter.ServiceNameAdapter serviceNameAdapter;
    private SpinnerAdapter.DepartmentAdapter departmentAdapter;

    private MaterialSpinner cpoeservice_spinner_type;
    private MaterialSpinner cpoeservice_spinner_priority;
    private AutoCompleteTextView cpoeservice_edt_name;
    private EditText cpoeservice_edt_rate;
    private TextView service_name_spinner_search_btn;

    private String serviceID = "";
    private String serviceName = "";
    private String isUpdate = "";
    private String SearchText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copeservice_add_update);
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
        bookAppointmentAdapterDB = databaseAdapter.new BookAppointmentAdapter();
        bookAppointmentArrayList = bookAppointmentAdapterDB.listLast();
        cpoeServiceAdapterDB = databaseAdapter.new CPOEServiceAdapter();
        priorityAdapterDB = databaseAdapter.new PriorityAdapter();
        departmentAdapterDB = databaseAdapter.new DepartmentAdapter();

        cpoeService = new CPOEService();
    }

    private void MasterFlagTask() {
        masterflag = masterFlagAdapter.listCurrent();
        masterflag.setFlag(Constants.EMR_SERVICE_MASTER_TASK);
        masterFlagAdapter.create(masterflag);
        SchedulerManager.getInstance().runNow(context, MasterTask.class, 1);
    }

    private void InitiView() {
        cpoeservice_edt_name = (AutoCompleteTextView) findViewById(R.id.cpoeservice_edt_name);
        cpoeservice_edt_rate = (EditText) findViewById(R.id.cpoeservice_edt_rate);
        service_name_spinner_search_btn = (TextView) findViewById(R.id.service_name_spinner_search_btn);
        cpoeservice_spinner_type = (MaterialSpinner) findViewById(R.id.cpoeservice_spinner_type);
        cpoeservice_spinner_priority = (MaterialSpinner) findViewById(R.id.cpoeservice_spinner_priority);

        service_name_spinner_search_btn.setOnClickListener(this);
    }

    private void SetSpinnerAdapter() {
        try {
            //serviceNameArrayList = serviceNameAdapterDB.listAll();
            priorityArrayList = priorityAdapterDB.listAll();
            departmentArrayList = departmentAdapterDB.listAll();
            serviceNameArrayList = EMRNavigationDrawerActivity.serviceNameArrayList;
            if (serviceNameArrayList != null && serviceNameArrayList.size() > 0) {

                serviceNameAdapter = new SpinnerAdapter.ServiceNameAdapter(context, serviceNameArrayList);
                cpoeservice_edt_name.setAdapter(serviceNameAdapter);
                cpoeservice_edt_name.setThreshold(1);
                serviceNameAdapter.notifyDataSetChanged();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cpoeservice_edt_name.showDropDown();
                    }
                }, 1000);

                cpoeservice_edt_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (serviceNameArrayList != null && serviceNameArrayList.size() > 0) {
                            serviceNameAdapter.getFilter().filter(s.toString());
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                cpoeservice_edt_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (serviceNameArrayList != null && serviceNameArrayList.size() > 0) {
                            Log.d(Constants.TAG + "" + "ServicePosition : ", "" + position);
                            Log.d(Constants.TAG + "" + "ServiceID : ", "" + serviceNameArrayList.get(position).getID());
                            Log.d(Constants.TAG + "" + "ServiceName : ", "" + serviceNameArrayList.get(position).getDescription());
                            Log.d(Constants.TAG + "" + "ServiceRate : ", "" + serviceNameArrayList.get(position).getBaseServiceRate());

                            serviceID = serviceNameArrayList.get(position).getID();
                            serviceName = serviceNameArrayList.get(position).getDescription();
                            cpoeservice_edt_name.setText(serviceName);
                            cpoeservice_edt_rate.setText(serviceNameArrayList.get(position).getBaseServiceRate());
                        }
                    }
                });
            }

            if (priorityArrayList != null && priorityArrayList.size() > 0) {
                priorityAdapter = new SpinnerAdapter.PriorityAdapter(context, priorityArrayList);
                cpoeservice_spinner_priority.setAdapter(priorityAdapter);
                priorityAdapter.notifyDataSetChanged();
            }

            if (departmentArrayList != null && departmentArrayList.size() > 0) {
                departmentAdapter = new SpinnerAdapter.DepartmentAdapter(context, departmentArrayList);
                cpoeservice_spinner_type.setAdapter(departmentAdapter);
                departmentAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateControls() {
        if (serviceName.equals("") || cpoeservice_edt_name.getText().toString().equals("null") || cpoeservice_edt_name.getText().toString().trim().equals("")
                || cpoeservice_edt_name.getText().toString().trim().length() == 0) {
            Validate.Msgshow(context, "Please enter service name.");
            return false;
        } else if (cpoeservice_edt_rate.getText().toString().equals("null") || cpoeservice_edt_rate.getText().toString().trim().equals("")
                || cpoeservice_edt_rate.getText().toString().trim().length() == 0) {
            Validate.Msgshow(context, "Please enter service name.");
            return false;
        } else if (!Validate.isValidOption(cpoeservice_spinner_priority.getSelectedItemPosition())) {
            Validate.Msgshow(context, "Please select priority.");
            return false;
        }

        return true;
    }

    private void LoadUpdateRecordData() {
        cpoeServiceArrayList = cpoeServiceAdapterDB.CurrentUpdateNotes();
        if (cpoeServiceArrayList != null && cpoeServiceArrayList.size() > 0) {
            cpoeService = cpoeServiceArrayList.get(0);
            serviceID = cpoeService.getServiceID();
            serviceName = cpoeService.getServiceName();
            cpoeservice_edt_name.setText(cpoeService.getServiceName());
            cpoeservice_edt_rate.setText(cpoeService.getRate());
            int pos_priority = 0;
            try {
                pos_priority = Integer.parseInt(cpoeServiceArrayList.get(0).getPriority());
            } catch (NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }
            cpoeservice_spinner_priority.setSelection(pos_priority);

            cpoeServiceAdapterDB.removeCurrentUpdateNotes();
        }
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
        cpoeservice_edt_name.setText("");
        cpoeservice_edt_rate.setText("");
        cpoeservice_spinner_priority.setSelection(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.service_name_spinner_search_btn:
                if (cpoeservice_edt_name.getText().toString() != null && cpoeservice_edt_name.getText().toString().length() > 0) {
                    SearchText = cpoeservice_edt_name.getText().toString();
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
                        CPOEServiceUpdateBindView();
                    } else {
                        CPOEServiceAddBindView();
                    }
                }
                return true;
            case R.id.menu_toolbar_cancle:
                Clear();
                return true;
            case R.id.menu_toolbar_refresh:
                MasterFlagTask();
                SetSpinnerAdapter();
                return true;
            case android.R.id.home:
                Constants.backFromAddEMR = true;
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void CPOEServiceUpdateBindView() {
        try {
            new AlertDialog
                    .Builder(context)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Do you really want to update CPOE service?")
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int Pospriority = cpoeservice_spinner_priority.getSelectedItemPosition();
                            if (Pospriority > 0) {
                                Pospriority = Pospriority - 1;
                            }
                            int Postype = cpoeservice_spinner_type.getSelectedItemPosition();
                            if (Postype > 0) {
                                Postype = Postype - 1;
                            }
                            cpoeService.setServiceID(serviceID);
                            cpoeService.setServiceName(serviceName);
                            cpoeService.setPriority(priorityArrayList.get(Pospriority).getID());
                            cpoeService.setPriorityDescription(priorityArrayList.get(Pospriority).getDescription());
                            cpoeService.setRate(cpoeservice_edt_rate.getText().toString());
                            cpoeService.setIsSync("1");
                            callToWebservice();
                            //new cpoeServiceAddUpdateTask().execute();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(R.mipmap.ic_launcher)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CPOEServiceAddBindView() {
        try {
            new AlertDialog
                    .Builder(context)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Do you really want to add CPOE Service?")
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cpoeService = new CPOEService();
                            int Pospriority = cpoeservice_spinner_priority.getSelectedItemPosition();
                            if (Pospriority > 0) {
                                Pospriority = Pospriority - 1;
                            }
                            int Postype = cpoeservice_spinner_type.getSelectedItemPosition();
                            if (Postype > 0) {
                                Postype = Postype - 1;
                            }

                            cpoeService.setUnitID(bookAppointmentArrayList.get(0).getDoctorUnitID());
                            cpoeService.setPatientID(bookAppointmentArrayList.get(0).getPatientID());
                            cpoeService.setPatientUnitID(bookAppointmentArrayList.get(0).getUnitID());
                            cpoeService.setVisitID(bookAppointmentArrayList.get(0).getVisitID());
                            cpoeService.setServiceID(serviceID);
                            cpoeService.setServiceName(serviceName);
                            cpoeService.setDoctorID(bookAppointmentArrayList.get(0).getDoctorID());
                            cpoeService.setPriority(priorityArrayList.get(Pospriority).getID());
                            cpoeService.setPriorityDescription(priorityArrayList.get(Pospriority).getDescription());
                            cpoeService.setRate(cpoeservice_edt_rate.getText().toString());
                            cpoeService.setIsSync("1");
                            callToWebservice();
                            //new cpoeServiceAddUpdateTask().execute();
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
            new cpoeServiceAddUpdateTask().execute();
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
                                cpoeServiceAdapterDB.updateUnSync(cpoeService);
                            } else {
                                cpoeServiceAdapterDB.createUnSync(cpoeService);
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

    public class cpoeServiceAddUpdateTask extends AsyncTask<Void, Void, String> {

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
                jSonData = objMapper.unMap(cpoeService);
                serviceConsumer = new WebServiceConsumer(context, null, null);
                response = serviceConsumer.POST(Constants.CPOESERVICE_ADD_UPDATE_URL, jSonData);
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
                if (responseCode == Constants.HTTP_CREATED_201 && responseMSG.equals("Created")) {
                    localSetting.hideDialog(progressDialog);
                    new AlertDialog
                            .Builder(context)
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage("Service added successfully.")
                            .setCancelable(false)
                            .setPositiveButton("Go to Patient EMR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Clear();
                                    finish();
                                }
                            })
                            .setIcon(R.mipmap.ic_launcher).show();
                } else if (responseCode == Constants.HTTP_OK_200 && responseMSG.equals("OK")) {
                    localSetting.hideDialog(progressDialog);
                    new AlertDialog
                            .Builder(context)
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage("Service updated successfully.")
                            .setCancelable(false)
                            .setPositiveButton("Go to Patient EMR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Clear();
                                    finish();
                                }
                            })
                            .setIcon(R.mipmap.ic_launcher).show();
                } else {
                    localSetting.hideDialog(progressDialog);
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
                                        cpoeServiceAdapterDB.updateUnSync(cpoeService);
                                    } else {
                                        cpoeServiceAdapterDB.createUnSync(cpoeService);
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
                serviceConsumer = new WebServiceConsumer(context, null, null);
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
                    EMRNavigationDrawerActivity.serviceNameArrayList = objMapper.map(responseString, ServiceName.class);

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
}
