package com.mk.events.utils.library.generic.consumer.manager;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.function.Consumer;

@Component
public class GenericKafkaConsumerManager {

    private final KafkaListenerEndpointRegistry registry;
    private final MessageHandlerMethodFactory messageHandlerMethodFactory;

    public GenericKafkaConsumerManager(KafkaListenerEndpointRegistry registry, MessageHandlerMethodFactory messageHandlerMethodFactory) {
        this.registry = registry;
        this.messageHandlerMethodFactory = messageHandlerMethodFactory;
    }

    public void registerListener(
            String topic,
            String groupId,
            Consumer<String> messageHandler,
            KafkaListenerContainerFactory<? extends MessageListenerContainer> containerFactory
    ) {
        MethodKafkaListenerEndpoint<String, String> endpoint = new MethodKafkaListenerEndpoint<>();
        endpoint.setId(UUID.randomUUID().toString());
        endpoint.setGroupId(groupId);
        endpoint.setTopics(topic);
        endpoint.setMessageHandlerMethodFactory(messageHandlerMethodFactory);
        endpoint.setBean(new DynamicMessageHandler(messageHandler));

        try {
            Method method = DynamicMessageHandler.class.getMethod("handle", String.class);
            endpoint.setMethod(method);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to get handler method", e);
        }

        registry.registerListenerContainer(endpoint, containerFactory);
    }

    public static class DynamicMessageHandler {
        private final Consumer<String> handler;

        public DynamicMessageHandler(Consumer<String> handler) {
            this.handler = handler;
        }

        public void handle(String message) {
            handler.accept(message);
        }
    }
}