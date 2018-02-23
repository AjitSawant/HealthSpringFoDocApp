/**
 * Bean to store information about user and their settings
 */
package com.palash.healthspringapp.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ELPackageValidity {
    private String IsMonth;
    private String PackageEffectiveDate;
    private String PackageExpiryDate ;
    private String Validity ;
    private String PatientEffectiveDate ;
    private String PatientExpiryDate;

    @JsonProperty("IsMonth")
    public String getIsMonth() {
        return IsMonth;
    }

    public void setIsMonth(String isMonth) {
        IsMonth = isMonth;
    }

    @JsonProperty("PackageEffectiveDate")
    public String getPackageEffectiveDate() {
        return PackageEffectiveDate;
    }

    public void setPackageEffectiveDate(String packageEffectiveDate) {
        PackageEffectiveDate = packageEffectiveDate;
    }

    @JsonProperty("PackageExpiryDate")
    public String getPackageExpiryDate() {
        return PackageExpiryDate;
    }

    public void setPackageExpiryDate(String packageExpiryDate) {
        PackageExpiryDate = packageExpiryDate;
    }

    @JsonProperty("Validity")
    public String getValidity() {
        return Validity;
    }

    public void setValidity(String validity) {
        Validity = validity;
    }

    @JsonProperty("PatientEffectiveDate")
    public String getPatientEffectiveDate() {
        return PatientEffectiveDate;
    }

    public void setPatientEffectiveDate(String patientEffectiveDate) {
        PatientEffectiveDate = patientEffectiveDate;
    }

    @JsonProperty("PatientExpiryDate")
    public String getPatientExpiryDate() {
        return PatientExpiryDate;
    }

    public void setPatientExpiryDate(String patientExpiryDate) {
        PatientExpiryDate = patientExpiryDate;
    }
}
