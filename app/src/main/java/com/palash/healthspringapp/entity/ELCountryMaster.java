/**
 * Bean to store information about user and their settings
 */
package com.palash.healthspringapp.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ELCountryMaster {
    private String ID;
    private String CountryName;

    @JsonProperty("ID")
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @JsonProperty("CountryName")
    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }
}
