package com.palash.healthspringfoapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.palash.healthspringfoapp.R;
import com.palash.healthspringfoapp.api.JsonObjectMapper;
import com.palash.healthspringfoapp.api.WebServiceConsumer;
import com.palash.healthspringfoapp.database.DatabaseAdapter;
import com.palash.healthspringfoapp.database.DatabaseContract;
import com.palash.healthspringfoapp.entity.BookAppointment;
import com.palash.healthspringfoapp.entity.DaignosisMaster;
import com.palash.healthspringfoapp.entity.ELMedicalHistoryForICE;
import com.palash.healthspringfoapp.entity.MedicienName;
import com.palash.healthspringfoapp.entity.ServiceName;
import com.palash.healthspringfoapp.fragment.CPOEInvestigationFragment;
import com.palash.healthspringfoapp.fragment.CPOEPrescriptionFragment;
import com.palash.healthspringfoapp.fragment.ComplaintsFragment;
import com.palash.healthspringfoapp.fragment.DiagnosisFragment;
import com.palash.healthspringfoapp.fragment.FollowUpFragment;
import com.palash.healthspringfoapp.fragment.ReferralFragment;
import com.palash.healthspringfoapp.fragment.VitalsFragment;
import com.palash.healthspringfoapp.utilities.Constants;
import com.palash.healthspringfoapp.utilities.LocalSetting;
import com.palash.healthspringfoapp.utilities.TransparentProgressDialog;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

public class EMRNavigationDrawerActivity extends AppCompatActivity {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapter;

    private ArrayList<BookAppointment> bookAppointmentArrayList;
    private ArrayList<ELMedicalHistoryForICE> elMedicalHistoryForICEArrayList;

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
    public static ArrayList<DaignosisMaster> daignosisMasterArrayListDB = new ArrayList<>();
    public static ArrayList<ServiceName> serviceNameArrayList = new ArrayList<>();
    public static ArrayList<MedicienName> medicienNameArrayList = new ArrayList<>();
    public static ArrayList<ServiceName> referralServiceNameArrayList = new ArrayList<>();

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
            context = this;
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
            Constants.IsVitals = 1;
            Constants.backFromAddEMR = false;

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    if (menuItem.isChecked()) menuItem.setChecked(false);
                    else menuItem.setChecked(true);

                    switch (menuItem.getItemId()) {
                        case R.id.action_vitals:
                            VitalsFragment vitalsFragment = new VitalsFragment();
                            FragmentTransaction vitalsFragmentTransaction = getSupportFragmentManager().beginTransaction();
                            vitalsFragmentTransaction.replace(R.id.flContent, vitalsFragment);
                            vitalsFragmentTransaction.commit();
                            setTitle(R.string.emr_vitals);
                            Title = context.getResources().getString(R.string.emr_vitals);
                            Constants.IsVitals = 1;
                            Constants.backFromAddEMR = false;
                            drawerLayout.closeDrawers();
                            return true;
                        case R.id.action_diagnosis:
                            DiagnosisFragment diagnosisFragment = new DiagnosisFragment();
                            FragmentTransaction diagnosisFragmentTransaction = getSupportFragmentManager().beginTransaction();
                            diagnosisFragmentTransaction.replace(R.id.flContent, diagnosisFragment);
                            diagnosisFragmentTransaction.commit();
                            setTitle(R.string.emr_diagnosis);
                            Title = context.getResources().getString(R.string.emr_diagnosis);
                            Constants.backFromAddEMR = false;
                            drawerLayout.closeDrawers();
                            return true;
                        case R.id.action_cpoe_service:
                            CPOEInvestigationFragment cpoeServiceFragment = new CPOEInvestigationFragment();
                            FragmentTransaction cpoeServiceFragmentTransaction = getSupportFragmentManager().beginTransaction();
                            cpoeServiceFragmentTransaction.replace(R.id.flContent, cpoeServiceFragment);
                            cpoeServiceFragmentTransaction.commit();
                            setTitle(R.string.emr_cpoe_investigation);
                            Title = context.getResources().getString(R.string.emr_cpoe_investigation);
                            Constants.backFromAddEMR = false;
                            drawerLayout.closeDrawers();
                            return true;
                        case R.id.action_cpoe_medicine:
                            CPOEPrescriptionFragment cpoeMedicineFragment = new CPOEPrescriptionFragment();
                            FragmentTransaction cpoeMedicineFragmentTransaction = getSupportFragmentManager().beginTransaction();
                            cpoeMedicineFragmentTransaction.replace(R.id.flContent, cpoeMedicineFragment);
                            cpoeMedicineFragmentTransaction.commit();
                            setTitle(R.string.emr_cpoe_medicine);
                            Title = context.getResources().getString(R.string.emr_cpoe_medicine);
                            Constants.backFromAddEMR = false;
                            drawerLayout.closeDrawers();
                            return true;
                        case R.id.action_cpoe_complaint:
                            ComplaintsFragment complaintsFragment = new ComplaintsFragment();
                            FragmentTransaction complaintsFragmentTransaction = getSupportFragmentManager().beginTransaction();
                            complaintsFragmentTransaction.replace(R.id.flContent, complaintsFragment);
                            complaintsFragmentTransaction.commit();
                            setTitle(R.string.emr_complaint);
                            Title = context.getResources().getString(R.string.emr_complaint);
                            Constants.backFromAddEMR = false;
                            drawerLayout.closeDrawers();
                            return true;
                        case R.id.action_followup:
                            FollowUpFragment followUpFragment = new FollowUpFragment();
                            FragmentTransaction followupFragmentTransaction = getSupportFragmentManager().beginTransaction();
                            followupFragmentTransaction.replace(R.id.flContent, followUpFragment);
                            followupFragmentTransaction.commit();
                            setTitle(R.string.emr_followup);
                            Title = context.getResources().getString(R.string.emr_followup);
                            Constants.backFromAddEMR = false;
                            drawerLayout.closeDrawers();
                            return true;
                        case R.id.action_refarral:
                            ReferralFragment referralFragment = new ReferralFragment();
                            FragmentTransaction referralFragmentTransaction = getSupportFragmentManager().beginTransaction();
                            referralFragmentTransaction.replace(R.id.flContent, referralFragment);
                            referralFragmentTransaction.commit();
                            setTitle(R.string.emr_referral);
                            Title = context.getResources().getString(R.string.emr_referral);
                            Constants.backFromAddEMR = false;
                            drawerLayout.closeDrawers();
                            return true;
                        case R.id.action_visit_summary:
                            Constants.IsVitals = 0;
                            if (bookAppointmentArrayList != null && bookAppointmentArrayList.get(0).getVisitID() != null && bookAppointmentArrayList.get(0).getVisitID().length() > 0) {
                                String url = localSetting.returnPDFUrl("Summary", bookAppointmentArrayList.get(0).getUnitID(), bookAppointmentArrayList.get(0).getPatientID(),
                                        bookAppointmentArrayList.get(0).getUnitID(), bookAppointmentArrayList.get(0).getVisitID(), "", "", "", "");
                                //context.startActivity(new Intent(context, ViewPDFActivity.class).putExtra("url", url));
                                context.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                            }
                            Constants.backFromAddEMR = false;
                            return false;
                        case R.id.action_ice:
                            Constants.IsVitals = 0;
                            callToGetPastMedicalHistoryForICE();
                            Constants.backFromAddEMR = false;
                            return false;
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

            if (bookAppointmentArrayList != null && bookAppointmentArrayList.size() > 0) {
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


    private void callToGetPastMedicalHistoryForICE() {
        if (localSetting.isNetworkAvailable(context)) {
            new GetPastMedicalHistoryForICE().execute();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
        }
    }

    private class GetPastMedicalHistoryForICE extends AsyncTask<Void, Void, String> {
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
                response = webServiceConsumer.GET(Constants.GET_PAST_MEDICAL_HISTORY_CE_LIST_URL + bookAppointmentArrayList.get(0).getUnitID()
                        + "&PatientID=" +
                        bookAppointmentArrayList.get(0).getPatientID()
                        + "&PatientUnitID=" +
                        bookAppointmentArrayList.get(0).getUnitID());
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
                elMedicalHistoryForICEArrayList = jsonObjectMapper.map(responseString, ELMedicalHistoryForICE.class);
                if (elMedicalHistoryForICEArrayList != null && elMedicalHistoryForICEArrayList.size() > 0) {
                    if (bookAppointmentArrayList != null && bookAppointmentArrayList.get(0).getVisitID() != null && bookAppointmentArrayList.get(0).getVisitID().length() > 0) {
                        String url = localSetting.returnPDFUrl("ICE", bookAppointmentArrayList.get(0).getUnitID(), bookAppointmentArrayList.get(0).getPatientID(),
                                bookAppointmentArrayList.get(0).getUnitID(), bookAppointmentArrayList.get(0).getVisitID(), bookAppointmentArrayList.get(0).getMRNo(),
                                bookAppointmentArrayList.get(0).getVisitTypeID(),elMedicalHistoryForICEArrayList.get(0).getL1(),elMedicalHistoryForICEArrayList.get(0).getL3());
                        context.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    }
                }
            } else if (responseCode == Constants.HTTP_DELETED_OK_204) {
                Toast.makeText(context, "ICE form is not generate for current patient", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, localSetting.handleError(responseCode), Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }
}
