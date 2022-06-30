package org.example.mqtt.subscribe;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

@Component
public class FooMessageHandler implements MessageHandler {
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        System.out.println("FOO : "+message.getPayload());
    }
}
