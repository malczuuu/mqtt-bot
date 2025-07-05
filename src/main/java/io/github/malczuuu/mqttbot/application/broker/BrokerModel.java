package io.github.malczuuu.mqttbot.application.broker;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BrokerModel(
    @JsonProperty("id") String id,
    @JsonProperty("server_uri") String serverUri,
    @JsonProperty("username") String username,
    @JsonProperty("password") String password,
    @JsonProperty("ssl_verification_enabled") Boolean sslVerificationEnabled,
    @JsonProperty("version") Long version)
    implements BrokerModelAdapter {

  public BrokerModel {
    if (sslVerificationEnabled == null) {
      sslVerificationEnabled = true;
    }
  }
}
