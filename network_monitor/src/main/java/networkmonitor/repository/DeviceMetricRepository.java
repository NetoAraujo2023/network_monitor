package networkmonitor.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import networkmonitor.model.DeviceMetric;

public interface DeviceMetricRepository extends MongoRepository<DeviceMetric, String>{

	List<DeviceMetric> findByDeviceId(String deviceId);
	
}
