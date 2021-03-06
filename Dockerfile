FROM openjdk:8-alpine
MAINTAINER u6k.apps@gmail.com

# Setup Apache Maven
RUN apk update && \
    apk add maven

# Setup work directory
RUN mkdir -p /var/my-app
WORKDIR /var/my-app

# Setup docker run setting
CMD ["mvn", "clean", "spring-boot:run"]
