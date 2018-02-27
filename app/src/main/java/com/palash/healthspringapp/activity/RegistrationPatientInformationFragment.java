package com.palash.healthspringapp.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.palash.healthspringapp.R;
import com.palash.healthspringapp.adapter.SpinnerAdapter;
import com.palash.healthspringapp.api.JsonObjectMapper;
import com.palash.healthspringapp.api.WebServiceConsumer;
import com.palash.healthspringapp.database.DatabaseAdapter;
import com.palash.healthspringapp.database.DatabaseContract;
import com.palash.healthspringapp.entity.BloodGroup;
import com.palash.healthspringapp.entity.DoctorProfile;
import com.palash.healthspringapp.entity.ELCityMaster;
import com.palash.healthspringapp.entity.ELCountryMaster;
import com.palash.healthspringapp.entity.ELHealthspringReferral;
import com.palash.healthspringapp.entity.ELRegionMaster;
import com.palash.healthspringapp.entity.ELStateMaster;
import com.palash.healthspringapp.entity.Flag;
import com.palash.healthspringapp.entity.Gender;
import com.palash.healthspringapp.entity.MaritalStatus;
import com.palash.healthspringapp.entity.Patient;
import com.palash.healthspringapp.entity.Prefix;
import com.palash.healthspringapp.fragment.ComplaintsFragment;
import com.palash.healthspringapp.utilities.Constants;
import com.palash.healthspringapp.utilities.LocalSetting;
import com.palash.healthspringapp.utilities.TransparentProgressDialog;
import com.palash.healthspringapp.utilities.Validate;
import com.squareup.okhttp.Response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fr.ganfra.materialspinner.MaterialSpinner;

public class RegistrationPatientInformationFragment extends Fragment {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.PrefixAdapter prefixAdapter;
    private DatabaseAdapter.GenderAdapter genderAdapter;
    private DatabaseAdapter.MaritalStatusAdapter maritalStatusAdapter;
    private DatabaseAdapter.BloodGroupAdapter bloodGroupAdapter;
    private DatabaseAdapter.CountryMasterAdapter countryMasterAdapter;
    private DatabaseAdapter.RegionMasterAdapter regionMasterAdapter;
    private DatabaseAdapter.StateMasterAdapter stateMasterAdapter;
    private DatabaseAdapter.CityMasterAdapter cityMasterAdapter;
    private DatabaseAdapter.HealthSpringReferralMasterAdapter healthSpringReferralMasterAdapter;

    private Patient patient;
    private Flag flag;
    private ArrayList<DoctorProfile> listProfile;
    private ArrayList<Prefix> listPrefix;
    private ArrayList<Gender> listGender;
    private ArrayList<MaritalStatus> listMaritalStatus;
    private ArrayList<BloodGroup> listbloodGroups;
    private ArrayList<ELCountryMaster> elCountryMasterArrayList;
    private ArrayList<ELRegionMaster> elRegionMasterArrayList;
    private ArrayList<ELStateMaster> elStateMasterArrayList;
    private ArrayList<ELCityMaster> elCityMasterArrayList;
    private ArrayList<ELHealthspringReferral> elHealthspringReferralArrayList;

    private static EditText patient_reg_edt_fname;
    private static EditText patient_reg_edt_mname;
    private static EditText patient_reg_edt_lname;
    private static EditText patient_reg_edt_dob;
    //private static EditText patient_reg_edt_education;
    private static EditText patient_reg_edt_email;
    private static EditText patient_reg_edt_mobile;
    private static EditText patient_reg_flat_building_name;
    private static EditText patient_reg_street;

    private MaterialSpinner patient_reg_spinner_perfix;
    private MaterialSpinner patient_reg_spinner_gender;
    private MaterialSpinner patient_reg_spinner_maritalstatus;
    private MaterialSpinner patient_reg_spinner_bloodgroup;
    private MaterialSpinner patient_reg_spinner_country;
    private MaterialSpinner patient_reg_spinner_region;
    private MaterialSpinner patient_reg_spinner_state;
    private MaterialSpinner patient_reg_spinner_city;
    private MaterialSpinner patient_reg_spinner_known_from;

    private SpinnerAdapter.GenderAdapter getGenderArrayAdapter;
    private SpinnerAdapter.PerfixAdapter getPerfixArrayAdapter;
    private SpinnerAdapter.MaritalStatusAdapter getMaritalStatusArrayAdapter;
    private SpinnerAdapter.BloodGroupAdapter getBloodGroupArrayAdapter;
    private SpinnerAdapter.CountryAdapter countryAdapter;
    private SpinnerAdapter.RegionAdapter regionAdapter;
    private SpinnerAdapter.StateAdapter stateAdapter;
    private SpinnerAdapter.CityAdapter cityAdapter;
    private SpinnerAdapter.HealthspringReferralAdapter healthspringReferralAdapter;

    private static String PrefixID = "0";
    private static String GenderID = "0";
    private static String MarriedStatusID = "0";
    private static String BloodGroupID = "0";
    private static String HealthSpringReferralID = "0";
    private static String CountryID = "0";
    private static String RegionID = "0";
    private static String StateID = "0";
    private static String CityID = "0";

    private DatePickerDialog.OnDateSetListener dateListener;
    private Calendar calendar = Calendar.getInstance();
    private static SimpleDateFormat formate = new SimpleDateFormat(Constants.TIME_FORMAT);

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);
        InitSetting();
        InitView();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_patient_registration, container, false);
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
            doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();
            genderAdapter = databaseAdapter.new GenderAdapter();
            maritalStatusAdapter = databaseAdapter.new MaritalStatusAdapter();
            prefixAdapter = databaseAdapter.new PrefixAdapter();
            bloodGroupAdapter = databaseAdapter.new BloodGroupAdapter();
            countryMasterAdapter = databaseAdapter.new CountryMasterAdapter();
            stateMasterAdapter = databaseAdapter.new StateMasterAdapter();
            regionMasterAdapter = databaseAdapter.new RegionMasterAdapter();
            cityMasterAdapter = databaseAdapter.new CityMasterAdapter();
            healthSpringReferralMasterAdapter = databaseAdapter.new HealthSpringReferralMasterAdapter();

            listProfile = doctorProfileAdapter.listAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView(View rootView) {
        try {
            patient_reg_edt_fname = (EditText) rootView.findViewById(R.id.patient_reg_edt_fname);
            patient_reg_edt_mname = (EditText) rootView.findViewById(R.id.patient_reg_edt_mname);
            patient_reg_edt_lname = (EditText) rootView.findViewById(R.id.patient_reg_edt_lname);
            patient_reg_edt_dob = (EditText) rootView.findViewById(R.id.patient_reg_edt_dob);
            //patient_reg_edt_education = (EditText) rootView.findViewById(R.id.patient_reg_edt_education);
            patient_reg_edt_email = (EditText) rootView.findViewById(R.id.patient_reg_email);
            patient_reg_edt_mobile = (EditText) rootView.findViewById(R.id.patient_reg_mobile);
            patient_reg_flat_building_name = (EditText) rootView.findViewById(R.id.patient_reg_flat_building_name);
            patient_reg_street = (EditText) rootView.findViewById(R.id.patient_reg_street);

            patient_reg_spinner_perfix = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_spinnerperfix);
            patient_reg_spinner_gender = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_spinner_gender);
            patient_reg_spinner_maritalstatus = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_spinner_maritalstatus);
            patient_reg_spinner_bloodgroup = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_spinner_bloodgroup);
            patient_reg_spinner_country = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_spinner_country);
            patient_reg_spinner_region = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_spinner_region);
            patient_reg_spinner_state = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_spinner_state);
            patient_reg_spinner_city = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_spinner_city);
            patient_reg_spinner_known_from = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_spinner_known_from);
            patient_reg_edt_dob.setFocusable(false);

            patient_reg_edt_dob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog dialog = new DatePickerDialog(context, dateListener, Calendar.getInstance().get(Calendar.YEAR),
                            Calendar.getInstance().get(Calendar.MONTH),
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    dialog.getDatePicker().setMaxDate(new Date().getTime());
                    dialog.show();
                }
            });
            dateListener = new DatePickerDialog.OnDateSetListener() {
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

                    patient_reg_edt_dob.setText(year + "-" + mMonth + "-" + mDay);
                }
            };
            InitAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitAdapter() {
        try {
            listGender = genderAdapter.listAll();
            listMaritalStatus = maritalStatusAdapter.listAll();
            listPrefix = prefixAdapter.listAll();
            listbloodGroups = bloodGroupAdapter.listAll();
            elCountryMasterArrayList = countryMasterAdapter.listAll();
            elHealthspringReferralArrayList = healthSpringReferralMasterAdapter.listAll();

            if (listGender != null && listGender.size() > 0) {
                getGenderArrayAdapter = new SpinnerAdapter.GenderAdapter(context, listGender);
                patient_reg_spinner_gender.setAdapter(getGenderArrayAdapter);
                getGenderArrayAdapter.notifyDataSetChanged();

                patient_reg_spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int sGenderPos = patient_reg_spinner_gender.getSelectedItemPosition();
                        if (sGenderPos == 0) {
                            GenderID = "0";
                        } else if (sGenderPos > 0) {
                            sGenderPos = sGenderPos - 1;
                            GenderID = listGender.get(sGenderPos).getID();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                getGenderArrayAdapter = new SpinnerAdapter.GenderAdapter(context, listGender);
                patient_reg_spinner_gender.setAdapter(getGenderArrayAdapter);
            }

            if (listPrefix != null && listPrefix.size() > 0) {
                getPerfixArrayAdapter = new SpinnerAdapter.PerfixAdapter(context, listPrefix);
                patient_reg_spinner_perfix.setAdapter(getPerfixArrayAdapter);
                getPerfixArrayAdapter.notifyDataSetChanged();

                patient_reg_spinner_perfix.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int sPrefixPos = patient_reg_spinner_perfix.getSelectedItemPosition();
                        if (sPrefixPos == 0) {
                            PrefixID = "0";
                        } else if (sPrefixPos > 0) {
                            sPrefixPos = sPrefixPos - 1;
                            PrefixID = listPrefix.get(sPrefixPos).getID();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                getPerfixArrayAdapter = new SpinnerAdapter.PerfixAdapter(context, listPrefix);
                patient_reg_spinner_perfix.setAdapter(getPerfixArrayAdapter);
            }

            if (listMaritalStatus != null && listMaritalStatus.size() > 0) {
                getMaritalStatusArrayAdapter = new SpinnerAdapter.MaritalStatusAdapter(context, listMaritalStatus);
                patient_reg_spinner_maritalstatus.setAdapter(getMaritalStatusArrayAdapter);
                getMaritalStatusArrayAdapter.notifyDataSetChanged();

                patient_reg_spinner_maritalstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int sMarriedStatusPos = patient_reg_spinner_maritalstatus.getSelectedItemPosition();
                        if (sMarriedStatusPos == 0) {
                            MarriedStatusID = "0";
                        } else if (sMarriedStatusPos > 0) {
                            sMarriedStatusPos = sMarriedStatusPos - 1;
                            MarriedStatusID = listMaritalStatus.get(sMarriedStatusPos).getID();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                getMaritalStatusArrayAdapter = new SpinnerAdapter.MaritalStatusAdapter(context, listMaritalStatus);
                patient_reg_spinner_maritalstatus.setAdapter(getMaritalStatusArrayAdapter);
            }

            if (listbloodGroups != null && listbloodGroups.size() > 0) {
                getBloodGroupArrayAdapter = new SpinnerAdapter.BloodGroupAdapter(context, listbloodGroups);
                patient_reg_spinner_bloodgroup.setAdapter(getBloodGroupArrayAdapter);
                getBloodGroupArrayAdapter.notifyDataSetChanged();

                patient_reg_spinner_bloodgroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int sBloodGroupPos = patient_reg_spinner_bloodgroup.getSelectedItemPosition();
                        if (sBloodGroupPos == 0) {
                            BloodGroupID = "0";
                        } else if (sBloodGroupPos > 0) {
                            sBloodGroupPos = sBloodGroupPos - 1;
                            BloodGroupID = listbloodGroups.get(sBloodGroupPos).getID();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                getBloodGroupArrayAdapter = new SpinnerAdapter.BloodGroupAdapter(context, listbloodGroups);
                patient_reg_spinner_bloodgroup.setAdapter(getBloodGroupArrayAdapter);
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

            if (elCountryMasterArrayList != null && elCountryMasterArrayList.size() > 0) {
                countryAdapter = new SpinnerAdapter.CountryAdapter(context, elCountryMasterArrayList);
                patient_reg_spinner_country.setAdapter(countryAdapter);
                countryAdapter.notifyDataSetChanged();

                patient_reg_spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int sCountryPos = patient_reg_spinner_country.getSelectedItemPosition();
                        if (sCountryPos == 0) {
                            CountryID = "0";
                        } else if (sCountryPos > 0) {
                            sCountryPos = sCountryPos - 1;
                            CountryID = elCountryMasterArrayList.get(sCountryPos).getID();
                        }
                        setRegionDropdown(CountryID);
                        setStateDropdown(CountryID, "0");
                        setCityDropdown(CountryID, "0", "0");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                countryAdapter = new SpinnerAdapter.CountryAdapter(context, elCountryMasterArrayList);
                patient_reg_spinner_country.setAdapter(countryAdapter);
                CountryID = "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setRegionDropdown(String countryID) {
        elRegionMasterArrayList = regionMasterAdapter.listAll(countryID);
        if (elRegionMasterArrayList != null && elRegionMasterArrayList.size() > 0) {
            regionAdapter = new SpinnerAdapter.RegionAdapter(context, elRegionMasterArrayList);
            patient_reg_spinner_region.setAdapter(regionAdapter);
            regionAdapter.notifyDataSetChanged();

            patient_reg_spinner_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int sRegionPos = patient_reg_spinner_region.getSelectedItemPosition();
                    if (sRegionPos == 0) {
                        RegionID = "0";
                    } else if (sRegionPos > 0) {
                        sRegionPos = sRegionPos - 1;
                        RegionID = elRegionMasterArrayList.get(sRegionPos).getID();
                    }
                    setStateDropdown(CountryID, RegionID);
                    setCityDropdown(CountryID, RegionID, "0");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } else {
            regionAdapter = new SpinnerAdapter.RegionAdapter(context, elRegionMasterArrayList);
            patient_reg_spinner_region.setAdapter(regionAdapter);
            RegionID = "0";
        }
    }

    private void setStateDropdown(String countryID, String regionID) {
        elStateMasterArrayList = stateMasterAdapter.listAll(countryID, regionID);
        if (elStateMasterArrayList != null && elStateMasterArrayList.size() > 0) {
            stateAdapter = new SpinnerAdapter.StateAdapter(context, elStateMasterArrayList);
            patient_reg_spinner_state.setAdapter(stateAdapter);
            stateAdapter.notifyDataSetChanged();

            patient_reg_spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int sStatePos = patient_reg_spinner_state.getSelectedItemPosition();
                    if (sStatePos == 0) {
                        StateID = "0";
                    } else if (sStatePos > 0) {
                        sStatePos = sStatePos - 1;
                        StateID = elStateMasterArrayList.get(sStatePos).getID();
                    }
                    setCityDropdown(CountryID, RegionID, StateID);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } else {
            stateAdapter = new SpinnerAdapter.StateAdapter(context, elStateMasterArrayList);
            patient_reg_spinner_state.setAdapter(stateAdapter);
            StateID = "0";
        }
    }

    private void setCityDropdown(String countryID, String regionID, String stateID) {
        elCityMasterArrayList = cityMasterAdapter.listAll(countryID, regionID, stateID);
        if (elCityMasterArrayList != null && elCityMasterArrayList.size() > 0) {
            cityAdapter = new SpinnerAdapter.CityAdapter(context, elCityMasterArrayList);
            patient_reg_spinner_city.setAdapter(cityAdapter);
            cityAdapter.notifyDataSetChanged();

            patient_reg_spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int sCityPos = patient_reg_spinner_city.getSelectedItemPosition();
                    if (sCityPos == 0) {
                        CityID = "0";
                    } else if (sCityPos > 0) {
                        sCityPos = sCityPos - 1;
                        CityID = elCityMasterArrayList.get(sCityPos).getID();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } else {
            cityAdapter = new SpinnerAdapter.CityAdapter(context, elCityMasterArrayList);
            patient_reg_spinner_city.setAdapter(cityAdapter);
            CityID = "0";
        }
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.menu_toolbar_save).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toolbar_save:
                if (validateControls(context)) {
                    PatientRegistration();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    public static boolean validateControls(Context context) {
        if (PrefixID.equals("0")) {
            Validate.Msgshow(context, "Please select prefix.");
            return false;
        } else if (!Validate.hasTextIn(patient_reg_edt_fname.getText().toString())) {
            Validate.Msgshow(context, "Please enter first name.");
            return false;
        }
        /*else if (!Validate.hasTextIn(patient_reg_edt_mname.getText().toString())) {
            Validate.Msgshow(context, "Please enter middle name.");
            return false;
        }*/
        else if (!Validate.hasTextIn(patient_reg_edt_lname.getText().toString())) {
            Validate.Msgshow(context, "Please enter last name.");
            return false;
        } else if (!Validate.hasTextIn(patient_reg_edt_dob.getText().toString())) {
            Validate.Msgshow(context, "Please enter birth date.");
            return false;
        } else if (GenderID.equals("0")) {
            Validate.Msgshow(context, "Please select Gender.");
            return false;
        }
        /*else if (MarriedStatusID.equals("")) {
            Validate.Msgshow(context, "Please select Marital Status.");
            return false;
        }*/
        else if (HealthSpringReferralID.equals("0")) {
            Validate.Msgshow(context, "Please select known of Healthspring.");
            return false;
        } else if (!Validate.hasTextIn(patient_reg_edt_email.getText().toString())) {
            Validate.Msgshow(context, "Please enter email address.");
            return false;
        } else if (!Validate.isValidEmail(patient_reg_edt_email.getText().toString())) {
            Validate.Msgshow(context, "Invalid email address.");
            return false;
        } else if (!Validate.hasTextIn(patient_reg_edt_mobile.getText().toString())) {
            Validate.Msgshow(context, "Please enter Mobile number.");
            return false;
        } else if (patient_reg_edt_mobile.getText().toString().trim().length() != 10) {
            Validate.Msgshow(context, "Mobile number length must be 10 digit.");
            return false;
        } else if (!Validate.hasTextIn(patient_reg_flat_building_name.getText().toString())) {
            Validate.Msgshow(context, "Please enter flat and Building info.");
            return false;
        } else if (!Validate.hasTextIn(patient_reg_street.getText().toString())) {
            Validate.Msgshow(context, "Please enter street address.");
            return false;
        }
        return true;
    }

    public static Patient PatientInformation() {
        Patient patient = new Patient();
        try {
            patient.setPrefixID(PrefixID);
            patient.setFirstName(patient_reg_edt_fname.getText().toString());
            patient.setMiddleName(patient_reg_edt_mname.getText().toString());
            patient.setLastName(patient_reg_edt_lname.getText().toString());
            patient.setDateOfBirth(patient_reg_edt_dob.getText().toString().trim());
            patient.setGenderID(GenderID);
            patient.setMaritalStatusID(MarriedStatusID);
            patient.setBloodGroupID(BloodGroupID);
            patient.setHealthSpringReferralID(HealthSpringReferralID);
            patient.setEmail(patient_reg_edt_email.getText().toString().trim());
            patient.setContactNo1(patient_reg_edt_mobile.getText().toString().trim());
            patient.setFlatBuildingName(patient_reg_flat_building_name.getText().toString().trim());
            patient.setStreetName(patient_reg_street.getText().toString().trim());
            patient.setCountryID(CountryID);
            patient.setRegionID(RegionID);
            patient.setStateID(StateID);
            patient.setCityID(CityID);
            patient.setRegistrationDate(formate.format(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patient;
    }

    /*private void Clear() {
        patient_reg_edt_fname.setText(null);
        patient_reg_edt_mname.setText(null);
        patient_reg_edt_lname.setText(null);
        patient_reg_edt_dob.setText(null);
        patient_reg_edt_email.setText(null);
        patient_reg_edt_mobile.setText(null);
        //patient_reg_edt_education.setText(null);
        patient_reg_spinner_gender.setSelection(0);
        patient_reg_spinner_perfix.setSelection(0);
        patient_reg_spinner_maritalstatus.setSelection(0);
        patient_reg_spinner_perfix.requestFocus();
    }*/

    /*public class PatientRegistrationTask extends AsyncTask<Void, Void, String> {
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
                jSonData = objMapper.unMap(patient);
                serviceConsumer = new WebServiceConsumer(context, null, null);
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
                if (responseCode == Constants.HTTP_CREATED_201) {
                    localSetting.hideDialog(progressDialog);
                    new AlertDialog
                            .Builder(context)
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage("Patient registered successfully.")
                            .setCancelable(true)
                            .setPositiveButton("Go to Patient List", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    *//*flag = flagAdapter.listCurrent();
                                    flag.setFlag(Constants.PATIENT_LIST_TASK);
                                    flagAdapter.create(flag);
                                    SchedulerManager.getInstance().runNow(context, SynchronizationTask.class, 1);*//*
                                    Clear();
                                    Constants.refreshPatient = true;
                                    //finish();
                                    //startActivity(new Intent(context, PatientListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    //startActivity(new Intent(context, DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                }
                            })
                            .setIcon(R.mipmap.ic_launcher).show();
                } else {
                    localSetting.hideDialog(progressDialog);
                    localSetting.alertbox(context, localSetting.handleError(responseCode), false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }*/
}
