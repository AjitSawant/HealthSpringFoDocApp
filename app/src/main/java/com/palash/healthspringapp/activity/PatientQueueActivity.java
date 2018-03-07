package com.palash.healthspringapp.activity;

import android.animation.LayoutTransition;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
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
import com.palash.healthspringapp.adapter.PatientQueueAdapter;
import com.palash.healthspringapp.adapter.SpinnerAdapter;
import com.palash.healthspringapp.api.JsonObjectMapper;
import com.palash.healthspringapp.api.WebServiceConsumer;
import com.palash.healthspringapp.database.DatabaseAdapter;
import com.palash.healthspringapp.database.DatabaseContract;
import com.palash.healthspringapp.entity.Department;
import com.palash.healthspringapp.entity.DoctorProfile;
import com.palash.healthspringapp.entity.ELAppointmentStatus;
import com.palash.healthspringapp.entity.ELFilter;
import com.palash.healthspringapp.entity.ELVisitType;
import com.palash.healthspringapp.entity.PatientQueue;
import com.palash.healthspringapp.utilities.Constants;
import com.palash.healthspringapp.utilities.LocalSetting;
import com.palash.healthspringapp.utilities.TransparentProgressDialog;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fr.ganfra.materialspinner.MaterialSpinner;

public class PatientQueueActivity extends AppCompatActivity {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.PatientQueueAdapter patientQueueAdapterDB;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.DepartmentAdapter departmentAdapterBD;
    private DatabaseAdapter.VisitTypeMasterAdapter visitTypeMasterAdapterDB;

    private ArrayList<DoctorProfile> doctorProfileList;
    private ArrayList<PatientQueue> patientQueueArrayList = null;
    ArrayList<ELAppointmentStatus> elVisitStatusArrayList = null;
    private ArrayList<Department> departmentslist;
    private ArrayList<ELVisitType> elVisitTypeArrayList;

    private PatientQueueAdapter patientQueueAdapter;
    private SpinnerAdapter.DepartmentAdapter departmentAdapter;
    private SpinnerAdapter.AppointmentStatusAdapter visitStatusAdapter;
    private SpinnerAdapter.VisitTypeListAdapter visitTypeListAdapter;

    private Chronometer patient_queue_chronometer;
    private ListView patient_queue_list;
    private TextView patient_queue_empty;

    //private View parentView;
    private String patientName = null;
    private boolean isSearchPanelVisible = true;
    private int listCount = 0;
    private int currentCount = 0;

    private static String mrNO = "";
    private static String firstName = "";
    private static String lastName = "";
    private static String mobileNo = "";
    private static String SelectedDoctorID = "";
    private static String SelectedDeptID = "";
    private static String SelectedCategoryID = "";
    private static String SelectedVisitType = "";
    private static String SelectedVisitStatus = "";

    DialogPlus dialog;

    private RadioGroup radio_group_1;
    private RadioGroup radio_group_2;
    private RadioGroup radio_group_3;
    private RadioButton radio_button_by_mrno;
    private RadioButton radio_button_by_fname;
    private RadioButton radio_button_by_lname;
    private RadioButton radio_button_by_mobileNo;
    //private RadioButton radio_button_by_doctor;
    private RadioButton radio_button_by_dept;
    private RadioButton radio_button_by_category;
    private RadioButton radio_button_by_visit_type;
    private RadioButton radio_button_by_visit_status;

    private TextView filter_text_1;
    private TextView filter_text_2;

    private EditText patient_registration_mrno_edt;
    private EditText patient_registration_firstname_edt;
    private EditText patient_registration_lastname_edt;
    private EditText patient_registration_mobile_no_edt;
    //private MaterialSpinner patient_register_doctor;
    private MaterialSpinner patient_register_department;
    private MaterialSpinner patient_register_category_L3;
    private MaterialSpinner patient_register_visit_type;
    private MaterialSpinner patient_register_visit_status;

    //private LinearLayout layout_search_by_doctor;
    private LinearLayout layout_search_by_department;
    private LinearLayout patient_register_layout_category_L3;
    private LinearLayout layout_search_by_visit_type;
    private LinearLayout layout_search_by_visit_status;

    private Button patient_report_date_search_btn;

    private int checkRadio = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_queue);
        InitSetting();
        InitView();
        if (localSetting.isNetworkAvailable(context)) {
            new GetPatientQueueTask().execute();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
        }
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
            patientQueueAdapterDB = databaseAdapter.new PatientQueueAdapter();
            doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();
            departmentAdapterBD = databaseAdapter.new DepartmentAdapter();
            visitTypeMasterAdapterDB = databaseAdapter.new VisitTypeMasterAdapter();
            doctorProfileList = doctorProfileAdapter.listAll();
            departmentslist = departmentAdapterBD.listAll();
            elVisitTypeArrayList = visitTypeMasterAdapterDB.listAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView() {
        try {
            patient_queue_chronometer = (Chronometer) findViewById(R.id.patient_queue_chronometer);
            patient_queue_list = (ListView) findViewById(R.id.patient_queue_list);
            patient_queue_empty = (TextView) findViewById(R.id.patient_queue_empty);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void LoadList() {
        doctorProfileList = doctorProfileAdapter.listAll();
        if (patientName != null && patientName.equals("")) {
            patientName = null;
        }
        currentCount = patientQueueAdapterDB.CountToday(doctorProfileList.get(0).getUnitID(), null);
        if (patientQueueArrayList != null) {
            listCount = patientQueueArrayList.size();
        }
        if (currentCount != listCount) {
            RefreshList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.menu_toolbar_filter).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_toolbar_filter:
                showFilterDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        RefreshList();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void showFilterDialog() {
        dialog = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(R.layout.filter_layout_patient_queue))
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
        //radio_button_by_doctor = (RadioButton) view.findViewById(R.id.radio_button_by_doctor);
        radio_button_by_dept = (RadioButton) view.findViewById(R.id.radio_button_by_dept);
        radio_button_by_category = (RadioButton) view.findViewById(R.id.radio_button_by_category);
        radio_button_by_visit_type = (RadioButton) view.findViewById(R.id.radio_button_by_visit_type);
        radio_button_by_visit_status = (RadioButton) view.findViewById(R.id.radio_button_by_visit_status);

        filter_text_1 = (TextView) view.findViewById(R.id.filter_text_1);
        filter_text_2 = (TextView) view.findViewById(R.id.filter_text_2);

        patient_registration_mrno_edt = (EditText) view.findViewById(R.id.patient_registration_mrno_edt);
        patient_registration_firstname_edt = (EditText) view.findViewById(R.id.patient_registration_firstname_edt);
        patient_registration_lastname_edt = (EditText) view.findViewById(R.id.patient_registration_lastname_edt);
        patient_registration_mobile_no_edt = (EditText) view.findViewById(R.id.patient_registration_mobile_no_edt);
        //patient_register_doctor = (MaterialSpinner) view.findViewById(R.id.patient_register_doctor);
        patient_register_department = (MaterialSpinner) view.findViewById(R.id.patient_register_department);
        patient_register_category_L3 = (MaterialSpinner) view.findViewById(R.id.patient_register_category_L3);
        patient_register_visit_type = (MaterialSpinner) view.findViewById(R.id.patient_register_visit_type);
        patient_register_visit_status = (MaterialSpinner) view.findViewById(R.id.patient_register_visit_status);

        //layout_search_by_doctor = (LinearLayout) view.findViewById(R.id.layout_search_by_doctor);
        layout_search_by_department = (LinearLayout) view.findViewById(R.id.layout_search_by_department);
        patient_register_layout_category_L3 = (LinearLayout) view.findViewById(R.id.patient_register_layout_category_L3);
        layout_search_by_visit_type = (LinearLayout) view.findViewById(R.id.layout_search_by_visit_type);
        layout_search_by_visit_status = (LinearLayout) view.findViewById(R.id.layout_search_by_visit_status);

        patient_report_date_search_btn = (Button) view.findViewById(R.id.patient_report_date_search_btn);

        patient_registration_mrno_edt.setVisibility(View.VISIBLE);
        patient_registration_firstname_edt.setVisibility(View.GONE);
        patient_registration_lastname_edt.setVisibility(View.GONE);
        patient_registration_mobile_no_edt.setVisibility(View.GONE);
        //layout_search_by_doctor.setVisibility(View.GONE);
        layout_search_by_department.setVisibility(View.GONE);
        patient_register_layout_category_L3.setVisibility(View.GONE);
        layout_search_by_visit_type.setVisibility(View.GONE);
        layout_search_by_visit_status.setVisibility(View.GONE);
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
                patient_registration_firstname_edt.setVisibility(View.GONE);
                patient_registration_lastname_edt.setVisibility(View.GONE);
                patient_registration_mobile_no_edt.setVisibility(View.GONE);
                //layout_search_by_doctor.setVisibility(View.GONE);
                layout_search_by_department.setVisibility(View.GONE);
                patient_register_layout_category_L3.setVisibility(View.GONE);
                layout_search_by_visit_type.setVisibility(View.GONE);
                layout_search_by_visit_status.setVisibility(View.GONE);
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
                patient_registration_firstname_edt.setVisibility(View.VISIBLE);
                patient_registration_lastname_edt.setVisibility(View.GONE);
                patient_registration_mobile_no_edt.setVisibility(View.GONE);
                //layout_search_by_doctor.setVisibility(View.GONE);
                layout_search_by_department.setVisibility(View.GONE);
                patient_register_layout_category_L3.setVisibility(View.GONE);
                layout_search_by_visit_type.setVisibility(View.GONE);
                layout_search_by_visit_status.setVisibility(View.GONE);
                radio_group_2.clearCheck();
                radio_group_3.clearCheck();

                filter_text_1.setVisibility(View.VISIBLE);
                filter_text_2.setVisibility(View.GONE);
                filter_text_1.setText("First Name");
                checkRadio = 1;
            }
        });

        radio_button_by_lname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patient_registration_mrno_edt.setVisibility(View.GONE);
                patient_registration_firstname_edt.setVisibility(View.GONE);
                patient_registration_lastname_edt.setVisibility(View.VISIBLE);
                patient_registration_mobile_no_edt.setVisibility(View.GONE);
                //layout_search_by_doctor.setVisibility(View.GONE);
                layout_search_by_department.setVisibility(View.GONE);
                patient_register_layout_category_L3.setVisibility(View.GONE);
                layout_search_by_visit_type.setVisibility(View.GONE);
                layout_search_by_visit_status.setVisibility(View.GONE);
                radio_group_2.clearCheck();
                radio_group_3.clearCheck();

                filter_text_1.setVisibility(View.VISIBLE);
                filter_text_2.setVisibility(View.GONE);
                filter_text_1.setText("Last Name");
                checkRadio = 2;
            }
        });

        radio_button_by_mobileNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patient_registration_mrno_edt.setVisibility(View.GONE);
                patient_registration_firstname_edt.setVisibility(View.GONE);
                patient_registration_lastname_edt.setVisibility(View.GONE);
                patient_registration_mobile_no_edt.setVisibility(View.VISIBLE);
                //layout_search_by_doctor.setVisibility(View.GONE);
                layout_search_by_department.setVisibility(View.GONE);
                patient_register_layout_category_L3.setVisibility(View.GONE);
                layout_search_by_visit_type.setVisibility(View.GONE);
                layout_search_by_visit_status.setVisibility(View.GONE);
                radio_group_1.clearCheck();
                radio_group_3.clearCheck();

                filter_text_1.setVisibility(View.VISIBLE);
                filter_text_2.setVisibility(View.GONE);
                filter_text_1.setText("Mobile Number");
                checkRadio = 3;
            }
        });

        /*radio_button_by_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patient_registration_mrno_edt.setVisibility(View.GONE);
                patient_registration_firstname_edt.setVisibility(View.GONE);
                patient_registration_lastname_edt.setVisibility(View.GONE);
                patient_registration_mobile_no_edt.setVisibility(View.GONE);
                layout_search_by_doctor.setVisibility(View.VISIBLE);
                layout_search_by_department.setVisibility(View.GONE);
                patient_register_layout_category_L3.setVisibility(View.GONE);
                layout_search_by_visit_type.setVisibility(View.GONE);
                layout_search_by_visit_status.setVisibility(View.GONE);
                radio_group_1.clearCheck();
                radio_group_3.clearCheck();

                filter_text_1.setVisibility(View.VISIBLE);
                filter_text_2.setVisibility(View.GONE);
                filter_text_1.setText("Select Doctor");
                checkRadio = 4;
            }
        });*/

        radio_button_by_dept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patient_registration_mrno_edt.setVisibility(View.GONE);
                patient_registration_firstname_edt.setVisibility(View.GONE);
                patient_registration_lastname_edt.setVisibility(View.GONE);
                patient_registration_mobile_no_edt.setVisibility(View.GONE);
                //layout_search_by_doctor.setVisibility(View.GONE);
                layout_search_by_department.setVisibility(View.VISIBLE);
                patient_register_layout_category_L3.setVisibility(View.GONE);
                layout_search_by_visit_type.setVisibility(View.GONE);
                layout_search_by_visit_status.setVisibility(View.GONE);
                radio_group_1.clearCheck();
                radio_group_3.clearCheck();

                filter_text_1.setVisibility(View.VISIBLE);
                filter_text_2.setVisibility(View.GONE);
                filter_text_1.setText("Select Department");
                checkRadio = 5;

                setDepartmentData();
            }
        });

        radio_button_by_visit_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patient_registration_mrno_edt.setVisibility(View.GONE);
                patient_registration_firstname_edt.setVisibility(View.GONE);
                patient_registration_lastname_edt.setVisibility(View.GONE);
                patient_registration_mobile_no_edt.setVisibility(View.GONE);
                //layout_search_by_doctor.setVisibility(View.GONE);
                layout_search_by_department.setVisibility(View.GONE);
                patient_register_layout_category_L3.setVisibility(View.GONE);
                layout_search_by_visit_type.setVisibility(View.VISIBLE);
                layout_search_by_visit_status.setVisibility(View.GONE);
                radio_group_1.clearCheck();
                radio_group_3.clearCheck();

                filter_text_1.setVisibility(View.VISIBLE);
                filter_text_2.setVisibility(View.GONE);
                filter_text_1.setText("Select Visit Type");
                checkRadio = 7;

                setVisitTypeData();
            }
        });

        radio_button_by_visit_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patient_registration_mrno_edt.setVisibility(View.GONE);
                patient_registration_firstname_edt.setVisibility(View.GONE);
                patient_registration_lastname_edt.setVisibility(View.GONE);
                patient_registration_mobile_no_edt.setVisibility(View.GONE);
                //layout_search_by_doctor.setVisibility(View.GONE);
                layout_search_by_department.setVisibility(View.GONE);
                patient_register_layout_category_L3.setVisibility(View.GONE);
                layout_search_by_visit_type.setVisibility(View.GONE);
                layout_search_by_visit_status.setVisibility(View.VISIBLE);
                radio_group_1.clearCheck();
                radio_group_2.clearCheck();

                filter_text_1.setVisibility(View.VISIBLE);
                filter_text_2.setVisibility(View.GONE);
                filter_text_1.setText("Select Visit Status");
                checkRadio = 8;

                setVisitStatusData();
            }
        });

        radio_button_by_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patient_registration_mrno_edt.setVisibility(View.GONE);
                patient_registration_firstname_edt.setVisibility(View.GONE);
                patient_registration_lastname_edt.setVisibility(View.GONE);
                patient_registration_mobile_no_edt.setVisibility(View.GONE);
                //layout_search_by_doctor.setVisibility(View.GONE);
                layout_search_by_department.setVisibility(View.GONE);
                patient_register_layout_category_L3.setVisibility(View.VISIBLE);
                layout_search_by_visit_type.setVisibility(View.GONE);
                layout_search_by_visit_status.setVisibility(View.GONE);
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
                    GetPatientQueueWebcall();
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
            if (mobileNo.equals("") || mobileNo.length() == 0) {
                Toast.makeText(context, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                return false;
            } else if (mobileNo.length() > 0 && mobileNo.length() < 10) {
                Toast.makeText(context, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (checkRadio == 4) {
            if (SelectedDoctorID == null || SelectedDoctorID.equals("") || SelectedDoctorID.length() == 0) {
                Toast.makeText(context, "Please select doctor...", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (checkRadio == 5) {
            if (departmentslist != null && departmentslist.size() > 0) {
                SelectedDeptID = departmentslist.get(patient_register_department.getSelectedItemPosition()).getID();
            }
            if (SelectedDeptID == null || SelectedDeptID.equals("") || SelectedDeptID.length() == 0) {
                Toast.makeText(context, "Please select department...", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (checkRadio == 6) {
            if (SelectedCategoryID == null || SelectedCategoryID.equals("") || SelectedCategoryID.length() == 0) {
                Toast.makeText(context, "Please select category...", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (checkRadio == 7) {
            if (SelectedVisitType == null || SelectedVisitType.equals("") || SelectedVisitType.length() == 0) {
                Toast.makeText(context, "Please select visit type...", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (checkRadio == 8) {
            if (elVisitStatusArrayList != null && elVisitStatusArrayList.size() > 0) {
                SelectedVisitStatus = elVisitStatusArrayList.get(patient_register_visit_status.getSelectedItemPosition()).getID();
            }
            if (SelectedVisitStatus == null || SelectedVisitStatus.equals("") || SelectedVisitStatus.length() == 0) {
                Toast.makeText(context, "Please select visit status...", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void setVisitStatusData() {
        elVisitStatusArrayList = localSetting.returnVisitStatus();
        if (elVisitStatusArrayList != null && elVisitStatusArrayList.size() > 0) {
            visitStatusAdapter = new SpinnerAdapter.AppointmentStatusAdapter(context, elVisitStatusArrayList);
            patient_register_visit_status.setAdapter(visitStatusAdapter);
            visitStatusAdapter.notifyDataSetChanged();

            patient_register_visit_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int sVisitStatusPos = patient_register_visit_status.getSelectedItemPosition();
                    if (sVisitStatusPos == 0) {
                        SelectedVisitStatus = "0";
                    } else if (sVisitStatusPos > 0) {
                        sVisitStatusPos = sVisitStatusPos - 1;
                        SelectedVisitStatus = elVisitStatusArrayList.get(sVisitStatusPos).getID();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    private void setDepartmentData() {
        if (departmentslist != null && departmentslist.size() > 0) {
            departmentAdapter = new SpinnerAdapter.DepartmentAdapter(context, departmentslist);
            patient_register_department.setAdapter(departmentAdapter);
            departmentAdapter.notifyDataSetChanged();

            patient_register_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int sDeptPos = patient_register_department.getSelectedItemPosition();
                    if (sDeptPos == 0) {
                        SelectedDeptID = "0";
                    } else if (sDeptPos > 0) {
                        sDeptPos = sDeptPos - 1;
                        SelectedDeptID = departmentslist.get(sDeptPos).getID();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    private void setVisitTypeData() {
        if (elVisitTypeArrayList != null && elVisitTypeArrayList.size() > 0) {
            visitTypeListAdapter = new SpinnerAdapter.VisitTypeListAdapter(context, elVisitTypeArrayList);
            patient_register_visit_type.setAdapter(visitTypeListAdapter);
            visitTypeListAdapter.notifyDataSetChanged();

            patient_register_visit_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int sVisitTypePos = patient_register_visit_type.getSelectedItemPosition();
                    if (sVisitTypePos == 0) {
                        SelectedVisitType = "0";
                    } else if (sVisitTypePos > 0) {
                        sVisitTypePos = sVisitTypePos - 1;
                        SelectedVisitType = elVisitTypeArrayList.get(sVisitTypePos).getID();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    private void RefreshList() {
        doctorProfileList = doctorProfileAdapter.listAll();
        if (doctorProfileList != null && doctorProfileList.size() > 0) {
            if (patientName != null && patientName.equals("")) {
                patientName = null;
            }
            patientQueueArrayList = patientQueueAdapterDB.listToday(doctorProfileList.get(0).getUnitID(), patientName);
            if (patientQueueArrayList != null && patientQueueArrayList.size() > 0) {
                patientQueueAdapter = new PatientQueueAdapter(context, patientQueueArrayList);
                patient_queue_list.setAdapter(patientQueueAdapter);
                patientQueueAdapter.notifyDataSetChanged();
                patient_queue_list.setVisibility(View.VISIBLE);
                patient_queue_empty.setVisibility(View.GONE);
            } else {
                patient_queue_list.setVisibility(View.GONE);
                patient_queue_empty.setVisibility(View.VISIBLE);
            }
        }
    }

    private void GetPatientQueueWebcall() {
        doctorProfileList = doctorProfileAdapter.listAll();
        if (localSetting.isNetworkAvailable(context)) {
            new GetPatientQueueTask().execute();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
        }
    }

    private class GetPatientQueueTask extends AsyncTask<Void, Void, String> {
        private TransparentProgressDialog progressDialog;
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
                jsonObjectMapper = new JsonObjectMapper();
                webServiceConsumer = new WebServiceConsumer(context, null, null);

                if (doctorProfileList != null && doctorProfileList.size() > 0) {
                    ELFilter elFilter = new ELFilter();
                    elFilter.setUnitID(doctorProfileList.get(0).getUnitID());
                    elFilter.setMRNo(mrNO);
                    elFilter.setFirstName(firstName);
                    elFilter.setLastName(lastName);
                    elFilter.setMobileNo(mobileNo);
                    elFilter.setSelectedDoctorID(SelectedDoctorID);
                    elFilter.setSelectedDeptID(SelectedDeptID);
                    elFilter.setSelectedCategoryID(SelectedCategoryID);
                    elFilter.setSelectedVisitType(SelectedVisitType);
                    elFilter.setSelectedVisitStatus(SelectedVisitStatus);
                    elFilter.setFilterFlag(String.valueOf(checkRadio));

                    jSonData = jsonObjectMapper.unMap(elFilter);
                    response = webServiceConsumer.POST(Constants.PATIENT_QUEUE_URL, jSonData);

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
                patientQueueArrayList = jsonObjectMapper.map(responseString, PatientQueue.class);
                if (patientQueueArrayList != null && patientQueueArrayList.size() > 0) {
                    patientQueueAdapterDB.delete(doctorProfileList.get(0).getUnitID());
                    for (int index = 0; index < patientQueueArrayList.size(); index++) {
                        patientQueueAdapterDB.create(patientQueueArrayList.get(index));
                    }
                }
            } else if (responseCode == Constants.HTTP_NO_RECORD_FOUND_OK_204) {
                patientQueueAdapterDB.delete(doctorProfileList.get(0).getUnitID());
                Toast.makeText(context, "Queue not available for selected unit", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, localSetting.handleError(responseCode), Toast.LENGTH_SHORT).show();
            }
            localSetting.hideDialog(progressDialog);
            RefreshList();
            super.onPostExecute(result);
        }
    }
}
