package networkmonitor.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import networkmonitor.model.Device;
import networkmonitor.repository.DeviceRepository;

@Service
public class MacVendorLookupImpl {
    private static final Map<String, String> OUI_DATABASE = new HashMap<>();

    @Autowired
    private DeviceRepository deviceRepository;

    static {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new ClassPathResource("oui.txt").getInputStream()))) {
            String line;
            String macPrefix = null;
            String vendor = null;
            
            while ((line = reader.readLine()) != null) {
                if (line.contains("(hex)") || line.contains("(base 16)")) {
                    macPrefix = line.split("\\s+")[0].toUpperCase(); 
                }
                else if (macPrefix != null && !line.trim().isEmpty()) {
                    vendor = line.trim(); 
                    OUI_DATABASE.put(macPrefix, vendor); 
                    macPrefix = null; 
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //obter o fornecedor a partir do MAC
    public static String getVendor(String mac) {
        String prefix = mac.replace(":", "").substring(0, 8).toUpperCase().replace("-", "");
        return OUI_DATABASE.getOrDefault(prefix, "Unknown");
    }

    public void saveOrUpdateDevice(String ipAddress, String macAddress, String name, String mac) {
        Optional<Device> existingDevice = deviceRepository.findByMacAddress(macAddress);
        Device device = existingDevice.orElseGet(Device::new);
        
        device.setIpAddress(ipAddress);
        device.setMacAddress(macAddress);
        device.setName(name);
        device.setVendor(getVendor(mac)); 
        device.setStatus("Active"); 

        deviceRepository.save(device); 
    }

}
