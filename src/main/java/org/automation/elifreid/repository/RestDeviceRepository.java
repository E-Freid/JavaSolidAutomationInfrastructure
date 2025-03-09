package org.automation.elifreid.repository;

import org.automation.elifreid.dto.Device;
import org.automation.elifreid.dto.DeviceSummary;
import org.automation.elifreid.serializers.ISerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class RestDeviceRepository implements IDeviceRepository {
    private static final Logger logger = LoggerFactory.getLogger(RestDeviceRepository.class);

    private final String baseUrl;
    private final HttpClient httpClient;

    private final ISerializer<List<DeviceSummary>, String> deviceSummaryListSerializer;
    private final ISerializer<Device, String> deviceSerializer;

    private static final String API_ENDPOINT_SUFFIX = "api/devices";
    private static final int STATUS_CODE_OK = 200;

    public RestDeviceRepository(String baseUrl,
                                ISerializer<List<DeviceSummary>, String> deviceSummaryListSerializer,
                                ISerializer<Device, String> deviceSerializer) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        this.httpClient = HttpClient.newHttpClient();
        this.deviceSummaryListSerializer = deviceSummaryListSerializer;
        this.deviceSerializer = deviceSerializer;
    }

    @Override
    public List<DeviceSummary> getAllDevices() {
        String url = baseUrl + API_ENDPOINT_SUFFIX;
        logger.info("Going to try fetch all devices from endpoint : >> {}", url);

        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            logBasicResponseInformation(response);
            int statusCode = response.statusCode();
            if (statusCode != STATUS_CODE_OK) {
                logger.error("GET request to : >> {} failed with status code : >> {}, response body : >> {}", url, statusCode, response.body());
                throw new RuntimeException(String.format("GET to resource : >> %s failed with status : >> %d",
                        API_ENDPOINT_SUFFIX, statusCode));
            }
            logger.info("Going to deserialize the response body");
            List<DeviceSummary> deviceSummariesList = deviceSummaryListSerializer.deserialize(response.body());
            logger.debug("deserialized JSON data : >> {}", deviceSummariesList);
            return deviceSummariesList;
        } catch (Exception e) {
            logger.error("Error fetching all devices", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Device getDeviceById(int deviceId) {
        String resourceSuffix = API_ENDPOINT_SUFFIX + "/" + deviceId;
        String url = baseUrl + resourceSuffix;
        logger.info("Going to try fetch data for device with id : >> {} from endpoint : >> {}", deviceId, url);

        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            logBasicResponseInformation(response);
            int statusCode = response.statusCode();
            if (statusCode != STATUS_CODE_OK) {
                logger.error("GET request to : >> {} failed with status code : >> {}, response body : >> {}", url, statusCode, response.body());
                throw new RuntimeException(String.format("GET to resource : >> %s failed with status code : >> %d",
                        resourceSuffix, statusCode));
            }
            return deserializeDeviceFromResponse(response);
        } catch (Exception e) {
            logger.error("Error fetching data for device with id : >> {}", deviceId, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Device updateDevice(Device device) {
        String url = baseUrl + API_ENDPOINT_SUFFIX;
        String deviceJson = deviceSerializer.serialize(device);
        logger.info("Going to try update device with id : >> {}", device.getDeviceId());

        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.ofString(deviceJson))
                .header("Content-Type", "application/json")
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            logBasicResponseInformation(response);
            int statusCode = response.statusCode();
            if (statusCode != STATUS_CODE_OK)
            {
                throw new RuntimeException(String.format("PUT to resource : >> %s failed with status code : >> %d",
                        url, statusCode));
            }

            String responseBody = response.body();
            if (responseBody == null || responseBody.isEmpty()) {
                logger.info("PUT returned empty body -> performing GET to fetch updated device");
                return getDeviceById(device.getDeviceId());
            }
            return deserializeDeviceFromResponse(response);
        } catch (Exception e) {
            logger.error("Error updating device with id : >> {}", device.getDeviceId(), e);
            throw new RuntimeException(e);
        }
    }

    private Device deserializeDeviceFromResponse(HttpResponse<String> response) {
        logger.info("Going to deserialize the response body");
        Device device = deviceSerializer.deserialize(response.body());
        logger.debug("deserialized JSON data : >> {}", device);
        return device;
    }

    private void logBasicResponseInformation(HttpResponse<String> response) {
        logger.info("Received HTTP response with status code : >> {}", response.statusCode());
        logger.debug("Response body was : >> {}", response.body());
    }
}
