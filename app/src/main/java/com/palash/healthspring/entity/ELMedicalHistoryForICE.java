/**
 * Bean to store information about user and their settings
 */
package com.palash.healthspring.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ELMedicalHistoryForICE {
    private String ID;
    private String PatientID;
    private String PatientUnitID;
    private String Template;
    private String Value;
    private String L1;
    private String L3;

    @JsonProperty("ID")
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    @JsonProperty("Template")
    public String getTemplate() {
        return Template;
    }

    public void setTemplate(String template) {
        Template = template;
    }

    @JsonProperty("Value")
    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    @JsonProperty("L1")
    public String getL1() {
        return L1;
    }

    public void setL1(String l1) {
        L1 = l1;
    }

    @JsonProperty("L3")
    public String getL3() {
        return L3;
    }

    public void setL3(String l3) {
        L3 = l3;
    }
}
