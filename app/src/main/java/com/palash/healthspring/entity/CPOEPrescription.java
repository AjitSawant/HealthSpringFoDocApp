package com.palash.healthspring.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by manishas on 7/29/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CPOEPrescription {

    private int _ID;
    private String ID;
    private String UnitID;
    private String VisitID;
    private String PatientID;
    private String PatientUnitID;
    private String DoctorID;
    private String TemplateID;
    private String PrescriptionID;
    private String DrugID;
    private String ItemName;
    private String Dose;
    private String Route;
    private String RouteID;
    private String Frequency;
    private String FrequencyID;
    private String Days;
    private String Quantity;
    private String IsOther;
    private String Reason;
    private String ReasonID;
    private String FromHistory;
    private String Date;
    private String Rate;
    private String BilledDate;
    private String GeneralInstruction;
    private String IsDespence;
    private String Status;
    private String AddedBy;
    private String UpdatedDateTime;
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

    @JsonProperty("DoctorID")
    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String doctorID) {
        DoctorID = doctorID;
    }

    @JsonProperty("TemplateID")
    public String getTemplateID() {
        return TemplateID;
    }

    public void setTemplateID(String templateID) {
        TemplateID = templateID;
    }

    @JsonProperty("PrescriptionID")
    public String getPrescriptionID() {
        return PrescriptionID;
    }

    public void setPrescriptionID(String prescriptionID) {
        PrescriptionID = prescriptionID;
    }

    @JsonProperty("DrugID")
    public String getDrugID() {
        return DrugID;
    }

    public void setDrugID(String drugID) {
        DrugID = drugID;
    }

    @JsonProperty("ItemName")
    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    @JsonProperty("Dose")
    public String getDose() {
        return Dose;
    }

    public void setDose(String dose) {
        Dose = dose;
    }

    @JsonProperty("Route")
    public String getRoute() {
        return Route;
    }

    public void setRoute(String route) {
        Route = route;
    }

    @JsonProperty("RouteID")
    public String getRouteID() {
        return RouteID;
    }

    public void setRouteID(String routeID) {
        RouteID = routeID;
    }

    @JsonProperty("Frequency")
    public String getFrequency() {
        return Frequency;
    }

    public void setFrequency(String frequency) {
        Frequency = frequency;
    }

    @JsonProperty("FrequencyID")
    public String getFrequencyID() {
        return FrequencyID;
    }

    public void setFrequencyID(String frequencyID) {
        FrequencyID = frequencyID;
    }

    @JsonProperty("Days")
    public String getDays() {
        return Days;
    }

    public void setDays(String days) {
        Days = days;
    }

    @JsonProperty("Quantity")
    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    @JsonProperty("IsOther")
    public String getIsOther() {
        return IsOther;
    }

    public void setIsOther(String isOther) {
        IsOther = isOther;
    }

    @JsonProperty("Reason")
    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    @JsonProperty("ReasonID")
    public String getReasonID() {
        return ReasonID;
    }

    public void setReasonID(String reasonID) {
        ReasonID = reasonID;
    }

    @JsonProperty("FromHistory")
    public String getFromHistory() {
        return FromHistory;
    }

    public void setFromHistory(String fromHistory) {
        FromHistory = fromHistory;
    }

    @JsonProperty("Date")
    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    @JsonProperty("Rate")
    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    @JsonProperty("BilledDate")
    public String getBilledDate() {
        return BilledDate;
    }

    public void setBilledDate(String billedDate) {
        BilledDate = billedDate;
    }

    @JsonProperty("GeneralInstruction")
    public String getGeneralInstruction() {
        return GeneralInstruction;
    }

    public void setGeneralInstruction(String generalInstruction) {
        GeneralInstruction = generalInstruction;
    }

    @JsonProperty("IsDespence")
    public String getIsDespence() {
        return IsDespence;
    }

    public void setIsDespence(String isDespence) {
        IsDespence = isDespence;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @JsonProperty("AddedBy")
    public String getAddedBy() {
        return AddedBy;
    }

    public void setAddedBy(String addedBy) {
        AddedBy = addedBy;
    }

    @JsonProperty("UpdatedDateTime")
    public String getUpdatedDateTime() {
        return UpdatedDateTime;
    }

    public void setUpdatedDateTime(String updatedDateTime) {
        UpdatedDateTime = updatedDateTime;
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
