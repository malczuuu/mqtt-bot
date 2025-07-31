package io.github.malczuuu.mqttbot.domain;

import java.util.Optional;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface BrokerRepository
    extends ListCrudRepository<BrokerEntity, String>,
        ListPagingAndSortingRepository<BrokerEntity, String> {

  Optional<BrokerEntity> findByUid(String uid);

  void deleteByUid(String uid);
}
