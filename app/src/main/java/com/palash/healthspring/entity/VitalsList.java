package com.palash.healthspring.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VitalsList {

    private int _ID;
    private String ID;
    private String UnitID;
    private String VisitID;
    private String PatientID;
    private String PatientUnitID;
    private String TemplateID;
    private String DoctorID;
    private String Date;
    private String Time;
    private String VitalID;
    private String VitalsDecription;
    private String Value;
    private String Unit;
    private String Status;
    private String AddedBy;
    private String VisitDateTime;
    private String ISStatus;
    private String ISLocal;
    private String IsSync;
    private String IsUpdate;

    @JsonProperty("_ID")
    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
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

    @JsonProperty("VisitID")
    public String getVisitID() {
        return VisitID;
    }

    public void setVisitID(String visitID) {
        VisitID = visitID;
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

    @JsonProperty("TemplateID")
    public String getTemplateID() {
        return TemplateID;
    }

    public void setTemplateID(String templateID) {
        TemplateID = templateID;
    }

    @JsonProperty("DoctorID")
    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String doctorID) {
        DoctorID = doctorID;
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

    @JsonProperty("VitalID")
    public String getVitalID() {
        return VitalID;
    }

    public void setVitalID(String vitalID) {
        VitalID = vitalID;
    }

    @JsonProperty("VitalsDecription")
    public String getVitalsDecription() {
        return VitalsDecription;
    }

    public void setVitalsDecription(String vitalsDecription) {
        VitalsDecription = vitalsDecription;
    }

    @JsonProperty("Value")
    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    @JsonProperty("Unit")
    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @JsonProperty("ISLocal")
    public String getISLocal() {
        return ISLocal;
    }

    public void setISLocal(String ISLocal) {
        this.ISLocal = ISLocal;
    }

    @JsonProperty("AddedBy")
    public String getAddedBy() {
        return AddedBy;
    }

    public void setAddedBy(String addedBy) {
        AddedBy = addedBy;
    }

    @JsonProperty("VisitDateTime")
    public String getVisitDateTime() {
        return VisitDateTime;
    }

    public void setVisitDateTime(String visitDateTime) {
        VisitDateTime = visitDateTime;
    }

    @JsonProperty("ISStatus")
    public String getISStatus() {
        return ISStatus;
    }

    public void setISStatus(String ISStatus) {
        this.ISStatus = ISStatus;
    }

    @JsonProperty("IsSync")
    public String getIsSync() {
        return IsSync;
    }

    public void setIsSync(String isSync) {
        IsSync = isSync;
    }

    @JsonProperty("IsUpdate")
    public String getIsUpdate() {
        return IsUpdate;
    }

    public void setIsUpdate(String isUpdate) {
        IsUpdate = isUpdate;
    }
}
