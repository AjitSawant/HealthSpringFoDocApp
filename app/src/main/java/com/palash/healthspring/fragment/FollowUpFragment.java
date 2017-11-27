package com.palash.healthspring.fragment;

import android.content.Context;
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
import com.palash.healthspring.adapter.CPOEPrescriptionListAdapter;
import com.palash.healthspring.api.JsonObjectMapper;
import com.palash.healthspring.api.WebServiceConsumer;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.BookAppointment;
import com.palash.healthspring.entity.CPOEPrescription;
import com.palash.healthspring.entity.DoctorProfile;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;
import com.palash.healthspring.utilities.TransparentProgressDialog;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by manishas on 7/21/2016.
 */
public class FollowUpFragment extends Fragment {

    private static final String TAG = FollowUpFragment.class.getSimpleName();
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

    private ArrayList<CPOEPrescription> cpoeMedicineArrayList;
    private ArrayList<BookAppointment> bookAppointmentArrayList;
    private ArrayList<DoctorProfile> doctorProfileList;

    private ListView emr_followup_List;
    private TextView emr_followup_empty;
    private Chronometer emr_followup_chronometer;

    private int currentCount = 0;
    private int listCount = 0;

    public FollowUpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_followup, container, false);
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
            emr_followup_List = (ListView) rootView.findViewById(R.id.emr_followup_List);
            emr_followup_empty = (TextView) rootView.findViewById(R.id.emr_followup_empty);
            emr_followup_chronometer = (Chronometer) rootView.findViewById(R.id.emr_followup_chronometer);
            /*emr_followup_chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    //flagTask();
                    LoadList(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                    //MasterFlagTask();
                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        emr_followup_chronometer.setBase(SystemClock.elapsedRealtime());
        emr_followup_chronometer.start();
        if (Constants.backFromAddEMR == false) {
            new GetFollowUp().execute();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        emr_followup_chronometer.stop();
        super.onPause();
    }

    /*private void flagTask(){
        flag = flagAdapter.listCurrent();
        flag.setFlag(Constants.EMR_CPOEMEDICINE_TASK);
        flagAdapter.create(flag);
        SchedulerManager.getInstance().runNow(context, SynchronizationTask.class, 1);
    }
    private void MasterFlagTask() {
        masterflag =  masterFlagAdapter.listCurrent();
        masterflag.setFlag(Constants.EMR_MEDICINE_MASTER_TASK);
        masterFlagAdapter.create(masterflag);
        SchedulerManager.getInstance().runNow(context, MasterTask.class, 1);
    }*/

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
            emr_followup_List.setAdapter(cpoeMedicineListAdapter);
            cpoeMedicineListAdapter.notifyDataSetChanged();
            emr_followup_empty.setVisibility(View.GONE);
            emr_followup_List.setVisibility(View.VISIBLE);
        } else {
            emr_followup_empty.setVisibility(View.VISIBLE);
            emr_followup_List.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_current_medication, menu);
        menu.findItem(R.id.menu_current_medication_add).setVisible(true);
        menu.findItem(R.id.menu_current_medication_refresh).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_current_medication_add:
                //context.startActivity(new Intent(context, CPOEPrescriptionAddUpdateActivity.class).putExtra("isUpdate", "No"));
                Constants.backFromAddEMR = false;
                return true;
            case R.id.menu_current_medication_refresh:
                new GetFollowUp().execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetFollowUp extends AsyncTask<Void, Void, String> {
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
                cpoeMedicineAdapterDB.delete(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                emr_followup_empty.setVisibility(View.VISIBLE);
                emr_followup_List.setVisibility(View.GONE);
            } else {
                Toast.makeText(context, localSetting.handleError(responseCode), Toast.LENGTH_SHORT).show();
            }
            //refreshList(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
            super.onPostExecute(result);
        }
    }
}


