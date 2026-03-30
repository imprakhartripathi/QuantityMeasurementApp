FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml ./
COPY src ./src

RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app

RUN addgroup --system spring && adduser --system spring --ingroup spring
COPY --from=build /workspace/target/*-SNAPSHOT.jar /app/app.jar

USER spring:spring
EXPOSE 4000

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
