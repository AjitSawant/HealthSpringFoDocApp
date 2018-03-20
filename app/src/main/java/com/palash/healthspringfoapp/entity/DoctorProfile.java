package com.palash.healthspringfoapp.entity;

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
    private String DOB;
    private String Education;
    private String Specialization;
    private String EmailId;
    private String PFNumber;
    private String LoginStatus;
    private String RememberMe;
    private String DepartmentID;
    private String IsFrontOfficeUser;
    private String Status;
    private String Locked;

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

    @JsonProperty("Specialization")
    public String getSpecialization() {
        return Specialization;
    }

    public void setSpecialization(String Specialization) {
        this.Specialization = Specialization;
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

    @JsonProperty("IsFrontOfficeUser")
    public String getIsFrontOfficeUser() {
        return IsFrontOfficeUser;
    }

    public void setIsFrontOfficeUser(String isFrontOfficeUser) {
        IsFrontOfficeUser = isFrontOfficeUser;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @JsonProperty("Locked")
    public String getLocked() {
        return Locked;
    }

    public void setLocked(String locked) {
        Locked = locked;
    }
}
