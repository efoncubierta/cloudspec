FROM amazoncorretto:8
ARG VERSION
COPY runner/target/cloudspec-${VERSION}.jar /cloudspec.jar
ENTRYPOINT ["java", "-jar", "/cloudspec.jar"]