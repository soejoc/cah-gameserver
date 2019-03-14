FROM openjdk:8
VOLUME /tmp /log
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-noverify", "-javaagent:/app/lib/spring-instrument.jar", "-cp","app:app/lib/*","io.jochimsen.cahgameserver.CahGameserverApplication"]