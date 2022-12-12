# This is for 52Hz
FROM java:8

COPY *.jar /app.jar

CMD ["--server.port=7070"]

VOLUME /tmp

EXPOSE 7070

RUN 	cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime\
	&& echo 'Asia/Shanghai' > /etc/timezone

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

