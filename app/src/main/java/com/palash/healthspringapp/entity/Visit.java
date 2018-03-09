package com.palash.healthspringapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Visit {
    private String AppointmentId;
    private String PatientID;
    private String PatientUnitID;
    private String UnitID;
    private String VisitTypeID;
    private String Complaints;
    private String DepartmentID;
    private String DoctorID;
    private String ReferredDoctorID;
    private String ReferredDoctor;
    private String AddedBy;
    private String VisitTypeServiceID;
    private String VisitDateTime;
    private String AddedDateTime;
    private String Date;

    @JsonProperty("Date")
    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    @JsonProperty("AppointmentId")
    public String getAppointmentId() {
        return AppointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        AppointmentId = appointmentId;
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

    @JsonProperty("PatientUnitID")
    public String getPatientUnitID() {
        return PatientUnitID;
    }

    public void setPatientUnitID(String patientUnitID) {
        PatientUnitID = patientUnitID;
    }

    @JsonProperty("AddedDateTime")
    public String getAddedDateTime() {
        return AddedDateTime;
    }

    public void setAddedDateTime(String addedDateTime) {
        AddedDateTime = addedDateTime;
    }

    @JsonProperty("VisitTypeID")
    public String getVisitTypeID() {
        return VisitTypeID;
    }

    public void setVisitTypeID(String visitTypeID) {
        VisitTypeID = visitTypeID;
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

    @JsonProperty("AddedBy")
    public String getAddedBy() {
        return AddedBy;
    }

    public void setAddedBy(String addedBy) {
        AddedBy = addedBy;
    }

    @JsonProperty("VisitTypeServiceID")
    public String getVisitTypeServiceID() {
        return VisitTypeServiceID;
    }

    public void setVisitTypeServiceID(String visitTypeServiceID) {
        VisitTypeServiceID = visitTypeServiceID;
    }

    @JsonProperty("VisitDateTime")
    public String getVisitDateTime() {
        return VisitDateTime;
    }

    public void setVisitDateTime(String visitDateTime) {
        VisitDateTime = visitDateTime;
    }
}
