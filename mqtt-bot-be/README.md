# MQTT Bot BE

Backend service for MQTT Bot.

## Useful Gradle commands

Consider adding following Gradle configurations to your IDE.

1. **`./gradlew bootRun`**. Start Spring Boot server. In your IDE you mey consider running directly from `main()` method
   in [`Application`][Application.java] class.
2. **`./gradlew spotlessApply build`**. Format code according to Google Java Format and build project. **Note** that
   `spotless` plugin require code to be properly formatted of `build` job will fail.
3. **`./gradlew build --refresh-dependencies`**. To build and ignore cached dependencies. Useful if using snapshot
   builds of libraries.

[Application.java]: ./src/main/java/io/github/malczuuu/mqttbot/Application.java
