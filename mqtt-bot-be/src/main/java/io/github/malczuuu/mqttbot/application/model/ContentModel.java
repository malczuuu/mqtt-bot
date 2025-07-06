package io.github.malczuuu.mqttbot.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ContentModel<T>(@JsonProperty("content") List<T> content) {}
