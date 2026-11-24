# ---------- Etapa 1: compilar el código y armar el WAR ----------
FROM tomcat:10.1-jdk21 AS build
WORKDIR /app
COPY PrototipoLimpio/ ./
RUN mkdir -p war/WEB-INF/classes war/WEB-INF/lib \
 && cp -r web/. war/ \
 && cp ./*.jar war/WEB-INF/lib/ \
 && javac -implicit:none --release 21 -encoding UTF-8 \
      -d war/WEB-INF/classes \
      -cp "/usr/local/tomcat/lib/*:war/WEB-INF/lib/*" \
      $(find src/java -name "*.java") \
 && cd war && jar cf /app/ROOT.war .

# ---------- Etapa 2: imagen final con Tomcat ----------
FROM tomcat:10.1-jdk21
RUN sed -i 's/port="8005"/port="-1"/g' /usr/local/tomcat/conf/server.xml
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/ROOT.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
