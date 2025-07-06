package io.github.malczuuu.mqttbot.rest;

import io.github.malczuuu.mqttbot.application.broker.BrokerCreateModel;
import io.github.malczuuu.mqttbot.application.broker.BrokerModel;
import io.github.malczuuu.mqttbot.application.broker.BrokerService;
import io.github.malczuuu.mqttbot.application.broker.BrokerUpdateModel;
import io.github.malczuuu.mqttbot.application.model.ContentModel;
import io.github.malczuuu.mqttbot.rest.exception.BrokerNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(path = "/api/brokers")
@Tag(name = "brokers")
public class BrokerController {

  private final BrokerService brokerService;

  public BrokerController(BrokerService brokerService) {
    this.brokerService = brokerService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ContentModel<BrokerModel> getBrokers() {
    return brokerService.getAllBrokers();
  }

  @GetMapping(path = "/{brokerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public BrokerModel getBrokerById(@PathVariable("brokerId") String brokerId) {
    return brokerService
        .getBroker(brokerId)
        .orElseThrow(() -> new BrokerNotFoundException(brokerId));
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BrokerModel> createBroker(
      @RequestBody @Valid BrokerCreateModel requestBody) {
    BrokerModel responseBody = brokerService.createBroker(requestBody);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{brokerId}")
            .buildAndExpand(responseBody.id())
            .toUri();
    return ResponseEntity.created(location).body(responseBody);
  }

  @PutMapping(
      path = "/{brokerId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void updateBroker(
      @PathVariable("brokerId") String brokerId,
      @RequestBody @Valid BrokerUpdateModel requestBody) {
    brokerService
        .updateBroker(brokerId, requestBody)
        .orElseThrow(() -> new BrokerNotFoundException(brokerId));
  }

  @DeleteMapping(path = "/{brokerId}")
  public void deleteBroker(@PathVariable("brokerId") String brokerId) {
    brokerService.deleteBroker(brokerId);
  }
}
