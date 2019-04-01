FROM postgres:11.2
# RUN localedef -i en_US -c -f UTF-8 -A /usr/share/locale/locale.alias en_US.UTF-8
# ENV LANG zh_CN.utf8
COPY ./*.* /docker-entrypoint-initdb.d/
