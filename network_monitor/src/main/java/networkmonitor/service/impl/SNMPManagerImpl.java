package networkmonitor.service.impl;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.stereotype.Service;
import networkmonitor.model.Device;

@Service
public class SNMPManagerImpl {

    private Snmp snmp;
    private ExecutorService executorService;

    public SNMPManagerImpl() {
        executorService = Executors.newFixedThreadPool(10);
    }

    public void start() throws IOException {
        TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
    }

    public Device enrichDevice(Device device) {
        try {
            start();
            
            String deviceName = getAsString(device.getIpAddress(), "1.3.6.1.2.1.1.5.0");
            String vendorInfo = getAsString(device.getIpAddress(), "1.3.6.1.2.1.1.1.0");
            
            device.setName(deviceName);
            device.setVendor(vendorInfo);
            System.out.println("Enriquecendo dispositivo: " + device);

            device.setType(deviceType(vendorInfo));
        } catch (IOException e) {
            System.err.println("Erro ao enriquecer o dispositivo: " + device.getIpAddress());
            e.printStackTrace();
        }
        return device;
    }

    private String deviceType(String vendorInfo) {
        if (vendorInfo.contains("Router")) return "Router";
        if (vendorInfo.contains("Switch")) return "Switch";
        return "Unknown";
    }

    public String getAsString(String ip, String oid) throws IOException {
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(oid)));
        pdu.setType(PDU.GET);

        CommunityTarget<UdpAddress> target = new CommunityTarget<>();
        target.setCommunity(new OctetString("public"));
        target.setAddress((UdpAddress) GenericAddress.parse("udp:" + ip + "/161"));
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version2c);

        ResponseEvent<UdpAddress> response = snmp.send(pdu, target);
        
        if (response == null || response.getResponse() == null) {
            System.out.println("Nenhuma resposta recebida do dispositivo: " + ip);
            return "Erro na consulta SNMP";
        }

        System.out.println("Resposta SNMP: " + response.getResponse());
        return response.getResponse().get(0).getVariable().toString();
    }
    
    public void queryDevices(Device[] devices) {
        for (Device device : devices) {
            executorService.submit(() -> {
                try {
                    enrichDevice(device);
                } catch (Exception e) {
                    System.err.println("Erro ao consultar dispositivo " + device.getIpAddress());
                    e.printStackTrace();
                }
            });
        }
    }
}
