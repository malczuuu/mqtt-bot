package io.github.malczuuu.mqttbot.rest.exception;

import io.github.malczuuu.problem4j.core.Problem;

public class MessageNotFoundException extends NotFoundException {

  public MessageNotFoundException(String id) {
    super("message " + id + " not found", Problem.extension("id", id));
  }
}
