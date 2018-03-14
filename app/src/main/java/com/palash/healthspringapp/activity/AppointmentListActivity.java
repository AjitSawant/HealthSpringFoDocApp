package com.palash.healthspringapp.activity;

import android.animation.LayoutTransition;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.palash.healthspringapp.R;
import com.palash.healthspringapp.adapter.AppointmentExpandListAdapter;
import com.palash.healthspringapp.adapter.SpinnerAdapter;
import com.palash.healthspringapp.api.JsonObjectMapper;
import com.palash.healthspringapp.api.WebServiceConsumer;
import com.palash.healthspringapp.database.DatabaseAdapter;
import com.palash.healthspringapp.database.DatabaseContract;
import com.palash.healthspringapp.entity.Appointment;
import com.palash.healthspringapp.entity.Department;
import com.palash.healthspringapp.entity.DoctorProfile;
import com.palash.healthspringapp.entity.ELAppointmentStatus;
import com.palash.healthspringapp.entity.ELFilter;
import com.palash.healthspringapp.entity.Flag;
import com.palash.healthspringapp.utilities.Constants;
import com.palash.healthspringapp.utilities.LocalSetting;
import com.palash.healthspringapp.utilities.TransparentProgressDialog;
import com.palash.healthspringapp.utilities.Validate;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import fr.ganfra.materialspinner.MaterialSpinner;

public class AppointmentListActivity extends AppCompatActivity {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.AppointmentAdapter appointmentAdapterDB;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.DepartmentAdapter departmentAdapterBD;

    private Appointment elAppointment = null;
    private HashMap<String, ArrayList<Appointment>> listDataChild;
    private ArrayList<Appointment> elAppointmentArrayList = null;
    private ArrayList<DoctorProfile> doctorProfileList;
    private ArrayList<ELAppointmentStatus> elAppointmentStatusArrayList = null;
    private ArrayList<Department> departmentslist;

    //private View parentView;
    private ExpandableListView appointment_List;
    private TextView appointment_empty;
    private Chronometer appointment_chronometer;

    private AppointmentExpandListAdapter appointmentExpListAdapter;
    private SpinnerAdapter.AppointmentStatusAdapter appointmentStatusAdapter;
    private SpinnerAdapter.DepartmentAdapter departmentAdapter;

    private DialogPlus dialog;
    private RadioGroup radio_group_1;
    private RadioGroup radio_group_2;

    private RadioButton radio_button_by_mrno;
    private RadioButton radio_button_by_fname;
    private RadioButton radio_button_by_lname;
    private RadioButton radio_button_by_appointment_date;
    private RadioButton radio_button_by_dept;
    private RadioButton radio_button_by_status;

    private TextView filter_text_1;
    private TextView filter_text_2;

    private EditText appointment_mrno_edt;
    private EditText appointment_firstname_edt;
    private EditText appointment_lastname_edt;
    private TextView appointment_start_date_edt;
    private TextView appointment_end_date_edt;
    private MaterialSpinner appointment_dept;
    private MaterialSpinner appointment_status;

    private LinearLayout layout_search_by_appointmnet_date_view;
    private LinearLayout appointment_layout_dept;
    private LinearLayout appointment_layout_status;
    private Button patient_report_date_search_btn;

    private int checkRadio = 0;
    private static String mrNO = "";
    private static String firstName = "";
    private static String lastName = "";
    private static String startDate = "";
    private static String endDate = "";
    private static String SelectedDeptID = "";
    private static String SelectedDoctorID = "";
    private static String SelectedAppointmentStatus = "";

    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat format = new SimpleDateFormat(Constants.PATIENT_QUEUE_DATE);
    private SimpleDateFormat dates = new SimpleDateFormat(Constants.SEARCH_DATE_FORMAT);
    private DatePickerDialog.OnDateSetListener dateListenerFrom;
    private DatePickerDialog.OnDateSetListener dateListenerTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        InitSetting();
        InitView();
        //showFilterDialog();
        setAppointmentList();
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
            appointmentAdapterDB = databaseAdapter.new AppointmentAdapter();
            doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();
            departmentAdapterBD = databaseAdapter.new DepartmentAdapter();
            doctorProfileList = doctorProfileAdapter.listAll();

            if (doctorProfileList != null && doctorProfileList.size() > 0) {
                departmentslist = departmentAdapterBD.listAll(doctorProfileList.get(0).getUnitID());
            }
            elAppointment = new Appointment();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView() {
        try {
            appointment_List = (ExpandableListView) findViewById(R.id.appointment_List);
            appointment_empty = (TextView) findViewById(R.id.appointment_empty);
            appointment_chronometer = (Chronometer) findViewById(R.id.appointment_chronometer);

            appointment_chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    //searchLoadList();
                }
            });

            /*appointment_List.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                int previousGroup = -1;

                @Override
                public void onGroupExpand(int groupPosition) {
                    appointment_List.smoothScrollToPosition(groupPosition);
                    if (groupPosition != previousGroup)
                        appointment_List.collapseGroup(previousGroup);
                    previousGroup = groupPosition;
                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        appointment_chronometer.setBase(SystemClock.elapsedRealtime());
        appointment_chronometer.start();
        /*if (localSetting.isNetworkAvailable(context)) {
            new GetAppointmentListTask().execute();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
        }*/
        super.onResume();
    }

    @Override
    public void onPause() {
        appointment_chronometer.stop();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.menu_toolbar_filter).setVisible(true);
        menu.findItem(R.id.menu_toolbar_book).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toolbar_filter:
                showFilterDialog();
                return true;
            case R.id.menu_toolbar_book:
                Constants.refreshPatient = true;
                startActivity(new Intent(context, PatientListActivity.class));
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
                .setContentHolder(new ViewHolder(R.layout.filter_appointment_list))
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

        radio_button_by_mrno = (RadioButton) view.findViewById(R.id.radio_button_by_mrno);
        radio_button_by_fname = (RadioButton) view.findViewById(R.id.radio_button_by_fname);
        radio_button_by_lname = (RadioButton) view.findViewById(R.id.radio_button_by_lname);
        radio_button_by_appointment_date = (RadioButton) view.findViewById(R.id.radio_button_by_appointment_date);
        radio_button_by_dept = (RadioButton) view.findViewById(R.id.radio_button_by_dept);
        radio_button_by_status = (RadioButton) view.findViewById(R.id.radio_button_by_status);

        filter_text_1 = (TextView) view.findViewById(R.id.filter_text_1);
        filter_text_2 = (TextView) view.findViewById(R.id.filter_text_2);

        appointment_mrno_edt = (EditText) view.findViewById(R.id.appointment_mrno_edt);
        appointment_firstname_edt = (EditText) view.findViewById(R.id.appointment_firstname_edt);
        appointment_lastname_edt = (EditText) view.findViewById(R.id.appointment_lastname_edt);
        appointment_start_date_edt = (TextView) view.findViewById(R.id.appointment_start_date_edt);
        appointment_end_date_edt = (TextView) view.findViewById(R.id.appointment_end_date_edt);
        appointment_dept = (MaterialSpinner) view.findViewById(R.id.appointment_dept);
        appointment_status = (MaterialSpinner) view.findViewById(R.id.appointment_status);

        layout_search_by_appointmnet_date_view = (LinearLayout) view.findViewById(R.id.layout_search_by_appointmnet_date_view);
        appointment_layout_dept = (LinearLayout) view.findViewById(R.id.appointment_layout_dept);
        appointment_layout_status = (LinearLayout) view.findViewById(R.id.appointment_layout_status);

        patient_report_date_search_btn = (Button) view.findViewById(R.id.patient_report_date_search_btn);

        appointment_mrno_edt.setVisibility(View.VISIBLE);
        appointment_firstname_edt.setVisibility(View.GONE);
        appointment_lastname_edt.setVisibility(View.GONE);
        layout_search_by_appointmnet_date_view.setVisibility(View.GONE);
        appointment_layout_dept.setVisibility(View.GONE);
        appointment_layout_status.setVisibility(View.GONE);
        radio_group_2.clearCheck();

        filter_text_1.setVisibility(View.VISIBLE);
        filter_text_2.setVisibility(View.GONE);
        filter_text_1.setText("MRNo");
        appointment_mrno_edt.setText("");
        checkRadio = 0;

        radio_button_by_mrno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appointment_mrno_edt.setVisibility(View.VISIBLE);
                appointment_firstname_edt.setVisibility(View.GONE);
                appointment_lastname_edt.setVisibility(View.GONE);
                layout_search_by_appointmnet_date_view.setVisibility(View.GONE);
                appointment_layout_dept.setVisibility(View.GONE);
                appointment_layout_status.setVisibility(View.GONE);
                radio_group_2.clearCheck();

                filter_text_1.setVisibility(View.VISIBLE);
                filter_text_2.setVisibility(View.GONE);
                filter_text_1.setText("MRNo");
                checkRadio = 0;
            }
        });

        radio_button_by_fname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appointment_mrno_edt.setVisibility(View.GONE);
                appointment_firstname_edt.setVisibility(View.VISIBLE);
                appointment_lastname_edt.setVisibility(View.GONE);
                layout_search_by_appointmnet_date_view.setVisibility(View.GONE);
                appointment_layout_dept.setVisibility(View.GONE);
                appointment_layout_status.setVisibility(View.GONE);
                radio_group_2.clearCheck();

                filter_text_1.setVisibility(View.VISIBLE);
                filter_text_2.setVisibility(View.GONE);
                filter_text_1.setText("First Name");
                checkRadio = 1;
            }
        });

        radio_button_by_lname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appointment_mrno_edt.setVisibility(View.GONE);
                appointment_firstname_edt.setVisibility(View.GONE);
                appointment_lastname_edt.setVisibility(View.VISIBLE);
                layout_search_by_appointmnet_date_view.setVisibility(View.GONE);
                appointment_layout_dept.setVisibility(View.GONE);
                appointment_layout_status.setVisibility(View.GONE);
                radio_group_2.clearCheck();

                filter_text_1.setVisibility(View.VISIBLE);
                filter_text_2.setVisibility(View.GONE);
                filter_text_1.setText("Last Name");
                checkRadio = 2;
            }
        });

        radio_button_by_appointment_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appointment_mrno_edt.setVisibility(View.GONE);
                appointment_firstname_edt.setVisibility(View.GONE);
                appointment_lastname_edt.setVisibility(View.GONE);
                layout_search_by_appointmnet_date_view.setVisibility(View.VISIBLE);
                appointment_layout_dept.setVisibility(View.GONE);
                appointment_layout_status.setVisibility(View.GONE);
                radio_group_1.clearCheck();

                filter_text_1.setVisibility(View.VISIBLE);
                filter_text_2.setVisibility(View.VISIBLE);
                filter_text_1.setText("Appointment start date");
                filter_text_2.setText("Appointment end date");
                checkRadio = 3;

                appointment_start_date_edt.setText(format.format(new Date()));
                appointment_end_date_edt.setText(format.format(new Date()));
                startDate = localSetting.formatDate(appointment_start_date_edt.getText().toString(), Constants.PATIENT_QUEUE_DATE, Constants.SEARCH_DATE_FORMAT);
                endDate = localSetting.formatDate(appointment_end_date_edt.getText().toString(), Constants.PATIENT_QUEUE_DATE, Constants.SEARCH_DATE_FORMAT);
            }
        });

        appointment_start_date_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(context, dateListenerFrom, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                endDate = "";
            }
        });

        appointment_end_date_edt.setOnClickListener(new View.OnClickListener() {
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

        appointment_start_date_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(context, dateListenerFrom, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                endDate = "";
            }
        });

        appointment_end_date_edt.setOnClickListener(new View.OnClickListener() {
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
                appointment_start_date_edt.setText(localSetting.dateToString(day, month, year, Constants.PATIENT_QUEUE_DATE));
                appointment_end_date_edt.setText(localSetting.dateToString(day, month, year, Constants.PATIENT_QUEUE_DATE));

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
                            appointment_end_date_edt.setText(localSetting.dateToString(day, month, year, Constants.PATIENT_QUEUE_DATE));
                        } else {
                            endDate = "";
                            appointment_end_date_edt.setText("");
                            Toast.makeText(context, context.getString(R.string.filter_date_alter), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        endDate = "";
                        appointment_end_date_edt.setText("");
                        Toast.makeText(context, "End date should greater than start date", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        };

        radio_button_by_dept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appointment_mrno_edt.setVisibility(View.GONE);
                appointment_firstname_edt.setVisibility(View.GONE);
                appointment_lastname_edt.setVisibility(View.GONE);
                layout_search_by_appointmnet_date_view.setVisibility(View.GONE);
                appointment_layout_dept.setVisibility(View.VISIBLE);
                appointment_layout_status.setVisibility(View.GONE);
                radio_group_1.clearCheck();

                filter_text_1.setVisibility(View.VISIBLE);
                filter_text_2.setVisibility(View.GONE);
                filter_text_1.setText("Select Department");
                checkRadio = 4;

                setDepartmentData();
            }
        });

        radio_button_by_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appointment_mrno_edt.setVisibility(View.GONE);
                appointment_firstname_edt.setVisibility(View.GONE);
                appointment_lastname_edt.setVisibility(View.GONE);
                layout_search_by_appointmnet_date_view.setVisibility(View.GONE);
                appointment_layout_dept.setVisibility(View.GONE);
                appointment_layout_status.setVisibility(View.VISIBLE);
                radio_group_1.clearCheck();

                filter_text_1.setVisibility(View.VISIBLE);
                filter_text_2.setVisibility(View.GONE);
                filter_text_1.setText("Select Appointment Status");
                checkRadio = 5;

                setFilterAppointmentStatusData();
            }
        });

        patient_report_date_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidateDate()) {
                    dialog.dismiss();
                    GetAppointmentWebcall();
                }
            }
        });
    }

    private void setFilterAppointmentStatusData() {
        elAppointmentStatusArrayList = localSetting.returnAppointmentStatus();
        if (elAppointmentStatusArrayList != null && elAppointmentStatusArrayList.size() > 0) {
            appointmentStatusAdapter = new SpinnerAdapter.AppointmentStatusAdapter(context, elAppointmentStatusArrayList);
            appointment_status.setAdapter(appointmentStatusAdapter);
            appointmentStatusAdapter.notifyDataSetChanged();
        }
    }

    private void setDepartmentData() {
        if (departmentslist != null && departmentslist.size() > 0) {
            departmentAdapter = new SpinnerAdapter.DepartmentAdapter(context, departmentslist);
            appointment_dept.setAdapter(departmentAdapter);
            departmentAdapter.notifyDataSetChanged();
        }
    }

    private boolean ValidateDate() {
        mrNO = appointment_mrno_edt.getText().toString().trim();
        firstName = appointment_firstname_edt.getText().toString().trim();
        lastName = appointment_lastname_edt.getText().toString().trim();

        if (checkRadio == 0) {
            if (mrNO.equals("") || mrNO.length() == 0) {
                Toast.makeText(context, "Please enter MRNo", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (checkRadio == 1) {
            if (firstName.equals("") || firstName.length() == 0) {
                Toast.makeText(context, "Please enter first name", Toast.LENGTH_SHORT).show();
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
            if (startDate == null || startDate.equals("") || startDate.length() == 0) {
                Toast.makeText(context, "Please select start date...", Toast.LENGTH_SHORT).show();
                return false;
            } else if (endDate.equals("") || endDate.length() == 0) {
                Toast.makeText(context, "Please select end date...", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (checkRadio == 4) {
            if (departmentslist != null && departmentslist.size() > 0) {
                SelectedDeptID = departmentslist.get(appointment_dept.getSelectedItemPosition()).getID();
            }
            if (SelectedDeptID == null || SelectedDeptID.equals("") || SelectedDeptID.length() == 0) {
                Toast.makeText(context, "Please select department...", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (checkRadio == 5) {
            if (elAppointmentStatusArrayList != null && elAppointmentStatusArrayList.size() > 0) {
                SelectedAppointmentStatus = elAppointmentStatusArrayList.get(appointment_status.getSelectedItemPosition()).getID();
            }
            if (SelectedAppointmentStatus == null || SelectedAppointmentStatus.equals("") || SelectedAppointmentStatus.length() == 0) {
                Toast.makeText(context, "Please select status...", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

   /* private void searchLoadList() {
        try {
            doctorProfileList = doctorProfileAdapter.listAll();
            *//*SimpleDateFormat date_format = new SimpleDateFormat(Constants.SEARCH_DATE_FORMAT);
            String patientName = appointment_edt_patient_name.getText().toString();
            FromDate = date_format.format(format.parse(appointment_edt_fromdate.getText().toString()));
            ToDate = date_format.format(format.parse(appointment_edt_todate.getText().toString()));
            if (isSearchPanelVisible) {
                ToDate = date_format.format(format.parse(appointment_edt_todate.getText().toString()));
            } else {
                ToDate = null;
            }*//*
            switch (Validate.validateSearchDate(date_format.format(format.parse(appointment_edt_fromdate.getText().toString())), date_format.format(format.parse(appointment_edt_todate.getText().toString())))) {
                case 1:
                    currentCountheader = appointmentAdapterDB.HeaderCount(null, FromDate, ToDate);
                    if (headerList != null) {
                        headerlistCount = headerList.size();
                    }
                    if (currentCountheader != headerlistCount) {
                        PrepareListData(patientName, FromDate, ToDate);
                    } else {
                        if (headerList != null && headerList.size() > 0) {
                            for (int i = 0; i < headerList.size(); i++) {
                                currentCountchild = appointmentAdapterDB.listAllCount(headerList.get(i), patientName);
                            }
                            if (elAppointmentArrayList != null) {
                                childlistCount = elAppointmentArrayList.size();
                            }
                            if (currentCountchild != childlistCount) {
                                PrepareListData(patientName, FromDate, ToDate);
                            }
                        } else {
                            appointment_List.setVisibility(View.GONE);
                            appointment_empty.setVisibility(View.VISIBLE);
                            appointment_empty.setText("No Record Found");
                            appointment_empty.setTextColor(Color.GRAY);
                        }
                    }
                    break;
                case 2:
                    appointment_List.setVisibility(View.GONE);
                    appointment_empty.setVisibility(View.VISIBLE);
                    appointment_empty.setText("Please check date format is " + Constants.SEARCH_DATE_FORMAT);
                    appointment_empty.setTextColor(Color.RED);
                    break;
                case 3:
                    appointment_List.setVisibility(View.GONE);
                    appointment_empty.setVisibility(View.VISIBLE);
                    appointment_empty.setText("From Date should be less than To Date");
                    appointment_empty.setTextColor(Color.RED);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

   /* private void PrepareListData() {
        try {
            headerList = appointmentAdapterDB.listHeaderWithUnit(doctorProfileList.get(0).getUnitID());
            if (headerList != null && headerList.size() > 0) {
                listDataChild = new HashMap<String, ArrayList<Appointment>>();
                for (int i = 0; i < headerList.size(); i++) {
                    elAppointmentArrayList = appointmentAdapterDB.listAll(headerList.get(i), null);
                    if (elAppointmentArrayList != null && elAppointmentArrayList.size() > 0) {
                        listDataChild.put(headerList.get(i), elAppointmentArrayList);
                    }
                }
                appointmentListAdapter = new AppointmentExpandListAdapter(context, headerList, listDataChild);
                appointment_List.setAdapter(appointmentListAdapter);
                appointmentListAdapter.notifyDataSetChanged();
                appointment_List.expandGroup(0);
                appointment_List.setVisibility(View.VISIBLE);
                //layout_search_by_patient_name.setVisibility(View.VISIBLE);
                appointment_empty.setVisibility(View.GONE);
            } else {
                appointment_List.setVisibility(View.GONE);
                //layout_search_by_patient_name.setVisibility(View.GONE);
                appointment_empty.setVisibility(View.VISIBLE);
                appointment_empty.setText("No Record Found");
                appointment_empty.setTextColor(Color.GRAY);
                //animatePanel(2);
                // isSearchPanelVisible = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void setAppointmentList() {
        if (doctorProfileList != null && doctorProfileList.size() > 0) {
            elAppointmentArrayList = appointmentAdapterDB.listAllAppointmentWithUnit(doctorProfileList.get(0).getUnitID());
            ArrayList<String> mListDates = new ArrayList<>();
            if (elAppointmentArrayList != null && elAppointmentArrayList.size() > 0) {
                for (int i = 0; i < elAppointmentArrayList.size(); i++) {
                    elAppointment = elAppointmentArrayList.get(i);
                    String newID = elAppointment.getAppointmentDate();
                    if (mListDates.size() > 0) {
                        Boolean flagMatch = false;
                        for (int j = 0; j < mListDates.size(); j++) {
                            String checkID = mListDates.get(j);
                            if (checkID.equals(newID)) {
                                flagMatch = true;
                            }
                        }
                        if (flagMatch == false) {
                            mListDates.add(elAppointment.getAppointmentDate());
                        }
                    } else {
                        mListDates.add(elAppointment.getAppointmentDate());
                    }
                }

                if (mListDates.size() > 0) {
                    HashMap<String, ArrayList<ArrayList<Appointment>>> elAppointmentMainHashMapList = new HashMap<>();
                    ArrayList<ArrayList<Appointment>> elAppointmentChildList = new ArrayList<>();
                    ArrayList<String> appointmentChildList = new ArrayList<>();
                    for (int k = 0; k < mListDates.size(); k++) {
                        ArrayList<Appointment> elPathologiesList = appointmentAdapterDB.listAllWithDate(mListDates.get(k));
                        if (elPathologiesList.size() > 0) {
                            Appointment elAppointment1 = elPathologiesList.get(0);
                            elAppointmentChildList = new ArrayList<>();
                            elAppointmentChildList.add(elPathologiesList);
                            String key = String.valueOf(k) + ",.," + elAppointment1.getAppointmentDate();
                            elAppointmentMainHashMapList.put(key, elAppointmentChildList);
                            appointmentChildList.add(key);
                        }
                    }

                    if (elAppointmentMainHashMapList != null && elAppointmentMainHashMapList.size() > 0) {
                        appointmentExpListAdapter = new AppointmentExpandListAdapter(context, elAppointmentMainHashMapList, appointmentChildList);
                        appointment_List.setAdapter(appointmentExpListAdapter);
                        appointmentExpListAdapter.notifyDataSetChanged();
                        appointment_empty.setVisibility(View.GONE);
                        appointment_List.setVisibility(View.VISIBLE);
                    } else {
                        appointment_List.setVisibility(View.GONE);
                        appointment_empty.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                appointment_List.setVisibility(View.GONE);
                appointment_empty.setVisibility(View.VISIBLE);
            }
        }
    }

    private void GetAppointmentWebcall() {
        doctorProfileList = doctorProfileAdapter.listAll();
        if (localSetting.isNetworkAvailable(context)) {
            new GetAppointmentListTask().execute();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
        }
    }

    private class GetAppointmentListTask extends AsyncTask<Void, Void, String> {
        private TransparentProgressDialog progressDialog;
        private String jSon = "";
        private JsonObjectMapper jsonObjectMapper;
        private WebServiceConsumer webServiceConsumer;
        private int responseCode = 0;
        private String responseString = "";
        private String jSonData = "";
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
                if (doctorProfileList != null && doctorProfileList.size() > 0) {
                    jsonObjectMapper = new JsonObjectMapper();
                    webServiceConsumer = new WebServiceConsumer(context, null, null, null);
                    ELFilter elFilter = new ELFilter();
                    elFilter.setUnitID(doctorProfileList.get(0).getUnitID());
                    elFilter.setSelectedDoctorID(doctorProfileList.get(0).getDoctorID());
                    elFilter.setMRNo(mrNO);
                    elFilter.setFirstName(firstName);
                    elFilter.setLastName(lastName);
                    elFilter.setAppointmentStartDate(startDate);
                    elFilter.setAppointmentEndDate(endDate);
                    elFilter.setSelectedDeptID(SelectedDeptID);
                    elFilter.setSelectedAppointmentStatus(SelectedAppointmentStatus);
                    elFilter.setFilterFlag(String.valueOf(checkRadio));

                    jSonData = jsonObjectMapper.unMap(elFilter);
                    response = webServiceConsumer.POST(Constants.GET_APPOINTMENT_URL, jSonData);

                    if (response != null) {
                        responseCode = response.code();
                        responseString = response.body().string();
                        Log.d(Constants.TAG, "Response Code :" + responseCode);
                        Log.d(Constants.TAG, "Response String :" + responseString);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            if (responseCode == Constants.HTTP_OK_200) {
                ArrayList<Appointment> appointmentList = jsonObjectMapper.map(responseString, Appointment.class);
                if (appointmentList != null && appointmentList.size() > 0) {
                    appointmentAdapterDB.delete();
                    for (int index = 0; index < appointmentList.size(); index++) {
                        appointmentAdapterDB.create(appointmentList.get(index));
                    }
                }
            } else if (responseCode == Constants.HTTP_NO_RECORD_FOUND_OK_204) {
                appointmentAdapterDB.delete();
            } else {
                Toast.makeText(context, localSetting.handleError(responseCode), Toast.LENGTH_SHORT).show();
            }
            localSetting.hideDialog(progressDialog);
            setAppointmentList();
            super.onPostExecute(result);
        }
    }
}


