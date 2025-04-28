package networkmonitor.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import networkmonitor.model.Device;

public interface DeviceRepository extends MongoRepository<Device, String>{
	
	Optional<Device> findFirstByIpAddress(String ipAddress);
    Optional<Device> findFirstByMacAddress(String macAddress);
    Optional<Device> findByMacAddress(String macAddress);
	
}
