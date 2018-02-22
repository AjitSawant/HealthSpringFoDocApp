package com.palash.healthspringapp.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;

import com.palash.healthspringapp.R;
import com.palash.healthspringapp.adapter.SpinnerAdapter;
import com.palash.healthspringapp.database.DatabaseAdapter;
import com.palash.healthspringapp.database.DatabaseContract;
import com.palash.healthspringapp.entity.BloodGroup;
import com.palash.healthspringapp.entity.DoctorProfile;
import com.palash.healthspringapp.entity.ELCityMaster;
import com.palash.healthspringapp.entity.ELCountryMaster;
import com.palash.healthspringapp.entity.ELHealthspringReferral;
import com.palash.healthspringapp.entity.ELPatientCategory;
import com.palash.healthspringapp.entity.ELRegionMaster;
import com.palash.healthspringapp.entity.ELStateMaster;
import com.palash.healthspringapp.entity.Flag;
import com.palash.healthspringapp.entity.Gender;
import com.palash.healthspringapp.entity.MaritalStatus;
import com.palash.healthspringapp.entity.Patient;
import com.palash.healthspringapp.entity.Prefix;
import com.palash.healthspringapp.utilities.Constants;
import com.palash.healthspringapp.utilities.LocalSetting;
import com.palash.healthspringapp.utilities.Validate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fr.ganfra.materialspinner.MaterialSpinner;

public class RegistrationSponsorFragment extends Fragment {

    private Context context;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;

    private DatabaseAdapter.PatientCategoryL1Adapter patientCategoryL1AdapterDB;
    private DatabaseAdapter.GenderAdapter patientCompanyAdapterDB;
    private DatabaseAdapter.MaritalStatusAdapter elPatientCategoryL2AdapterDB;
    private DatabaseAdapter.BloodGroupAdapter elPatientCategoryL3AdapterDB;
    private DatabaseAdapter.CountryMasterAdapter patientPCPDoctorAdapterDB;
    private DatabaseAdapter.RegionMasterAdapter patientDoctorNameAdapterDB;

    private ArrayList<ELPatientCategory> elPatientCategoryL1ArrayList;
    private ArrayList<Gender> elPatientCompanyArrayList;
    private ArrayList<MaritalStatus> elPatientCategoryL2ArrayList;
    private ArrayList<BloodGroup> elPatientCategoryL3ArrayList;
    private ArrayList<ELCountryMaster> elPCPDoctorArrayList;
    private ArrayList<ELRegionMaster> elDoctorNameArrayList;

    private static EditText patient_reg_edt_effective_date;
    private static EditText patient_reg_edt_expiry_date;
    private static EditText patient_reg_edt_card_issue_date;

    private MaterialSpinner patient_reg_category_L1;
    private MaterialSpinner patient_reg_category_company;
    private MaterialSpinner patient_reg_category_L2;
    private MaterialSpinner patient_reg_category_L3;
    private MaterialSpinner patient_reg_pcp_doctor;
    private MaterialSpinner patient_reg_spinner_doctor_name;

    private SpinnerAdapter.PatientCatogoryL1Adapter patientCatogoryListAdapter;
    private SpinnerAdapter.GenderAdapter patientCompanyListAdapter;
    private SpinnerAdapter.MaritalStatusAdapter patientCatogoryL2ListAdapter;
    private SpinnerAdapter.BloodGroupAdapter patientCatogoryL3ListAdapter;
    private SpinnerAdapter.CountryAdapter patientPCPDoctorListAdapter;
    private SpinnerAdapter.RegionAdapter patientDoctorNameListAdapter;

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
            databaseContract = new DatabaseContract(context);
            databaseAdapter = new DatabaseAdapter(databaseContract);
            patientCategoryL1AdapterDB = databaseAdapter.new PatientCategoryL1Adapter();
            patientCompanyAdapterDB = databaseAdapter.new GenderAdapter();
            elPatientCategoryL2AdapterDB = databaseAdapter.new MaritalStatusAdapter();
            elPatientCategoryL3AdapterDB = databaseAdapter.new BloodGroupAdapter();
            patientPCPDoctorAdapterDB = databaseAdapter.new CountryMasterAdapter();
            patientDoctorNameAdapterDB = databaseAdapter.new RegionMasterAdapter();
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
                    patient_reg_edt_effective_date.setText(day + "/" + (month + 1) + "/" + year);
                }
            };

            patient_reg_edt_expiry_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog dialog = new DatePickerDialog(context, dateListener2, Calendar.getInstance().get(Calendar.YEAR),
                            Calendar.getInstance().get(Calendar.MONTH),
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    //dialog.getDatePicker().setMinDate(new Date().getTime());
                    dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    dialog.show();
                }
            });
            dateListener2 = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    patient_reg_edt_expiry_date.setText(day + "/" + (month + 1) + "/" + year);
                }
            };

            patient_reg_edt_card_issue_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog dialog = new DatePickerDialog(context, dateListener3, Calendar.getInstance().get(Calendar.YEAR),
                            Calendar.getInstance().get(Calendar.MONTH),
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    //dialog.getDatePicker().setMinDate(new Date().getTime());
                    dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    dialog.show();
                }
            });
            dateListener3 = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    patient_reg_edt_card_issue_date.setText(day + "/" + (month + 1) + "/" + year);
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
            elPatientCompanyArrayList = patientCompanyAdapterDB.listAll();
            elPatientCategoryL2ArrayList = elPatientCategoryL2AdapterDB.listAll();
            elPatientCategoryL3ArrayList = elPatientCategoryL3AdapterDB.listAll();
            elPCPDoctorArrayList = patientPCPDoctorAdapterDB.listAll();
            elDoctorNameArrayList = patientDoctorNameAdapterDB.listAll("");

            if (elPatientCategoryL1ArrayList != null && elPatientCategoryL1ArrayList.size() > 0) {
                patientCatogoryListAdapter = new SpinnerAdapter.PatientCatogoryL1Adapter(context, elPatientCategoryL1ArrayList);
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
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                patientCatogoryListAdapter = new SpinnerAdapter.PatientCatogoryL1Adapter(context, elPatientCategoryL1ArrayList);
                patient_reg_category_L1.setAdapter(patientCatogoryListAdapter);
            }

            if (elPatientCompanyArrayList != null && elPatientCompanyArrayList.size() > 0) {
                patientCompanyListAdapter = new SpinnerAdapter.GenderAdapter(context, elPatientCompanyArrayList);
                patient_reg_category_company.setAdapter(patientCompanyListAdapter);
                patientCompanyListAdapter.notifyDataSetChanged();

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
                patientCompanyListAdapter = new SpinnerAdapter.GenderAdapter(context, elPatientCompanyArrayList);
                patient_reg_category_company.setAdapter(patientCompanyListAdapter);
            }

            if (elPatientCategoryL2ArrayList != null && elPatientCategoryL2ArrayList.size() > 0) {
                patientCatogoryL2ListAdapter = new SpinnerAdapter.MaritalStatusAdapter(context, elPatientCategoryL2ArrayList);
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
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                patientCatogoryL2ListAdapter = new SpinnerAdapter.MaritalStatusAdapter(context, elPatientCategoryL2ArrayList);
                patient_reg_category_L2.setAdapter(patientCatogoryL2ListAdapter);
            }

            if (elPatientCategoryL3ArrayList != null && elPatientCategoryL3ArrayList.size() > 0) {
                patientCatogoryL3ListAdapter = new SpinnerAdapter.BloodGroupAdapter(context, elPatientCategoryL3ArrayList);
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
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                patientCatogoryL3ListAdapter = new SpinnerAdapter.BloodGroupAdapter(context, elPatientCategoryL3ArrayList);
                patient_reg_category_L3.setAdapter(patientCatogoryL3ListAdapter);
            }

            if (elPCPDoctorArrayList != null && elPCPDoctorArrayList.size() > 0) {
                patientPCPDoctorListAdapter = new SpinnerAdapter.CountryAdapter(context, elPCPDoctorArrayList);
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
                            PCPDoctorID = elPCPDoctorArrayList.get(sPCPDoctorPos).getID();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                patientPCPDoctorListAdapter = new SpinnerAdapter.CountryAdapter(context, elPCPDoctorArrayList);
                patient_reg_pcp_doctor.setAdapter(patientPCPDoctorListAdapter);
                DoctorNameID = "0";
            }

            if (elDoctorNameArrayList != null && elDoctorNameArrayList.size() > 0) {
                patientDoctorNameListAdapter = new SpinnerAdapter.RegionAdapter(context, elDoctorNameArrayList);
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
                            DoctorNameID = elDoctorNameArrayList.get(sDoctorNamePos).getID();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                patientDoctorNameListAdapter = new SpinnerAdapter.RegionAdapter(context, elDoctorNameArrayList);
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
        } else if (CompanyID.equals("0")) {
            Validate.Msgshow(context, "Please select company.");
            return false;
        } else if (CategoryL2ID.equals("0")) {
            Validate.Msgshow(context, "Please select Category L2.");
            return false;
        } else if (CategoryL3ID.equals("0")) {
            Validate.Msgshow(context, "Please select Category L3.");
            return false;
        } else if (!Validate.hasTextIn(patient_reg_edt_effective_date.getText().toString())) {
            Validate.Msgshow(context, "Please enter effective date.");
            return false;
        } else if (!Validate.hasTextIn(patient_reg_edt_expiry_date.getText().toString())) {
            Validate.Msgshow(context, "Please enter expiry date.");
            return false;
        } else if (!Validate.hasTextIn(patient_reg_edt_card_issue_date.getText().toString())) {
            Validate.Msgshow(context, "Please enter card issue date.");
            return false;
        } else if (PCPDoctorID.equals("0")) {
            Validate.Msgshow(context, "Please select PCP doctor.");
            return false;
        } else if (DoctorNameID.equals("0")) {
            Validate.Msgshow(context, "Please select doctor name.");
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
            patient.setEffectiveDate(patient_reg_edt_effective_date.getText().toString());
            patient.setExpirydate(patient_reg_edt_expiry_date.getText().toString());
            patient.setCardIssueDate(patient_reg_edt_card_issue_date.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patient;
    }
}
