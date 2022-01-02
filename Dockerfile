# Phase 1 - Build the application .jar file and name it builder
FROM adoptopenjdk/openjdk11:alpine-slim as builder
VOLUME /tmp
COPY . .
RUN chmod +x gradlew
RUN ./gradlew build

# Phase 2 - Build container with runtime only to use .jar file within and port assigned by heroku
FROM openjdk:11.0-jre-slim
WORKDIR /app
COPY --from=builder build/libs/*.jar app.jar
CMD [ "sh", "-c", "java -Xmx300m -Xss512k -Dserver.port=$PORT $JAVA_OPTS -jar /app/app.jar" ]
