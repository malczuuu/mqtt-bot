package io.github.malczuuu.mqttbot.rest;

import io.github.malczuuu.mqttbot.Application;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(classes = Application.class)
abstract class AbstractIntegrationTests {

  protected static MongoDBContainer mongo =
      new MongoDBContainer("mongo:8.0.6").withExposedPorts(27017).withReuse(true);

  @BeforeAll
  static void beforeAllBase() {
    mongo.start();
  }

  @DynamicPropertySource
  static void containersProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
  }
}
