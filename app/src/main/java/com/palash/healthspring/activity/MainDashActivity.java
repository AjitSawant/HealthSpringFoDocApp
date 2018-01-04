package com.palash.healthspring.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.buzzbox.mob.android.scheduler.SchedulerManager;
import com.palash.healthspring.R;
import com.palash.healthspring.adapter.SpinnerAdapter;
import com.palash.healthspring.api.JsonObjectMapper;
import com.palash.healthspring.api.WebServiceConsumer;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.CPOEPrescription;
import com.palash.healthspring.entity.CPOEService;
import com.palash.healthspring.entity.ComplaintsList;
import com.palash.healthspring.entity.DiagnosisList;
import com.palash.healthspring.entity.DoctorProfile;
import com.palash.healthspring.entity.ELFollowUp;
import com.palash.healthspring.entity.ELUnitMaster;
import com.palash.healthspring.entity.ReferralDoctorPerService;
import com.palash.healthspring.entity.VitalsList;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;
import com.palash.healthspring.utilities.TransparentProgressDialog;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class MainDashActivity extends AppCompatActivity {

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
    private DatabaseAdapter.PatientFollowUpAdapter patientFollowUpAdapter;

    private static final String TAB_1_TAG = "tab_1";
    private static final String TAB_2_TAG = "tab_2";
    private static final String TAB_3_TAG = "tab_3";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private MaterialSpinner unitMasterSpinner;

    private SpinnerAdapter.UnitMasterListAdapter unitMasterListAdapter;

    private ArrayList<DoctorProfile> listProfile;
    private ArrayList<ELUnitMaster> listELUnitMaster;

    private static int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        InitSetting();
        InitView();
        setUnitMasterData();
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
            patientFollowUpAdapter = databaseAdapter.new PatientFollowUpAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView() {
        // Initializing Toolbar and setting it as the actionbar
        unitMasterSpinner = (MaterialSpinner) findViewById(R.id.unitMasterSpinner);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        viewPager.setCurrentItem(0);
        toolbar.setTitle(context.getResources().getString(R.string.dashboard_patientqueue));
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

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

                    Intent intent = new Intent(Constants.KEY_ForRefreshData); //If you need extra, add: intent.putExtra("extra","something");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        viewPager.setCurrentItem(0);
                        toolbar.setTitle(context.getResources().getString(R.string.dashboard_patientqueue));
                        break;
                    case 1:
                        viewPager.setCurrentItem(1);
                        toolbar.setTitle(context.getResources().getString(R.string.dashboard_appointments));
                        break;
                    case 2:
                        viewPager.setCurrentItem(2);
                        toolbar.setTitle(context.getResources().getString(R.string.dashboard_searchpatient));
                        break;
                    default:
                        viewPager.setCurrentItem(tab.getPosition());
                        toolbar.setTitle("Fragment Star");
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.dash_patient_queue);
        tabLayout.getTabAt(1).setIcon(R.drawable.dash_appointment);
        tabLayout.getTabAt(2).setIcon(R.drawable.dash_search_patient);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new PatientQueueFragment(), getResources().getString(R.string.dashboard_patientqueue));
        adapter.addFrag(new AppointmentFragment(), getResources().getString(R.string.dashboard_appointments));
        adapter.addFrag(new PatientFragment(), getResources().getString(R.string.dashboard_searchpatient));
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard_drawer, menu);
        menu.findItem(R.id.action_setting).setVisible(true);
        menu.findItem(R.id.action_logout).setVisible(true);
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
                    ArrayList<ELFollowUp> FollowUpArrayList = patientFollowUpAdapter.listAllUnSync();

                    if ((vitalsArrayList != null && vitalsArrayList.size() > 0) || (diagnosisArrayList != null && diagnosisArrayList.size() > 0)
                            || (cpoeServiceArrayList != null && cpoeServiceArrayList.size() > 0) || (cpoeMedicineArrayList != null && cpoeMedicineArrayList.size() > 0)
                            || (complaintsArrayList != null && complaintsArrayList.size() > 0) || (referralServiceArrayList != null && referralServiceArrayList.size() > 0)
                            || (FollowUpArrayList != null && FollowUpArrayList.size() > 0)) {
                        startActivity(new Intent(context, SynchronizationActivity.class).putExtra("reason", "offline data"));
                    } else {
                        Toast.makeText(context, "No data available. All data synchronized.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
                }
                return true;
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
                startActivity(new Intent(context, SettingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        listProfile = doctorProfileAdapter.listAll();
        setUnitMasterData();
    }

    private void setUnitMasterData() {
        listELUnitMaster = unitMasterAdapter.listAll();
        if (listELUnitMaster == null && listELUnitMaster.size() == 0) {
            GetUnitMasterList();
        } else {
            RefreshUnitMatserSpinnerData();
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
                response = webServiceConsumer.GET(Constants.GET_UNIT_MASTER_URL);
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
            RefreshUnitMatserSpinnerData();
            super.onPostExecute(result);
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(getResources().getString(R.string.exit_app_alert));
        builder1.setCancelable(true);
        builder1.setTitle(getResources().getString(R.string.app_name));
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                        System.exit(0);
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}