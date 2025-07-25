package io.github.malczuuu.mqttbot.application.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MessageCreateModel(
    @NotNull @Size(min = 1, max = 1024) @JsonProperty("topic") String topic,
    @NotNull @JsonProperty("body") String body) {}
