package com.palash.healthspring.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.buzzbox.mob.android.scheduler.SchedulerManager;
import com.palash.healthspring.R;
import com.palash.healthspring.adapter.SpinnerAdapter;
import com.palash.healthspring.adapter.VitalsListUpdateAdapter;
import com.palash.healthspring.api.JsonObjectMapper;
import com.palash.healthspring.api.WebServiceConsumer;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.BookAppointment;
import com.palash.healthspring.entity.Flag;
import com.palash.healthspring.entity.Vital;
import com.palash.healthspring.entity.VitalsList;
import com.palash.healthspring.entity.VitalsListSave;
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
 * Created by manishas on 7/27/2016.
 */
public class VitalsAddUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.VitalAdapter vitalAdapterDB;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapterDB;
    private DatabaseAdapter.VitalsListAdapter vitalsListAdapterDB;
    private DatabaseAdapter.MasterFlagAdapter masterFlagAdapter;

    private VitalsList elVitalsList;
    private VitalsListSave elSaveVitalsList;
    private Flag masterflag;
    private ArrayList<BookAppointment> bookAppointmentArrayList;
    private ArrayList<Vital> vitalArrayList = new ArrayList<>();
    private ArrayList<VitalsList> vitalsListArrayList = new ArrayList<>();

    private SpinnerAdapter.VitalAdapter vitalAdapter;
    private VitalsListUpdateAdapter vitalsListAdapter;

    private MaterialSpinner vitals_list_spinner_vitals_name;
    private ListView emr_vitals_list_List;
    private TextView emr_vitals_list_empty;
    private EditText vitals_list_edt_vlaue;
    private EditText vitals_list_edt_unit;
    private EditText vitals_list_edt_minvalue;
    private EditText vitals_list_edt_maxvalue;
    private Button save_vitals_button;
    private Chronometer emr_vitals_list_chronometer;

    private String CurrentDate;
    private String isUpdate = "";
    private String selectedVitalID = "";
    private String selectedVitalDesc = "";

    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat simpleDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_vital);
        InitSetting();
        MasterFlagTask();
        InitView();
        SetSpinnerAdapter();
    }

    private void InitSetting() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        context = this;
        localSetting = new LocalSetting();
        databaseContract = new DatabaseContract(context);
        databaseAdapter = new DatabaseAdapter(databaseContract);
        bookAppointmentAdapterDB = databaseAdapter.new BookAppointmentAdapter();
        vitalAdapterDB = databaseAdapter.new VitalAdapter();
        vitalsListAdapterDB = databaseAdapter.new VitalsListAdapter();
        masterFlagAdapter = databaseAdapter.new MasterFlagAdapter();

        elVitalsList = new VitalsList();
        elSaveVitalsList = new VitalsListSave();
        bookAppointmentArrayList = bookAppointmentAdapterDB.listLast();

        simpleDF = new SimpleDateFormat(Constants.TIME_FORMAT, Locale.US);
    }

    private void MasterFlagTask() {
        masterflag = masterFlagAdapter.listCurrent();
        masterflag.setFlag(Constants.EMR_VITAL_MASTER_TASK);
        masterFlagAdapter.create(masterflag);
        SchedulerManager.getInstance().runNow(context, MasterTask.class, 1);
    }

    private void InitView() {
        try {
            vitals_list_spinner_vitals_name = (MaterialSpinner) findViewById(R.id.vitals_list_spinner_vitals_name);
            emr_vitals_list_List = (ListView) findViewById(R.id.emr_vitals_list_List);
            emr_vitals_list_empty = (TextView) findViewById(R.id.emr_vitals_list_empty);
            vitals_list_edt_vlaue = (EditText) findViewById(R.id.vitals_list_edt_vlaue);
            vitals_list_edt_unit = (EditText) findViewById(R.id.vitals_list_edt_unit);
            vitals_list_edt_minvalue = (EditText) findViewById(R.id.vitals_list_edt_minvalue);
            vitals_list_edt_maxvalue = (EditText) findViewById(R.id.vitals_list_edt_maxvalue);
            save_vitals_button = (Button) findViewById(R.id.save_vitals_button);
            emr_vitals_list_chronometer = (Chronometer) findViewById(R.id.emr_vitals_list_chronometer);

            save_vitals_button.setOnClickListener(this);

            vitals_list_edt_vlaue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        if (s.length() > 0 && vitals_list_edt_minvalue.getText().toString().length() > 0 && vitals_list_edt_maxvalue.getText().toString().length() > 0) {
                            validValue();
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            });

            vitals_list_spinner_vitals_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (vitalArrayList != null && vitalArrayList.size() > 0) {
                        vitals_list_edt_unit.setText(vitalArrayList.get(position).getUnit());
                        vitals_list_edt_minvalue.setText(vitalArrayList.get(position).getMinValue());
                        vitals_list_edt_maxvalue.setText(vitalArrayList.get(position).getMaxValue());
                        selectedVitalID = vitalArrayList.get(position).getID();
                        selectedVitalDesc = vitalArrayList.get(position).getDescription();
                        validValue();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        refreshList(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
        super.onResume();
    }

    @Override
    public void onPause() {
        emr_vitals_list_chronometer.stop();
        super.onPause();
    }

    private void refreshList(String PatientID, String VisitID) {
        vitalsListArrayList = vitalsListAdapterDB.listAll(PatientID, VisitID);
        if (vitalsListArrayList != null && vitalsListArrayList.size() > 0) {
            vitalsListAdapter = new VitalsListUpdateAdapter(context, vitalsListArrayList, PatientID, VisitID);
            emr_vitals_list_List.setAdapter(vitalsListAdapter);
            vitalsListAdapter.notifyDataSetChanged();
            emr_vitals_list_empty.setVisibility(View.GONE);
            emr_vitals_list_List.setVisibility(View.VISIBLE);
        } else {
            emr_vitals_list_empty.setVisibility(View.VISIBLE);
            emr_vitals_list_List.setVisibility(View.GONE);
        }
    }

    private void SetSpinnerAdapter() {
        try {
            vitalArrayList = vitalAdapterDB.listAll();
            if (vitalArrayList != null && vitalArrayList.size() > 0) {
                vitalAdapter = new SpinnerAdapter.VitalAdapter(context, vitalArrayList);
                vitals_list_spinner_vitals_name.setAdapter(vitalAdapter);
                vitalAdapter.notifyDataSetChanged();
                vitals_list_spinner_vitals_name.setSelection(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void LoadUpdateRecordData() {
        vitalsListArrayList = vitalsListAdapterDB.CurrentUpdateNotes();
        if (vitalsListArrayList != null && vitalsListArrayList.size() > 0) {
            elVitalsList = vitalsListArrayList.get(0);
            try {
                vitals_list_edt_vlaue.setText(elVitalsList.getValue());
                for (int i = 0; i < vitalArrayList.size(); i++) {
                    if (vitalArrayList.get(i).getDescription().equals(elVitalsList.getVitalsDecription())) {
                        vitals_list_spinner_vitals_name.setSelection(i);
                    }
                }
                vitalsListAdapterDB.removeCurrentUpdateNotes();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }*/

    private void validValue() {
        if (vitals_list_edt_vlaue.getText().toString().length() > 0 && vitals_list_edt_minvalue.getText().toString().length() > 0
                && vitals_list_edt_maxvalue.getText().toString().length() > 0) {
            Double value = Double.parseDouble(vitals_list_edt_vlaue.getText().toString().trim());
            Double minvalue = Double.parseDouble(vitals_list_edt_minvalue.getText().toString().trim());
            Double maxvalue = Double.parseDouble(vitals_list_edt_maxvalue.getText().toString().trim());
            if (value > maxvalue || value < minvalue) {
                vitals_list_edt_vlaue.setTextColor(Color.RED);
            } else {
                vitals_list_edt_vlaue.setTextColor(Color.GREEN);
            }
        }
    }

    private boolean validateControls() {
        if (vitalArrayList.size() == 0) {
            Validate.Msgshow(context, "Please select vitals name.");
            return false;
        } else if (!Validate.hasTextIn(vitals_list_edt_vlaue.getText().toString())) {
            Validate.Msgshow(context, "Please enter value.");
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_current_medication, menu);
        menu.findItem(R.id.menu_current_medication_save).setVisible(true);
        menu.findItem(R.id.menu_current_medication_cancle).setVisible(false);
        menu.findItem(R.id.menu_current_medication_refresh).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_vitals_button:
                if (validateControls()) {
                    VitalsAddLocal();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_current_medication_save:
                if (vitalsListArrayList != null && vitalsListArrayList.size() > 0) {
                    VitalsListAddBindView();
                } else {
                    Toast.makeText(context, "Please add vitals", Toast.LENGTH_SHORT).show();
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
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void Clear() {
        CurrentDate = simpleDF.format(c.getTime());
        vitals_list_edt_vlaue.setText("");
        vitals_list_edt_unit.setText("");
        vitals_list_edt_minvalue.setText("");
        vitals_list_edt_maxvalue.setText("");
        vitals_list_spinner_vitals_name.setSelection(0);
        isUpdate = "No";
    }

    private void VitalsAddLocal() {
        try {
            elVitalsList = new VitalsList();

            if (selectedVitalID != null && selectedVitalID.length() > 0
                    && vitalsListAdapterDB.IsVitalsExist(selectedVitalID, bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID()) == false) {
                elVitalsList.setUnitID(bookAppointmentArrayList.get(0).getDoctorUnitID());
                elVitalsList.setPatientID(bookAppointmentArrayList.get(0).getPatientID());
                elVitalsList.setPatientUnitID(bookAppointmentArrayList.get(0).getUnitID());
                elVitalsList.setVisitID(bookAppointmentArrayList.get(0).getVisitID());
                elVitalsList.setTemplateID("0");
                elVitalsList.setDoctorID(bookAppointmentArrayList.get(0).getDoctorID());
                elVitalsList.setVitalID(selectedVitalID);
                elVitalsList.setVitalsDecription(selectedVitalDesc);
                elVitalsList.setValue(vitals_list_edt_vlaue.getText().toString());
                elVitalsList.setUnit(vitals_list_edt_unit.getText().toString());
                elVitalsList.setAddedBy(bookAppointmentArrayList.get(0).getDoctorID());
                elVitalsList.setDate(CurrentDate);
                elVitalsList.setTime(CurrentDate);
                elVitalsList.setIsSync("1");

                vitalsListAdapterDB.createLocal(elVitalsList);
                refreshList(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
            } else {
                Toast.makeText(context, "Vitals already added", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void VitalsListAddBindView() {
        try {
            new AlertDialog
                    .Builder(context)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Do you really want to add vitals details?")
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            elSaveVitalsList = new VitalsListSave();
                            CurrentDate = simpleDF.format(c.getTime());

                            elSaveVitalsList.setUnitID(bookAppointmentArrayList.get(0).getDoctorUnitID());
                            elSaveVitalsList.setPatientID(bookAppointmentArrayList.get(0).getPatientID());
                            elSaveVitalsList.setPatientUnitID(bookAppointmentArrayList.get(0).getUnitID());
                            elSaveVitalsList.setVisitID(bookAppointmentArrayList.get(0).getVisitID());
                            elSaveVitalsList.setDoctorID(bookAppointmentArrayList.get(0).getDoctorID());
                            elSaveVitalsList.setDate(CurrentDate);
                            elSaveVitalsList.setTime(CurrentDate);
                            elSaveVitalsList.setAllvitalsList(vitalsListAdapterDB.listAll(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID()));
                            new VitalsListAddUpdateTask().execute();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(R.mipmap.ic_launcher)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class VitalsListAddUpdateTask extends AsyncTask<Void, Void, String> {
        private TransparentProgressDialog progressDialog = null;
        private JsonObjectMapper objMapper = null;
        private WebServiceConsumer serviceConsumer = null;
        private Response response = null;
        private String jSonData = "";
        private int responseCode = 0;
        private String responseString = "";
        private String responseMSG = "";

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
                jSonData = objMapper.unMap(elSaveVitalsList);
                serviceConsumer = new WebServiceConsumer(context, null, null);
                response = serviceConsumer.POST(Constants.MULTI_VITALS_ADD_UPDATE_URL, jSonData);
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
                            .setMessage("Vital details added successfully.")
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
                    new AlertDialog
                            .Builder(context)
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage("Vital details updated successfully.")
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
                                        vitalsListAdapterDB.updateUnSync(elVitalsList);
                                    } else {
                                        vitalsListAdapterDB.createUnSync(elVitalsList);
                                    }
                                    Clear();
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.mipmap.ic_launcher)
                            .show();*/
                }
                refreshList(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }

    /*public class VitalsListUpDateTask extends AsyncTask<Void, Void, String> {
        private int responseCode = 0;
        private TransparentProgressDialog progressDialog = null;
        private JsonObjectMapper objMapper = null;
        private WebServiceConsumer serviceConsumer = null;
        private Response response = null;
        private String jSonData = "";
        private String responseMSG = "";

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
                jSonData = objMapper.unMap(elVitalsList);
                serviceConsumer = new WebServiceConsumer(context, null, null);
                response = serviceConsumer.POST(Constants.VITALS_ADD_UPDATE_URL, jSonData);
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    responseMSG = response.message().toString();
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
                if (responseCode == Constants.HTTP_OK_200 && responseMSG.equals("OK")) {
                    localSetting.hideDialog(progressDialog);
                    new AlertDialog
                            .Builder(context)
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage("Vital details updated successfully.")
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
                                    vitalsListAdapterDB.updateUnSync(elVitalsList);
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
    }*/
}
