package io.github.malczuuu.mqttbot.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.mqttbot.application.broker.BrokerCreateModel;
import io.github.malczuuu.mqttbot.application.broker.BrokerModel;
import io.github.malczuuu.mqttbot.application.model.ContentModel;
import io.github.malczuuu.mqttbot.domain.BrokerEntity;
import io.github.malczuuu.mqttbot.domain.BrokerRepository;
import io.github.malczuuu.mqttbot.infrastructure.TestcontainersConfiguration;
import io.github.malczuuu.problem4j.core.Problem;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureMockMvc
public class BrokerIntegrationTests {

  @Autowired private MockMvc mvc;

  @Autowired private BrokerRepository brokerRepository;

  @Autowired private ObjectMapper objectMapper;

  private final List<BrokerEntity> entities = new ArrayList<>();

  @BeforeEach
  void beforeEach() {
    brokerRepository.deleteAll();
    for (int i = 0; i < 20; ++i) {
      entities.add(
          new BrokerEntity(
              String.format("BRK%02d", i),
              String.format("tcp://serverUri%02d:1883", i),
              String.format("uname%02d", i),
              String.format("upass%02d", i),
              i % 2 == 0));
    }
    brokerRepository.saveAll(entities);
  }

  @Test
  void shouldReturnAllBrokers() throws Exception {
    // when
    MvcResult result = mvc.perform(get("/api/brokers")).andReturn();

    // then
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

    ContentModel<BrokerModel> responseBody =
        objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

    assertEquals(entities.size(), responseBody.content().size());
    for (int i = 0; i < entities.size(); ++i) {
      BrokerModel broker = responseBody.content().get(i);
      BrokerEntity entity = entities.get(i);
      assertEquals(entity.getUid(), broker.id());
      assertEquals(entity.getServerUri(), broker.serverUri());
      assertEquals(entity.getUsername(), broker.username());
      assertEquals(entity.isSslVerificationEnabled(), broker.sslVerificationEnabled());
      assertEquals(entity.getVersion(), broker.version());
    }
  }

  @Test
  void shouldReturnSingleBroker() throws Exception {
    // given
    BrokerEntity entity = entities.get(5);

    // when
    MvcResult result = mvc.perform(get("/api/brokers/{brokerId}", entity.getUid())).andReturn();

    // then
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

    BrokerModel broker =
        objectMapper.readValue(result.getResponse().getContentAsString(), BrokerModel.class);

    assertEquals(entity.getUid(), broker.id());
    assertEquals(entity.getServerUri(), broker.serverUri());
    assertEquals(entity.getUsername(), broker.username());
    assertEquals(entity.isSslVerificationEnabled(), broker.sslVerificationEnabled());
    assertEquals(entity.getVersion(), broker.version());
  }

  @Test
  void shouldReturnNotFoundForNonExistingBroker() throws Exception {
    // when
    MvcResult result =
        mvc.perform(get("/api/brokers/{brokerId}", "NON_EXISTING_BROKER")).andReturn();

    // then
    assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());

    Problem problem =
        objectMapper.readValue(result.getResponse().getContentAsString(), Problem.class);

    assertEquals(Problem.BLANK_TYPE, problem.getType());
    assertEquals(HttpStatus.NOT_FOUND.value(), problem.getStatus());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), problem.getTitle());
    assertEquals("broker NON_EXISTING_BROKER not found", problem.getDetail());
  }

  @Test
  void whenCreatingNewBroker_shouldReturnCreatedBroker() throws Exception {
    // given
    BrokerCreateModel newBroker =
        new BrokerCreateModel("tcp://newserver:1883", "newuser", "newpass", true);

    // when
    MvcResult result =
        mvc.perform(
                post("/api/brokers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newBroker)))
            .andReturn();

    // then
    assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());

    BrokerModel createdBroker =
        objectMapper.readValue(result.getResponse().getContentAsString(), BrokerModel.class);

    assertNotNull(createdBroker.id());
    assertEquals(newBroker.serverUri(), createdBroker.serverUri());
    assertEquals(newBroker.username(), createdBroker.username());
    assertEquals(newBroker.sslVerificationEnabled(), createdBroker.sslVerificationEnabled());
    assertEquals(0L, createdBroker.version());
  }

  @Test
  void whenCreatingNewBroker_shouldReturnLocationHeader() throws Exception {
    // given
    BrokerCreateModel newBroker =
        new BrokerCreateModel("tcp://newserver:1883", "newuser", "newpass", true);

    // when
    MvcResult result =
        mvc.perform(
                post("/api/brokers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newBroker)))
            .andReturn();

    // then
    assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());

    BrokerModel createdBroker =
        objectMapper.readValue(result.getResponse().getContentAsString(), BrokerModel.class);

    assertNotNull(createdBroker.id());

    assertTrue(result.getResponse().containsHeader(HttpHeaders.LOCATION));
    String location = result.getResponse().getHeader(HttpHeaders.LOCATION);
    assertEquals("http://localhost/api/brokers/" + createdBroker.id(), location);
  }

  @Test
  void whenCreatingNewBroker_shouldSaveEntityToDatabase() throws Exception {
    // given
    BrokerCreateModel newBroker =
        new BrokerCreateModel("tcp://newserver:1883", "newuser", "newpass", true);

    // when
    MvcResult result =
        mvc.perform(
                post("/api/brokers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newBroker)))
            .andReturn();

    // then
    assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());

    BrokerModel createdBroker =
        objectMapper.readValue(result.getResponse().getContentAsString(), BrokerModel.class);

    BrokerEntity entity = brokerRepository.findByUid(createdBroker.id()).orElseThrow();

    assertEquals(createdBroker.id(), entity.getUid());
    assertEquals(newBroker.serverUri(), entity.getServerUri());
    assertEquals(newBroker.username(), entity.getUsername());
    assertEquals(newBroker.sslVerificationEnabled(), entity.isSslVerificationEnabled());
    assertEquals(newBroker.password(), entity.getPassword());
    assertEquals(0L, entity.getVersion());
  }
}
