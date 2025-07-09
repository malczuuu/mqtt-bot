package io.github.malczuuu.mqttbot.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.mqttbot.application.message.MessageModel;
import io.github.malczuuu.mqttbot.domain.MessageEntity;
import io.github.malczuuu.mqttbot.domain.MessageRepository;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
public class MessageIntegrationTests extends AbstractIntegrationTests {

  @Autowired private MockMvc mvc;

  @Autowired private MessageRepository messageRepository;

  @Autowired private ObjectMapper objectMapper;

  @BeforeEach
  void beforeEach() {
    messageRepository.deleteAll();
    messageRepository.save(
        new MessageEntity(
            "MSG1",
            "BRK1",
            "topic",
            "hello, MQTT!",
            Instant.parse("2025-01-01T00:00:00Z").toEpochMilli()));
  }

  @Test
  void shouldReturnMessageByIdOnRestApiCall() throws Exception {
    MvcResult result =
        mvc.perform(get("/api/brokers/{brokerId}/messages/{messageId}", "BRK1", "MSG1"))
            .andReturn();

    String responseBodyAsString = result.getResponse().getContentAsString();

    MessageModel message = objectMapper.readValue(responseBodyAsString, MessageModel.class);

    assertEquals("MSG1", message.id());
  }
}
