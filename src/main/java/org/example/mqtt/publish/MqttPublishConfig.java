package org.example.mqtt.publish;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;

@Configuration
@IntegrationComponentScan
public class MqttPublishConfig {

    @Bean
    public MessageChannel mqttPubChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttPubChannel")
    public MessageHandler mqttOutbound(MqttPahoClientFactory mqttClientFactory) {
        MqttPahoMessageHandler messageHandler =
                new MqttPahoMessageHandler("Pub-Client-ID", mqttClientFactory);
        // 동기 전송 여부
        messageHandler.setAsync(true);
        messageHandler.setDefaultQos(0);
        // 브로커에 마지막 메세지 보관 여부
        messageHandler.setDefaultRetained(true);
        messageHandler.setDefaultTopic("#");
        messageHandler.setConverter(new DefaultPahoMessageConverter());
        return messageHandler;
    }

    @MessagingGateway(defaultRequestChannel = "mqttPubChannel")
    public interface PublishGateway {
        void publish(@Header(MqttHeaders.TOPIC) String topic, String data);
    }


    @Bean
    public IntegrationFlow mqttOutboundFlow(MqttPahoClientFactory mqttClientFactory) {
        return f -> {
            System.out.println("mqttOutboundFlow");
            f.handle(new MqttPahoMessageHandler( "Pub-Client-ID-Flow", mqttClientFactory));
        };
    }

}
