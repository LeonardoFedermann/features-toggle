FROM openjdk-20
COPY target/users-and-features-toggle.jar /app/users-and-features-toggle.jar
WORKDIR /app
RUN apt-get update && apt-get install -y mysql-server
EXPOSE 8080
CMD ["java", "-jar", "users-and-features-toggle.jar"]