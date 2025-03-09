package org.automation.elifreid.dto;

import java.util.List;

public class DeviceSummary {
    private int deviceId;
    private String deviceType;

    public DeviceSummary() {}

    public DeviceSummary(int deviceId, String deviceType) {
        this.deviceId = deviceId;
        this.deviceType = deviceType;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public String toString() {
        return "DeviceSummary{" +
                "deviceId=" + deviceId +
                ", deviceType='" + deviceType + '\'' +
                '}';
    }
}
