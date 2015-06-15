# Blog microserice app using Javaee7 

The microservices-based application using Java EE 7, JPA and RESTful Web Services. Prepared for wildfly or glassfish containers. 

## Integration testing
For integration testing I added Arquillian with 2 profiles for grassfish 4.1 and wildfly 8.2 containers.
Run integration test 
```
mvn clean && mvn test -Parquillian-glassfish-embedded
# OR 
mvn clean && mvn test -P arquillian-wildfly-managed
```

## Run application
The most straightforward way to do it is via docker. There is a script srartup.sh which makes package, runs database docker container and glassfish docker container. 
```
sh docker/startup.sh
```
You can check Blog microservice by [http://docker-host:8080/blog/resources/posts](http://docker-host:8080/blog/resources/posts)




