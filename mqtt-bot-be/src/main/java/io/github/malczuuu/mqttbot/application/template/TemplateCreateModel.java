package io.github.malczuuu.mqttbot.application.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TemplateCreateModel(
   @JsonProperty("name") @NotNull@Size(max = 256) String name,
   @JsonProperty("template") @NotNull @Size(max = 2048) String template
) {}
