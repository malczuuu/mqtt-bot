package io.github.malczuuu.mqttbot.application.message;

import io.github.malczuuu.mqttbot.application.model.ContentModel;
import io.github.malczuuu.mqttbot.domain.MessageEntity;
import io.github.malczuuu.mqttbot.domain.MessageRepository;
import java.time.Clock;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

  private final MessageRepository messageRepository;
  private final Clock clock;

  public MessageService(MessageRepository messageRepository, Clock clock) {
    this.messageRepository = messageRepository;
    this.clock = clock;
  }

  public ContentModel<MessageModel> findAllMessages(String brokerId) {
    return new ContentModel<>(
        messageRepository.findAllByBrokerUid(brokerId).stream().map(this::toMessageModel).toList());
  }

  public Optional<MessageModel> findMessage(String brokerId, String id) {
    return messageRepository.findByBrokerUidAndUid(brokerId, id).map(this::toMessageModel);
  }

  public MessageModel createMessage(String brokerId, MessageCreateModel message) {
    MessageEntity entity =
        new MessageEntity(
            UUID.randomUUID().toString(),
            brokerId,
            message.topic(),
            message.body(),
            clock.instant().toEpochMilli());
    entity = messageRepository.save(entity);
    return toMessageModel(entity);
  }

  private MessageModel toMessageModel(MessageEntity entity) {
    return new MessageModel(
        entity.getUid(),
        entity.getTopic(),
        entity.getBody(),
        entity.getMessageTime(),
        entity.getPublishTime(),
        entity.getVersion());
  }
}
