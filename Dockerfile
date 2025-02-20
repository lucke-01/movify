FROM maven:3.8.8-eclipse-temurin-21-alpine AS build
RUN mkdir /home/app
COPY . /home/app
RUN mvn -f /home/app/pom.xml clean install -DskipTests


FROM openjdk:21
COPY --from=build /home/app/target/*.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Dspring.profiles.active=docker", "-jar" ,"/usr/local/lib/app.jar"]