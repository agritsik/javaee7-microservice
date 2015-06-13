# Wildfly container configured for Blog app
#
# VERSION	0.1

FROM jboss/wildfly
MAINTAINER Andrii Grytsyk

# add mysql
COPY module.xml wildfly/modules/system/layers/base/com/mysql/driver/main/module.xml

USER root
RUN curl http://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.34/mysql-connector-java-5.1.34.jar -o wildfly/modules/system/layers/base/com/mysql/driver/main/mysql-connector-java-5.1.34.jar

USER root
RUN chown -R jboss:jboss wildfly/modules/system/layers/base/com/mysql/driver/main/

COPY standalone.xml wildfly/standalone/configuration/standalone.xml

COPY blog.war wildfly/standalone/deployments/blog.war

# run admin console
RUN /opt/jboss/wildfly/bin/add-user.sh admin adminadmin --silent
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]