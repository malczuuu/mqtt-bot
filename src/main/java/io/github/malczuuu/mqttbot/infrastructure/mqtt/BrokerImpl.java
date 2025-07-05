package io.github.malczuuu.mqttbot.infrastructure.mqtt;

import io.github.malczuuu.mqttbot.domain.Broker;

record BrokerImpl(
    String serverUri, String username, String password, boolean sslVerificationEnabled)
    implements Broker {

  BrokerImpl(Broker broker) {
    this(
        broker.getServerUri(),
        broker.getUsername(),
        broker.getPassword(),
        broker.isSslVerificationEnabled());
  }

  @Override
  public String getServerUri() {
    return serverUri();
  }

  @Override
  public String getUsername() {
    return username();
  }

  @Override
  public String getPassword() {
    return password();
  }

  @Override
  public boolean isSslVerificationEnabled() {
    return sslVerificationEnabled();
  }
}
