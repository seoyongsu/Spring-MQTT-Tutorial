package org.example.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

/**
 * MQTT Connection Config Example
 */
@Configuration
public class MqttConnectionConfig {
    private static final String BROKER_URL = "tcp://localhost:1883";
    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    /**
     * Mqtt Connect Option Bean
     */
    @Bean
    public MqttConnectOptions mqttConnectOptions(){
        MqttConnectOptions connectOptions = new MqttConnectOptions();

        // 커넥션 실패시 재연결 설정값
        connectOptions.setAutomaticReconnect(true);
        // 연결할 Mqtt Server URLs
        connectOptions.setServerURIs(new String[]{BROKER_URL});

        // Mqtt Server 인증 접속 정보
        connectOptions.setUserName(USERNAME);
        connectOptions.setPassword(PASSWORD.toCharArray());

        // 케넥션 세션 유지 여부
        connectOptions.setCleanSession(true);

        // 연결 유지 시간 설정
        connectOptions.setKeepAliveInterval(100);
        // 커넥션 타임 아웃 설정
        connectOptions.setConnectionTimeout(10);

        //TODO : 기타 옵션은 MqttConnectOptions 참고 하여 설정

        return connectOptions;
    }

    /**
     * Mqtt Client Factory Bean
     * {@link org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory}
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory(){
        DefaultMqttPahoClientFactory mqttClientFactory = new DefaultMqttPahoClientFactory();
        // Client Factory 커넥션 옵션 설정
        mqttClientFactory.setConnectionOptions(this.mqttConnectOptions());
        return mqttClientFactory;
    }


    /**
     * Sync Mqtt Client Bean
     * {@link org.eclipse.paho.client.mqttv3.MqttClient}
     */
    @Bean
    public IMqttClient mqttClient() throws MqttException {
        MqttPahoClientFactory factory = this.mqttClientFactory();
        return factory.getClientInstance(BROKER_URL, "Client-ID");
    }

    /**
     * Async Mqtt Client Bean
     * {@link org.eclipse.paho.client.mqttv3.MqttAsyncClient}
     */
    @Bean
    public IMqttAsyncClient mqttAsyncClient() throws MqttException {
        MqttPahoClientFactory factory = this.mqttClientFactory();
        return factory.getAsyncClientInstance(BROKER_URL,"Client-ID");
    }
}
