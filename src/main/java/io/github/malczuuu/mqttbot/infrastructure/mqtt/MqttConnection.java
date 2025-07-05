package io.github.malczuuu.mqttbot.infrastructure.mqtt;

import io.github.malczuuu.mqttbot.domain.Broker;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttConnection {

  private final Clock clock;
  private final Broker broker;

  private MqttClient client;

  private Instant lastPublishTime;

  public MqttConnection(Clock clock, Broker broker) {
    this.clock = clock;
    this.broker = new BrokerImpl(broker);
    this.lastPublishTime = clock.instant();
  }

  public void connect() throws MqttException {
    client = connectMqttClient();
  }

  public void destroy() throws MqttException {
    client.close();
  }

  public Broker getBroker() {
    return broker;
  }

  public void publish(String topic, MqttMessage message) throws MqttException {
    client.publish(topic, message);
    lastPublishTime = clock.instant();
  }

  private MqttClient connectMqttClient() throws MqttException {
    MqttClient client = new MqttClient(broker.getServerUri(), UUID.randomUUID().toString());
    MqttConnectOptions options = new MqttConnectOptions();
    options.setAutomaticReconnect(true);
    options.setCleanSession(true);
    options.setConnectionTimeout(10);
    options.setUserName(broker.getUsername());
    options.setPassword(broker.getPassword().toCharArray());

    if (!broker.isSslVerificationEnabled() && broker.getServerUri().startsWith("ssl://")) {
      new MqttInsecureSslAdapter().apply(options);
    }

    client.connect(options);
    return client;
  }

  public Instant getLastPublishTime() {
    return lastPublishTime;
  }
}
