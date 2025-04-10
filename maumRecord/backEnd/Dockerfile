FROM openjdk:17-slim

RUN apt-get update && apt-get install -y findutils

WORKDIR /app

COPY backEnd/gradlew .
COPY backEnd/gradle ./gradle
COPY backEnd/build.gradle .
COPY backEnd/settings.gradle .
COPY backEnd/src ./src

# 종속성 미리 다운
RUN ./gradlew --no-daemon build -x test

EXPOSE 8080
CMD ["./gradlew", "--no-daemon", "bootRun"]