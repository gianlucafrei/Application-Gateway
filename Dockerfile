#
# Build stage
#
FROM maven:3.6.3-openjdk-15 AS build
COPY oag/src /home/app/src
COPY oag/pom.xml /home/app
COPY oag/*.yaml /home/app
COPY oag/*.txt /home/app
RUN mvn dependency:go-offline -B -f /home/app/pom.xml
RUN mvn package -f /home/app/pom.xml

#
# Package stage
#
FROM openjdk:15.0.2-oraclelinux7
RUN useradd --user-group --system --create-home --no-log-init app

RUN mkdir -p /app
RUN chown app /app

COPY --from=build /home/app/target/* /home/app/*.yaml /home/app/*.txt /app/

RUN mv /app/*.jar /app/oag.jar

USER app
WORKDIR /app
ENTRYPOINT ["java","-jar","oag.jar"]

#
# HOW TO:
#
# docker build -t owasp/application-gateway:SNAPSHOT .
# docker run -p 8080:8080 owasp/application-gateway:SNAPSHOT

# Test for docker misconfiguration with dockle
# VERSION=$(curl --silent "https://api.github.com/repos/goodwithtech/dockle/releases/latest" | grep '"tag_name":' | sed -E 's/.*"v([^"]+)".*/\1/') && curl -L -o dockle.tar.gz https://github.com/goodwithtech/dockle/releases/download/v${VERSION}/dockle_${VERSION}_Linux-64bit.tar.gz &&  tar zxvf dockle.tar.gz
# ./dockle --exit-code 1 owasp/application-gateway:SNAPSHOT

# Publish docker image
# docker push owasp/application-gateway:SNAPSHOT