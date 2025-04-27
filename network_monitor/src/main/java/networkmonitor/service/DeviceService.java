package networkmonitor.service;

import java.util.List;

import networkmonitor.model.Device;

public interface DeviceService {
	List<Device> getAllDevices();
	Device addDevice(Device device);
	Device updateDeviceStatus(String ipAddress, String status);
	boolean pingDevice(String ipAddress);
	List<Device> discoverAndSaveDevices();
}
