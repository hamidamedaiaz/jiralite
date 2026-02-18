# ════ STAGE 1 : BUILD ════
# On part d'une image qui a Maven + Java
FROM maven:3.9.11-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline   # télécharge les dépendances
COPY src ./src
RUN mvn clean package -DskipTests  # génère le .jar

# ════ STAGE 2 : RUN ════
# On repart d'une image légère avec juste Java
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# On copie UNIQUEMENT le .jar depuis le stage 1
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]