package io.github.malczuuu.iot.bot.mqtt.configuration;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfiguration {

  private static final Logger log = LoggerFactory.getLogger(MqttConfiguration.class);

  private final String mqttUri;
  private final String mqttUsername;
  private final String mqttPassword;
  private final boolean mqttSslValidation;

  public MqttConfiguration(
      @Value("${mqtt-client.uri}") String mqttUri,
      @Value("${mqtt-client.username}") String mqttUsername,
      @Value("${mqtt-client.password}") String mqttPassword,
      @Value("${mqtt-client.ssl-validation}") boolean mqttSslValidation) {
    this.mqttUri = mqttUri;
    this.mqttUsername = mqttUsername;
    this.mqttPassword = mqttPassword;
    this.mqttSslValidation = mqttSslValidation;
  }

  @Bean
  @ConditionalOnProperty(prefix = "mqtt-bot", name = "mqtt-enabled")
  public MqttClient mqttClient() throws MqttException {
    MqttClient client = new MqttClient(mqttUri, UUID.randomUUID().toString());
    MqttConnectOptions options = new MqttConnectOptions();
    options.setAutomaticReconnect(true);
    options.setCleanSession(true);
    options.setConnectionTimeout(10);
    options.setUserName(mqttUsername);
    options.setPassword(mqttPassword.toCharArray());

    if (!mqttSslValidation) {
      new MqttInsecureSSLAdapter().apply(options);
      log.warn("Using mode with disabled certificate validation for MQTT ssl:// connections");
    }

    client.connect(options);

    return client;
  }
}
