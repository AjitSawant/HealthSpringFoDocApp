package com.palash.healthspringfoapp.fragment;

import android.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.buzzbox.mob.android.scheduler.SchedulerManager;
import com.palash.healthspringfoapp.R;
import com.palash.healthspringfoapp.adapter.SpinnerAdapter;
import com.palash.healthspringfoapp.api.JsonObjectMapper;
import com.palash.healthspringfoapp.api.WebServiceConsumer;
import com.palash.healthspringfoapp.database.DatabaseAdapter;
import com.palash.healthspringfoapp.database.DatabaseContract;
import com.palash.healthspringfoapp.entity.BookAppointment;
import com.palash.healthspringfoapp.entity.Complaint;
import com.palash.healthspringfoapp.entity.ComplaintsList;
import com.palash.healthspringfoapp.entity.DoctorProfile;
import com.palash.healthspringfoapp.entity.Flag;
import com.palash.healthspringfoapp.task.MasterTask;
import com.palash.healthspringfoapp.utilities.Constants;
import com.palash.healthspringfoapp.utilities.LocalSetting;
import com.palash.healthspringfoapp.utilities.TransparentProgressDialog;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;


public class ComplaintsFragment extends Fragment {

    private static final String TAG = ComplaintsFragment.class.getSimpleName();
    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.MasterFlagAdapter masterFlagAdapter;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapterDB;
    private DatabaseAdapter.ComplaintsListDBAdapter complaintsListDBAdapter;
    private DatabaseAdapter.ComplaintAdapter masterCcomplaintAdapter;

    private SpinnerAdapter.ComplaintAdapter chiefComplaintAdapter;
    private SpinnerAdapter.ComplaintAdapter associateComplaintAdapter;

    private ArrayList<Complaint> masterComplaintList;
    private ArrayList<ComplaintsList> complaintsLists;
    private ArrayList<BookAppointment> bookAppointmentArrayList;
    private ArrayList<DoctorProfile> doctorProfileList;
    private ArrayList<String> selectedChiefComplaintsList = new ArrayList<>();
    private ArrayList<String> selectedAssociateComplaintsList = new ArrayList<>();

    private EditText chiefComplaints_edit;
    private EditText assosciateComplaints_edit;
    private MaterialSpinner chiefComplaints_dropdown;
    private MaterialSpinner assosciateComplaints_dropdown;
    private TextView is_record_sync_tv1;
    private TextView is_record_sync_tv2;

    private Flag masterflag;
    private ComplaintsList elComplaintsList;

    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat simpleDF;
    private String noComplaints = "no complaints";

    public ComplaintsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_complaints, container, false);
        InitSetting();
        if (localSetting.fragment_name.equals("PatientQueue")) {
            setHasOptionsMenu(true);
        } else if (localSetting.fragment_name.equals("VisitList")) {
            setHasOptionsMenu(false);
        }
        InitView(rootView);
        MasterFlagTask();
        SetSpinnerAdapter();
        return rootView;
    }

    private void InitSetting() {
        try {
            context = getActivity();
            localSetting = new LocalSetting();
            databaseContract = new DatabaseContract(context);
            databaseAdapter = new DatabaseAdapter(databaseContract);
            masterFlagAdapter = databaseAdapter.new MasterFlagAdapter();
            doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();
            bookAppointmentAdapterDB = databaseAdapter.new BookAppointmentAdapter();
            masterCcomplaintAdapter = databaseAdapter.new ComplaintAdapter();
            complaintsListDBAdapter = databaseAdapter.new ComplaintsListDBAdapter();

            bookAppointmentArrayList = bookAppointmentAdapterDB.listLast();
            doctorProfileList = doctorProfileAdapter.listAll();

            masterflag = new Flag();
            elComplaintsList = new ComplaintsList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView(View rootView) {
        try {
            chiefComplaints_edit = (EditText) rootView.findViewById(R.id.chiefComplaints_edit);
            assosciateComplaints_edit = (EditText) rootView.findViewById(R.id.assosciateComplaints_edit);
            chiefComplaints_dropdown = (MaterialSpinner) rootView.findViewById(R.id.chiefComplaints_dropdown);
            assosciateComplaints_dropdown = (MaterialSpinner) rootView.findViewById(R.id.assosciateComplaints_dropdown);
            is_record_sync_tv1 = (TextView) rootView.findViewById(R.id.is_record_sync_tv1);
            is_record_sync_tv2 = (TextView) rootView.findViewById(R.id.is_record_sync_tv2);

            is_record_sync_tv1.setVisibility(View.GONE);
            is_record_sync_tv2.setVisibility(View.GONE);

            if (localSetting.fragment_name.equals("VisitList")) {
                chiefComplaints_dropdown.setVisibility(View.GONE);
                assosciateComplaints_dropdown.setVisibility(View.GONE);
            }

            simpleDF = new SimpleDateFormat(Constants.TIME_FORMAT, Locale.US);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetSpinnerAdapter() {
        try {
            masterComplaintList = masterCcomplaintAdapter.listAll();
            if (masterComplaintList != null && masterComplaintList.size() > 0) {
                chiefComplaintAdapter = new SpinnerAdapter.ComplaintAdapter(context, masterComplaintList);
                chiefComplaints_dropdown.setAdapter(chiefComplaintAdapter);
                chiefComplaintAdapter.notifyDataSetChanged();

                chiefComplaints_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int selectedItemPosition = chiefComplaints_dropdown.getSelectedItemPosition();
                        if (selectedItemPosition > 0) {
                            selectedItemPosition = selectedItemPosition - 1;
                            if (chiefComplaints_edit.getText().toString().equals("") || chiefComplaints_edit.getText().toString().length() == 0) {
                                chiefComplaints_edit.setText(masterComplaintList.get(selectedItemPosition).getDescription());
                                chiefComplaints_edit.setSelection(chiefComplaints_edit.getText().length());
                            } else if (chiefComplaints_edit.getText().toString().contains(masterComplaintList.get(selectedItemPosition).getDescription())) {
                                //String comlaints = chiefComplaints_edit.getText().toString().replace(masterComplaintList.get(selectedItemPosition).getDescription(), "");
                                //chiefComplaints_edit.setText(comlaints + ", " + masterComplaintList.get(selectedItemPosition).getDescription());
                            } else {
                                chiefComplaints_edit.setText(chiefComplaints_edit.getText().toString() + ", " + masterComplaintList.get(selectedItemPosition).getDescription());
                                chiefComplaints_edit.setSelection(chiefComplaints_edit.getText().length());
                            }

                            /*if (selectedChiefComplaintsList.size() == 0) {
                                selectedChiefComplaintsList.add(masterComplaintList.get(selectedItemPosition).getDescription());
                                chiefComplaints_edit.setText(selectedChiefComplaintsList.toString().replace("[", "").replace("]", ""));
                                //Toast.makeText(context, "Complaint added", Toast.LENGTH_SHORT).show();
                            } else if (selectedChiefComplaintsList.size() > 0) {
                                Boolean flag = false;
                                int pos = 0;
                                for (int i = 0; i < selectedChiefComplaintsList.size(); i++) {
                                    if (masterComplaintList.get(selectedItemPosition).getDescription().trim().equals(selectedChiefComplaintsList.get(i).trim())) {
                                        flag = true;
                                        pos = i;
                                    }
                                }

                                if (flag == true) {
                                    selectedChiefComplaintsList.remove(pos);
                                    //Toast.makeText(context, "Complaint removed", Toast.LENGTH_SHORT).show();
                                } else {
                                    selectedChiefComplaintsList.add(masterComplaintList.get(selectedItemPosition).getDescription());
                                    //Toast.makeText(context, "Complaint added", Toast.LENGTH_SHORT).show();
                                }
                                chiefComplaints_edit.setText(selectedChiefComplaintsList.toString().replace("[", "").replace("]", ""));
                                if (selectedChiefComplaintsList.size() == 0) {
                                    chiefComplaints_edit.setHint(noComplaints);
                                }
                            } else {
                                chiefComplaints_edit.setHint(noComplaints);
                            }*/
                        }
                        chiefComplaints_dropdown.setSelection(0);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                associateComplaintAdapter = new SpinnerAdapter.ComplaintAdapter(context, masterComplaintList);
                assosciateComplaints_dropdown.setAdapter(associateComplaintAdapter);
                associateComplaintAdapter.notifyDataSetChanged();

                assosciateComplaints_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int selectedItemPosition = assosciateComplaints_dropdown.getSelectedItemPosition();
                        if (selectedItemPosition > 0) {
                            selectedItemPosition = selectedItemPosition - 1;

                            if (assosciateComplaints_edit.getText().toString().equals("") || assosciateComplaints_edit.getText().toString().length() == 0) {
                                assosciateComplaints_edit.setText(masterComplaintList.get(selectedItemPosition).getDescription());
                                assosciateComplaints_edit.setSelection(assosciateComplaints_edit.getText().length());
                            } else if (assosciateComplaints_edit.getText().toString().contains(masterComplaintList.get(selectedItemPosition).getDescription())) {
                                // String comlaints = assosciateComplaints_edit.getText().toString().replace(masterComplaintList.get(selectedItemPosition).getDescription(), "");
                                //assosciateComplaints_edit.setText(comlaints + ", " + masterComplaintList.get(selectedItemPosition).getDescription());
                            } else {
                                assosciateComplaints_edit.setText(assosciateComplaints_edit.getText().toString() + ", " + masterComplaintList.get(selectedItemPosition).getDescription());
                                assosciateComplaints_edit.setSelection(assosciateComplaints_edit.getText().length());
                            }

                            /*if (selectedAssociateComplaintsList.size() == 0) {
                                selectedAssociateComplaintsList.add(masterComplaintList.get(selectedItemPosition).getDescription());
                                assosciateComplaints_edit.setText(selectedAssociateComplaintsList.toString().replace("[", "").replace("]", ""));
                                //Toast.makeText(context, "Complaint added", Toast.LENGTH_SHORT).show();
                            } else if (selectedAssociateComplaintsList.size() > 0) {
                                Boolean flag = false;
                                int pos = 0;
                                for (int i = 0; i < selectedAssociateComplaintsList.size(); i++) {
                                    if (masterComplaintList.get(selectedItemPosition).getDescription().trim().equals(selectedAssociateComplaintsList.get(i).trim())) {
                                        flag = true;
                                        pos = i;
                                    }
                                }

                                if (flag == true) {
                                    selectedAssociateComplaintsList.remove(pos);
                                    //Toast.makeText(context, "Complaint removed", Toast.LENGTH_SHORT).show();
                                } else {
                                    selectedAssociateComplaintsList.add(masterComplaintList.get(selectedItemPosition).getDescription());
                                    //Toast.makeText(context, "Complaint added", Toast.LENGTH_SHORT).show();
                                }
                                assosciateComplaints_edit.setText(selectedAssociateComplaintsList.toString().replace("[", "").replace("]", ""));
                                if (selectedAssociateComplaintsList.size() == 0) {
                                    assosciateComplaints_edit.setHint(noComplaints);
                                }
                            } else {
                                assosciateComplaints_edit.setHint(noComplaints);
                            }*/
                        }
                        assosciateComplaints_dropdown.setSelection(0);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        if (Constants.backFromAddEMR == false) {
            refreshList();
            if (localSetting.isNetworkAvailable(context)) {
                new GetComplaints().execute();
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
            }
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void MasterFlagTask() {
        masterflag = masterFlagAdapter.listCurrent();
        masterflag.setFlag(Constants.EMR_COMPLAINT_MASTER_TASK);
        masterFlagAdapter.create(masterflag);
        SchedulerManager.getInstance().runNow(context, MasterTask.class, 1);
    }

    private void refreshList() {
        complaintsLists = complaintsListDBAdapter.listAll(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
        if (complaintsLists != null && complaintsLists.size() > 0) {
            elComplaintsList = complaintsLists.get(0);

            if (elComplaintsList.getChiefComplaints() != null && elComplaintsList.getChiefComplaints().length() > 0) {
                chiefComplaints_edit.setText(elComplaintsList.getChiefComplaints());
                chiefComplaints_edit.setSelection(chiefComplaints_edit.getText().length());
                //selectedChiefComplaintsList = new ArrayList<String>(Arrays.asList(elComplaintsList.getChiefComplaints().split(",")));
            } else {
                chiefComplaints_edit.setHint(noComplaints);
            }

            if (elComplaintsList.getAssosciateComplaints() != null && elComplaintsList.getAssosciateComplaints().length() > 0) {
                assosciateComplaints_edit.setText(elComplaintsList.getAssosciateComplaints());
                assosciateComplaints_edit.setSelection(assosciateComplaints_edit.getText().length());
                //selectedAssociateComplaintsList = new ArrayList<String>(Arrays.asList(elComplaintsList.getAssosciateComplaints().split(",")));
            } else {
                assosciateComplaints_edit.setHint(noComplaints);
            }

            if (elComplaintsList.getIsSync() != null && elComplaintsList.getIsSync().equals("1")) {
                is_record_sync_tv1.setVisibility(View.VISIBLE);
                is_record_sync_tv2.setVisibility(View.VISIBLE);
            } else {
                is_record_sync_tv1.setVisibility(View.GONE);
                is_record_sync_tv2.setVisibility(View.GONE);
            }
        } else {
            chiefComplaints_edit.setHint(noComplaints);
            assosciateComplaints_edit.setHint(noComplaints);
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
            case R.id.menu_toolbar_refresh:
                SetSpinnerAdapter();
                if (localSetting.isNetworkAvailable(context)) {
                    new GetComplaints().execute();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.menu_toolbar_save:
                if (assosciateComplaints_edit.getText().toString().equals("") && assosciateComplaints_edit.getText().toString().trim().length() == 0
                        && chiefComplaints_edit.getText().toString().equals(noComplaints) && chiefComplaints_edit.getText().toString().trim().length() == 0) {
                    Toast.makeText(context, "Please add any complaints", Toast.LENGTH_SHORT).show();
                } else {
                    AddComplaintView();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void AddComplaintView() {
        try {
            new AlertDialog
                    .Builder(context)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Do you really want to save complaint details?")
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String CurrentDate = simpleDF.format(c.getTime());
                            elComplaintsList.setUnitID(bookAppointmentArrayList.get(0).getDoctorUnitID());
                            elComplaintsList.setVisitID(bookAppointmentArrayList.get(0).getVisitID());
                            elComplaintsList.setVisitUnitID(bookAppointmentArrayList.get(0).getUnitID());
                            elComplaintsList.setPatientID(bookAppointmentArrayList.get(0).getPatientID());
                            elComplaintsList.setPatientUnitID(bookAppointmentArrayList.get(0).getUnitID());
                            elComplaintsList.setDoctorID(bookAppointmentArrayList.get(0).getDoctorID());
                            elComplaintsList.setChiefComplaints(chiefComplaints_edit.getText().toString());
                            elComplaintsList.setAssosciateComplaints(assosciateComplaints_edit.getText().toString());
                            elComplaintsList.setStatus("1");
                            elComplaintsList.setCreatedUnitID(bookAppointmentArrayList.get(0).getDoctorUnitID());
                            elComplaintsList.setUpdatedUnitID(bookAppointmentArrayList.get(0).getDoctorUnitID());
                            elComplaintsList.setAddedBy(bookAppointmentArrayList.get(0).getDoctorID());
                            elComplaintsList.setAddedDateTime(CurrentDate);
                            elComplaintsList.setUpdatedBy(bookAppointmentArrayList.get(0).getDoctorID());
                            elComplaintsList.setUpdatedDateTime(CurrentDate);
                            elComplaintsList.setIsSync("1");

                            /*if (selectedChiefComplaintsList != null && selectedChiefComplaintsList.size() > 0) {
                                String strChiefComplaint = "";
                                for (int i = 0; i < selectedChiefComplaintsList.size(); i++) {
                                    if (i == 0) {
                                        strChiefComplaint = selectedChiefComplaintsList.get(i);
                                    } else {
                                        strChiefComplaint = strChiefComplaint + "," + selectedChiefComplaintsList.get(i).trim();
                                    }
                                    elComplaintsList.setChiefComplaints(strChiefComplaint);
                                }
                            } else {
                                elComplaintsList.setChiefComplaints("");
                            }

                            if (selectedAssociateComplaintsList != null && selectedAssociateComplaintsList.size() > 0) {
                                String strAssComplaint = "";
                                for (int i = 0; i < selectedAssociateComplaintsList.size(); i++) {
                                    if (i == 0) {
                                        strAssComplaint = selectedAssociateComplaintsList.get(i);
                                    } else {
                                        strAssComplaint = strAssComplaint + "," + selectedAssociateComplaintsList.get(i).trim();
                                    }
                                    elComplaintsList.setAssosciateComplaints(strAssComplaint);
                                }
                            } else {
                                elComplaintsList.setAssosciateComplaints("");
                            }*/
                            callToWebservice();
                            //new AddUpdateComplaints().execute();
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
            new AddUpdateComplaints().execute();
        } else {
            new AlertDialog
                    .Builder(context)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage(context.getResources().getString(R.string.offline_net_alert))
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            complaintsListDBAdapter.delete(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                            complaintsListDBAdapter.createUnSync(elComplaintsList);
                            refreshList();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(R.mipmap.ic_launcher)
                    .show();
        }
    }

    private class AddUpdateComplaints extends AsyncTask<Void, Void, String> {
        private TransparentProgressDialog progressDialog;
        private JsonObjectMapper objMapper = null;
        private WebServiceConsumer serviceConsumer = null;
        private Response response = null;
        private String jSonData = "";
        private int responseCode = 0;
        private String responseString = "";
        private String responseMSG = "";

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                objMapper = new JsonObjectMapper();
                jSonData = objMapper.unMap(elComplaintsList);
                serviceConsumer = new WebServiceConsumer(context, null, null, null);
                response = serviceConsumer.POST(Constants.COMPLAINTS_ADD_UPDATE_URL, jSonData);
                if (response != null) {
                    responseCode = response.code();
                    responseMSG = response.message().toString();
                    responseString = response.body().string();
                    Log.d(Constants.TAG, "Response code:" + responseCode);
                    Log.d(Constants.TAG, "Response MSG:" + responseMSG);
                    Log.d(Constants.TAG, "Response string:" + responseString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            localSetting.hideDialog(progressDialog);
            if (responseCode == Constants.HTTP_CREATED_201 && responseMSG.equals("Created")) {
                ArrayList<ComplaintsList> elComplaintsList = objMapper.map(responseString, ComplaintsList.class);
                if (elComplaintsList != null && elComplaintsList.size() > 0) {
                    for (int index = 0; index < elComplaintsList.size(); index++) {
                        complaintsListDBAdapter.create(elComplaintsList.get(index));
                    }
                }
                Toast.makeText(context, "Complaints added successfully.", Toast.LENGTH_SHORT).show();
            } else if (responseCode == Constants.HTTP_OK_200 && responseMSG.equals("OK")) {
                ArrayList<ComplaintsList> elComplaintsList = objMapper.map(responseString, ComplaintsList.class);
                if (elComplaintsList != null && elComplaintsList.size() > 0) {
                    for (int index = 0; index < elComplaintsList.size(); index++) {
                        complaintsListDBAdapter.create(elComplaintsList.get(index));
                    }
                }
                Toast.makeText(context, "Complaints updated successfully.", Toast.LENGTH_SHORT).show();
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
                                complaintsListDBAdapter.delete(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                                complaintsListDBAdapter.createUnSync(elComplaintsList);
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

    private class GetComplaints extends AsyncTask<Void, Void, String> {
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
                response = webServiceConsumer.GET(Constants.COMPLAINT_LIST_URL + doctorProfileList.get(0).getUnitID()
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
                complaintsLists = jsonObjectMapper.map(responseString, ComplaintsList.class);
                if (complaintsLists != null && complaintsLists.size() > 0) {
                    complaintsListDBAdapter.delete(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                    for (int index = 0; index < complaintsLists.size(); index++) {
                        complaintsListDBAdapter.create(complaintsLists.get(index));
                    }
                }
            } else if (responseCode == Constants.HTTP_DELETED_OK_204) {
                Toast.makeText(context, localSetting.handleError(responseCode), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, localSetting.handleError(responseCode), Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
            refreshList();
        }
    }
}


