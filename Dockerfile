FROM eclipse-temurin:17-jre-alpine

EXPOSE 8080

WORKDIR /hackathon-santo-digital

COPY ./target/hackathon-santo-digital-*.jar /hackathon-santo-digital.jar

CMD ["java", "-jar", "/hackathon-santo-digital.jar"]
