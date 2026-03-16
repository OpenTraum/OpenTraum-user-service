FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY build/libs/opentraum-user-service-*.jar app.jar

RUN chown -R appuser:appgroup /app

USER appuser

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
