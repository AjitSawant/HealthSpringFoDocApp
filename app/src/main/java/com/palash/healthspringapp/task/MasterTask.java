package com.palash.healthspringapp.task;

import android.content.ContextWrapper;
import android.util.Log;

import com.buzzbox.mob.android.scheduler.NotificationMessage;
import com.buzzbox.mob.android.scheduler.Task;
import com.buzzbox.mob.android.scheduler.TaskResult;
import com.palash.healthspringapp.R;
import com.palash.healthspringapp.api.JsonObjectMapper;
import com.palash.healthspringapp.api.WebServiceConsumer;
import com.palash.healthspringapp.database.DatabaseAdapter;
import com.palash.healthspringapp.database.DatabaseContract;
import com.palash.healthspringapp.entity.AppointmentReason;
import com.palash.healthspringapp.entity.BloodGroup;
import com.palash.healthspringapp.entity.Complaint;
import com.palash.healthspringapp.entity.DaignosisTypeMaster;
import com.palash.healthspringapp.entity.Department;
import com.palash.healthspringapp.entity.DoctorProfile;
import com.palash.healthspringapp.entity.DoctorType;
import com.palash.healthspringapp.entity.ELUnitMaster;
import com.palash.healthspringapp.entity.Flag;
import com.palash.healthspringapp.entity.Gender;
import com.palash.healthspringapp.entity.MaritalStatus;
import com.palash.healthspringapp.entity.MedicienFrequency;
import com.palash.healthspringapp.entity.MedicienInstruction;
import com.palash.healthspringapp.entity.MedicienRoute;
import com.palash.healthspringapp.entity.Prefix;
import com.palash.healthspringapp.entity.Priority;
import com.palash.healthspringapp.entity.Specialization;
import com.palash.healthspringapp.entity.Synchronization;
import com.palash.healthspringapp.entity.Vital;
import com.palash.healthspringapp.utilities.Constants;
import com.palash.healthspringapp.utilities.LocalSetting;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

public class MasterTask implements Task {

    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.UnitMasterAdapter unitMasterAdapter;
    private DatabaseAdapter.DoctorTypeAdapter doctorTypeAdapter;
    private DatabaseAdapter.SpecializationAdapter specializationAdapter;
    private DatabaseAdapter.AppointmentReasonAdapter appointmentReasonAdapter;
    private DatabaseAdapter.DepartmentAdapter departmentAdapter;
    private DatabaseAdapter.ComplaintAdapter complaintAdapter;
    private DatabaseAdapter.MedicienRouteAdapter medicienRouteAdapter;
    private DatabaseAdapter.MedicienFrequencyAdapter medicienFrequencyAdapter;
    private DatabaseAdapter.MedicienInstructionAdapter medicienInstructionAdapter;
    private DatabaseAdapter.VitalAdapter vitalAdapter;
    private DatabaseAdapter.DaignosisTypeMasterAdapter daignosisTypeMasterAdapter;
    private DatabaseAdapter.PriorityAdapter priorityAdapter;
    private DatabaseAdapter.MasterFlagAdapter masterFlagAdapter;

    private DatabaseAdapter.PrefixAdapter prefixAdapter;
    private DatabaseAdapter.GenderAdapter genderAdapter;
    private DatabaseAdapter.MaritalStatusAdapter maritalStatusAdapter;
    private DatabaseAdapter.BloodGroupAdapter bloodGroupAdapter;

    /*private DatabaseAdapter.MedicienNameAdapter medicienNameAdapter;
    private DatabaseAdapter.DaignosisMasterAdapter daignosisMasterAdapter;
    private DatabaseAdapter.ServiceNameAdapter serviceNameAdapter;
    private DatabaseAdapter.MoleculeAdapter moleculeAdapter;*/

    private WebServiceConsumer serviceConsumer;
    private Response response;
    private JsonObjectMapper objectMapper;
    private TaskResult taskResult;

    private ArrayList<DoctorProfile> doctorProfileList;
    private ArrayList<Gender> genderList;
    private ArrayList<Prefix> prefixList;
    private ArrayList<MaritalStatus> maritalStatusList;
    private ArrayList<DoctorType> doctorTypeList;
    private ArrayList<ELUnitMaster> unitMasterList;
    private ArrayList<Specialization> specializationList;
    private ArrayList<AppointmentReason> appointmentReasonList;
    private ArrayList<Department> departmentList;
    private ArrayList<Complaint> complaintList;
    private ArrayList<BloodGroup> bloodGroupList;
    private ArrayList<MedicienRoute> medicienRouteList;
    private ArrayList<Synchronization> synchronizationList;
    private ArrayList<MedicienFrequency> medicienFrequencyList;
    private ArrayList<MedicienInstruction> medicienInstructionList;
    private ArrayList<Vital> vitalList;
    private ArrayList<DaignosisTypeMaster> daignosisTypeMasterList;
    private ArrayList<Priority> priorityList;
    private ArrayList<Flag> masterflagArrayList;

    /*private ArrayList<MedicienName> medicienNameList;
    private ArrayList<DaignosisMaster> daignosisMasterList;
    private ArrayList<ServiceName> serviceNameList;
    private ArrayList<Molecule> moleculeList;*/

    private Flag masterdataflag;
    private NotificationMessage notificationMessage;
    private Synchronization synchronization;

    private String responseString;
    private int responseCode;
    private int Count;

    @Override
    public TaskResult doWork(ContextWrapper contextWrapper) {
        try {
            //Initialization
            responseCode = 0;
            responseString = null;
            taskResult = new TaskResult();
            localSetting = new LocalSetting();
            localSetting.Init(contextWrapper);
            localSetting.Load();
            objectMapper = new JsonObjectMapper();
            serviceConsumer = new WebServiceConsumer(contextWrapper, null, null);
            //Database initialization
            databaseContract = new DatabaseContract(contextWrapper);
            databaseAdapter = new DatabaseAdapter(databaseContract);
            doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();
            unitMasterAdapter = databaseAdapter.new UnitMasterAdapter();
            doctorTypeAdapter = databaseAdapter.new DoctorTypeAdapter();
            specializationAdapter = databaseAdapter.new SpecializationAdapter();
            appointmentReasonAdapter = databaseAdapter.new AppointmentReasonAdapter();
            departmentAdapter = databaseAdapter.new DepartmentAdapter();
            complaintAdapter = databaseAdapter.new ComplaintAdapter();
            medicienRouteAdapter = databaseAdapter.new MedicienRouteAdapter();
            medicienFrequencyAdapter = databaseAdapter.new MedicienFrequencyAdapter();
            medicienInstructionAdapter = databaseAdapter.new MedicienInstructionAdapter();
            vitalAdapter = databaseAdapter.new VitalAdapter();
            daignosisTypeMasterAdapter = databaseAdapter.new DaignosisTypeMasterAdapter();
            priorityAdapter = databaseAdapter.new PriorityAdapter();
            masterFlagAdapter = databaseAdapter.new MasterFlagAdapter();

            prefixAdapter = databaseAdapter.new PrefixAdapter();
            genderAdapter = databaseAdapter.new GenderAdapter();
            maritalStatusAdapter = databaseAdapter.new MaritalStatusAdapter();
            bloodGroupAdapter = databaseAdapter.new BloodGroupAdapter();

            /*medicienNameAdapter = databaseAdapter.new MedicienNameAdapter();
            daignosisMasterAdapter = databaseAdapter.new DaignosisMasterAdapter();
            serviceNameAdapter = databaseAdapter.new ServiceNameAdapter();
            moleculeAdapter = databaseAdapter.new MoleculeAdapter();*/

            //Login data
            doctorProfileList = doctorProfileAdapter.listAll();

            if (doctorProfileList != null && doctorProfileList.size() > 0 && doctorProfileList.get(0).getLoginStatus().equals(Constants.STATUS_LOG_IN)) {
                synchronization = new Synchronization();
                synchronization.setUnitID(doctorProfileList.get(0).getUnitID());
                synchronization.setDoctorID(doctorProfileList.get(0).getDoctorID());
                // Synchronization task
                response = serviceConsumer.POST(Constants.SYNCHRONIZATION_URL, objectMapper.unMap(synchronization));
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    Log.d(Constants.TAG, "Sync Data : " + responseString);
                    if (responseCode == Constants.HTTP_OK_200) {
                        synchronizationList = objectMapper.map(responseString, Synchronization.class);

                        if (synchronizationList != null && synchronizationList.size() > 0) {
                            masterflagArrayList = masterFlagAdapter.listLast();
                            if (masterflagArrayList != null && masterflagArrayList.size() > 0) {
                                masterdataflag = masterflagArrayList.get(0);
                                switch (masterdataflag.getFlag()) {
                                    case Constants.PATIENT_REGISTRATION_TASK:
                                        //PrefixTask();
                                        //GenderTask();
                                        //MaritalStatusTask();
                                        //BloodGroupTask();
                                        DepartmentTask();
                                        AppointmentReasonTask();
                                        ComplaintTask();
                                        break;
                                    case Constants.BOOK_APPOINTMENT_TASK:
                                        DepartmentTask();
                                        AppointmentReasonTask();
                                        ComplaintTask();
                                        SpecializationTask();
                                        DoctorTypeTask();
                                        break;
                                    case Constants.EMR_MEDICINE_MASTER_TASK:
                                        MedicienRouteTask();
                                        MedicineFrequencyTask();
                                        MedicienInstructionTask();
                                        //MedicienNameTask();
                                        break;
                                    case Constants.EMR_COMPLAINT_MASTER_TASK:
                                        ComplaintTask();
                                        break;
                                    case Constants.EMR_DIAGNOSIS_MASTER_TASK:
                                        DaignosisTypeTask();
                                        //DiagnosisTask();
                                        break;
                                    case Constants.EMR_SERVICE_MASTER_TASK:
                                        PriorityTask();
                                        //ServiceNameTask();
                                        break;
                                    case Constants.EMR_VITAL_MASTER_TASK:
                                        VitalTask();
                                    case Constants.EMR_DEPARTMENT_MASTER_TASK:
                                        DepartmentTask();
                                        break;
                                    case Constants.EMR_TASK:

                                        //Master data
                                        masterdataflag = masterFlagAdapter.listCurrent();
                                        masterdataflag.setMsg("Synchronizing Master");
                                        masterFlagAdapter.create(masterdataflag);

                                        PrefixTask();
                                        GenderTask();
                                        MaritalStatusTask();
                                        BloodGroupTask();

                                        //Appointment related data
                                        masterdataflag = masterFlagAdapter.listCurrent();
                                        masterdataflag.setMsg("Synchronizing Unit Master");
                                        masterFlagAdapter.create(masterdataflag);
                                        UnitMasterTask();

                                        masterdataflag = masterFlagAdapter.listCurrent();
                                        masterdataflag.setMsg("Synchronizing Appointment Reason");
                                        masterFlagAdapter.create(masterdataflag);
                                        AppointmentReasonTask();

                                        masterdataflag = masterFlagAdapter.listCurrent();
                                        masterdataflag.setMsg("Synchronizing Complaints ");
                                        masterFlagAdapter.create(masterdataflag);
                                        ComplaintTask();

                                        masterdataflag = masterFlagAdapter.listCurrent();
                                        masterdataflag.setMsg("Synchronizing Department ");
                                        masterFlagAdapter.create(masterdataflag);
                                        DepartmentTask();

                                        masterdataflag = masterFlagAdapter.listCurrent();
                                        masterdataflag.setMsg("Synchronizing Specialization ");
                                        masterFlagAdapter.create(masterdataflag);
                                        SpecializationTask();
                                        DoctorTypeTask();

                                        //Vital data
                                        masterdataflag = masterFlagAdapter.listCurrent();
                                        masterdataflag.setMsg("Synchronizing Vital");
                                        masterFlagAdapter.create(masterdataflag);
                                        VitalTask();

                                        //medicine related data
                                        masterdataflag = masterFlagAdapter.listCurrent();
                                        masterdataflag.setMsg("Synchronizing Medicine Route");
                                        masterFlagAdapter.create(masterdataflag);
                                        MedicienRouteTask();

                                        masterdataflag = masterFlagAdapter.listCurrent();
                                        masterdataflag.setMsg("Synchronizing Medicine Frequency");
                                        masterFlagAdapter.create(masterdataflag);
                                        MedicineFrequencyTask();

                                        masterdataflag = masterFlagAdapter.listCurrent();
                                        masterdataflag.setMsg("Synchronizing Medicine Instruction");
                                        masterFlagAdapter.create(masterdataflag);
                                        MedicienInstructionTask();

//                                        masterdataflag = masterFlagAdapter.listCurrent();
//                                        masterdataflag.setMsg("Synchronizing Medicine Name");
//                                        masterFlagAdapter.create(masterdataflag);
//                                        MedicienNameTask();

                                        /*masterdataflag = masterFlagAdapter.listCurrent();
                                        masterdataflag.setMsg("Synchronizing Molecules ");
                                        masterFlagAdapter.create(masterdataflag);
                                        MoleculeTask();
*/
                                        //Diagnosis data
                                        masterdataflag = masterFlagAdapter.listCurrent();
                                        masterdataflag.setMsg("Synchronizing Diagnosis Type");
                                        masterFlagAdapter.create(masterdataflag);
                                        DaignosisTypeTask();
                                        //DiagnosisTask();

                                        //Service data
                                        masterdataflag = masterFlagAdapter.listCurrent();
                                        masterdataflag.setMsg("Synchronizing Service");
                                        masterFlagAdapter.create(masterdataflag);
                                        PriorityTask();
                                        //ServiceNameTask();

                                        masterdataflag = masterFlagAdapter.listCurrent();
                                        masterdataflag.setMsg("Synchronizing Completed.");
                                        masterFlagAdapter.create(masterdataflag);

                                        //ControlCaptionTask();
                                        masterdataflag = masterFlagAdapter.listCurrent();
                                        masterdataflag.setFlag(Constants.STOP_TASK);
                                        masterFlagAdapter.create(masterdataflag);
                                        break;
                                    case Constants.STOP_TASK:
                                        break;
                                }
                            }
                        }
                    }
                }
            }
            notificationMessage = new NotificationMessage(
                    contextWrapper.getResources().getString(R.string.app_name),
                    "Data synchronization complete.")
                    .setNotificationSettings(true, false, false);
            notificationMessage.notificationIconResource = R.mipmap.ic_launcher;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskResult;
    }

    public void AppointmentReasonTask() {
        try {
            responseCode = 0;
            responseString = null;
            Count = appointmentReasonAdapter.TotalCount();
            Log.d(Constants.TAG, "Appointment Reason Local size : " + Count);
            if (synchronizationList.get(0).getAppointmentReasonCount() != null && (!synchronizationList.get(0).getAppointmentReasonCount().equals("")) && (!synchronizationList.get(0).getAppointmentReasonCount().equals(String.valueOf(Count)))) {
                // Appointment reason list task
                response = serviceConsumer.GET(Constants.GET_APPOINTMENT_REASON_URL);
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    if (responseCode == Constants.HTTP_OK_200) {
                        appointmentReasonList = objectMapper.map(responseString, AppointmentReason.class);
                        Log.d(Constants.TAG, "AppointmentReasonTask size : " + appointmentReasonList.size());
                        appointmentReasonAdapter.delete();
                        if (appointmentReasonList != null && appointmentReasonList.size() > 0) {
                            for (int index = 0; index < appointmentReasonList.size(); index++) {
                                appointmentReasonAdapter.create(appointmentReasonList.get(index));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void UnitMasterTask() {
        try {
            responseCode = 0;
            responseString = null;
            Count = unitMasterAdapter.TotalCount();
            Log.d(Constants.TAG, "Unit Master Local size : " + Count);
            if ((synchronizationList.get(0).getUnitMasterCount() != null) && (!synchronizationList.get(0).getUnitMasterCount().equals("")) && (!synchronizationList.get(0).getUnitMasterCount().equals(String.valueOf(Count)))) {
                response = serviceConsumer.GET(Constants.GET_UNIT_MASTER_URL + doctorProfileList.get(0).getID());
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    if (responseCode == Constants.HTTP_OK_200) {
                        unitMasterList = objectMapper.map(responseString, ELUnitMaster.class);
                        Log.d(Constants.TAG, "Unit Master Task size : " + unitMasterList.size());
                        unitMasterAdapter.delete();
                        if (unitMasterList != null && unitMasterList.size() > 0) {
                            for (int index = 0; index < unitMasterList.size(); index++) {
                                unitMasterAdapter.create(unitMasterList.get(index));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SpecializationTask() {
        try {
            responseCode = 0;
            responseString = null;
            Count = specializationAdapter.TotalCount();
            Log.d(Constants.TAG, "SpecializationTask Local size : " + Count);
            if (synchronizationList.get(0).getSpecializationCount() != null && (!synchronizationList.get(0).getSpecializationCount().equals("")) && (!synchronizationList.get(0).getSpecializationCount().equals(String.valueOf(Count)))) {
                //Specialization Task
                response = serviceConsumer.GET(Constants.GET_SPECIALIZATION_URL);
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    if (responseCode == Constants.HTTP_OK_200) {
                        specializationList = objectMapper.map(responseString, Specialization.class);
                        Log.d(Constants.TAG, "SpecializationTask size : " + specializationList.size());
                        specializationAdapter.delete();
                        if (specializationList != null && specializationList.size() > 0) {
                            for (int index = 0; index < specializationList.size(); index++) {
                                specializationAdapter.create(specializationList.get(index));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void DoctorTypeTask() {
        try {
            responseCode = 0;
            responseString = null;
            Count = doctorTypeAdapter.TotalCount();
            Log.d(Constants.TAG, "DoctorType Task Local size : " + Count);
            if (synchronizationList.get(0).getDoctorTypeCount() != null && (!synchronizationList.get(0).getDoctorTypeCount().equals("")) && (!synchronizationList.get(0).getDoctorTypeCount().equals(String.valueOf(Count)))) {
                //Doctor type list
                response = serviceConsumer.GET(Constants.GET_DOCTOR_TYPE_URL);
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    if (responseCode == Constants.HTTP_OK_200) {
                        doctorTypeList = objectMapper.map(responseString, DoctorType.class);
                        Log.d(Constants.TAG, "DoctorTypeTask size : " + doctorTypeList.size());
                        doctorTypeAdapter.delete();
                        if (doctorTypeList != null && doctorTypeList.size() > 0) {
                            for (int index = 0; index < doctorTypeList.size(); index++) {
                                doctorTypeAdapter.create(doctorTypeList.get(index));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void DepartmentTask() {
        try {
            responseCode = 0;
            responseString = null;
            Count = departmentAdapter.TotalCount();
            Log.d(Constants.TAG, "Department Task Local size : " + Count);
            if (synchronizationList.get(0).getDepartmentCount() != null && (!synchronizationList.get(0).getDepartmentCount().equals("")) && (!synchronizationList.get(0).getDepartmentCount().equals(String.valueOf(Count)))) {
                //Department list task
                response = serviceConsumer.GET(Constants.GET_DEPARTMENT_URL + doctorProfileList.get(0).getDoctorID() + "&UnitID=" + doctorProfileList.get(0).getUnitID());
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    if (responseCode == Constants.HTTP_OK_200) {
                        departmentList = objectMapper.map(responseString, Department.class);
                        Log.d(Constants.TAG, "DepartmentTask size : " + departmentList.size());
                        departmentAdapter.delete();
                        if (departmentList != null && departmentList.size() > 0) {
                            for (int index = 0; index < departmentList.size(); index++) {
                                departmentAdapter.create(departmentList.get(index));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ComplaintTask() {
        try {
            responseCode = 0;
            responseString = null;
            Count = complaintAdapter.TotalCount();
            Log.d(Constants.TAG, "ComplaintTask Local size : " + Count);
            if (synchronizationList.get(0).getComplaintCount() != null && (!synchronizationList.get(0).getComplaintCount().equals("")) && (!synchronizationList.get(0).getComplaintCount().equals(String.valueOf(Count)))) {
                //Complaint list task
                response = serviceConsumer.GET(Constants.GET_COMPLAINT_URL);
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    if (responseCode == Constants.HTTP_OK_200) {
                        complaintList = objectMapper.map(responseString, Complaint.class);
                        Log.d(Constants.TAG, "ComplaintTask size : " + complaintList.size());
                        complaintAdapter.delete();
                        if (complaintList != null && complaintList.size() > 0) {
                            for (int index = 0; index < complaintList.size(); index++) {
                                complaintAdapter.create(complaintList.get(index));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void MedicienRouteTask() {
        try {
            responseCode = 0;
            responseString = null;
            Count = medicienRouteAdapter.TotalCount();
            Log.d(Constants.TAG, "Medicine RouteTask Local size : " + Count);
            if (synchronizationList.get(0).getMedicienRouteCount() != null && (!synchronizationList.get(0).getMedicienRouteCount().equals("")) && (!synchronizationList.get(0).getMedicienRouteCount().equals(String.valueOf(Count)))) {
                //MedicineRoute list task
                response = serviceConsumer.GET(Constants.MEDICIENROUTE_URL);
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    if (responseCode == Constants.HTTP_OK_200) {
                        medicienRouteList = objectMapper.map(responseString, MedicienRoute.class);
                        Log.d(Constants.TAG, "MedicineRouteTask size : " + medicienRouteList.size());
                        medicienRouteAdapter.delete();
                        if (medicienRouteList != null && medicienRouteList.size() > 0) {
                            for (int index = 0; index < medicienRouteList.size(); index++) {
                                medicienRouteAdapter.create(medicienRouteList.get(index));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void MedicineFrequencyTask() {
        try {
            responseCode = 0;
            responseString = null;
            Count = medicienFrequencyAdapter.TotalCount();
            Log.d(Constants.TAG, "MedicineFrequencyTask Local size : " + Count);
            if (synchronizationList.get(0).getMedicienFrequencyCount() != null && (!synchronizationList.get(0).getMedicienFrequencyCount().equals("")) && (!synchronizationList.get(0).getMedicienFrequencyCount().equals(String.valueOf(Count)))) {

                response = serviceConsumer.GET(Constants.MEDICIENFREQUENCY_URL);
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    if (responseCode == Constants.HTTP_OK_200) {
                        medicienFrequencyList = objectMapper.map(responseString, MedicienFrequency.class);
                        Log.d(Constants.TAG, "MedicineFrequencyTask size : " + medicienFrequencyList.size());
                        medicienFrequencyAdapter.delete();
                        if (medicienFrequencyList != null && medicienFrequencyList.size() > 0) {
                            for (int index = 0; index < medicienFrequencyList.size(); index++) {
                                medicienFrequencyAdapter.create(medicienFrequencyList.get(index));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void VitalTask() {
        try {
            responseCode = 0;
            responseString = null;
            Count = vitalAdapter.TotalCount();
            Log.d(Constants.TAG, "VitalTask Local size : " + Count);
            if (synchronizationList.get(0).getVitalCount() != null && (!synchronizationList.get(0).getVitalCount().equals("")) && (!synchronizationList.get(0).getVitalCount().equals(String.valueOf(Count)))) {
                //Vital list task
                response = serviceConsumer.GET(Constants.VITAL_URL);
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    if (responseCode == Constants.HTTP_OK_200) {
                        vitalList = objectMapper.map(responseString, Vital.class);
                        Log.d(Constants.TAG, "VitalTask size : " + vitalList.size());
                        vitalAdapter.delete();
                        if (vitalList != null && vitalList.size() > 0) {
                            for (int index = 0; index < vitalList.size(); index++) {
                                vitalAdapter.create(vitalList.get(index));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void DaignosisTypeTask() {
        try {
            responseCode = 0;
            responseString = null;
            Count = daignosisTypeMasterAdapter.TotalCount();
            Log.d(Constants.TAG, "DiagnosisType Task Local size : " + Count);
            if (synchronizationList.get(0).getDaignosisTypeCount() != null && (!synchronizationList.get(0).getDaignosisTypeCount().equals("")) && (!synchronizationList.get(0).getDaignosisTypeCount().equals(String.valueOf(Count)))) {
                //DiagnosisTypeMaster list task
                response = serviceConsumer.GET(Constants.DAIGNOSISTYPEMASTER_URL);
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    if (responseCode == Constants.HTTP_OK_200) {
                        daignosisTypeMasterList = objectMapper.map(responseString, DaignosisTypeMaster.class);
                        Log.d(Constants.TAG, "Diagnosis TypeTask size : " + daignosisTypeMasterList.size());
                        daignosisTypeMasterAdapter.delete();
                        if (daignosisTypeMasterList != null && daignosisTypeMasterList.size() > 0) {
                            for (int index = 0; index < daignosisTypeMasterList.size(); index++) {
                                daignosisTypeMasterAdapter.create(daignosisTypeMasterList.get(index));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void PriorityTask() {
        try {
            responseCode = 0;
            responseString = null;
            Count = priorityAdapter.TotalCount();
            Log.d(Constants.TAG, "PriorityTask Local size : " + Count);
            if (synchronizationList.get(0).getPriorityCount() != null && (!synchronizationList.get(0).getPriorityCount().equals("")) && (!synchronizationList.get(0).getPriorityCount().equals(String.valueOf(Count)))) {
                //Priority list task
                response = serviceConsumer.GET(Constants.PRIORITY_URL);
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    if (responseCode == Constants.HTTP_OK_200) {
                        priorityList = objectMapper.map(responseString, Priority.class);
                        Log.d(Constants.TAG, "PriorityTask size : " + priorityList.size());
                        priorityAdapter.delete();
                        if (priorityList != null && priorityList.size() > 0) {
                            for (int index = 0; index < priorityList.size(); index++) {
                                priorityAdapter.create(priorityList.get(index));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void MedicienInstructionTask() {
        try {
            responseCode = 0;
            responseString = null;
            Count = medicienInstructionAdapter.TotalCount();
            Log.d(Constants.TAG, "Medicine InstructionTask Local size : " + Count);
            if (synchronizationList.get(0).getMedicienInstructionCount() != null && (!synchronizationList.get(0).getMedicienInstructionCount().equals("")) && (!synchronizationList.get(0).getMedicienInstructionCount().equals(String.valueOf(Count)))) {
                //Medicine Instruction list task
                response = serviceConsumer.GET(Constants.MEDICIENINSTRUCTION_URL);
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    if (responseCode == Constants.HTTP_OK_200) {
                        medicienInstructionList = objectMapper.map(responseString, MedicienInstruction.class);
                        Log.d(Constants.TAG, "Medicine InstructionTask size : " + medicienInstructionList.size());
                        medicienInstructionAdapter.delete();
                        if (medicienInstructionList != null && medicienInstructionList.size() > 0) {
                            for (int index = 0; index < medicienInstructionList.size(); index++) {
                                medicienInstructionAdapter.create(medicienInstructionList.get(index));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void PrefixTask() {
        try {
            responseCode = 0;
            responseString = null;
            Count = prefixAdapter.TotalCount();
            Log.d(Constants.TAG, "Prefix Task Local size : " + Count);
            if (synchronizationList.get(0).getPrefixCount() != null && (!synchronizationList.get(0).getPrefixCount().equals("")) && (!synchronizationList.get(0).getPrefixCount().equals(String.valueOf(Count)))) {
                //Prefix list task
                response = serviceConsumer.GET(Constants.GET_PREFIX_URL);
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    if (responseCode == Constants.HTTP_OK_200) {
                        prefixList = objectMapper.map(responseString, Prefix.class);
                        Log.d(Constants.TAG, "PrefixTask size:" + prefixList.size());
                        prefixAdapter.delete();
                        if (prefixList != null && prefixList.size() > 0) {
                            for (int index = 0; index < prefixList.size(); index++) {
                                prefixAdapter.create(prefixList.get(index));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GenderTask() {
        try {
            responseCode = 0;
            responseString = null;
            Count = genderAdapter.TotalCount();
            Log.d(Constants.TAG, "Gender Task Local size : " + Count);
            if (synchronizationList.get(0).getGenderCount() != null && (!synchronizationList.get(0).getGenderCount().equals("")) && (!synchronizationList.get(0).getGenderCount().equals(String.valueOf(Count)))) {
                //Gender list task
                response = serviceConsumer.GET(Constants.GET_GENDER_URL);
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    if (responseCode == Constants.HTTP_OK_200) {
                        genderList = objectMapper.map(responseString, Gender.class);
                        Log.d(Constants.TAG, "GenderTask size : " + genderList.size());
                        genderAdapter.delete();
                        if (genderList != null && genderList.size() > 0) {
                            for (int index = 0; index < genderList.size(); index++) {
                                genderAdapter.create(genderList.get(index));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void MaritalStatusTask() {
        try {
            responseCode = 0;
            responseString = null;
            Count = maritalStatusAdapter.TotalCount();
            Log.d(Constants.TAG, "MaritalStatus Task Local size : " + Count);
            if (synchronizationList.get(0).getMaritalStatusCount() != null && (!synchronizationList.get(0).getMaritalStatusCount().equals("")) && (!synchronizationList.get(0).getMaritalStatusCount().equals(String.valueOf(Count)))) {
                //Marital status list
                response = serviceConsumer.GET(Constants.GET_MARITAL_STATUS_URL);
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    if (responseCode == Constants.HTTP_OK_200) {
                        maritalStatusList = objectMapper.map(responseString, MaritalStatus.class);
                        Log.d(Constants.TAG, "MaritalStatusTask size : " + maritalStatusList.size());
                        maritalStatusAdapter.delete();
                        if (maritalStatusList != null && maritalStatusList.size() > 0) {
                            for (int index = 0; index < maritalStatusList.size(); index++) {
                                maritalStatusAdapter.create(maritalStatusList.get(index));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void BloodGroupTask() {
        try {
            responseCode = 0;
            responseString = null;
            Count = bloodGroupAdapter.TotalCount();
            Log.d(Constants.TAG, "bloodGroup List Local size : " + Count);
            if (synchronizationList.get(0).getBloodGroupCount() != null && (!synchronizationList.get(0).getBloodGroupCount().equals("")) && (!synchronizationList.get(0).getBloodGroupCount().equals(String.valueOf(Count)))) {
                //Bloodgroup list task
                response = serviceConsumer.GET(Constants.GET_BLOODGROUP_URL);
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    if (responseCode == Constants.HTTP_OK_200) {
                        bloodGroupList = objectMapper.map(responseString, BloodGroup.class);
                        Log.d(Constants.TAG, "bloodGroup List size : " + bloodGroupList.size());
                        bloodGroupAdapter.delete();
                        if (bloodGroupList != null && bloodGroupList.size() > 0) {
                            for (int index = 0; index < bloodGroupList.size(); index++) {
                                bloodGroupAdapter.create(bloodGroupList.get(index));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*public void MedicienNameTask() {
        try {
            responseCode = 0;
            responseString = null;
            Count = medicienNameAdapter.TotalCount();
            Log.d(Constants.TAG, "MedicineName Task Local size : " + Count);
            if ((!synchronizationList.get(0).getMedicienNameCount().equals("")) && (!synchronizationList.get(0).getMedicienNameCount().equals(String.valueOf(Count)))) {
                //Medicine Name list task
                response = serviceConsumer.GET(Constants.MEDICIENNAME_URL + "?SearchText=" + "");
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    if (responseCode == Constants.HTTP_OK_200) {
                        medicienNameList = objectMapper.map(responseString, MedicienName.class);
                        Log.d(Constants.TAG, "MedicienNameTask size : " + medicienNameList.size());
                        medicienNameAdapter.delete();
                        if (medicienNameList != null && medicienNameList.size() > 0) {
                            for (int index = 0; index < medicienNameList.size(); index++) {
                                medicienNameAdapter.create(medicienNameList.get(index));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void MoleculeTask() {
        try {
            responseCode = 0;
            responseString = null;
            Count = moleculeAdapter.TotalCount();
            Log.d(Constants.TAG, "MoleculeTask Local size : " + Count);
            if ((!synchronizationList.get(0).getMoleculeCount().equals("")) && (!synchronizationList.get(0).getMoleculeCount().equals(String.valueOf(Count)))) {
                //Molecule list task
                response = serviceConsumer.GET(Constants.MOLECULE_URL+ "?SearchText=" + "");
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    if (responseCode == Constants.HTTP_OK_200) {
                        moleculeList = objectMapper.map(responseString, Molecule.class);
                        Log.d(Constants.TAG, "MoleculeTask size : " + moleculeList.size());
                        moleculeAdapter.delete();
                        if (moleculeList != null && moleculeList.size() > 0) {
                            for (int index = 0; index < moleculeList.size(); index++) {
                                moleculeAdapter.create(moleculeList.get(index));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void DiagnosisTask() {
        try {
            responseCode = 0;
            responseString = null;
            Count = daignosisMasterAdapter.TotalCount();
            Log.d(Constants.TAG, "Diagnosis Task Local size : " + Count);
            if ((!synchronizationList.get(0).getDaignosisCount().equals("")) && (!synchronizationList.get(0).getDaignosisCount().equals(String.valueOf(Count)))) {
                //Diagnosis Master list task
                response = serviceConsumer.GET(Constants.DAIGNOSISMASTER_URL);
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    if (responseCode == Constants.HTTP_OK_200) {
                        daignosisMasterList = objectMapper.map(responseString, DaignosisMaster.class);
                        Log.d(Constants.TAG, "DaignosisTask size : " + daignosisMasterList.size());
                        daignosisMasterAdapter.delete();
                        if (daignosisMasterList != null && daignosisMasterList.size() > 0) {
                            //daignosisMasterAdapter.
                            for (int index = 0; index < daignosisMasterList.size(); index++) {
                                daignosisMasterAdapter.create(daignosisMasterList.get(index));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ServiceNameTask() {
        try {
            responseCode = 0;
            responseString = null;
            Count = serviceNameAdapter.TotalCount();
            Log.d(Constants.TAG, "ServiceNameTask Local size : " + Count);
            if ((!synchronizationList.get(0).getServiceNameCount().equals("")) && (!synchronizationList.get(0).getServiceNameCount().equals(String.valueOf(Count)))) {
                //ServiceName list task
                response = serviceConsumer.GET(Constants.SERVICENAME_URL);
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    if (responseCode == Constants.HTTP_OK_200) {
                        serviceNameList = objectMapper.map(responseString, ServiceName.class);
                        Log.d(Constants.TAG, "ServiceNameTask size : " + serviceNameList.size());
                        serviceNameAdapter.delete();
                        if (serviceNameList != null && serviceNameList.size() > 0) {
                            for (int index = 0; index < serviceNameList.size(); index++) {
                                serviceNameAdapter.create(serviceNameList.get(index));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public String getTitle() {
        return "SynchronizationTask";
    }

    @Override
    public String getId() {
        return "SynchronizationTask";
    }
}
