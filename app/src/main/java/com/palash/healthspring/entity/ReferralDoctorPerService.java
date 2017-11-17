/**
 * Bean to store information about user and their settings
 */
package com.palash.healthspring.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReferralDoctorPerService {

    private int _ID;
    private String ID;
    private String UnitID;
    private String PatientID;
    private String PatientUnitID;
    private String DoctorID;
    private String VisitId;
    private String DepartmentID;
    private String ServiceID;
    private String ServiceName;
    private String ReferralDoctorID;
    private String ReferralDoctorName;
    private String Rate;
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

    @JsonProperty("DoctorID")
    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String doctorID) {
        DoctorID = doctorID;
    }

    @JsonProperty("VisitId")
    public String getVisitId() {
        return VisitId;
    }

    public void setVisitId(String visitId) {
        VisitId = visitId;
    }

    @JsonProperty("DepartmentID")
    public String getDepartmentID() {
        return DepartmentID;
    }

    public void setDepartmentID(String departmentID) {
        DepartmentID = departmentID;
    }

    @JsonProperty("ServiceID")
    public String getServiceID() {
        return ServiceID;
    }

    public void setServiceID(String serviceID) {
        ServiceID = serviceID;
    }

    @JsonProperty("ServiceName")
    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    @JsonProperty("ReferralDoctorID")
    public String getReferralDoctorID() {
        return ReferralDoctorID;
    }

    public void setReferralDoctorID(String referralDoctorID) {
        ReferralDoctorID = referralDoctorID;
    }

    @JsonProperty("ReferralDoctorName")
    public String getReferralDoctorName() {
        return ReferralDoctorName;
    }

    public void setReferralDoctorName(String referralDoctorName) {
        ReferralDoctorName = referralDoctorName;
    }

    @JsonProperty("Rate")
    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
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
