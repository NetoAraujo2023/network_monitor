package networkmonitor.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import networkmonitor.service.impl.DeviceServiceImpl;

@Component
public class ScheduledTasks {
    private final DeviceServiceImpl deviceService;

    public ScheduledTasks(DeviceServiceImpl deviceService) {
        this.deviceService = deviceService;
    }

    @Scheduled(fixedRate = 300000) //5 min
    public void autoDiscoverDevices() {
        deviceService.discoverAndSaveDevices();
    }

    @Scheduled(fixedRate = 60000) //1 min
    public void checkDeviceStatus() {
        deviceService.getAllDevices().forEach(device -> {
            deviceService.pingDevice(device.getIpAddress());
        });
    }
}