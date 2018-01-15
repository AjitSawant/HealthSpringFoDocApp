package com.palash.healthspringapp.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.palash.healthspringapp.utilities.Constants;

public class DatabaseContract {

    public static final String DATABASE_NAME = "ppd.db";

    public static final int DATABASE_VERSION = 1;

    public static final class SynchOfflineData implements BaseColumns {

        private SynchOfflineData() {
        }

        public static final String TABLE_NAME = "T_SynchOfflineData";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_OFFLINE_DATE = "OfflineLastDate";
        public static final String COLUMN_NAME_VERSION_CODE = "VersionCode";
        public static final String COLUMN_NAME_VERSION_NAME = "VersionName";
        public static final String COLUMN_NAME_OFFLINE_STATUS = "OfflineStatus";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + SynchOfflineData._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_OFFLINE_DATE
                + " TEXT,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_VERSION_CODE
                + " TEXT,"
                + COLUMN_NAME_VERSION_NAME
                + " TEXT,"
                + COLUMN_NAME_OFFLINE_STATUS
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class DoctorProfile implements BaseColumns {

        private DoctorProfile() {
        }

        public static final String TABLE_NAME = "T_DoctorProfile";
        public static final String DEFAULT_SORT_ORDER = "_id DESC";

        public static final String COLUMN_NAME_DOCTOR_ID = "DoctorID";
        public static final String COLUMN_NAME_LOGIN_NAME = "LoginName";
        public static final String COLUMN_NAME_PASSWORD = "Password";
        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_UNITID = "UnitID";
        public static final String COLUMN_NAME_DEPARTMENTID = "DepartmentID";
        public static final String COLUMN_NAME_UNITNAME = "UnitName";
        public static final String COLUMN_NAME_FIRST_NAME = "FirstName";
        public static final String COLUMN_NAME_MIDDLE_NAME = "MiddleName";
        public static final String COLUMN_NAME_LAST_NAME = "LastName";
        public static final String COLUMN_NAME_GENDER = "Gender";
        public static final String COLUMN_NAME_EMPLOYEENUMBER = "EmployeeNumber";
        public static final String COLUMN_NAME_DOB = "DOB";
        public static final String COLUMN_NAME_EDUCATION = "Education";
        public static final String COLUMN_NAME_EXPERIENCE = "Experience";
        public static final String COLUMN_NAME_SPECIALIZTION = "Specialization";
        public static final String COLUMN_NAME_SUBSPECIALIZTION = "SubSpecialization";
        public static final String COLUMN_NAME_DOCTORTYPE = "DoctorType";
        public static final String COLUMN_NAME_EMAIL_ID = "EmailId";
        public static final String COLUMN_NAME_PFNUMBER = "PFNumber";
        public static final String COLUMN_NAME_PANNUMBER = "PANNumber";
        public static final String COLUMN_NAME_DATEOFJOINING = "DateOfJoining";
        public static final String COLUMN_NAME_ACCESSCARDNUMBER = "AccessCardNumber";
        public static final String COLUMN_NAME_PHOTO = "Photo";
        public static final String COLUMN_NAME_DIGITALSIGNATURE = "DigitalSignature";
        public static final String COLUMN_NAME_STATUS = "Status";
        public static final String COLUMN_NAME_SYNCHRONIZED = "Synchronized";
        public static final String COLUMN_NAME_REGESTRATIONNUMBER = "RegestrationNumber";
        public static final String COLUMN_NAME_MARITAL_STATUS = "MaritialStatus";
        public static final String COLUMN_NAME_REMEMBER_ME = "RememberMe";
        public static final String COLUMN_NAME_LOGIN_STATUS = "LoginStatus";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + DoctorProfile._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_DOCTOR_ID
                + " TEXT,"
                + COLUMN_NAME_LOGIN_NAME
                + " TEXT,"
                + COLUMN_NAME_PASSWORD
                + " TEXT,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_UNITID
                + " TEXT,"
                + COLUMN_NAME_DEPARTMENTID
                + " TEXT,"
                + COLUMN_NAME_UNITNAME
                + " TEXT,"
                + COLUMN_NAME_FIRST_NAME
                + " TEXT,"
                + COLUMN_NAME_MIDDLE_NAME
                + " TEXT,"
                + COLUMN_NAME_LAST_NAME
                + " TEXT,"
                + COLUMN_NAME_GENDER
                + " TEXT,"
                + COLUMN_NAME_EMPLOYEENUMBER
                + " TEXT,"
                + COLUMN_NAME_DOB
                + " TEXT,"
                + COLUMN_NAME_EDUCATION
                + " TEXT,"
                + COLUMN_NAME_EXPERIENCE
                + " TEXT,"
                + COLUMN_NAME_SPECIALIZTION
                + " TEXT,"
                + COLUMN_NAME_SUBSPECIALIZTION
                + " TEXT,"
                + COLUMN_NAME_DOCTORTYPE
                + " TEXT,"
                + COLUMN_NAME_EMAIL_ID
                + " TEXT,"
                + COLUMN_NAME_PFNUMBER
                + " TEXT,"
                + COLUMN_NAME_PANNUMBER
                + " TEXT,"
                + COLUMN_NAME_DATEOFJOINING
                + " TEXT,"
                + COLUMN_NAME_ACCESSCARDNUMBER
                + " TEXT,"
                + COLUMN_NAME_PHOTO
                + " TEXT,"
                + COLUMN_NAME_DIGITALSIGNATURE
                + " TEXT,"
                + COLUMN_NAME_STATUS
                + " TEXT,"
                + COLUMN_NAME_SYNCHRONIZED
                + " TEXT,"
                + COLUMN_NAME_REGESTRATIONNUMBER
                + " TEXT,"
                + COLUMN_NAME_MARITAL_STATUS
                + " TEXT,"
                + COLUMN_NAME_REMEMBER_ME
                + " TEXT,"
                + COLUMN_NAME_LOGIN_STATUS
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class DoctorType implements BaseColumns {

        private DoctorType() {
        }

        public static final String TABLE_NAME = "T_DoctorType";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + DoctorType._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_DESCRIPTION
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class UnitMaster implements BaseColumns {

        private UnitMaster() {
        }

        public static final String TABLE_NAME = "T_UnitMaster";

        public static final String COLUMN_NAME_UNITID = "UnitID";
        public static final String COLUMN_NAME_UNIT_CODE = "UnitCode";
        public static final String COLUMN_NAME_UNIT_DESC = "UnitDesc";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + UnitMaster._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_UNITID
                + " TEXT,"
                + COLUMN_NAME_UNIT_CODE
                + " TEXT,"
                + COLUMN_NAME_UNIT_DESC
                + " TEXT"
                + ")";
    }

    public static final class Specialization implements BaseColumns {

        private Specialization() {
        }

        public static final String TABLE_NAME = "T_Specialization";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + Specialization._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_DESCRIPTION
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class AppointmentReason implements BaseColumns {

        private AppointmentReason() {
        }

        public static final String TABLE_NAME = "T_AppointmentReason";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_COLORCODE = "ColorCode";
        public static final String COLUMN_NAME_SERVICEID = "ServiceID";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + AppointmentReason._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_DESCRIPTION
                + " TEXT,"
                + COLUMN_NAME_COLORCODE
                + " TEXT,"
                + COLUMN_NAME_SERVICEID
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class Department implements BaseColumns {

        private Department() {
        }

        public static final String TABLE_NAME = "T_Department";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + Department._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_DESCRIPTION
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class Complaint implements BaseColumns {

        private Complaint() {
        }

        public static final String TABLE_NAME = "T_Complaint";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + Complaint._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_DESCRIPTION
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class BookAppointment implements BaseColumns {

        private BookAppointment() {
        }

        public static final String TABLE_NAME = "T_BookAppointment";
        public static final String DEFAULT_SORT_ORDER = "_id DESC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_UNIT_ID = "UnitID";
        public static final String COLUMN_NAME_PATIENT_ID = "PatientID";
        public static final String COLUMN_NAME_MRNo = "MRNo";
        public static final String COLUMN_NAME_AGE = "Age";
        public static final String COLUMN_NAME_REGISTRATION_DATE = "RegistrationDate";
        public static final String COLUMN_NAME_CLINIC_NAME = "ClinicName";
        public static final String COLUMN_NAME_FIRST_NAME = "FirstName";
        public static final String COLUMN_NAME_MIDDLE_NAME = "MiddleName";
        public static final String COLUMN_NAME_LAST_NAME = "LastName";
        public static final String COLUMN_NAME_GENDER_ID = "GenderID";
        public static final String COLUMN_NAME_DOB = "DOB";
        public static final String COLUMN_NAME_MATRITAL_STATUS_ID = "MaritalStatusID";
        public static final String COLUMN_NAME_MATRITAL_STATUS = "MaritalStatus";
        public static final String COLUMN_NAME_CONTACT1 = "Contact1";
        public static final String COLUMN_NAME_EMAIL_ID = "EmailId";
        public static final String COLUMN_NAME_DEPARTMENT_ID = "DepartmentID";
        public static final String COLUMN_NAME_DOCTOR_ID = "DoctorID";
        public static final String COLUMN_NAME_DOCTOR_UNIT_ID = "DoctorUnitID";
        public static final String COLUMN_NAME_DOCTOR_NAME = "DoctorName";
        public static final String COLUMN_NAME_SPECIALIZATION = "Specialization";
        public static final String COLUMN_NAME_DOCTOR_EDUCATION = "Education";
        public static final String COLUMN_NAME_DOCTOR_MOBILE_NO = "DoctorMobileNo";
        public static final String COLUMN_NAME_APPOINTMENT_REASON_ID = "AppointmentReasonID";
        public static final String COLUMN_NAME_APPOINTMENT_DATE = "AppointmentDate";
        public static final String COLUMN_NAME_FROM_TIME = "FromTime";
        public static final String COLUMN_NAME_TO_TIME = "ToTime";
        public static final String COLUMN_NAME_REMARK = "Remark";
        public static final String COLUMN_NAME_COMPLAINT_ID = "ComplaintId";
        public static final String COLUMN_NAME_BLOOD_GROUP_ID = "BloodGroupID";
        public static final String COLUMN_NAME_RESCHEDULINGREASON = "ReSchedulingReason";
        public static final String COLUMN_NAME_APPOINTMENTID = "AppointmentId";
        public static final String COLUMN_NAME_VISITID = "VisitID";
        public static final String COLUMN_NAME_VISIT_TYPEID = "VisitTypeID";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + BookAppointment._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_UNIT_ID
                + " TEXT,"
                + COLUMN_NAME_PATIENT_ID
                + " TEXT,"
                + COLUMN_NAME_MRNo
                + " TEXT,"
                + COLUMN_NAME_AGE
                + " TEXT,"
                + COLUMN_NAME_REGISTRATION_DATE
                + " TEXT,"
                + COLUMN_NAME_CLINIC_NAME
                + " TEXT,"
                + COLUMN_NAME_FIRST_NAME
                + " TEXT,"
                + COLUMN_NAME_MIDDLE_NAME
                + " TEXT,"
                + COLUMN_NAME_LAST_NAME
                + " TEXT,"
                + COLUMN_NAME_GENDER_ID
                + " TEXT,"
                + COLUMN_NAME_DOB
                + " TEXT,"
                + COLUMN_NAME_MATRITAL_STATUS_ID
                + " TEXT,"
                + COLUMN_NAME_MATRITAL_STATUS
                + " TEXT,"
                + COLUMN_NAME_CONTACT1
                + " TEXT,"
                + COLUMN_NAME_EMAIL_ID
                + " TEXT,"
                + COLUMN_NAME_DEPARTMENT_ID
                + " TEXT,"
                + COLUMN_NAME_DOCTOR_ID
                + " TEXT,"
                + COLUMN_NAME_DOCTOR_UNIT_ID
                + " TEXT,"
                + COLUMN_NAME_DOCTOR_NAME
                + " TEXT,"
                + COLUMN_NAME_SPECIALIZATION
                + " TEXT,"
                + COLUMN_NAME_DOCTOR_EDUCATION
                + " TEXT,"
                + COLUMN_NAME_DOCTOR_MOBILE_NO
                + " TEXT,"
                + COLUMN_NAME_APPOINTMENT_REASON_ID
                + " TEXT,"
                + COLUMN_NAME_APPOINTMENT_DATE
                + " TEXT,"
                + COLUMN_NAME_FROM_TIME
                + " TEXT,"
                + COLUMN_NAME_TO_TIME
                + " TEXT,"
                + COLUMN_NAME_REMARK
                + " TEXT,"
                + COLUMN_NAME_COMPLAINT_ID
                + " TEXT,"
                + COLUMN_NAME_BLOOD_GROUP_ID
                + " TEXT,"
                + COLUMN_NAME_RESCHEDULINGREASON
                + " TEXT,"
                + COLUMN_NAME_APPOINTMENTID
                + " TEXT,"
                + COLUMN_NAME_VISITID
                + " TEXT,"
                + COLUMN_NAME_VISIT_TYPEID
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class Appointment implements BaseColumns {

        private Appointment() {
        }

        public static final String TABLE_NAME = "T_Appointment";
        public static final String DEFAULT_SORT_ORDER = "Datetime(SortingDateTime) ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_UNIT_ID = "UnitID";
        public static final String COLUMN_NAME_UNIT_NAME = "UnitName";
        public static final String COLUMN_NAME_PATIENT_ID = "PatientID";
        public static final String COLUMN_NAME_PATIENT_UNIT_ID = "PatientUnitID";
        public static final String COLUMN_NAME_VISIT_ID = "VisitID";
        public static final String COLUMN_NAME_FIRST_NAME = "FirstName";
        public static final String COLUMN_NAME_MIDDLE_NAME = "MiddleName";
        public static final String COLUMN_NAME_LAST_NAME = "LastName";
        public static final String COLUMN_NAME_DOB = "DOB";
        public static final String COLUMN_NAME_GENDER = "Gender";
        public static final String COLUMN_NAME_BLOOD_GROUP = "BloodGroup";
        public static final String COLUMN_NAME_MATRITAL_STATUS = "MaritalStatus";
        public static final String COLUMN_NAME_GENDER_ID = "GenderID";
        public static final String COLUMN_NAME_BLOOD_GROUP_ID = "BloodGroupID";
        public static final String COLUMN_NAME_MARITAL_STATUS_ID = "MaritalStatusID";
        public static final String COLUMN_NAME_CONTACT1 = "Contact1";
        public static final String COLUMN_NAME_EMAIL_ID = "EmailId";
        public static final String COLUMN_NAME_DEPARTMENT = "Department";
        public static final String COLUMN_NAME_DEPARTMENT_ID = "DepartmentID";
        public static final String COLUMN_NAME_DOCTOR_ID = "DoctorID";
        public static final String COLUMN_NAME_APPOINTMENT_REASON = "AppointmentReason";
        public static final String COLUMN_NAME_APPOINTMENT_REASON_ID = "AppointmentReasonID";
        public static final String COLUMN_NAME_APPOINTMENT_DATE = "AppointmentDate";
        public static final String COLUMN_NAME_FROM_TIME = "FromTime";
        public static final String COLUMN_NAME_TO_TIME = "ToTime";
        public static final String COLUMN_NAME_SEARCH_APPOINTMENT_DATE = "SearchAppointmentDate";
        public static final String COLUMN_NAME_REMARK = "Remark";
        public static final String COLUMN_NAME_APP_CANCEL_REASON = "AppCancelReason";
        public static final String COLUMN_NAME_STATUS = "Status";
        public static final String COLUMN_NAME_CREATED_UNIT_ID = "CreatedUnitID";
        public static final String COLUMN_NAME_VISIT_MARK = "VisitMark";
        public static final String COLUMN_NAME_APPOINTMENT_STATUS = "AppointmentStatus";
        public static final String COLUMN_NAME_PARENT_APPOINT_ID = "ParentappointID";
        public static final String COLUMN_NAME_PARENT_APPOINT_UNIT_ID = "ParentappointUnitID";
        public static final String COLUMN_NAME_RESCHEDUlING_REASON = "ReSchedulingReason";
        public static final String COLUMN_NAME_COMPLAINT = "Complaint";
        public static final String COLUMN_NAME_COMPLAINT_ID = "ComplaintId";
        public static final String COLUMN_NAME_SORTINGDATETIME = "SortingDateTime";
        public static final String COLUMN_NAME_DrName = "DrName";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + Appointment._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_UNIT_ID
                + " TEXT,"
                + COLUMN_NAME_UNIT_NAME
                + " TEXT,"
                + COLUMN_NAME_PATIENT_ID
                + " TEXT,"
                + COLUMN_NAME_PATIENT_UNIT_ID
                + " TEXT,"
                + COLUMN_NAME_VISIT_ID
                + " TEXT,"
                + COLUMN_NAME_FIRST_NAME
                + " TEXT,"
                + COLUMN_NAME_MIDDLE_NAME
                + " TEXT,"
                + COLUMN_NAME_LAST_NAME
                + " TEXT,"
                + COLUMN_NAME_GENDER
                + " TEXT,"
                + COLUMN_NAME_BLOOD_GROUP
                + " TEXT,"
                + COLUMN_NAME_DOB
                + " TEXT,"
                + COLUMN_NAME_MATRITAL_STATUS
                + " TEXT,"
                + COLUMN_NAME_GENDER_ID
                + " TEXT,"
                + COLUMN_NAME_BLOOD_GROUP_ID
                + " TEXT,"
                + COLUMN_NAME_MARITAL_STATUS_ID
                + " TEXT,"
                + COLUMN_NAME_CONTACT1
                + " TEXT,"
                + COLUMN_NAME_EMAIL_ID
                + " TEXT,"
                + COLUMN_NAME_DEPARTMENT
                + " TEXT,"
                + COLUMN_NAME_DEPARTMENT_ID
                + " TEXT,"
                + COLUMN_NAME_DOCTOR_ID
                + " TEXT,"
                + COLUMN_NAME_APPOINTMENT_REASON
                + " TEXT,"
                + COLUMN_NAME_APPOINTMENT_REASON_ID
                + " TEXT,"
                + COLUMN_NAME_APPOINTMENT_DATE
                + " TEXT,"
                + COLUMN_NAME_SEARCH_APPOINTMENT_DATE
                + " TEXT,"
                + COLUMN_NAME_REMARK
                + " TEXT,"
                + COLUMN_NAME_APP_CANCEL_REASON
                + " TEXT,"
                + COLUMN_NAME_STATUS
                + " TEXT,"
                + COLUMN_NAME_FROM_TIME
                + " TEXT,"
                + COLUMN_NAME_TO_TIME
                + " TEXT,"
                + COLUMN_NAME_CREATED_UNIT_ID
                + " TEXT,"
                + COLUMN_NAME_RESCHEDUlING_REASON
                + " TEXT,"
                + COLUMN_NAME_VISIT_MARK
                + " TEXT,"
                + COLUMN_NAME_APPOINTMENT_STATUS
                + " TEXT,"
                + COLUMN_NAME_PARENT_APPOINT_ID
                + " TEXT,"
                + COLUMN_NAME_PARENT_APPOINT_UNIT_ID
                + " TEXT,"
                + COLUMN_NAME_COMPLAINT
                + " TEXT,"
                + COLUMN_NAME_COMPLAINT_ID
                + " TEXT,"
                + COLUMN_NAME_SORTINGDATETIME
                + " TEXT,"
                + COLUMN_NAME_DrName
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class Patient implements BaseColumns {

        private Patient() {
        }

        public static final String TABLE_NAME = "T_Patient";
        public static final String DEFAULT_SORT_ORDER = "FirstName ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_UNITID = "UnitID";
        public static final String COLUMN_NAME_PATIENTCATEGORYID = "PatientCategoryID";
        public static final String COLUMN_NAME_MRNO = "MRNo";
        public static final String COLUMN_NAME_CLINIC = "ClinicName";
        public static final String COLUMN_NAME_REGISTRATIONDATE = "RegistrationDate";
        public static final String COLUMN_NAME_LAST_NAME = "LastName";
        public static final String COLUMN_NAME_FIRST_NAME = "FirstName";
        public static final String COLUMN_NAME_MIDDLE_NAME = "MiddleName";
        public static final String COLUMN_NAME_FIMLIY_NAME = "FamilyName";
        public static final String COLUMN_NAME_GENDER = "Gender";
        public static final String COLUMN_NAME_DATEOFBIRTH = "DateOfBirth";
        public static final String COLUMN_NAME_EDUCATION = "Education";
        public static final String COLUMN_NAME_MARITALSTATUS = "MaritalStatus";
        public static final String COLUMN_NAME_CONTACTNO1 = "ContactNo1";
        public static final String COLUMN_NAME_EMAIL = "Email";
        public static final String COLUMN_NAME_AGE = "Age";
        public static final String COLUMN_NAME_GENDERID = "GenderID";
        public static final String COLUMN_NAME_MARITALSTATUSID = "MaritalStatusID";
        public static final String COLUMN_NAME_BLOOD_GROUP_ID = "BloodGroupID";
        public static final String COLUMN_NAME_BLOOD_GROUP = "BloodGroup";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + Patient._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_UNITID
                + " TEXT,"
                + COLUMN_NAME_PATIENTCATEGORYID
                + " TEXT,"
                + COLUMN_NAME_MRNO
                + " TEXT,"
                + COLUMN_NAME_CLINIC
                + " TEXT,"
                + COLUMN_NAME_REGISTRATIONDATE
                + " TEXT,"
                + COLUMN_NAME_FIRST_NAME
                + " TEXT,"
                + COLUMN_NAME_MIDDLE_NAME
                + " TEXT,"
                + COLUMN_NAME_LAST_NAME
                + " TEXT,"
                + COLUMN_NAME_FIMLIY_NAME
                + " TEXT,"
                + COLUMN_NAME_GENDER
                + " TEXT,"
                + COLUMN_NAME_DATEOFBIRTH
                + " TEXT,"
                + COLUMN_NAME_EDUCATION
                + " TEXT,"
                + COLUMN_NAME_MARITALSTATUS
                + " TEXT,"
                + COLUMN_NAME_CONTACTNO1
                + " TEXT,"
                + COLUMN_NAME_EMAIL
                + " TEXT,"
                + COLUMN_NAME_AGE
                + " TEXT,"
                + COLUMN_NAME_GENDERID
                + " TEXT,"
                + COLUMN_NAME_MARITALSTATUSID
                + " TEXT,"
                + COLUMN_NAME_BLOOD_GROUP_ID
                + " TEXT,"
                + COLUMN_NAME_BLOOD_GROUP
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class PatientQueue implements BaseColumns {

        private PatientQueue() {
        }

        public static final String TABLE_NAME = "T_PatientQueue";
        public static final String DEFAULT_SORT_ORDER = "Datetime(FromTime) ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_UNITID = "UnitId";
        public static final String COLUMN_NAME_UnitName = "UnitName";
        public static final String COLUMN_NAME_DATE = "Date";
        public static final String COLUMN_NAME_FROMTIME = "FromTime";
        public static final String COLUMN_NAME_TOTIME = "ToTime";
        public static final String COLUMN_NAME_QUEUETIME = "Queuetime";
        public static final String COLUMN_NAME_VISITID = "VisitID";
        public static final String COLUMN_NAME_PATIENTID = "PatientId";
        public static final String COLUMN_NAME_PATIENTUNITID = "PatientUnitID";
        public static final String COLUMN_NAME_OPDNO = "OPDNO";
        public static final String COLUMN_NAME_VISITTYPEID = "VisitTypeID";
        public static final String COLUMN_NAME_VISITDESCRIPTION = "VisitDescription";
        public static final String COLUMN_NAME_MRNO = "MRNo";
        public static final String COLUMN_NAME_FIRSTNAME = "FirstName";
        public static final String COLUMN_NAME_MIDDLENAME = "MiddleName";
        public static final String COLUMN_NAME_LASTNAME = "LastName";
        public static final String COLUMN_NAME_DATEOFBIRTH = "DateOfBirth";
        public static final String COLUMN_NAME_EMAIL = "Email";
        public static final String COLUMN_NAME_CONTACTNO1 = "ContactNo1";
        public static final String COLUMN_NAME_MARITALSTATUSID = "MaritalStatusID";
        public static final String COLUMN_NAME_MARITALSTATUS = "MaritalStatus";
        public static final String COLUMN_NAME_BLOODGROUPID = "BloodGroupID";
        public static final String COLUMN_NAME_BLOODGROUP = "BloodGroup";
        public static final String COLUMN_NAME_GENDERID = "GenderID";
        public static final String COLUMN_NAME_GENDER = "Gender";
        public static final String COLUMN_NAME_COMPLAINTSID = "ComplaintsID";
        public static final String COLUMN_NAME_COMPLAINTS = "Complaints";
        public static final String COLUMN_NAME_DEPARTMENTID = "DepartmentID";
        public static final String COLUMN_NAME_DEPARTMENT = "Department";
        public static final String COLUMN_NAME_DOCTORID = "DoctorID";
        public static final String COLUMN_NAME_REFERREDDOCTORID = "ReferredDoctorID";
        public static final String COLUMN_NAME_REFERREDDOCTOR = "ReferredDoctor";
        public static final String COLUMN_NAME_ISSTATUS = "ISStatus";
        public static final String COLUMN_NAME_ISBILLED_SUBMITID = "IsBilledSubmitID";
        public static final String COLUMN_NAME_DrName = "DrName";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + PatientQueue._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_UNITID
                + " TEXT,"
                + COLUMN_NAME_UnitName
                + " TEXT,"
                + COLUMN_NAME_DATE
                + " TEXT,"
                + COLUMN_NAME_FROMTIME
                + " TEXT,"
                + COLUMN_NAME_TOTIME
                + " TEXT,"
                + COLUMN_NAME_QUEUETIME
                + " TEXT,"
                + COLUMN_NAME_VISITID
                + " TEXT,"
                + COLUMN_NAME_PATIENTID
                + " TEXT,"
                + COLUMN_NAME_PATIENTUNITID
                + " TEXT,"
                + COLUMN_NAME_OPDNO
                + " TEXT,"
                + COLUMN_NAME_VISITTYPEID
                + " TEXT,"
                + COLUMN_NAME_VISITDESCRIPTION
                + " TEXT,"
                + COLUMN_NAME_MRNO
                + " TEXT,"
                + COLUMN_NAME_FIRSTNAME
                + " TEXT,"
                + COLUMN_NAME_MIDDLENAME
                + " TEXT,"
                + COLUMN_NAME_LASTNAME
                + " TEXT,"
                + COLUMN_NAME_DATEOFBIRTH
                + " TEXT,"
                + COLUMN_NAME_EMAIL
                + " TEXT,"
                + COLUMN_NAME_CONTACTNO1
                + " TEXT,"
                + COLUMN_NAME_MARITALSTATUSID
                + " TEXT,"
                + COLUMN_NAME_MARITALSTATUS
                + " TEXT,"
                + COLUMN_NAME_BLOODGROUPID
                + " TEXT,"
                + COLUMN_NAME_BLOODGROUP
                + " TEXT,"
                + COLUMN_NAME_GENDERID
                + " TEXT,"
                + COLUMN_NAME_GENDER
                + " TEXT,"
                + COLUMN_NAME_COMPLAINTSID
                + " TEXT,"
                + COLUMN_NAME_COMPLAINTS
                + " TEXT,"
                + COLUMN_NAME_DEPARTMENTID
                + " TEXT,"
                + COLUMN_NAME_DEPARTMENT
                + " TEXT,"
                + COLUMN_NAME_DOCTORID
                + " TEXT,"
                + COLUMN_NAME_REFERREDDOCTORID
                + " TEXT,"
                + COLUMN_NAME_REFERREDDOCTOR
                + " TEXT,"
                + COLUMN_NAME_ISSTATUS
                + " TEXT,"
                + COLUMN_NAME_ISBILLED_SUBMITID
                + " TEXT,"
                + COLUMN_NAME_DrName
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class VisitList implements BaseColumns {

        private VisitList() {
        }

        public static final String TABLE_NAME = "T_VisitList";
        public static final String DEFAULT_SORT_ORDER = "Datetime(FromTime) ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_UNITID = "UnitId";
        public static final String COLUMN_NAME_DATE = "SearchDate";
        public static final String COLUMN_NAME_TIME = "Time";
        public static final String COLUMN_NAME_APPOINTMENTDATE = "AppointmentDate";
        public static final String COLUMN_NAME_FROMTIME = "FromTime";
        public static final String COLUMN_NAME_TOTIME = "ToTime";
        public static final String COLUMN_NAME_PATIENTID = "PatientId";
        public static final String COLUMN_NAME_PATIENTUNITID = "PatientUnitId";
        public static final String COLUMN_NAME_FIRSTNAME = "FirstName";
        public static final String COLUMN_NAME_MIDDLENAME = "MiddleName";
        public static final String COLUMN_NAME_LASTNAME = "LastName";
        public static final String COLUMN_NAME_OPDNO = "OPDNO";
        public static final String COLUMN_NAME_VISITTYPEID = "VisitTypeID";
        public static final String COLUMN_NAME_VISITDESCRIPTION = "VisitDescription";
        public static final String COLUMN_NAME_COMPLAINTSID = "ComplaintsID";
        public static final String COLUMN_NAME_COMPLAINTS = "Complaints";
        public static final String COLUMN_NAME_DEPARTMENTID = "DepartmentID";
        public static final String COLUMN_NAME_DEPARTMENT = "Department";
        public static final String COLUMN_NAME_DOCTORID = "DoctorID";
        public static final String COLUMN_NAME_REFERREDDOCTORID = "ReferredDoctorID";
        public static final String COLUMN_NAME_REFERREDDOCTOR = "ReferredDoctor";
        public static final String COLUMN_NAME_VISITTYPESERVICEID = "VisitTypeServiceID";
        public static final String COLUMN_NAME_ISEMR = "IsEMR";
        public static final String COLUMN_NAME_ISBASICEMR = "IsBasicEMR";
        public static final String COLUMN_NAME_ISLAB = "IsLab";
        public static final String COLUMN_NAME_ISRADIOLOG = "IsRadiology";
        public static final String COLUMN_NAME_ISPROCEDURE = "IsProcedure";
        public static final String COLUMN_NAME_ISOTHERSERVIC = "IsOtherService";
        public static final String COLUMN_NAME_ISCURRENTMEDICATION = "IsCurrentMedication";
        public static final String COLUMN_NAME_ISPRESCRIPTION = "IsPrescription";
        public static final String COLUMN_NAME_ISREFERRAL = "IsReferral";
        public static final String COLUMN_NAME_REFENTITYTYPE = "RefEntityType";
        public static final String COLUMN_NAME_REFENTITYID = "RefEntityID";
        public static final String COLUMN_NAME_VISITDATETIME = "VisitDateTime";
        public static final String COLUMN_NAME_STATUS = "Status";
        public static final String COLUMN_NAME_VISITSTATUS = "VisitStatus";
        public static final String COLUMN_NAME_CURRENTVISITSTATUS = "CurrentVisitStatus";
        public static final String COLUMN_NAME_UnitName = "UnitName";
        public static final String COLUMN_NAME_DrName = "DrName";

        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + VisitList._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_UNITID
                + " TEXT,"
                + COLUMN_NAME_DATE
                + " TEXT,"
                + COLUMN_NAME_TIME
                + " TEXT,"
                + COLUMN_NAME_APPOINTMENTDATE
                + " TEXT,"
                + COLUMN_NAME_FROMTIME
                + " TEXT,"
                + COLUMN_NAME_TOTIME
                + " TEXT,"
                + COLUMN_NAME_PATIENTID
                + " TEXT,"
                + COLUMN_NAME_PATIENTUNITID
                + " TEXT,"
                + COLUMN_NAME_FIRSTNAME
                + " TEXT,"
                + COLUMN_NAME_MIDDLENAME
                + " TEXT,"
                + COLUMN_NAME_LASTNAME
                + " TEXT,"
                + COLUMN_NAME_OPDNO
                + " TEXT,"
                + COLUMN_NAME_VISITTYPEID
                + " TEXT,"
                + COLUMN_NAME_VISITDESCRIPTION
                + " TEXT,"
                + COLUMN_NAME_COMPLAINTSID
                + " TEXT,"
                + COLUMN_NAME_COMPLAINTS
                + " TEXT,"
                + COLUMN_NAME_DEPARTMENTID
                + " TEXT,"
                + COLUMN_NAME_DEPARTMENT
                + " TEXT,"
                + COLUMN_NAME_DOCTORID
                + " TEXT,"
                + COLUMN_NAME_REFERREDDOCTORID
                + " TEXT,"
                + COLUMN_NAME_REFERREDDOCTOR
                + " TEXT,"
                + COLUMN_NAME_VISITTYPESERVICEID
                + " TEXT,"
                + COLUMN_NAME_ISEMR
                + " TEXT,"
                + COLUMN_NAME_ISBASICEMR
                + " TEXT,"
                + COLUMN_NAME_ISLAB
                + " TEXT,"
                + COLUMN_NAME_ISRADIOLOG
                + " TEXT,"
                + COLUMN_NAME_ISPROCEDURE
                + " TEXT,"
                + COLUMN_NAME_ISOTHERSERVIC
                + " TEXT,"
                + COLUMN_NAME_ISCURRENTMEDICATION
                + " TEXT,"
                + COLUMN_NAME_ISPRESCRIPTION
                + " TEXT,"
                + COLUMN_NAME_ISREFERRAL
                + " TEXT,"
                + COLUMN_NAME_REFENTITYTYPE
                + " TEXT,"
                + COLUMN_NAME_REFENTITYID
                + " TEXT,"
                + COLUMN_NAME_VISITDATETIME
                + " TEXT,"
                + COLUMN_NAME_STATUS
                + " TEXT,"
                + COLUMN_NAME_VISITSTATUS
                + " TEXT,"
                + COLUMN_NAME_CURRENTVISITSTATUS
                + " TEXT,"
                + COLUMN_NAME_UnitName
                + " TEXT,"
                + COLUMN_NAME_DrName
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class MedicienRoute implements BaseColumns {

        private MedicienRoute() {
        }

        public static final String TABLE_NAME = "T_MedicienRoute";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + MedicienRoute._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_DESCRIPTION
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class MedicienFrequency implements BaseColumns {

        private MedicienFrequency() {
        }

        public static final String TABLE_NAME = "T_MedicienFrequency";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_QUNTITYPERDAY = "QuntityPerDay";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + MedicienFrequency._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_DESCRIPTION
                + " TEXT,"
                + COLUMN_NAME_QUNTITYPERDAY
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class MedicienInstruction implements BaseColumns {

        private MedicienInstruction() {
        }

        public static final String TABLE_NAME = "T_MedicienInstruction";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + MedicienInstruction._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_DESCRIPTION
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class Vital implements BaseColumns {

        private Vital() {
        }

        public static final String TABLE_NAME = "T_Vital";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_UNIT = "Unit";
        public static final String COLUMN_NAME_MINVALUE = "MinValue";
        public static final String COLUMN_NAME_MAXVALUE = "MaxValue";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + Vital._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_DESCRIPTION
                + " TEXT,"
                + COLUMN_NAME_UNIT
                + " TEXT,"
                + COLUMN_NAME_MINVALUE
                + " TEXT,"
                + COLUMN_NAME_MAXVALUE
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class DaignosisTypeMaster implements BaseColumns {

        private DaignosisTypeMaster() {
        }

        public static final String TABLE_NAME = "T_DaignosisTypeMaster";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + DaignosisTypeMaster._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_DESCRIPTION
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class Priority implements BaseColumns {

        private Priority() {
        }

        public static final String TABLE_NAME = "T_Priority";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + Priority._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_DESCRIPTION
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class DiagnosisList implements BaseColumns {

        private DiagnosisList() {
        }

        public static final String TABLE_NAME = "T_DiagnosisList";
        public static final String DEFAULT_SORT_ORDER = "ID ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_UNITID = "UnitID";
        public static final String COLUMN_NAME_PATIENTID = "PatientID";
        public static final String COLUMN_NAME_PATIENTUNITID = "PatientUnitID";
        public static final String COLUMN_NAME_VISITID = "VisitId";
        public static final String COLUMN_NAME_DIAGNOSISID = "DiagnosisID";
        public static final String COLUMN_NAME_CODE = "Code";
        public static final String COLUMN_NAME_DIAGNOSISNAME = "DiagnosisName";
        public static final String COLUMN_NAME_DATE = "Date";
        public static final String COLUMN_NAME_ICDID = "ICDId";
        public static final String COLUMN_NAME_PRIMARYDIAGNOSIS = "PrimaryDiagnosis";
        public static final String COLUMN_NAME_ADDEDBY = "AddedBy";
        public static final String COLUMN_NAME_STATUS = "Status";
        public static final String COLUMN_NAME_DIAGNOSISTYPEID = "DiagnosisTypeID";
        public static final String COLUMN_NAME_DIAGNOSISTYPE = "DiagnosisType";
        public static final String COLUMN_NAME_REMARK = "Remark";
        public static final String COLUMN_NAME_ISSTATUS = "ISStatus";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";
        public static final String COLUMN_NAME_IS_UPDATE = "IsUpdate";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + DiagnosisList._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_UNITID
                + " TEXT,"
                + COLUMN_NAME_PATIENTID
                + " TEXT,"
                + COLUMN_NAME_PATIENTUNITID
                + " TEXT,"
                + COLUMN_NAME_VISITID
                + " TEXT,"
                + COLUMN_NAME_DIAGNOSISID
                + " TEXT,"
                + COLUMN_NAME_CODE
                + " TEXT,"
                + COLUMN_NAME_DIAGNOSISNAME
                + " TEXT,"
                + COLUMN_NAME_DATE
                + " TEXT,"
                + COLUMN_NAME_ICDID
                + " TEXT,"
                + COLUMN_NAME_PRIMARYDIAGNOSIS
                + " TEXT,"
                + COLUMN_NAME_ADDEDBY
                + " TEXT,"
                + COLUMN_NAME_STATUS
                + " TEXT,"
                + COLUMN_NAME_DIAGNOSISTYPEID
                + " TEXT,"
                + COLUMN_NAME_DIAGNOSISTYPE
                + " TEXT,"
                + COLUMN_NAME_REMARK
                + " TEXT,"
                + COLUMN_NAME_ISSTATUS
                + " TEXT,"
                + COLUMN_NAME_IS_UPDATE
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " TEXT DEFAULT 0"
                + ")";
    }

    public static final class VitalsList implements BaseColumns {

        private VitalsList() {
        }

        public static final String TABLE_NAME = "T_VitalsList";
        public static final String DEFAULT_SORT_ORDER = "ID ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_UNITID = "UnitID";
        public static final String COLUMN_NAME_VISITID = "VisitID";
        public static final String COLUMN_NAME_PATIENTID = "PatientID";
        public static final String COLUMN_NAME_PATIENTUNITID = "PatientUnitID";
        public static final String COLUMN_NAME_TEMPLATEID = "TemplateID";
        public static final String COLUMN_NAME_DOCTORID = "DoctorID";
        public static final String COLUMN_NAME_DATE = "Date";
        public static final String COLUMN_NAME_TIME = "Time";
        public static final String COLUMN_NAME_VITALID = "VitalID";
        public static final String COLUMN_NAME_VITALSDECRIPTION = "VitalsDecription";
        public static final String COLUMN_NAME_VALUE = "Value";
        public static final String COLUMN_NAME_UNIT = "Unit";
        public static final String COLUMN_NAME_STATUS = "Status";
        public static final String COLUMN_NAME_ADDEDBY = "AddedBy";
        public static final String COLUMN_NAME_ISSTATUS = "ISStatus";
        public static final String COLUMN_NAME_ISLOCAL = "ISLocal";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";
        public static final String COLUMN_NAME_IS_UPDATE = "IsUpdate";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + VitalsList._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_UNITID
                + " TEXT,"
                + COLUMN_NAME_VISITID
                + " TEXT,"
                + COLUMN_NAME_PATIENTID
                + " TEXT,"
                + COLUMN_NAME_PATIENTUNITID
                + " TEXT,"
                + COLUMN_NAME_TEMPLATEID
                + " TEXT,"
                + COLUMN_NAME_DOCTORID
                + " TEXT,"
                + COLUMN_NAME_DATE
                + " TEXT,"
                + COLUMN_NAME_TIME
                + " TEXT,"
                + COLUMN_NAME_VITALID
                + " TEXT,"
                + COLUMN_NAME_VITALSDECRIPTION
                + " TEXT,"
                + COLUMN_NAME_VALUE
                + " TEXT,"
                + COLUMN_NAME_UNIT
                + " TEXT,"
                + COLUMN_NAME_STATUS
                + " TEXT,"
                + COLUMN_NAME_ADDEDBY
                + " TEXT,"
                + COLUMN_NAME_ISSTATUS
                + " TEXT,"
                + COLUMN_NAME_ISLOCAL
                + " TEXT DEFAULT 0,"
                + COLUMN_NAME_IS_UPDATE
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " TEXT DEFAULT 0"
                + ")";
    }

    public static final class VitalsListLocal implements BaseColumns {

        private VitalsListLocal() {
        }

        public static final String TABLE_NAME = "T_VitalsListLocal";
        public static final String DEFAULT_SORT_ORDER = "ID ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_UNITID = "UnitID";
        public static final String COLUMN_NAME_VISITID = "VisitID";
        public static final String COLUMN_NAME_PATIENTID = "PatientID";
        public static final String COLUMN_NAME_PATIENTUNITID = "PatientUnitID";
        public static final String COLUMN_NAME_TEMPLATEID = "TemplateID";
        public static final String COLUMN_NAME_DOCTORID = "DoctorID";
        public static final String COLUMN_NAME_DATE = "Date";
        public static final String COLUMN_NAME_TIME = "Time";
        public static final String COLUMN_NAME_VITALID = "VitalID";
        public static final String COLUMN_NAME_VITALSDECRIPTION = "VitalsDecription";
        public static final String COLUMN_NAME_VALUE = "Value";
        public static final String COLUMN_NAME_UNIT = "Unit";
        public static final String COLUMN_NAME_STATUS = "Status";
        public static final String COLUMN_NAME_ADDEDBY = "AddedBy";
        public static final String COLUMN_NAME_ISSTATUS = "ISStatus";
        public static final String COLUMN_NAME_ISLOCAL = "ISLocal";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";
        public static final String COLUMN_NAME_IS_UPDATE = "IsUpdate";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + VitalsListLocal._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_UNITID
                + " TEXT,"
                + COLUMN_NAME_VISITID
                + " TEXT,"
                + COLUMN_NAME_PATIENTID
                + " TEXT,"
                + COLUMN_NAME_PATIENTUNITID
                + " TEXT,"
                + COLUMN_NAME_TEMPLATEID
                + " TEXT,"
                + COLUMN_NAME_DOCTORID
                + " TEXT,"
                + COLUMN_NAME_DATE
                + " TEXT,"
                + COLUMN_NAME_TIME
                + " TEXT,"
                + COLUMN_NAME_VITALID
                + " TEXT,"
                + COLUMN_NAME_VITALSDECRIPTION
                + " TEXT,"
                + COLUMN_NAME_VALUE
                + " TEXT,"
                + COLUMN_NAME_UNIT
                + " TEXT,"
                + COLUMN_NAME_STATUS
                + " TEXT,"
                + COLUMN_NAME_ADDEDBY
                + " TEXT,"
                + COLUMN_NAME_ISSTATUS
                + " TEXT,"
                + COLUMN_NAME_ISLOCAL
                + " TEXT DEFAULT 0,"
                + COLUMN_NAME_IS_UPDATE
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " TEXT DEFAULT 0"
                + ")";
    }

    public static final class CPOEService implements BaseColumns {

        private CPOEService() {
        }

        public static final String TABLE_NAME = "T_CPOEService";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_UNITID = "UnitID";
        public static final String COLUMN_NAME_VISITID = "VisitID";
        public static final String COLUMN_NAME_PATIENTID = "PatientID";
        public static final String COLUMN_NAME_PATIENTUNITID = "PatientUnitID";
        public static final String COLUMN_NAME_DOCTORID = "DoctorID";
        public static final String COLUMN_NAME_SERVICEID = "ServiceID";
        public static final String COLUMN_NAME_SERVICENAME = "ServiceName";
        public static final String COLUMN_NAME_REASON = "Reason";
        public static final String COLUMN_NAME_RATE = "Rate";
        public static final String COLUMN_NAME_PRIORITY = "Priority";
        public static final String COLUMN_NAME_TEMPLATEID = "TemplateID";
        public static final String COLUMN_NAME_ADVICE = "Advice";
        public static final String COLUMN_NAME_ADDEDBY = "AddedBy";
        public static final String COLUMN_NAME_PRESCRIPTIONID = "PrescriptionID";
        public static final String COLUMN_NAME_PRIORITYDESCRIPTION = "PriorityDescription";
        public static final String COLUMN_NAME_STATUS = "Status";
        public static final String COLUMN_NAME_BILLEDDATE = "BilledDate";
        public static final String COLUMN_NAME_ISSTATUS = "ISStatus";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";
        public static final String COLUMN_NAME_IS_UPDATE = "IsUpdate";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + CPOEService._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_UNITID
                + " TEXT,"
                + COLUMN_NAME_VISITID
                + " TEXT,"
                + COLUMN_NAME_PATIENTID
                + " TEXT,"
                + COLUMN_NAME_PATIENTUNITID
                + " TEXT,"
                + COLUMN_NAME_DOCTORID
                + " TEXT,"
                + COLUMN_NAME_SERVICEID
                + " TEXT,"
                + COLUMN_NAME_SERVICENAME
                + " TEXT,"
                + COLUMN_NAME_REASON
                + " TEXT,"
                + COLUMN_NAME_RATE
                + " TEXT,"
                + COLUMN_NAME_PRIORITY
                + " TEXT,"
                + COLUMN_NAME_TEMPLATEID
                + " TEXT,"
                + COLUMN_NAME_ADVICE
                + " TEXT,"
                + COLUMN_NAME_ADDEDBY
                + " TEXT,"
                + COLUMN_NAME_PRESCRIPTIONID
                + " TEXT,"
                + COLUMN_NAME_PRIORITYDESCRIPTION
                + " TEXT,"
                + COLUMN_NAME_STATUS
                + " TEXT,"
                + COLUMN_NAME_BILLEDDATE
                + " TEXT,"
                + COLUMN_NAME_ISSTATUS
                + " TEXT,"
                + COLUMN_NAME_IS_UPDATE
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " TEXT DEFAULT 0"
                + ")";
    }

    public static final class CPOEMedicine implements BaseColumns {

        private CPOEMedicine() {
        }

        public static final String TABLE_NAME = "T_CPOEMedicine";
        public static final String DEFAULT_SORT_ORDER = "CAST(ID AS INTEGER) ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_UNITID = "UnitID";
        public static final String COLUMN_NAME_VISITID = "VisitID";
        public static final String COLUMN_NAME_PATIENTID = "PatientID";
        public static final String COLUMN_NAME_PATIENTUNITID = "PatientUnitID";
        public static final String COLUMN_NAME_DOCTORID = "DoctorID";
        public static final String COLUMN_NAME_TEMPLATEID = "TemplateID";
        public static final String COLUMN_NAME_PRESCRIPTIONID = "PrescriptionID";
        public static final String COLUMN_NAME_DRUGID = "DrugID";
        public static final String COLUMN_NAME_ITEMNAME = "ItemName";
        public static final String COLUMN_NAME_DOSE = "Dose";
        public static final String COLUMN_NAME_ROUTE = "Route";
        public static final String COLUMN_NAME_ROUTEID = "RouteID";
        public static final String COLUMN_NAME_FREQUENCY = "Frequency";
        public static final String COLUMN_NAME_FREQUENCYID = "FrequencyID";
        public static final String COLUMN_NAME_DAYS = "Days";
        public static final String COLUMN_NAME_QUANTITY = "Quantity";
        public static final String COLUMN_NAME_ISOTHER = "IsOther";
        public static final String COLUMN_NAME_REASON = "Reason";
        public static final String COLUMN_NAME_REASONID = "ReasonID";
        public static final String COLUMN_NAME_FROMHISTORY = "FromHistory";
        public static final String COLUMN_NAME_DATE = "Date";
        public static final String COLUMN_NAME_RATE = "Rate";
        public static final String COLUMN_NAME_BILLEDDATE = "BilledDate";
        public static final String COLUMN_NAME_GENERALINSTRUCTION = "GeneralInstruction";
        public static final String COLUMN_NAME_ISDESPENCE = "IsDespence";
        public static final String COLUMN_NAME_STATUS = "Status";
        public static final String COLUMN_NAME_ADDEDBY = "AddedBy";
        public static final String COLUMN_NAME_UPDATEDDATETIME = "UpdatedDateTime";
        public static final String COLUMN_NAME_ISSTATUS = "ISStatus";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";
        public static final String COLUMN_NAME_IS_UPDATE = "IsUpdate";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + CPOEMedicine._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_UNITID
                + " TEXT,"
                + COLUMN_NAME_VISITID
                + " TEXT,"
                + COLUMN_NAME_PATIENTID
                + " TEXT,"
                + COLUMN_NAME_PATIENTUNITID
                + " TEXT,"
                + COLUMN_NAME_DOCTORID
                + " TEXT,"
                + COLUMN_NAME_TEMPLATEID
                + " TEXT,"
                + COLUMN_NAME_PRESCRIPTIONID
                + " TEXT,"
                + COLUMN_NAME_DRUGID
                + " TEXT,"
                + COLUMN_NAME_ITEMNAME
                + " TEXT,"
                + COLUMN_NAME_DOSE
                + " TEXT,"
                + COLUMN_NAME_ROUTE
                + " TEXT,"
                + COLUMN_NAME_ROUTEID
                + " TEXT,"
                + COLUMN_NAME_FREQUENCY
                + " TEXT,"
                + COLUMN_NAME_FREQUENCYID
                + " TEXT,"
                + COLUMN_NAME_DAYS
                + " TEXT,"
                + COLUMN_NAME_QUANTITY
                + " TEXT,"
                + COLUMN_NAME_ISOTHER
                + " TEXT,"
                + COLUMN_NAME_REASON
                + " TEXT,"
                + COLUMN_NAME_REASONID
                + " TEXT,"
                + COLUMN_NAME_FROMHISTORY
                + " TEXT,"
                + COLUMN_NAME_DATE
                + " TEXT,"
                + COLUMN_NAME_RATE
                + " TEXT,"
                + COLUMN_NAME_BILLEDDATE
                + " TEXT,"
                + COLUMN_NAME_GENERALINSTRUCTION
                + " TEXT,"
                + COLUMN_NAME_ISDESPENCE
                + " TEXT,"
                + COLUMN_NAME_STATUS
                + " TEXT,"
                + COLUMN_NAME_ADDEDBY
                + " TEXT,"
                + COLUMN_NAME_UPDATEDDATETIME
                + " TEXT,"
                + COLUMN_NAME_ISSTATUS
                + " TEXT,"
                + COLUMN_NAME_IS_UPDATE
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " TEXT DEFAULT 0"
                + ")";
    }

    public static final class Flag implements BaseColumns {

        private Flag() {
        }

        public static final String TABLE_NAME = "T_Flag";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";

        public static final String COLUMN_NAME_FLAG = "Flag";
        public static final String COLUMN_NAME_MSG = "Msg";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + Flag._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_FLAG
                + " TEXT,"
                + COLUMN_NAME_MSG
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class MasterFlag implements BaseColumns {

        private MasterFlag() {
        }

        public static final String TABLE_NAME = "T_MasterFlag";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";

        public static final String COLUMN_NAME_MASTERFLAG = "MasterFlag";
        public static final String COLUMN_NAME_MSG = "Msg";
        public static final String COLUMN_NAME_DATETIME = "DateTime";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + MasterFlag._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_MASTERFLAG
                + " TEXT,"
                + COLUMN_NAME_MSG
                + " TEXT,"
                + COLUMN_NAME_DATETIME
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class ComplaintsList implements BaseColumns {

        private ComplaintsList() {
        }

        public static final String TABLE_NAME = "T_ComplaintsList";
        public static final String DEFAULT_SORT_ORDER = "ID DESC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_UNITID = "UnitID";
        public static final String COLUMN_NAME_VISITID = "VisitID";
        public static final String COLUMN_NAME_VISIT_UNITID = "VisitUnitID";
        public static final String COLUMN_NAME_PATIENTID = "PatientID";
        public static final String COLUMN_NAME_PATIENT_UNITID = "PatientUnitID";
        public static final String COLUMN_NAME_DOCTORID = "DoctorID";
        public static final String COLUMN_NAME_CHIEF_COMPLAINTS = "ChiefComplaints";
        public static final String COLUMN_NAME_ASSOCIATE_COMPLAINTS = "AssosciateComplaints";
        public static final String COLUMN_NAME_REMARK = "Remark";
        public static final String COLUMN_NAME_STATUS = "Status";
        public static final String COLUMN_NAME_CREATED_UNITID = "CreatedUnitID";
        public static final String COLUMN_NAME_UPDATED_UNITID = "UpdatedUnitID";
        public static final String COLUMN_NAME_ADDEDBY = "AddedBy";
        public static final String COLUMN_NAME_ADDED_DATETIME = "AddedDateTime";
        public static final String COLUMN_NAME_UPDATEDBY = "UpdatedBy";
        public static final String COLUMN_NAME_UPDATED_DATETIME = "UpdatedDateTime";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";
        public static final String COLUMN_NAME_IS_UPDATE = "IsUpdate";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + ComplaintsList._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_UNITID
                + " TEXT,"
                + COLUMN_NAME_VISITID
                + " TEXT,"
                + COLUMN_NAME_VISIT_UNITID
                + " TEXT,"
                + COLUMN_NAME_PATIENTID
                + " TEXT,"
                + COLUMN_NAME_PATIENT_UNITID
                + " TEXT,"
                + COLUMN_NAME_DOCTORID
                + " TEXT,"
                + COLUMN_NAME_CHIEF_COMPLAINTS
                + " TEXT,"
                + COLUMN_NAME_ASSOCIATE_COMPLAINTS
                + " TEXT,"
                + COLUMN_NAME_REMARK
                + " TEXT,"
                + COLUMN_NAME_STATUS
                + " TEXT,"
                + COLUMN_NAME_CREATED_UNITID
                + " TEXT,"
                + COLUMN_NAME_UPDATED_UNITID
                + " TEXT,"
                + COLUMN_NAME_ADDEDBY
                + " TEXT,"
                + COLUMN_NAME_ADDED_DATETIME
                + " TEXT,"
                + COLUMN_NAME_UPDATEDBY
                + " TEXT,"
                + COLUMN_NAME_UPDATED_DATETIME
                + " TEXT,"
                + COLUMN_NAME_IS_UPDATE
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " TEXT DEFAULT 0"
                + ")";
    }

    public static final class ReferralServiceList implements BaseColumns {

        private ReferralServiceList() {
        }

        public static final String TABLE_NAME = "T_ReferralServiceList";
        public static final String DEFAULT_SORT_ORDER = "ID ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_UNITID = "UnitID";
        public static final String COLUMN_NAME_PATIENTID = "PatientID";
        public static final String COLUMN_NAME_PATIENT_UNITID = "PatientUnitID";
        public static final String COLUMN_NAME_DOCTORID = "DoctorID";
        public static final String COLUMN_NAME_VISITID = "VisitId";
        public static final String COLUMN_NAME_DEPTID = "DepartmentID";
        public static final String COLUMN_NAME_SERVICEID = "ServiceID";
        public static final String COLUMN_NAME_SERVICE_NAME = "ServiceName";
        public static final String COLUMN_NAME_REFERRAL_DOCTORID = "ReferralDoctorID";
        public static final String COLUMN_NAME_REFERRAL_DOCTORNAME = "ReferralDoctorName";
        public static final String COLUMN_NAME_RATE = "Rate";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";
        public static final String COLUMN_NAME_IS_UPDATE = "IsUpdate";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + ReferralServiceList._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_UNITID
                + " TEXT,"
                + COLUMN_NAME_PATIENTID
                + " TEXT,"
                + COLUMN_NAME_PATIENT_UNITID
                + " TEXT,"
                + COLUMN_NAME_DOCTORID
                + " TEXT,"
                + COLUMN_NAME_VISITID
                + " TEXT,"
                + COLUMN_NAME_DEPTID
                + " TEXT,"
                + COLUMN_NAME_SERVICEID
                + " TEXT,"
                + COLUMN_NAME_SERVICE_NAME
                + " TEXT,"
                + COLUMN_NAME_REFERRAL_DOCTORID
                + " TEXT,"
                + COLUMN_NAME_REFERRAL_DOCTORNAME
                + " TEXT,"
                + COLUMN_NAME_RATE
                + " TEXT,"
                + COLUMN_NAME_IS_UPDATE
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " TEXT DEFAULT 0"
                + ")";
    }

    /*public static final class PatientConsoleList implements BaseColumns {

        private PatientConsoleList() {
        }

        public static final String TABLE_NAME = "T_PatientConsoleList";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_VisitID = "VisitID";
        public static final String COLUMN_NAME_VisitDate = "VisitDate";
        public static final String COLUMN_NAME_visitDoctor = "visitDoctor";
        public static final String COLUMN_NAME_clinic = "clinic";
        public static final String COLUMN_NAME_OPDNO = "OPDNO";
        public static final String COLUMN_NAME_VisitType = "VisitType";
        public static final String COLUMN_NAME_Prescription = "Prescription";
        public static final String COLUMN_NAME_EMR = "EMR";
        public static final String COLUMN_NAME_Attachment = "Attachment";
        public static final String COLUMN_NAME_FilePath = "FilePath";
        public static final String COLUMN_NAME_IS_UPDATE = "IsUpdate";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + PatientConsoleList._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_VisitID
                + " TEXT,"
                + COLUMN_NAME_VisitDate
                + " TEXT,"
                + COLUMN_NAME_visitDoctor
                + " TEXT,"
                + COLUMN_NAME_clinic
                + " TEXT,"
                + COLUMN_NAME_OPDNO
                + " TEXT,"
                + COLUMN_NAME_VisitType
                + " TEXT,"
                + COLUMN_NAME_Prescription
                + " TEXT,"
                + COLUMN_NAME_EMR
                + " TEXT,"
                + COLUMN_NAME_Attachment
                + " TEXT,"
                + COLUMN_NAME_FilePath
                + " TEXT,"
                + COLUMN_NAME_IS_UPDATE
                + " TEXT DEFAULT 0"
                + ")";
    }*/

    public static final class PatientFollowUp implements BaseColumns {

        private PatientFollowUp() {
        }

        public static final String TABLE_NAME = "T_PatientFollowUp";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_UnitID = "UnitID";
        public static final String COLUMN_NAME_PatientID = "PatientID";
        public static final String COLUMN_NAME_PatientUnitID = "PatientUnitID";
        public static final String COLUMN_NAME_VisitID = "VisitID";
        public static final String COLUMN_NAME_DoctorID = "DoctorID";
        public static final String COLUMN_NAME_Date = "Date";
        public static final String COLUMN_NAME_Advice = "Advice";
        public static final String COLUMN_NAME_FollowUpRemarks = "FollowUpRemarks";
        public static final String COLUMN_NAME_FollowUpDate = "FollowUpDate";
        public static final String COLUMN_NAME_IsSync = "IsSync";
        public static final String COLUMN_NAME_IsUpdate = "IsUpdate";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + PatientFollowUp._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_UnitID
                + " TEXT,"
                + COLUMN_NAME_PatientID
                + " TEXT,"
                + COLUMN_NAME_PatientUnitID
                + " TEXT,"
                + COLUMN_NAME_VisitID
                + " TEXT,"
                + COLUMN_NAME_DoctorID
                + " TEXT,"
                + COLUMN_NAME_Date
                + " TEXT,"
                + COLUMN_NAME_Advice
                + " TEXT,"
                + COLUMN_NAME_FollowUpRemarks
                + " TEXT,"
                + COLUMN_NAME_FollowUpDate
                + " TEXT,"
                + COLUMN_NAME_IsSync
                + " TEXT,"
                + COLUMN_NAME_IsUpdate
                + " TEXT DEFAULT 0"
                + ")";
    }

     /*public static final class BloodGroup implements BaseColumns {

        private BloodGroup() {
        }

        public static final String TABLE_NAME = "T_BloodGroup";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + BloodGroup._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_DESCRIPTION
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class Gender implements BaseColumns {

        private Gender() {
        }

        public static final String TABLE_NAME = "T_Gender";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + Gender._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_DESCRIPTION
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class MaritalStatus implements BaseColumns {

        private MaritalStatus() {
        }

        public static final String TABLE_NAME = "T_MaritalStatus";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + MaritalStatus._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_DESCRIPTION
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }

    public static final class Prefix implements BaseColumns {

        private Prefix() {
        }

        public static final String TABLE_NAME = "T_Prefix";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + Prefix._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_DESCRIPTION
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }*/


    /*public static final class MedicienName implements BaseColumns {

        private MedicienName() {
        }

        public static final String TABLE_NAME = "T_MedicienName";
        public static final String DEFAULT_SORT_ORDER = "ItemName ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_ITEMNAME = "ItemName";
        public static final String COLUMN_NAME_MRP = "MRP";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + MedicienName._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_ITEMNAME
                + " TEXT,"
                + COLUMN_NAME_MRP
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }
    public static final class DaignosisMaster implements BaseColumns {

        private DaignosisMaster() {
        }

        public static final String TABLE_NAME = "T_DaignosisMaster";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_CODE = "Code";
        public static final String COLUMN_NAME_DIAGNOSIS = "Diagnosis";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + DaignosisMaster._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_CODE
                + " TEXT,"
                + COLUMN_NAME_DIAGNOSIS
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }
    public static final class Molecule implements BaseColumns {

        private Molecule() {
        }

        public static final String TABLE_NAME = "T_Molecule";
        public static final String DEFAULT_SORT_ORDER = "ID ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + Molecule._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_DESCRIPTION
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }
    public static final class ServiceName implements BaseColumns {

        private ServiceName() {
        }

        public static final String TABLE_NAME = "T_ServiceName";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_BASESERVICERATE = "BaseServiceRate";
        public static final String COLUMN_NAME_IS_SYNC = "IsSync";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + ServiceName._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ID
                + " TEXT,"
                + COLUMN_NAME_DESCRIPTION
                + " TEXT,"
                + COLUMN_NAME_BASESERVICERATE
                + " TEXT,"
                + COLUMN_NAME_IS_SYNC
                + " INTEGER DEFAULT 0"
                + ")";
    }*/

    private final Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public DatabaseContract(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(this.context);
    }

    public SQLiteDatabase open() throws SQLException {
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return sqLiteDatabase;
    }

    public void close() {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            /*super(context, Environment.getExternalStorageDirectory() + File.separator + Constants.FILE_DIR + File.separator
                    + DATABASE_NAME, null, DATABASE_VERSION);*/
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                Log.d(Constants.TAG, "Creating table 0: " + SynchOfflineData.CREATE_TABLE);
                db.execSQL(SynchOfflineData.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 1: " + Flag.CREATE_TABLE);
                db.execSQL(Flag.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 2: " + MasterFlag.CREATE_TABLE);
                db.execSQL(MasterFlag.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 3: " + DoctorProfile.CREATE_TABLE);
                db.execSQL(DoctorProfile.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 4: " + UnitMaster.CREATE_TABLE);
                db.execSQL(UnitMaster.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 5: " + DoctorType.CREATE_TABLE);
                db.execSQL(DoctorType.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 6: " + Specialization.CREATE_TABLE);
                db.execSQL(Specialization.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 7: " + AppointmentReason.CREATE_TABLE);
                db.execSQL(AppointmentReason.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 8: " + BookAppointment.CREATE_TABLE);
                db.execSQL(BookAppointment.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 9: " + Appointment.CREATE_TABLE);
                db.execSQL(Appointment.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 10: " + Department.CREATE_TABLE);
                db.execSQL(Department.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 11: " + Complaint.CREATE_TABLE);
                db.execSQL(Complaint.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 12: " + Patient.CREATE_TABLE);
                db.execSQL(Patient.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 13: " + PatientQueue.CREATE_TABLE);
                db.execSQL(PatientQueue.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 14: " + VisitList.CREATE_TABLE);
                db.execSQL(VisitList.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 15: " + VitalsListLocal.CREATE_TABLE);
                db.execSQL(VitalsListLocal.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 16: " + MedicienRoute.CREATE_TABLE);
                db.execSQL(MedicienRoute.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 17: " + MedicienFrequency.CREATE_TABLE);
                db.execSQL(MedicienFrequency.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 18: " + MedicienInstruction.CREATE_TABLE);
                db.execSQL(MedicienInstruction.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 19: " + Vital.CREATE_TABLE);
                db.execSQL(Vital.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 20: " + DaignosisTypeMaster.CREATE_TABLE);
                db.execSQL(DaignosisTypeMaster.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 21: " + Priority.CREATE_TABLE);
                db.execSQL(Priority.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 22: " + DiagnosisList.CREATE_TABLE);
                db.execSQL(DiagnosisList.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 23: " + VitalsList.CREATE_TABLE);
                db.execSQL(VitalsList.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 24: " + CPOEService.CREATE_TABLE);
                db.execSQL(CPOEService.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 25: " + CPOEMedicine.CREATE_TABLE);
                db.execSQL(CPOEMedicine.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 26: " + ComplaintsList.CREATE_TABLE);
                db.execSQL(ComplaintsList.CREATE_TABLE);

                Log.d(Constants.TAG, "Creating table 27: " + ReferralServiceList.CREATE_TABLE);
                db.execSQL(ReferralServiceList.CREATE_TABLE);

                /*Log.d(Constants.TAG, "Creating table 28: " + PatientConsoleList.CREATE_TABLE);
                db.execSQL(PatientConsoleList.CREATE_TABLE);*/

                Log.d(Constants.TAG, "Creating table 29: " + PatientFollowUp.CREATE_TABLE);
                db.execSQL(PatientFollowUp.CREATE_TABLE);

              /*Log.d(Constants.TAG, "Creating table 4: " + Gender.CREATE_TABLE);
                db.execSQL(Gender.CREATE_TABLE);
                Log.d(Constants.TAG, "Creating table 5: " + MaritalStatus.CREATE_TABLE);
                db.execSQL(MaritalStatus.CREATE_TABLE);
                Log.d(Constants.TAG, "Creating table 6: " + Prefix.CREATE_TABLE);
                db.execSQL(Prefix.CREATE_TABLE);
                Log.d(Constants.TAG, "Creating table 14: " + BloodGroup.CREATE_TABLE);
                db.execSQL(BloodGroup.CREATE_TABLE);
                Log.d(Constants.TAG, "Creating table 17: " + MedicienName.CREATE_TABLE);
                db.execSQL(MedicienName.CREATE_TABLE);
                Log.d(Constants.TAG, "Creating table 24: " + ServiceName.CREATE_TABLE);
                db.execSQL(ServiceName.CREATE_TABLE);
                Log.d(Constants.TAG, "Creating table 22: " + DaignosisMaster.CREATE_TABLE);
                db.execSQL(DaignosisMaster.CREATE_TABLE);
                Log.d(Constants.TAG, "Creating table 30: " + Molecule.CREATE_TABLE);
                db.execSQL(Molecule.CREATE_TABLE);*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
