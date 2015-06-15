# Use latest jboss/base-jdk:8 image as the base
FROM jboss/base-jdk:8

# Set the WILDFLY_VERSION env variable
ENV WILDFLY_VERSION 8.2.0.Final

# Add the WildFly distribution to /opt, and make wildfly the owner of the extracted tar content
RUN cd $HOME && curl -O http://download.jboss.org/wildfly/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.zip && unzip wildfly-$WILDFLY_VERSION.zip && mv $HOME/wildfly-$WILDFLY_VERSION $HOME/wildfly && rm wildfly-$WILDFLY_VERSION.zip

# Set the JBOSS_HOME env variable
ENV JBOSS_HOME /opt/jboss/wildfly

# Expose the ports we're interested in
EXPOSE 8080 9990

# add mysql
COPY module.xml /opt/jboss/wildfly/modules/system/layers/base/com/mysql/driver/main/module.xml
USER root
RUN curl http://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.34/mysql-connector-java-5.1.34.jar -o /opt/jboss/wildfly/modules/system/layers/base/com/mysql/driver/main/mysql-connector-java-5.1.34.jar
USER root
RUN chown -R jboss:jboss /opt/jboss/wildfly/modules/system/layers/base/com/mysql/
COPY standalone-full.xml /opt/jboss/wildfly/standalone/configuration/standalone-full.xml

# deploy app
COPY blog.war /opt/jboss/wildfly/standalone/deployments/blog.war

RUN /opt/jboss/wildfly/bin/add-user.sh admin adminadmin --silent
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-c", "standalone-full.xml", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

