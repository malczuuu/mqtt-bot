package io.github.malczuuu.mqttbot.rest.exception;

import io.github.malczuuu.problem4j.core.Problem;
import io.github.malczuuu.problem4j.spring.web.ExceptionAdapter;
import io.github.malczuuu.problem4j.spring.web.ProblemProperties;
import io.github.malczuuu.problem4j.spring.web.ProblemResponseEntityExceptionHandler;
import java.util.List;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ExtendedProblemResponseEntityExceptionHandler
    extends ProblemResponseEntityExceptionHandler {

  public ExtendedProblemResponseEntityExceptionHandler(
      JacksonProperties jacksonProperties,
      ProblemProperties problemProperties,
      List<ExceptionAdapter> adapters) {
    super(jacksonProperties, problemProperties, adapters);
  }

  @ExceptionHandler(OptimisticLockingFailureException.class)
  public ResponseEntity<Object> handleOptimisticLockingFailureException(
      OptimisticLockingFailureException ex, WebRequest request) {
    HttpStatus status = HttpStatus.CONFLICT;
    HttpHeaders headers = new HttpHeaders();
    Problem problem =
        Problem.builder()
            .title(status.getReasonPhrase())
            .status(status.value())
            .detail("version mismatch")
            .build();
    return handleExceptionInternal(ex, problem, headers, status, request);
  }
}
