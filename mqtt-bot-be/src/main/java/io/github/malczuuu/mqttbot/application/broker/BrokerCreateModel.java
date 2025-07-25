package io.github.malczuuu.mqttbot.application.broker;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BrokerCreateModel(
    @JsonProperty("server_uri") @NotNull @Size(min = 1, max = 1024) String serverUri,
    @JsonProperty("username") @Size(min = 1, max = 1024) String username,
    @JsonProperty("password") @Size(min = 1, max = 1024) String password,
    @JsonProperty("ssl_verification_enabled") Boolean sslVerificationEnabled) {

  public BrokerCreateModel {
    if (sslVerificationEnabled == null) {
      sslVerificationEnabled = true;
    }
  }
}
