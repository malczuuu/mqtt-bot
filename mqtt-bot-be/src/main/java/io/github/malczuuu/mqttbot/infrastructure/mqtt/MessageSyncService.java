package io.github.malczuuu.mqttbot.infrastructure.mqtt;

import io.github.malczuuu.mqttbot.application.message.MessageSyncTrigger;
import io.github.malczuuu.mqttbot.domain.BrokerEntity;
import io.github.malczuuu.mqttbot.domain.BrokerRepository;
import io.github.malczuuu.mqttbot.domain.MessageEntity;
import io.github.malczuuu.mqttbot.domain.MessageRepository;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class MessageSyncService implements MessageSyncTrigger, InitializingBean, DisposableBean {

  private static final Logger log = LoggerFactory.getLogger(MessageSyncService.class);

  private final MessageRepository messageRepository;
  private final BrokerRepository brokerRepository;

  private final MqttService mqttService;
  private final Clock clock;

  private ExecutorService executor;

  public MessageSyncService(
      MessageRepository messageRepository,
      BrokerRepository brokerRepository,
      MqttService mqttService,
      Clock clock) {
    this.messageRepository = messageRepository;
    this.brokerRepository = brokerRepository;
    this.mqttService = mqttService;
    this.clock = clock;
  }

  @Override
  public void afterPropertiesSet() {
    executor = Executors.newSingleThreadExecutor();
  }

  @Override
  public void destroy() throws Exception {
    executor.shutdown();
    if (executor.awaitTermination(5L, TimeUnit.SECONDS)) {
      log.info("Gracefully shutdown executor");
    } else {
      log.error("Failed to gracefully shutdown executor");
    }
  }

  @Override
  public void trigger() {
    executor.submit(
        () -> {
          try {
            Optional<MessageEntity> optionalMessage =
                messageRepository.findFirstByPublishTimeNull();

            while (optionalMessage.isPresent()) {
              MessageEntity message = optionalMessage.get();
              Optional<BrokerEntity> optionalBroker =
                  brokerRepository.findByUid(message.getBrokerUid());

              if (optionalBroker.isPresent()) {
                BrokerEntity broker = optionalBroker.get();
                try {

                  mqttService.publish(
                      broker,
                      message.getTopic(),
                      new MqttMessage(message.getBody().getBytes(StandardCharsets.UTF_8)));
                } catch (Exception e) {
                  message.setError(e.getMessage());
                }
                message.setPublishTime(clock.instant().toEpochMilli());
                messageRepository.save(message);
              } else {
                message.setError("broker missing");
                message.setPublishTime(-1L);
                messageRepository.save(message);
              }
              optionalMessage = messageRepository.findFirstByPublishTimeNull();
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
  }
}
