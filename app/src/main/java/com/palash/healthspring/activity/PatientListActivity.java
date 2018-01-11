package com.palash.healthspring.activity;


import android.animation.LayoutTransition;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.palash.healthspring.R;
import com.palash.healthspring.adapter.SearchPatientAdapter;
import com.palash.healthspring.api.JsonObjectMapper;
import com.palash.healthspring.api.WebServiceConsumer;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.DoctorProfile;
import com.palash.healthspring.entity.Patient;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;
import com.palash.healthspring.utilities.TransparentProgressDialog;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PatientListActivity extends PatientQueueActivity implements View.OnClickListener {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.PatientAdapter patientAdapterDB;

    private SearchPatientAdapter searchPatientAdapter;

    private ArrayList<DoctorProfile> doctorProfileList;
    private ArrayList<Patient> patientList;

    //private View parentView;
    private ListView search_patient_List;
    private TextView search_patient_empty;
    //private Chronometer patient_list_chronometer;
    private LinearLayout layout_patient_list_search;
    private EditText patient_list_edt_search;
    private EditText patient_list_edt_mr_no;
    private TextView patient_list_bnt_clear;
    private TextView patient_register_start_date_edt;
    private TextView patient_register_end_date_edt;
    private TextView patient_report_date_search_btn;
    private LinearLayout layout_search_by_patient_name;
    private RadioButton radio_button_by_date;
    private RadioButton radio_button_by_mrno;

    private boolean isSearchPanelVisible = true;
    private boolean isByDateSearch = true;
    private static String startDate = "";
    private static String endDate = "";
    private static String mrNO = "";

    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat format = new SimpleDateFormat(Constants.PATIENT_QUEUE_DATE);
    private SimpleDateFormat dates = new SimpleDateFormat(Constants.SEARCH_DATE_FORMAT);
    private DatePickerDialog.OnDateSetListener dateListenerFrom;
    private DatePickerDialog.OnDateSetListener dateListenerTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_patient);
        InitSetting();
        InitView();
    }

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.activity_list_patient, container, false);
        InitSetting();
        InitView();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(myBroadcastReceiver,
                new IntentFilter(Constants.KEY_ForRefreshData));
        return parentView;
    }

    private final BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, android.content.Intent intent) {
            //Toast.makeText(getActivity(), "Broadcast received!", Toast.LENGTH_SHORT).show();//Do what you want when the broadcast is received...
            RefreshPatientList();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(myBroadcastReceiver);
    }*/

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
            patientAdapterDB = databaseAdapter.new PatientAdapter();
            doctorProfileList = doctorProfileAdapter.listAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView() {
        try {
            search_patient_List = (ListView) findViewById(R.id.search_patient_List);
            search_patient_empty = (TextView) findViewById(R.id.search_patient_empty);
            layout_patient_list_search = (LinearLayout) findViewById(R.id.layout_patient_list_search);
            layout_search_by_patient_name = (LinearLayout) findViewById(R.id.layout_search_by_patient_name);
            patient_list_edt_search = (EditText) findViewById(R.id.patient_list_edt_search);
            patient_list_edt_mr_no = (EditText) findViewById(R.id.patient_list_edt_mr_no);
            patient_list_bnt_clear = (TextView) findViewById(R.id.patient_list_bnt_clear);
            patient_register_start_date_edt = (TextView) findViewById(R.id.patient_register_start_date_edt);
            patient_register_end_date_edt = (TextView) findViewById(R.id.patient_register_end_date_edt);
            patient_report_date_search_btn = (TextView) findViewById(R.id.patient_report_date_search_btn);
            radio_button_by_date = (RadioButton) findViewById(R.id.radio_button_by_date);
            radio_button_by_mrno = (RadioButton) findViewById(R.id.radio_button_by_mrno);
            //patient_list_chronometer = (Chronometer) parentView.findViewById(R.id.patient_list_chronometer);

            layout_search_by_patient_name.setVisibility(View.GONE);
            patient_list_edt_mr_no.setVisibility(View.GONE);

            patient_list_bnt_clear.setOnClickListener(this);
            patient_register_start_date_edt.setOnClickListener(this);
            patient_register_end_date_edt.setOnClickListener(this);
            patient_report_date_search_btn.setOnClickListener(this);

            patient_register_start_date_edt.setText(format.format(new Date()));
            patient_register_end_date_edt.setText(format.format(new Date()));
            startDate = localSetting.formatDate(patient_register_start_date_edt.getText().toString(), Constants.PATIENT_QUEUE_DATE, Constants.SEARCH_DATE_FORMAT);
            endDate = localSetting.formatDate(patient_register_end_date_edt.getText().toString(), Constants.PATIENT_QUEUE_DATE, Constants.SEARCH_DATE_FORMAT);

            patient_list_edt_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    if (patientList != null && patientList.size() > 0) {
                        searchPatientAdapter.getFilter().filter(s);
                    }
                }
            });

            dateListenerFrom = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);

                    startDate = localSetting.dateToString(day, month, year, Constants.SEARCH_DATE_FORMAT);
                    endDate = localSetting.dateToString(day, month, year, Constants.SEARCH_DATE_FORMAT);

                    patient_register_start_date_edt.setText(localSetting.dateToString(day, month, year, Constants.PATIENT_QUEUE_DATE));
                    patient_register_end_date_edt.setText(localSetting.dateToString(day, month, year, Constants.PATIENT_QUEUE_DATE));
                }
            };

            dateListenerTo = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    endDate = localSetting.dateToString(day, month, year, Constants.SEARCH_DATE_FORMAT);
                    try {
                        //Dates to compare
                        Date date1;
                        Date date2;

                        //Setting dates
                        date1 = dates.parse(startDate);
                        date2 = dates.parse(endDate);

                        //Comparing dates
                        long difference = Math.abs(date1.getTime() - date2.getTime());
                        long differenceDates = difference / (24 * 60 * 60 * 1000);

                        //Convert long to String
                        String dayDifference = Long.toString(differenceDates);

                        if (date1.before(date2) || date1.equals(date2)) {
                            if (Integer.parseInt(dayDifference) < Constants.FILTER_DAYS_COUNT) {
                                patient_register_end_date_edt.setText(localSetting.dateToString(day, month, year, Constants.PATIENT_QUEUE_DATE));
                            } else {
                                endDate = "";
                                patient_register_end_date_edt.setText("");
                                Toast.makeText(context, context.getString(R.string.filter_date_alter), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            endDate = "";
                            patient_register_end_date_edt.setText("");
                            Toast.makeText(context, "End date should greater than start date", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            };

            radio_button_by_mrno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    patient_register_end_date_edt.setVisibility(View.VISIBLE);
                    patient_register_start_date_edt.setVisibility(View.VISIBLE);
                    patient_list_edt_mr_no.setVisibility(View.GONE);
                    isByDateSearch = true;
                }
            });

            radio_button_by_date.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    patient_list_edt_mr_no.setVisibility(View.VISIBLE);
                    patient_register_end_date_edt.setVisibility(View.GONE);
                    patient_register_start_date_edt.setVisibility(View.GONE);
                    isByDateSearch = false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try {
            if (isVisibleToUser) {
                RefreshPatientList();
                //GetPatientListWebcall();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void RefreshPatientList() {
        doctorProfileList = doctorProfileAdapter.listAll();
        patientList = patientAdapterDB.listPatient(doctorProfileList.get(0).getUnitID(), null);
        if (patientList != null && patientList.size() > 0) {
            searchPatientAdapter = new SearchPatientAdapter(context, patientList);
            search_patient_List.setAdapter(searchPatientAdapter);
            searchPatientAdapter.notifyDataSetChanged();
            search_patient_empty.setVisibility(View.GONE);
            search_patient_List.setVisibility(View.VISIBLE);
            layout_search_by_patient_name.setVisibility(View.VISIBLE);
        } else {
            search_patient_empty.setVisibility(View.VISIBLE);
            search_patient_List.setVisibility(View.GONE);
            layout_search_by_patient_name.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.patient_report_date_search_btn:
                if (ValidateDate()) {
                    GetPatientListWebcall();
                }
                break;
            case R.id.patient_register_start_date_edt:
                new DatePickerDialog(context, dateListenerFrom, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                patient_register_end_date_edt.setText(null);
                endDate = "";
                break;

            case R.id.patient_register_end_date_edt:
                if (startDate.equals("") || startDate.length() == 0) {
                    Toast.makeText(context, "Please first select start date.", Toast.LENGTH_SHORT).show();
                } else {
                    // calendar = Calendar.getInstance();
                    new DatePickerDialog(context, dateListenerTo, calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                break;
            case R.id.patient_list_bnt_clear:
                patient_list_edt_search.setText("");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.menu_toolbar_add_patient).setVisible(false);
        menu.findItem(R.id.menu_toolbar_search).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toolbar_search:
                if (!isSearchPanelVisible) {
                    animatePanel(1);
                } else {
                    animatePanel(2);
                }
                return true;
            case R.id.menu_toolbar_add_patient:
                startActivity(new Intent(context, PatientRegistrationActivity.class));
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void animatePanel(int direction) {
        switch (direction) {
            case 1:
                layout_patient_list_search.setVisibility(View.VISIBLE);
                LayoutTransition transition = new LayoutTransition();
                layout_patient_list_search.setLayoutTransition(transition);
                isSearchPanelVisible = true;
                break;
            case 2:
                isSearchPanelVisible = false;
                layout_patient_list_search.setVisibility(View.GONE);
                patient_list_edt_search.setText("");
                break;
        }
    }

    private boolean ValidateDate() {
        mrNO = patient_list_edt_mr_no.getText().toString().trim();
        if (isByDateSearch) {
            mrNO = "";
            if (startDate == null || startDate.equals("") || startDate.length() == 0) {
                Toast.makeText(context, "Please select start date...", Toast.LENGTH_SHORT).show();
                return false;
            } else if (endDate.equals("") || endDate.length() == 0) {
                Toast.makeText(context, "Please select end date...", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            if (mrNO.equals("") || mrNO.length() == 0) {
                Toast.makeText(context, "Please enter MRNo", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void GetPatientListWebcall() {
        doctorProfileList = doctorProfileAdapter.listAll();
        if (localSetting.isNetworkAvailable(context)) {
            new GetPatientListTask().execute();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
        }
    }

    private class GetPatientListTask extends AsyncTask<Void, Void, String> {
        private TransparentProgressDialog progressDialog;
        private JsonObjectMapper jsonObjectMapper;
        private WebServiceConsumer webServiceConsumer;
        private int responseCode = 0;
        private String responseString = "";
        private Response response = null;

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                jsonObjectMapper = new JsonObjectMapper();
                webServiceConsumer = new WebServiceConsumer(context, null, null);
                response = webServiceConsumer.GET(Constants.PATIENT_LIST_URL + doctorProfileList.get(0).getUnitID() + "&StartDate=" + startDate + "&EndDate=" + endDate
                        + "&MRNo=" + mrNO);
                if (response != null) {
                    responseCode = response.code();
                    responseString = response.body().string();
                    Log.d(Constants.TAG, "Response Code :" + responseCode);
                    Log.d(Constants.TAG, "Response String :" + responseString);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            if (responseCode == Constants.HTTP_OK_200) {
                patientList = jsonObjectMapper.map(responseString, Patient.class);
                if (patientList != null && patientList.size() > 0) {
                    patientAdapterDB.delete(doctorProfileList.get(0).getUnitID());
                    for (int i = 0; i < patientList.size(); i++) {
                        patientAdapterDB.create(patientList.get(i));
                    }
                } else {
                    search_patient_empty.setVisibility(View.VISIBLE);
                    search_patient_List.setVisibility(View.GONE);
                    layout_search_by_patient_name.setVisibility(View.GONE);
                }
            } else if (responseCode == Constants.HTTP_NO_RECORD_FOUND_OK_204) {
                patientAdapterDB.delete(doctorProfileList.get(0).getUnitID());
                search_patient_empty.setVisibility(View.VISIBLE);
                search_patient_List.setVisibility(View.GONE);
                Toast.makeText(context, localSetting.handleError(responseCode), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, localSetting.handleError(responseCode), Toast.LENGTH_SHORT).show();
            }
            localSetting.hideDialog(progressDialog);
            RefreshPatientList();
            super.onPostExecute(result);
        }
    }
}
