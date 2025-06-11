package io.github.malczuuu.mqttbot.broker;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BrokerRepository extends MongoRepository<BrokerEntity, String> {

  Optional<BrokerEntity> findByUid(String id);
}
