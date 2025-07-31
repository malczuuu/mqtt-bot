package io.github.malczuuu.mqttbot.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface MessageRepository
    extends ListCrudRepository<MessageEntity, String>,
        ListPagingAndSortingRepository<MessageEntity, String> {

  List<MessageEntity> findAllByBrokerUid(String brokerUid);

  Optional<MessageEntity> findByBrokerUidAndUid(String brokerUid, String uid);

  Optional<MessageEntity> findFirstByPublishTimeNull();
}
