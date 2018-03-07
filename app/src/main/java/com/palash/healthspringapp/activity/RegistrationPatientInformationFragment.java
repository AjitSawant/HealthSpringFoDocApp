package com.palash.healthspringapp.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
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
import com.palash.healthspringapp.entity.ELDoctorMaster;
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class RegistrationPatientInformationFragment extends Fragment {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.PrefixAdapter prefixAdapter;
    private DatabaseAdapter.GenderAdapter genderAdapter;
    private DatabaseAdapter.PCPDoctorMasterAdapter patientPCPDoctorAdapterDB;
    //private DatabaseAdapter.CountryMasterAdapter countryMasterAdapter;
    //private DatabaseAdapter.RegionMasterAdapter regionMasterAdapter;
    //private DatabaseAdapter.StateMasterAdapter stateMasterAdapter;
    //private DatabaseAdapter.CityMasterAdapter cityMasterAdapter;

    private ArrayList<DoctorProfile> listProfile;
    private ArrayList<Prefix> listPrefix;
    private ArrayList<Gender> listGender;
    private ArrayList<ELDoctorMaster> elPCPDoctorArrayList = new ArrayList<>();
    //private ArrayList<ELCountryMaster> elCountryMasterArrayList;
    //private ArrayList<ELRegionMaster> elRegionMasterArrayList;
    //private ArrayList<ELStateMaster> elStateMasterArrayList;
    //private ArrayList<ELCityMaster> elCityMasterArrayList;

    private static EditText patient_reg_edt_fname;
    private static EditText patient_reg_edt_lname;
    private static EditText patient_reg_edt_dob;
    private static EditText patient_reg_edt_email;
    private static EditText patient_reg_edt_mobile;
    private static EditText patient_reg_address;

    private MaterialSpinner patient_reg_spinner_perfix;
    private MaterialSpinner patient_reg_spinner_gender;
    private MaterialSpinner patient_reg_pcp_doctor;
    //private MaterialSpinner patient_reg_spinner_country;
    //private MaterialSpinner patient_reg_spinner_region;
    //private MaterialSpinner patient_reg_spinner_state;
    //private MaterialSpinner patient_reg_spinner_city;

    private SpinnerAdapter.GenderAdapter getGenderArrayAdapter;
    private SpinnerAdapter.PerfixAdapter getPerfixArrayAdapter;
    private SpinnerAdapter.PCPDoctorListAdapter patientPCPDoctorListAdapter;
    //private SpinnerAdapter.CountryAdapter countryAdapter;
    //private SpinnerAdapter.RegionAdapter regionAdapter;
    //private SpinnerAdapter.StateAdapter stateAdapter;
    //private SpinnerAdapter.CityAdapter cityAdapter;

    private static String PrefixID = "0";
    private static String GenderID = "0";
    private static String PCPDoctorID = "0";
    //private static String CountryID = "0";
    //private static String RegionID = "0";
    //private static String StateID = "0";
    //private static String CityID = "0";

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private DatePickerDialog.OnDateSetListener dateListener;
    private Calendar calendar = Calendar.getInstance();
    private static SimpleDateFormat formate = new SimpleDateFormat(Constants.TIME_FORMAT);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_patient_registration, container, false);
        InitSetting();
        InitView(rootView);
        InitAdapter();
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
            prefixAdapter = databaseAdapter.new PrefixAdapter();
            patientPCPDoctorAdapterDB = databaseAdapter.new PCPDoctorMasterAdapter();
            //countryMasterAdapter = databaseAdapter.new CountryMasterAdapter();
            //stateMasterAdapter = databaseAdapter.new StateMasterAdapter();
            //regionMasterAdapter = databaseAdapter.new RegionMasterAdapter();
            //cityMasterAdapter = databaseAdapter.new CityMasterAdapter();

            listProfile = doctorProfileAdapter.listAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView(View rootView) {
        try {
            patient_reg_edt_fname = (EditText) rootView.findViewById(R.id.patient_reg_edt_fname);
            patient_reg_edt_lname = (EditText) rootView.findViewById(R.id.patient_reg_edt_lname);
            patient_reg_edt_dob = (EditText) rootView.findViewById(R.id.patient_reg_edt_dob);
            patient_reg_edt_email = (EditText) rootView.findViewById(R.id.patient_reg_email);
            patient_reg_edt_mobile = (EditText) rootView.findViewById(R.id.patient_reg_mobile);
            patient_reg_address = (EditText) rootView.findViewById(R.id.patient_reg_address);

            patient_reg_spinner_perfix = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_spinnerperfix);
            patient_reg_spinner_gender = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_spinner_gender);
            patient_reg_pcp_doctor = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_pcp_doctor);
            //patient_reg_spinner_country = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_spinner_country);
            //patient_reg_spinner_region = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_spinner_region);
            //patient_reg_spinner_state = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_spinner_state);
            //patient_reg_spinner_city = (MaterialSpinner) rootView.findViewById(R.id.patient_reg_spinner_city);
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

            patient_reg_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        /*AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                                .build();*/

                        Intent intent =
                                new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                        //.setFilter(typeFilter)
                                        .build(getActivity());
                        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                    } catch (GooglePlayServicesRepairableException e) {
                        // TODO: Handle the error.
                    } catch (GooglePlayServicesNotAvailableException e) {
                        // TODO: Handle the error.
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i(Constants.TAG, "Place: " + place.getAddress());
                patient_reg_address.setText(place.getAddress());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i(Constants.TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void InitAdapter() {
        try {
            listGender = genderAdapter.listAll();
            listPrefix = prefixAdapter.listAll();
            //elCountryMasterArrayList = countryMasterAdapter.listAll();

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

            if(listProfile!=null && listProfile.size()>0) {
                elPCPDoctorArrayList = patientPCPDoctorAdapterDB.listAll(listProfile.get(0).getUnitID());
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

            /*if (elCountryMasterArrayList != null && elCountryMasterArrayList.size() > 0) {
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
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void setRegionDropdown(String countryID) {
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
    }*/

    public static boolean validateControls(Context context) {
        if (PrefixID.equals("0")) {
            Validate.Msgshow(context, "Please select prefix.");
            return false;
        } else if (!Validate.hasTextIn(patient_reg_edt_fname.getText().toString())) {
            Validate.Msgshow(context, "Please enter first name.");
            return false;
        } else if (!Validate.hasTextIn(patient_reg_edt_lname.getText().toString())) {
            Validate.Msgshow(context, "Please enter last name.");
            return false;
        } else if (!Validate.hasTextIn(patient_reg_edt_dob.getText().toString())) {
            Validate.Msgshow(context, "Please enter birth date.");
            return false;
        } else if (GenderID.equals("0")) {
            Validate.Msgshow(context, "Please select Gender.");
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
        } else if (!Validate.hasTextIn(patient_reg_address.getText().toString())) {
            Validate.Msgshow(context, "Please enter address.");
            return false;
        }else if (PCPDoctorID.equals("0")) {
            Validate.Msgshow(context, "Please select PCP doctor.");
            return false;
        }
        return true;
    }

    public static Patient PatientInformation() {
        Patient patient = new Patient();
        try {
            patient.setPrefixID(PrefixID);
            patient.setFirstName(patient_reg_edt_fname.getText().toString());
            patient.setLastName(patient_reg_edt_lname.getText().toString());
            patient.setDateOfBirth(patient_reg_edt_dob.getText().toString().trim());
            patient.setGenderID(GenderID);
            patient.setEmail(patient_reg_edt_email.getText().toString().trim());
            patient.setContactNo1(patient_reg_edt_mobile.getText().toString().trim());
            patient.setFlatBuildingName(patient_reg_address.getText().toString().trim());
            patient.setPCPDoctorID(PCPDoctorID);
            //patient.setCountryID(CountryID);
            //patient.setRegionID(RegionID);
            //patient.setStateID(StateID);
            //patient.setCityID(CityID);
            patient.setRegistrationDate(formate.format(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patient;
    }
}
