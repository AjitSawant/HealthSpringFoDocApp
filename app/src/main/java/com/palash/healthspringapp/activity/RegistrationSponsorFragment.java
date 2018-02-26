package com.palash.healthspringapp.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;

import com.palash.healthspringapp.R;
import com.palash.healthspringapp.adapter.SpinnerAdapter;
import com.palash.healthspringapp.api.JsonObjectMapper;
import com.palash.healthspringapp.api.WebServiceConsumer;
import com.palash.healthspringapp.database.DatabaseAdapter;
import com.palash.healthspringapp.database.DatabaseContract;
import com.palash.healthspringapp.entity.ELCompanyName;
import com.palash.healthspringapp.entity.ELDoctorMaster;
import com.palash.healthspringapp.entity.ELPackageValidity;
import com.palash.healthspringapp.entity.ELPatientCategory;
import com.palash.healthspringapp.entity.Patient;
import com.palash.healthspringapp.utilities.Constants;
import com.palash.healthspringapp.utilities.LocalSetting;
import com.palash.healthspringapp.utilities.TransparentProgressDialog;
import com.palash.healthspringapp.utilities.Validate;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.Calendar;

import fr.ganfra.materialspinner.MaterialSpinner;

public class RegistrationSponsorFragment extends Fragment {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;

    private DatabaseAdapter.PatientCategoryL1Adapter patientCategoryL1AdapterDB;
    private DatabaseAdapter.GenderAdapter patientCompanyAdapterDB;
    private DatabaseAdapter.MaritalStatusAdapter elPatientCategoryL2AdapterDB;
    private DatabaseAdapter.BloodGroupAdapter elPatientCategoryL3AdapterDB;
    private DatabaseAdapter.PCPDoctorMasterAdapter patientPCPDoctorAdapterDB;
    private DatabaseAdapter.DoctorNameMasterAdapter patientDoctorNameAdapterDB;

    private ArrayList<ELPatientCategory> elPatientCategoryL1ArrayList = new ArrayList<>();
    private ArrayList<ELCompanyName> elPatientCompanyArrayList = new ArrayList<>();
    private ArrayList<ELPatientCategory> elPatientCategoryL2ArrayList = new ArrayList<>();
    private ArrayList<ELPatientCategory> elPatientCategoryL3ArrayList = new ArrayList<>();
    private ArrayList<ELDoctorMaster> elPCPDoctorArrayList = new ArrayList<>();
    private ArrayList<ELDoctorMaster> elDoctorNameArrayList = new ArrayList<>();
    private ArrayList<ELPackageValidity> elPackageValidityArrayList = new ArrayList<>();

    private static EditText patient_reg_edt_effective_date;
    private static EditText patient_reg_edt_expiry_date;
    private static EditText patient_reg_edt_card_issue_date;

    private MaterialSpinner patient_reg_category_L1;
    private MaterialSpinner patient_reg_category_company;
    private MaterialSpinner patient_reg_category_L2;
    private MaterialSpinner patient_reg_category_L3;
    private MaterialSpinner patient_reg_pcp_doctor;
    private MaterialSpinner patient_reg_spinner_doctor_name;

    private SpinnerAdapter.PatientCatogoryListAdapter patientCatogoryListAdapter;
    private SpinnerAdapter.CompanyListAdapter patientCompanyListAdapter;
    private SpinnerAdapter.PatientCatogoryListAdapter patientCatogoryL2ListAdapter;
    private SpinnerAdapter.PatientCatogoryListAdapter patientCatogoryL3ListAdapter;
    private SpinnerAdapter.PCPDoctorListAdapter patientPCPDoctorListAdapter;
    private SpinnerAdapter.DoctorNameListAdapter patientDoctorNameListAdapter;

    private static String CategoryL1ID = "0";
    private static String CompanyID = "0";
    private static String CategoryL2ID = "0";
    private static String CategoryL3ID = "0";
    private static String PCPDoctorID = "0";
    private static String DoctorNameID = "0";

    private DatePickerDialog.OnDateSetListener dateListener1;
    private DatePickerDialog.OnDateSetListener dateListener2;
    private DatePickerDialog.OnDateSetListener dateListener3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registration_sponsor, container, false);
        InitSetting();
        InitView(rootView);
        return rootView;
    }

    private void InitSetting() {
        try {
            context = getActivity();
            localSetting = new LocalSetting();
            databaseContract = new DatabaseContract(context);
            databaseAdapter = new DatabaseAdapter(databaseContract);
            patientCategoryL1AdapterDB = databaseAdapter.new PatientCategoryL1Adapter();
            patientCompanyAdapterDB = databaseAdapter.new GenderAdapter();
            elPatientCategoryL2AdapterDB = databaseAdapter.new MaritalStatusAdapter();
            elPatientCategoryL3AdapterDB = databaseAdapter.new BloodGroupAdapter();
            patientPCPDoctorAdapterDB = databaseAdapter.new PCPDoctorMasterAdapter();
            patientDoctorNameAdapterDB = databaseAdapter.new DoctorNameMasterAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView(View rootView) {
        try {
            patient_reg_edt_effective_date = (EditText) rootView.findViewById(R.id.patient_reg_edt_effective_date);
            patient_reg_edt_expiry_date = (EditText) rootView.findViewById(R.id.patient_reg_edt_expiry_date);
            patient_reg_edt_card_issue_date = (EditText) rootView.findViewById(R.id.patient_reg_edt_card_issue_date);

            patient_reg_category_L1 = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_category_L1);
            patient_reg_category_company = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_category_company);
            patient_reg_category_L2 = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_category_L2);
            patient_reg_category_L3 = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_category_L3);
            patient_reg_pcp_doctor = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_pcp_doctor);
            patient_reg_spinner_doctor_name = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_spinner_doctor_name);

            patient_reg_edt_effective_date.setFocusable(false);
            patient_reg_edt_expiry_date.setFocusable(false);
            patient_reg_edt_card_issue_date.setFocusable(false);

            patient_reg_edt_effective_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog dialog = new DatePickerDialog(context, dateListener1, Calendar.getInstance().get(Calendar.YEAR),
                            Calendar.getInstance().get(Calendar.MONTH),
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    //dialog.getDatePicker().setMinDate(new Date().getTime());
                    dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    dialog.show();
                }
            });

            dateListener1 = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;
                    String mMonth = "0";
                    if (month >= 0 && month < 9) {
                        mMonth = "0" + String.valueOf(month);
                        //month = Integer.parseInt(mMonth);
                    } else {
                        mMonth = String.valueOf(month);
                    }

                    String mDay = "0";
                    if (day >= 0 && day < 9) {
                        mDay = "0" + String.valueOf(day);
                    } else {
                        mDay = String.valueOf(day);
                    }

                    patient_reg_edt_effective_date.setText(year + "-" + mMonth + "-" + mDay);
                    setExpiryDate();
                }
            };

            patient_reg_edt_card_issue_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog dialog = new DatePickerDialog(context, dateListener3, Calendar.getInstance().get(Calendar.YEAR),
                            Calendar.getInstance().get(Calendar.MONTH),
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    //dialog.getDatePicker().setMinDate(new Date().getTime());
                    dialog.show();
                }
            });
            dateListener3 = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;
                    String mMonth = "0";
                    if (month >= 0 && month < 9) {
                        mMonth = "0" + String.valueOf(month);
                        //month = Integer.parseInt(mMonth);
                    } else {
                        mMonth = String.valueOf(month);
                    }

                    String mDay = "0";
                    if (day >= 0 && day < 9) {
                        mDay = "0" + String.valueOf(day);
                    } else {
                        mDay = String.valueOf(day);
                    }

                    patient_reg_edt_card_issue_date.setText(year + "-" + mMonth + "-" + mDay);
                }
            };
            InitAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitAdapter() {
        try {
            elPatientCategoryL1ArrayList = patientCategoryL1AdapterDB.listAll();
            elPCPDoctorArrayList = patientPCPDoctorAdapterDB.listAll();
            elDoctorNameArrayList = patientDoctorNameAdapterDB.listAll();

            //  --------------- Patient category L1 drop down  -------------------------
            if (elPatientCategoryL1ArrayList != null && elPatientCategoryL1ArrayList.size() > 0) {
                patientCatogoryListAdapter = new SpinnerAdapter.PatientCatogoryListAdapter(context, elPatientCategoryL1ArrayList);
                patient_reg_category_L1.setAdapter(patientCatogoryListAdapter);
                patientCatogoryListAdapter.notifyDataSetChanged();
                patient_reg_category_L1.setSelection(2);

                patient_reg_category_L1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int sCategoryL1Pos = patient_reg_category_L1.getSelectedItemPosition();
                        if (sCategoryL1Pos == 0) {
                            CategoryL1ID = "0";
                        } else if (sCategoryL1Pos > 0) {
                            sCategoryL1Pos = sCategoryL1Pos - 1;
                            CategoryL1ID = elPatientCategoryL1ArrayList.get(sCategoryL1Pos).getID();
                        }

                        // call company webservice
                        if (CategoryL1ID.equals("1")) {
                            patient_reg_category_company.setEnabled(true);
                            patient_reg_category_company.setClickable(true);
                            if (elPatientCompanyArrayList.size() == 0) {
                                new CompanyNameListTask().execute();
                            }
                        } else {
                            patientCompanyListAdapter = new SpinnerAdapter.CompanyListAdapter(context, elPatientCompanyArrayList);
                            patient_reg_category_company.setAdapter(patientCompanyListAdapter);
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

                        EnableDisableDate(false);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                patientCatogoryListAdapter = new SpinnerAdapter.PatientCatogoryListAdapter(context, elPatientCategoryL1ArrayList);
                patient_reg_category_L1.setAdapter(patientCatogoryListAdapter);
            }

            //  --------------- Patient PCP Doctor drop down  -------------------------
            if (elPCPDoctorArrayList != null && elPCPDoctorArrayList.size() > 0) {
                patientPCPDoctorListAdapter = new SpinnerAdapter.PCPDoctorListAdapter(context, elPCPDoctorArrayList);
                patient_reg_pcp_doctor.setAdapter(patientPCPDoctorListAdapter);
                patientPCPDoctorListAdapter.notifyDataSetChanged();

                patient_reg_pcp_doctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int sPCPDoctorPos = patient_reg_pcp_doctor.getSelectedItemPosition();
                        if (sPCPDoctorPos == 0) {
                            PCPDoctorID = "0";
                        } else if (sPCPDoctorPos > 0) {
                            sPCPDoctorPos = sPCPDoctorPos - 1;
                            PCPDoctorID = elPCPDoctorArrayList.get(sPCPDoctorPos).getDoctorID();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                patientPCPDoctorListAdapter = new SpinnerAdapter.PCPDoctorListAdapter(context, elPCPDoctorArrayList);
                patient_reg_pcp_doctor.setAdapter(patientPCPDoctorListAdapter);
                PCPDoctorID = "0";
            }

            //  --------------- Doctor Name drop down  -------------------------
            if (elDoctorNameArrayList != null && elDoctorNameArrayList.size() > 0) {
                patientDoctorNameListAdapter = new SpinnerAdapter.DoctorNameListAdapter(context, elDoctorNameArrayList);
                patient_reg_spinner_doctor_name.setAdapter(patientDoctorNameListAdapter);
                patientDoctorNameListAdapter.notifyDataSetChanged();

                patient_reg_spinner_doctor_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int sDoctorNamePos = patient_reg_spinner_doctor_name.getSelectedItemPosition();
                        if (sDoctorNamePos == 0) {
                            DoctorNameID = "0";
                        } else if (sDoctorNamePos > 0) {
                            sDoctorNamePos = sDoctorNamePos - 1;
                            DoctorNameID = elDoctorNameArrayList.get(sDoctorNamePos).getDoctorID();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                patientDoctorNameListAdapter = new SpinnerAdapter.DoctorNameListAdapter(context, elDoctorNameArrayList);
                patient_reg_spinner_doctor_name.setAdapter(patientDoctorNameListAdapter);
                DoctorNameID = "0";
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
        }
        /*else if (!Validate.hasTextIn(patient_reg_edt_effective_date.getText().toString())) {
            Validate.Msgshow(context, "Please enter effective date.");
            return false;
        } else if (!Validate.hasTextIn(patient_reg_edt_expiry_date.getText().toString())) {
            Validate.Msgshow(context, "Please enter expiry date.");
            return false;
        } else if (!Validate.hasTextIn(patient_reg_edt_card_issue_date.getText().toString())) {
            Validate.Msgshow(context, "Please enter card issue date.");
            return false;
        }*/
        else if (PCPDoctorID.equals("0")) {
            Validate.Msgshow(context, "Please select PCP doctor.");
            return false;
        } else if (DoctorNameID.equals("0")) {
            Validate.Msgshow(context, "Please select doctor name.");
            return false;
        }
        return true;
    }

    private void EnableDisableDate(Boolean flag) {
        if (flag == false) {
            patient_reg_edt_effective_date.setText(null);
            patient_reg_edt_expiry_date.setText(null);
        }
        patient_reg_edt_effective_date.setEnabled(flag);
    }

    public static Patient SponsorInformation() {
        Patient patient = new Patient();
        try {
            patient.setCategoryL1ID(CategoryL1ID);
            patient.setCompanyID(CompanyID);
            patient.setCategoryL2ID(CategoryL2ID);
            patient.setCategoryL3ID(CategoryL3ID);
            patient.setPCPDoctorID(PCPDoctorID);
            patient.setDoctorNameID(DoctorNameID);
            patient.setEffectiveDate(patient_reg_edt_effective_date.getText().toString());
            patient.setExpirydate(patient_reg_edt_expiry_date.getText().toString());
            patient.setCardIssueDate(patient_reg_edt_card_issue_date.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patient;
    }

    public class CompanyNameListTask extends AsyncTask<Void, Void, String> {
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
                WebServiceConsumer serviceConsumer = new WebServiceConsumer(context, null, null);
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
                    if (elPatientCompanyArrayList != null && elPatientCompanyArrayList.size() > 0) {
                        patientCompanyListAdapter = new SpinnerAdapter.CompanyListAdapter(context, elPatientCompanyArrayList);
                        patient_reg_category_company.setAdapter(patientCompanyListAdapter);
                        patientCompanyListAdapter.notifyDataSetChanged();
                        patient_reg_category_company.setSelection(1);

                        patient_reg_category_company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                int sCompanyPos = patient_reg_category_company.getSelectedItemPosition();
                                if (sCompanyPos == 0) {
                                    CompanyID = "0";
                                } else if (sCompanyPos > 0) {
                                    sCompanyPos = sCompanyPos - 1;
                                    CompanyID = elPatientCompanyArrayList.get(sCompanyPos).getID();
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                localSetting.alertbox(context, localSetting.handleError(responseCode), false);
            }
            super.onPostExecute(result);
        }
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
                WebServiceConsumer serviceConsumer = new WebServiceConsumer(context, null, null);
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

                        patient_reg_category_L2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                int sCategoryL2Pos = patient_reg_category_L2.getSelectedItemPosition();
                                if (sCategoryL2Pos == 0) {
                                    CategoryL2ID = "0";
                                } else if (sCategoryL2Pos > 0) {
                                    sCategoryL2Pos = sCategoryL2Pos - 1;
                                    CategoryL2ID = elPatientCategoryL2ArrayList.get(sCategoryL2Pos).getID();
                                }

                                if (!CategoryL2ID.equals("0")) {
                                    new PatientCategoryL3Task().execute();
                                } else {
                                    elPatientCategoryL3ArrayList = new ArrayList<ELPatientCategory>();
                                    patientCatogoryL3ListAdapter = new SpinnerAdapter.PatientCatogoryListAdapter(context, elPatientCategoryL3ArrayList);
                                    patient_reg_category_L3.setAdapter(patientCatogoryL3ListAdapter);
                                }

                                EnableDisableDate(false);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    } else {
                        CategoryL2ID = "0";
                        EnableDisableDate(false);
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
                WebServiceConsumer serviceConsumer = new WebServiceConsumer(context, null, null);
                Response response = serviceConsumer.GET(Constants.PATIENT_CATEGORY_L3_URL + CategoryL2ID);
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
                                }

                                EnableDisableDate(true);
                                if (!CategoryL3ID.equals("0")) {
                                    new GetPackageValidityTask().execute();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    } else {
                        CategoryL3ID = "0";
                        EnableDisableDate(false);
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

    public class GetPackageValidityTask extends AsyncTask<Void, Void, String> {
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
                WebServiceConsumer serviceConsumer = new WebServiceConsumer(context, null, null);
                Response response = serviceConsumer.GET(Constants.GET_PACKAGE_VALIDITY_URL + CategoryL3ID);
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
                    elPackageValidityArrayList = objectMapper.map(responseString, ELPackageValidity.class);
                    if (elPackageValidityArrayList != null && elPackageValidityArrayList.size() > 0) {
                        setDateDate(elPackageValidityArrayList.get(0));
                        EnableDisableDate(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (responseCode == Constants.HTTP_NO_RECORD_FOUND_OK_204) {
                EnableDisableDate(false);
            } else {
                localSetting.alertbox(context, localSetting.handleError(responseCode), false);
            }
            super.onPostExecute(result);
        }
    }

    private void setDateDate(ELPackageValidity elPackageValidity) {
        if (elPackageValidity.getPatientEffectiveDate() != null && elPackageValidity.getPatientEffectiveDate().length() > 0) {
            patient_reg_edt_effective_date.setText(elPackageValidity.getPatientEffectiveDate());
        } else {
            patient_reg_edt_effective_date.setText(null);
        }

        if (elPackageValidity.getPatientExpiryDate() != null && elPackageValidity.getPatientExpiryDate().length() > 0) {
            patient_reg_edt_expiry_date.setText(elPackageValidity.getPatientExpiryDate());
        } else {
            patient_reg_edt_effective_date.setText(null);
        }
    }

    private void setExpiryDate() {
        if (elPackageValidityArrayList != null && elPackageValidityArrayList.size() > 0) {
            String calculateExpiryDate = localSetting.calculateExpiryDate(patient_reg_edt_effective_date.getText().toString(), elPackageValidityArrayList.get(0).getValidity());
            //String calculateExpiryDate = localSetting.calculateExpiryDate(patient_reg_edt_effective_date.getText().toString(),"4");
            patient_reg_edt_expiry_date.setText(calculateExpiryDate);
        }
    }
}
