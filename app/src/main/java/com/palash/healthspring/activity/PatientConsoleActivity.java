package com.palash.healthspring.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.palash.healthspring.R;
import com.palash.healthspring.adapter.PatientConsoleListAdapter;
import com.palash.healthspring.adapter.SpinnerAdapter;
import com.palash.healthspring.api.JsonObjectMapper;
import com.palash.healthspring.api.WebServiceConsumer;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.BookAppointment;
import com.palash.healthspring.entity.DoctorProfile;
import com.palash.healthspring.entity.ELUnitMaster;
import com.palash.healthspring.entity.PatientConsole;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;
import com.palash.healthspring.utilities.TransparentProgressDialog;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PatientConsoleActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.UnitMasterAdapter unitMasterAdapter;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapter;

    private SpinnerAdapter.UnitMasterListAdapter unitMasterListAdapter;
    private PatientConsoleListAdapter patientConsoleListAdapter;

    private DoctorProfile doctorProfile;
    private ELUnitMaster elUnitMaster;
    private BookAppointment bookAppointment;

    private ArrayList<DoctorProfile> listProfile;
    private ArrayList<ELUnitMaster> listELUnitMaster;
    private ArrayList<ELUnitMaster> listELUnitMasterNew;
    private ArrayList<BookAppointment> bookAppointmentArrayList;

    private ImageView patient_console_img_gender;
    private TextView patient_console_tv_name;
    private TextView patient_console_tv_age;
    private TextView patient_console_tv_mrno;
    private TextView patient_console_tv_maried_status;
    private TextView patient_console_tv_contact_no;
    private TextView patient_console_tv_email;
    private TextView patient_console_tv_regdate;
    private TextView patient_console_tv_clinic;
    private TextView patient_console_search_tv_visit_fmdate;
    private TextView patient_console_search_tv_visit_todate;
    private Spinner all_clinic_spinner;
    private TextView patient_console_list_search_bnt;
    private TextView patient_console_tv_no_data;
    private ListView patient_console_list;

    private String selectedClinicID;
    private String DoctorID = "0";
    private String FromDate = "";
    private String ToDate = "";

    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat format = new SimpleDateFormat(Constants.PATIENT_QUEUE_DATE);
    private DatePickerDialog.OnDateSetListener fromDatepickerdialog;
    private DatePickerDialog.OnDateSetListener toDatepickerdialog;

    private SimpleDateFormat dates = new SimpleDateFormat(Constants.SEARCH_DATE_FORMAT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_console);
        InitSetting();
        InitView();
        setdata();
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
            unitMasterAdapter = databaseAdapter.new UnitMasterAdapter();
            bookAppointmentAdapter = databaseAdapter.new BookAppointmentAdapter();

            listProfile = doctorProfileAdapter.listAll();
            listELUnitMaster = unitMasterAdapter.listAll();
            bookAppointmentArrayList = bookAppointmentAdapter.listAll();

            doctorProfile = new DoctorProfile();
            elUnitMaster = new ELUnitMaster();
            bookAppointment = new BookAppointment();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView() {
        patient_console_img_gender = (ImageView) findViewById(R.id.patient_console_img_gender);
        patient_console_tv_name = (TextView) findViewById(R.id.patient_console_tv_name);
        patient_console_tv_age = (TextView) findViewById(R.id.patient_console_tv_age);
        patient_console_tv_mrno = (TextView) findViewById(R.id.patient_console_tv_mrno);
        patient_console_tv_maried_status = (TextView) findViewById(R.id.patient_console_tv_maried_status);
        patient_console_tv_contact_no = (TextView) findViewById(R.id.patient_console_tv_contact_no);
        patient_console_tv_email = (TextView) findViewById(R.id.patient_console_tv_email);
        patient_console_tv_regdate = (TextView) findViewById(R.id.patient_console_tv_regdate);
        patient_console_tv_clinic = (TextView) findViewById(R.id.patient_console_tv_clinic);
        patient_console_search_tv_visit_fmdate = (TextView) findViewById(R.id.patient_console_search_tv_visit_fmdate);
        patient_console_search_tv_visit_todate = (TextView) findViewById(R.id.patient_console_search_tv_visit_todate);
        patient_console_list_search_bnt = (TextView) findViewById(R.id.patient_console_list_search_bnt);
        patient_console_tv_no_data = (TextView) findViewById(R.id.patient_console_tv_no_data);

        all_clinic_spinner = (Spinner) findViewById(R.id.all_clinic_spinner);
        patient_console_list = (ListView) findViewById(R.id.patient_console_list);

        patient_console_list_search_bnt.setOnClickListener(this);

        patient_console_search_tv_visit_fmdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToDate = "";
                //patient_console_search_tv_visit_todate.setText("");
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, fromDatepickerdialog,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
            }
        });

        fromDatepickerdialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                FromDate = localSetting.dateToString(day, month, year, Constants.SEARCH_DATE_FORMAT);
                ToDate = localSetting.dateToString(day, month, year, Constants.SEARCH_DATE_FORMAT);

                patient_console_search_tv_visit_fmdate.setText(localSetting.dateToString(day, month, year, Constants.PATIENT_QUEUE_DATE));
                patient_console_search_tv_visit_todate.setText(localSetting.dateToString(day, month, year, Constants.PATIENT_QUEUE_DATE));
            }
        };

        patient_console_search_tv_visit_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, toDatepickerdialog,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
            }
        });

        toDatepickerdialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                ToDate = localSetting.dateToString(day, month, year, Constants.SEARCH_DATE_FORMAT);
                try {
                    //Dates to compare
                    Date date1;
                    Date date2;

                    //Setting dates
                    date1 = dates.parse(FromDate);
                    date2 = dates.parse(ToDate);

                    //Comparing dates
                    long difference = Math.abs(date1.getTime() - date2.getTime());
                    long differenceDates = difference / (24 * 60 * 60 * 1000);

                    //Convert long to String
                    String dayDifference = Long.toString(differenceDates);

                    if (date1.before(date2) || date1.equals(date2)) {
                        if (Integer.parseInt(dayDifference) < 7) {
                            patient_console_search_tv_visit_todate.setText(localSetting.dateToString(day, month, year, Constants.PATIENT_QUEUE_DATE));
                        } else {
                            ToDate = "";
                            patient_console_search_tv_visit_todate.setText("");
                            Toast.makeText(context, "Date difference should be maximum 7 days", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        ToDate = "";
                        patient_console_search_tv_visit_todate.setText("");
                        Toast.makeText(context, "End date should greater than start date", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        };

    }

    private void setdata() {
        patient_console_search_tv_visit_fmdate.setText(format.format(new Date()));
        patient_console_search_tv_visit_todate.setText(format.format(new Date()));
        FromDate = localSetting.formatDate(patient_console_search_tv_visit_fmdate.getText().toString(), Constants.PATIENT_QUEUE_DATE, Constants.SEARCH_DATE_FORMAT);
        ToDate = localSetting.formatDate(patient_console_search_tv_visit_todate.getText().toString(), Constants.PATIENT_QUEUE_DATE, Constants.SEARCH_DATE_FORMAT);

        if (listProfile != null && listProfile.size() > 0) {
            doctorProfile = listProfile.get(0);
        }

        if (bookAppointmentArrayList != null && bookAppointmentArrayList.size() > 0) {
            bookAppointment = bookAppointmentArrayList.get(0);

            if (bookAppointment.getGenderID() != null && bookAppointment.getGenderID().trim().length() > 0) {
                if (bookAppointment.getGenderID().equals("1")) {
                    patient_console_img_gender.setImageResource(R.drawable.personmale);
                } else {
                    patient_console_img_gender.setImageResource(R.drawable.personfemale);
                }
            }

            String FirstName = bookAppointment.getFirstName();
            String MiddleName = bookAppointment.getMiddleName();
            String LastName = bookAppointment.getLastName();
            String Name;
            //if (MiddleName != null && MiddleName.length() > 0) {
            //    Name = FirstName + " " + MiddleName + " " + LastName;
            //} else {
            Name = FirstName + " " + LastName;
            //}
            patient_console_tv_name.setText(Name);

            if (bookAppointment.getMRNo() != null && bookAppointment.getMRNo().trim().length() > 0) {
                patient_console_tv_mrno.setText(bookAppointment.getMRNo());
            } else {
                patient_console_tv_mrno.setText("-");
            }

            if (bookAppointment.getAge() != null && bookAppointment.getAge().trim().length() > 0) {
                patient_console_tv_age.setText(bookAppointment.getAge());
            } else {
                patient_console_tv_age.setText("-");
            }

            if (bookAppointment.getMaritalStatus() != null && bookAppointment.getMaritalStatus().trim().length() > 0) {
                patient_console_tv_maried_status.setText(bookAppointment.getMaritalStatus());
            } else {
                patient_console_tv_maried_status.setText("-");
            }

            if (bookAppointment.getContact1() != null && bookAppointment.getContact1().trim().length() > 0) {
                patient_console_tv_contact_no.setText(bookAppointment.getContact1());
            } else {
                patient_console_tv_contact_no.setText("-");
            }

            if (bookAppointment.getEmailId() != null && bookAppointment.getEmailId().trim().length() > 0) {
                patient_console_tv_email.setText(bookAppointment.getEmailId());
            } else {
                patient_console_tv_email.setText("-");
            }

            if (bookAppointment.getRegistrationDate() != null && bookAppointment.getRegistrationDate().trim().length() > 0) {
                patient_console_tv_regdate.setText(localSetting.formatDate(bookAppointment.getRegistrationDate(), "dd-mm-yyyy HH:mm:ss", Constants.OFFLINE_DATE));
            } else {
                patient_console_tv_regdate.setText("-");
            }

            if (bookAppointment.getClinicName() != null && bookAppointment.getClinicName().trim().length() > 0) {
                patient_console_tv_clinic.setText(bookAppointment.getClinicName());
            } else {
                patient_console_tv_clinic.setText("-");
            }
        }

        if (listELUnitMaster != null && listELUnitMaster.size() > 0) {
            listELUnitMasterNew = new ArrayList<>();
            elUnitMaster.setUnitID("0");
            elUnitMaster.setUnitDesc("All Clinic");
            listELUnitMasterNew.add(elUnitMaster);
            for (int i = 0; i < listELUnitMaster.size(); i++) {
                listELUnitMasterNew.add(listELUnitMaster.get(i));
            }

            all_clinic_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedClinicID = listELUnitMasterNew.get(all_clinic_spinner.getSelectedItemPosition()).getUnitID();
                    Log.e("selectedClinicID: ", "" + selectedClinicID);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            unitMasterListAdapter = new SpinnerAdapter.UnitMasterListAdapter(context, listELUnitMasterNew);
            all_clinic_spinner.setAdapter(unitMasterListAdapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.patient_console_list_search_bnt:
                if (ValidateDate())
                    if (localSetting.isNetworkAvailable(context)) {
                        new GetPatientConsoleListTask().execute();
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
                    }
                break;
        }
    }

    private Boolean ValidateDate() {
        if (FromDate.equals("") || FromDate.length() == 0) {
            Toast.makeText(context, "Please select start date...", Toast.LENGTH_SHORT).show();
            return false;
        } else if (ToDate.equals("") || ToDate.length() == 0) {
            Toast.makeText(context, "Please select end date...", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private class GetPatientConsoleListTask extends AsyncTask<Void, Void, String> {
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
                webServiceConsumer = new WebServiceConsumer(context, null, null);
                response = webServiceConsumer.GET(Constants.GET_PATIENT_CONSOLE_LIST_URL + doctorProfile.getUnitID() + "&PatientID=" + bookAppointment.getPatientID()
                        + "&PatientUnitID=" + bookAppointment.getUnitID() + "&DoctorID=" + DoctorID + "&VisitedUnitID=" + selectedClinicID + "&FromDate="
                        + FromDate + "&ToDate=" + ToDate);
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
                ArrayList<PatientConsole> patientConsoleList = jsonObjectMapper.map(responseString, PatientConsole.class);
                if (patientConsoleList != null && patientConsoleList.size() > 0) {
                    patient_console_list.setVisibility(View.VISIBLE);
                    patient_console_tv_no_data.setVisibility(View.GONE);
                    patientConsoleListAdapter = new PatientConsoleListAdapter(context, patientConsoleList);
                    patient_console_list.setAdapter(patientConsoleListAdapter);
                    patientConsoleListAdapter.notifyDataSetChanged();
                }
            } else {
                patient_console_list.setVisibility(View.GONE);
                patient_console_tv_no_data.setVisibility(View.VISIBLE);
                Toast.makeText(context, localSetting.handleError(responseCode), Toast.LENGTH_SHORT).show();
            }
            localSetting.hideDialog(progressDialog);
            super.onPostExecute(result);
        }
    }
}
