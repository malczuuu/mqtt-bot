package io.github.malczuuu.mqttbot.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TemplateRepository extends MongoRepository<TemplateEntity, String> {

  List<TemplateEntity> findAllByBrokerUid(String brokerUid);

  Optional<TemplateEntity> findAllByBrokerUidAndUid(String brokerUid, String uid);

  void deleteByBrokerUidAndUid(String brokerUid, String uid);
}
