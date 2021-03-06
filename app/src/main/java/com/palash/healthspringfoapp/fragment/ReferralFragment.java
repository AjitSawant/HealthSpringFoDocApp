package com.palash.healthspringfoapp.fragment;

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

import com.palash.healthspringfoapp.R;
import com.palash.healthspringfoapp.adapter.ReferralDoctorServiceListAdapter;
import com.palash.healthspringfoapp.api.JsonObjectMapper;
import com.palash.healthspringfoapp.api.WebServiceConsumer;
import com.palash.healthspringfoapp.database.DatabaseAdapter;
import com.palash.healthspringfoapp.database.DatabaseContract;
import com.palash.healthspringfoapp.entity.BookAppointment;
import com.palash.healthspringfoapp.entity.DoctorProfile;
import com.palash.healthspringfoapp.entity.Flag;
import com.palash.healthspringfoapp.entity.ReferralDoctorPerService;
import com.palash.healthspringfoapp.utilities.Constants;
import com.palash.healthspringfoapp.utilities.LocalSetting;
import com.palash.healthspringfoapp.utilities.TransparentProgressDialog;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

public class ReferralFragment extends Fragment {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.MasterFlagAdapter masterFlagAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.ReferralServiceListDBAdapter referralServiceListDBAdapter;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapterDB;

    private ReferralDoctorServiceListAdapter referralDoctorServiceListAdapter;

    private Flag masterflag;
    private ArrayList<ReferralDoctorPerService> referralDoctorPerServicearrayList;
    private ArrayList<BookAppointment> bookAppointmentArrayList;
    private ArrayList<DoctorProfile> doctorProfileList;

    private Chronometer emr_referral_service_chronometer;
    private ListView emr_referral_service_List;
    private TextView emr_referral_service_empty;

    int currentCount = 0;
    int listCount = 0;

    public ReferralFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_referral_doctor_service, container, false);
        InitSetting();
        if (localSetting.fragment_name.equals("PatientQueue")) {
            setHasOptionsMenu(true);
        } else if (localSetting.fragment_name.equals("VisitList")) {
            setHasOptionsMenu(false);
        }
        InitView(rootView);
        return rootView;
    }

    private void InitView(View rootView) {
        try {
            emr_referral_service_List = (ListView) rootView.findViewById(R.id.emr_referral_service_List);
            emr_referral_service_empty = (TextView) rootView.findViewById(R.id.emr_referral_service_empty);
            emr_referral_service_chronometer = (Chronometer) rootView.findViewById(R.id.emr_referral_service_chronometer);
//            emr_referral_service_chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
//                @Override
//                public void onChronometerTick(Chronometer chronometer) {
//                    //flagTask();
//                    LoadList();
//                    //MasterFlagTask();
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitSetting() {
        try {
            context = getActivity();
            localSetting = new LocalSetting();
            localSetting.Init(getActivity());
            localSetting.Load();
            databaseContract = new DatabaseContract(getActivity());
            databaseAdapter = new DatabaseAdapter(databaseContract);
            masterFlagAdapter = databaseAdapter.new MasterFlagAdapter();
            doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();
            referralServiceListDBAdapter = databaseAdapter.new ReferralServiceListDBAdapter();
            bookAppointmentAdapterDB = databaseAdapter.new BookAppointmentAdapter();
            bookAppointmentArrayList = bookAppointmentAdapterDB.listLast();
            doctorProfileList = doctorProfileAdapter.listAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        emr_referral_service_chronometer.setBase(SystemClock.elapsedRealtime());
        emr_referral_service_chronometer.start();
        if (Constants.backFromAddEMR == false) {
            refreshList();
            if (localSetting.isNetworkAvailable(context)) {
                new GetReferralDoctorService().execute();
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
            }
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        emr_referral_service_chronometer.stop();
        super.onPause();
    }

   /* private void flagTask() {
        flag = flagAdapter.listCurrent();
        flag.setFlag(Constants.EMR_CPOESERVICE_TASK);
        flagAdapter.create(flag);
        SchedulerManager.getInstance().runNow(getActivity(), SynchronizationTask.class, 1);
    }*/

    private void refreshList() {
        referralDoctorPerServicearrayList = referralServiceListDBAdapter.listAll(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
        if (referralDoctorPerServicearrayList != null && referralDoctorPerServicearrayList.size() > 0) {
            referralDoctorServiceListAdapter = new ReferralDoctorServiceListAdapter(getActivity(), referralDoctorPerServicearrayList);
            emr_referral_service_List.setAdapter(referralDoctorServiceListAdapter);
            referralDoctorServiceListAdapter.notifyDataSetChanged();
            emr_referral_service_empty.setVisibility(View.GONE);
            emr_referral_service_List.setVisibility(View.VISIBLE);
        } else {
            emr_referral_service_empty.setVisibility(View.VISIBLE);
            emr_referral_service_List.setVisibility(View.GONE);
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
                Constants.backFromAddEMR = false;
                context.startActivity(new Intent(context, ReferralAddUpdateActivity.class).putExtra("isUpdate", "No"));
                return true;
            case R.id.menu_toolbar_refresh:
                new GetReferralDoctorService().execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetReferralDoctorService extends AsyncTask<Void, Void, String> {
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
                response = webServiceConsumer.GET(Constants.REFERRAL_DOCTOR_EMR_LIST_PER_SERVICE_URL + doctorProfileList.get(0).getUnitID()
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
                referralDoctorPerServicearrayList = jsonObjectMapper.map(responseString, ReferralDoctorPerService.class);
                if (referralDoctorPerServicearrayList != null && referralDoctorPerServicearrayList.size() > 0) {
                    //referralServiceListDBAdapter.delete(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                    for (int index = 0; index < referralDoctorPerServicearrayList.size(); index++) {
                        referralServiceListDBAdapter.create(referralDoctorPerServicearrayList.get(index));
                    }
                }
            } else if (responseCode == Constants.HTTP_NO_RECORD_FOUND_OK_204) {
                //referralServiceListDBAdapter.delete(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                emr_referral_service_empty.setVisibility(View.VISIBLE);
                emr_referral_service_List.setVisibility(View.GONE);
            } else {
                Toast.makeText(context, localSetting.handleError(responseCode), Toast.LENGTH_SHORT).show();
            }
            refreshList();
            super.onPostExecute(result);
        }
    }
}
