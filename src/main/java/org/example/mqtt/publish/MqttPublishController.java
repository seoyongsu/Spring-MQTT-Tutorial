package org.example.mqtt.publish;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MqttPublishController {

    public final MqttPublishConfig.PublishGateway publishGateway;

    public MqttPublishController(MqttPublishConfig.PublishGateway publishGateway) {
        this.publishGateway = publishGateway;
    }
    
    @PostMapping("/pub")
    public void publishExample(){
        publishGateway.publish("foobar", "안녕하세요");
    }
}
