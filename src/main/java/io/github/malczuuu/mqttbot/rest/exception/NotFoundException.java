package io.github.malczuuu.mqttbot.rest.exception;

import io.github.malczuuu.problem4j.core.Problem;
import io.github.malczuuu.problem4j.core.ProblemBuilder;
import io.github.malczuuu.problem4j.core.ProblemException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends ProblemException {

  public NotFoundException(String detail, Problem.Extension... extensions) {
    super(buildProblem(detail, extensions));
  }

  private static Problem buildProblem(String detail, Problem.Extension... extensions) {
    ProblemBuilder builder =
        Problem.builder()
            .title(HttpStatus.NOT_FOUND.getReasonPhrase())
            .status(HttpStatus.NOT_FOUND.value())
            .detail(detail);

    for (Problem.Extension extension : extensions) {
      builder = builder.extension(extension.getKey(), extension.getValue());
    }

    return builder.build();
  }
}
