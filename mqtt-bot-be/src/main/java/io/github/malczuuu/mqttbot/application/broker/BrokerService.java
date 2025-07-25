package io.github.malczuuu.mqttbot.application.broker;

import io.github.malczuuu.mqttbot.application.model.ContentModel;
import io.github.malczuuu.mqttbot.domain.BrokerEntity;
import io.github.malczuuu.mqttbot.domain.BrokerRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BrokerService {

  private final BrokerRepository brokerRepository;

  public BrokerService(BrokerRepository brokerRepository) {
    this.brokerRepository = brokerRepository;
  }

  public ContentModel<BrokerModel> getAllBrokers() {
    return new ContentModel<>(
        brokerRepository.findAll().stream().map(this::toBrokerModel).toList());
  }

  public Optional<BrokerModel> getBroker(String id) {
    return brokerRepository.findByUid(id).map(this::toBrokerModel);
  }

  private BrokerModel toBrokerModel(BrokerEntity broker) {
    return new BrokerModel(
        broker.getUid(),
        broker.getServerUri(),
        broker.getUsername(),
        broker.isSslVerificationEnabled(),
        broker.getVersion());
  }

  public BrokerModel createBroker(BrokerCreateModel broker) {
    BrokerEntity entity =
        new BrokerEntity(
            UUID.randomUUID().toString(),
            broker.serverUri(),
            broker.username(),
            broker.password(),
            broker.sslVerificationEnabled());
    entity = brokerRepository.save(entity);
    return toBrokerModel(entity);
  }

  public Optional<BrokerModel> updateBroker(String id, BrokerUpdateModel broker) {
    Optional<BrokerEntity> optionalEntity = brokerRepository.findByUid(id);
    if (optionalEntity.isEmpty()) {
      return Optional.empty();
    }

    BrokerEntity entity = optionalEntity.get();
    entity.setServerUri(broker.serverUri());
    entity.setUsername(broker.username());
    entity.setPassword(broker.password());
    entity.setSslVerificationEnabled(broker.sslVerificationEnabled());
    entity.setVersion(broker.version());

    entity = brokerRepository.save(entity);

    return Optional.of(toBrokerModel(entity));
  }

  public void deleteBroker(String id) {
    brokerRepository.deleteByUid(id);
  }
}
