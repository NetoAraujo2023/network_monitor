package networkmonitor.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketAlerts {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketAlerts(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendAlert(String message) {
        messagingTemplate.convertAndSend("/topic/alerts", message);
    }
}