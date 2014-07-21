To run the sample:

```
mvn package

<<<<<<< HEAD
java -jar target/swagger-java-dropwizard-sample-app_2.10-1.3.8-SNAPSHOT.jar server conf/swagger-sample.yml 
=======
java -jar target/swagger-java-dropwizard-sample-app_2.11-1.3.7-SNAPSHOT.jar server conf/swagger-sample.yml 
>>>>>>> develop_scala-2.11

```

You can then access swagger at http://localhost:8080/api-docs
