package com.palash.healthspring.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.palash.healthspring.R;
import com.palash.healthspring.adapter.VitalsListAdapter;
import com.palash.healthspring.api.JsonObjectMapper;
import com.palash.healthspring.api.WebServiceConsumer;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.BookAppointment;
import com.palash.healthspring.entity.DoctorProfile;
import com.palash.healthspring.entity.Flag;
import com.palash.healthspring.entity.VitalsList;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;
import com.palash.healthspring.utilities.TransparentProgressDialog;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by manishas on 7/21/2016.
 */
public class VitalsFragment extends Fragment {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.VitalsListAdapter vitalsListAdapterDB;
    private DatabaseAdapter.VitalsListLocalAdapter vitalsListLocalAdapterDB;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapterDB;
    private DatabaseAdapter.FlagAdapter flagAdapter;
    private DatabaseAdapter.MasterFlagAdapter masterFlagAdapter;

    private Flag flag;
    private Flag masterflag;
    private ArrayList<DoctorProfile> doctorProfileList;
    private ArrayList<VitalsList> vitalsListArrayList;
    private ArrayList<BookAppointment> bookAppointmentArrayList;

    private VitalsListAdapter vitalsListAdapter;

    private ListView emr_vitals_list_List;
    private TextView emr_vitals_list_empty;
    private Chronometer emr_vitals_list_chronometer;

    int currentCount = 0;
    int listCount = 0;

    public VitalsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_vitals, container, false);
        InitSetting();
        if (localSetting.fragment_name.equals("PatientQueue")) {
            setHasOptionsMenu(true);
        } else if (localSetting.fragment_name.equals("VisitList")) {
            setHasOptionsMenu(false);
        }
        InitView(rootView);
        return rootView;
    }

    private void InitSetting() {
        try {
            context = getActivity();
            localSetting = new LocalSetting();
            databaseContract = new DatabaseContract(context);
            databaseAdapter = new DatabaseAdapter(databaseContract);
            doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();
            flagAdapter = databaseAdapter.new FlagAdapter();
            masterFlagAdapter = databaseAdapter.new MasterFlagAdapter();
            vitalsListAdapterDB = databaseAdapter.new VitalsListAdapter();
            vitalsListLocalAdapterDB = databaseAdapter.new VitalsListLocalAdapter();
            bookAppointmentAdapterDB = databaseAdapter.new BookAppointmentAdapter();
            bookAppointmentArrayList = bookAppointmentAdapterDB.listLast();
            doctorProfileList = doctorProfileAdapter.listAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView(View rootView) {
        try {
            emr_vitals_list_List = (ListView) rootView.findViewById(R.id.emr_vitals_list_List);
            emr_vitals_list_empty = (TextView) rootView.findViewById(R.id.emr_vitals_list_empty);
            emr_vitals_list_chronometer = (Chronometer) rootView.findViewById(R.id.emr_vitals_list_chronometer);
            /*emr_vitals_list_chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    *//*MasterFlagTask();
                    flagTask();*//*
                    LoadList(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        emr_vitals_list_chronometer.setBase(SystemClock.elapsedRealtime());
        emr_vitals_list_chronometer.start();
        //if (Constants.backFromAddEMR == false) {
        refreshList();
        if (localSetting.isNetworkAvailable(context)) {
            new GetVitalsListTask().execute();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
        }
        //}
        super.onResume();
    }

    @Override
    public void onPause() {
        emr_vitals_list_chronometer.stop();
        super.onPause();
    }

    private void refreshList() {
        vitalsListArrayList = vitalsListAdapterDB.listAll(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
        if (vitalsListArrayList != null && vitalsListArrayList.size() > 0) {
            vitalsListAdapter = new VitalsListAdapter(context, vitalsListArrayList);
            emr_vitals_list_List.setAdapter(vitalsListAdapter);
            vitalsListAdapter.notifyDataSetChanged();
            emr_vitals_list_empty.setVisibility(View.GONE);
            emr_vitals_list_List.setVisibility(View.VISIBLE);
        } else {
            emr_vitals_list_empty.setVisibility(View.VISIBLE);
            emr_vitals_list_List.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.menu_toolbar_add).setVisible(true);
        menu.findItem(R.id.menu_toolbar_refresh).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toolbar_add:
                //MasterFlagTask();
                Constants.backFromAddEMR = false;
                context.startActivity(new Intent(context, VitalsAddUpdateActivity.class).putExtra("isUpdate", "No"));
                return true;
            case R.id.menu_toolbar_refresh:
                new GetVitalsListTask().execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetVitalsListTask extends AsyncTask<Void, Void, String> {
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
                response = webServiceConsumer.GET(Constants.VITALS_PATIENT_LIST_URL + doctorProfileList.get(0).getUnitID()
                        + "&PatientID=" +
                        bookAppointmentArrayList.get(0).getPatientID()
                        + "&VisitID=" +
                        bookAppointmentArrayList.get(0).getVisitID());
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
            localSetting.hideDialog(progressDialog);
            if (responseCode == Constants.HTTP_OK_200) {
                vitalsListArrayList = jsonObjectMapper.map(responseString, VitalsList.class);
                if (vitalsListArrayList != null && vitalsListArrayList.size() > 0) {
                    vitalsListAdapterDB.delete(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                    vitalsListLocalAdapterDB.delete(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                    for (int index = 0; index < vitalsListArrayList.size(); index++) {
                        vitalsListAdapterDB.create(vitalsListArrayList.get(index));
                        vitalsListLocalAdapterDB.create(vitalsListArrayList.get(index));
                    }
                }
            } else if (responseCode == Constants.HTTP_NO_RECORD_FOUND_OK_204) {
                vitalsListLocalAdapterDB.delete(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                vitalsListAdapterDB.delete(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                emr_vitals_list_empty.setVisibility(View.VISIBLE);
                emr_vitals_list_List.setVisibility(View.GONE);
            } else {
                Toast.makeText(context, localSetting.handleError(responseCode), Toast.LENGTH_SHORT).show();
            }
            refreshList();
            super.onPostExecute(result);
        }
    }
}
