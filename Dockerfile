FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/learning-course-app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
