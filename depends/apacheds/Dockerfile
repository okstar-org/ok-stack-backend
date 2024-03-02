#** Copyright (c) 2022 船山信息 chuanshaninfo.com
#OkStack is licensed under Mulan PubL v2.
#You can use this software according to the terms and conditions of the Mulan
#PubL v2. You may obtain a copy of Mulan PubL v2 at:
#         http://license.coscl.org.cn/MulanPubL-2.0
#THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
#EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
#MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
#See the Mulan PubL v2 for more details.
#

FROM openjdk:11-jre-slim-bullseye

# Install LDAP tools
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y -qq --no-install-recommends \
    ldap-utils netcat wget procps \
    && rm -rf /var/lib/apt/lists/*

# Define ApacheDS version
ENV APACHEDS_VERSION=2.0.0.AM26
ENV APACHEDS_SERVICE_NAME=apacheds-${APACHEDS_VERSION}-default

# Install ApacheDS
RUN wget https://archive.apache.org/dist/directory/apacheds/dist/${APACHEDS_VERSION}/apacheds-${APACHEDS_VERSION}-amd64.deb -q -O /tmp/apacheds.deb \
    && dpkg -i /tmp/apacheds.deb \
    && rm /tmp/apacheds.deb

# Add fortress schema
ADD schema/okstar-partitions.ldif /tmp
ADD schema/okstar-ou.ldif /tmp

RUN service ${APACHEDS_SERVICE_NAME} start \
    && timeout 10 sh -c "while ! nc -z localhost 10389; do sleep 1; done" \
    && service ${APACHEDS_SERVICE_NAME} status \
    && ldapadd -v -x -h localhost -p 10389 -a -f /tmp/okstar-partitions.ldif \
    && sleep 5 \
    && service ${APACHEDS_SERVICE_NAME} stop
RUN sleep 5
RUN service ${APACHEDS_SERVICE_NAME} start \
    && service ${APACHEDS_SERVICE_NAME} status \
    && timeout 10 sh -c "while ! nc -z localhost 10389; do sleep 1; done" \
    && ldapmodify -v -x -h localhost -p 10389 -a -f /tmp/okstar-ou.ldif \
    && service ${APACHEDS_SERVICE_NAME} stop

RUN echo "done."

EXPOSE 10389
CMD ["sh", "-c", "service ${APACHEDS_SERVICE_NAME} console"]
