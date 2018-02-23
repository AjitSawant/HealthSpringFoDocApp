package com.palash.healthspringapp.utilities;

public class Constants {
    public static final String TAG = "HealthSpring";
    public static final String STATUS_LOG_IN = "login";
    public static final String STATUS_LOG_OUT = "logout";
    public static final String KEY_REQUEST_DATA = "request_data";
    public static final String KEY_ForRefreshData = "thisIsForRefreshData";

    public static final int HTTP_OK_200 = 200;
    public static final int HTTP_CREATED_201 = 201;
    public static final int HTTP_DELETED_OK_204 = 204;
    public static final int HTTP_NO_RECORD_FOUND_OK_204 = 204;
    public static final int HTTP_NOT_FOUND_401 = 401;
    public static final int HTTP_NOT_OK_404 = 404;
    public static final int HTTP_NOT_OK_500 = 500;
    public static final int HTTP_NOT_OK_501 = 501;
    public static final int HTTP_AMBIGUOUS_300 = 300;
    public static final int HTTP_Expectation_Failed_417 = 417;
    public static final int FILTER_DAYS_COUNT = 7;
    public static int IsVitals = 1;

    public static boolean refreshPatient = false;
    public static boolean backFromAddEMR = false;
    public static boolean isFromLogin = false;

    //localhost Test URL
    public static final String BASE_URL = "http://192.168.1.133/HealthSpringDocApp/";

    // Test URL
    //public static final String BASE_URL = "http://103.229.5.99/HealthSpringDocApp/";
    public static final String PATIENT_CONSOLE_DOCUMENT_FTP_PATH_URL = "http://ftp://103.229.5.99:2121";
    public static final String PATIENT_VISIT_SUMMARY_URL = "http://103.229.5.99/healthspring_test/Reports/EMR/PatientEMRReport.aspx?UnitID=";
    public static final String Patient_PRESCRIPTION_URL = "http://103.229.5.99/healthspring_test/Reports/Patient/EMRPatientPrescription.aspx?UnitID=";
    public static final String Patient_ICE_URL = "http://103.229.5.99/healthspring_test/Reports/OPD/PatientEmergencyForm.aspx?UnitID=";
    public static final String FTP_Link = "ftp://103.229.5.99:2121/&FtpUserName=customerexperience@healthspring.in&FtpPassword=Health$pring&MrNumber=";

    //Live URL
    /*public static final String BASE_URL = "http://103.229.5.91/HealthSpringDocApp/";
    public static final String PATIENT_CONSOLE_DOCUMENT_FTP_PATH_URL = "http://ftp://103.229.5.91:2121";
    public static final String PATIENT_VISIT_SUMMARY_URL = "http://103.229.5.91/healthspring/Reports/EMR/PatientEMRReport.aspx?UnitID=";
    public static final String Patient_PRESCRIPTION_URL = "http://103.229.5.91/healthspring/Reports/Patient/EMRPatientPrescription.aspx?UnitID=";
    public static final String Patient_ICE_URL = "http://103.229.5.91/healthspring/Reports/OPD/PatientEmergencyForm.aspx?UnitID=";
    public static final String FTP_Link = "ftp://103.229.5.91:2121/&FtpUserName=HSFTP&FtpPassword=Rational@1&MrNumber=";*/

    public static final String LOGIN_URL = BASE_URL + "Profile/DoctorProfile";
    public static final String APP_VERSION_URL = BASE_URL + "Master/AppVersion";
    public static final String PATIENT_LIST_URL = BASE_URL + "Patient/PatientList";
    public static final String TIME_SLOT_URL = BASE_URL + "timeslot/timeslot";
    public static final String PATIENT_REGISTRATION_URL = BASE_URL + "Patient/PatientRegistration";

    public static final String SYNCHRONIZATION_URL = BASE_URL + "Synchronization/Count";
    public static final String GET_GENDER_URL = BASE_URL + "Master/GetGender";
    public static final String GET_MARITAL_STATUS_URL = BASE_URL + "Master/GetMaritalStatus";
    public static final String UPDATE_PASSWORD_URL = BASE_URL + "Profile/UpdatePassword";
    public static final String GET_PREFIX_URL = BASE_URL + "Master/GetPrefix";
    public static final String GET_DOCTOR_TYPE_URL = BASE_URL + "Master/GetDoctorType";
    public static final String GET_UNIT_MASTER_URL = BASE_URL + "Master/GetUnitMaster?ID=";
    public static final String GET_SPECIALIZATION_URL = BASE_URL + "Master/GetSpecilization";
    public static final String GET_APPOINTMENT_URL = BASE_URL + "Appointment/GetApointmentList";
    public static final String GET_APPOINTMENT_REASON_URL = BASE_URL + "Master/GetAppointmentReason";
    public static final String GET_DEPARTMENT_URL = BASE_URL + "Master/GetDepartment?DoctorID=";
    public static final String GET_COMPLAINT_URL = BASE_URL + "Master/GetComplaint";
    public static final String GET_BLOODGROUP_URL = BASE_URL + "Master/GetBloodGropMaster";
    public static final String BOOK_APPOINTMENT_URL = BASE_URL + "Appointment/AppointmentBook";
    public static final String RECHEDUAL_APPOINTMENT_URL = BASE_URL + "Appointment/RescheduleAppointment";
    public static final String CANCLE_APPOINTMENT_URL = BASE_URL + "Appointment/CancleAppointment";
    public static final String VISIT_MARK_APPOINTMENT_URL = BASE_URL + "Visit/VisitMarkAdd";
    public static final String VISIT_LISIT_URL = BASE_URL + "Visit/VisitList?UnitID=";
    public static final String PATIENT_QUEUE_URL = BASE_URL + "Patient/PatientQueue";
    public static final String VISIT_BOOK_URL = BASE_URL + "Visit/VisitBook";
    public static final String MEDICIENNAME_URL = BASE_URL + "Master/GetMedicienName";
    public static final String MEDICIENROUTE_URL = BASE_URL + "Master/GetMedicienRoute";
    public static final String MEDICIENFREQUENCY_URL = BASE_URL + "Master/GetMedicienFrequency";
    public static final String MEDICIENINSTRUCTION_URL = BASE_URL + "Master/GetMedicienInstruction";
    public static final String VITAL_URL = BASE_URL + "Master/GetVital";
    public static final String DAIGNOSISMASTER_URL = BASE_URL + "Master/GetDaignosisMaster";
    public static final String DAIGNOSISTYPEMASTER_URL = BASE_URL + "Master/GetDaignosisTypeMaster";
    public static final String SERVICENAME_URL = BASE_URL + "Master/GetServiceName";
    public static final String PRIORITY_URL = BASE_URL + "Master/GetPriority";
    public static final String DIAGNOSIS_ADD_UPDATE_URL = BASE_URL + "DiagnosisList/DiagnosisListAddUpDate";
    public static final String MULTI_VITALS_ADD_UPDATE_URL = BASE_URL + "VitalsList/AddMultiVitalsList";
    public static final String CPOESERVICE_ADD_UPDATE_URL = BASE_URL + "CPOEService/AddUpdateCPOEService";
    public static final String MOLECULE_URL = BASE_URL + "Master/GetMolecule";
    public static final String COUNTRY_URL = BASE_URL + "Master/GetCountry";
    public static final String REGION_URL = BASE_URL + "Master/GetRegion";
    public static final String STATE_URL = BASE_URL + "Master/GetState";
    public static final String CITY_URL = BASE_URL + "Master/GetCity";
    public static final String PATIENT_CATEGORY_L1_URL = BASE_URL + "Master/GetPatientCategory";
    public static final String PATIENT_CATEGORY_L2_URL = BASE_URL + "Master/GetPatientCategoryL2?CategoryL1ID=";
    public static final String PATIENT_CATEGORY_L3_URL = BASE_URL + "Master/GetPatientCategoryL3?CategoryL2ID=";
    public static final String DOCTOR_NAME_URL = BASE_URL + "Master/GetDoctorName";
    public static final String PCPDOCTOR_URL = BASE_URL + "Master/GetPCPDoctor?UnitID=";
    public static final String COMPANY_NAME_URL = BASE_URL + "Master/GetCompanyList";
    public static final String GET_PACKAGE_VALIDITY_URL = BASE_URL + "Master/GetPackageValidity?TariffID=";
    public static final String HEALTHSPRING_REFERRAL_URL = BASE_URL + "Master/GetHealthspringReferral";
    public static final String CPOEMEDICINE_ADD_UPDATE_URL = BASE_URL + "CPOEMedicine/AddCPOEMedicine";
    public static final String COMPLAINTS_ADD_UPDATE_URL = BASE_URL + "Complaint/AddUpdateComplaint";
    public static final String FOLLOWUP_ADD_UPDATE_URL = BASE_URL + "FollowUp/AddUpdateFollowUp";
    public static final String GET_FOLLOWUP_LIST_URL = BASE_URL + "FollowUp/GetFollowUpList?UnitID=";
    public static final String GET_PAST_MEDICAL_HISTORY_CE_LIST_URL = BASE_URL + "Patient/GetPastMedicalHistoryForICE?UnitID=";
    public static final String REFERRAL_DOCTOR_LIST_MASTER_PER_SERVICE_URL = BASE_URL + "Referral/ListReferralDoctorAsperService";
    public static final String REFERRAL_DOCTOR_ADD_UPDATE_PER_SERVICE_URL = BASE_URL + "Referral/AddUpdateReferralDoctorService";

    //-------------******* Using PatientID EMR URL *******----------------//
    public static final String DIAGNOSIS_PATIENT_LIST_URL = BASE_URL + "DiagnosisList/DiagnosisPatientList?UnitID=";
    public static final String VITALS_PATIENT_LIST_URL = BASE_URL + "VitalsList/VitalsPateintList?UnitID=";
    public static final String CPOESERVICE_PATIENT_LIST_URL = BASE_URL + "CPOEService/ListPatientCPOEService?UnitID=";
    public static final String CPOEMEDICINE_PATIENT_LIST_URL = BASE_URL + "CPOEMedicine/ListPatientCPOEMedicine?UnitID=";
    public static final String COMPLAINT_LIST_URL = BASE_URL + "Complaint/ListPatientComplaints?UnitID=";
    public static final String REFERRAL_DOCTOR_EMR_LIST_PER_SERVICE_URL = BASE_URL + "Referral/ListReferralDoctorService?UnitID=";
    public static final String GET_PATIENT_CONSOLE_LIST_URL = BASE_URL + "Patient/ListPatientConsole?UnitID=";

    public static final String REQUIRED_MSG = "Required !!!";
    public static final String SEARCH_DATE_FORMAT = "yyyy-MM-dd";
    public static final String PATIENT_QUEUE_DATE = "dd MMM yyyy";
    public static final String DATE_FORMAT = "yyyy-MM-dd hh:mm aa";
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String OFFLINE_DATE = "dd MMM yyyy";
    public static final String OFFLINE_TIME = "hh:mm aa";

    public static final int PATIENT_REGISTRATION_TASK = 16;
    public static final int BOOK_APPOINTMENT_TASK = 17;
    public static final int EMR_TASK = 88;
    public static final int EMR_MEDICINE_MASTER_TASK = 19;
    public static final int EMR_DIAGNOSIS_MASTER_TASK = 20;
    public static final int EMR_SERVICE_MASTER_TASK = 21;
    public static final int EMR_VITAL_MASTER_TASK = 22;
    public static final int EMR_COMPLAINT_MASTER_TASK = 23;
    public static final int EMR_DEPARTMENT_MASTER_TASK = 24;
    public static final int STOP_TASK = 99;
    public static final int ALL_URL_TASK = 0;
    public static final int ONLINE_SYNC = 10;
}
