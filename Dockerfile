FROM gcr.io/distroless/java17-debian11:debug

COPY target/*.jar /app/app.jar
WORKDIR /app
EXPOSE 8080 8443
USER 1000
CMD ["app.jar"]
