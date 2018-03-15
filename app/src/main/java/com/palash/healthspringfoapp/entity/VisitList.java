package com.palash.healthspringfoapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VisitList {
    private String ID;
    private String UnitId;
    private String Date;
    private String Time;
    private String AppointmentDate;
    private String FromTime;
    private String ToTime;
    private String PatientId;
    private String PatientUnitId;
    private String FirstName;
    private String MiddleName;
    private String LastName;
    private String OPDNO;
    private String VisitTypeID;
    private String VisitDescription;
    private String ComplaintsID;
    private String Complaints;
    private String DepartmentID;
    private String Department;
    private String DoctorID;
    private String ReferredDoctorID;
    private String ReferredDoctor;
    private String VisitTypeServiceID;
    private String IsEMR;
    private String IsBasicEMR;
    private String IsLab;
    private String IsRadiology;
    private String IsProcedure;
    private String IsOtherService;
    private String IsCurrentMedication;
    private String IsPrescription;
    private String IsReferral;
    private String RefEntityType;
    private String RefEntityID;
    private String VisitDateTime;
    private String Status;
    private String VisitStatus;
    private String CurrentVisitStatus;
    private String UnitName;
    private String DrName;

    @JsonProperty("UnitID")
    public String getPatientUnitId() {
        return PatientUnitId;
    }

    public void setPatientUnitId(String patientUnitId) {
        PatientUnitId = patientUnitId;
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

    @JsonProperty("Time")
    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
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


    @JsonProperty("PatientId")
    public String getPatientId() {
        return PatientId;
    }

    public void setPatientId(String patientId) {
        PatientId = patientId;
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

    @JsonProperty("VisitTypeServiceID")
    public String getVisitTypeServiceID() {
        return VisitTypeServiceID;
    }

    public void setVisitTypeServiceID(String visitTypeServiceID) {
        VisitTypeServiceID = visitTypeServiceID;
    }

    @JsonProperty("IsEMR")
    public String getIsEMR() {
        return IsEMR;
    }

    public void setIsEMR(String isEMR) {
        IsEMR = isEMR;
    }

    @JsonProperty("IsBasicEMR")
    public String getIsBasicEMR() {
        return IsBasicEMR;
    }

    public void setIsBasicEMR(String isBasicEMR) {
        IsBasicEMR = isBasicEMR;
    }

    @JsonProperty("IsLab")
    public String getIsLab() {
        return IsLab;
    }

    public void setIsLab(String isLab) {
        IsLab = isLab;
    }

    @JsonProperty("IsRadiology")
    public String getIsRadiology() {
        return IsRadiology;
    }

    public void setIsRadiology(String isRadiology) {
        IsRadiology = isRadiology;
    }

    @JsonProperty("IsProcedure")
    public String getIsProcedure() {
        return IsProcedure;
    }

    public void setIsProcedure(String isProcedure) {
        IsProcedure = isProcedure;
    }

    @JsonProperty("IsOtherService")
    public String getIsOtherService() {
        return IsOtherService;
    }

    public void setIsOtherService(String isOtherService) {
        IsOtherService = isOtherService;
    }

    @JsonProperty("IsCurrentMedication")
    public String getIsCurrentMedication() {
        return IsCurrentMedication;
    }

    public void setIsCurrentMedication(String isCurrentMedication) {
        IsCurrentMedication = isCurrentMedication;
    }

    @JsonProperty("IsPrescription")
    public String getIsPrescription() {
        return IsPrescription;
    }

    public void setIsPrescription(String isPrescription) {
        IsPrescription = isPrescription;
    }

    @JsonProperty("IsReferral")
    public String getIsReferral() {
        return IsReferral;
    }

    public void setIsReferral(String isReferral) {
        IsReferral = isReferral;
    }

    @JsonProperty("RefEntityType")
    public String getRefEntityType() {
        return RefEntityType;
    }

    public void setRefEntityType(String refEntityType) {
        RefEntityType = refEntityType;
    }

    @JsonProperty("RefEntityID")
    public String getRefEntityID() {
        return RefEntityID;
    }

    public void setRefEntityID(String refEntityID) {
        RefEntityID = refEntityID;
    }

    @JsonProperty("VisitDateTime")
    public String getVisitDateTime() {
        return VisitDateTime;
    }

    public void setVisitDateTime(String visitDateTime) {
        VisitDateTime = visitDateTime;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @JsonProperty("VisitStatus")
    public String getVisitStatus() {
        return VisitStatus;
    }

    public void setVisitStatus(String visitStatus) {
        VisitStatus = visitStatus;
    }

    @JsonProperty("CurrentVisitStatus")
    public String getCurrentVisitStatus() {
        return CurrentVisitStatus;
    }

    public void setCurrentVisitStatus(String currentVisitStatus) {
        CurrentVisitStatus = currentVisitStatus;
    }

    @JsonProperty("UnitName")
    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    @JsonProperty("DrName")
    public String getDrName() {
        return DrName;
    }

    public void setDrName(String drName) {
        DrName = drName;
    }
}
