/**
 * Bean to store information about user and their settings
 */
package com.palash.healthspringfoapp.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MedicienFrequency {
    private String ID;
    private String Description;
    private String QuntityPerDay;

    @JsonProperty("ID")
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    @JsonProperty("QuntityPerDay")
    public String getQuntityPerDay() {
        return QuntityPerDay;
    }

    public void setQuntityPerDay(String quntityPerDay) {
        QuntityPerDay = quntityPerDay;
    }
}
