package networkmonitor.dto;

public class DeviceDTO {
    private String ipAddress;
    private String macAddress;
    private String name;
    private String vendor;

    public DeviceDTO(String ipAddress, String macAddress, String name, String vendor) {
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.name = name;
        this.vendor = vendor;
    }

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

    
}

