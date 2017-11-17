/**
 * Bean to store information about user and their settings
 */
package com.palash.healthspring.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ComplaintsList {

    private int _ID;
    private String ID;
    private String UnitID;
    private String VisitID;
    private String VisitUnitID;
    private String PatientID;
    private String PatientUnitID;
    private String ChiefComplaints;
    private String AssosciateComplaints;
    private String Remark;
    private String Status;
    private String DoctorID;
    private String CreatedUnitID;
    private String UpdatedUnitID;
    private String AddedBy;
    private String AddedDateTime;
    private String UpdatedBy;
    private String UpdatedDateTime;
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

    @JsonProperty("VisitUnitID")
    public String getVisitUnitID() {
        return VisitUnitID;
    }

    public void setVisitUnitID(String visitUnitID) {
        VisitUnitID = visitUnitID;
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

    @JsonProperty("ChiefComplaints")
    public String getChiefComplaints() {
        return ChiefComplaints;
    }

    public void setChiefComplaints(String chiefComplaints) {
        ChiefComplaints = chiefComplaints;
    }

    @JsonProperty("AssosciateComplaints")
    public String getAssosciateComplaints() {
        return AssosciateComplaints;
    }

    public void setAssosciateComplaints(String assosciateComplaints) {
        AssosciateComplaints = assosciateComplaints;
    }

    @JsonProperty("Remark")
    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @JsonProperty("DoctorID")
    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String doctorID) {
        DoctorID = doctorID;
    }

    @JsonProperty("CreatedUnitID")
    public String getCreatedUnitID() {
        return CreatedUnitID;
    }

    public void setCreatedUnitID(String createdUnitID) {
        CreatedUnitID = createdUnitID;
    }

    @JsonProperty("UpdatedUnitID")
    public String getUpdatedUnitID() {
        return UpdatedUnitID;
    }

    public void setUpdatedUnitID(String updatedUnitID) {
        UpdatedUnitID = updatedUnitID;
    }

    @JsonProperty("AddedBy")
    public String getAddedBy() {
        return AddedBy;
    }

    public void setAddedBy(String addedBy) {
        AddedBy = addedBy;
    }

    @JsonProperty("AddedDateTime")
    public String getAddedDateTime() {
        return AddedDateTime;
    }

    public void setAddedDateTime(String addedDateTime) {
        AddedDateTime = addedDateTime;
    }

    @JsonProperty("UpdatedBy")
    public String getUpdatedBy() {
        return UpdatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        UpdatedBy = updatedBy;
    }

    @JsonProperty("UpdatedDateTime")
    public String getUpdatedDateTime() {
        return UpdatedDateTime;
    }

    public void setUpdatedDateTime(String updatedDateTime) {
        UpdatedDateTime = updatedDateTime;
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
