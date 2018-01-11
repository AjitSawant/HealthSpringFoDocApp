package com.palash.healthspring.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Appointment {
    private String ID;
    private String UnitID;
    private String UnitName;
    private String PatientID;
    private String PatientUnitID;
    private String VisitID;
    private String FirstName;
    private String MiddleName;
    private String LastName;
    private String DOB;
    private String Gender;
    private String BloodGroup;
    private String MaritalStatus;
    private String GenderID;
    private String BloodGroupID;
    private String MaritalStatusID;
    private String Contact1;
    private String EmailId;
    private String Department;
    private String DepartmentID;
    private String DoctorID;
    private String AppointmentReason;
    private String AppointmentReasonID;
    private String AppointmentDate;
    private String FromTime;
    private String ToTime;
    private String SearchAppointmentDate;
    private String Remark;
    private String AppCancelReason;
    private String Status;
    private String CreatedUnitID;
    private String VisitMark;
    private String AppointmentStatus;
    private String ParentappointID;
    private String ParentappointUnitID;
    private String ReSchedulingReason;
    private String Complaint;
    private String ComplaintId;
    private String AppointmentId;
    private String SortingDateTime;
    private String DrName;

    @JsonProperty("AppointmentId")
    public String getAppointmentId() {
        return AppointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        AppointmentId = appointmentId;
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

    public void setUnitID(String unitID) {
        UnitID = unitID;
    }

    @JsonProperty("UnitName")
    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    @JsonProperty("PatientID")
    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }

    @JsonProperty("PatientUnitID")
    public String getPatientUnitID() {
        return PatientUnitID;
    }

    public void setPatientUnitID(String patientUnitID) {
        PatientUnitID = patientUnitID;
    }

    @JsonProperty("VisitID")
    public String getVisitID() {
        return VisitID;
    }

    public void setVisitID(String visitID) {
        VisitID = visitID;
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

    @JsonProperty("DOB")
    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    @JsonProperty("Gender")
    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    @JsonProperty("BloodGroup")
    public String getBloodGroup() {
        return BloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }

    @JsonProperty("MaritalStatus")
    public String getMaritalStatus() {
        return MaritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        MaritalStatus = maritalStatus;
    }

    @JsonProperty("GenderID")
    public String getGenderID() {
        return GenderID;
    }

    public void setGenderID(String genderID) {
        GenderID = genderID;
    }

    @JsonProperty("BloodGroupID")
    public String getBloodGroupID() {
        return BloodGroupID;
    }

    public void setBloodGroupID(String bloodGroupID) {
        BloodGroupID = bloodGroupID;
    }

    @JsonProperty("MaritalStatusID")
    public String getMaritalStatusID() {
        return MaritalStatusID;
    }

    public void setMaritalStatusID(String maritalStatusID) {
        MaritalStatusID = maritalStatusID;
    }

    @JsonProperty("Contact1")
    public String getContact1() {
        return Contact1;
    }

    public void setContact1(String contact1) {
        Contact1 = contact1;
    }

    @JsonProperty("EmailId")
    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    @JsonProperty("Department")
    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
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

    public void setDoctorID(String doctorID) {
        DoctorID = doctorID;
    }

    @JsonProperty("AppointmentReason")
    public String getAppointmentReason() {
        return AppointmentReason;
    }

    public void setAppointmentReason(String appointmentReason) {
        AppointmentReason = appointmentReason;
    }

    @JsonProperty("AppointmentReasonID")
    public String getAppointmentReasonID() {
        return AppointmentReasonID;
    }

    public void setAppointmentReasonID(String appointmentReasonID) {
        AppointmentReasonID = appointmentReasonID;
    }

    @JsonProperty("AppointmentDate")
    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        AppointmentDate = appointmentDate;
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

    @JsonProperty("SearchAppointmentDate")
    public String getSearchAppointmentDate() {
        return SearchAppointmentDate;
    }

    public void setSearchAppointmentDate(String searchAppointmentDate) {
        SearchAppointmentDate = searchAppointmentDate;
    }

    @JsonProperty("Remark")
    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    @JsonProperty("AppCancelReason")
    public String getAppCancelReason() {
        return AppCancelReason;
    }

    public void setAppCancelReason(String appCancelReason) {
        AppCancelReason = appCancelReason;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @JsonProperty("CreatedUnitID")
    public String getCreatedUnitID() {
        return CreatedUnitID;
    }

    public void setCreatedUnitID(String createdUnitID) {
        CreatedUnitID = createdUnitID;
    }

    @JsonProperty("VisitMark")
    public String getVisitMark() {
        return VisitMark;
    }

    public void setVisitMark(String visitMark) {
        VisitMark = visitMark;
    }

    @JsonProperty("AppointmentStatus")
    public String getAppointmentStatus() {
        return AppointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        AppointmentStatus = appointmentStatus;
    }

    @JsonProperty("ParentappointID")
    public String getParentappointID() {
        return ParentappointID;
    }

    public void setParentappointID(String parentappointID) {
        ParentappointID = parentappointID;
    }

    @JsonProperty("ParentappointUnitID")
    public String getParentappointUnitID() {
        return ParentappointUnitID;
    }

    public void setParentappointUnitID(String parentappointUnitID) {
        ParentappointUnitID = parentappointUnitID;
    }

    @JsonProperty("ReSchedulingReason")
    public String getReSchedulingReason() {
        return ReSchedulingReason;
    }

    public void setReSchedulingReason(String reSchedulingReason) {
        ReSchedulingReason = reSchedulingReason;
    }

    @JsonProperty("Complaint")
    public String getComplaint() {
        return Complaint;
    }

    public void setComplaint(String complaint) {
        Complaint = complaint;
    }

    @JsonProperty("ComplaintId")
    public String getComplaintId() {
        return ComplaintId;
    }

    public void setComplaintId(String complaintId) {
        ComplaintId = complaintId;
    }

    @JsonProperty("SortingDateTime")
    public String getSortingDateTime() {
        return SortingDateTime;
    }

    public void setSortingDateTime(String sortingDateTime) {
        SortingDateTime = sortingDateTime;
    }

    @JsonProperty("DrName")
    public String getDrName() {
        return DrName;
    }

    public void setDrName(String drName) {
        DrName = drName;
    }
}
