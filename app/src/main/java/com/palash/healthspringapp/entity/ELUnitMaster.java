/**
 * Bean to store information about user and their settings
 */
package com.palash.healthspringapp.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ELUnitMaster {
    private String UnitID;
    private String UnitCode;
    private String UnitDesc;
    private String IsDefault;

    @JsonProperty("UnitID")
    public String getUnitID() {
        return UnitID;
    }

    public void setUnitID(String unitID) {
        UnitID = unitID;
    }

    @JsonProperty("UnitCode")
    public String getUnitCode() {
        return UnitCode;
    }

    public void setUnitCode(String unitCode) {
        UnitCode = unitCode;
    }

    @JsonProperty("UnitDesc")
    public String getUnitDesc() {
        return UnitDesc;
    }

    public void setUnitDesc(String unitDesc) {
        UnitDesc = unitDesc;
    }

    @JsonProperty("IsDefault")
    public String getIsDefault() {
        return IsDefault;
    }

    public void setIsDefault(String isDefault) {
        IsDefault = isDefault;
    }
}
