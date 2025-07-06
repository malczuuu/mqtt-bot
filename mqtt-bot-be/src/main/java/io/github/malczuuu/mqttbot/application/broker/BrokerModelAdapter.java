package io.github.malczuuu.mqttbot.application.broker;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.malczuuu.mqttbot.domain.Broker;

public interface BrokerModelAdapter extends Broker {

  String serverUri();

  String username();

  String password();

  Boolean sslVerificationEnabled();

  @JsonIgnore
  default String getServerUri() {
    return serverUri();
  }

  @JsonIgnore
  default String getUsername() {
    return username();
  }

  @JsonIgnore
  default String getPassword() {
    return password();
  }

  @JsonIgnore
  default boolean isSslVerificationEnabled() {
    return sslVerificationEnabled() == null || sslVerificationEnabled();
  }
}
