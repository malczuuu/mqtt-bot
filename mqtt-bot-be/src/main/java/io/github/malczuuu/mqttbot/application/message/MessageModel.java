package io.github.malczuuu.mqttbot.application.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MessageModel(
    @JsonProperty("id") String id,
    @JsonProperty("topic") String topic,
    @JsonProperty("body") String body,
    @JsonProperty("message_time") Long messageTime,
    @JsonProperty("publish_time") Long publishTime,
    @JsonProperty("version") Long version) {}
