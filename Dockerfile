FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY build/libs/usuario.jar usuario.jar
EXPOSE 9090
CMD ["java", "-jar", "usuario.jar"]

