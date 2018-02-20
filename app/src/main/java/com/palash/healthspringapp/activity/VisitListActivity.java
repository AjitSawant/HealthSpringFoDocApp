package com.palash.healthspringapp.activity;

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
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.palash.healthspringapp.R;
import com.palash.healthspringapp.adapter.VisitListAdapter;
import com.palash.healthspringapp.api.JsonObjectMapper;
import com.palash.healthspringapp.api.WebServiceConsumer;
import com.palash.healthspringapp.database.DatabaseAdapter;
import com.palash.healthspringapp.database.DatabaseContract;
import com.palash.healthspringapp.entity.BookAppointment;
import com.palash.healthspringapp.entity.DoctorProfile;
import com.palash.healthspringapp.entity.Flag;
import com.palash.healthspringapp.entity.VisitList;
import com.palash.healthspringapp.utilities.Constants;
import com.palash.healthspringapp.utilities.LocalSetting;
import com.palash.healthspringapp.utilities.TransparentProgressDialog;
import com.palash.healthspringapp.utilities.Validate;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class VisitListActivity extends AppCompatActivity {
    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;

    private DatabaseAdapter.VisitListAdapter visitListAdapterDB;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapter;
    private DatabaseAdapter.FlagAdapter flagAdapter;
    private DatabaseAdapter.MasterFlagAdapter masterFlagAdapter;

    private Flag flag;
    private Flag masterflag;

    private ArrayList<DoctorProfile> doctorProfileList;
    private ArrayList<VisitList> visitListArrayList = null;
    private ArrayList<BookAppointment> bookAppointmentArrayList;

    private VisitListAdapter visitListAdapter;

    private ListView visit_list_list;
    private TextView visit_list_empty;
    private Chronometer visit_list_chronometer;
    private LinearLayout layout_search;
    private TextView visit_list_bnt_search;
    private EditText visit_edt_fromdate;
    private EditText visit_edt_todate;

    // private int listCount = 0;
    // private int currentCount = 0;
    private boolean isSearchPanelVisible = false;

    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener fromDatepickerdialog;
    private DatePickerDialog.OnDateSetListener toDatepickerdialog;
    //private SimpleDateFormat format = new SimpleDateFormat(Constants.SEARCH_DATE_FORMAT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_list);
        InitSetting();
        //MasterflagTask();
        InitView();
        new GetVisitListTask().execute();
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
            masterFlagAdapter = databaseAdapter.new MasterFlagAdapter();
            visitListAdapterDB = databaseAdapter.new VisitListAdapter();
            bookAppointmentAdapter = databaseAdapter.new BookAppointmentAdapter();
            bookAppointmentArrayList = bookAppointmentAdapter.listLast();
            doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();
            doctorProfileList = doctorProfileAdapter.listAll();
            masterflag = new Flag();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView() {
        try {
            visit_list_list = (ListView) findViewById(R.id.visit_list_list);
            visit_list_empty = (TextView) findViewById(R.id.visit_list_empty);
            layout_search = (LinearLayout) findViewById(R.id.layout_search);
            visit_list_bnt_search = (TextView) findViewById(R.id.visit_list_bnt_search);
            visit_edt_fromdate = (EditText) findViewById(R.id.visit_edt_fromdate);
            visit_edt_todate = (EditText) findViewById(R.id.visit_edt_todate);

            //visit_edt_fromdate.setText(format.format(new Date()));
            //visit_edt_todate.setText(format.format(new Date()));

            visit_list_chronometer = (Chronometer) findViewById(R.id.visit_list_chronometer);
           /* visit_list_chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                   // flagTask();
                    LoadList();
                }
            });*/

            visit_edt_fromdate.setFocusable(false);
            visit_edt_fromdate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    new DatePickerDialog(context, fromDatepickerdialog, calendar.get(Calendar.YEAR), calendar
                            .get(Calendar.MONTH), calendar
                            .get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            visit_edt_todate.setFocusable(false);
            visit_edt_todate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    new DatePickerDialog(context, toDatepickerdialog,
                            calendar.get(Calendar.YEAR), calendar
                            .get(Calendar.MONTH), calendar
                            .get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            fromDatepickerdialog = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    Date date = calendar.getTime();
                    SimpleDateFormat format = new SimpleDateFormat(Constants.SEARCH_DATE_FORMAT);
                    visit_edt_fromdate.setText(format.format(date));
                }
            };

            toDatepickerdialog = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    Date date = calendar.getTime();
                    SimpleDateFormat format = new SimpleDateFormat(Constants.SEARCH_DATE_FORMAT);
                    visit_edt_todate.setText(format.format(date));

                }
            };
            visit_list_bnt_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearDate();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        visit_list_chronometer.setBase(SystemClock.elapsedRealtime());
        visit_list_chronometer.start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        visit_list_chronometer.stop();
        super.onPause();
    }

    private void LoadList() {
        try {
            String patientID = null;
            String From_date = null;
            String To_date = null;
            if (bookAppointmentArrayList != null) {
                patientID = bookAppointmentArrayList.get(0).getPatientID();
            }
            if (!visit_edt_fromdate.getText().toString().trim().isEmpty() && !visit_edt_todate.getText().toString().trim().isEmpty()) {
                From_date = visit_edt_fromdate.getText().toString();
                To_date = visit_edt_todate.getText().toString();
            }
            /*currentCount = visitListAdapterDB.PatientIDCount(patientID, From_date, To_date);
            if (visitListArrayList != null) {
                listCount = visitListArrayList.size();
            }
            if (currentCount != listCount) {
            Log.d(Constants.TAG, "Count do not match. Loading list.");*/
            visitListArrayList = visitListAdapterDB.PatientIDList(patientID, From_date, To_date);
            if (visitListArrayList != null && visitListArrayList.size() > 0) {
                visitListAdapter = new VisitListAdapter(context, visitListArrayList);
                visit_list_list.setAdapter(visitListAdapter);
                visitListAdapter.notifyDataSetChanged();
                visit_list_list.setVisibility(View.VISIBLE);
                visit_list_empty.setVisibility(View.GONE);
            } else {
                switch (Validate.validateSearchDate(visit_edt_fromdate.getText().toString(), visit_edt_todate.getText().toString())) {
                    case 1:
                        visit_list_list.setVisibility(View.GONE);
                        visit_list_empty.setVisibility(View.VISIBLE);
                        visit_list_empty.setText("No Record Found");
                        visit_list_empty.setTextColor(Color.WHITE);
                        break;
                    case 2:
                        visit_list_list.setVisibility(View.GONE);
                        visit_list_empty.setVisibility(View.VISIBLE);
                        visit_list_empty.setText("Please check date format is " + Constants.SEARCH_DATE_FORMAT);
                        visit_list_empty.setTextColor(Color.RED);
                        break;
                    case 3:
                        visit_list_list.setVisibility(View.GONE);
                        visit_list_empty.setVisibility(View.VISIBLE);
                        visit_list_empty.setText("From Date should be less than To Date");
                        visit_list_empty.setTextColor(Color.RED);
                        break;
                }
//                }
//            } else if (currentCount == listCount) {
//                Log.d(Constants.TAG, "Count match.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void MasterflagTask() {
        masterflag = masterFlagAdapter.listCurrent();
        masterflag.setFlag(Constants.BOOK_APPOINTMENT_TASK);
        masterFlagAdapter.create(masterflag);
        SchedulerManager.getInstance().runNow(context, MasterTask.class, 1);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.menu_toolbar_search).setVisible(false);
        menu.findItem(R.id.menu_toolbar_add).setVisible(true);
        menu.findItem(R.id.menu_toolbar_refresh).setVisible(true);
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
            case R.id.menu_toolbar_add:
                if (localSetting.checkUnitName(doctorProfileList.get(0).getUnitID())) {
                    Toast.makeText(context, "Visit booking functionality is not available for Head office.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(context, VisitBookActivity.class);
                    startActivity(intent);
                }
                return true;
            case R.id.menu_toolbar_refresh:
                new GetVisitListTask().execute();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                clearDate();
                break;
        }
    }

    private void clearDate() {
        visit_edt_fromdate.setText("");
        visit_edt_todate.setText("");
    }

    private class GetVisitListTask extends AsyncTask<Void, Void, String> {
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
                response = webServiceConsumer.GET(Constants.VISIT_LISIT_URL + doctorProfileList.get(0).getUnitID() + "&PatientID=" +
                        bookAppointmentArrayList.get(0).getPatientID());
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
                visitListArrayList = jsonObjectMapper.map(responseString, VisitList.class);
                if (visitListArrayList != null && visitListArrayList.size() > 0) {
                    visitListAdapterDB.delete();
                    for (int index = 0; index < visitListArrayList.size(); index++) {
                        visitListAdapterDB.create(visitListArrayList.get(index));
                    }
                }
            }else if (responseCode == Constants.HTTP_NO_RECORD_FOUND_OK_204) {
                visitListAdapterDB.delete();
                Toast.makeText(context, "Visit not available for selected unit", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, localSetting.handleError(responseCode), Toast.LENGTH_SHORT).show();
            }
            localSetting.hideDialog(progressDialog);
            LoadList();
            super.onPostExecute(result);
        }
    }
}
