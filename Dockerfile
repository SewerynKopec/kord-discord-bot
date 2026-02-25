

COPY src /src
WORKDIR /src
RUN ./gradlew --no-daemon shadowJar

FROM amazoncorretto:25-alpine

WORKDIR /app

COPY --from=BUILD /src/build/libs/*-all.jar app.jar

RUN addgroup -S app && adduser -S app -G app
USER app

ENTRYPOINT ["java","-jar","/bin/project/run.jar"]
