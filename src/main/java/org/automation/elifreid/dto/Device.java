package org.automation.elifreid.dto;

import java.time.Instant;

public class Device extends DeviceSummary {
    private boolean turnedOn;
    private String location;
    private String description;
    private String companyCode;
    private String model;
    private Instant updateDate;

    public Device() {}

    public Device(int deviceId,
                  String deviceType,
                  boolean turnedOn,
                  String location,
                  String description,
                  String companyCode,
                  String model,
                  Instant updateDate) {
        super(deviceId, deviceType);
        this.turnedOn = turnedOn;
        this.location = location;
        this.description = description;
        this.companyCode = companyCode;
        this.model = model;
        this.updateDate = updateDate;
    }

    public boolean isTurnedOn() {
        return turnedOn;
    }

    public void setTurnedOn(boolean turnedOn) {
        this.turnedOn = turnedOn;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Instant getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "deviceId=" + getDeviceId() +
                ", deviceType='" + getDeviceType() + '\'' +
                ", updateDate=" + updateDate +
                ", turnedOn=" + turnedOn +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", companyCode='" + companyCode + '\'' +
                ", model='" + model + '\'' +
                '}';
    }
}
