package com.palash.healthspring.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.palash.healthspring.R;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.BookAppointment;
import com.palash.healthspring.fragment.CPOEInvestigationFragment;
import com.palash.healthspring.fragment.CPOEPrescriptionFragment;
import com.palash.healthspring.fragment.ComplaintsFragment;
import com.palash.healthspring.fragment.DiagnosisFragment;
import com.palash.healthspring.fragment.FollowUpFragment;
import com.palash.healthspring.fragment.ReferralFragment;
import com.palash.healthspring.fragment.VitalsFragment;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;

import java.util.ArrayList;


/**
 * Created by manishas on 7/21/2016.
 */
public class EMRNavigationDrawerActivity extends AppCompatActivity {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapter;

    private ArrayList<BookAppointment> bookAppointmentArrayList;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TextView patient_name;
    private TextView patient_bloodgroup;
    private TextView patient_gender;
    private TextView patient_maritalstatus;
    private TextView patient_contact;
    private TextView patient_email;

    private String Title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_main);
        InitSetting();
        InitView();
        InitNavigation();
    }

    private void InitSetting() {
        try {
            context = EMRNavigationDrawerActivity.this;
            localSetting = new LocalSetting();
            localSetting.Init(context);
            localSetting.Load();
            databaseContract = new DatabaseContract(context);
            databaseAdapter = new DatabaseAdapter(databaseContract);

            bookAppointmentAdapter = databaseAdapter.new BookAppointmentAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView() {
        try {
            toolbar = (Toolbar) findViewById(R.id.emr_toolbar);
            setSupportActionBar(toolbar);
            navigationView = (NavigationView) findViewById(R.id.navigation_view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitNavigation() {
        try {
            VitalsFragment fragment = new VitalsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flContent, fragment);
            fragmentTransaction.commit();
            setTitle(R.string.emr_vitals);
            Title = context.getResources().getString(R.string.emr_vitals);
            Constants.backFromAddEMR = false;

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    if (menuItem.isChecked()) menuItem.setChecked(false);
                    else menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                    switch (menuItem.getItemId()) {
                        case R.id.action_vitals:
                            VitalsFragment vitalsFragment = new VitalsFragment();
                            FragmentTransaction vitalsFragmentTransaction = getSupportFragmentManager().beginTransaction();
                            vitalsFragmentTransaction.replace(R.id.flContent, vitalsFragment);
                            vitalsFragmentTransaction.commit();
                            setTitle(R.string.emr_vitals);
                            Title = context.getResources().getString(R.string.emr_vitals);
                            Constants.backFromAddEMR = false;
                            return true;
                        case R.id.action_diagnosis:
                            DiagnosisFragment diagnosisFragment = new DiagnosisFragment();
                            FragmentTransaction diagnosisFragmentTransaction = getSupportFragmentManager().beginTransaction();
                            diagnosisFragmentTransaction.replace(R.id.flContent, diagnosisFragment);
                            diagnosisFragmentTransaction.commit();
                            setTitle(R.string.emr_diagnosis);
                            Title = context.getResources().getString(R.string.emr_diagnosis);
                            Constants.backFromAddEMR = false;
                            return true;
                        case R.id.action_cpoe_service:
                            CPOEInvestigationFragment cpoeServiceFragment = new CPOEInvestigationFragment();
                            FragmentTransaction cpoeServiceFragmentTransaction = getSupportFragmentManager().beginTransaction();
                            cpoeServiceFragmentTransaction.replace(R.id.flContent, cpoeServiceFragment);
                            cpoeServiceFragmentTransaction.commit();
                            setTitle(R.string.emr_cpoe_investigation);
                            Title = context.getResources().getString(R.string.emr_cpoe_investigation);
                            Constants.backFromAddEMR = false;
                            return true;
                        case R.id.action_cpoe_medicine:
                            CPOEPrescriptionFragment cpoeMedicineFragment = new CPOEPrescriptionFragment();
                            FragmentTransaction cpoeMedicineFragmentTransaction = getSupportFragmentManager().beginTransaction();
                            cpoeMedicineFragmentTransaction.replace(R.id.flContent, cpoeMedicineFragment);
                            cpoeMedicineFragmentTransaction.commit();
                            setTitle(R.string.emr_cpoe_medicine);
                            Title = context.getResources().getString(R.string.emr_cpoe_medicine);
                            Constants.backFromAddEMR = false;
                            return true;
                        case R.id.action_cpoe_complaint:
                            ComplaintsFragment complaintsFragment = new ComplaintsFragment();
                            FragmentTransaction complaintsFragmentTransaction = getSupportFragmentManager().beginTransaction();
                            complaintsFragmentTransaction.replace(R.id.flContent, complaintsFragment);
                            complaintsFragmentTransaction.commit();
                            setTitle(R.string.emr_complaint);
                            Title = context.getResources().getString(R.string.emr_complaint);
                            Constants.backFromAddEMR = false;
                            return true;
                        case R.id.action_followup:
                            FollowUpFragment followUpFragment = new FollowUpFragment();
                            FragmentTransaction followupFragmentTransaction = getSupportFragmentManager().beginTransaction();
                            followupFragmentTransaction.replace(R.id.flContent, followUpFragment);
                            followupFragmentTransaction.commit();
                            setTitle(R.string.emr_followup);
                            Title = context.getResources().getString(R.string.emr_followup);
                            Constants.backFromAddEMR = false;
                            return true;
                        case R.id.action_refarral:
                            ReferralFragment referralFragment = new ReferralFragment();
                            FragmentTransaction referralFragmentTransaction = getSupportFragmentManager().beginTransaction();
                            referralFragmentTransaction.replace(R.id.flContent, referralFragment);
                            referralFragmentTransaction.commit();
                            setTitle(R.string.emr_referral);
                            Title = context.getResources().getString(R.string.emr_referral);
                            Constants.backFromAddEMR = false;
                            return true;
                        default:
                            Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                            return true;
                    }
                }
            });

            // Initializing Drawer Layout and ActionBarToggle
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView.setBackgroundResource(R.color.white);
            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    drawerInitView(drawerView);
                    setTitle(Title);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    drawerInitView(drawerView);
                    setTitle(R.string.patient_emr);
                }
            };

            drawerLayout.setDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
            navigationView.getMenu().getItem(0).setChecked(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawerInitView(View view) {
        try {
            patient_name = (TextView) view.findViewById(R.id.patient_name);
            patient_bloodgroup = (TextView) view.findViewById(R.id.patient_booldgroup);
            patient_gender = (TextView) view.findViewById(R.id.patient_gender);
            patient_maritalstatus = (TextView) view.findViewById(R.id.patient_maritalstatus);
            patient_contact = (TextView) view.findViewById(R.id.patient_contact);
            patient_email = (TextView) view.findViewById(R.id.patient_email);
            bookAppointmentArrayList = bookAppointmentAdapter.listLast();

            if (bookAppointmentArrayList != null) {
                BookAppointment bookAppointment = bookAppointmentArrayList.get(0);
                String FirstName = bookAppointment.getFirstName();
                String MiddleName = bookAppointment.getMiddleName();
                String LastName = bookAppointment.getLastName();
                String Name;
                if (MiddleName != null && MiddleName.length() > 0) {
                    Name = FirstName + " " + MiddleName + " " + LastName;
                } else {
                    Name = FirstName + " " + LastName;
                }

                patient_name.setText(Name);
                if (bookAppointment.getContact1() != null && bookAppointment.getContact1().length() > 0) {
                    patient_contact.setVisibility(View.VISIBLE);
                    patient_contact.setText(bookAppointment.getContact1());
                } else {
                    patient_contact.setVisibility(View.GONE);
                }

                if (bookAppointment.getEmailId() != null && bookAppointment.getEmailId().length() > 0) {
                    patient_email.setVisibility(View.VISIBLE);
                    patient_email.setText(bookAppointment.getEmailId());
                } else {
                    patient_email.setVisibility(View.GONE);
                }

                if (bookAppointment.getBloodGroupID() != null && bookAppointment.getBloodGroupID().length() > 0) {
                    String Bloodgroup = bookAppointment.getBloodGroupID();
                    String[] splited = Bloodgroup.split("\\s+");
                    Bloodgroup = splited[0];
                    String mark = splited[1];

                    if (mark.equals("Negative")) {
                        mark = "-";
                    } else {
                        mark = "+";
                    }
                    patient_bloodgroup.setVisibility(View.VISIBLE);
                    patient_bloodgroup.setText(Bloodgroup + "" + mark);
                } else {
                    patient_bloodgroup.setVisibility(View.GONE);
                }

                if (bookAppointment.getGenderID() != null && bookAppointment.getGenderID().length() > 0) {
                    patient_gender.setText(bookAppointment.getGenderID());
                    patient_gender.setVisibility(View.VISIBLE);
                } else {
                    patient_gender.setVisibility(View.GONE);
                }

                if (bookAppointment.getMaritalStatusID() != null && bookAppointment.getMaritalStatusID().length() > 0) {
                    patient_maritalstatus.setText(bookAppointment.getMaritalStatusID());
                    patient_maritalstatus.setVisibility(View.VISIBLE);
                } else {
                    patient_maritalstatus.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
