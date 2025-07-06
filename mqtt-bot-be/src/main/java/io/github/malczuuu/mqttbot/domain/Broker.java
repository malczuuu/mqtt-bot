package io.github.malczuuu.mqttbot.domain;

public interface Broker {

  String getServerUri();

  String getUsername();

  String getPassword();

  boolean isSslVerificationEnabled();
}
