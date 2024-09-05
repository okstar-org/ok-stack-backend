FROM eclipse-temurin:17-jdk

LABEL maintainer="cto@chuanshaninfo.com"

ENV OK_STAR_USER=okstar
ENV OK_PROJECT=ok-stack-backend
ENV OK_STAR_DIR=/home/${OK_STAR_USER}
ENV OK_STACK_DIR=${OK_STAR_DIR}/${OK_PROJECT}
ENV OK_STACK_LOG_DIR=${OK_STAR_DIR}/${OK_PROJECT}/logs

WORKDIR ${OK_STACK_DIR}

RUN apt-get update -qq && apt-get dist-upgrade -y
RUN cat /etc/apt/sources.list

RUN apt install -y nginx-full zip unzip
RUN rm -rf /var/lib/apt/lists/*


COPY thirdparty/ok-commons/common-base/src/main/resources/lib/libsigar-amd64-linux-1.6.4.so /lib
ADD distribution/target/ok-stack-backend-assembly.tar.gz ${OK_STAR_DIR}

# UI
COPY build/docker/default /etc/nginx/sites-enabled

#ok-stack-ui/dist/ok-stack-ui.zip
ADD ok-stack-ui/dist/ok-stack-ui.zip ${OK_STAR_DIR}
RUN unzip ${OK_STAR_DIR}/ok-stack-ui.zip -d /usr/share/nginx/html/



EXPOSE 80
VOLUME ["${OK_STACK_LOG_DIR}"]


COPY ./build/docker/entrypoint.sh /sbin/entrypoint.sh
RUN chmod 755 /sbin/entrypoint.sh
ENTRYPOINT [ "/sbin/entrypoint.sh" ]
