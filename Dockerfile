FROM amazoncorretto:21-alpine-jdk

COPY sgart.backend/target/sgart_backend-0.1.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]