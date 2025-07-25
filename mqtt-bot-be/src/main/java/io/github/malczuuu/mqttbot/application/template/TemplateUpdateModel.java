package io.github.malczuuu.mqttbot.application.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record TemplateUpdateModel(
    @JsonProperty("name") @Size(max = 256) String name,
    @JsonProperty("template") @NotNull @Size(max = 2048) String template,
    @JsonProperty("version") @NotNull @PositiveOrZero Long version) {}
