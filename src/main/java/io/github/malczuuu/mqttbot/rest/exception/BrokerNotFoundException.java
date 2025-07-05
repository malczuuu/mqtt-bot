package io.github.malczuuu.mqttbot.rest.exception;

import io.github.malczuuu.problem4j.core.Problem;

public class BrokerNotFoundException extends NotFoundException {

  public BrokerNotFoundException(String id) {
    super("broker " + id + " not found", Problem.extension("id", id));
  }
}
