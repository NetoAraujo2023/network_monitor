package networkmonitor.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import networkmonitor.dto.DeviceDTO;
import networkmonitor.model.Device;
import networkmonitor.service.impl.DeviceServiceImpl;

@RestController
@RequiredArgsConstructor
public class DeviceController {
	@Autowired
	private DeviceServiceImpl deviceService;

    @GetMapping("/api/devices")
    public List<Device> getAllDevices() {
        return deviceService.getAllDevices(); 
    }

    @PostMapping("/api/devices")
    public ResponseEntity<Device> addDevice(@RequestBody Device device) {
        return ResponseEntity.ok(deviceService.addDevice(device));
    }

    @PostMapping("/scan")
    public List<DeviceDTO> discoverAndSaveDevices() {
        List<Device> devices = deviceService.discoverAndSaveDevices();
        return devices.stream()
                      .map(device -> new DeviceDTO(device.getIpAddress(), device.getMacAddress(), device.getName(), device.getVendor()))
                      .collect(Collectors.toList());
    }
    
    @PostMapping("/nmap")
    public ResponseEntity<Object> nmapDevices() {
        List<Device> devices = deviceService.nmapScan();
        return ResponseEntity.ok().body(devices);
        
    }

}
