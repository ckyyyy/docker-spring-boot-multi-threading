FROM openjdk:8
ADD target/springboot-multithreading.jar springboot-multithreading.jar
EXPOSE 9191
ENTRYPOINT ["java", "-jar", "springboot-multithreading.jar"]