package io.github.malczuuu.mqttbot.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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

  // TODO: Add test for creating broker with invalid data

  @Test
  void whenUpdatingBroker_shouldUpdateEntityInDatabase() throws Exception {
    // given
    BrokerEntity entity = entities.get(5);
    BrokerModel requestBody =
        new BrokerModel(
            entity.getUid(), "tcp://updatedserver:1883", "updateduser", true, entity.getVersion());

    // when
    MvcResult result =
        mvc.perform(
                put("/api/brokers/{brokerId}", entity.getUid())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestBody)))
            .andReturn();

    // then
    assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());

    BrokerEntity updatedEntity = brokerRepository.findByUid(entity.getUid()).orElseThrow();
    assertEquals(requestBody.serverUri(), updatedEntity.getServerUri());
    assertEquals(requestBody.username(), updatedEntity.getUsername());
    assertEquals(requestBody.sslVerificationEnabled(), updatedEntity.isSslVerificationEnabled());
    assertEquals(requestBody.version() + 1, updatedEntity.getVersion());
  }

  @Test
  void whenUpdatingNotExistingBroker_shouldReturnNotFound() throws Exception {
    // given
    BrokerModel requestBody =
        new BrokerModel("NON_EXISTING_BROKER", "tcp://updatedserver:1883", "updateduser", true, 1L);

    // when
    MvcResult result =
        mvc.perform(
                put("/api/brokers/{brokerId}", "NON_EXISTING_BROKER")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestBody)))
            .andReturn();

    // then
    assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());

    Problem responseBody =
        objectMapper.readValue(result.getResponse().getContentAsString(), Problem.class);

    assertEquals(Problem.BLANK_TYPE, responseBody.getType());
    assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.getStatus());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), responseBody.getTitle());
    assertEquals("broker NON_EXISTING_BROKER not found", responseBody.getDetail());
  }

  @Test
  void whenUpdatingBrokerWithInvalidVersion_shouldReturnConflict() throws Exception {
    // given
    BrokerEntity entity = entities.get(5);
    BrokerModel requestBody =
        new BrokerModel(
            entity.getUid(),
            "tcp://updatedserver:1883",
            "updateduser",
            true,
            entity.getVersion() + 1);

    // when
    MvcResult result =
        mvc.perform(
                put("/api/brokers/{brokerId}", entity.getUid())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestBody)))
            .andReturn();

    // then
    assertEquals(HttpStatus.CONFLICT.value(), result.getResponse().getStatus());

    Problem responseBody =
        objectMapper.readValue(result.getResponse().getContentAsString(), Problem.class);

    assertEquals(Problem.BLANK_TYPE, responseBody.getType());
    assertEquals(HttpStatus.CONFLICT.value(), responseBody.getStatus());
    assertEquals(HttpStatus.CONFLICT.getReasonPhrase(), responseBody.getTitle());
    assertEquals("version mismatch", responseBody.getDetail());
  }

  // TODO: Add test for updating broker with invalid data

  @Test
  void whenDeletingBroker_shouldRemoveEntityFromDatabase() throws Exception {
    // given
    BrokerEntity entity = entities.get(5);
    assertTrue(brokerRepository.findByUid(entity.getUid()).isPresent());

    // when
    MvcResult result = mvc.perform(delete("/api/brokers/{brokerId}", entity.getUid())).andReturn();

    // then
    assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());

    assertFalse(brokerRepository.findByUid(entity.getUid()).isPresent());
  }

  @Test
  void whenDeletingNotExistingBroker_shouldReturnNoContentIgnoringNonExistence() throws Exception {
    // given
    String brokerId = "NON_EXISTING_BROKER";
    assertFalse(brokerRepository.findByUid(brokerId).isPresent());

    // when
    MvcResult result = mvc.perform(delete("/api/brokers/{brokerId}", brokerId)).andReturn();

    // then
    assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());

    assertFalse(brokerRepository.findByUid(brokerId).isPresent());
  }
}
