package io.github.malczuuu.mqttbot.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface TemplateRepository
    extends ListCrudRepository<TemplateEntity, String>,
        ListPagingAndSortingRepository<TemplateEntity, String> {

  List<TemplateEntity> findAllByBrokerUid(String brokerUid);

  Optional<TemplateEntity> findAllByBrokerUidAndUid(String brokerUid, String uid);

  void deleteByBrokerUidAndUid(String brokerUid, String uid);
}
