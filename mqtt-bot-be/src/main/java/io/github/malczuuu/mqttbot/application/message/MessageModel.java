package io.github.malczuuu.mqttbot.application.message;

public record MessageModel(
    String id, String topic, String body, Long messageTime, Long publishTime, Long version) {}
