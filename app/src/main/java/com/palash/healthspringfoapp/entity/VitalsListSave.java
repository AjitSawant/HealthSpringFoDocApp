package com.palash.healthspringfoapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VitalsListSave {

    private String UnitID;
    private String VisitID;
    private String PatientID;
    private String PatientUnitID;
    private String DoctorID;
    private String Date;
    private String Time;
    private ArrayList<VitalsList> allvitalsList;

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

    @JsonProperty("allvitalsList")
    public ArrayList<VitalsList> getAllvitalsList() {
        return allvitalsList;
    }

    public void setAllvitalsList(ArrayList<VitalsList> allvitalsList) {
        this.allvitalsList = allvitalsList;
    }
}
