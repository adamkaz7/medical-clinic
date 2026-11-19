FROM eclipse-temurin:21-jdk
COPY target/medical-clinic-0.0.1-SNAPSHOT.jar /medical-clinic.jar
ENTRYPOINT ["java", "-jar", "/medical-clinic.jar"]