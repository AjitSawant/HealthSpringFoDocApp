package com.palash.healthspring.activity;

import android.animation.LayoutTransition;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.palash.healthspring.R;
import com.palash.healthspring.adapter.PatientQueueAdapter;
import com.palash.healthspring.api.JsonObjectMapper;
import com.palash.healthspring.api.WebServiceConsumer;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.DoctorProfile;
import com.palash.healthspring.entity.PatientQueue;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;
import com.palash.healthspring.utilities.TransparentProgressDialog;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

public class PatientQueueActivity extends AppCompatActivity {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.PatientQueueAdapter patientQueueAdapterDB;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;

    private ArrayList<DoctorProfile> doctorProfileList;
    private ArrayList<PatientQueue> patientQueueArrayList = null;

    private PatientQueueAdapter patientQueueAdapter;

    private ListView patient_queue_list;
    private TextView patient_queue_empty;
    private Chronometer patient_queue_chronometer;
    private LinearLayout layout_search;
    private EditText patientqueue_edt_search;
    private TextView patientqueue_bnt_clear;

    private String patientName = null;
    private boolean isSearchPanelVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_queue);
        InitSetting();
        InitView();
        if (localSetting.isNetworkAvailable(context)) {
            new GetPatientQueueTask().execute();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
        }
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
            patientQueueAdapterDB = databaseAdapter.new PatientQueueAdapter();
            doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();
            doctorProfileList = doctorProfileAdapter.listAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView() {
        try {
            patient_queue_list = (ListView) findViewById(R.id.patient_queue_list);
            patient_queue_empty = (TextView) findViewById(R.id.patient_queue_empty);
            layout_search = (LinearLayout) findViewById(R.id.layout_search);
            patientqueue_edt_search = (EditText) findViewById(R.id.patientqueue_edt_search);
            patientqueue_bnt_clear = (TextView) findViewById(R.id.patientqueue_bnt_search);
            patient_queue_chronometer = (Chronometer) findViewById(R.id.patient_queue_chronometer);

            /*patient_queue_chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                   // flagTask();
                   // LoadList();
                }
            });*/

            patientqueue_bnt_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearDate();
                }
            });

            patientqueue_edt_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (patientQueueArrayList != null && patientQueueArrayList.size() > 0) {
                        patientQueueAdapter.getFilter().filter(s);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        patient_queue_chronometer.setBase(SystemClock.elapsedRealtime());
        patient_queue_chronometer.start();
        RefreshList();
        super.onResume();
    }

    @Override
    protected void onPause() {
        patient_queue_chronometer.stop();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void RefreshList() {
        patientQueueArrayList = patientQueueAdapterDB.listToday(doctorProfileList.get(0).getUnitID(),patientName);
        if (patientQueueArrayList != null && patientQueueArrayList.size() > 0) {
            patientQueueAdapter = new PatientQueueAdapter(context, patientQueueArrayList);
            patient_queue_list.setAdapter(patientQueueAdapter);
            patientQueueAdapter.notifyDataSetChanged();
            patient_queue_list.setVisibility(View.VISIBLE);
            patient_queue_empty.setVisibility(View.GONE);
        } else {
            patient_queue_list.setVisibility(View.GONE);
            patient_queue_empty.setVisibility(View.VISIBLE);
        }
    }

    /*private void flagTask() {
        flag = flagAdapter.listCurrent();
        flag.setFlag(Constants.PATIENT_QUEUE_TASK);
        flagAdapter.create(flag);
        SchedulerManager.getInstance().runNow(context, SynchronizationTask.class, 1);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.menu_toolbar_search).setVisible(true);
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
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_toolbar_refresh:
                new GetPatientQueueTask().execute();
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
        patientqueue_edt_search.setText("");
    }

    private class GetPatientQueueTask extends AsyncTask<Void, Void, String> {
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
                response = webServiceConsumer.GET(Constants.PATIENT_QUEUE_URL + doctorProfileList.get(0).getUnitID());
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
                patientQueueArrayList = jsonObjectMapper.map(responseString, PatientQueue.class);
                if (patientQueueArrayList != null && patientQueueArrayList.size() > 0) {
                    patientQueueAdapterDB.delete(doctorProfileList.get(0).getUnitID());
                    for (int index = 0; index < patientQueueArrayList.size(); index++) {
                        patientQueueAdapterDB.create(patientQueueArrayList.get(index));
                    }
                }
            } else if (responseCode == Constants.HTTP_NO_RECORD_FOUND_OK_204) {
                patientQueueAdapterDB.delete(doctorProfileList.get(0).getUnitID());
                Toast.makeText(context, localSetting.handleError(responseCode), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, localSetting.handleError(responseCode), Toast.LENGTH_SHORT).show();
            }
            localSetting.hideDialog(progressDialog);
            RefreshList();
            super.onPostExecute(result);
        }
    }
}
