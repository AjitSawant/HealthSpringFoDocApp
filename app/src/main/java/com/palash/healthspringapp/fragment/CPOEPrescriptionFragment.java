package com.palash.healthspringapp.fragment;

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

import com.buzzbox.mob.android.scheduler.SchedulerManager;
import com.palash.healthspringapp.R;
import com.palash.healthspringapp.adapter.CPOEPrescriptionListAdapter;
import com.palash.healthspringapp.api.JsonObjectMapper;
import com.palash.healthspringapp.api.WebServiceConsumer;
import com.palash.healthspringapp.database.DatabaseAdapter;
import com.palash.healthspringapp.database.DatabaseContract;
import com.palash.healthspringapp.entity.BookAppointment;
import com.palash.healthspringapp.entity.CPOEPrescription;
import com.palash.healthspringapp.entity.DoctorProfile;
import com.palash.healthspringapp.entity.Flag;
import com.palash.healthspringapp.task.MasterTask;
import com.palash.healthspringapp.utilities.Constants;
import com.palash.healthspringapp.utilities.LocalSetting;
import com.palash.healthspringapp.utilities.TransparentProgressDialog;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

public class CPOEPrescriptionFragment extends Fragment {

    private static final String TAG = CPOEPrescriptionFragment.class.getSimpleName();
    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.MasterFlagAdapter masterFlagAdapter;
    private DatabaseAdapter.FlagAdapter flagAdapter;
    private DatabaseAdapter.CPOEMedicineAdapter cpoeMedicineAdapterDB;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapterDB;

    private CPOEPrescriptionListAdapter cpoeMedicineListAdapter;

    private Flag flag;
    private Flag masterflag;
    private ArrayList<CPOEPrescription> cpoeMedicineArrayList;
    private ArrayList<BookAppointment> bookAppointmentArrayList;
    private ArrayList<DoctorProfile> doctorProfileList;

    private ListView emr_cpoemedicine_List;
    private TextView emr_cpoemedicine_empty;
    private Chronometer emr_cpoemedicine_chronometer;
    private MenuItem addItem;

    private int currentCount = 0;
    private int listCount = 0;

    public CPOEPrescriptionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cpoemeficine, container, false);
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
            cpoeMedicineAdapterDB = databaseAdapter.new CPOEMedicineAdapter();
            bookAppointmentAdapterDB = databaseAdapter.new BookAppointmentAdapter();
            bookAppointmentArrayList = bookAppointmentAdapterDB.listLast();
            doctorProfileList = doctorProfileAdapter.listAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView(View rootView) {
        try {
            emr_cpoemedicine_List = (ListView) rootView.findViewById(R.id.emr_cpoemedicine_List);
            emr_cpoemedicine_empty = (TextView) rootView.findViewById(R.id.emr_cpoemedicine_empty);
            emr_cpoemedicine_chronometer = (Chronometer) rootView.findViewById(R.id.emr_cpoemedicine_chronometer);
            emr_cpoemedicine_chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    //flagTask();
                    LoadList(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                    //MasterFlagTask();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        emr_cpoemedicine_chronometer.setBase(SystemClock.elapsedRealtime());
        emr_cpoemedicine_chronometer.start();
        if (Constants.backFromAddEMR == false) {
            refreshList(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
            if (localSetting.isNetworkAvailable(context)) {
                new GetPrescription().execute();
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
            }
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        emr_cpoemedicine_chronometer.stop();
        super.onPause();
    }

    /*private void flagTask(){
        flag = flagAdapter.listCurrent();
        flag.setFlag(Constants.EMR_CPOEMEDICINE_TASK);
        flagAdapter.create(flag);
        SchedulerManager.getInstance().runNow(context, SynchronizationTask.class, 1);
    }*/

    private void MasterFlagTask() {
        masterflag = masterFlagAdapter.listCurrent();
        masterflag.setFlag(Constants.EMR_MEDICINE_MASTER_TASK);
        masterFlagAdapter.create(masterflag);
        SchedulerManager.getInstance().runNow(context, MasterTask.class, 1);
    }

    private void LoadList(String PatientID, String VisitID) {
        try {
            currentCount = cpoeMedicineAdapterDB.CountID(PatientID, VisitID);
            if (cpoeMedicineArrayList != null) {
                listCount = cpoeMedicineArrayList.size();
            }
            if (currentCount != listCount) {
                refreshList(PatientID, VisitID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshList(String PatientID, String VisitID) {
        cpoeMedicineArrayList = cpoeMedicineAdapterDB.listAll(PatientID, VisitID);
        if (cpoeMedicineArrayList != null && cpoeMedicineArrayList.size() > 0) {
            cpoeMedicineListAdapter = new CPOEPrescriptionListAdapter(context, cpoeMedicineArrayList);
            emr_cpoemedicine_List.setAdapter(cpoeMedicineListAdapter);
            cpoeMedicineListAdapter.notifyDataSetChanged();
            emr_cpoemedicine_empty.setVisibility(View.GONE);
            emr_cpoemedicine_List.setVisibility(View.VISIBLE);
        } else {
            emr_cpoemedicine_empty.setVisibility(View.VISIBLE);
            emr_cpoemedicine_List.setVisibility(View.GONE);
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
                MasterFlagTask();
                Constants.backFromAddEMR = false;
                context.startActivity(new Intent(context, CPOEPrescriptionAddUpdateActivity.class).putExtra("isUpdate", "No"));
                return true;
            case R.id.menu_toolbar_refresh:
                new GetPrescription().execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetPrescription extends AsyncTask<Void, Void, String> {
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
                webServiceConsumer = new WebServiceConsumer(context, null, null, null);
                response = webServiceConsumer.GET(Constants.CPOEMEDICINE_PATIENT_LIST_URL + doctorProfileList.get(0).getUnitID()
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
                cpoeMedicineArrayList = jsonObjectMapper.map(responseString, CPOEPrescription.class);
                if (cpoeMedicineArrayList != null && cpoeMedicineArrayList.size() > 0) {
                    for (int index = 0; index < cpoeMedicineArrayList.size(); index++) {
                        cpoeMedicineAdapterDB.create(cpoeMedicineArrayList.get(index));
                    }
                }
            } else if (responseCode == Constants.HTTP_DELETED_OK_204) {
                //cpoeMedicineAdapterDB.delete(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                emr_cpoemedicine_empty.setVisibility(View.VISIBLE);
                emr_cpoemedicine_List.setVisibility(View.GONE);
            } else {
                Toast.makeText(context, localSetting.handleError(responseCode), Toast.LENGTH_SHORT).show();
            }
            refreshList(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
            super.onPostExecute(result);
        }
    }
}


