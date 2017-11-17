package com.palash.healthspring.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by manishas on 7/21/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiagnosisList {

    private int _ID;
    private String ID;
    private String UnitID;
    private String PatientID;
    private String PatientUnitID;
    private String VisitId;
    private String DiagnosisID;
    private String Code;
    private String DiagnosisName;
    private String Date;
    private String ICDId;
    private String PrimaryDiagnosis;
    private String AddedBy;
    private String Status;
    private String DiagnosisTypeID;
    private String DiagnosisType;
    private String ResultStatus;
    private String Remark;
    private String ISStatus;
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

    @JsonProperty("VisitId")
    public String getVisitId() {
        return VisitId;
    }

    public void setVisitId(String visitId) {
        VisitId = visitId;
    }

    @JsonProperty("DiagnosisID")
    public String getDiagnosisID() {
        return DiagnosisID;
    }

    public void setDiagnosisID(String diagnosisID) {
        DiagnosisID = diagnosisID;
    }

    @JsonProperty("Code")
    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    @JsonProperty("DiagnosisName")
    public String getDiagnosisName() {
        return DiagnosisName;
    }

    public void setDiagnosisName(String diagnosisName) {
        DiagnosisName = diagnosisName;
    }

    @JsonProperty("Date")
    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    @JsonProperty("ICDId")
    public String getICDId() {
        return ICDId;
    }

    public void setICDId(String ICDId) {
        this.ICDId = ICDId;
    }

    @JsonProperty("PrimaryDiagnosis")
    public String getPrimaryDiagnosis() {
        return PrimaryDiagnosis;
    }

    public void setPrimaryDiagnosis(String primaryDiagnosis) {
        PrimaryDiagnosis = primaryDiagnosis;
    }

    @JsonProperty("AddedBy")
    public String getAddedBy() {
        return AddedBy;
    }

    public void setAddedBy(String addedBy) {
        AddedBy = addedBy;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @JsonProperty("DiagnosisTypeID")
    public String getDiagnosisTypeID() {
        return DiagnosisTypeID;
    }

    public void setDiagnosisTypeID(String diagnosisTypeID) {
        DiagnosisTypeID = diagnosisTypeID;
    }

    @JsonProperty("DiagnosisType")
    public String getDiagnosisType() {
        return DiagnosisType;
    }

    public void setDiagnosisType(String diagnosisType) {
        DiagnosisType = diagnosisType;
    }

    @JsonProperty("ResultStatus")
    public String getResultStatus() {
        return ResultStatus;
    }

    public void setResultStatus(String resultStatus) {
        ResultStatus = resultStatus;
    }

    @JsonProperty("Remark")
    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
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
