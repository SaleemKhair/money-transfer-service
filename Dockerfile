FROM amazoncorretto

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /opt/app.jar
WORKDIR /opt

ENTRYPOINT ["java","-jar","-Dapplication.name=${APP_NAME}", "/opt/app.jar"]