package com.palash.healthspringapp.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.palash.healthspringapp.R;
import com.palash.healthspringapp.api.JsonObjectMapper;
import com.palash.healthspringapp.api.WebServiceConsumer;
import com.palash.healthspringapp.database.DatabaseAdapter;
import com.palash.healthspringapp.database.DatabaseContract;
import com.palash.healthspringapp.entity.BookAppointment;
import com.palash.healthspringapp.entity.DoctorProfile;
import com.palash.healthspringapp.entity.ELFollowUp;
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

public class FollowUpFragment extends Fragment {

    private static final String TAG = FollowUpFragment.class.getSimpleName();
    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapterDB;
    private DatabaseAdapter.PatientFollowUpAdapter patientFollowUpAdapterDB;

    private ELFollowUp elFollowUp;
    private ArrayList<BookAppointment> bookAppointmentArrayList;
    private ArrayList<DoctorProfile> doctorProfileList;
    private ArrayList<ELFollowUp> elFollowUpList;

    private EditText advice_followup_edt;
    private TextView followup_edt_date;
    private EditText followup_remark_edt;
    private TextView is_record_sync_tv1;

    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat formate = new SimpleDateFormat(Constants.TIME_FORMAT);
    private DatePickerDialog.OnDateSetListener fromDatepickerdialog;

    private String FollowUpDate = null;

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
            doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();
            bookAppointmentAdapterDB = databaseAdapter.new BookAppointmentAdapter();
            patientFollowUpAdapterDB = databaseAdapter.new PatientFollowUpAdapter();
            bookAppointmentArrayList = bookAppointmentAdapterDB.listLast();
            doctorProfileList = doctorProfileAdapter.listAll();
            elFollowUp = new ELFollowUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView(View rootView) {
        try {
            advice_followup_edt = (EditText) rootView.findViewById(R.id.advice_followup_edt);
            followup_edt_date = (TextView) rootView.findViewById(R.id.followup_edt_date);
            followup_remark_edt = (EditText) rootView.findViewById(R.id.followup_remark_edt);
            is_record_sync_tv1 = (TextView) rootView.findViewById(R.id.is_record_sync_tv1);

            followup_edt_date.setOnClickListener(new View.OnClickListener() {
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

            fromDatepickerdialog = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    try {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        followup_edt_date.setText(localSetting.dateToString(day, month, year, Constants.SEARCH_DATE_FORMAT));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        if (localSetting.isNetworkAvailable(context)) {
            new GetFollowUp().execute();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void refreshList() {
        elFollowUpList = patientFollowUpAdapterDB.listAll(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
        if (elFollowUpList != null && elFollowUpList.size() > 0) {
            elFollowUp = elFollowUpList.get(0);
            if (elFollowUp.getAdvice() != null && elFollowUp.getAdvice().length() > 0) {
                advice_followup_edt.setText(elFollowUp.getAdvice());
            } else {
                advice_followup_edt.setText(null);
            }

            if (elFollowUp.getFollowUpDate() != null && elFollowUp.getFollowUpDate().length() > 0) {
                followup_edt_date.setText(elFollowUp.getFollowUpDate());
            } else {
                followup_edt_date.setText(null);
            }

            if (elFollowUp.getFollowUpRemarks() != null && elFollowUp.getFollowUpRemarks().length() > 0) {
                followup_remark_edt.setText(elFollowUp.getFollowUpRemarks());
            } else {
                followup_remark_edt.setText(null);
            }

            if (elFollowUp.getIsSync() != null && elFollowUp.getIsSync().equals("1")) {
                is_record_sync_tv1.setVisibility(View.VISIBLE);
            } else {
                is_record_sync_tv1.setVisibility(View.GONE);
            }
        } else {
            advice_followup_edt.setText(null);
            followup_edt_date.setText(null);
            followup_remark_edt.setText(null);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.menu_toolbar_save).setVisible(true);
        menu.findItem(R.id.menu_toolbar_refresh).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toolbar_save:
                if (validateControls()) {
                    Constants.backFromAddEMR = false;
                    AddFollowUpView();
                }
                return true;
            case R.id.menu_toolbar_refresh:
                new GetFollowUp().execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validateControls() {
        if (advice_followup_edt.getText().toString().trim().equals("") || advice_followup_edt.getText().toString().trim().length() == 0) {
            Validate.Msgshow(context, "Please enter follow up advice");
            return false;
        } else if (followup_edt_date.getText().toString().trim().equals("") || followup_edt_date.getText().toString().trim().length() == 0) {
            Validate.Msgshow(context, "Please select follow up date");
            return false;
        } else if (followup_remark_edt.getText().toString().trim().equals("") || followup_remark_edt.getText().toString().trim().length() == 0) {
            Validate.Msgshow(context, "Please enter follow up remark");
            return false;
        }
        return true;
    }

    private void AddFollowUpView() {
        try {
            new AlertDialog
                    .Builder(context)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Do you really want to save follow up details?")
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            elFollowUp.setUnitID(bookAppointmentArrayList.get(0).getDoctorUnitID());
                            elFollowUp.setPatientID(bookAppointmentArrayList.get(0).getPatientID());
                            elFollowUp.setPatientUnitID(bookAppointmentArrayList.get(0).getUnitID());
                            elFollowUp.setVisitID(bookAppointmentArrayList.get(0).getVisitID());
                            elFollowUp.setDoctorID(bookAppointmentArrayList.get(0).getDoctorID());
                            elFollowUp.setIsSync("1");
                            elFollowUp.setAdvice(advice_followup_edt.getText().toString().trim());
                            elFollowUp.setDate(formate.format(new Date()));
                            FollowUpDate = localSetting.formatDate(followup_edt_date.getText().toString().trim(), Constants.SEARCH_DATE_FORMAT, Constants.TIME_FORMAT);
                            elFollowUp.setFollowUpDate(FollowUpDate);
                            elFollowUp.setFollowUpRemarks(followup_remark_edt.getText().toString().trim());
                            callToWebservice();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(R.mipmap.ic_launcher)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callToWebservice() {
        if (localSetting.isNetworkAvailable(context)) {
            new AddFollowUp().execute();
        } else {
            new AlertDialog
                    .Builder(context)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage(context.getResources().getString(R.string.offline_net_alert))
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            patientFollowUpAdapterDB.delete(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                            patientFollowUpAdapterDB.createUnSync(elFollowUp);
                            refreshList();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(R.mipmap.ic_launcher)
                    .show();
        }
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
                webServiceConsumer = new WebServiceConsumer(context, null, null, null);
                response = webServiceConsumer.GET(Constants.GET_FOLLOWUP_LIST_URL + doctorProfileList.get(0).getUnitID()
                        + "&PatientID=" + bookAppointmentArrayList.get(0).getPatientID()
                        + "&VisitID=" + bookAppointmentArrayList.get(0).getVisitID());
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
                elFollowUpList = jsonObjectMapper.map(responseString, ELFollowUp.class);
                if (elFollowUpList != null && elFollowUpList.size() > 0) {
                    patientFollowUpAdapterDB.delete(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                    for (int index = 0; index < elFollowUpList.size(); index++) {
                        patientFollowUpAdapterDB.create(elFollowUpList.get(index));
                    }
                }
            } else if (responseCode == Constants.HTTP_DELETED_OK_204) {
                patientFollowUpAdapterDB.delete(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
            } else {
                Toast.makeText(context, localSetting.handleError(responseCode), Toast.LENGTH_SHORT).show();
            }
            refreshList();
            super.onPostExecute(result);
        }
    }

    private class AddFollowUp extends AsyncTask<Void, Void, String> {
        private TransparentProgressDialog progressDialog;
        private String jSon = "";
        private JsonObjectMapper jsonObjectMapper;
        private WebServiceConsumer webServiceConsumer;
        private int responseCode = 0;
        private String responseString = "";
        private String responseMSG = "";
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
                jSon = jsonObjectMapper.unMap(elFollowUp);
                webServiceConsumer = new WebServiceConsumer(context, null, null, null);
                response = webServiceConsumer.POST(Constants.FOLLOWUP_ADD_UPDATE_URL, jSon);
                if (response != null) {
                    responseCode = response.code();
                    responseMSG = response.message().toString();
                    responseString = response.body().string();
                    Log.d(Constants.TAG, "Response code:" + responseCode);
                    Log.d(Constants.TAG, "Response MSG:" + responseMSG);
                    Log.d(Constants.TAG, "Response string:" + responseString);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            localSetting.hideDialog(progressDialog);
            if (responseCode == Constants.HTTP_CREATED_201 && responseMSG.equals("Created")) {
                ArrayList<ELFollowUp> followUpLists = jsonObjectMapper.map(responseString, ELFollowUp.class);
                if (followUpLists != null && followUpLists.size() > 0) {
                    for (int index = 0; index < followUpLists.size(); index++) {
                        patientFollowUpAdapterDB.create(followUpLists.get(index));
                    }
                }
                Toast.makeText(context, "Follow up added successfully.", Toast.LENGTH_SHORT).show();
            } else if (responseCode == Constants.HTTP_OK_200 && responseMSG.equals("OK")) {
                ArrayList<ELFollowUp> followUpLists = jsonObjectMapper.map(responseString, ELFollowUp.class);
                if (followUpLists != null && followUpLists.size() > 0) {
                    for (int index = 0; index < followUpLists.size(); index++) {
                        patientFollowUpAdapterDB.create(followUpLists.get(index));
                    }
                }
                Toast.makeText(context, "Follow up added successfully.", Toast.LENGTH_SHORT).show();
            } else {
                //localSetting.alertbox(context, localSetting.handleError(responseCode), false);
                new AlertDialog
                        .Builder(context)
                        .setTitle(getResources().getString(R.string.app_name))
                        .setMessage(context.getResources().getString(R.string.offline_alert))
                        .setCancelable(true)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                patientFollowUpAdapterDB.delete(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                                patientFollowUpAdapterDB.createUnSync(elFollowUp);
                                refreshList();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.mipmap.ic_launcher)
                        .show();
            }
            super.onPostExecute(result);
        }
    }
}


