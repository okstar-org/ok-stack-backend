FROM eclipse-temurin:17-jdk

LABEL maintainer="cto@chuanshaninfo.com"

ENV OK_STAR_USER=okstar
ENV OK_STACK_DIR=/home/okstar/ok-stack
ENV OK_STAR_DIR=/home/okstar
ENV OK_STACK_DATA_DIR=/home/okstar/ok-stack
ENV OK_STACK_LOG_DIR=/home/okstar/ok-stack/logs

RUN apt-get update -qq && apt-get dist-upgrade -y
RUN cat /etc/apt/sources.list

RUN apt install -y nginx-full zip unzip
RUN rm -rf /var/lib/apt/lists/*

WORKDIR ${OK_STACK_DIR}

COPY ./build/docker/entrypoint.sh /sbin/entrypoint.sh
RUN chmod 755 /sbin/entrypoint.sh

COPY platform-infra/commons/common-base/src/main/resources/lib/libsigar-amd64-linux-1.6.4.so /lib
ADD distribution/target/ok-stack-assembly.tar.gz ${OK_STAR_DIR}

# UI
COPY build/docker/default /etc/nginx/sites-enabled

#ok-stack-ui/dist/ok-stack-ui.zip
ADD ok-stack-ui/dist/ok-stack-ui.zip ${OK_STAR_DIR}
RUN unzip ${OK_STAR_DIR}/ok-stack-ui.zip -d /usr/share/nginx/html/

EXPOSE 80
VOLUME ["${OK_STACK_LOG_DIR}"]
ENTRYPOINT [ "/sbin/entrypoint.sh" ]
