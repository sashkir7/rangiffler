ARG DOCKER
FROM $DOCKER
ARG APP_VER
ENV VER=${APP_VER}
ENV JAVA_OPTS=""
WORKDIR /application
COPY rangiffler-geo-$VER.jar ./

EXPOSE 9004
CMD java -jar -Dspring.profiles.active=docker $JAVA_OPTS rangiffler-geo-$VER.jar
