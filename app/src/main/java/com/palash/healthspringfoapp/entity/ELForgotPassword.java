/**
 * Bean to store information about user and their settings
 */
package com.palash.healthspringfoapp.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ELForgotPassword implements Serializable {
    private String UserID;
    private String LoginName;
    private String DoctorID;
    private String EmployeeID;
    private String EmployeeNo;
    private String IsFontOfficeUser;
    private String OTP;
    private String EmailId;
    private String MobileNo;
    private String Password;
    private String AddedDateTime;
    private String clickflag;

    @JsonProperty("UserID")
    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    @JsonProperty("LoginName")
    public String getLoginName() {
        return LoginName;
    }

    public void setLoginName(String loginName) {
        LoginName = loginName;
    }

    @JsonProperty("DoctorID")
    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String doctorID) {
        DoctorID = doctorID;
    }

    @JsonProperty("EmployeeID")
    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    @JsonProperty("EmployeeNo")
    public String getEmployeeNo() {
        return EmployeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        EmployeeNo = employeeNo;
    }

    @JsonProperty("IsFontOfficeUser")
    public String getIsFontOfficeUser() {
        return IsFontOfficeUser;
    }

    public void setIsFontOfficeUser(String isFontOfficeUser) {
        IsFontOfficeUser = isFontOfficeUser;
    }

    @JsonProperty("OTP")
    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    @JsonProperty("EmailId")
    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    @JsonProperty("MobileNo")
    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    @JsonProperty("Password")
    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    @JsonProperty("AddedDateTime")
    public String getAddedDateTime() {
        return AddedDateTime;
    }

    public void setAddedDateTime(String addedDateTime) {
        AddedDateTime = addedDateTime;
    }

    @JsonProperty("clickflag")
    public String getClickflag() {
        return clickflag;
    }

    public void setClickflag(String clickflag) {
        this.clickflag = clickflag;
    }
}
