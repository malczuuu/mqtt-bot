package io.github.malczuuu.mqttbot.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.mqttbot.application.broker.BrokerModel;
import io.github.malczuuu.mqttbot.domain.BrokerEntity;
import io.github.malczuuu.mqttbot.domain.BrokerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
public class BrokerIntegrationTests extends AbstractIntegrationTests {

  @Autowired private MockMvc mvc;

  @Autowired private BrokerRepository brokerRepository;

  @Autowired private ObjectMapper objectMapper;

  @BeforeEach
  void beforeEach() {
    brokerRepository.deleteAll();
    brokerRepository.save(new BrokerEntity("BRK1", "tcp://localhost:1883", "uname", "upass", true));
  }

  @Test
  void shouldReturnBrokerByIdOnRestApiCall() throws Exception {
    MvcResult result = mvc.perform(get("/api/brokers/{brokerId}", "BRK1")).andReturn();

    String responseBodyAsString = result.getResponse().getContentAsString();

    BrokerModel broker = objectMapper.readValue(responseBodyAsString, BrokerModel.class);

    assertEquals("BRK1", broker.id());
  }
}
