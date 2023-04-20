ARG DOCKER
FROM $DOCKER
ARG APP_VER
ENV VER=${APP_VER}
ENV JAVA_OPTS=""
WORKDIR /application
COPY rangiffler-auth-$VER.jar ./

EXPOSE 9000
CMD java -jar -Dspring.profiles.active=docker $JAVA_OPTS rangiffler-auth-$VER.jar
