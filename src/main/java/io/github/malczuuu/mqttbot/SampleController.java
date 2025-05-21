package io.github.malczuuu.mqttbot;

import io.github.malczuuu.problem4j.core.Problem;
import io.github.malczuuu.problem4j.core.ProblemException;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(path = "/api/samples")
public class SampleController {

  @GetMapping
  public void getSample() {
    throw new ProblemException(
        Problem.builder()
            .title(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .status(HttpStatus.BAD_REQUEST.value())
            .build());
  }

  @GetMapping(path = "/{id}")
  public void getSampleById(@PathVariable("id") @Size(min = 4, max = 32) String id) {
    throw new ProblemException(
        Problem.builder()
            .title(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .status(HttpStatus.BAD_REQUEST.value())
            .extension("id", id)
            .build());
  }
}
