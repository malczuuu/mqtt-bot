package io.github.malczuuu.mqttbot.broker;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BrokerCreateModel(
    @NotBlank String serverUri,
    @NotNull String username,
    @NotNull String password,
    @NotNull Boolean sslVerificationEnabled) {}
