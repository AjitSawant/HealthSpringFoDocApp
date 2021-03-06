package com.palash.healthspringfoapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.palash.healthspringfoapp.R;
import com.palash.healthspringfoapp.adapter.SpinnerAdapter;
import com.palash.healthspringfoapp.api.JsonObjectMapper;
import com.palash.healthspringfoapp.api.WebServiceConsumer;
import com.palash.healthspringfoapp.database.DatabaseAdapter;
import com.palash.healthspringfoapp.database.DatabaseContract;
import com.palash.healthspringfoapp.entity.ELCompanyName;
import com.palash.healthspringfoapp.entity.ELHealthspringReferral;
import com.palash.healthspringfoapp.entity.ELPatientCategory;
import com.palash.healthspringfoapp.utilities.Constants;
import com.palash.healthspringfoapp.utilities.LocalSetting;
import com.palash.healthspringfoapp.utilities.TransparentProgressDialog;
import com.palash.healthspringfoapp.utilities.Validate;
import com.squareup.okhttp.Response;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

public class AppSettingActivity extends AppCompatActivity {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;

    private DatabaseAdapter.PatientCategoryL1Adapter patientCategoryL1AdapterDB;
    private DatabaseAdapter.CompanyMasterAdapter companyMasterAdapterDB;
    private DatabaseAdapter.HealthSpringReferralMasterAdapter healthSpringReferralMasterAdapter;

    private ArrayList<ELPatientCategory> elPatientCategoryL1ArrayList = new ArrayList<>();
    private ArrayList<ELCompanyName> elPatientCompanyArrayList = new ArrayList<>();
    private ArrayList<ELPatientCategory> elPatientCategoryL2ArrayList = new ArrayList<>();
    private ArrayList<ELPatientCategory> elPatientCategoryL3ArrayList = new ArrayList<>();
    private ArrayList<ELHealthspringReferral> elHealthspringReferralArrayList;

    private MaterialSpinner patient_reg_category_L1;
    private MaterialSpinner patient_reg_category_company;
    private MaterialSpinner patient_reg_category_L2;
    private MaterialSpinner patient_reg_category_L3;
    private MaterialSpinner patient_reg_spinner_known_from;

    private SpinnerAdapter.PatientCatogoryListAdapter patientCatogoryListAdapter;
    private SpinnerAdapter.CompanyListAdapter patientCompanyListAdapter;
    private SpinnerAdapter.PatientCatogoryListAdapter patientCatogoryL2ListAdapter;
    private SpinnerAdapter.PatientCatogoryListAdapter patientCatogoryL3ListAdapter;
    private SpinnerAdapter.HealthspringReferralAdapter healthspringReferralAdapter;

    private static String CategoryL1ID = "0";
    private static String CategoryL1Name = "";
    private static String CompanyID = "0";
    private static String CompanyName = "";
    private static String CategoryL2ID = "0";
    private static String CategoryL2Name = "";
    private static String CategoryL3ID = "0";
    private static String CategoryL3Name = "";
    private static String HealthSpringReferralID = "0";
    private static String HealthSpringReferralName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_category_setting);
        InitSetting();
        InitView();
        InitAdapter();
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
            patientCategoryL1AdapterDB = databaseAdapter.new PatientCategoryL1Adapter();
            companyMasterAdapterDB = databaseAdapter.new CompanyMasterAdapter();
            healthSpringReferralMasterAdapter = databaseAdapter.new HealthSpringReferralMasterAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView() {
        try {
            patient_reg_category_L1 = (MaterialSpinner) findViewById(R.id.patient_reg_category_L1);
            patient_reg_category_company = (MaterialSpinner) findViewById(R.id.patient_reg_category_company);
            patient_reg_category_L2 = (MaterialSpinner) findViewById(R.id.patient_reg_category_L2);
            patient_reg_category_L3 = (MaterialSpinner) findViewById(R.id.patient_reg_category_L3);
            patient_reg_spinner_known_from = (MaterialSpinner) findViewById(R.id.patient_reg_spinner_known_from);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.menu_toolbar_save).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toolbar_save:
                addLocalData();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addLocalData() {
        if (validateControls(context)) {
            SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.KEY_SHARED_Pref, 0); // 0 - for private mode
            final SharedPreferences.Editor editor = pref.edit();

            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("Do you really want to save data locally!")
                    .setConfirmText("Yes")
                    .setCancelText("No")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            localSetting.showSuccessAlert(context, "Saved!", "Data saved successfully!");

                            editor.putString("CategoryL1ID", CategoryL1ID);
                            if (CategoryL1ID.equals("0")) {
                                editor.putString("CategoryL1Name", "");
                            } else {
                                editor.putString("CategoryL1Name", CategoryL1Name);
                            }

                            editor.putString("CompanyID", CompanyID);
                            if (CompanyID.equals("0")) {
                                editor.putString("CompanyName", "Self");
                            } else {
                                editor.putString("CompanyName", CompanyName);
                            }

                            editor.putString("CategoryL2ID", CategoryL2ID);
                            if (CategoryL2ID.equals("0")) {
                                editor.putString("CategoryL2Name", "");
                            } else {
                                editor.putString("CategoryL2Name", CategoryL2Name);
                            }

                            editor.putString("CategoryL3ID", CategoryL3ID);
                            if (CategoryL3ID.equals("0")) {
                                editor.putString("CategoryL3Name", "");
                            } else {
                                editor.putString("CategoryL3Name", CategoryL3Name);
                            }

                            editor.putString("HealthSpringReferralID", HealthSpringReferralID);
                            if (HealthSpringReferralID.equals("0")) {
                                editor.putString("HealthSpringReferralName", "");
                            } else {
                                editor.putString("HealthSpringReferralName", HealthSpringReferralName);
                            }
                            editor.commit();
                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
    }

    private void InitAdapter() {
        try {
            elPatientCategoryL1ArrayList = patientCategoryL1AdapterDB.listAll();
            elPatientCompanyArrayList = companyMasterAdapterDB.listAll();
            elHealthspringReferralArrayList = healthSpringReferralMasterAdapter.listAll();

            //  --------------- Patient category L1 drop down  -------------------------
            if (elPatientCategoryL1ArrayList != null && elPatientCategoryL1ArrayList.size() > 0) {
                patientCatogoryListAdapter = new SpinnerAdapter.PatientCatogoryListAdapter(context, elPatientCategoryL1ArrayList);
                patient_reg_category_L1.setAdapter(patientCatogoryListAdapter);
                patientCatogoryListAdapter.notifyDataSetChanged();
                patient_reg_category_L1.setSelection(2);

                if (elPatientCompanyArrayList.size() > 0) {
                    //patient_reg_category_company.setSelection(1);
                }

                patient_reg_category_L1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int sCategoryL1Pos = patient_reg_category_L1.getSelectedItemPosition();
                        if (sCategoryL1Pos == 0) {
                            CategoryL1ID = "0";
                        } else if (sCategoryL1Pos > 0) {
                            sCategoryL1Pos = sCategoryL1Pos - 1;
                            CategoryL1ID = elPatientCategoryL1ArrayList.get(sCategoryL1Pos).getID();
                            CategoryL1Name = elPatientCategoryL1ArrayList.get(sCategoryL1Pos).getDescription();
                        }

                        // call company webservice
                        if (CategoryL1ID.equals("1")) {
                            CompanyID = "0";
                            patient_reg_category_company.setEnabled(true);
                            patient_reg_category_company.setClickable(true);
                            if (elPatientCompanyArrayList.size() > 0) {
                                patient_reg_category_company.setSelection(1);
                            }
                        } else {
                            CompanyID = "0";
                            if (elPatientCompanyArrayList.size() > 0) {
                                //patient_reg_category_company.setSelection(1);
                            }
                            patient_reg_category_company.setEnabled(false);
                            patient_reg_category_company.setClickable(false);
                        }

                        if (!CategoryL1ID.equals("0")) {
                            new PatientCategoryL2Task().execute();
                        } else {
                            elPatientCategoryL2ArrayList = new ArrayList<ELPatientCategory>();
                            patientCatogoryL2ListAdapter = new SpinnerAdapter.PatientCatogoryListAdapter(context, elPatientCategoryL2ArrayList);
                            patient_reg_category_L2.setAdapter(patientCatogoryL2ListAdapter);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                patientCatogoryListAdapter = new SpinnerAdapter.PatientCatogoryListAdapter(context, elPatientCategoryL1ArrayList);
                patient_reg_category_L1.setAdapter(patientCatogoryListAdapter);
            }

            if (elPatientCompanyArrayList != null && elPatientCompanyArrayList.size() > 0) {
                patientCompanyListAdapter = new SpinnerAdapter.CompanyListAdapter(context, elPatientCompanyArrayList);
                patient_reg_category_company.setAdapter(patientCompanyListAdapter);
                patientCompanyListAdapter.notifyDataSetChanged();
                //patient_reg_category_company.setSelection(1);

                patient_reg_category_company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int sCompanyPos = patient_reg_category_company.getSelectedItemPosition();
                        if (sCompanyPos == 0) {
                            CompanyID = "0";
                        } else if (sCompanyPos > 0) {
                            sCompanyPos = sCompanyPos - 1;
                            CompanyID = elPatientCompanyArrayList.get(sCompanyPos).getID();
                            CompanyName = elPatientCompanyArrayList.get(sCompanyPos).getDescription();
                        }

                        if (!CompanyID.equals("0")) {
                            new PatientCategoryL3Task().execute();
                        } else {
                            elPatientCategoryL3ArrayList = new ArrayList<ELPatientCategory>();
                            patientCatogoryL3ListAdapter = new SpinnerAdapter.PatientCatogoryListAdapter(context, elPatientCategoryL3ArrayList);
                            patient_reg_category_L3.setAdapter(patientCatogoryL3ListAdapter);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                CompanyID = "0";
                patientCompanyListAdapter = new SpinnerAdapter.CompanyListAdapter(context, elPatientCompanyArrayList);
                patient_reg_category_company.setAdapter(patientCompanyListAdapter);
            }

            if (elHealthspringReferralArrayList != null && elHealthspringReferralArrayList.size() > 0) {
                healthspringReferralAdapter = new SpinnerAdapter.HealthspringReferralAdapter(context, elHealthspringReferralArrayList);
                patient_reg_spinner_known_from.setAdapter(healthspringReferralAdapter);
                healthspringReferralAdapter.notifyDataSetChanged();

                patient_reg_spinner_known_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int sRefferalPos = patient_reg_spinner_known_from.getSelectedItemPosition();
                        if (sRefferalPos == 0) {
                            HealthSpringReferralID = "0";
                        } else if (sRefferalPos > 0) {
                            sRefferalPos = sRefferalPos - 1;
                            HealthSpringReferralID = elHealthspringReferralArrayList.get(sRefferalPos).getID();
                            HealthSpringReferralName = elHealthspringReferralArrayList.get(sRefferalPos).getDescription();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                healthspringReferralAdapter = new SpinnerAdapter.HealthspringReferralAdapter(context, elHealthspringReferralArrayList);
                patient_reg_spinner_known_from.setAdapter(healthspringReferralAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean validateControls(Context context) {
        if (CategoryL1ID.equals("0")) {
            Validate.Msgshow(context, "Please Select Category L1.");
            return false;
        } else if (CategoryL1ID.equals("1") && CompanyID.equals("0")) {
            Validate.Msgshow(context, "Please select company.");
            return false;
        } else if (CategoryL2ID.equals("0")) {
            Validate.Msgshow(context, "Please select Category L2.");
            return false;
        } else if (CategoryL3ID.equals("0")) {
            Validate.Msgshow(context, "Please select Category L3.");
            return false;
        } else if (HealthSpringReferralID.equals("0")) {
            Validate.Msgshow(context, "Please select known Healthspring from.");
            return false;
        }
        return true;
    }


    public class PatientCategoryL2Task extends AsyncTask<Void, Void, String> {
        private JsonObjectMapper objectMapper;
        private String responseString = "";
        private int responseCode = 0;
        private TransparentProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                WebServiceConsumer serviceConsumer = new WebServiceConsumer(context, null, null, null);
                Response response = serviceConsumer.GET(Constants.PATIENT_CATEGORY_L2_URL + CategoryL1ID);
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
            localSetting.hideDialog(progressDialog);
            if (responseCode == Constants.HTTP_OK_200) {
                try {
                    objectMapper = new JsonObjectMapper();
                    elPatientCategoryL2ArrayList = objectMapper.map(responseString, ELPatientCategory.class);

                    //  --------------- Patient category L2 drop down  -------------------------
                    if (elPatientCategoryL2ArrayList != null && elPatientCategoryL2ArrayList.size() > 0) {
                        patientCatogoryL2ListAdapter = new SpinnerAdapter.PatientCatogoryListAdapter(context, elPatientCategoryL2ArrayList);
                        patient_reg_category_L2.setAdapter(patientCatogoryL2ListAdapter);
                        patientCatogoryL2ListAdapter.notifyDataSetChanged();

                        /*if (CategoryL1ID.equals("2")) {
                            patient_reg_category_L2.setSelection(1);
                        }*/

                        patient_reg_category_L2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                int sCategoryL2Pos = patient_reg_category_L2.getSelectedItemPosition();
                                if (sCategoryL2Pos == 0) {
                                    CategoryL2ID = "0";
                                } else if (sCategoryL2Pos > 0) {
                                    sCategoryL2Pos = sCategoryL2Pos - 1;
                                    CategoryL2ID = elPatientCategoryL2ArrayList.get(sCategoryL2Pos).getID();
                                    CategoryL2Name = elPatientCategoryL2ArrayList.get(sCategoryL2Pos).getDescription();
                                }

                                if (!CategoryL2ID.equals("0") && !CategoryL1ID.equals("1")) {
                                    new PatientCategoryL3Task().execute();
                                } else if (!CategoryL1ID.equals("1")) {
                                    elPatientCategoryL3ArrayList = new ArrayList<ELPatientCategory>();
                                    patientCatogoryL3ListAdapter = new SpinnerAdapter.PatientCatogoryListAdapter(context, elPatientCategoryL3ArrayList);
                                    patient_reg_category_L3.setAdapter(patientCatogoryL3ListAdapter);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    } else {
                        CategoryL2ID = "0";
                        patientCatogoryL2ListAdapter = new SpinnerAdapter.PatientCatogoryListAdapter(context, elPatientCategoryL2ArrayList);
                        patient_reg_category_L2.setAdapter(patientCatogoryL2ListAdapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (responseCode == Constants.HTTP_NO_RECORD_FOUND_OK_204) {
                // clear Category L2 drop down
                CategoryL2ID = "0";
                elPatientCategoryL2ArrayList = new ArrayList<>();
                patientCatogoryL2ListAdapter = new SpinnerAdapter.PatientCatogoryListAdapter(context, elPatientCategoryL2ArrayList);
                patient_reg_category_L2.setAdapter(patientCatogoryL2ListAdapter);

                // clear Category L3 drop down
                CategoryL3ID = "0";
                elPatientCategoryL3ArrayList = new ArrayList<ELPatientCategory>();
                patientCatogoryL3ListAdapter = new SpinnerAdapter.PatientCatogoryListAdapter(context, elPatientCategoryL3ArrayList);
                patient_reg_category_L3.setAdapter(patientCatogoryL3ListAdapter);
            } else {
                localSetting.alertbox(context, localSetting.handleError(responseCode), false);
            }
            super.onPostExecute(result);
        }
    }

    public class PatientCategoryL3Task extends AsyncTask<Void, Void, String> {
        private JsonObjectMapper objectMapper;
        private String responseString = "";
        private int responseCode = 0;
        private TransparentProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                WebServiceConsumer serviceConsumer = new WebServiceConsumer(context, null, null, null);
                Response response = serviceConsumer.GET(Constants.PATIENT_CATEGORY_L3_URL + CategoryL2ID + "&CompanyID=" + CompanyID);
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
            localSetting.hideDialog(progressDialog);
            if (responseCode == Constants.HTTP_OK_200) {
                try {
                    objectMapper = new JsonObjectMapper();
                    elPatientCategoryL3ArrayList = objectMapper.map(responseString, ELPatientCategory.class);

                    //  --------------- Patient category L3 drop down  -------------------------
                    if (elPatientCategoryL3ArrayList != null && elPatientCategoryL3ArrayList.size() > 0) {
                        patientCatogoryL3ListAdapter = new SpinnerAdapter.PatientCatogoryListAdapter(context, elPatientCategoryL3ArrayList);
                        patient_reg_category_L3.setAdapter(patientCatogoryL3ListAdapter);
                        patientCatogoryL3ListAdapter.notifyDataSetChanged();

                        patient_reg_category_L3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                int sCategoryL3Pos = patient_reg_category_L3.getSelectedItemPosition();
                                if (sCategoryL3Pos == 0) {
                                    CategoryL3ID = "0";
                                } else if (sCategoryL3Pos > 0) {
                                    sCategoryL3Pos = sCategoryL3Pos - 1;
                                    CategoryL3ID = elPatientCategoryL3ArrayList.get(sCategoryL3Pos).getID();
                                    CategoryL3Name = elPatientCategoryL3ArrayList.get(sCategoryL3Pos).getDescription();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    } else {
                        CategoryL3ID = "0";
                        patientCatogoryL3ListAdapter = new SpinnerAdapter.PatientCatogoryListAdapter(context, elPatientCategoryL3ArrayList);
                        patient_reg_category_L3.setAdapter(patientCatogoryL3ListAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (responseCode == Constants.HTTP_NO_RECORD_FOUND_OK_204) {
                // clear Category L3 drop down
                CategoryL3ID = "0";
                elPatientCategoryL3ArrayList = new ArrayList<ELPatientCategory>();
                patientCatogoryL3ListAdapter = new SpinnerAdapter.PatientCatogoryListAdapter(context, elPatientCategoryL3ArrayList);
                patient_reg_category_L3.setAdapter(patientCatogoryL3ListAdapter);
            } else {
                localSetting.alertbox(context, localSetting.handleError(responseCode), false);
            }
            super.onPostExecute(result);
        }
    }

    /*public class CompanyNameListTask extends AsyncTask<Void, Void, String> {
        private JsonObjectMapper objectMapper;
        private String responseString = "";
        private int responseCode = 0;
        private TransparentProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                WebServiceConsumer serviceConsumer = new WebServiceConsumer(context, null, null, null);
                Response response = serviceConsumer.GET(Constants.COMPANY_NAME_URL);
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
            localSetting.hideDialog(progressDialog);
            if (responseCode == Constants.HTTP_OK_200) {
                try {
                    objectMapper = new JsonObjectMapper();
                    elPatientCompanyArrayList = objectMapper.map(responseString, ELCompanyName.class);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                localSetting.alertbox(context, localSetting.handleError(responseCode), false);
            }
            super.onPostExecute(result);
        }
    }*/

}
