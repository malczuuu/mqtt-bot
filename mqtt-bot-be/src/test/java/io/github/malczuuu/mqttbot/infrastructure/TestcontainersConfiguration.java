package io.github.malczuuu.mqttbot.infrastructure;

import static io.github.malczuuu.mqttbot.infrastructure.ContainerVersions.MONGO_IMAGE;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

  @Bean
  @ServiceConnection
  public MongoDBContainer mongoDbContainer() {
    return new MongoDBContainer(DockerImageName.parse(MONGO_IMAGE));
  }
}
