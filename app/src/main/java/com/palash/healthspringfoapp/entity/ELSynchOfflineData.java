/**
 * Bean to store information about user and their settings
 */
package com.palash.healthspringfoapp.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ELSynchOfflineData {
    private String ID;
    private String OfflineLastDate;
    private String VersionCode;
    private String VersionName;
    private String OfflineStatus;

    @JsonProperty("ID")
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @JsonProperty("OfflineLastDate")
    public String getOfflineLastDate() {
        return OfflineLastDate;
    }

    public void setOfflineLastDate(String offlineLastDate) {
        OfflineLastDate = offlineLastDate;
    }

    @JsonProperty("VersionCode")
    public String getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(String versionCode) {
        VersionCode = versionCode;
    }

    @JsonProperty("VersionName")
    public String getVersionName() {
        return VersionName;
    }

    public void setVersionName(String versionName) {
        VersionName = versionName;
    }

    @JsonProperty("OfflineStatus")
    public String getOfflineStatus() {
        return OfflineStatus;
    }

    public void setOfflineStatus(String offlineStatus) {
        OfflineStatus = offlineStatus;
    }
}
