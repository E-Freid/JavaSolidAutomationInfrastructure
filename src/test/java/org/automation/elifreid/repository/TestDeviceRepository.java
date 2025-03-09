package org.automation.elifreid.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import org.automation.elifreid.dto.Device;
import org.automation.elifreid.dto.DeviceSummary;
import org.automation.elifreid.serializers.JacksonJsonSerializer;
import org.automation.elifreid.utils.TestReporter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestDeviceRepository {

    private static IDeviceRepository deviceRepo = new RestDeviceRepository(
            "http://localhost:8080",
            new JacksonJsonSerializer<>(new TypeReference<List<DeviceSummary>>() {}),
            new JacksonJsonSerializer<>(new TypeReference<Device>() {})
    );

    @BeforeAll
    static void setup() {
        TestReporter.log("{} - BeforeAll setup started!", TestDeviceRepository.class.getSimpleName());
        TestReporter.log("{} - BeforeAll setup finished!", TestDeviceRepository.class.getSimpleName());
    }

    @AfterEach
    public void logSuccessMsg() {
        TestReporter.log("Test passed successfully!");
    }

    @Test
    void testGetAllDevices() {
        TestReporter.log("Going to check GetAllDevices functionality");
        List<DeviceSummary> deviceSummaryList = deviceRepo.getAllDevices();
        TestReporter.log("devices as returned from getAllDevices : >> {}", deviceSummaryList);
        assertNotNull(deviceSummaryList, "Devices list should not be null");
    }

    @ParameterizedTest
    @CsvSource({
            "2"
    })
    void testGetDeviceDataUsingListIndex(int deviceIndex) {
        TestReporter.log("Going to check GetDeviceDataUsingListIndex functionality with list index : >> {}", deviceIndex);
        List<DeviceSummary> deviceSummaryList  = deviceRepo.getAllDevices();
        TestReporter.log("devices as returned from getAllDevices : >> {}", deviceSummaryList);
        assertNotNull(deviceSummaryList, "Devices list should not be null");
        assertTrue(deviceSummaryList.size() > deviceIndex,
                String.format("Devices list size was : >> %d but it should be at least : >> %d", deviceSummaryList.size(), deviceIndex));
        Device device = deviceRepo.getDeviceById(deviceSummaryList.get(deviceIndex).getDeviceId());
        TestReporter.log("Device object as fetched from repo : >> {}", device);
        assertNotNull(device, "failed to fetch device, returned object was null");
    }

    @Test
    void testGetDeviceById_unknown() {
        TestReporter.log("Running GetDeviceById functionality with unknown");
        List<DeviceSummary> devices = deviceRepo.getAllDevices();
        int unknownId;

        if (!devices.isEmpty()) {
            TestReporter.log("Device list is not empty, we will take unknownId to be max+1");
            int maxId = devices.stream()
                    .mapToInt(DeviceSummary::getDeviceId)
                    .max()
                    .orElse(0);
            TestReporter.log("Max deviceId found : >> {}, setting unknownId to : >> {}", maxId, maxId+1);
            unknownId = maxId + 1;
        } else {
            TestReporter.log("devices list is empty, going to use 1 as unknownId");
            unknownId = 1;
        }

        Exception ex = assertThrows(RuntimeException.class, () -> {
            deviceRepo.getDeviceById(unknownId);
        });
        assertNotNull(ex, "No exception was thrown as expected.");

        String errorMsg = ex.getMessage();
        TestReporter.log("Caught exception as expected : >> {}", errorMsg);
        assertTrue(errorMsg.contains("failed"));
    }

    @Test
    void testUpdateDevice_modifiesLocation() {
        TestReporter.log("Going to check updateDevice functionality");

        List<DeviceSummary> deviceSummaryList = deviceRepo.getAllDevices();

        // instead of failing on empty list, we can create new instance and put. didn't have enough time
        assertNotNull(deviceSummaryList, "Device summary list should not be null");
        assertFalse(deviceSummaryList.isEmpty(), "Device summary list should not be empty");

        int deviceId = deviceSummaryList.get(0).getDeviceId();
        TestReporter.log("Going to update device : >>  {}", deviceId);
        Device originalDevice = deviceRepo.getDeviceById(deviceId);
        assertNotNull(originalDevice, "device should not be null");

        String newLocation = originalDevice.getLocation() + "_updated";
        originalDevice.setLocation(newLocation);

        Device updatedDevice = deviceRepo.updateDevice(originalDevice);
        assertNotNull(updatedDevice, "Updated device should not be null");
        assertEquals(newLocation, updatedDevice.getLocation(), "Device location was not updated as expected");

        Device refetchedDevice = deviceRepo.getDeviceById(deviceId);
        assertEquals(newLocation, refetchedDevice.getLocation(), "Device location was not updated as expected");
    }
}
