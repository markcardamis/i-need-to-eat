FROM java

VOLUME /tmp
##EXPOSE 8080
ADD lunchapp-0.0.1-SNAPSHOT.jar app.jar
##ADD /build/libs/lunchapp-0.0.1-SNAPSHOT.jar app.jar

RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
