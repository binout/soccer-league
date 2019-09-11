FROM maven:3.6.1-jdk-8 as build
COPY . /workspace 

WORKDIR /workspace

ENV MAVEN_OPTS="-Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn -Djansi.force=true"
RUN mvn -B -Dstyle.color=always package 

FROM gcr.io/distroless/java:8
EXPOSE 8080
WORKDIR /app

COPY --from=build /workspace/target/soccer-league.jar /app/soccer-league.jar

CMD ["soccer-league.jar"]
