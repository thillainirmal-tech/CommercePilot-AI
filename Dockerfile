FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/commercepilot-ai-1.0.0.jar app.jar
EXPOSE 8088
USER 10001
ENTRYPOINT ["java","-jar","app.jar"]

