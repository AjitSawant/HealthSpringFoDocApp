package com.palash.healthspringapp.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;

import com.palash.healthspringapp.R;
import com.palash.healthspringapp.adapter.SpinnerAdapter;
import com.palash.healthspringapp.api.JsonObjectMapper;
import com.palash.healthspringapp.api.WebServiceConsumer;
import com.palash.healthspringapp.database.DatabaseAdapter;
import com.palash.healthspringapp.database.DatabaseContract;
import com.palash.healthspringapp.entity.DoctorProfile;
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
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;

    private DatabaseAdapter.PatientCategoryL1Adapter patientCategoryL1AdapterDB;
    private DatabaseAdapter.PCPDoctorMasterAdapter patientPCPDoctorAdapterDB;
    private DatabaseAdapter.DoctorNameMasterAdapter patientDoctorNameAdapterDB;

    private ArrayList<DoctorProfile> listProfile;
    private ArrayList<ELDoctorMaster> elPCPDoctorArrayList = new ArrayList<>();

    private static EditText patient_reg_category_L1;
    private static EditText patient_reg_category_company;
    private static EditText patient_reg_category_L2;
    private static EditText patient_reg_category_L3;

    private MaterialSpinner patient_reg_pcp_doctor;

    private SpinnerAdapter.PCPDoctorListAdapter patientPCPDoctorListAdapter;

    private static String CategoryL1ID = "0";
    private static String CompanyID = "0";
    private static String CategoryL2ID = "0";
    private static String CategoryL3ID = "0";
    private static String PCPDoctorID = "0";
    private static String DoctorNameID = "0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registration_sponsor, container, false);
        InitSetting();
        InitView(rootView);
        InitAdapter();
        setData();
        return rootView;
    }

    private void InitSetting() {
        try {
            context = getActivity();
            localSetting = new LocalSetting();
            databaseContract = new DatabaseContract(context);
            databaseAdapter = new DatabaseAdapter(databaseContract);
            doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();
            patientCategoryL1AdapterDB = databaseAdapter.new PatientCategoryL1Adapter();
            patientPCPDoctorAdapterDB = databaseAdapter.new PCPDoctorMasterAdapter();
            patientDoctorNameAdapterDB = databaseAdapter.new DoctorNameMasterAdapter();

            listProfile = doctorProfileAdapter.listAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView(View rootView) {
        try {
            patient_reg_category_L1 = (EditText) rootView.findViewById(R.id.patient_reg_category_L1);
            patient_reg_category_company = (EditText) rootView.findViewById(R.id.patient_reg_category_company);
            patient_reg_category_L2 = (EditText) rootView.findViewById(R.id.patient_reg_category_L2);
            patient_reg_category_L3 = (EditText) rootView.findViewById(R.id.patient_reg_category_L3);

            patient_reg_pcp_doctor = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_pcp_doctor);

            patient_reg_category_L1.setFocusable(false);
            patient_reg_category_company.setFocusable(false);
            patient_reg_category_L2.setFocusable(false);
            patient_reg_category_L3.setFocusable(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData() {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constants.KEY_SHARED_Pref, Context.MODE_PRIVATE);
        CategoryL1ID = sharedpreferences.getString("CategoryL1ID", "");
        if (!CategoryL1ID.equals("0")) {
            patient_reg_category_L1.setText(sharedpreferences.getString("CategoryL1Name", ""));
        } else {
            patient_reg_category_L1.setText(null);
        }

        CompanyID = sharedpreferences.getString("CompanyID", "");
        if (!CompanyID.equals("0")) {
            patient_reg_category_company.setText(sharedpreferences.getString("CompanyName", ""));
        } else {
            patient_reg_category_company.setText(null);
        }

        CategoryL2ID = sharedpreferences.getString("CategoryL2ID", "");
        if (!CategoryL2ID.equals("0")) {
            patient_reg_category_L2.setText(sharedpreferences.getString("CategoryL2Name", ""));
        } else {
            patient_reg_category_L2.setText(null);
        }

        CategoryL3ID = sharedpreferences.getString("CategoryL3ID", "");
        if (!CategoryL3ID.equals("0")) {
            patient_reg_category_L3.setText(sharedpreferences.getString("CategoryL3Name", ""));
        } else {
            patient_reg_category_L3.setText(null);
        }
    }


    private void InitAdapter() {
        try {
            elPCPDoctorArrayList = patientPCPDoctorAdapterDB.listAll(listProfile.get(0).getUnitID());

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
        } else if (PCPDoctorID.equals("0")) {
            Validate.Msgshow(context, "Please select PCP doctor.");
            return false;
        }
        return true;
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
            patient.setEffectiveDate(patient_reg_category_L1.getText().toString());
            patient.setExpirydate(patient_reg_category_company.getText().toString());
            patient.setCardIssueDate(patient_reg_category_L2.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patient;
    }
}
