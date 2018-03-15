package com.palash.healthspringfoapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.palash.healthspringfoapp.R;
import com.palash.healthspringfoapp.database.DatabaseAdapter;
import com.palash.healthspringfoapp.database.DatabaseContract;
import com.palash.healthspringfoapp.entity.Patient;
import com.palash.healthspringfoapp.utilities.Constants;
import com.palash.healthspringfoapp.utilities.LocalSetting;
import com.palash.healthspringfoapp.utilities.Validate;

public class RegistrationSponsorFragment extends Fragment {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;

    private static EditText patient_reg_category_L1;
    private static EditText patient_reg_category_company;
    private static EditText patient_reg_category_L2;
    private static EditText patient_reg_category_L3;
    private static EditText patient_reg_known_healthspring;

    private static String CategoryL1ID = "0";
    private static String CompanyID = "0";
    private static String CategoryL2ID = "0";
    private static String CategoryL3ID = "0";
    private static String HealthSpringReferralID = "0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registration_sponsor, container, false);
        InitSetting();
        InitView(rootView);
        setData();
        return rootView;
    }

    private void InitSetting() {
        try {
            context = getActivity();
            localSetting = new LocalSetting();
            databaseContract = new DatabaseContract(context);
            databaseAdapter = new DatabaseAdapter(databaseContract);
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
            patient_reg_known_healthspring = (EditText) rootView.findViewById(R.id.patient_reg_known_healthspring);

            patient_reg_category_L1.setFocusable(false);
            patient_reg_category_company.setFocusable(false);
            patient_reg_category_L2.setFocusable(false);
            patient_reg_category_L3.setFocusable(false);
            patient_reg_known_healthspring.setFocusable(false);

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

        HealthSpringReferralID = sharedpreferences.getString("HealthSpringReferralID", "");
        if (!HealthSpringReferralID.equals("0")) {
            patient_reg_known_healthspring.setText(sharedpreferences.getString("HealthSpringReferralName", ""));
        } else {
            patient_reg_known_healthspring.setText(null);
        }
    }

    public static boolean validateControls(Context context) {
        if (CategoryL1ID == null || CategoryL1ID.equals("") || CategoryL1ID.equals("0")) {
            Validate.Msgshow(context, "Please Select Category L1 from app setting.");
            return false;
        } else if (CategoryL1ID.equals("1") && (CompanyID == null || CompanyID.equals("") || CompanyID.equals("0"))) {
            Validate.Msgshow(context, "Please select company from app setting.");
            return false;
        } else if (CategoryL2ID == null || CategoryL2ID.equals("") || CategoryL2ID.equals("0")) {
            Validate.Msgshow(context, "Please select Category L2 from app setting.");
            return false;
        } else if (CategoryL3ID == null || CategoryL3ID.equals("") || CategoryL3ID.equals("0")) {
            Validate.Msgshow(context, "Please select Category L3 from app setting.");
            return false;
        } else if (HealthSpringReferralID == null || HealthSpringReferralID.equals("") || HealthSpringReferralID.equals("0")) {
            Validate.Msgshow(context, "Please select Healthspring known from app setting.");
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
            patient.setHealthSpringReferralID(HealthSpringReferralID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patient;
    }
}
