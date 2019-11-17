FROM java

VOLUME /tmp

EXPOSE 8080

ADD lunchapp-1.0.0.jar lunchapp.jar

RUN bash -c 'touch /lunchapp.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/lunchapp.jar"]
