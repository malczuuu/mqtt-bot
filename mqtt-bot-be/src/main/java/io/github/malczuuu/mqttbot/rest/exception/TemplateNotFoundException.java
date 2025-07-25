package io.github.malczuuu.mqttbot.rest.exception;

import io.github.malczuuu.problem4j.core.Problem;

public class TemplateNotFoundException extends NotFoundException {

  public TemplateNotFoundException(String id) {
    super("template " + id + " not found", Problem.extension("id", id));
  }
}
