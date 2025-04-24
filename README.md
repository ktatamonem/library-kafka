# Generic Spring Kafka Event Consumer Library

This library allows dynamic registration of Kafka topic consumers at runtime, supporting both **Apache Kafka** and **Azure Event Hub** as brokers. It is built with full Spring Boot support, making it clean, injectable, and easily configurable.

---

## ✅ Features

- Dynamically register Kafka listeners at runtime
- Support for multiple Kafka backends (Apache Kafka, Azure Event Hub)
- Fully Spring Boot-compatible
- Accepts `Consumer<String>` handler functions for message processing
- Clean, extensible design using Spring's `KafkaListenerEndpointRegistry`

---

## 🧱 Architecture

The library is built around a core class:

```java
DynamicKafkaConsumerManager
```

You pass it:
- A topic name
- A consumer group ID
- A message handler (Java `Consumer<String>`)
- A `KafkaListenerContainerFactory` to bind to either Kafka or Azure Event Hub

---

## 🛠️ Usage

### 1. Add the Library to Your Project

Include the classes:
- `DynamicKafkaConsumerManager`
- `DynamicKafkaConsumerManager.DynamicMessageHandler`

### 2. Provide Kafka Configuration in Your Application

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: my-group
```

Or for Azure Event Hub:
```yaml
spring:
  kafka:
    bootstrap-servers: <namespace>.servicebus.windows.net:9093
    properties:
      security.protocol: SASL_SSL
      sasl.mechanism: PLAIN
      sasl.jaas.config: >
        org.apache.kafka.common.security.plain.PlainLoginModule required
        username="$ConnectionString"
        password="Endpoint=sb://...";
```

### 3. Register Factories in Your App

```java
@Bean("kafkaFactory")
KafkaListenerContainerFactory<?> kafkaFactory(...) { ... }

@Bean("eventHubFactory")
KafkaListenerContainerFactory<?> eventHubFactory(...) { ... }
```

### 4. Register Listeners at Runtime

```java
manager.registerListener(
    "kafka-topic",
    "kafka-group",
    msg -> System.out.println("Kafka message: " + msg),
    kafkaFactory
);

manager.registerListener(
    "eventhub-topic",
    "eventhub-group",
    msg -> System.out.println("EventHub message: " + msg),
    eventHubFactory
);
```

---

## 🧪 Testing

You can register mock factories in tests or use embedded Kafka for testing consumers.

---

## 📦 Requirements

- Java 11+
- Spring Boot 2.7+ or 3.x
- Spring Kafka
- Kafka client dependencies

---


