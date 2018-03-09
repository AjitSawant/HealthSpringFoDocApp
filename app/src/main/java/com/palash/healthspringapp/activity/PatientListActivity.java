package com.palash.healthspringapp.activity;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.palash.healthspringapp.R;
import com.palash.healthspringapp.adapter.SearchPatientAdapter;
import com.palash.healthspringapp.api.JsonObjectMapper;
import com.palash.healthspringapp.api.WebServiceConsumer;
import com.palash.healthspringapp.database.DatabaseAdapter;
import com.palash.healthspringapp.database.DatabaseContract;
import com.palash.healthspringapp.entity.DoctorProfile;
import com.palash.healthspringapp.entity.ELFilter;
import com.palash.healthspringapp.entity.Patient;
import com.palash.healthspringapp.utilities.Constants;
import com.palash.healthspringapp.utilities.LocalSetting;
import com.palash.healthspringapp.utilities.TransparentProgressDialog;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fr.ganfra.materialspinner.MaterialSpinner;

public class PatientListActivity extends AppCompatActivity {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.PatientAdapter patientAdapterDB;

    private SearchPatientAdapter searchPatientAdapter;

    private ArrayList<DoctorProfile> doctorProfileList;
    private ArrayList<Patient> patientList;
    private ListView search_patient_List;
    private TextView search_patient_empty;
    private EditText patient_list_search_by_name;

    private static String startDate = "";
    private static String endDate = "";
    private static String mrNO = "";
    private static String firstName = "";
    private static String lastName = "";
    private static String mobileNo = "";
    private static String selectedID = "";

    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat format = new SimpleDateFormat(Constants.PATIENT_QUEUE_DATE);
    private SimpleDateFormat dates = new SimpleDateFormat(Constants.SEARCH_DATE_FORMAT);
    private DatePickerDialog.OnDateSetListener dateListenerFrom;
    private DatePickerDialog.OnDateSetListener dateListenerTo;

    DialogPlus dialog;

    private RadioGroup radio_group_1;
    private RadioGroup radio_group_2;
    private RadioGroup radio_group_3;
    private RadioButton radio_button_by_mrno;
    private RadioButton radio_button_by_fname;
    private RadioButton radio_button_by_lname;
    private RadioButton radio_button_by_mobileNo;
    private RadioButton radio_button_by_reg_date;
    private RadioButton radio_button_by_visit_date;
    private RadioButton radio_button_by_category;

    private TextView filter_text_1;
    private TextView filter_text_2;

    private EditText patient_registration_mrno_edt;
    private EditText patient_registration_firstname_edt;
    private EditText patient_registration_lastname_edt;
    private EditText patient_registration_mobile_no_edt;
    private TextView patient_registration_start_date_edt;
    private TextView patient_registration_end_date_edt;
    private TextView patient_register_visit_start_date_edt;
    private TextView patient_register_visit_end_date_edt;

    private LinearLayout layout_search_by_name_view;
    private LinearLayout layout_search_by_reg_date_view;
    private LinearLayout layout_search_by_visit_date_view;
    private LinearLayout patient_register_layout_category_L3;
    private MaterialSpinner patient_register_category_L3;
    private Button patient_report_date_search_btn;

    private int checkRadio = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_patient);
        InitSetting();
        InitView();
        showFilterDialog();
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
            patientAdapterDB = databaseAdapter.new PatientAdapter();
            doctorProfileList = doctorProfileAdapter.listAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView() {
        try {
            search_patient_List = (ListView) findViewById(R.id.search_patient_List);
            search_patient_empty = (TextView) findViewById(R.id.search_patient_empty);
            patient_list_search_by_name = (EditText) findViewById(R.id.patient_list_search_by_name);

            patient_list_search_by_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    if (patientList != null && patientList.size() > 0) {
                        searchPatientAdapter.getFilter().filter(s);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        if (Constants.refreshPatient == true) {
            RefreshPatientList();
        }
        super.onResume();
    }

    private void RefreshPatientList() {
        doctorProfileList = doctorProfileAdapter.listAll();
        if (doctorProfileList != null && doctorProfileList.size() > 0) {
            patientList = patientAdapterDB.listPatient(doctorProfileList.get(0).getUnitID(), null);
            if (patientList != null && patientList.size() > 0) {
                searchPatientAdapter = new SearchPatientAdapter(context, patientList);
                search_patient_List.setAdapter(searchPatientAdapter);
                searchPatientAdapter.notifyDataSetChanged();
                search_patient_empty.setVisibility(View.GONE);
                search_patient_List.setVisibility(View.VISIBLE);
                patient_list_search_by_name.setVisibility(View.VISIBLE);
            } else {
                search_patient_empty.setVisibility(View.VISIBLE);
                search_patient_List.setVisibility(View.GONE);
                patient_list_search_by_name.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.menu_toolbar_add_patient).setVisible(false);
        menu.findItem(R.id.menu_toolbar_filter).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toolbar_add_patient:
                if (doctorProfileList != null && doctorProfileList.size() > 0) {
                    if (localSetting.checkUnitName(doctorProfileList.get(0).getUnitID())) {
                        localSetting.showWarningAlert(context, context.getResources().getString(R.string.opps_alert), context.getResources().getString(R.string.register_alert));
                    } else {
                        startActivity(new Intent(context, RegistrationDashActivity.class));
                    }
                }else{
                    startActivity(new Intent(context, RegistrationDashActivity.class));
                }
                return true;
            case R.id.menu_toolbar_filter:
                showFilterDialog();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showFilterDialog() {
        dialog = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(R.layout.filter_search_patient_view))
                .setExpanded(true)
                .setCancelable(true)// This will enable the expand feature, (similar to android L share dialog)
                .create();
        View view = dialog.getHolderView();
        initFilter(view);
        dialog.show();
    }

    private void initFilter(View view) {
        radio_group_1 = (RadioGroup) view.findViewById(R.id.radio_group_1);
        radio_group_2 = (RadioGroup) view.findViewById(R.id.radio_group_2);
        radio_group_3 = (RadioGroup) view.findViewById(R.id.radio_group_3);

        radio_button_by_mrno = (RadioButton) view.findViewById(R.id.radio_button_by_mrno);
        radio_button_by_fname = (RadioButton) view.findViewById(R.id.radio_button_by_fname);
        radio_button_by_lname = (RadioButton) view.findViewById(R.id.radio_button_by_lname);
        radio_button_by_mobileNo = (RadioButton) view.findViewById(R.id.radio_button_by_mobileNo);
        radio_button_by_reg_date = (RadioButton) view.findViewById(R.id.radio_button_by_reg_date);
        radio_button_by_visit_date = (RadioButton) view.findViewById(R.id.radio_button_by_visit_date);
        radio_button_by_category = (RadioButton) view.findViewById(R.id.radio_button_by_category);

        filter_text_1 = (TextView) view.findViewById(R.id.filter_text_1);
        filter_text_2 = (TextView) view.findViewById(R.id.filter_text_2);

        patient_registration_mrno_edt = (EditText) view.findViewById(R.id.patient_registration_mrno_edt);
        patient_registration_firstname_edt = (EditText) view.findViewById(R.id.patient_registration_firstname_edt);
        patient_registration_lastname_edt = (EditText) view.findViewById(R.id.patient_registration_lastname_edt);
        patient_registration_mobile_no_edt = (EditText) view.findViewById(R.id.patient_registration_mobile_no_edt);
        patient_registration_start_date_edt = (TextView) view.findViewById(R.id.patient_registration_start_date_edt);
        patient_registration_end_date_edt = (TextView) view.findViewById(R.id.patient_registration_end_date_edt);
        patient_register_visit_start_date_edt = (TextView) view.findViewById(R.id.patient_register_visit_start_date_edt);
        patient_register_visit_end_date_edt = (TextView) view.findViewById(R.id.patient_register_visit_end_date_edt);

        layout_search_by_name_view = (LinearLayout) view.findViewById(R.id.layout_search_by_name_view);
        layout_search_by_reg_date_view = (LinearLayout) view.findViewById(R.id.layout_search_by_reg_date_view);
        layout_search_by_visit_date_view = (LinearLayout) view.findViewById(R.id.layout_search_by_visit_date_view);
        patient_register_layout_category_L3 = (LinearLayout) view.findViewById(R.id.patient_register_layout_category_L3);
        patient_register_category_L3 = (MaterialSpinner) view.findViewById(R.id.patient_register_category_L3);
        patient_report_date_search_btn = (Button) view.findViewById(R.id.patient_report_date_search_btn);

        patient_registration_mrno_edt.setVisibility(View.VISIBLE);
        layout_search_by_name_view.setVisibility(View.GONE);
        patient_registration_mobile_no_edt.setVisibility(View.GONE);
        layout_search_by_reg_date_view.setVisibility(View.GONE);
        layout_search_by_visit_date_view.setVisibility(View.GONE);
        patient_register_layout_category_L3.setVisibility(View.GONE);
        radio_group_2.clearCheck();
        radio_group_3.clearCheck();

        filter_text_1.setVisibility(View.VISIBLE);
        filter_text_2.setVisibility(View.GONE);
        filter_text_1.setText("MRNo");
        patient_registration_mrno_edt.setText("");
        checkRadio = 0;

        radio_button_by_mrno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patient_registration_mrno_edt.setVisibility(View.VISIBLE);
                layout_search_by_name_view.setVisibility(View.GONE);
                patient_registration_mobile_no_edt.setVisibility(View.GONE);
                layout_search_by_reg_date_view.setVisibility(View.GONE);
                layout_search_by_visit_date_view.setVisibility(View.GONE);
                patient_register_layout_category_L3.setVisibility(View.GONE);
                radio_group_2.clearCheck();
                radio_group_3.clearCheck();

                filter_text_1.setVisibility(View.VISIBLE);
                filter_text_2.setVisibility(View.GONE);
                filter_text_1.setText("MRNo");
                checkRadio = 0;
            }
        });

        radio_button_by_fname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patient_registration_mrno_edt.setVisibility(View.GONE);
                layout_search_by_name_view.setVisibility(View.VISIBLE);
                patient_registration_mobile_no_edt.setVisibility(View.GONE);
                layout_search_by_reg_date_view.setVisibility(View.GONE);
                layout_search_by_visit_date_view.setVisibility(View.GONE);
                patient_register_layout_category_L3.setVisibility(View.GONE);
                radio_group_2.clearCheck();
                radio_group_3.clearCheck();

                filter_text_1.setVisibility(View.VISIBLE);
                filter_text_2.setVisibility(View.VISIBLE);
                filter_text_1.setText("First Name");
                filter_text_2.setText("Last Name");
                checkRadio = 1;
            }
        });

        /*radio_button_by_lname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patient_registration_mrno_edt.setVisibility(View.GONE);
                patient_registration_firstname_edt.setVisibility(View.GONE);
                patient_registration_lastname_edt.setVisibility(View.VISIBLE);
                patient_registration_mobile_no_edt.setVisibility(View.GONE);
                layout_search_by_reg_date_view.setVisibility(View.GONE);
                layout_search_by_visit_date_view.setVisibility(View.GONE);
                patient_register_layout_category_L3.setVisibility(View.GONE);
                radio_group_2.clearCheck();
                radio_group_3.clearCheck();

                filter_text_1.setVisibility(View.VISIBLE);
                filter_text_2.setVisibility(View.GONE);
                filter_text_1.setText("Last Name");
                checkRadio = 2;
            }
        });*/

        radio_button_by_mobileNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patient_registration_mrno_edt.setVisibility(View.GONE);
                patient_registration_firstname_edt.setVisibility(View.GONE);
                patient_registration_lastname_edt.setVisibility(View.GONE);
                patient_registration_mobile_no_edt.setVisibility(View.VISIBLE);
                layout_search_by_reg_date_view.setVisibility(View.GONE);
                layout_search_by_visit_date_view.setVisibility(View.GONE);
                patient_register_layout_category_L3.setVisibility(View.GONE);
                radio_group_2.clearCheck();
                radio_group_3.clearCheck();

                filter_text_1.setVisibility(View.VISIBLE);
                filter_text_2.setVisibility(View.GONE);
                filter_text_1.setText("Mobile Number");
                checkRadio = 3;
            }
        });

        radio_button_by_reg_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patient_registration_mrno_edt.setVisibility(View.GONE);
                patient_registration_firstname_edt.setVisibility(View.GONE);
                patient_registration_lastname_edt.setVisibility(View.GONE);
                patient_registration_mobile_no_edt.setVisibility(View.GONE);
                layout_search_by_reg_date_view.setVisibility(View.VISIBLE);
                layout_search_by_visit_date_view.setVisibility(View.GONE);
                patient_register_layout_category_L3.setVisibility(View.GONE);
                radio_group_1.clearCheck();
                radio_group_3.clearCheck();

                filter_text_1.setVisibility(View.VISIBLE);
                filter_text_2.setVisibility(View.VISIBLE);
                filter_text_1.setText("Reg start date");
                filter_text_2.setText("Reg end date");
                checkRadio = 4;

                patient_registration_start_date_edt.setText(format.format(new Date()));
                patient_registration_end_date_edt.setText(format.format(new Date()));
                startDate = localSetting.formatDate(patient_registration_start_date_edt.getText().toString(), Constants.PATIENT_QUEUE_DATE, Constants.SEARCH_DATE_FORMAT);
                endDate = localSetting.formatDate(patient_registration_end_date_edt.getText().toString(), Constants.PATIENT_QUEUE_DATE, Constants.SEARCH_DATE_FORMAT);
            }
        });

        radio_button_by_visit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patient_registration_mrno_edt.setVisibility(View.GONE);
                patient_registration_firstname_edt.setVisibility(View.GONE);
                patient_registration_lastname_edt.setVisibility(View.GONE);
                patient_registration_mobile_no_edt.setVisibility(View.GONE);
                layout_search_by_reg_date_view.setVisibility(View.GONE);
                layout_search_by_visit_date_view.setVisibility(View.VISIBLE);
                patient_register_layout_category_L3.setVisibility(View.GONE);
                radio_group_1.clearCheck();
                radio_group_3.clearCheck();

                filter_text_1.setVisibility(View.VISIBLE);
                filter_text_2.setVisibility(View.VISIBLE);
                filter_text_1.setText("Visit start date");
                filter_text_2.setText("Visit end date");
                checkRadio = 5;

                patient_register_visit_start_date_edt.setText(format.format(new Date()));
                patient_register_visit_end_date_edt.setText(format.format(new Date()));
                startDate = localSetting.formatDate(patient_register_visit_start_date_edt.getText().toString(), Constants.PATIENT_QUEUE_DATE, Constants.SEARCH_DATE_FORMAT);
                endDate = localSetting.formatDate(patient_register_visit_end_date_edt.getText().toString(), Constants.PATIENT_QUEUE_DATE, Constants.SEARCH_DATE_FORMAT);
            }
        });

        patient_registration_start_date_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(context, dateListenerFrom, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                endDate = "";
            }
        });

        patient_registration_end_date_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startDate.equals("") || startDate.length() == 0) {
                    Toast.makeText(context, "Please first select start date.", Toast.LENGTH_SHORT).show();
                } else {
                    new DatePickerDialog(context, dateListenerTo, calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        patient_register_visit_start_date_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(context, dateListenerFrom, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                endDate = "";
            }
        });

        patient_register_visit_end_date_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startDate.equals("") || startDate.length() == 0) {
                    Toast.makeText(context, "Please first select start date.", Toast.LENGTH_SHORT).show();
                } else {
                    new DatePickerDialog(context, dateListenerTo, calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        dateListenerFrom = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                startDate = localSetting.dateToString(day, month, year, Constants.SEARCH_DATE_FORMAT);
                endDate = localSetting.dateToString(day, month, year, Constants.SEARCH_DATE_FORMAT);

                // for reg
                patient_registration_start_date_edt.setText(localSetting.dateToString(day, month, year, Constants.PATIENT_QUEUE_DATE));
                patient_registration_end_date_edt.setText(localSetting.dateToString(day, month, year, Constants.PATIENT_QUEUE_DATE));

                //for visit
                patient_register_visit_start_date_edt.setText(localSetting.dateToString(day, month, year, Constants.PATIENT_QUEUE_DATE));
                patient_register_visit_end_date_edt.setText(localSetting.dateToString(day, month, year, Constants.PATIENT_QUEUE_DATE));
            }
        };

        dateListenerTo = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                endDate = localSetting.dateToString(day, month, year, Constants.SEARCH_DATE_FORMAT);
                try {
                    //Dates to compare
                    Date date1;
                    Date date2;

                    //Setting dates
                    date1 = dates.parse(startDate);
                    date2 = dates.parse(endDate);

                    //Comparing dates
                    long difference = Math.abs(date1.getTime() - date2.getTime());
                    long differenceDates = difference / (24 * 60 * 60 * 1000);

                    //Convert long to String
                    String dayDifference = Long.toString(differenceDates);

                    if (date1.before(date2) || date1.equals(date2)) {
                        if (Integer.parseInt(dayDifference) < Constants.FILTER_DAYS_COUNT) {
                            patient_registration_end_date_edt.setText(localSetting.dateToString(day, month, year, Constants.PATIENT_QUEUE_DATE));
                            patient_register_visit_end_date_edt.setText(localSetting.dateToString(day, month, year, Constants.PATIENT_QUEUE_DATE));
                        } else {
                            endDate = "";
                            patient_registration_end_date_edt.setText("");
                            patient_register_visit_end_date_edt.setText("");
                            Toast.makeText(context, context.getString(R.string.filter_date_alter), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        endDate = "";
                        patient_registration_end_date_edt.setText("");
                        patient_register_visit_end_date_edt.setText("");
                        Toast.makeText(context, "End date should greater than start date", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        };

        radio_button_by_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patient_registration_mrno_edt.setVisibility(View.GONE);
                patient_registration_firstname_edt.setVisibility(View.GONE);
                patient_registration_lastname_edt.setVisibility(View.GONE);
                patient_registration_mobile_no_edt.setVisibility(View.GONE);
                layout_search_by_reg_date_view.setVisibility(View.GONE);
                layout_search_by_visit_date_view.setVisibility(View.GONE);
                patient_register_layout_category_L3.setVisibility(View.VISIBLE);
                radio_group_1.clearCheck();
                radio_group_2.clearCheck();

                filter_text_1.setVisibility(View.VISIBLE);
                filter_text_2.setVisibility(View.GONE);
                filter_text_1.setText("Select Patient Category L3");
                checkRadio = 6;
            }
        });

        patient_report_date_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidateDate()) {
                    dialog.dismiss();
                    GetPatientListWebcall();
                }
            }
        });
    }

    private boolean ValidateDate() {
        mrNO = patient_registration_mrno_edt.getText().toString().trim();
        firstName = patient_registration_firstname_edt.getText().toString().trim();
        lastName = patient_registration_lastname_edt.getText().toString().trim();
        mobileNo = patient_registration_mobile_no_edt.getText().toString().trim();

        if (checkRadio == 0) {
            if (mrNO.equals("") || mrNO.length() == 0) {
                Toast.makeText(context, "Please enter MRNo", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (checkRadio == 1) {
            if ((firstName.equals("") || firstName.length() == 0) || (lastName.equals("") || lastName.length() == 0)) {
                Toast.makeText(context, "Please enter first and last name", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (checkRadio == 2) {
            if (lastName.equals("") || lastName.length() == 0) {
                Toast.makeText(context, "Please enter last name", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (checkRadio == 3) {
            if (mobileNo.equals("") || mobileNo.length() == 0) {
                Toast.makeText(context, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                return false;
            } else if (mobileNo.length() > 0 && mobileNo.length() < 10) {
                Toast.makeText(context, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (checkRadio == 4 || checkRadio == 5) {
            if (startDate == null || startDate.equals("") || startDate.length() == 0) {
                Toast.makeText(context, "Please select start date...", Toast.LENGTH_SHORT).show();
                return false;
            } else if (endDate.equals("") || endDate.length() == 0) {
                Toast.makeText(context, "Please select end date...", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (checkRadio == 6) {
            if (selectedID == null || selectedID.equals("") || selectedID.length() == 0) {
                Toast.makeText(context, "Please select category...", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void GetPatientListWebcall() {
        doctorProfileList = doctorProfileAdapter.listAll();
        if (localSetting.isNetworkAvailable(context)) {
            new GetPatientListTask().execute();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
        }
    }

    private class GetPatientListTask extends AsyncTask<Void, Void, String> {
        private TransparentProgressDialog progressDialog;
        private JsonObjectMapper jsonObjectMapper;
        private WebServiceConsumer webServiceConsumer;
        private int responseCode = 0;
        private String jSonData = "";
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
                ELFilter elFilter = new ELFilter();
                elFilter.setUnitID(doctorProfileList.get(0).getUnitID());
                elFilter.setSelectedDoctorID(doctorProfileList.get(0).getDoctorID());
                elFilter.setMRNo(mrNO);
                elFilter.setFirstName(firstName);
                elFilter.setLastName(lastName);
                elFilter.setMobileNo(mobileNo);
                elFilter.setRegStartDate(startDate);
                elFilter.setRegEndDate(endDate);
                elFilter.setVisitStartDate(startDate);
                elFilter.setVisitEndDate(endDate);
                elFilter.setFilterFlag(String.valueOf(checkRadio));

                jSonData = jsonObjectMapper.unMap(elFilter);
                response = webServiceConsumer.POST(Constants.PATIENT_LIST_URL, jSonData);

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
            if (responseCode == Constants.HTTP_OK_200) {
                patientList = jsonObjectMapper.map(responseString, Patient.class);
                if (patientList != null && patientList.size() > 0) {
                    patientAdapterDB.delete(doctorProfileList.get(0).getUnitID());
                    for (int i = 0; i < patientList.size(); i++) {
                        patientAdapterDB.create(patientList.get(i));
                    }
                } else {
                    search_patient_empty.setVisibility(View.VISIBLE);
                    search_patient_List.setVisibility(View.GONE);
                }
            } else if (responseCode == Constants.HTTP_NO_RECORD_FOUND_OK_204) {
                patientAdapterDB.delete(doctorProfileList.get(0).getUnitID());
                search_patient_empty.setVisibility(View.VISIBLE);
                search_patient_List.setVisibility(View.GONE);
                localSetting.showErrorAlert(context, context.getResources().getString(R.string.opps_alert), localSetting.handleError(responseCode));
            } else {
                localSetting.showErrorAlert(context, context.getResources().getString(R.string.opps_alert), localSetting.handleError(responseCode));
            }
            localSetting.hideDialog(progressDialog);
            RefreshPatientList();
            super.onPostExecute(result);
        }
    }
}
