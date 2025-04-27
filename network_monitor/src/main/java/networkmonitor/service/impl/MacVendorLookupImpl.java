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
			
			while ((line = reader.readLine()) != null){
				if (line.startsWith(" - ")) {
					String[] parts = line.split("\\s+");
					String macPrefix = parts[1].replace(":", "").substring(0, 6);
					String vendor = line.substring(line.indexOf(parts[2]));
					OUI_DATABASE.put(macPrefix, vendor);
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getVendor(String mac) {
		String prefix = mac.replace(":", "").substring(0, 6).toUpperCase();
		return OUI_DATABASE.getOrDefault(prefix, "Unkown");
	}
}
