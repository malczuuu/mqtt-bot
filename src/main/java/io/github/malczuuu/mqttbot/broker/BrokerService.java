package io.github.malczuuu.mqttbot.broker;

import io.github.malczuuu.mqttbot.model.ContentModel;
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
        broker.getPassword(),
        broker.isSslVerificationEnabled());
  }

  public BrokerModel createBroker(BrokerCreateModel broker) {
    BrokerEntity entity =
        new BrokerEntity(
            UUID.randomUUID().toString().replace("-", ""),
            broker.serverUri(),
            broker.username(),
            broker.password(),
            broker.sslVerificationEnabled());
    entity = brokerRepository.save(entity);
    return toBrokerModel(entity);
  }

  public void updateBroker(String id, BrokerCreateModel broker) {
    throw new RuntimeException("to be implemented");
  }
}
