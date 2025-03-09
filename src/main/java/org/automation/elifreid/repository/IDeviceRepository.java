package org.automation.elifreid.repository;

import org.automation.elifreid.dto.Device;
import org.automation.elifreid.dto.DeviceSummary;

import java.util.List;

public interface IDeviceRepository {
    List<DeviceSummary> getAllDevices();
    Device getDeviceById(int deviceId);
    Device updateDevice(Device device);
}
