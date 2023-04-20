ARG DOCKER
FROM $DOCKER
ARG APP_VER
ENV VER=${APP_VER}
ENV JAVA_OPTS=""
WORKDIR /application
COPY rangiffler-gateway-$VER.jar ./

EXPOSE 8090
CMD java -jar -Dspring.profiles.active=docker $JAVA_OPTS rangiffler-gateway-$VER.jar
