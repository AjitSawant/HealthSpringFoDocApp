package com.palash.healthspringapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Synchronization {

    private String ID;
    private String DoctorID;
    private String UnitID;
    private String DoctorProfileCount;
    private String UnitMasterCount;
    private String AppointmentCount;
    private String PatientCount;
    private String DoctorTypeCount;
    private String PrefixCount;
    private String GenderCount;
    private String MaritalStatusCount;
    private String SpecializationCount;
    private String AppointmentReasonCount;
    private String DepartmentCount;
    private String ComplaintCount;
    private String BloodGroupCount;
    private String PatientQueueCount;
    private String VisitCount;
    private String MedicienNameCount;
    private String MedicienRouteCount;
    private String MedicienFrequencyCount;
    private String MedicienInstructionCount;
    private String VitalCount;
    private String DaignosisCount;
    private String DaignosisTypeCount;
    private String ServiceNameCount;
    private String PriorityCount;
    private String ControlCaptionCount;
    private String MoleculeCount;
    private String CountryCount;
    private String RegionCount;
    private String StateCount;
    private String CityCount;
    private String ReferralFromCount;
    private String PatientCategoryL1Count;
    private String DoctorNameCount;
    private String PCPDoctorCount;
    private String CompanyNameCount;
    private String VisitTypeCount;

    @JsonProperty("ID")
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @JsonProperty("BloodGroupCount")
    public String getBloodGroupCount() {
        return BloodGroupCount;
    }

    public void setBloodGroupCount(String bloodGroupCount) {
        BloodGroupCount = bloodGroupCount;
    }

    @JsonProperty("ComplaintCount")
    public String getComplaintCount() {
        return ComplaintCount;
    }

    public void setComplaintCount(String complaintCount) {
        ComplaintCount = complaintCount;
    }

    @JsonProperty("DepartmentCount")
    public String getDepartmentCount() {
        return DepartmentCount;
    }

    public void setDepartmentCount(String departmentCount) {
        DepartmentCount = departmentCount;
    }

    @JsonProperty("DoctorID")
    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String doctorID) {
        DoctorID = doctorID;
    }

    @JsonProperty("UnitID")
    public String getUnitID() {
        return UnitID;
    }

    public void setUnitID(String unitID) {
        UnitID = unitID;
    }

    @JsonProperty("DoctorProfileCount")
    public String getDoctorProfileCount() {
        return DoctorProfileCount;
    }

    public void setDoctorProfileCount(String doctorProfileCount) {
        DoctorProfileCount = doctorProfileCount;
    }

    @JsonProperty("UnitMasterCount")
    public String getUnitMasterCount() {
        return UnitMasterCount;
    }

    public void setUnitMasterCount(String unitMasterCount) {
        UnitMasterCount = unitMasterCount;
    }

    @JsonProperty("AppointmentCount")
    public String getAppointmentCount() {
        return AppointmentCount;
    }

    public void setAppointmentCount(String appointmentCount) {
        AppointmentCount = appointmentCount;
    }

    @JsonProperty("PatientCount")
    public String getPatientCount() {
        return PatientCount;
    }

    public void setPatientCount(String patientCount) {
        PatientCount = patientCount;
    }

    @JsonProperty("DoctorTypeCount")
    public String getDoctorTypeCount() {
        return DoctorTypeCount;
    }

    public void setDoctorTypeCount(String doctorTypeCount) {
        DoctorTypeCount = doctorTypeCount;
    }

    @JsonProperty("PrefixCount")
    public String getPrefixCount() {
        return PrefixCount;
    }

    public void setPrefixCount(String prefixCount) {
        PrefixCount = prefixCount;
    }

    @JsonProperty("GenderCount")
    public String getGenderCount() {
        return GenderCount;
    }

    public void setGenderCount(String genderCount) {
        GenderCount = genderCount;
    }

    @JsonProperty("MaritalStatusCount")
    public String getMaritalStatusCount() {
        return MaritalStatusCount;
    }

    public void setMaritalStatusCount(String maritalStatusCount) {
        MaritalStatusCount = maritalStatusCount;
    }

    @JsonProperty("SpecializationCount")
    public String getSpecializationCount() {
        return SpecializationCount;
    }

    public void setSpecializationCount(String specializationCount) {
        SpecializationCount = specializationCount;
    }

    @JsonProperty("AppointmentReasonCount")
    public String getAppointmentReasonCount() {
        return AppointmentReasonCount;
    }

    public void setAppointmentReasonCount(String appointmentReasonCount) {
        AppointmentReasonCount = appointmentReasonCount;
    }

    @JsonProperty("PatientQueueCount")
    public String getPatientQueueCount() {
        return PatientQueueCount;
    }

    public void setPatientQueueCount(String patientQueueCount) {
        PatientQueueCount = patientQueueCount;
    }

    @JsonProperty("VisitCount")
    public String getVisitCount() {
        return VisitCount;
    }

    public void setVisitCount(String visitCount) {
        VisitCount = visitCount;
    }

    @JsonProperty("MedicienNameCount")
    public String getMedicienNameCount() {
        return MedicienNameCount;
    }

    public void setMedicienNameCount(String medicienNameCount) {
        MedicienNameCount = medicienNameCount;
    }

    @JsonProperty("MedicienRouteCount")
    public String getMedicienRouteCount() {
        return MedicienRouteCount;
    }

    public void setMedicienRouteCount(String medicienRouteCount) {
        MedicienRouteCount = medicienRouteCount;
    }

    @JsonProperty("MedicienFrequencyCount")
    public String getMedicienFrequencyCount() {
        return MedicienFrequencyCount;
    }

    public void setMedicienFrequencyCount(String medicienFrequencyCount) {
        MedicienFrequencyCount = medicienFrequencyCount;
    }

    @JsonProperty("MedicienInstructionCount")
    public String getMedicienInstructionCount() {
        return MedicienInstructionCount;
    }

    public void setMedicienInstructionCount(String medicienInstructionCount) {
        MedicienInstructionCount = medicienInstructionCount;
    }

    @JsonProperty("VitalCount")
    public String getVitalCount() {
        return VitalCount;
    }

    public void setVitalCount(String vitalCount) {
        VitalCount = vitalCount;
    }

    @JsonProperty("DaignosisCount")
    public String getDaignosisCount() {
        return DaignosisCount;
    }

    public void setDaignosisCount(String daignosisCount) {
        DaignosisCount = daignosisCount;
    }

    @JsonProperty("DaignosisTypeCount")
    public String getDaignosisTypeCount() {
        return DaignosisTypeCount;
    }

    public void setDaignosisTypeCount(String daignosisTypeCount) {
        DaignosisTypeCount = daignosisTypeCount;
    }

    @JsonProperty("ServiceNameCount")
    public String getServiceNameCount() {
        return ServiceNameCount;
    }

    public void setServiceNameCount(String serviceNameCount) {
        ServiceNameCount = serviceNameCount;
    }

    @JsonProperty("PriorityCount")
    public String getPriorityCount() {
        return PriorityCount;
    }

    public void setPriorityCount(String priorityCount) {
        PriorityCount = priorityCount;
    }

    @JsonProperty("ControlCaptionCount")
    public String getControlCaptionCount() {
        return ControlCaptionCount;
    }

    public void setControlCaptionCount(String controlCaptionCount) {
        ControlCaptionCount = controlCaptionCount;
    }

    @JsonProperty("MoleculeCount")
    public String getMoleculeCount() {
        return MoleculeCount;
    }

    public void setMoleculeCount(String moleculeCount) {
        MoleculeCount = moleculeCount;
    }

    @JsonProperty("CountryCount")
    public String getCountryCount() {
        return CountryCount;
    }

    public void setCountryCount(String countryCount) {
        CountryCount = countryCount;
    }

    @JsonProperty("RegionCount")
    public String getRegionCount() {
        return RegionCount;
    }

    public void setRegionCount(String regionCount) {
        RegionCount = regionCount;
    }

    @JsonProperty("StateCount")
    public String getStateCount() {
        return StateCount;
    }

    public void setStateCount(String stateCount) {
        StateCount = stateCount;
    }

    @JsonProperty("CityCount")
    public String getCityCount() {
        return CityCount;
    }

    public void setCityCount(String cityCount) {
        CityCount = cityCount;
    }

    @JsonProperty("ReferralFromCount")
    public String getReferralFromCount() {
        return ReferralFromCount;
    }

    public void setReferralFromCount(String referralFromCount) {
        ReferralFromCount = referralFromCount;
    }

    @JsonProperty("PatientCategoryL1Count")
    public String getPatientCategoryL1Count() {
        return PatientCategoryL1Count;
    }

    public void setPatientCategoryL1Count(String patientCategoryL1Count) {
        PatientCategoryL1Count = patientCategoryL1Count;
    }

    @JsonProperty("DoctorNameCount")
    public String getDoctorNameCount() {
        return DoctorNameCount;
    }

    public void setDoctorNameCount(String doctorNameCount) {
        DoctorNameCount = doctorNameCount;
    }

    @JsonProperty("PCPDoctorCount")
    public String getPCPDoctorCount() {
        return PCPDoctorCount;
    }

    public void setPCPDoctorCount(String PCPDoctorCount) {
        this.PCPDoctorCount = PCPDoctorCount;
    }

    @JsonProperty("VisitTypeCount")
    public String getVisitTypeCount() {
        return VisitTypeCount;
    }

    public void setVisitTypeCount(String visitTypeCount) {
        VisitTypeCount = visitTypeCount;
    }

    @JsonProperty("CompanyNameCount")
    public String getCompanyNameCount() {
        return CompanyNameCount;
    }

    public void setCompanyNameCount(String companyNameCount) {
        CompanyNameCount = companyNameCount;
    }
}
