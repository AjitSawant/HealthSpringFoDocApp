package com.palash.healthspringfoapp.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.palash.healthspringfoapp.R;
import com.palash.healthspringfoapp.api.JsonObjectMapper;
import com.palash.healthspringfoapp.api.WebServiceConsumer;
import com.palash.healthspringfoapp.database.DatabaseAdapter;
import com.palash.healthspringfoapp.database.DatabaseContract;
import com.palash.healthspringfoapp.entity.DoctorProfile;
import com.palash.healthspringfoapp.entity.Patient;
import com.palash.healthspringfoapp.utilities.Constants;
import com.palash.healthspringfoapp.utilities.LocalSetting;
import com.palash.healthspringfoapp.utilities.TransparentProgressDialog;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegistrationDashActivity extends AppCompatActivity {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseAdapter databaseAdapter;
    private DatabaseContract databaseContract;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Patient elPatient;
    private ArrayList<DoctorProfile> listProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registartion_tab);
        InitSetting();
        InitView();
    }

    private void InitSetting() {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            context = this;
            localSetting = new LocalSetting();
            databaseContract = new DatabaseContract(context);
            databaseAdapter = new DatabaseAdapter(databaseContract);
            doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();
            listProfile = doctorProfileAdapter.listAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
        //getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        //setupTabIcons();
    }

    /*private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.dash_patient_queue);
        tabLayout.getTabAt(1).setIcon(R.drawable.dash_appointment);
    }*/

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new RegistrationPatientInformationFragment(), getResources().getString(R.string.dashboard_patientinfo));
        adapter.addFrag(new RegistrationSponsorFragment(), getResources().getString(R.string.dashboard_sponsor_info));
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.menu_toolbar_save).setVisible(true);
        return super.onCreateOptionsMenu(menu);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toolbar_save:
                if (listProfile != null && listProfile.size() > 0) {
                    if (RegistrationPatientInformationFragment.validateControls(context)) {
                        elPatient = new Patient();
                        elPatient = RegistrationPatientInformationFragment.PatientInformation();
                        elPatient.setUnitID(listProfile.get(0).getUnitID());
                        elPatient.setAddedBy(listProfile.get(0).getID());
                        elPatient.setCreatedUnitID(listProfile.get(0).getUnitID());

                        if (RegistrationSponsorFragment.validateControls(context)) {
                            Patient elPatient1 = new Patient();
                            elPatient1 = RegistrationSponsorFragment.SponsorInformation();
                            elPatient.setCategoryL1ID(elPatient1.getCategoryL1ID());
                            elPatient.setCompanyID(elPatient1.getCompanyID());
                            elPatient.setCategoryL2ID(elPatient1.getCategoryL2ID());
                            elPatient.setCategoryL3ID(elPatient1.getCategoryL3ID());
                            elPatient.setHealthSpringReferralID(elPatient1.getHealthSpringReferralID());

                            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Are you sure?")
                                    .setContentText("Do you really want to register patient!")
                                    .setConfirmText("Yes")
                                    .setCancelText("No")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            if (localSetting.isNetworkAvailable(context)) {
                                                new PatientRegistrationTask().execute();
                                            } else {
                                                Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    })
                                    .setCancelClickListener(null)
                                    .show();
                        }
                    }
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class PatientRegistrationTask extends AsyncTask<Void, Void, String> {
        private int responseCode = 0;
        private TransparentProgressDialog progressDialog = null;
        private JsonObjectMapper objMapper = null;
        private WebServiceConsumer serviceConsumer = null;
        private Response response = null;
        private String jSonData = null;

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String responseString = "";
            try {
                objMapper = new JsonObjectMapper();
                jSonData = objMapper.unMap(elPatient);
                Log.d(Constants.TAG + "jSonData:", "" + jSonData);
                serviceConsumer = new WebServiceConsumer(context, null, null, null);
                response = serviceConsumer.POST(Constants.PATIENT_REGISTRATION_URL, jSonData);
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    Log.d(Constants.TAG, "Response code:" + responseCode);
                    Log.d(Constants.TAG, "Response string:" + responseString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                localSetting.hideDialog(progressDialog);
                if (responseCode == Constants.HTTP_CREATED_201) {
                    localSetting.showSuccessAlert(context, getResources().getString(R.string.app_name), "Patient registered successfully!");
                } else if (responseCode == Constants.HTTP_AMBIGUOUS_300) {
                    localSetting.showWarningAlert(context, context.getResources().getString(R.string.opps_alert), "Patient already exits!");
                } else {
                    localSetting.showErrorAlert(context, context.getResources().getString(R.string.opps_alert), localSetting.handleError(responseCode));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }
}