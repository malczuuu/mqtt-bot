package io.github.malczuuu.mqttbot.domain;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BrokerRepository extends MongoRepository<BrokerEntity, String> {

  Optional<BrokerEntity> findByUid(String uid);

  void deleteByUid(String uid);
}
