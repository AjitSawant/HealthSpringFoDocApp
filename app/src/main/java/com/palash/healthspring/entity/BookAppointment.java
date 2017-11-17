package com.palash.healthspring.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by manishas on 6/3/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookAppointment {
    private String ID;
    private String UnitID;
    private String PatientID;
    private String FirstName;
    private String MiddleName;
    private String LastName;
    private String GenderID;
    private String DOB;
    private String MaritalStatusID;
    private String Contact1;
    private String EmailId;
    private String BloodGroupID;
    private String DepartmentID;
    private String DoctorID;
    private String DoctorUnitID;
    private String DoctorName;
    private String Specialization;
    private String DoctorEducation;
    private String DoctorMobileNo;
    private String AppointmentReasonID;
    private String ComplaintId;
    private String AppointmentDate;
    private String FromTime;
    private String ToTime;
    private String Remark;
    private String ReSchedulingReason;
    private String AppointmentId;
    private String VisitID;
    private String AddedBy;

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

    @JsonProperty("PatientID")
    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        PatientID = patientID;
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

    @JsonProperty("GenderID")
    public String getGenderID() {
        return GenderID;
    }

    public void setGenderID(String genderID) {
        GenderID = genderID;
    }

    @JsonProperty("DOB")
    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    @JsonProperty("Contact1")
    public String getContact1() {
        return Contact1;
    }

    public void setContact1(String contact1) {
        Contact1 = contact1;
    }

    @JsonProperty("MaritalStatusID")
    public String getMaritalStatusID() {
        return MaritalStatusID;
    }

    public void setMaritalStatusID(String maritalStatusID) {
        MaritalStatusID = maritalStatusID;
    }

    @JsonProperty("EmailId")
    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    @JsonProperty("BloodGroupID")
    public String getBloodGroupID() {
        return BloodGroupID;
    }

    public void setBloodGroupID(String bloodGroupID) {
        BloodGroupID = bloodGroupID;
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

    @JsonProperty("DoctorUnitID")
    public String getDoctorUnitID() {
        return DoctorUnitID;
    }

    public void setDoctorUnitID(String doctorUnitID) {
        DoctorUnitID = doctorUnitID;
    }

    @JsonProperty("DoctorName")
    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    @JsonProperty("Specialization")
    public String getSpecialization() {
        return Specialization;
    }

    public void setSpecialization(String specialization) {
        Specialization = specialization;
    }

    @JsonProperty("DoctorEducation")
    public String getDoctorEducation() {
        return DoctorEducation;
    }

    public void setDoctorEducation(String doctorEducation) {
        DoctorEducation = doctorEducation;
    }

    @JsonProperty("DoctorMobileNo")
    public String getDoctorMobileNo() {
        return DoctorMobileNo;
    }

    public void setDoctorMobileNo(String doctorMobileNo) {
        DoctorMobileNo = doctorMobileNo;
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

    @JsonProperty("ComplaintId")
    public String getComplaintId() {
        return ComplaintId;
    }

    public void setComplaintId(String complaintId) {
        ComplaintId = complaintId;
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

    @JsonProperty("Remark")
    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    @JsonProperty("ReSchedulingReason")
    public String getReSchedulingReason() {
        return ReSchedulingReason;
    }

    public void setReSchedulingReason(String rescheduleReason) {
        ReSchedulingReason = rescheduleReason;
    }

    @JsonProperty("AppointmentId")
    public String getAppointmentId() {
        return AppointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        AppointmentId = appointmentId;
    }

    @JsonProperty("VisitID")
    public String getVisitID() {
        return VisitID;
    }

    public void setVisitID(String visitID) {
        VisitID = visitID;
    }

    @JsonProperty("AddedBy")
    public String getAddedBy() {
        return AddedBy;
    }

    public void setAddedBy(String addedBy) {
        AddedBy = addedBy;
    }
}
