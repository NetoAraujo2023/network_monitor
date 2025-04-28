package networkmonitor.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import networkmonitor.repository.DeviceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import networkmonitor.model.Device;

@Service
public class DeviceScannerImpl {

	@Autowired
    private DeviceRepository deviceRepository;
	
	@Autowired
    private MacVendorLookupImpl macVendorLookup;

    DeviceScannerImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<Device> scanDevices() {
        List<Device> devices = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec("arp -a");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            String line;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.isEmpty() || line.startsWith("Interface:") || line.startsWith("Endereço") || line.startsWith("---")) {
                    continue;
                }

                String[] parts = line.split("\\s+");

                if (parts.length >= 3) {
                    String ip = parts[0];
                    String mac = parts[1];
                    String tipo = parts[2]; 
                    if (mac.matches("([0-9a-fA-F]{2}[:-]){5}[0-9a-fA-F]{2}")) {
                        String name = "Device " + mac.substring(0, 8); 

                        macVendorLookup.saveOrUpdateDevice(ip, mac, name, mac);

                        Device device = new Device();
                        device.setIpAddress(ip);
                        device.setMacAddress(mac);
                        device.setName(name);
                        device.setVendor(MacVendorLookupImpl.getVendor(mac));
                        device.setStatus("Active");

                        devices.add(device);
                        
                    } else {
                        System.out.println("MAC Address inválido: " + mac);
                    }
                } else {
                    System.out.println("Linha ignorada: " + line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return devices;
    }
    
    
    

}
