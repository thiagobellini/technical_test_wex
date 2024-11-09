FROM eclipse-temurin:17-jdk-alpine AS build
LABEL authors="Thiago Bellini"
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/technical_test_wex-0.0.1-SNAPSHOT.jar technical_test_wex.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "technical_test_wex.jar"]