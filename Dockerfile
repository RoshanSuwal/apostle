FROM openjdk:17-alpine
RUN mkdir /etc/ekmiddleware

RUN mkdir /var/log/ekmiddleware

COPY ./apostle /opt/
COPY env_var.properties /etc/ekmiddleware/properties/

WORKDIR /opt

RUN #ls bin

RUN ["chmod", "+x", "./bin/apostle.sh"]
CMD ./bin/apostle.sh
