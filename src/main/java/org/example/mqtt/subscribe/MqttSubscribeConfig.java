package org.example.mqtt.subscribe;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;


@Configuration
public class MqttSubscribeConfig {

    /**
     * Message Channel Bean
     * {@link org.springframework.integration.channel.DirectChannel}
     * {@link org.springframework.integration.channel.QueueChannel}
     * {@link org.springframework.integration.channel.PriorityChannel}
     * {@link org.springframework.integration.channel.PublishSubscribeChannel}
     * {@link org.springframework.integration.channel.RendezvousChannel}
     * {@link org.springframework.integration.channel.ExecutorChannel}
     * {@link org.springframework.integration.channel.FluxMessageChannel}
     * {@link org.springframework.integration.channel.ReactiveStreamsSubscribableChannel}
     */
    @Bean
    public MessageChannel mqttSubChannel(){
        return new DirectChannel();
    }


    /**
     * Mqtt MessageProducer Bean
     * {@link org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter}
     */
    @Bean
    public MessageProducer subMessageProducer(MqttPahoClientFactory mqttClientFactory) {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                "Subscribe-Client-ID-1",
                mqttClientFactory,
                "foobar"
        );
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel( this.mqttSubChannel() );
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttSubChannel")
    public MessageHandler subMessageHandler() {
        return message -> {
            System.out.println("FooBar payload : "+ message.getPayload());
        };
    }

    // DSL IntegrationFlow Example
    @Bean
    public MessageChannel mqttSubChannel2(){
        return new DirectChannel();
    }
    @Bean
    public MessageProducer subMessageProducer2(MqttPahoClientFactory mqttClientFactory) {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                "Subscribe-Client-ID-2",
                mqttClientFactory,
                "foobar", "foo","bar"
        );
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel( this.mqttSubChannel2() );
        return adapter;
    }
    @Bean
    public IntegrationFlow mqttSubFlow(FooMessageHandler fooMessageHandler) {
        return IntegrationFlows.from(this.mqttSubChannel2())
                .handle(message -> {
                    System.out.println("MqttSubFlow message");
                    String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
                        switch (topic) {
                            case "foo" -> fooMessageHandler.handleMessage(message);
                            case "bar" -> {
                                System.out.println("Bar!!!!");
                                this.subMessageHandler().handleMessage(message);
                            }
                            case "foobar"-> System.out.println("Hello Foobar");
                        }
                })
                .get();
    }





}
