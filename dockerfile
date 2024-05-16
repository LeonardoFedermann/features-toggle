FROM openjdk-20
COPY target/features-toggle.jar /app/features-toggle.jar
WORKDIR /app
RUN apt-get update && apt-get install -y mysql-server
EXPOSE 8080
CMD ["java", "-jar", "features-toggle.jar"]