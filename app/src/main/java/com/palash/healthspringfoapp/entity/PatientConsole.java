/**
 * Bean to store information about user and their settings
 */
package com.palash.healthspringfoapp.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientConsole {
    private String ID;
    private String PatientID;
    private String UnitID;
    private String PatientUnitID;
    private String DoctorID;
    private String VisitedUnitID;
    private String FromDate;
    private String ToDate;
    private String VisitID;
    private String VisitDate;
    private String visitDoctor;
    private String clinic;
    private String OPDNO;
    private String VisitType;
    private String Prescription;
    private String Radiology;
    private String Pathology;
    private String SuggestedService;
    private String Referal;
    private String EMR;
    private String Attachment;
    private String FilePath;

    @JsonProperty("ID")
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @JsonProperty("VisitID")
    public String getVisitID() {
        return VisitID;
    }

    public void setVisitID(String visitID) {
        VisitID = visitID;
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

    @JsonProperty("VisitedUnitID")
    public String getVisitedUnitID() {
        return VisitedUnitID;
    }

    public void setVisitedUnitID(String visitedUnitID) {
        VisitedUnitID = visitedUnitID;
    }

    @JsonProperty("FromDate")
    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    @JsonProperty("ToDate")
    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    @JsonProperty("VisitDate")
    public String getVisitDate() {
        return VisitDate;
    }

    public void setVisitDate(String visitDate) {
        VisitDate = visitDate;
    }

    @JsonProperty("visitDoctor")
    public String getVisitDoctor() {
        return visitDoctor;
    }

    public void setVisitDoctor(String visitDoctor) {
        this.visitDoctor = visitDoctor;
    }

    @JsonProperty("clinic")
    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

    @JsonProperty("OPDNO")
    public String getOPDNO() {
        return OPDNO;
    }

    public void setOPDNO(String OPDNO) {
        this.OPDNO = OPDNO;
    }

    @JsonProperty("VisitType")
    public String getVisitType() {
        return VisitType;
    }

    public void setVisitType(String visitType) {
        VisitType = visitType;
    }

    @JsonProperty("Prescription")
    public String getPrescription() {
        return Prescription;
    }

    public void setPrescription(String prescription) {
        Prescription = prescription;
    }

    @JsonProperty("Radiology")
    public String getRadiology() {
        return Radiology;
    }

    public void setRadiology(String radiology) {
        Radiology = radiology;
    }

    @JsonProperty("Pathology")
    public String getPathology() {
        return Pathology;
    }

    public void setPathology(String pathology) {
        Pathology = pathology;
    }

    @JsonProperty("SuggestedService")
    public String getSuggestedService() {
        return SuggestedService;
    }

    public void setSuggestedService(String suggestedService) {
        SuggestedService = suggestedService;
    }

    @JsonProperty("Referal")
    public String getReferal() {
        return Referal;
    }

    public void setReferal(String referal) {
        Referal = referal;
    }

    @JsonProperty("EMR")
    public String getEMR() {
        return EMR;
    }

    public void setEMR(String EMR) {
        this.EMR = EMR;
    }

    @JsonProperty("Attachment")
    public String getAttachment() {
        return Attachment;
    }

    public void setAttachment(String attachment) {
        Attachment = attachment;
    }

    @JsonProperty("FilePath")
    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }
}
