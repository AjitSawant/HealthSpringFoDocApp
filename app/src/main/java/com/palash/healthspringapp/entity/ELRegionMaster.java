/**
 * Bean to store information about user and their settings
 */
package com.palash.healthspringapp.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ELRegionMaster {
    private String ID;
    private String CountryID;
    private String RegionID;
    private String RegionName;

    @JsonProperty("ID")
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    @JsonProperty("RegionName")
    public String getRegionName() {
        return RegionName;
    }

    public void setRegionName(String regionName) {
        RegionName = regionName;
    }



}
