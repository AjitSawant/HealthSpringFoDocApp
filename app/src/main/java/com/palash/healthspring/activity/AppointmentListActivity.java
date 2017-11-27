package com.palash.healthspring.activity;

import android.animation.LayoutTransition;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.palash.healthspring.R;
import com.palash.healthspring.adapter.AppointmentExpandListAdapter;
import com.palash.healthspring.api.JsonObjectMapper;
import com.palash.healthspring.api.WebServiceConsumer;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.Appointment;
import com.palash.healthspring.entity.DoctorProfile;
import com.palash.healthspring.entity.Flag;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;
import com.palash.healthspring.utilities.TransparentProgressDialog;
import com.palash.healthspring.utilities.Validate;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AppointmentListActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.AppointmentAdapter appointmentAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.FlagAdapter flagAdapter;

    private HashMap<String, ArrayList<Appointment>> listDataChild;
    private ArrayList<Appointment> childList = null;
    private ArrayList<String> headerList = null;
    private ArrayList<DoctorProfile> doctorProfileList;

    private ExpandableListView appointment_List;
    private TextView appointment_empty;
    private Chronometer appointment_chronometer;
    private LinearLayout layout_search;
    private LinearLayout layout_search_by_patient_name;
    private Button appointment_list_search_bnt;
    private TextView appointment_list_bnt_clear;
    private TextView appointment_edt_fromdate;
    private TextView appointment_edt_todate;
    private EditText appointment_edt_patient_name;

    private Flag flag;
    private AppointmentExpandListAdapter appointmentListAdapter;

    private int headerlistCount = 0;
    private int childlistCount = 0;
    private int currentCountheader = 0;
    private int currentCountchild = 0;
    private String FromDate = null;
    private String ToDate = null;
    private boolean isSearchPanelVisible = true;

    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat format = new SimpleDateFormat(Constants.PATIENT_QUEUE_DATE);
    private DatePickerDialog.OnDateSetListener fromDatepickerdialog;
    private DatePickerDialog.OnDateSetListener toDatepickerdialog;

    private SimpleDateFormat dates = new SimpleDateFormat(Constants.SEARCH_DATE_FORMAT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        InitSetting();
        InitView();
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
            flagAdapter = databaseAdapter.new FlagAdapter();
            appointmentAdapter = databaseAdapter.new AppointmentAdapter();
            doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();

            doctorProfileList = doctorProfileAdapter.listAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView() {
        try {
            appointment_List = (ExpandableListView) findViewById(R.id.appointment_List);
            appointment_empty = (TextView) findViewById(R.id.appointment_empty);
            layout_search = (LinearLayout) findViewById(R.id.layout_search);
            layout_search_by_patient_name = (LinearLayout) findViewById(R.id.layout_search_by_patient_name);
            appointment_list_search_bnt = (Button) findViewById(R.id.appointment_list_search_bnt);
            appointment_edt_patient_name = (EditText) findViewById(R.id.appointment_edt_patient_name);
            appointment_edt_fromdate = (TextView) findViewById(R.id.appointment_edt_fromdate);
            appointment_edt_todate = (TextView) findViewById(R.id.appointment_edt_todate);
            appointment_list_bnt_clear = (TextView) findViewById(R.id.appointment_list_bnt_clear);
            appointment_chronometer = (Chronometer) findViewById(R.id.appointment_chronometer);

            appointment_edt_fromdate.setText(format.format(new Date()));
            appointment_edt_todate.setText(format.format(new Date()));

            layout_search_by_patient_name.setVisibility(View.GONE);
            appointment_list_search_bnt.setOnClickListener(this);
            appointment_list_bnt_clear.setOnClickListener(this);

            appointment_chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    //FlagTask();
                    searchLoadList();
                }
            });

            appointment_edt_fromdate.setFocusable(false);
            appointment_edt_fromdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, fromDatepickerdialog,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.setCancelable(false);
                    datePickerDialog.show();
                }
            });

            appointment_edt_todate.setFocusable(false);
            appointment_edt_todate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, toDatepickerdialog,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.setCancelable(false);
                    datePickerDialog.show();
                }
            });

            /*fromDatepickerdialog = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    Date date = calendar.getTime();
                    appointment_edt_fromdate.setText(format.format(date));
                }
            };*/

            fromDatepickerdialog = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    ToDate = localSetting.dateToString(day, month, year, Constants.SEARCH_DATE_FORMAT);
                    appointment_edt_fromdate.setText(localSetting.dateToString(day, month, year, Constants.PATIENT_QUEUE_DATE));
                }
            };

            /*toDatepickerdialog = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    Date date = calendar.getTime();
                    appointment_edt_todate.setText(format.format(date));
                }
            };*/

            toDatepickerdialog = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    ToDate = localSetting.dateToString(day, month, year, Constants.SEARCH_DATE_FORMAT);
                    try {
                        //Dates to compare
                        Date date1;
                        Date date2;

                        //Setting dates
                        date1 = dates.parse(FromDate);
                        date2 = dates.parse(ToDate);

                        //Comparing dates
                        long difference = Math.abs(date1.getTime() - date2.getTime());
                        long differenceDates = difference / (24 * 60 * 60 * 1000);

                        //Convert long to String
                        String dayDifference = Long.toString(differenceDates);

                        if (date1.before(date2) || date1.equals(date2)) {
                            if (Integer.parseInt(dayDifference) < 7) {
                                appointment_edt_todate.setText(localSetting.dateToString(day, month, year, Constants.PATIENT_QUEUE_DATE));
                            } else {
                                ToDate = "";
                                appointment_edt_todate.setText("");
                                Toast.makeText(context, "Date difference should be maximum 7 days", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            ToDate = "";
                            appointment_edt_todate.setText("");
                            Toast.makeText(context, "End date should greater than start date", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            };

            appointment_List.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                int previousGroup = -1;

                @Override
                public void onGroupExpand(int groupPosition) {
                    appointment_List.smoothScrollToPosition(groupPosition);
                    if (groupPosition != previousGroup)
                        appointment_List.collapseGroup(previousGroup);
                    previousGroup = groupPosition;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        appointment_chronometer.setBase(SystemClock.elapsedRealtime());
        appointment_chronometer.start();

        appointment_edt_fromdate.setText(format.format(new Date()));
        appointment_edt_todate.setText(format.format(new Date()));
        FromDate = localSetting.formatDate(appointment_edt_fromdate.getText().toString(), Constants.PATIENT_QUEUE_DATE, Constants.SEARCH_DATE_FORMAT);
        ToDate = localSetting.formatDate(appointment_edt_todate.getText().toString(), Constants.PATIENT_QUEUE_DATE, Constants.SEARCH_DATE_FORMAT);
        if (localSetting.isNetworkAvailable(context)) {
            new GetAppointmentListTask().execute();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        appointment_chronometer.stop();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    /*private void FlagTask() {
        flag = flagAdapter.listCurrent();
        flag.setFlag(Constants.APPOINTMENT_LIST_TASK);
        flagAdapter.create(flag);
        SchedulerManager.getInstance().runNow(context, SynchronizationTask.class, 1);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appointment_list, menu);
        animatePanel(1);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_appointment_list_search:
                if (!isSearchPanelVisible) {
                    animatePanel(1);
                } else {
                    animatePanel(2);
                }
                return true;
            case R.id.menu_appointment_list_addnew:
                Constants.refreshPatient = true;
                startActivity(new Intent(context, PatientListActivity.class));
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean ValidateDate() {
        if (FromDate.equals("") || FromDate.length() == 0) {
            Toast.makeText(context, "Please select start date...", Toast.LENGTH_SHORT).show();
            return false;
        } else if (ToDate.equals("") || ToDate.length() == 0) {
            Toast.makeText(context, "Please select end date...", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void animatePanel(int direction) {
        switch (direction) {
            case 1:
                layout_search.setVisibility(View.VISIBLE);
                LayoutTransition transition = new LayoutTransition();
                layout_search.setLayoutTransition(transition);
                isSearchPanelVisible = true;
                break;
            case 2:
                isSearchPanelVisible = false;
                layout_search.setVisibility(View.GONE);
                //clearDate();
                break;
        }
    }

    private void searchLoadList() {
        try {
            SimpleDateFormat date_format = new SimpleDateFormat(Constants.SEARCH_DATE_FORMAT);
            String patientName = appointment_edt_patient_name.getText().toString();
            FromDate = date_format.format(format.parse(appointment_edt_fromdate.getText().toString()));
            ToDate = date_format.format(format.parse(appointment_edt_todate.getText().toString()));
            if (isSearchPanelVisible) {
                ToDate = date_format.format(format.parse(appointment_edt_todate.getText().toString()));
            } else {
                ToDate = null;
            }
            switch (Validate.validateSearchDate(date_format.format(format.parse(appointment_edt_fromdate.getText().toString())), date_format.format(format.parse(appointment_edt_todate.getText().toString())))) {
                case 1:
                    currentCountheader = appointmentAdapter.HeaderCount(patientName, FromDate, ToDate);
                    if (headerList != null) {
                        headerlistCount = headerList.size();
                    }
                    if (currentCountheader != headerlistCount) {
                        PrepareListData(patientName, FromDate, ToDate);
                    } else {
                        if (headerList != null && headerList.size() > 0) {
                            for (int i = 0; i < headerList.size(); i++) {
                                currentCountchild = appointmentAdapter.listAllCount(headerList.get(i), patientName);
                            }
                            if (childList != null) {
                                childlistCount = childList.size();
                            }
                            if (currentCountchild != childlistCount) {
                                PrepareListData(patientName, FromDate, ToDate);
                            }
                        } else {
                            appointment_List.setVisibility(View.GONE);
                            appointment_empty.setVisibility(View.VISIBLE);
                            appointment_empty.setText("No Record Found");
                            appointment_empty.setTextColor(Color.GRAY);
                        }
                    }
                    break;
                case 2:
                    appointment_List.setVisibility(View.GONE);
                    appointment_empty.setVisibility(View.VISIBLE);
                    appointment_empty.setText("Please check date format is " + Constants.SEARCH_DATE_FORMAT);
                    appointment_empty.setTextColor(Color.RED);
                    break;
                case 3:
                    appointment_List.setVisibility(View.GONE);
                    appointment_empty.setVisibility(View.VISIBLE);
                    appointment_empty.setText("From Date should be less than To Date");
                    appointment_empty.setTextColor(Color.RED);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PrepareListData(String patientName, String Fromdate, String Todate) {
        try {
            headerList = appointmentAdapter.listHeader(patientName, Fromdate, Todate);
            if (headerList != null && headerList.size() > 0) {
                listDataChild = new HashMap<String, ArrayList<Appointment>>();
                for (int i = 0; i < headerList.size(); i++) {
                    childList = appointmentAdapter.listAll(headerList.get(i), patientName);
                    if (childList != null && childList.size() > 0) {
                        listDataChild.put(headerList.get(i), childList);
                    }
                }
                appointmentListAdapter = new AppointmentExpandListAdapter(context, headerList, listDataChild);
                appointment_List.setAdapter(appointmentListAdapter);
                appointmentListAdapter.notifyDataSetChanged();
                appointment_List.expandGroup(0);
                appointment_List.setVisibility(View.VISIBLE);
                appointment_empty.setVisibility(View.GONE);
            } else {
                appointment_List.setVisibility(View.GONE);
                appointment_empty.setVisibility(View.VISIBLE);
                appointment_empty.setText("No Record Found");
                appointment_empty.setTextColor(Color.GRAY);
                //animatePanel(2);
                // isSearchPanelVisible = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearDate() {
        appointment_edt_patient_name.setText(null);
        appointment_edt_fromdate.setText(format.format(new Date()));
        appointment_edt_todate.setText(format.format(new Date()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.appointment_list_search_bnt:
                if (ValidateDate()) {
                    if (localSetting.isNetworkAvailable(context)) {
                        new GetAppointmentListTask().execute();
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.appointment_list_bnt_clear:
                appointment_edt_patient_name.setText("");
                break;
        }
    }

    private class GetAppointmentListTask extends AsyncTask<Void, Void, String> {
        private TransparentProgressDialog progressDialog;
        private String jSon = "";
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
                response = webServiceConsumer.GET(Constants.GET_APPOINTMENT_URL + doctorProfileList.get(0).getDoctorID() + "&StartDate=" + FromDate + "&EndDate=" + ToDate);
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
                ArrayList<Appointment> appointmentList = jsonObjectMapper.map(responseString, Appointment.class);
                if (appointmentList.size() != appointmentAdapter.TotalCount()) {
                    appointmentAdapter.delete();
                    layout_search_by_patient_name.setVisibility(View.VISIBLE);
                    for (int index = 0; index < appointmentList.size(); index++) {
                        appointmentAdapter.create(appointmentList.get(index));
                    }
                }
            } else if (responseCode == Constants.HTTP_NO_RECORD_FOUND_OK_204) {
                layout_search_by_patient_name.setVisibility(View.GONE);
                appointmentAdapter.delete();
            } else {
                Toast.makeText(context, localSetting.handleError(responseCode), Toast.LENGTH_SHORT).show();
            }
            localSetting.hideDialog(progressDialog);
            super.onPostExecute(result);
        }
    }
}

