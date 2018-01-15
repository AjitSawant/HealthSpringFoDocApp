package com.palash.healthspringapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DoctorProfile {

    private String DoctorID;
    private String LoginName;
    private String Password;
    private String ID;
    private String UnitID;
    private String UnitName;
    private String FirstName;
    private String MiddleName;
    private String LastName;
    private String Gender;
    private String EmployeeNumber;
    private String DOB;
    private String Education;
    private String Experience;
    private String Specialization;
    private String SubSpecialization;
    private String DoctorType;
    private String EmailId;
    private String PFNumber;
    private String PANNumber;
    private String DateOfJoining;
    private String AccessCardNumber;
    private String Photo;
    private String DigitalSignature;
    private String Status;
    private String Synchronized;
    private String RegestrationNumber;
    private String MaritialStatus;
    private String LoginStatus;
    private String RememberMe;
    private String SubSpecializationID;
    private String DoctorTypeID;
    private String AppointmentSlot;
    private String DepartmentID;


    @JsonProperty("SubSpecializationID")
    public String getSubSpecializationID() {
        return SubSpecializationID;
    }

    public void setSubSpecializationID(String subSpecializationID) {
        SubSpecializationID = subSpecializationID;
    }

    @JsonProperty("DoctorTypeID")
    public String getDoctorTypeID() {
        return DoctorTypeID;
    }

    public void setDoctorTypeID(String doctorTypeID) {
        DoctorTypeID = doctorTypeID;
    }

    @JsonProperty("AppointmentSlot")
    public String getAppointmentSlot() {
        return AppointmentSlot;
    }

    public void setAppointmentSlot(String appointmentSlot) {
        AppointmentSlot = appointmentSlot;
    }

    @JsonProperty("DepartmentID")
    public String getDepartmentID() {
        return DepartmentID;
    }

    public void setDepartmentID(String departmentID) {
        DepartmentID = departmentID;
    }

    @JsonProperty("DoctorID")
    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String DoctorID) {
        this.DoctorID = DoctorID;
    }

    @JsonProperty("LoginName")
    public String getLoginName() {
        return LoginName;
    }

    public void setLoginName(String LoginName) {
        this.LoginName = LoginName;
    }

    @JsonProperty("Password")
    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    @JsonProperty("LoginStatus")
    public String getLoginStatus() {
        return LoginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        LoginStatus = loginStatus;
    }

    @JsonProperty("RememberMe")
    public String getRememberMe() {
        return RememberMe;
    }

    public void setRememberMe(String rememberMe) {
        RememberMe = rememberMe;
    }


    @JsonProperty("ID")
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @JsonProperty("UnitID")
    public String getUnitID() {
        return UnitID;
    }

    public void setUnitID(String UnitID) {
        this.UnitID = UnitID;
    }

    @JsonProperty("UnitName")
    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String UnitName) {
        this.UnitName = UnitName;
    }

    @JsonProperty("FirstName")
    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    @JsonProperty("MiddleName")
    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String MiddleName) {
        this.MiddleName = MiddleName;
    }

    @JsonProperty("LastName")
    public String getLastName() {
        return LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    @JsonProperty("Gender")
    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    @JsonProperty("EmployeeNumber")
    public String getEmployeeNumber() {
        return EmployeeNumber;
    }

    public void setEmployeeNumber(String EmployeeNumber) {
        this.EmployeeNumber = EmployeeNumber;
    }

    @JsonProperty("DOB")
    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    @JsonProperty("Education")
    public String getEducation() {
        return Education;
    }

    public void setEducation(String Education) {
        this.Education = Education;
    }

    @JsonProperty("Experience")
    public String getExperience() {
        return Experience;
    }

    public void setExperience(String Experience) {
        this.Experience = Experience;
    }

    @JsonProperty("Specialization")
    public String getSpecialization() {
        return Specialization;
    }

    public void setSpecialization(String Specialization) {
        this.Specialization = Specialization;
    }

    @JsonProperty("SubSpecialization")
    public String getSubSpecialization() {
        return SubSpecialization;
    }

    public void setSubSpecialization(String SubSpecialization) {
        this.SubSpecialization = SubSpecialization;
    }

    @JsonProperty("DoctorType")
    public String getDoctorType() {
        return DoctorType;
    }

    public void setDoctorType(String DoctorType) {
        this.DoctorType = DoctorType;
    }

    @JsonProperty("EmailId")
    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String EmailId) {
        this.EmailId = EmailId;
    }

    @JsonProperty("PFNumber")
    public String getPFNumber() {
        return PFNumber;
    }

    public void setPFNumber(String PFNumber) {
        this.PFNumber = PFNumber;
    }

    @JsonProperty("PANNumber")
    public String getPANNumber() {
        return PANNumber;
    }

    public void setPANNumber(String PANNumber) {
        this.PANNumber = PANNumber;
    }

    @JsonProperty("DateOfJoining")
    public String getDateOfJoining() {
        return DateOfJoining;
    }

    public void setDateOfJoining(String DateOfJoining) {
        this.DateOfJoining = DateOfJoining;
    }

    @JsonProperty("AccessCardNumber")
    public String getAccessCardNumber() {
        return AccessCardNumber;
    }

    public void setAccessCardNumber(String AccessCardNumber) {
        this.AccessCardNumber = AccessCardNumber;
    }

    @JsonProperty("Photo")
    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String Photo) {
        this.Photo = Photo;
    }

    @JsonProperty("DigitalSignature")
    public String getDigitalSignature() {
        return DigitalSignature;
    }

    public void setDigitalSignature(String DigitalSignature) {
        this.DigitalSignature = DigitalSignature;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    @JsonProperty("Synchronized")
    public String getSynchronized() {
        return Synchronized;
    }

    public void setSynchronized(String Synchronized) {
        this.Synchronized = Synchronized;
    }

    @JsonProperty("RegestrationNumber")
    public String getRegestrationNumber() {
        return RegestrationNumber;
    }

    public void setRegestrationNumber(String RegestrationNumber) {
        this.RegestrationNumber = RegestrationNumber;
    }

    @JsonProperty("MaritialStatus")
    public String getMaritialStatus() {
        return MaritialStatus;
    }

    public void setMaritialStatus(String MaritialStatus) {
        this.MaritialStatus = MaritialStatus;
    }


}
