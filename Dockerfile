FROM eclipse-temurin:17-jre

ENV OK_STAR_USER=okstar \
    OK_STACK_DIR=/home/okstar/ok-stack  \
    OK_STAR_DIIR=/home/okstar   \
    OK_STAR_DATA_DIR=/home/okstar/ok-stack

RUN apt-get update -qq \
    && apt-get install -yqq sudo \
    && adduser --disabled-password --quiet --system --home $OK_STAR_DIIR --group $OK_STAR_USER \
    && rm -rf /var/lib/apt/lists/*

COPY ./build/docker/entrypoint.sh /sbin/entrypoint.sh
RUN chmod 755 /sbin/entrypoint.sh
RUN mkdir ${OK_STACK_DIR}

COPY --chown=${OK_STAR_USER}:${OK_STAR_USER} distribution/target/ok-stack-assembly.tar.gz ${OK_STAR_DIIR}
RUN tar -xvf ${OK_STAR_DIIR}/ok-stack-assembly.tar.gz -C ${OK_STACK_DIR}
RUN rm -f ${OK_STAR_DIIR}/ok-stack-assembly.tar.gz

LABEL maintainer="cto@chuanshaninfo.com"
WORKDIR ${OK_STACK_DIR}

EXPOSE 9000 9100 9200 9300 9400
VOLUME ["${OK_STAR_DATA_DIR}"]
ENTRYPOINT [ "/sbin/entrypoint.sh" ]
