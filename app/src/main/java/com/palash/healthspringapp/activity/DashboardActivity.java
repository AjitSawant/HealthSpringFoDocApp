package com.palash.healthspringapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buzzbox.mob.android.scheduler.SchedulerManager;
import com.palash.healthspringapp.R;
import com.palash.healthspringapp.adapter.SpinnerAdapter;
import com.palash.healthspringapp.api.JsonObjectMapper;
import com.palash.healthspringapp.api.WebServiceConsumer;
import com.palash.healthspringapp.database.DatabaseAdapter;
import com.palash.healthspringapp.database.DatabaseContract;
import com.palash.healthspringapp.entity.CPOEPrescription;
import com.palash.healthspringapp.entity.CPOEService;
import com.palash.healthspringapp.entity.ComplaintsList;
import com.palash.healthspringapp.entity.DiagnosisList;
import com.palash.healthspringapp.entity.DoctorProfile;
import com.palash.healthspringapp.entity.ELSynchOfflineData;
import com.palash.healthspringapp.entity.ELUnitMaster;
import com.palash.healthspringapp.entity.ReferralDoctorPerService;
import com.palash.healthspringapp.entity.VitalsList;
import com.palash.healthspringapp.utilities.Constants;
import com.palash.healthspringapp.utilities.LocalSetting;
import com.palash.healthspringapp.utilities.TextDrawable;
import com.palash.healthspringapp.utilities.TransparentProgressDialog;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseAdapter databaseAdapter;
    private DatabaseContract databaseContract;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.UnitMasterAdapter unitMasterAdapter;
    private DatabaseAdapter.VitalsListAdapter vitalsListDBAdapter;
    private DatabaseAdapter.DiagnosisListAdapter diagnosisListAdapter;
    private DatabaseAdapter.CPOEServiceAdapter cpoeServiceAdapter;
    private DatabaseAdapter.CPOEMedicineAdapter cpoeMedicineAdapter;
    private DatabaseAdapter.ComplaintsListDBAdapter complaintsListDBAdapter;
    private DatabaseAdapter.ReferralServiceListDBAdapter referralServiceListDBAdapter;

    private SpinnerAdapter.UnitMasterListAdapter unitMasterListAdapter;

    private ArrayList<DoctorProfile> listProfile;
    private ArrayList<ELUnitMaster> listELUnitMaster;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private LinearLayout layout_appointments;
    private LinearLayout layout_patientqueue;
    private LinearLayout layout_searchpatient;
    private MaterialSpinner unitMasterSpinner;

    private static int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        InitSetting();
        InitView();
    }

    private void InitSetting() {
        try {
            context = this;
            localSetting = new LocalSetting();
            databaseContract = new DatabaseContract(context);
            databaseAdapter = new DatabaseAdapter(databaseContract);
            doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();
            unitMasterAdapter = databaseAdapter.new UnitMasterAdapter();
            vitalsListDBAdapter = databaseAdapter.new VitalsListAdapter();
            diagnosisListAdapter = databaseAdapter.new DiagnosisListAdapter();
            cpoeServiceAdapter = databaseAdapter.new CPOEServiceAdapter();
            cpoeMedicineAdapter = databaseAdapter.new CPOEMedicineAdapter();
            complaintsListDBAdapter = databaseAdapter.new ComplaintsListDBAdapter();
            referralServiceListDBAdapter = databaseAdapter.new ReferralServiceListDBAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView() {
        try {
            // Initializing Toolbar and setting it as the actionbar
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            layout_appointments = (LinearLayout) findViewById(R.id.layout_appointments);
            layout_patientqueue = (LinearLayout) findViewById(R.id.layout_patient_queue);
            layout_searchpatient = (LinearLayout) findViewById(R.id.layout_search_patient);
            unitMasterSpinner = (MaterialSpinner) findViewById(R.id.unitMasterSpinner);
            navigationView = (NavigationView) findViewById(R.id.navigation_view);
            drawerLayout = (DrawerLayout) findViewById(R.id.main_activity_drawer_layout);

            layout_patientqueue.setOnClickListener(this);
            layout_appointments.setOnClickListener(this);
            layout_searchpatient.setOnClickListener(this);
            navigationView.setNavigationItemSelectedListener(this);

            navigationView.setBackgroundResource(R.color.white);
            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    RelativeLayout headerlayout = (RelativeLayout) drawerView.findViewById(R.id.headerlayout);
                    headerlayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                        }
                    });

                    ImageView doctor_profile_image = (ImageView) drawerView.findViewById(R.id.doctor_profile_image);
                    TextView doctor_name = (TextView) drawerView.findViewById(R.id.doctor_name);
                    TextView doctor_education = (TextView) drawerView.findViewById(R.id.doctor_education);
                    TextView doctor_email = (TextView) drawerView.findViewById(R.id.doctor_email);

                    if (listProfile != null && listProfile.size() > 0) {
                        TextDrawable drawable = TextDrawable.builder().buildRound(listProfile.get(0).getFirstName().charAt(0) + "".toString() + listProfile.get(0).getLastName().charAt(0) + "".toString(), Color.parseColor("#29B6F6"));
                        doctor_profile_image.setImageDrawable(drawable);
                        doctor_name.setText(listProfile.get(0).getFirstName() + " " + listProfile.get(0).getLastName());
                        doctor_education.setText(listProfile.get(0).getEducation());
                        doctor_email.setText(listProfile.get(0).getEmailId());
                    }
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    RelativeLayout headerlayout = (RelativeLayout) drawerView.findViewById(R.id.headerlayout);
                    headerlayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                            drawerLayout.closeDrawers();
                        }
                    });

                    ImageView doctor_profile_image = (ImageView) drawerView.findViewById(R.id.doctor_profile_image);
                    TextView doctor_name = (TextView) drawerView.findViewById(R.id.doctor_name);
                    TextView doctor_education = (TextView) drawerView.findViewById(R.id.doctor_education);
                    TextView doctor_email = (TextView) drawerView.findViewById(R.id.doctor_email);

                    if (listProfile != null && listProfile.size() > 0) {
                        TextDrawable drawable = TextDrawable.builder().buildRound(listProfile.get(0).getFirstName().charAt(0) + "".toString() + listProfile.get(0).getLastName().charAt(0) + "".toString(), Color.parseColor("#29B6F6"));
                        doctor_profile_image.setImageDrawable(drawable);
                        doctor_name.setText(listProfile.get(0).getFirstName() + " " + listProfile.get(0).getLastName());
                        doctor_education.setText(listProfile.get(0).getEducation());
                        doctor_email.setText(listProfile.get(0).getEmailId());
                    }
                }
            };

            drawerLayout.setDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();

            unitMasterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    int position = unitMasterSpinner.getSelectedItemPosition();
                    if (listELUnitMaster != null && listELUnitMaster.size() > 0) {
                        DoctorProfile doctorProfile = doctorProfileAdapter.listAll().get(0);
                        doctorProfile.setUnitID(listELUnitMaster.get(position).getUnitID());
                        doctorProfile.setUnitName(listELUnitMaster.get(position).getUnitDesc());
                        doctorProfileAdapter.update(doctorProfile);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void CheckSynchDate() {
        elSynchOfflineDataList = synchOfflineDataAdapter.listAll();
        if (elSynchOfflineDataList != null && elSynchOfflineDataList.size() > 0) {
            String todayDate = new SimpleDateFormat(Constants.OFFLINE_DATE, Locale.getDefault()).format(new Date());
            if (todayDate.equals(elSynchOfflineDataList.get(0).getOfflineLastDate())) {
                Toast.makeText(context, "All look good", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "All un synchronize data will be clear", Toast.LENGTH_SHORT).show();
                synchOfflineDataAdapter.DeleteOfflineEMR();
                //synchOfflineDataAdapter.updateDate();
            }
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        listProfile = doctorProfileAdapter.listAll();
        setUnitMasterData();
    }

    private void setUnitMasterData() {
        listELUnitMaster = unitMasterAdapter.listAll();
        if (Constants.isFromLogin) {
            GetUnitMasterList();
            Constants.isFromLogin = false;
        } else {
            if (listELUnitMaster == null && listELUnitMaster.size() == 0) {
                GetUnitMasterList();
            } else {
                RefreshUnitMatserSpinnerData();
            }
        }
    }

    private void RefreshUnitMatserSpinnerData() {
        listELUnitMaster = unitMasterAdapter.listAll();
        listProfile = doctorProfileAdapter.listAll();
        if (listELUnitMaster != null && listELUnitMaster.size() > 0) {
            unitMasterListAdapter = new SpinnerAdapter.UnitMasterListAdapter(context, listELUnitMaster);
            unitMasterSpinner.setAdapter(unitMasterListAdapter);
            unitMasterListAdapter.notifyDataSetChanged();

            if (listProfile != null && listProfile.size() > 0 && listProfile.get(0).getUnitID() != null && listProfile.get(0).getUnitID().length() > 0) {
                try {
                    boolean matchFlag = false;
                    int pos = 0;
                    for (int i = 0; i < listELUnitMaster.size(); i++) {
                        if (listProfile.get(0).getUnitID().equals(listELUnitMaster.get(i).getUnitID())) {
                            matchFlag = true;
                            pos = i;
                        }
                    }
                    if (matchFlag == true) {
                        //pos = pos + 1;
                        unitMasterSpinner.setSelection(pos);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.action_logout:
                new AlertDialog
                        .Builder(context)
                        .setTitle(getResources().getString(R.string.app_name))
                        .setMessage("Do you really want to logout?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SchedulerManager.getInstance().stopAll(context);
                                doctorProfileAdapter.LogOut(listProfile.get(0).getDoctorID(), Constants.STATUS_LOG_OUT);
                                startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }
                        })
                        .setIcon(R.mipmap.ic_launcher)
                        .show();
                return true;
            case R.id.action_setting:
                Intent intent_setting = new Intent(DashboardActivity.this, SettingActivity.class);
                startActivity(intent_setting);
                return true;
            default:
                Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_profile:
                startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                break;
            case R.id.layout_appointments:
                startActivity(new Intent(DashboardActivity.this, AppointmentListActivity.class));
                break;
            case R.id.layout_patient_queue:
                startActivity(new Intent(DashboardActivity.this, PatientQueueActivity.class));
                break;
            case R.id.layout_search_patient:
                startActivity(new Intent(DashboardActivity.this, PatientListActivity.class));
                Constants.refreshPatient = true;
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard_drawer, menu);
        menu.findItem(R.id.action_setting).setVisible(false);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_synch_master_data).setVisible(true);
        menu.findItem(R.id.action_synch_offline_data).setVisible(true);
        menu.findItem(R.id.menu_toolbar_refresh).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toolbar_refresh:
                GetUnitMasterList();
                return true;
            case R.id.action_synch_master_data:
                if (localSetting.isNetworkAvailable(context)) {
                    startActivity(new Intent(context, SynchronizationActivity.class).putExtra("reason", "master data"));
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_synch_offline_data:
                if (localSetting.isNetworkAvailable(context)) {
                    ArrayList<VitalsList> vitalsArrayList = vitalsListDBAdapter.listAllUnSync("");
                    ArrayList<DiagnosisList> diagnosisArrayList = diagnosisListAdapter.listAllUnSync();
                    ArrayList<CPOEService> cpoeServiceArrayList = cpoeServiceAdapter.listAllUnSync();
                    ArrayList<CPOEPrescription> cpoeMedicineArrayList = cpoeMedicineAdapter.listAllUnSync();
                    ArrayList<ComplaintsList> complaintsArrayList = complaintsListDBAdapter.listAllUnSync();
                    ArrayList<ReferralDoctorPerService> referralServiceArrayList = referralServiceListDBAdapter.listAllUnSync();

                    if ((vitalsArrayList != null && vitalsArrayList.size() > 0) || (diagnosisArrayList != null && diagnosisArrayList.size() > 0)
                            || (cpoeServiceArrayList != null && cpoeServiceArrayList.size() > 0) || (cpoeMedicineArrayList != null && cpoeMedicineArrayList.size() > 0)
                            || (complaintsArrayList != null && complaintsArrayList.size() > 0) || (referralServiceArrayList != null && referralServiceArrayList.size() > 0)) {

                        startActivity(new Intent(context, SynchronizationActivity.class).putExtra("reason", "offline data"));
                    } else {
                        Toast.makeText(context, "No data available. All data synchronized.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        i++;
        if (i == 2) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(this, "Press one more time to exit", Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                i = 0;
            }
        }, 2000);
    }

    private void GetUnitMasterList() {
        if (localSetting.isNetworkAvailable(context)) {
            new GetUnitMasterTask().execute();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
        }
    }

    private class GetUnitMasterTask extends AsyncTask<Void, Void, String> {
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
                response = webServiceConsumer.GET(Constants.GET_UNIT_MASTER_URL + listProfile.get(0).getID());
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
                listELUnitMaster = jsonObjectMapper.map(responseString, ELUnitMaster.class);
                if (listELUnitMaster != null && listELUnitMaster.size() > 0 && listELUnitMaster.size() != unitMasterAdapter.TotalCount()) {
                    unitMasterAdapter.delete();
                    for (int index = 0; index < listELUnitMaster.size(); index++) {
                        unitMasterAdapter.create(listELUnitMaster.get(index));
                    }
                }
            } else if (responseCode == Constants.HTTP_NO_RECORD_FOUND_OK_204) {
                unitMasterAdapter.delete();
                Toast.makeText(context, localSetting.handleError(responseCode), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, localSetting.handleError(responseCode), Toast.LENGTH_SHORT).show();
            }
            localSetting.hideDialog(progressDialog);
            setDefaultUnitMatserSpinnerData();
            super.onPostExecute(result);
        }
    }

    private void setDefaultUnitMatserSpinnerData() {
        listELUnitMaster = unitMasterAdapter.listAll();
        if (listELUnitMaster != null && listELUnitMaster.size() > 0) {
            unitMasterListAdapter = new SpinnerAdapter.UnitMasterListAdapter(context, listELUnitMaster);
            unitMasterSpinner.setAdapter(unitMasterListAdapter);
            unitMasterListAdapter.notifyDataSetChanged();
            boolean matchFlag = false;
            int pos = 0;
            for (int i = 0; i < listELUnitMaster.size(); i++) {
                if (listELUnitMaster.get(i).getIsDefault().equals("1") || listELUnitMaster.get(i).getIsDefault().equals("True")) {
                    matchFlag = true;
                    pos = i;
                }
            }
            if (matchFlag == true) {
                //pos = pos + 1;
                unitMasterSpinner.setSelection(pos);
            }
        }
    }

    /*private void SynchOfflineData() {
        if (localSetting.isNetworkAvailable(context)) {

            ArrayList<VitalsList> vitalsArrayList = vitalsListDBAdapter.listAllUnSync("");
            ArrayList<DiagnosisList> diagnosisArrayList = diagnosisListAdapter.listAllUnSync();
            ArrayList<CPOEService> cpoeServiceArrayList = cpoeServiceAdapter.listAllUnSync();
            ArrayList<CPOEPrescription> cpoeMedicineArrayList = cpoeMedicineAdapter.listAllUnSync();
            ArrayList<ComplaintsList> complaintsArrayList = complaintsListDBAdapter.listAllUnSync();
            ArrayList<ReferralDoctorPerService> referralServiceArrayList = referralServiceListDBAdapter.listAllUnSync();

            if ((vitalsArrayList != null && vitalsArrayList.size() > 0) || (diagnosisArrayList != null && diagnosisArrayList.size() > 0)
                    || (cpoeServiceArrayList != null && cpoeServiceArrayList.size() > 0) || (cpoeMedicineArrayList != null && cpoeMedicineArrayList.size() > 0)
                    || (complaintsArrayList != null && complaintsArrayList.size() > 0) || (referralServiceArrayList != null && referralServiceArrayList.size() > 0)) {

                try {
                    Flag flag = flagAdapter.listCurrent();
                    flag.setFlag(Constants.ONLINE_SYNC);
                    flagAdapter.create(flag);
                    SchedulerManager.getInstance().runNow(context, SynchronizationTask.class, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }*/
}
