package io.github.malczuuu.mqttbot.rest;

import io.github.malczuuu.mqttbot.application.message.MessageCreateModel;
import io.github.malczuuu.mqttbot.application.message.MessageModel;
import io.github.malczuuu.mqttbot.application.message.MessageService;
import io.github.malczuuu.mqttbot.application.message.MessageSyncTrigger;
import io.github.malczuuu.mqttbot.application.model.ContentModel;
import io.github.malczuuu.mqttbot.rest.exception.MessageNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(path = "/api/brokers/{brokerId}/messages")
@Tag(name = "messages")
public class MessageController {

  private final MessageService messageService;
  private final MessageSyncTrigger messageSyncTrigger;

  public MessageController(MessageService messageService, MessageSyncTrigger messageSyncTrigger) {
    this.messageService = messageService;
    this.messageSyncTrigger = messageSyncTrigger;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ContentModel<MessageModel> getMessages(@PathVariable("brokerId") String brokerId) {
    return messageService.findAllMessages(brokerId);
  }

  @GetMapping(path = "/{messageId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public MessageModel getMessageById(
      @PathVariable("brokerId") String brokerId, @PathVariable("messageId") String messageId) {
    return messageService
        .findMessage(brokerId, messageId)
        .orElseThrow(() -> new MessageNotFoundException(messageId));
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MessageModel> createMessage(
      @PathVariable("brokerId") String brokerId,
      @RequestBody @Valid MessageCreateModel requestBody) {
    MessageModel responseBody = messageService.createMessage(brokerId, requestBody);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{messageId}")
            .buildAndExpand(responseBody.id())
            .toUri();
    messageSyncTrigger.trigger();
    return ResponseEntity.created(location).body(responseBody);
  }
}
