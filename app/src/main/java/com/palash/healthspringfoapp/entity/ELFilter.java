/**
 * Bean to store information about user and their settings
 */
package com.palash.healthspringfoapp.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ELFilter {

    private String UnitID;
    private String MRNo;
    private String FirstName;
    private String LastName;
    private String MobileNo;
    private String RegStartDate;
    private String RegEndDate;
    private String VisitStartDate;
    private String VisitEndDate;
    private String SelectedDoctorID;
    private String SelectedDeptID;
    private String SelectedCategoryID;
    private String SelectedVisitType;
    private String SelectedVisitStatus;
    private String SelectedAppointmentStatus;
    private String AppointmentStartDate;
    private String AppointmentEndDate;
    private String FilterFlag;

    @JsonProperty("UnitID")
    public String getUnitID() {
        return UnitID;
    }

    public void setUnitID(String unitID) {
        UnitID = unitID;
    }

    @JsonProperty("MRNo")
    public String getMRNo() {
        return MRNo;
    }

    public void setMRNo(String MRNo) {
        this.MRNo = MRNo;
    }

    @JsonProperty("FirstName")
    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    @JsonProperty("LastName")
    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    @JsonProperty("MobileNo")
    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    @JsonProperty("RegStartDate")
    public String getRegStartDate() {
        return RegStartDate;
    }

    public void setRegStartDate(String regStartDate) {
        RegStartDate = regStartDate;
    }

    @JsonProperty("RegEndDate")
    public String getRegEndDate() {
        return RegEndDate;
    }

    public void setRegEndDate(String regEndDate) {
        RegEndDate = regEndDate;
    }

    @JsonProperty("VisitStartDate")
    public String getVisitStartDate() {
        return VisitStartDate;
    }

    public void setVisitStartDate(String visitStartDate) {
        VisitStartDate = visitStartDate;
    }

    @JsonProperty("VisitEndDate")
    public String getVisitEndDate() {
        return VisitEndDate;
    }

    public void setVisitEndDate(String visitEndDate) {
        VisitEndDate = visitEndDate;
    }

    @JsonProperty("SelectedDoctorID")
    public String getSelectedDoctorID() {
        return SelectedDoctorID;
    }

    public void setSelectedDoctorID(String selectedDoctorID) {
        SelectedDoctorID = selectedDoctorID;
    }

    @JsonProperty("SelectedDeptID")
    public String getSelectedDeptID() {
        return SelectedDeptID;
    }

    public void setSelectedDeptID(String selectedDeptID) {
        SelectedDeptID = selectedDeptID;
    }

    @JsonProperty("SelectedCategoryID")
    public String getSelectedCategoryID() {
        return SelectedCategoryID;
    }

    public void setSelectedCategoryID(String selectedCategoryID) {
        SelectedCategoryID = selectedCategoryID;
    }

    @JsonProperty("SelectedVisitType")
    public String getSelectedVisitType() {
        return SelectedVisitType;
    }

    public void setSelectedVisitType(String selectedVisitType) {
        SelectedVisitType = selectedVisitType;
    }

    @JsonProperty("SelectedVisitStatus")
    public String getSelectedVisitStatus() {
        return SelectedVisitStatus;
    }

    public void setSelectedVisitStatus(String selectedVisitStatus) {
        SelectedVisitStatus = selectedVisitStatus;
    }

    @JsonProperty("SelectedAppointmentStatus")
    public String getSelectedAppointmentStatus() {
        return SelectedAppointmentStatus;
    }

    public void setSelectedAppointmentStatus(String selectedAppointmentStatus) {
        SelectedAppointmentStatus = selectedAppointmentStatus;
    }

    @JsonProperty("AppointmentStartDate")
    public String getAppointmentStartDate() {
        return AppointmentStartDate;
    }

    public void setAppointmentStartDate(String appointmentStartDate) {
        AppointmentStartDate = appointmentStartDate;
    }

    @JsonProperty("AppointmentEndDate")
    public String getAppointmentEndDate() {
        return AppointmentEndDate;
    }

    public void setAppointmentEndDate(String appointmentEndDate) {
        AppointmentEndDate = appointmentEndDate;
    }

    @JsonProperty("FilterFlag")
    public String getFilterFlag() {
        return FilterFlag;
    }

    public void setFilterFlag(String filterFlag) {
        FilterFlag = filterFlag;
    }
}
