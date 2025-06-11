package io.github.malczuuu.mqttbot.connection;

import io.github.malczuuu.mqttbot.broker.BrokerModel;
import io.github.malczuuu.mqttbot.infrastructure.mqtt.MqttInsecureSSLAdapter;
import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

@Service
public class ConnectionManager {

  public MqttClient connectMqttClient(BrokerModel broker) throws MqttException {
    MqttClient client = new MqttClient(broker.serverUri(), UUID.randomUUID().toString());
    MqttConnectOptions options = new MqttConnectOptions();
    options.setAutomaticReconnect(true);
    options.setCleanSession(true);
    options.setConnectionTimeout(10);
    options.setUserName(broker.username());
    options.setPassword(broker.password().toCharArray());

    if (!broker.sslVerificationEnabled()) {
      new MqttInsecureSSLAdapter().apply(options);
    }

    client.connect(options);
    return client;
  }
}
