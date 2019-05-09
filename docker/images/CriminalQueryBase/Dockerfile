FROM 11.0.3-jre
RUN mkdir /app
COPY ./criminalquerybase.jar /app
WORKDIR /app
CMD [ "java", "-jar", "criminalquerybase.jar" ]
EXPOSE 8082