package com.palash.healthspring.fragment;

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
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

import com.buzzbox.mob.android.scheduler.SchedulerManager;
import com.palash.healthspring.R;
import com.palash.healthspring.adapter.CPOEPrescriptionListAdapter;
import com.palash.healthspring.adapter.SpinnerAdapter;
import com.palash.healthspring.api.JsonObjectMapper;
import com.palash.healthspring.api.WebServiceConsumer;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.BookAppointment;
import com.palash.healthspring.entity.Complaint;
import com.palash.healthspring.entity.ComplaintsList;
import com.palash.healthspring.entity.DoctorProfile;
import com.palash.healthspring.entity.Flag;
import com.palash.healthspring.task.MasterTask;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;
import com.palash.healthspring.utilities.TransparentProgressDialog;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by manishas on 7/21/2016.
 */
public class ComplaintsFragment extends Fragment {

    private static final String TAG = ComplaintsFragment.class.getSimpleName();
    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.MasterFlagAdapter masterFlagAdapter;
    private DatabaseAdapter.FlagAdapter flagAdapter;
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
    private Chronometer complaints_list_chronometer;

    private Flag masterflag;
    private ComplaintsList elComplaintsList;

    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat simpleDF;
    private String noComplaints = "No Complaint";

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
            flagAdapter = databaseAdapter.new FlagAdapter();
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
            complaints_list_chronometer = (Chronometer) rootView.findViewById(R.id.complaints_list_chronometer);

            if (localSetting.fragment_name.equals("VisitList")) {
                chiefComplaints_dropdown.setVisibility(View.GONE);
                assosciateComplaints_dropdown.setVisibility(View.GONE);
            }

            simpleDF = new SimpleDateFormat(Constants.TIME_FORMAT, Locale.US);

            complaints_list_chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    refreshList(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                }
            });
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
                            if (selectedChiefComplaintsList.size() == 0) {
                                selectedChiefComplaintsList.add(masterComplaintList.get(selectedItemPosition).getDescription());
                                chiefComplaints_edit.setText(selectedChiefComplaintsList.toString().replace("[", "").replace("]", ""));                                Toast.makeText(context, "Complaint added", Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, "Complaint added", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(context, "Complaint removed", Toast.LENGTH_SHORT).show();
                                } else {
                                    selectedChiefComplaintsList.add(masterComplaintList.get(selectedItemPosition).getDescription());
                                    Toast.makeText(context, "Complaint added", Toast.LENGTH_SHORT).show();
                                }
                                chiefComplaints_edit.setText(selectedChiefComplaintsList.toString().replace("[", "").replace("]", ""));
                            } else {
                                chiefComplaints_edit.setText(noComplaints);
                            }
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
                            if (selectedAssociateComplaintsList.size() == 0) {
                                selectedAssociateComplaintsList.add(masterComplaintList.get(selectedItemPosition).getDescription());
                                assosciateComplaints_edit.setText(selectedAssociateComplaintsList.toString().replace("[", "").replace("]", ""));                                Toast.makeText(context, "Complaint added", Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, "Complaint added", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(context, "Complaint removed", Toast.LENGTH_SHORT).show();
                                } else {
                                    selectedAssociateComplaintsList.add(masterComplaintList.get(selectedItemPosition).getDescription());
                                    Toast.makeText(context, "Complaint added", Toast.LENGTH_SHORT).show();
                                }
                                assosciateComplaints_edit.setText(selectedAssociateComplaintsList.toString().replace("[", "").replace("]", ""));
                            } else {
                                assosciateComplaints_edit.setText(noComplaints);
                            }
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
        if (localSetting.isNetworkAvailable(context)) {
            new GetComplaints().execute();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
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

    private void refreshList(String PatientID, String VisitID) {
        complaintsLists = complaintsListDBAdapter.listAll(PatientID, VisitID);
        if (complaintsLists != null && complaintsLists.size() > 0) {
            elComplaintsList = complaintsLists.get(0);
            if (elComplaintsList.getChiefComplaints() != null && elComplaintsList.getChiefComplaints().length() > 0) {
                chiefComplaints_edit.setText(elComplaintsList.getChiefComplaints());
                selectedChiefComplaintsList = new ArrayList<String>(Arrays.asList(elComplaintsList.getChiefComplaints().split(",")));
            } else {
                chiefComplaints_edit.setText(noComplaints);
            }

            if (elComplaintsList.getAssosciateComplaints() != null && elComplaintsList.getAssosciateComplaints().length() > 0) {
                assosciateComplaints_edit.setText(elComplaintsList.getAssosciateComplaints());
                selectedAssociateComplaintsList = new ArrayList<String>(Arrays.asList(elComplaintsList.getAssosciateComplaints().split(",")));
            } else {
                assosciateComplaints_edit.setText(noComplaints);
            }
        } else {
            chiefComplaints_edit.setText(noComplaints);
            assosciateComplaints_edit.setText(noComplaints);
        }
    }

    /*private void SetComplaintData() {
        complaintsLists = complaintsListDBAdapter.listAll();
        if (complaintsLists != null && complaintsLists.size() > 0) {
            elComplaintsList = complaintsLists.get(0);

            if (elComplaintsList.getChiefComplaints() != null && elComplaintsList.getChiefComplaints().length() > 0) {
                selectedChiefComplaintsList = new ArrayList<String>(Arrays.asList(elComplaintsList.getChiefComplaints().split(",")));
                chiefComplaints_edit.setText(selectedChiefComplaintsList.toString().replace("[", "").replace("]", ""));
            } else {
                chiefComplaints_edit.setText("No Complaints");
            }

            if (elComplaintsList.getAssosciateComplaints() != null && elComplaintsList.getAssosciateComplaints().length() > 0) {
                selectedAssociateComplaintsList = new ArrayList<String>(Arrays.asList(elComplaintsList.getAssosciateComplaints().split(",")));
                assosciateComplaints_edit.setText(selectedAssociateComplaintsList.toString().replace("[", "").replace("]", ""));
            } else {
                assosciateComplaints_edit.setText("No Complaints");
            }
        }
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_current_medication, menu);
        menu.findItem(R.id.menu_current_medication_save).setVisible(true);
        menu.findItem(R.id.menu_current_medication_refresh).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_current_medication_refresh:
                SetSpinnerAdapter();
                new GetComplaints().execute();
                return true;
            case R.id.menu_current_medication_save:
                AddComplaintView();
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

                            if (selectedChiefComplaintsList != null && selectedChiefComplaintsList.size() > 0) {
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
                            }


                            new AddUpdateComplaints().execute();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(R.mipmap.ic_launcher)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
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
                serviceConsumer = new WebServiceConsumer(context, null, null);
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
                localSetting.hideDialog(progressDialog);
                new AlertDialog
                        .Builder(context)
                        .setTitle(getResources().getString(R.string.app_name))
                        .setMessage("Complaints added successfully.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.mipmap.ic_launcher).show();
            } else if (responseCode == Constants.HTTP_OK_200 && responseMSG.equals("OK")) {
                localSetting.hideDialog(progressDialog);
                new AlertDialog
                        .Builder(context)
                        .setTitle(getResources().getString(R.string.app_name))
                        .setMessage("Complaints updated successfully.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.mipmap.ic_launcher).show();
            } else {
                localSetting.hideDialog(progressDialog);
                //localSetting.alertbox(context, localSetting.handleError(responseCode), false);
                new AlertDialog
                        .Builder(context)
                        .setTitle(getResources().getString(R.string.app_name))
                        .setMessage(context.getResources().getString(R.string.offline_alert))
                        .setCancelable(true)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /*if(isUpdate.equals("Yes")) {
                                    vitalsListAdapterDB.updateUnSync(elVitalsList);
                                }else{
                                    vitalsListAdapterDB.createUnSync(elVitalsList);
                                }*/
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
                webServiceConsumer = new WebServiceConsumer(context, null, null);
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
                    for (int index = 0; index < complaintsLists.size(); index++) {
                        complaintsListDBAdapter.create(complaintsLists.get(index));
                    }
                }
            } else if (responseCode == Constants.HTTP_DELETED_OK_204) {
                complaintsListDBAdapter.delete(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
            } else {
                Toast.makeText(context, localSetting.handleError(responseCode), Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);

            refreshList(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
        }
    }
}


