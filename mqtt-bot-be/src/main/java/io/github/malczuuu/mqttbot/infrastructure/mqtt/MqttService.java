package io.github.malczuuu.mqttbot.infrastructure.mqtt;

import io.github.malczuuu.mqttbot.domain.Broker;
import java.time.Clock;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

@Service
public class MqttService {

  private final Map<String, MqttConnection> connections = new HashMap<>();

  private final Clock clock;

  public MqttService(Clock clock) {
    this.clock = clock;
  }

  public void publish(Broker broker, String topic, MqttMessage message) {
    try {
      connect(broker).publish(topic, message);
    } catch (MqttException e) {
      throw new RuntimeException(e);
    }
  }

  private MqttConnection connect(Broker broker) {
    return connections.computeIfAbsent(broker.getServerUri(), id -> connectInternal(broker));
  }

  private MqttConnection connectInternal(Broker broker) {
    try {
      MqttConnection connection = new MqttConnection(clock, broker);
      connection.connect();
      return connection;
    } catch (MqttException e) {
      throw new RuntimeException(e);
    }
  }
}
