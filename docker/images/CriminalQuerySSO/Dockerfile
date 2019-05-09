FROM 11.0.3-jre
RUN mkdir /app
COPY ./criminalquery-sso.jar /app
WORKDIR /app
CMD [ "java", "-jar", "criminalquery-sso.jar" ]
EXPOSE 8081