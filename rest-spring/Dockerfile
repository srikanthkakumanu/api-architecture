FROM eclipse-temurin:26-jdk
CMD ./gradlew build
COPY build/libs/rest-spring-0.0.1.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
