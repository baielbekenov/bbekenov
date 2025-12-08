FROM gradle:8.5-jdk21 AS build

WORKDIR /app
COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew build -x test --no-daemon --warning-mode all

FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]