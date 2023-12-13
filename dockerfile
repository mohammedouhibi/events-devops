FROM openjdk:8
EXPOSE 8089
ADD target/eventsProject.jar eventsProject.jar
ENTRYPOINT ["java","-jar","/eventsProject.jar"]