/**
 * Bean to store information about user and their settings
 */
package com.palash.healthspringfoapp.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeSlot {
    private String FromSlot;
    private String ToSlot;
    private String Commonfield;
    private String DateTime;
    private String DoctordId;
    private String UnitId;

    @JsonProperty("FromSlot")
    public String getFromSlot() {
        return FromSlot;
    }

    public void setFromSlot(String FromSlot) {
        this.FromSlot = FromSlot;
    }

    @JsonProperty("ToSlot")
    public String getToSlot() {
        return ToSlot;
    }

    public void setToSlot(String ToSlot) {
        this.ToSlot = ToSlot;
    }

    @JsonProperty("Commonfield")
    public String getCommonfield() {
        return Commonfield;
    }

    public void setCommonfield(String Commonfield) {
        this.Commonfield = Commonfield;
    }

    @JsonProperty("DateTime")
    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        this.DateTime = dateTime;
    }

    @JsonProperty("DoctorId")
    public String getDoctorId() {
        return DoctordId;
    }

    public void setDoctorId(String dateTime) {
        this.DoctordId = dateTime;
    }

    @JsonProperty("UnitId")
    public String getUnitId() {
        return UnitId;
    }

    public void setUnitId(String dateTime) {
        this.UnitId = dateTime;
    }
}
