package io.github.malczuuu.mqttbot.broker;

import io.github.malczuuu.mqttbot.model.NotFoundException;
import io.github.malczuuu.problem4j.core.Problem;

public class BrokerNotFoundException extends NotFoundException {

  public BrokerNotFoundException(String id) {
    super("broker " + id + " not found", Problem.extension("id", id));
  }
}
