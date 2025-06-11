package io.github.malczuuu.mqttbot.broker;

public record BrokerModel(
    String id,
    String serverUri,
    String username,
    String password,
    boolean sslVerificationEnabled) {}
