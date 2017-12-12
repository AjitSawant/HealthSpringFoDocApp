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
import com.palash.healthspring.adapter.DiagnosisListAdapter;
import com.palash.healthspring.api.JsonObjectMapper;
import com.palash.healthspring.api.WebServiceConsumer;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.BookAppointment;
import com.palash.healthspring.entity.DiagnosisList;
import com.palash.healthspring.entity.DoctorProfile;
import com.palash.healthspring.entity.Flag;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;
import com.palash.healthspring.utilities.TransparentProgressDialog;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by manishas on 7/21/2016.
 */
public class DiagnosisFragment extends Fragment {

    private static final String TAG = DiagnosisFragment.class.getSimpleName();
    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.FlagAdapter flagAdapter;
    private DatabaseAdapter.MasterFlagAdapter masterFlagAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.DiagnosisListAdapter diagnosisListAdapterDB;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapterDB;

    private ArrayList<DiagnosisList> diagnosisListArrayList;
    private ArrayList<BookAppointment> bookAppointmentArrayList;
    private ArrayList<DoctorProfile> doctorProfileList;

    private Flag flag;
    private Flag masterflag;
    private ListView emr_diagnosis_List;
    private TextView emr_diagnosis_empty;
    private Chronometer emr_diagnosis_chronometer;
    private DiagnosisListAdapter diagnosisListAdapter;
    private MenuItem addItem;
    int currentCount = 0;
    int listCount = 0;

    public DiagnosisFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_diagnosis, container, false);

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
            flagAdapter = databaseAdapter.new FlagAdapter();
            masterFlagAdapter = databaseAdapter.new MasterFlagAdapter();
            doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();
            diagnosisListAdapterDB = databaseAdapter.new DiagnosisListAdapter();
            bookAppointmentAdapterDB = databaseAdapter.new BookAppointmentAdapter();
            bookAppointmentArrayList = bookAppointmentAdapterDB.listLast();
            doctorProfileList = doctorProfileAdapter.listAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView(View rootView) {
        try {
            emr_diagnosis_List = (ListView) rootView.findViewById(R.id.emr_diagnosis_List);
            emr_diagnosis_empty = (TextView) rootView.findViewById(R.id.emr_diagnosis_empty);
            emr_diagnosis_chronometer = (Chronometer) rootView.findViewById(R.id.emr_diagnosis_chronometer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        emr_diagnosis_chronometer.setBase(SystemClock.elapsedRealtime());
        emr_diagnosis_chronometer.start();
        if (Constants.backFromAddEMR == false) {
            refreshList();
            if (localSetting.isNetworkAvailable(context)) {
                new GetDiagnosisListTask().execute();
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
            }
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        emr_diagnosis_chronometer.stop();
        super.onPause();
    }

    private void refreshList() {
        diagnosisListArrayList = diagnosisListAdapterDB.listAll(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
        if (diagnosisListArrayList != null && diagnosisListArrayList.size() > 0) {
            diagnosisListAdapter = new DiagnosisListAdapter(context, diagnosisListArrayList);
            emr_diagnosis_List.setAdapter(diagnosisListAdapter);
            diagnosisListAdapter.notifyDataSetChanged();
            emr_diagnosis_empty.setVisibility(View.GONE);
            emr_diagnosis_List.setVisibility(View.VISIBLE);
        } else {
            emr_diagnosis_empty.setVisibility(View.VISIBLE);
            emr_diagnosis_List.setVisibility(View.GONE);
        }
    }

   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_current_medication, menu);
        addItem = (MenuItem) menu.findItem(R.id.menu_current_medication_add);
        addItem.setVisible(true);
        addItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                localSetting.Activityname = "AddDiagnosisList";
                localSetting.Save();
                Intent DiagnosisList_add_intent = new Intent(context, DiagnosisListAddUpdateActivity.class);
                context.startActivity(DiagnosisList_add_intent);
                return true;
            }
        });
        MenuItem menu_current_medication_refresh = (MenuItem) menu.findItem(R.id.menu_current_medication_refresh);
        menu_current_medication_refresh.setVisible(true);
        menu_current_medication_refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                new GetDiagnosisListTask().execute();

                *//*flagTask();
                if (localSetting.fragment_name.equals("PatientQueue")) {
                    diagnosisListAdapterDB.delete(bookAppointmentArrayList.get(0).getPatientID(),bookAppointmentArrayList.get(0).getVisitID());
                }
                if (diagnosisListArrayList != null && diagnosisListArrayList.size() > 0) {
                    diagnosisListAdapter = new DiagnosisListAdapter(context, diagnosisListArrayList);
                    emr_diagnosis_List.setAdapter(diagnosisListAdapter);
                    diagnosisListAdapter.notifyDataSetChanged();
                    emr_diagnosis_empty.setVisibility(View.GONE);
                    emr_diagnosis_List.setVisibility(View.VISIBLE);
                } else {
                    emr_diagnosis_empty.setVisibility(View.VISIBLE);
                    emr_diagnosis_List.setVisibility(View.GONE);
                }*//*
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }*/

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
                context.startActivity(new Intent(context, DiagnosisAddUpdateActivity.class).putExtra("isUpdate", "No"));
                return true;
            case R.id.menu_toolbar_refresh:
                new GetDiagnosisListTask().execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetDiagnosisListTask extends AsyncTask<Void, Void, String> {
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
                response = webServiceConsumer.GET(Constants.DIAGNOSIS_PATIENT_LIST_URL + doctorProfileList.get(0).getUnitID()
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
                diagnosisListArrayList = jsonObjectMapper.map(responseString, DiagnosisList.class);
                if (diagnosisListArrayList != null && diagnosisListArrayList.size() > 0) {
                    for (int index = 0; index < diagnosisListArrayList.size(); index++) {
                        diagnosisListAdapterDB.create(diagnosisListArrayList.get(index));
                    }
                }
            } else if (responseCode == Constants.HTTP_DELETED_OK_204) {
                diagnosisListAdapterDB.delete(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                emr_diagnosis_empty.setVisibility(View.VISIBLE);
                emr_diagnosis_List.setVisibility(View.GONE);
            } else {
                Toast.makeText(context, localSetting.handleError(responseCode), Toast.LENGTH_SHORT).show();
            }
            refreshList();
            super.onPostExecute(result);
        }
    }
}
