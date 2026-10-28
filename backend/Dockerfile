FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw && ./mvnw -q -B dependency:go-offline
COPY src src
RUN ./mvnw -q -B -DskipTests package

FROM eclipse-temurin:21-jre
RUN useradd --system --no-create-home hub
WORKDIR /app
COPY --from=build /app/target/sponsorship-hub-0.0.1-SNAPSHOT.jar app.jar
USER hub
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
