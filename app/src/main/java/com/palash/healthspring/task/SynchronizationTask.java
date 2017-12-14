package com.palash.healthspring.task;

import android.content.ContextWrapper;
import android.util.Log;

import com.buzzbox.mob.android.scheduler.NotificationMessage;
import com.buzzbox.mob.android.scheduler.Task;
import com.buzzbox.mob.android.scheduler.TaskResult;
import com.palash.healthspring.R;
import com.palash.healthspring.api.JsonObjectMapper;
import com.palash.healthspring.api.WebServiceConsumer;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.Appointment;
import com.palash.healthspring.entity.BookAppointment;
import com.palash.healthspring.entity.CPOEPrescription;
import com.palash.healthspring.entity.CPOEService;
import com.palash.healthspring.entity.ComplaintsList;
import com.palash.healthspring.entity.DiagnosisList;
import com.palash.healthspring.entity.DoctorProfile;
import com.palash.healthspring.entity.Flag;
import com.palash.healthspring.entity.PatientQueue;
import com.palash.healthspring.entity.ReferralDoctorPerService;
import com.palash.healthspring.entity.Synchronization;
import com.palash.healthspring.entity.VisitList;
import com.palash.healthspring.entity.VitalsList;
import com.palash.healthspring.entity.VitalsListSave;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SynchronizationTask implements Task {

    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.AppointmentAdapter appointmentAdapter;
    //private DatabaseAdapter.PatientQueueAdapter patientQueueAdapter;
    //private DatabaseAdapter.VisitListAdapter visitListAdapter;
    private DatabaseAdapter.DiagnosisListAdapter diagnosisListAdapter;
    private DatabaseAdapter.VitalsListAdapter vitalsListAdapter;
    private DatabaseAdapter.CPOEServiceAdapter cpoeServiceAdapter;
    private DatabaseAdapter.CPOEMedicineAdapter cpoePrescriptionAdapter;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapterDB;
    private DatabaseAdapter.ComplaintsListDBAdapter complaintsListDBAdapter;
    private DatabaseAdapter.ReferralServiceListDBAdapter referralServiceListDBAdapter;
    private DatabaseAdapter.FlagAdapter flagAdapter;

    private WebServiceConsumer serviceConsumer;
    private Response response;
    private JsonObjectMapper objectMapper;
    private TaskResult taskResult;

    private ArrayList<DoctorProfile> doctorProfileList;
    private ArrayList<Appointment> appointmentList;
    private ArrayList<PatientQueue> patientQueueList;
    private ArrayList<VisitList> visitList;
    private ArrayList<Synchronization> synchronizationList;
    private ArrayList<DiagnosisList> diagnosisList;
    private ArrayList<VitalsList> vitalsList;
    private ArrayList<CPOEService> cpoeServiceList;
    private ArrayList<CPOEPrescription> cpoePrescriptionList;
    private ArrayList<ComplaintsList> complaintsListList;
    private ArrayList<ReferralDoctorPerService> referralDoctorPerServiceList;
    private ArrayList<Flag> flagList;
    private ArrayList<BookAppointment> bookAppointmentArrayList;

    private NotificationMessage notificationMessage;
    private Synchronization synchronization;

    private String responseString;
    private int responseCode;
    private int patientQueueCount;
    private int visitCount;

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
            appointmentAdapter = databaseAdapter.new AppointmentAdapter();
            //patientQueueAdapter = databaseAdapter.new PatientQueueAdapter();
            //visitListAdapter = databaseAdapter.new VisitListAdapter();
            diagnosisListAdapter = databaseAdapter.new DiagnosisListAdapter();
            vitalsListAdapter = databaseAdapter.new VitalsListAdapter();
            cpoeServiceAdapter = databaseAdapter.new CPOEServiceAdapter();
            cpoePrescriptionAdapter = databaseAdapter.new CPOEMedicineAdapter();
            complaintsListDBAdapter = databaseAdapter.new ComplaintsListDBAdapter();
            referralServiceListDBAdapter = databaseAdapter.new ReferralServiceListDBAdapter();
            flagAdapter = databaseAdapter.new FlagAdapter();
            flagList = flagAdapter.listLast();

            bookAppointmentAdapterDB = databaseAdapter.new BookAppointmentAdapter();
            bookAppointmentArrayList = bookAppointmentAdapterDB.listLast();

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
                    if (responseCode == Constants.HTTP_OK_200) {
                        synchronizationList = objectMapper.map(responseString, Synchronization.class);
                        if (synchronizationList != null && synchronizationList.size() > 0) {
                            if (flagList != null && flagList.size() > 0) {
                                switch (flagList.get(0).getFlag()) {
                                    case Constants.ONLINE_SYNC:
                                        SyncVitalsOfflineList();
                                        SyncDiagnosisOfflineList();
                                        SyncServiceOfflineList();
                                        SyncPrescriptionOfflineList();
                                        SyncComplaintsOfflineList();
                                        SyncReferalServiceOfflineList();
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
        }
        return taskResult;
    }

    private void SyncVitalsOfflineList() {
        try {
            responseCode = 0;
            responseString = null;

            ArrayList<VitalsList> visitTotalList = vitalsListAdapter.listAllVisitUnSync();   // return distinct visit from vitals table
            for (int k = 0; k < visitTotalList.size(); k++) {
                vitalsList = vitalsListAdapter.listAllUnSync(visitTotalList.get(k).getVisitID());   // return vitals list from visit
                if (vitalsList != null && vitalsList.size() > 0) {

                    VitalsListSave elSaveVitalsList = new VitalsListSave();
                    elSaveVitalsList.setUnitID(vitalsList.get(0).getUnitID());
                    elSaveVitalsList.setPatientID(vitalsList.get(0).getPatientID());
                    elSaveVitalsList.setPatientUnitID(vitalsList.get(0).getUnitID());
                    elSaveVitalsList.setVisitID(vitalsList.get(0).getVisitID());
                    elSaveVitalsList.setDoctorID(vitalsList.get(0).getDoctorID());
                    elSaveVitalsList.setDate(vitalsList.get(0).getDate());
                    elSaveVitalsList.setTime(vitalsList.get(0).getTime());
                    elSaveVitalsList.setAllvitalsList(vitalsList);

                    try {
                        Log.d(Constants.TAG, "Data Synchronising start date:" + DateFormat.getDateTimeInstance().format(new Date()));
                        response = serviceConsumer.POST(Constants.MULTI_VITALS_ADD_UPDATE_URL, objectMapper.unMap(elSaveVitalsList));
                        Log.d(Constants.TAG, "Data Synchronising end date:" + DateFormat.getDateTimeInstance().format(new Date()));
                        if (response != null) {
                            responseString = response.body().string();
                            responseCode = response.code();
                            Log.d(Constants.TAG, "ResponseString:" + responseString);
                            Log.d(Constants.TAG, "ResponseCode:" + responseCode);
                            if (responseCode == Constants.HTTP_OK_200 || responseCode == Constants.HTTP_CREATED_201) {
                                vitalsListAdapter.delete(visitTotalList.get(k).getPatientID(), visitTotalList.get(k).getVisitID());
                                for (int i = 0; i < vitalsList.size(); i++) {
                                    vitalsListAdapter.UpdateSyncLocalItem(vitalsList.get(i));  // reset isynch status
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SyncDiagnosisOfflineList() {
        try {
            responseCode = 0;
            responseString = null;
            diagnosisList = diagnosisListAdapter.listAllUnSync();
            if (diagnosisList != null && diagnosisList.size() > 0) {
                for (int i = 0; i < diagnosisList.size(); i++) {
                    try {
                        Log.d(Constants.TAG, "Data Synchronising start date:" + DateFormat.getDateTimeInstance().format(new Date()));
                        response = serviceConsumer.POST(Constants.DIAGNOSIS_ADD_UPDATE_URL, objectMapper.unMap(diagnosisList.get(i)));
                        Log.d(Constants.TAG, "Data Synchronising end date:" + DateFormat.getDateTimeInstance().format(new Date()));
                        if (response != null) {
                            responseString = response.body().string();
                            responseCode = response.code();
                            Log.d(Constants.TAG, "ResponseString:" + responseString);
                            Log.d(Constants.TAG, "ResponseCode:" + responseCode);
                            if (responseCode == Constants.HTTP_OK_200 || responseCode == Constants.HTTP_CREATED_201) {
                                ArrayList<DiagnosisList> elDiagnosisList = objectMapper.map(responseString, DiagnosisList.class);
                                diagnosisListAdapter.UpdateSyncLocalItem(diagnosisList.get(i).get_ID(), elDiagnosisList.get(0));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SyncServiceOfflineList() {
        try {
            responseCode = 0;
            responseString = null;
            cpoeServiceList = cpoeServiceAdapter.listAllUnSync();
            if (cpoeServiceList != null && cpoeServiceList.size() > 0) {
                for (int i = 0; i < cpoeServiceList.size(); i++) {
                    try {
                        Log.d(Constants.TAG, "Data Synchronising start date:" + DateFormat.getDateTimeInstance().format(new Date()));
                        response = serviceConsumer.POST(Constants.CPOESERVICE_ADD_UPDATE_URL, objectMapper.unMap(cpoeServiceList.get(i)));
                        Log.d(Constants.TAG, "Data Synchronising end date:" + DateFormat.getDateTimeInstance().format(new Date()));
                        if (response != null) {
                            responseString = response.body().string();
                            responseCode = response.code();
                            Log.d(Constants.TAG, "ResponseString:" + responseString);
                            Log.d(Constants.TAG, "ResponseCode:" + responseCode);
                            if (responseCode == Constants.HTTP_CREATED_201 || responseCode == Constants.HTTP_OK_200) {
                                ArrayList<CPOEService> elCPOEServiceList = objectMapper.map(responseString, CPOEService.class);
                                cpoeServiceAdapter.UpdateSyncLocalItem(cpoeServiceList.get(i).get_ID(), elCPOEServiceList.get(0));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SyncPrescriptionOfflineList() {
        try {
            responseCode = 0;
            responseString = null;
            cpoePrescriptionList = cpoePrescriptionAdapter.listAllUnSync();
            if (cpoePrescriptionList != null && cpoePrescriptionList.size() > 0) {
                for (int i = 0; i < cpoePrescriptionList.size(); i++) {
                    try {
                        Log.d(Constants.TAG, "Data Synchronising start date:" + DateFormat.getDateTimeInstance().format(new Date()));
                        response = serviceConsumer.POST(Constants.CPOEMEDICINE_ADD_UPDATE_URL, objectMapper.unMap(cpoePrescriptionList.get(i)));
                        Log.d(Constants.TAG, "Data Synchronising end date:" + DateFormat.getDateTimeInstance().format(new Date()));
                        if (response != null) {
                            responseString = response.body().string();
                            responseCode = response.code();
                            Log.d(Constants.TAG, "ResponseString:" + responseString);
                            Log.d(Constants.TAG, "ResponseCode:" + responseCode);
                            if (responseCode == Constants.HTTP_OK_200 || responseCode == Constants.HTTP_CREATED_201) {
                                ArrayList<CPOEPrescription> elCPOEPrescriptionList = objectMapper.map(responseString, CPOEPrescription.class);
                                cpoePrescriptionAdapter.UpdateSyncLocalItem(cpoePrescriptionList.get(i).get_ID(), elCPOEPrescriptionList.get(0));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SyncComplaintsOfflineList() {
        try {
            responseCode = 0;
            responseString = null;
            complaintsListList = complaintsListDBAdapter.listAllUnSync();
            if (complaintsListList != null && complaintsListList.size() > 0) {
                for (int i = 0; i < complaintsListList.size(); i++) {
                    try {
                        Log.d(Constants.TAG, "Data Synchronising start date:" + DateFormat.getDateTimeInstance().format(new Date()));
                        response = serviceConsumer.POST(Constants.COMPLAINTS_ADD_UPDATE_URL, objectMapper.unMap(complaintsListList.get(i)));
                        Log.d(Constants.TAG, "Data Synchronising end date:" + DateFormat.getDateTimeInstance().format(new Date()));
                        if (response != null) {
                            responseString = response.body().string();
                            responseCode = response.code();
                            Log.d(Constants.TAG, "ResponseString:" + responseString);
                            Log.d(Constants.TAG, "ResponseCode:" + responseCode);
                            if (responseCode == Constants.HTTP_OK_200 || responseCode == Constants.HTTP_CREATED_201) {
                                ArrayList<ComplaintsList> elComplaintsList = objectMapper.map(responseString, ComplaintsList.class);
                                complaintsListDBAdapter.UpdateSyncLocalItem(complaintsListList.get(i).get_ID(), elComplaintsList.get(0));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SyncReferalServiceOfflineList() {
        try {
            responseCode = 0;
            responseString = null;
            referralDoctorPerServiceList = referralServiceListDBAdapter.listAllUnSync();
            if (referralDoctorPerServiceList != null && referralDoctorPerServiceList.size() > 0) {
                for (int i = 0; i < referralDoctorPerServiceList.size(); i++) {
                    try {
                        Log.d(Constants.TAG, "Data Synchronising start date:" + DateFormat.getDateTimeInstance().format(new Date()));
                        response = serviceConsumer.POST(Constants.REFERRAL_DOCTOR_ADD_UPDATE_PER_SERVICE_URL, objectMapper.unMap(referralDoctorPerServiceList.get(i)));
                        Log.d(Constants.TAG, "Data Synchronising end date:" + DateFormat.getDateTimeInstance().format(new Date()));
                        if (response != null) {
                            responseString = response.body().string();
                            responseCode = response.code();
                            Log.d(Constants.TAG, "ResponseString:" + responseString);
                            Log.d(Constants.TAG, "ResponseCode:" + responseCode);
                            if (responseCode == Constants.HTTP_OK_200 || responseCode == Constants.HTTP_CREATED_201) {
                                ArrayList<ReferralDoctorPerService> referralServiceList = objectMapper.map(responseString, ReferralDoctorPerService.class);
                                referralServiceListDBAdapter.UpdateSyncLocalItem(referralDoctorPerServiceList.get(i).get_ID(), referralServiceList.get(0));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void AppointmentListTask() {
        try {
            responseCode = 0;
            responseString = null;
            //Appointment list task
            response = serviceConsumer.GET(Constants.GET_APPOINTMENT_URL + doctorProfileList.get(0).getDoctorID());
            if (response != null) {
                responseString = response.body().string();
                responseCode = response.code();
                if (responseCode == Constants.HTTP_OK_200) {
                    //responseString = localSetting.cleanResponseString(responseString);
                    appointmentList = objectMapper.map(responseString, Appointment.class);
                    if (appointmentList != null && appointmentList.size() > 0) {
                        for (int index = 0; index < appointmentList.size(); index++) {
                            appointmentAdapter.create(appointmentList.get(index));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

   /* public void PatientQueueTask() {
        try {
            responseCode = 0;
            responseString = null;
            String Patientname = null;
            patientQueueCount = patientQueueAdapter.CountToday(Patientname);
            if (Integer.parseInt(synchronizationList.get(0).getPatientQueueCount()) != patientQueueCount) {
                //Patient Queue list task
                response = serviceConsumer.GET(Constants.PATIENT_QUEUE_URL + doctorProfileList.get(0).getUnitID());
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    if (responseCode == Constants.HTTP_OK_200) {
                        responseString = localSetting.cleanResponseString(responseString);
                        patientQueueList = objectMapper.map(responseString, PatientQueue.class);
                        if (patientQueueList != null && patientQueueList.size() > 0) {
                            patientQueueAdapter.Updatelist(Patientname);
                            for (int index = 0; index < patientQueueList.size(); index++) {
                                patientQueueAdapter.create(patientQueueList.get(index));
                            }
                        }
                    } else if (responseCode == Constants.HTTP_DELETED_OK_204) {
                        patientQueueAdapter.Updatelist(Patientname);
                        patientQueueAdapter.delete();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /*public void VisitListTask() {
        try {
            responseCode = 0;
            responseString = null;
            visitCount = visitListAdapter.TotalCount();
            if (Integer.parseInt(synchronizationList.get(0).getVisitCount()) > visitCount) {
                //Visit list task
                response = serviceConsumer.GET(Constants.VISIT_LISIT_URL + doctorProfileList.get(0).getUnitID() + "&PatientID=" +
                        bookAppointmentArrayList.get(0).getPatientID());
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    if (responseCode == Constants.HTTP_OK_200) {
                        responseString = localSetting.cleanResponseString(responseString);
                        visitList = objectMapper.map(responseString, VisitList.class);
                        if (visitList != null && visitList.size() != visitCount) {
                            for (int index = 0; index < visitList.size(); index++) {
                                visitListAdapter.create(visitList.get(index));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

   /* public void EMRVitalsTask() {
        try {
            responseCode = 0;
            responseString = null;
            //Patient list task
            response = serviceConsumer.GET(Constants.VITALS_PATIENT_LIST_URL + doctorProfileList.get(0).getUnitID()
                    + "&PatientID=" +
                    bookAppointmentArrayList.get(0).getVisitID());
            if (response != null) {
                responseString = response.body().string();
                responseCode = response.code();
                if (responseCode == Constants.HTTP_OK_200) {
                    responseString = localSetting.cleanResponseString(responseString);
                    vitalsList = objectMapper.map(responseString, VitalsList.class);
                    if (vitalsList != null && vitalsList.size() > 0) {
                        for (int index = 0; index < vitalsList.size(); index++) {
                            vitalsListAdapter.create(vitalsList.get(index));
                        }
                    }
                } else if (responseCode == Constants.HTTP_DELETED_OK_204) {
                    vitalsListAdapter.delete(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /*public void EMRDiagnosisTask() {
        try {
            responseCode = 0;
            responseString = null;
            //DIAGNOSIS list task
            response = serviceConsumer.GET(Constants.DIAGNOSIS_PATIENT_LIST_URL + doctorProfileList.get(0).getUnitID() + "&PatientID=" +
                    bookAppointmentArrayList.get(0).getPatientID());
            if (response != null) {
                responseString = response.body().string();
                responseCode = response.code();
                if (responseCode == Constants.HTTP_OK_200) {
                    responseString = localSetting.cleanResponseString(responseString);
                    diagnosisList = objectMapper.map(responseString, DiagnosisList.class);
                    if (diagnosisList != null && diagnosisList.size() > 0) {
                        diagnosisListAdapter.Updatelist(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                        for (int index = 0; index < diagnosisList.size(); index++) {
                            diagnosisListAdapter.create(diagnosisList.get(index));
                        }
                    }
                } else if (responseCode == Constants.HTTP_DELETED_OK_204) {
                    diagnosisListAdapter.Updatelist(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                    diagnosisListAdapter.delete(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /*public void EMRCPOEServiceTask() {
        try {
            responseCode = 0;
            responseString = null;
            //CPOEService list task
            response = serviceConsumer.GET(Constants.CPOESERVICE_PATIENT_LIST_URL + doctorProfileList.get(0).getUnitID()
                    + "&PatientID=" +
                    bookAppointmentArrayList.get(0).getPatientID());
            if (response != null) {
                responseString = response.body().string();
                responseCode = response.code();
                if (responseCode == Constants.HTTP_OK_200) {
                    responseString = localSetting.cleanResponseString(responseString);
                    cpoeServiceList = objectMapper.map(responseString, CPOEService.class);
                    if (cpoeServiceList != null && cpoeServiceList.size() > 0) {
                        cpoeServiceAdapter.Updatelist(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                        for (int index = 0; index < cpoeServiceList.size(); index++) {
                            cpoeServiceAdapter.create(cpoeServiceList.get(index));
                        }
                    }
                } else if (responseCode == Constants.HTTP_DELETED_OK_204) {
                    cpoeServiceAdapter.Updatelist(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                    cpoeServiceAdapter.delete(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /*public void EMRCPOEMedicineTask() {
        try {
            responseCode = 0;
            responseString = null;
            //CPOEMedicine list task
            response = serviceConsumer.GET(Constants.CPOEMEDICINE_PATIENT_LIST_URL + doctorProfileList.get(0).getUnitID()
                    + "&PatientID=" +
                    bookAppointmentArrayList.get(0).getPatientID());
            if (response != null) {
                responseString = response.body().string();
                responseCode = response.code();
                if (responseCode == Constants.HTTP_OK_200) {
                    responseString = localSetting.cleanResponseString(responseString);
                    cpoePrescriptionList = objectMapper.map(responseString, CPOEPrescription.class);
                    if (cpoePrescriptionList != null && cpoePrescriptionList.size() > 0) {
                        cpoePrescriptionAdapter.Updatelist(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                        for (int index = 0; index < cpoePrescriptionList.size(); index++) {
                            cpoePrescriptionAdapter.create(cpoePrescriptionList.get(index));
                        }
                    }
                } else if (responseCode == Constants.HTTP_DELETED_OK_204) {
                    cpoePrescriptionAdapter.Updatelist(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                    cpoePrescriptionAdapter.delete(bookAppointmentArrayList.get(0).getPatientID(), bookAppointmentArrayList.get(0).getVisitID());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /*private void SyncVitalsOfflineList() {
        try {
            responseCode = 0;
            responseString = null;
            vitalsList = vitalsListAdapter.listAllUnSync();
            if (vitalsList != null && vitalsList.size() > 0) {
                for (int i = 0; i < vitalsList.size(); i++) {
                    try {
                        Log.d(Constants.TAG, "Data Synchronising start date:" + DateFormat.getDateTimeInstance().format(new Date()));
                        response = serviceConsumer.POST(Constants.VITALS_ADD_UPDATE_URL, objectMapper.unMap(vitalsList.get(i)));
                        Log.d(Constants.TAG, "Data Synchronising end date:" + DateFormat.getDateTimeInstance().format(new Date()));
                        if (response != null) {
                            responseString = response.body().string();
                            responseCode = response.code();
                            Log.d(Constants.TAG, "ResponseString:" + responseString);
                            Log.d(Constants.TAG, "ResponseCode:" + responseCode);
                            if (responseCode == Constants.HTTP_CREATED_201) {
                                vitalsListAdapter.RemoveSyncItem(vitalsList.get(i).get_ID());
                            } else if (responseCode == Constants.HTTP_OK_200) {
                                vitalsListAdapter.RemoveSyncItem(vitalsList.get(i).get_ID());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
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
