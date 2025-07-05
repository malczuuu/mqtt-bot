package io.github.malczuuu.mqttbot.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<MessageEntity, String> {

  List<MessageEntity> findAllByBrokerUid(String brokerUid);

  Optional<MessageEntity> findByBrokerUidAndUid(String brokerUid, String uid);

  Optional<MessageEntity> findFirstByPublishTimeNull();
}
