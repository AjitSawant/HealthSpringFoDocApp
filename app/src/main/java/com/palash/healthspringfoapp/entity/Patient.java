/**
 * Bean to store information about user and their settings
 */
package com.palash.healthspringfoapp.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Patient {

    private String PrefixID;
    private String FirstName;
    private String MiddleName;
    private String LastName;
    private String FamilyName;
    private String DateOfBirth;
    private String Age;
    private String GenderID;
    private String Gender;
    private String MaritalStatusID;
    private String MaritalStatus;
    private String BloodGroupID;
    private String BloodGroup;
    private String HealthSpringReferralID;
    private String Email;
    private String ContactNo1;
    private String FlatBuildingName;
    private String StreetName;
    private String CountryID;
    private String RegionID;
    private String StateID;
    private String CityID;

    private String CategoryL1ID;
    private String CompanyID;
    private String CategoryL2ID;
    private String CategoryL3ID;
    private String EffectiveDate;
    private String Expirydate;
    private String CardIssueDate;
    private String PCPDoctorID;
    private String DoctorNameID;

    private String RegistrationDate;
    private String UnitID;
    private String ClinicName;
    private String CreatedUnitID;
    private String AddedBy;

    private String ID;
    private String MRNo;

    public Patient() {

    }

    @JsonProperty("PrefixID")
    public String getPrefixID() {
        return PrefixID;
    }

    public void setPrefixID(String prefixID) {
        PrefixID = prefixID;
    }

    @JsonProperty("FirstName")
    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    @JsonProperty("MiddleName")
    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String MiddleName) {
        this.MiddleName = MiddleName;
    }

    @JsonProperty("LastName")
    public String getLastName() {
        return LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    @JsonProperty("FamilyName")
    public String getFamilyName() {
        return FamilyName;
    }

    public void setFamilyName(String FamilyName) {
        this.FamilyName = FamilyName;
    }

    @JsonProperty("DateOfBirth")
    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String DateOfBirth) {
        this.DateOfBirth = DateOfBirth;
    }

    @JsonProperty("Age")
    public String getAge() {
        return Age;
    }

    public void setAge(String Age) {
        this.Age = Age;
    }

    @JsonProperty("GenderID")
    public String getGenderID() {
        return GenderID;
    }

    public void setGenderID(String GenderID) {
        this.GenderID = GenderID;
    }

    @JsonProperty("Gender")
    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    @JsonProperty("MaritalStatusID")
    public String getMaritalStatusID() {
        return MaritalStatusID;
    }

    public void setMaritalStatusID(String MaritalStatusID) {
        this.MaritalStatusID = MaritalStatusID;
    }

    @JsonProperty("MaritalStatus")
    public String getMaritalStatus() {
        return MaritalStatus;
    }

    public void setMaritalStatus(String MaritalStatus) {
        this.MaritalStatus = MaritalStatus;
    }

    @JsonProperty("BloodGroupID")
    public String getBloodGroupID() {
        return BloodGroupID;
    }

    public void setBloodGroupID(String bloodGroupID) {
        BloodGroupID = bloodGroupID;
    }

    @JsonProperty("BloodGroup")
    public String getBloodGroup() {
        return BloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }

    @JsonProperty("PatientReferenceTypeID")
    public String getHealthSpringReferralID() {
        return HealthSpringReferralID;
    }

    public void setHealthSpringReferralID(String healthSpringReferralID) {
        HealthSpringReferralID = healthSpringReferralID;
    }

    @JsonProperty("Email")
    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    @JsonProperty("ContactNo1")
    public String getContactNo1() {
        return ContactNo1;
    }

    public void setContactNo1(String ContactNo1) {
        this.ContactNo1 = ContactNo1;
    }

    @JsonProperty("AddressLine1")
    public String getFlatBuildingName() {
        return FlatBuildingName;
    }

    public void setFlatBuildingName(String flatBuildingName) {
        FlatBuildingName = flatBuildingName;
    }

    @JsonProperty("AddressLine2")
    public String getStreetName() {
        return StreetName;
    }

    public void setStreetName(String streetName) {
        StreetName = streetName;
    }

    @JsonProperty("CountryID")
    public String getCountryID() {
        return CountryID;
    }

    public void setCountryID(String countryID) {
        CountryID = countryID;
    }

    @JsonProperty("RegionID")
    public String getRegionID() {
        return RegionID;
    }

    public void setRegionID(String regionID) {
        RegionID = regionID;
    }

    @JsonProperty("StateID")
    public String getStateID() {
        return StateID;
    }

    public void setStateID(String stateID) {
        StateID = stateID;
    }

    @JsonProperty("CityID")
    public String getCityID() {
        return CityID;
    }

    public void setCityID(String cityID) {
        CityID = cityID;
    }

    @JsonProperty("CategoryL1ID")
    public String getCategoryL1ID() {
        return CategoryL1ID;
    }

    public void setCategoryL1ID(String categoryL1ID) {
        CategoryL1ID = categoryL1ID;
    }

    @JsonProperty("CompanyID")
    public String getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(String companyID) {
        CompanyID = companyID;
    }

    @JsonProperty("CategoryL2ID")
    public String getCategoryL2ID() {
        return CategoryL2ID;
    }

    public void setCategoryL2ID(String categoryL2ID) {
        CategoryL2ID = categoryL2ID;
    }

    @JsonProperty("CategoryL3ID")
    public String getCategoryL3ID() {
        return CategoryL3ID;
    }

    public void setCategoryL3ID(String categoryL3ID) {
        CategoryL3ID = categoryL3ID;
    }

    @JsonProperty("EffectiveDate")
    public String getEffectiveDate() {
        return EffectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        EffectiveDate = effectiveDate;
    }

    @JsonProperty("Expirydate")
    public String getExpirydate() {
        return Expirydate;
    }

    public void setExpirydate(String expirydate) {
        Expirydate = expirydate;
    }

    @JsonProperty("IssueDate")
    public String getCardIssueDate() {
        return CardIssueDate;
    }

    public void setCardIssueDate(String cardIssueDate) {
        CardIssueDate = cardIssueDate;
    }

    @JsonProperty("PCPDoctorID")
    public String getPCPDoctorID() {
        return PCPDoctorID;
    }

    public void setPCPDoctorID(String PCPDoctorID) {
        this.PCPDoctorID = PCPDoctorID;
    }

    @JsonProperty("DoctorNameID")
    public String getDoctorNameID() {
        return DoctorNameID;
    }

    public void setDoctorNameID(String doctorNameID) {
        DoctorNameID = doctorNameID;
    }

    @JsonProperty("RegistrationDate")
    public String getRegistrationDate() {
        return RegistrationDate;
    }

    public void setRegistrationDate(String RegistrationDate) {
        this.RegistrationDate = RegistrationDate;
    }

    @JsonProperty("UnitID")
    public String getUnitID() {
        return UnitID;
    }

    public void setUnitID(String UnitID) {
        this.UnitID = UnitID;
    }

    @JsonProperty("ClinicName")
    public String getClinicName() {
        return ClinicName;
    }

    public void setClinicName(String clinicName) {
        ClinicName = clinicName;
    }

    @JsonProperty("CreatedUnitID")
    public String getCreatedUnitID() {
        return CreatedUnitID;
    }

    public void setCreatedUnitID(String createdUnitID) {
        CreatedUnitID = createdUnitID;
    }

    @JsonProperty("AddedBy")
    public String getAddedBy() {
        return AddedBy;
    }

    public void setAddedBy(String addedBy) {
        AddedBy = addedBy;
    }

    @JsonProperty("ID")
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @JsonProperty("MRNo")
    public String getMRNo() {
        return MRNo;
    }

    public void setMRNo(String MRNo) {
        this.MRNo = MRNo;
    }

}
