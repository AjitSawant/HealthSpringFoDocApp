package com.palash.healthspring.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by manishas on 7/6/;0 private String 6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientQueue {
    private String ID;
    private String UnitId;
    private String Date;
    private String FromTime;
    private String ToTime;
    private String VisitID;
    private String Queuetime;
    private String PatientId;
    private String PatientUnitID;
    private String OPDNO;
    private String VisitTypeID;
    private String VisitDescription;
    private String MRNo;
    private String FirstName;
    private String MiddleName;
    private String LastName;
    private String DateOfBirth;
    private String Email;
    private String ContactNo1;
    private String MaritalStatusID;
    private String MaritalStatus;
    private String BloodGroupID;
    private String BloodGroup;
    private String GenderID;
    private String Gender;
    private String ComplaintsID;
    private String Complaints;
    private String DepartmentID;
    private String Department;
    private String DoctorID;
    private String ReferredDoctorID;
    private String ReferredDoctor;
    private String IsBilledSubmitID;
    private String ISStatus;

    public PatientQueue() {

    }

    public PatientQueue(String ID, String UnitId, String FromTime, String ToTime,
                        String PatientId, String FirstName, String MiddleName, String LastName, String DateOfBirth,
                        String Email, String ContactNo1, String MaritalStatus, String BloodGroup, String Gender, String VisitDescription) {
        this.ID = ID;
        this.UnitId = UnitId;
        this.FromTime = FromTime;
        this.ToTime = ToTime;
        this.PatientId = PatientId;
        this.FirstName = FirstName;
        this.MiddleName = MiddleName;
        this.LastName = LastName;
        this.DateOfBirth = DateOfBirth;
        this.Email = Email;
        this.ContactNo1 = ContactNo1;
        this.MaritalStatus = MaritalStatus;
        this.BloodGroup = BloodGroup;
        this.Gender = Gender;
        this.VisitDescription = VisitDescription;
    }

    @JsonProperty("ID")
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @JsonProperty("UnitId")
    public String getUnitId() {
        return UnitId;
    }

    public void setUnitId(String unitId) {
        UnitId = unitId;
    }

    @JsonProperty("Date")
    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    @JsonProperty("FromTime")
    public String getFromTime() {
        return FromTime;
    }

    public void setFromTime(String fromTime) {
        FromTime = fromTime;
    }

    @JsonProperty("ToTime")
    public String getToTime() {
        return ToTime;
    }

    public void setToTime(String toTime) {
        ToTime = toTime;
    }

    @JsonProperty("Queuetime")
    public String getQueuetime() {
        return Queuetime;
    }

    public void setQueuetime(String queuetime) {
        Queuetime = queuetime;
    }

    @JsonProperty("VisitID")
    public String getVisitID() {
        return VisitID;
    }

    public void setVisitID(String visitID) {
        VisitID = visitID;
    }

    @JsonProperty("PatientId")
    public String getPatientId() {
        return PatientId;
    }

    public void setPatientId(String patientId) {
        PatientId = patientId;
    }

    @JsonProperty("PatientUnitID")
    public String getPatientUnitID() {
        return PatientUnitID;
    }

    public void setPatientUnitID(String patientUnitID) {
        PatientUnitID = patientUnitID;
    }

    @JsonProperty("OPDNO")
    public String getOPDNO() {
        return OPDNO;
    }

    public void setOPDNO(String OPDNO) {
        this.OPDNO = OPDNO;
    }

    @JsonProperty("VisitTypeID")
    public String getVisitTypeID() {
        return VisitTypeID;
    }

    public void setVisitTypeID(String visitTypeID) {
        VisitTypeID = visitTypeID;
    }

    @JsonProperty("VisitDescription")
    public String getVisitDescription() {
        return VisitDescription;
    }

    public void setVisitDescription(String visitDescription) {
        VisitDescription = visitDescription;
    }

    @JsonProperty("MRNo")
    public String getMRNo() {
        return MRNo;
    }

    public void setMRNo(String MRNo) {
        this.MRNo = MRNo;
    }

    @JsonProperty("FirstName")
    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    @JsonProperty("MiddleName")
    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    @JsonProperty("LastName")
    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    @JsonProperty("DateOfBirth")
    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    @JsonProperty("Email")
    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @JsonProperty("ContactNo1")
    public String getContactNo1() {
        return ContactNo1;
    }

    public void setContactNo1(String contactNo1) {
        ContactNo1 = contactNo1;
    }

    @JsonProperty("MaritalStatusID")
    public String getMaritalStatusID() {
        return MaritalStatusID;
    }

    public void setMaritalStatusID(String maritalStatusID) {
        MaritalStatusID = maritalStatusID;
    }

    @JsonProperty("MaritalStatus")
    public String getMaritalStatus() {
        return MaritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        MaritalStatus = maritalStatus;
    }

    @JsonProperty("BloodGroupID")
    public String getBloodGroupID() {
        return BloodGroupID;
    }

    public void setBloodGroupID(String bloodGroupID) {
        BloodGroupID = bloodGroupID;
    }

    @JsonProperty("BloodGroup")
    public String getBloodGroup() {
        return BloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }

    @JsonProperty("GenderID")
    public String getGenderID() {
        return GenderID;
    }

    public void setGenderID(String genderID) {
        GenderID = genderID;
    }

    @JsonProperty("Gender")
    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    @JsonProperty("ComplaintsID")
    public String getComplaintsID() {
        return ComplaintsID;
    }

    public void setComplaintsID(String complaintsID) {
        ComplaintsID = complaintsID;
    }

    @JsonProperty("Complaints")
    public String getComplaints() {
        return Complaints;
    }

    public void setComplaints(String complaints) {
        Complaints = complaints;
    }

    @JsonProperty("DepartmentID")
    public String getDepartmentID() {
        return DepartmentID;
    }

    public void setDepartmentID(String departmentID) {
        DepartmentID = departmentID;
    }

    @JsonProperty("Department")
    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    @JsonProperty("DoctorID")
    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String doctorID) {
        DoctorID = doctorID;
    }

    @JsonProperty("ReferredDoctorID")
    public String getReferredDoctorID() {
        return ReferredDoctorID;
    }

    public void setReferredDoctorID(String referredDoctorID) {
        ReferredDoctorID = referredDoctorID;
    }

    @JsonProperty("ReferredDoctor")
    public String getReferredDoctor() {
        return ReferredDoctor;
    }

    public void setReferredDoctor(String referredDoctor) {
        ReferredDoctor = referredDoctor;
    }

    @JsonProperty("IsBilledSubmitID")
    public String getIsBilledSubmitID() {
        return IsBilledSubmitID;
    }

    public void setIsBilledSubmitID(String isBilledSubmitID) {
        IsBilledSubmitID = isBilledSubmitID;
    }

    @JsonProperty("ISStatus")
    public String getISStatus() {
        return ISStatus;
    }

    public void setISStatus(String ISStatus) {
        this.ISStatus = ISStatus;
    }
}
