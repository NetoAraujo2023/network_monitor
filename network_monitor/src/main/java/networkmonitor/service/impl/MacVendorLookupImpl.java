package networkmonitor.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;

public class MacVendorLookupImpl {
    private static final Map<String, String> OUI_DATABASE = new HashMap<>();

    static {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new ClassPathResource("oui.txt").getInputStream()))) {
            String line;
            String macPrefix = null;
            String vendor = null;
            
            while ((line = reader.readLine()) != null) {
                if (line.contains("(hex)") || line.contains("(base 16)")) {

                    macPrefix = line.split("\\s+")[0].replace("-", "").toUpperCase(); 
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

    public static String getVendor(String mac) {

        String prefix = mac.replace(":", "").substring(0, 6).toUpperCase();
        System.out.println("Prefixo:" + prefix);
        return OUI_DATABASE.getOrDefault(prefix, "Unknown");
    }
}
