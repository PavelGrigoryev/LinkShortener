FROM openjdk:17-jdk

ADD target/LinkShortener-0.0.1-SNAPSHOT.jar LinkShortener.jar

ENTRYPOINT ["java", "-jar", "LinkShortener.jar"]

EXPOSE 8080
