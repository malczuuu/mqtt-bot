package io.github.malczuuu.mqttbot.application.message;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MessageCreateModel(
    @NotNull @Size(min = 1, max = 1024) String topic, @NotNull String body) {}
