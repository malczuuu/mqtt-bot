package io.github.malczuuu.mqttbot.rest;

import io.github.malczuuu.mqttbot.broker.BrokerCreateModel;
import io.github.malczuuu.mqttbot.broker.BrokerModel;
import io.github.malczuuu.mqttbot.broker.BrokerNotFoundException;
import io.github.malczuuu.mqttbot.broker.BrokerService;
import io.github.malczuuu.mqttbot.model.ContentModel;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(path = "/api/brokers")
public class BrokerController {

  private final BrokerService brokerService;

  public BrokerController(BrokerService brokerService) {
    this.brokerService = brokerService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ContentModel<BrokerModel> getBrokers() {
    return brokerService.getAllBrokers();
  }

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public BrokerModel getBrokerById(@PathVariable("id") String id) {
    return brokerService.getBroker(id).orElseThrow(() -> new BrokerNotFoundException(id));
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BrokerModel> createBroker(
      @RequestBody @Valid BrokerCreateModel requestBody) {
    BrokerModel responseBody = brokerService.createBroker(requestBody);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(responseBody.id())
            .toUri();
    return ResponseEntity.created(location).body(responseBody);
  }

  @PutMapping(
      path = "/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void updateBroker(
      @PathVariable("id") String id, @RequestBody @Valid BrokerCreateModel requestBody) {
    brokerService.updateBroker(id, requestBody);
  }
}
