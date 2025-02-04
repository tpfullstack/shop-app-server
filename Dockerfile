FROM maven:3.9.9-amazoncorretto-23 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests
FROM amazoncorretto:23
WORKDIR /app
COPY --from=build /app/target/*.jar shop-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "shop-app.jar"]