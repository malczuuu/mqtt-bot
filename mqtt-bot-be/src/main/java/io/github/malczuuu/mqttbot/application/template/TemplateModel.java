package io.github.malczuuu.mqttbot.application.template;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TemplateModel(
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("template") String template,
    @JsonProperty("version") Long version) {}
