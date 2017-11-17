package com.palash.healthspring.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AutoCompleteTextView;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.buzzbox.mob.android.scheduler.SchedulerManager;
import com.palash.healthspring.R;
import com.palash.healthspring.adapter.SpinnerAdapter;
import com.palash.healthspring.api.JsonObjectMapper;
import com.palash.healthspring.api.WebServiceConsumer;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.BookAppointment;
import com.palash.healthspring.entity.CPOEPrescription;
import com.palash.healthspring.entity.Flag;
import com.palash.healthspring.entity.MedicienFrequency;
import com.palash.healthspring.entity.MedicienInstruction;
import com.palash.healthspring.entity.MedicienName;
import com.palash.healthspring.entity.MedicienRoute;
import com.palash.healthspring.task.MasterTask;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;
import com.palash.healthspring.utilities.TransparentProgressDialog;
import com.palash.healthspring.utilities.Validate;
import com.squareup.okhttp.Response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by manishas on 7/29/2016.
 */
public class CPOEPrescriptionAddUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.MasterFlagAdapter masterFlagAdapter;
    private DatabaseAdapter.CPOEMedicineAdapter cpoeMedicineAdapterDB;
    //private DatabaseAdapter.MedicienNameAdapter medicineNameAdapterDB;
    private DatabaseAdapter.MedicienRouteAdapter medicineRouteAdapterDB;
    private DatabaseAdapter.MedicienFrequencyAdapter medicineFrequencyAdapterDB;
    private DatabaseAdapter.MedicienInstructionAdapter medicineInstructionAdapterDB;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapterDB;

    private Flag masterflag;
    private ArrayList<BookAppointment> bookAppointmentArrayList;
    private ArrayList<MedicienName> medicienNameArrayList;
    private ArrayList<MedicienInstruction> medicienInstructionArrayList;
    private ArrayList<MedicienRoute> medicienRouteArrayList;
    private ArrayList<MedicienFrequency> medicienFrequencyArrayList;
    private ArrayList<CPOEPrescription> cpoePrescriptionArrayList;

    private CPOEPrescription cpoePrescription;
    private SpinnerAdapter.MedicienNameAdapter medicienNameAdapter;
    private SpinnerAdapter.MedicienFrequencyAdapter medicienFrequencyAdapter;
    private SpinnerAdapter.MedicienRouteAdapter medicienRouteAdapter;
    private SpinnerAdapter.MedicienInstructionAdapter medicienInstructionAdapter;

    private AutoCompleteTextView edt_medicine_name;
    private MaterialSpinner medicine_spinner_route;
    private MaterialSpinner medicine_spinner_instruction;
    private MaterialSpinner medicine_spinner_frequentcy;
    private EditText edt_medicine_dose;
    private EditText edt_medicine_days;
    private EditText edt_medicine_quntity;
    private EditText edt_medicine_rate;
    private TextView medical_spinner_search_btn;
    private Chronometer emr_CPOEMedicien_chronometer;

    private String medicine_ID;
    private String medicine_Name;
    private String QuntityPerDay;
    private String Msg;
    private String isUpdate = "";
    private String SearchText = "";
    private int Posmedicineroute;
    private int Posmedicineinstruction;
    private int Posmedicinefrequentcy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpoe_medicine);
        InitSetting();
        MasterFlagTask();
        InitiView();
        SetSpinnerAdapter();

        isUpdate = getIntent().getStringExtra("isUpdate");
        if (isUpdate.equals("Yes")) {
            LoadUpdateRecordData();
        }
    }

    private void InitSetting() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        context = this;
        localSetting = new LocalSetting();
        databaseContract = new DatabaseContract(context);
        databaseAdapter = new DatabaseAdapter(databaseContract);
        masterFlagAdapter = databaseAdapter.new MasterFlagAdapter();
        bookAppointmentAdapterDB = databaseAdapter.new BookAppointmentAdapter();
        bookAppointmentArrayList = bookAppointmentAdapterDB.listLast();
        cpoeMedicineAdapterDB = databaseAdapter.new CPOEMedicineAdapter();
        //medicineNameAdapterDB = databaseAdapter.new MedicienNameAdapter();
        medicineRouteAdapterDB = databaseAdapter.new MedicienRouteAdapter();
        medicineFrequencyAdapterDB = databaseAdapter.new MedicienFrequencyAdapter();
        medicineInstructionAdapterDB = databaseAdapter.new MedicienInstructionAdapter();
        cpoeMedicineAdapterDB = databaseAdapter.new CPOEMedicineAdapter();
    }

    private void InitiView() {
        edt_medicine_dose = (EditText) findViewById(R.id.cpoemedicine_add_edt_medicine_dose);
        edt_medicine_days = (EditText) findViewById(R.id.cpoemedicine_add_medicine_edt_days);
        edt_medicine_quntity = (EditText) findViewById(R.id.cpoemedicine_add_edt_medicine_quntity);
        edt_medicine_name = (AutoCompleteTextView) findViewById(R.id.cpoemedicine_add_edt_medicine_name);
        edt_medicine_rate = (EditText) findViewById(R.id.cpoemedicine_add_edt_rate);
        medical_spinner_search_btn = (TextView) findViewById(R.id.medical_spinner_search_btn);
        medicine_spinner_route = (MaterialSpinner) findViewById(R.id.cpoemedicine_add_medicine_spinner_route);
        medicine_spinner_instruction = (MaterialSpinner) findViewById(R.id.cpoemedicine_add_medicine_spinner_instruction);
        medicine_spinner_frequentcy = (MaterialSpinner) findViewById(R.id.cpoemedicine_add_medicine_spinner_frequentcy);
        emr_CPOEMedicien_chronometer = (Chronometer) findViewById(R.id.emr_CPOEMedicien_chronometer);
        emr_CPOEMedicien_chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                frequentcySet();
            }
        });

        medical_spinner_search_btn.setOnClickListener(this);
    }

    private void MasterFlagTask() {
        masterflag = masterFlagAdapter.listCurrent();
        masterflag.setFlag(Constants.EMR_MEDICINE_MASTER_TASK);
        masterFlagAdapter.create(masterflag);
        SchedulerManager.getInstance().runNow(context, MasterTask.class, 1);
    }

    private void SetSpinnerAdapter() {
        try {
            //medicienNameArrayList = medicineNameAdapterDB.listAll();
            medicienRouteArrayList = medicineRouteAdapterDB.listAll();
            medicienFrequencyArrayList = medicineFrequencyAdapterDB.listAll();
            medicienInstructionArrayList = medicineInstructionAdapterDB.listAll();

            if (medicienNameArrayList != null && medicienNameArrayList.size() > 0) {

                edt_medicine_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        medicienNameAdapter.getFilter().filter(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        medicienNameAdapter.getFilter().filter(s.toString());
                    }
                });

                edt_medicine_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (medicienNameArrayList != null && medicienNameArrayList.size() > 0) {
                            Log.d(Constants.TAG + "" + "MedicalPosition : ", "" + position);
                            Log.d(Constants.TAG + "" + "MedicalID : ", "" + medicienNameArrayList.get(position).getID());
                            Log.d(Constants.TAG + "" + "MedicalName : ", "" + medicienNameArrayList.get(position).getItemName());
                            Log.d(Constants.TAG + "" + "MedicalRate : ", "" + medicienNameArrayList.get(position).getMRP());

                            medicine_ID = medicienNameArrayList.get(position).getID();
                            medicine_Name = medicienNameArrayList.get(position).getItemName();
                            edt_medicine_rate.setText(medicienNameArrayList.get(position).getMRP());
                        }
                        /*medicienNameArrayListDB = medicineNameAdapterDB.listAll(medicine_Name);
                        edt_medicine_name.setText(medicine_Name);
                        if (medicienNameArrayListDB != null && medicienNameArrayListDB.size() > 0) {
                            medicine_ID = medicienNameArrayListDB.get(0).getID();
                            edt_medicine_rate.setText(medicienNameArrayListDB.get(0).getMRP());
                        }*/
                    }
                });
            }

            if (medicienRouteArrayList != null && medicienRouteArrayList.size() > 0) {
                medicienRouteAdapter = new SpinnerAdapter.MedicienRouteAdapter(context, medicienRouteArrayList);
                medicine_spinner_route.setAdapter(medicienRouteAdapter);
                medicienRouteAdapter.notifyDataSetChanged();
            }
            if (medicienInstructionArrayList != null && medicienInstructionArrayList.size() > 0) {
                medicienInstructionAdapter = new SpinnerAdapter.MedicienInstructionAdapter(context, medicienInstructionArrayList);
                medicine_spinner_instruction.setAdapter(medicienInstructionAdapter);
                medicienInstructionAdapter.notifyDataSetChanged();
            }
            if (medicienFrequencyArrayList != null && medicienFrequencyArrayList.size() > 0) {
                medicienFrequencyAdapter = new SpinnerAdapter.MedicienFrequencyAdapter(context, medicienFrequencyArrayList);
                medicine_spinner_frequentcy.setAdapter(medicienFrequencyAdapter);
                medicienFrequencyAdapter.notifyDataSetChanged();
            }

            medicine_spinner_frequentcy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Posmedicinefrequentcy = medicine_spinner_frequentcy.getSelectedItemPosition();
                    if (Posmedicinefrequentcy > 0) {
                        Posmedicinefrequentcy = Posmedicinefrequentcy - 1;
                    }
                    QuntityPerDay = medicienFrequencyArrayList.get(Posmedicinefrequentcy).getQuntityPerDay();
                    String numberOfDays = edt_medicine_days.getText().toString();
                    if (numberOfDays != null && numberOfDays.length() > 0) {
                        try {
                            Float totalNumberOfMedicine = Float.parseFloat(QuntityPerDay) * Integer.parseInt(numberOfDays);
                            edt_medicine_quntity.setText(String.valueOf(totalNumberOfMedicine));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void LoadUpdateRecordData() {
        cpoePrescriptionArrayList = cpoeMedicineAdapterDB.CurrentUpdateNotes();
        if (cpoePrescriptionArrayList != null && cpoePrescriptionArrayList.size() > 0) {
            cpoePrescription = cpoePrescriptionArrayList.get(0);

            medicine_ID = cpoePrescription.getDrugID();
            medicine_Name = cpoePrescription.getItemName();
            edt_medicine_dose.setText(cpoePrescription.getDose());
            edt_medicine_days.setText(cpoePrescription.getDays());
            edt_medicine_quntity.setText(cpoePrescription.getQuantity());
            edt_medicine_name.setText(cpoePrescription.getItemName());
            edt_medicine_rate.setText(cpoePrescription.getRate());
            int routepos = 0;
            int instructionpos = 0;
            int frequentcypos = 0;
            try {
                routepos = Integer.parseInt(cpoePrescription.getRouteID());
                instructionpos = Integer.parseInt(cpoePrescription.getReasonID());
                frequentcypos = Integer.parseInt(cpoePrescription.getFrequencyID());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            medicine_spinner_route.setSelection(routepos);
            medicine_spinner_instruction.setSelection(instructionpos);
            medicine_spinner_frequentcy.setSelection(frequentcypos);

            cpoeMedicineAdapterDB.removeCurrentUpdateNotes();
        }
    }

    @Override
    public void onResume() {
        emr_CPOEMedicien_chronometer.setBase(SystemClock.elapsedRealtime());
        emr_CPOEMedicien_chronometer.start();
        super.onResume();
    }

    private void frequentcySet() {
        Posmedicinefrequentcy = medicine_spinner_frequentcy.getSelectedItemPosition();
        if (Posmedicinefrequentcy > 0) {
            Posmedicinefrequentcy = Posmedicinefrequentcy - 1;
        }
        if (medicienFrequencyArrayList != null && medicienFrequencyArrayList.size() > 0) {
            QuntityPerDay = medicienFrequencyArrayList.get(Posmedicinefrequentcy).getQuntityPerDay();
            String numberOfDays = edt_medicine_days.getText().toString();
            if (numberOfDays != null && edt_medicine_days.getText().toString().length() > 0) {
                try {
                    Float totalNumberOfMedicine = Float.parseFloat(QuntityPerDay) * Integer.parseInt(numberOfDays);
                    edt_medicine_quntity.setText(String.valueOf(totalNumberOfMedicine));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                edt_medicine_quntity.setText("");
            }
        }
    }

    private String QuntityPerDay(int Posmedicinefrequentcy) {
        if (medicienFrequencyArrayList.get(Posmedicinefrequentcy).getDescription().equals("1-0-0")) {
            QuntityPerDay = "1";
        } else if (medicienFrequencyArrayList.get(Posmedicinefrequentcy).getDescription().equals("1-1-1")) {
            QuntityPerDay = "3";
        } else if (medicienFrequencyArrayList.get(Posmedicinefrequentcy).getDescription().equals("0-1-0")) {
            QuntityPerDay = "1";
        } else if (medicienFrequencyArrayList.get(Posmedicinefrequentcy).getDescription().equals("0-0-1")) {
            QuntityPerDay = "1";
        } else if (medicienFrequencyArrayList.get(Posmedicinefrequentcy).getDescription().equals("1-1-1-1")) {
            QuntityPerDay = "4";
        } else if (medicienFrequencyArrayList.get(Posmedicinefrequentcy).getDescription().equals("1-1-0")) {
            QuntityPerDay = "2";
        } else if (medicienFrequencyArrayList.get(Posmedicinefrequentcy).getDescription().equals("0-1-1")) {
            QuntityPerDay = "2";
        } else if (medicienFrequencyArrayList.get(Posmedicinefrequentcy).getDescription().equals("1-0-1")) {
            QuntityPerDay = "2";
        } else if (medicienFrequencyArrayList.get(Posmedicinefrequentcy).getDescription().equals("2-2-2")) {
            QuntityPerDay = "6";
        } else if (medicienFrequencyArrayList.get(Posmedicinefrequentcy).getDescription().equals("SOS")) {
            QuntityPerDay = "1";
        } else if (medicienFrequencyArrayList.get(Posmedicinefrequentcy).getDescription().equals("1-1/2-1")) {
            QuntityPerDay = "2.5";
        } else if (medicienFrequencyArrayList.get(Posmedicinefrequentcy).getDescription().equals("1-1-1/2")) {
            QuntityPerDay = "2.5";
        } else if (medicienFrequencyArrayList.get(Posmedicinefrequentcy).getDescription().equals("1/2-1/2-1/2")) {
            QuntityPerDay = "1.5";
        } else if (medicienFrequencyArrayList.get(Posmedicinefrequentcy).getDescription().equals("")) {
            QuntityPerDay = "1";
        }
        return QuntityPerDay;
    }

    private boolean validateControls() {
        if (medicine_Name.equals("") || edt_medicine_name.getText().toString().equals("null") || edt_medicine_name.getText().toString().trim().equals("")
                || edt_medicine_name.getText().toString().trim().length() == 0) {
            Validate.Msgshow(context, "Please enter medicine name.");
            return false;
        } else if (edt_medicine_rate.getText().toString().equals("null") || edt_medicine_rate.getText().toString().trim().equals("")
                || edt_medicine_rate.getText().toString().trim().length() == 0) {
            Validate.Msgshow(context, "Please enter medicine name.");
            return false;
        } else if (!Validate.hasTextIn(edt_medicine_dose.getText().toString())) {
            Validate.Msgshow(context, "Please enter medicine dose.");
            return false;
        } else if (!Validate.isValidOption(medicine_spinner_frequentcy.getSelectedItemPosition())) {
            Validate.Msgshow(context, "Please select frequency.");
            return false;
        } else if (!Validate.hasTextIn(edt_medicine_days.getText().toString())) {
            Validate.Msgshow(context, "Please enter medicine days.");
            return false;
        } else if (!Validate.isValidOption(medicine_spinner_route.getSelectedItemPosition())) {
            Validate.Msgshow(context, "Please select route.");
            return false;
        } else if (!Validate.isValidOption(medicine_spinner_instruction.getSelectedItemPosition())) {
            Validate.Msgshow(context, "Please select instruction.");
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_current_medication, menu);
        menu.findItem(R.id.menu_current_medication_save).setVisible(true);
        menu.findItem(R.id.menu_current_medication_cancle).setVisible(true);
        menu.findItem(R.id.menu_current_medication_refresh).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_current_medication_save:
                if (validateControls()) {
                    if (isUpdate.equals("Yes")) {
                        CPOEMedicineUpDateBindView();
                    } else {
                        CPOEMedicineAddBindView();
                    }
                }
                return true;
            case R.id.menu_current_medication_cancle:
                Clear();
                return true;
            case R.id.menu_current_medication_refresh:
                MasterFlagTask();
                SetSpinnerAdapter();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.medical_spinner_search_btn:
                if (edt_medicine_name.getText().toString() != null && edt_medicine_name.getText().toString().length() > 0) {
                    SearchText = edt_medicine_name.getText().toString();
                    if (localSetting.isNetworkAvailable(context)) {
                        new GetMedicineNameListTask().execute();
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void Clear() {
        edt_medicine_dose.setText("");
        edt_medicine_days.setText("");
        edt_medicine_quntity.setText("");
        edt_medicine_name.setText("");
        medicine_spinner_route.setSelection(0);
        medicine_spinner_instruction.setSelection(0);
        medicine_spinner_frequentcy.setSelection(0);
        isUpdate = "No";
    }

    private void CPOEMedicineAddBindView() {
        try {
            new AlertDialog
                    .Builder(context)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Do you really want to add cpoe medicine?")
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Calendar c = Calendar.getInstance();
                            cpoePrescription = new CPOEPrescription();
                            Posmedicineroute = medicine_spinner_route.getSelectedItemPosition();
                            if (Posmedicineroute > 0) {
                                Posmedicineroute = Posmedicineroute - 1;
                            }
                            Posmedicineinstruction = medicine_spinner_instruction.getSelectedItemPosition();
                            if (Posmedicineinstruction > 0) {
                                Posmedicineinstruction = Posmedicineinstruction - 1;
                            }
                            Posmedicinefrequentcy = medicine_spinner_frequentcy.getSelectedItemPosition();
                            if (Posmedicinefrequentcy > 0) {
                                Posmedicinefrequentcy = Posmedicinefrequentcy - 1;
                            }
                            cpoePrescription.setUnitID(bookAppointmentArrayList.get(0).getDoctorUnitID());
                            cpoePrescription.setPatientID(bookAppointmentArrayList.get(0).getPatientID());
                            cpoePrescription.setPatientUnitID(bookAppointmentArrayList.get(0).getUnitID());
                            cpoePrescription.setVisitID(bookAppointmentArrayList.get(0).getVisitID());
                            cpoePrescription.setDrugID(medicine_ID);
                            cpoePrescription.setItemName(medicine_Name);
                            cpoePrescription.setRate(edt_medicine_rate.getText().toString());
                            cpoePrescription.setDose(edt_medicine_dose.getText().toString().trim());
                            cpoePrescription.setDoctorID(bookAppointmentArrayList.get(0).getDoctorID());
                            cpoePrescription.setAddedBy(bookAppointmentArrayList.get(0).getDoctorID());
                            cpoePrescription.setRouteID(medicienRouteArrayList.get(Posmedicineroute).getID());
                            cpoePrescription.setRoute(medicienRouteArrayList.get(Posmedicineroute).getDescription());
                            cpoePrescription.setFrequencyID(medicienFrequencyArrayList.get(Posmedicinefrequentcy).getID());
                            cpoePrescription.setFrequency(medicienFrequencyArrayList.get(Posmedicinefrequentcy).getDescription());
                            cpoePrescription.setDays(edt_medicine_days.getText().toString().trim());
                            cpoePrescription.setQuantity(edt_medicine_quntity.getText().toString().trim());
                            cpoePrescription.setReasonID(medicienInstructionArrayList.get(Posmedicineinstruction).getID());
                            cpoePrescription.setReason(medicienInstructionArrayList.get(Posmedicineinstruction).getDescription());
                            cpoePrescription.setIsSync("1");

                            SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                            String CurrentDate = simpleDF.format(c.getTime());
                            cpoePrescription.setDate(CurrentDate);
                            Msg = "Medicine added successfully";

                            new CPOEMedicineAddUpdateTask().execute();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(R.mipmap.ic_launcher)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CPOEMedicineUpDateBindView() {
        try {
            new AlertDialog
                    .Builder(context)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Do you really want to update cpoe medicine?")
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Calendar c = Calendar.getInstance();

                            int Posmedicineroute = medicine_spinner_route.getSelectedItemPosition();
                            if (Posmedicineroute > 0) {
                                Posmedicineroute = Posmedicineroute - 1;
                            }
                            int Posmedicineinstruction = medicine_spinner_instruction.getSelectedItemPosition();
                            if (Posmedicineinstruction > 0) {
                                Posmedicineinstruction = Posmedicineinstruction - 1;
                            }
                            int Posmedicinefrequentcy = medicine_spinner_frequentcy.getSelectedItemPosition();
                            if (Posmedicinefrequentcy > 0) {
                                Posmedicinefrequentcy = Posmedicinefrequentcy - 1;
                            }
                            //cpoePrescription.setID(cpoePrescriptionArrayList.get(0).getID());
                            //cpoePrescription.setUnitID(cpoePrescriptionArrayList.get(0).getUnitID());
                            //cpoePrescription.setPatientID(cpoePrescriptionArrayList.get(0).getPatientID());
                            //cpoePrescription.setPatientUnitID(cpoePrescriptionArrayList.get(0).getUnitID());
                            //cpoePrescription.setVisitID(cpoePrescriptionArrayList.get(0).getVisitID());
                            cpoePrescription.setDrugID(medicine_ID);
                            cpoePrescription.setRate(edt_medicine_rate.getText().toString());
                            cpoePrescription.setItemName(medicine_Name);
                            cpoePrescription.setDose(edt_medicine_dose.getText().toString().trim());
                            cpoePrescription.setRouteID(medicienRouteArrayList.get(Posmedicineroute).getID());
                            cpoePrescription.setRoute(medicienRouteArrayList.get(Posmedicineroute).getDescription());
                            cpoePrescription.setFrequencyID(medicienFrequencyArrayList.get(Posmedicinefrequentcy).getID());
                            cpoePrescription.setFrequency(medicienFrequencyArrayList.get(Posmedicinefrequentcy).getDescription());
                            cpoePrescription.setDays(edt_medicine_days.getText().toString().trim());
                            cpoePrescription.setQuantity(edt_medicine_quntity.getText().toString().trim());
                            cpoePrescription.setReasonID(medicienInstructionArrayList.get(Posmedicineinstruction).getID());
                            cpoePrescription.setReason(medicienInstructionArrayList.get(Posmedicineinstruction).getDescription());
                            cpoePrescription.setDoctorID(bookAppointmentArrayList.get(0).getDoctorID());
                            cpoePrescription.setAddedBy(bookAppointmentArrayList.get(0).getDoctorID());
                            cpoePrescription.setIsSync("1");

                            SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                            String CurrentDate = simpleDF.format(c.getTime());
                            cpoePrescription.setDate(CurrentDate);

                            Msg = "Medicine updated successfully";
                            new CPOEMedicineAddUpdateTask().execute();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(R.mipmap.ic_launcher)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class CPOEMedicineAddUpdateTask extends AsyncTask<Void, Void, String> {
        private int responseCode = 0;
        private TransparentProgressDialog progressDialog = null;
        private JsonObjectMapper objMapper = null;
        private WebServiceConsumer serviceConsumer = null;
        private Response response = null;
        private String jSonData = "";
        private String responseMSG = "";
        private String responseString = "";

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                objMapper = new JsonObjectMapper();
                jSonData = objMapper.unMap(cpoePrescription);
                serviceConsumer = new WebServiceConsumer(context, null, null);
                response = serviceConsumer.POST(Constants.CPOEMEDICINE_ADD_UPDATE_URL, jSonData);
                if (response != null) {
                    responseCode = response.code();
                    responseMSG = response.message().toString();
                    responseString = response.body().string();
                    Log.d(Constants.TAG, "Response code:" + responseCode);
                    Log.d(Constants.TAG, "Response MSG:" + responseMSG);
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
                if (responseCode == Constants.HTTP_CREATED_201 && responseMSG.equals("Created")) {
                    localSetting.hideDialog(progressDialog);
                    new AlertDialog
                            .Builder(context)
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage(Msg)
                            .setCancelable(false)
                            .setPositiveButton("Go to Patient EMR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Clear();
                                    finish();
                                }
                            })
                            .setIcon(R.mipmap.ic_launcher).show();
                } else if (responseCode == Constants.HTTP_OK_200 && responseMSG.equals("OK")) {
                    localSetting.hideDialog(progressDialog);
                    new AlertDialog
                            .Builder(context)
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage(Msg)
                            .setCancelable(false)
                            .setPositiveButton("Go to Patient EMR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Clear();
                                    finish();
                                }
                            })
                            .setIcon(R.mipmap.ic_launcher).show();
                } else {
                    localSetting.hideDialog(progressDialog);
                    //localSetting.alertbox(context, localSetting.handleError(responseCode), false);
                    new AlertDialog
                            .Builder(context)
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage(context.getResources().getString(R.string.offline_alert))
                            .setCancelable(true)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (isUpdate.equals("Yes")) {
                                        cpoeMedicineAdapterDB.updateUnSync(cpoePrescription);
                                    } else {
                                        cpoeMedicineAdapterDB.createUnSync(cpoePrescription);
                                    }
                                    Clear();
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.mipmap.ic_launcher)
                            .show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }

    public class GetMedicineNameListTask extends AsyncTask<Void, Void, String> {
        private int responseCode = 0;
        private TransparentProgressDialog progressDialog = null;
        private JsonObjectMapper objMapper = null;
        private WebServiceConsumer serviceConsumer = null;
        private Response response = null;
        private String responseMSG = "";
        private String responseString = "";

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                objMapper = new JsonObjectMapper();
                serviceConsumer = new WebServiceConsumer(context, null, null);
                SearchText = SearchText.replaceAll(" ","_");
                response = serviceConsumer.GET(Constants.MEDICIENNAME_URL + "?SearchText=" + SearchText);
                if (response != null) {
                    responseCode = response.code();
                    responseMSG = response.message().toString();
                    responseString = response.body().string();
                    Log.d(Constants.TAG, "Response code:" + responseCode);
                    Log.d(Constants.TAG, "Response MSG:" + responseMSG);
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
                localSetting.hideDialog(progressDialog);
                if (responseCode == Constants.HTTP_OK_200) {
                    medicienNameArrayList = objMapper.map(responseString, MedicienName.class);
                    medicienNameAdapter = new SpinnerAdapter.MedicienNameAdapter(context, medicienNameArrayList);
                    edt_medicine_name.setAdapter(medicienNameAdapter);
                    edt_medicine_name.setThreshold(1);
                    edt_medicine_name.showDropDown();
                    medicienNameAdapter.notifyDataSetChanged();
                    SetSpinnerAdapter();
                } else if (responseCode == Constants.HTTP_NO_RECORD_FOUND_OK_204) {
                    localSetting.alertbox(context, "No data available for given search. Please try again", false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }
}
