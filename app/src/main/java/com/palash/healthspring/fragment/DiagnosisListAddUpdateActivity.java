package com.palash.healthspring.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.palash.healthspring.adapter.SpinnerAdapter;
import com.palash.healthspring.api.JsonObjectMapper;
import com.palash.healthspring.api.WebServiceConsumer;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.BookAppointment;
import com.palash.healthspring.entity.DaignosisMaster;
import com.palash.healthspring.entity.DaignosisTypeMaster;
import com.palash.healthspring.entity.DiagnosisList;
import com.palash.healthspring.entity.Flag;
import com.palash.healthspring.task.MasterTask;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;
import com.palash.healthspring.utilities.TransparentProgressDialog;
import com.palash.healthspring.utilities.Validate;
import com.squareup.okhttp.Response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by manishas on 7/26/2016.
 */
public class DiagnosisListAddUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.MasterFlagAdapter masterFlagAdapter;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapterDB;
    private DatabaseAdapter.DiagnosisListAdapter diagnosisListAdapterDB;
    //private DatabaseAdapter.DaignosisMasterAdapter daignosisMasterAdapterDB;
    private DatabaseAdapter.DaignosisTypeMasterAdapter daignosisTypeMasterAdapterDB;
    private SpinnerAdapter.DaignosisTypeMasterAdapter daignosisTypeMasterAdapter;

    private SpinnerAdapter.DaignosisMasterAdapter daignosisMasterAdapter;

    private Flag masterflag;
    private DiagnosisList diagnosisList;
    private ArrayList<BookAppointment> bookAppointmentArrayList;
    private ArrayList<DiagnosisList> diagnosisListArrayList;
    private ArrayList<DaignosisMaster> daignosisMasterArrayListDB;
    private ArrayList<DaignosisTypeMaster> daignosisTypeMasterArrayList;

    private AutoCompleteTextView diagnosis_list_edt_diagnosis_name;
    private EditText diagnosis_list_edt_dignosis_code;
    private EditText diagnosis_list_edt_remark;
    private TextView diagnosis_name_spinner_search_btn;
    private MaterialSpinner diagnosis_list_spinner_diagnosis_type;

    private String Diagnosis_name;
    private String Diagnosis_id;
    private String isUpdate = "";
    private String SearchText = "";
    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat simpleDF;
    private String CurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis_list_add_update);
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
        diagnosisListAdapterDB = databaseAdapter.new DiagnosisListAdapter();
        //daignosisMasterAdapterDB = databaseAdapter.new DaignosisMasterAdapter();
        daignosisTypeMasterAdapterDB = databaseAdapter.new DaignosisTypeMasterAdapter();

        simpleDF = new SimpleDateFormat(Constants.TIME_FORMAT, Locale.US);
    }

    private void MasterFlagTask() {
        masterflag = masterFlagAdapter.listCurrent();
        masterflag.setFlag(Constants.EMR_DIAGNOSIS_MASTER_TASK);
        masterFlagAdapter.create(masterflag);
        SchedulerManager.getInstance().runNow(context, MasterTask.class, 1);
    }

    private void InitiView() {
        diagnosis_list_edt_diagnosis_name = (AutoCompleteTextView) findViewById(R.id.diagnosis_list_edt_diagnosis_name);
        diagnosis_list_spinner_diagnosis_type = (MaterialSpinner) findViewById(R.id.diagnosis_list_spinner_diagnosis_type);
        diagnosis_list_edt_dignosis_code = (EditText) findViewById(R.id.diagnosis_list_edt_dignosis_code);
        diagnosis_list_edt_remark = (EditText) findViewById(R.id.diagnosis_list_edt_remark);
        diagnosis_name_spinner_search_btn = (TextView) findViewById(R.id.diagnosis_name_spinner_search_btn);

        diagnosis_name_spinner_search_btn.setOnClickListener(this);
    }

    private void SetSpinnerAdapter() {
        try {
            //daignosisMasterArrayList = daignosisMasterAdapterDB.listAll();
            daignosisTypeMasterArrayList = daignosisTypeMasterAdapterDB.listAll();

            if (daignosisMasterArrayListDB != null && daignosisMasterArrayListDB.size() > 0) {

                diagnosis_list_edt_diagnosis_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        daignosisMasterAdapter.getFilter().filter(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (daignosisMasterArrayListDB != null && daignosisMasterArrayListDB.size() > 0) {
                            daignosisMasterAdapter.getFilter().filter(s.toString());
                        }
                    }
                });

                diagnosis_list_edt_diagnosis_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (daignosisMasterArrayListDB != null && daignosisMasterArrayListDB.size() > 0) {
                            Log.d(Constants.TAG + "" + "DiagnosisPosition : ", "" + position);
                            Log.d(Constants.TAG + "" + "DiagnosisID : ", "" + daignosisMasterArrayListDB.get(position).getID());
                            Log.d(Constants.TAG + "" + "DiagnosisName : ", "" + daignosisMasterArrayListDB.get(position).getDiagnosis());
                            Log.d(Constants.TAG + "" + "DiagnosisCode : ", "" + daignosisMasterArrayListDB.get(position).getCode());

                            Diagnosis_id = daignosisMasterArrayListDB.get(position).getID();
                            Diagnosis_name = daignosisMasterArrayListDB.get(position).getDiagnosis();
                            diagnosis_list_edt_diagnosis_name.setText(Diagnosis_name);
                            diagnosis_list_edt_dignosis_code.setText(daignosisMasterArrayListDB.get(position).getCode());
                        }
                    }
                });
            }

            if (daignosisTypeMasterArrayList != null && daignosisTypeMasterArrayList.size() > 0) {
                daignosisTypeMasterAdapter = new SpinnerAdapter.DaignosisTypeMasterAdapter(context, daignosisTypeMasterArrayList);
                diagnosis_list_spinner_diagnosis_type.setAdapter(daignosisTypeMasterAdapter);
                daignosisTypeMasterAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateControls() {
        if (Diagnosis_name.equals("") || diagnosis_list_edt_diagnosis_name.getText().toString().equals("null") || diagnosis_list_edt_diagnosis_name.getText().toString().trim().equals("")
                || diagnosis_list_edt_diagnosis_name.getText().toString().trim().length() == 0) {
            Validate.Msgshow(context, "Please enter diagnosis name.");
            return false;
        } else if (diagnosis_list_edt_dignosis_code.getText().toString().equals("null") || diagnosis_list_edt_dignosis_code.getText().toString().trim().equals("")
                || diagnosis_list_edt_dignosis_code.getText().toString().trim().length() == 0) {
            Validate.Msgshow(context, "Please enter diagnosis name.");
            return false;
        } else if (!Validate.isValidOption(diagnosis_list_spinner_diagnosis_type.getSelectedItemPosition())) {
            Validate.Msgshow(context, "Please select diagnosis type.");
            return false;
        }
        /*if (!Validate.hasTextIn(diagnosis_list_edt_remark.getText().toString())) {
            Validate.Msgshow(context, "Please enter diagnosis remark.");
            return false;
        }*/
        return true;
    }

    private void LoadUpdateRecordData() {

        diagnosisListArrayList = diagnosisListAdapterDB.CurrentUpdateNotes();
        if (diagnosisListArrayList != null && diagnosisListArrayList.size() > 0) {
            diagnosisList = diagnosisListArrayList.get(0);
            Diagnosis_id = diagnosisList.getDiagnosisID();
            diagnosis_list_edt_diagnosis_name.setText(diagnosisList.getDiagnosisName());
            Diagnosis_name = diagnosisList.getDiagnosisName();
            diagnosis_list_edt_dignosis_code.setText(diagnosisList.getCode());
            diagnosis_list_edt_remark.setText(diagnosisList.getRemark());
            int posDiagnosisTypeID = 0;
            try {
                posDiagnosisTypeID = Integer.parseInt(diagnosisList.getDiagnosisTypeID());
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
            diagnosis_list_spinner_diagnosis_type.setSelection(posDiagnosisTypeID);

            diagnosisListAdapterDB.removeCurrentUpdateNotes();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.diagnosis_name_spinner_search_btn:
                if (diagnosis_list_edt_diagnosis_name.getText().toString() != null && diagnosis_list_edt_diagnosis_name.getText().toString().length() > 0) {
                    SearchText = diagnosis_list_edt_diagnosis_name.getText().toString();
                    if (localSetting.isNetworkAvailable(context)) {
                        new GetDiagnosisNameListTask().execute();
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Constants.backFromAddEMR=true;
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_current_medication, menu);
        menu.findItem(R.id.menu_current_medication_save).setVisible(true);
        menu.findItem(R.id.menu_current_medication_cancle).setVisible(true);
        menu.findItem(R.id.menu_current_medication_refresh).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_current_medication_save:
                if (validateControls()) {
                    if (isUpdate.equals("Yes")) {
                        DiagnosisListUpDateBindView();
                    } else {
                        DiagnosisListAddBindView();
                    }
                }
                return true;
            case R.id.menu_current_medication_cancle:
                Clear();
                return true;
            case R.id.menu_current_medication_refresh:
                MasterFlagTask();
                SetSpinnerAdapter();
                return true;
            case android.R.id.home:
                Constants.backFromAddEMR=true;
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void Clear() {
        diagnosis_list_edt_diagnosis_name.setText("");
        diagnosis_list_edt_dignosis_code.setText("");
        diagnosis_list_edt_remark.setText("");
        diagnosis_list_spinner_diagnosis_type.setSelection(0);
    }

    private void DiagnosisListAddBindView() {
        try {
            new AlertDialog
                    .Builder(context)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Do you really want to add diagnosis?")
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            diagnosisList = new DiagnosisList();
                            int pos_diagnosis_type = 0;
                            CurrentDate = simpleDF.format(c.getTime());
                            pos_diagnosis_type = diagnosis_list_spinner_diagnosis_type.getSelectedItemPosition();
                            if (pos_diagnosis_type > 0) {
                                pos_diagnosis_type = pos_diagnosis_type - 1;
                            }
                            diagnosisList.setUnitID(bookAppointmentArrayList.get(0).getDoctorUnitID());
                            diagnosisList.setPatientID(bookAppointmentArrayList.get(0).getPatientID());
                            diagnosisList.setPatientUnitID(bookAppointmentArrayList.get(0).getUnitID());
                            diagnosisList.setVisitId(bookAppointmentArrayList.get(0).getVisitID());
                            diagnosisList.setDiagnosisID(Diagnosis_id);
                            diagnosisList.setCode(diagnosis_list_edt_dignosis_code.getText().toString());
                            diagnosisList.setDiagnosisName(Diagnosis_name);
                            diagnosisList.setDiagnosisTypeID(daignosisTypeMasterArrayList.get(pos_diagnosis_type).getID());
                            diagnosisList.setDiagnosisType(daignosisTypeMasterArrayList.get(pos_diagnosis_type).getDescription());
                            if (pos_diagnosis_type == 0) {
                                diagnosisList.setPrimaryDiagnosis("1");
                            } else {
                                diagnosisList.setPrimaryDiagnosis("0");
                            }
                            diagnosisList.setRemark(diagnosis_list_edt_remark.getText().toString());
                            diagnosisList.setAddedBy(bookAppointmentArrayList.get(0).getDoctorID());
                            diagnosisList.setICDId(Diagnosis_id);
                            diagnosisList.setIsSync("1");
                            diagnosisList.setDate(CurrentDate);

                            new DiagnosisListAddUpdateTask().execute();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(R.mipmap.ic_launcher)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DiagnosisListUpDateBindView() {
        try {
            new AlertDialog
                    .Builder(context)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Do you really want to update diagnosis?")
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int pos_diagnosis_type = 0;
                            CurrentDate = simpleDF.format(c.getTime());
                            pos_diagnosis_type = diagnosis_list_spinner_diagnosis_type.getSelectedItemPosition();
                            if (pos_diagnosis_type > 0) {
                                pos_diagnosis_type = pos_diagnosis_type - 1;
                            }
                            //diagnosisList.setID(diagnosisListArrayList.get(0).getID());
                            //diagnosisList.setUnitID(diagnosisListArrayList.get(0).getUnitID());
                            //diagnosisList.setPatientID(diagnosisListArrayList.get(0).getPatientID());
                            //diagnosisList.setPatientUnitID(diagnosisListArrayList.get(0).getPatientUnitID());
                            //diagnosisList.setVisitId(diagnosisListArrayList.get(0).getVisitId());
                            diagnosisList.setDiagnosisID(Diagnosis_id);
                            diagnosisList.setCode(diagnosis_list_edt_dignosis_code.getText().toString());
                            diagnosisList.setDiagnosisName(Diagnosis_name);
                            diagnosisList.setDiagnosisTypeID(daignosisTypeMasterArrayList.get(pos_diagnosis_type).getID());
                            diagnosisList.setDiagnosisType(daignosisTypeMasterArrayList.get(pos_diagnosis_type).getDescription());
                            if (pos_diagnosis_type == 0) {
                                diagnosisList.setPrimaryDiagnosis("1");
                            } else {
                                diagnosisList.setPrimaryDiagnosis("0");
                            }
                            diagnosisList.setRemark(diagnosis_list_edt_remark.getText().toString());
                            diagnosisList.setICDId(Diagnosis_id);
                            diagnosisList.setIsSync("1");
                            diagnosisList.setDate(CurrentDate);

                            new DiagnosisListAddUpdateTask().execute();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(R.mipmap.ic_launcher)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class DiagnosisListAddUpdateTask extends AsyncTask<Void, Void, String> {
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
                jSonData = objMapper.unMap(diagnosisList);
                serviceConsumer = new WebServiceConsumer(context, null, null);
                response = serviceConsumer.POST(Constants.DIAGNOSIS_ADD_UPDATE_URL, jSonData);
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
                    new AlertDialog
                            .Builder(context)
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage("Diagnosis added successfully.")
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
                            .setMessage("Diagnosis updated successfully.")
                            .setCancelable(false)
                            .setPositiveButton("Go to Patient EMR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Clear();
                                    finish();
                                }
                            })
                            .setIcon(R.mipmap.ic_launcher).show();
                } else if (responseCode == Constants.HTTP_Expectation_Failed_417) {
                    localSetting.alertbox(context, "At least One Primary Diagnosis should be selected.", false);
                } else if (responseCode == Constants.HTTP_AMBIGUOUS_300) {
                    localSetting.alertbox(context, "Only one primary Diagnosis allowed.", false);
                } else {
                    localSetting.alertbox(context, localSetting.handleError(responseCode), false);
                    /*new AlertDialog
                            .Builder(context)
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage(context.getResources().getString(R.string.offline_alert))
                            .setCancelable(true)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (isUpdate.equals("Yes")) {
                                        diagnosisListAdapterDB.updateUnSync(diagnosisList);
                                    } else {
                                        diagnosisListAdapterDB.createUnSync(diagnosisList);
                                    }
                                    Clear();
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.mipmap.ic_launcher)
                            .show();*/
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }

    public class GetDiagnosisNameListTask extends AsyncTask<Void, Void, String> {
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
                response = serviceConsumer.GET(Constants.DAIGNOSISMASTER_URL + "?SearchText=" + SearchText);
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
                    daignosisMasterArrayListDB = objMapper.map(responseString, DaignosisMaster.class);
                    daignosisMasterAdapter = new SpinnerAdapter.DaignosisMasterAdapter(context, daignosisMasterArrayListDB);
                    diagnosis_list_edt_diagnosis_name.setAdapter(daignosisMasterAdapter);
                    diagnosis_list_edt_diagnosis_name.setThreshold(1);
                    diagnosis_list_edt_diagnosis_name.showDropDown();
                    daignosisMasterAdapter.notifyDataSetChanged();
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
