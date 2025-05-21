package io.github.malczuuu.mqttbot;

import io.github.malczuuu.problem4j.spring.web.EnableProblem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableProblem
@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
