---
layout: default
title: RESTEasy 2.3.5 Java service running on Tomcat 7 
selectedMenu: service
---

{% include java-jaxrs-page-header.html summaryTitle="RESTEasy 2.3.5 on Tomcat 7" framework="RESTEasy" %}

## Dependencies
If you're using maven you'll need to add swagger's jaxrs library to your pom.xml as shown below. Be sure to lookup [the latest version number](https://oss.sonatype.org/content/repositories/releases/com/wordnik/swagger-jaxrs_2.10/).

```xml
<dependency>
    <groupId>com.wordnik</groupId>
    <artifactId>swagger-jaxrs_2.10</artifactId>
    <version>1.3.5</version>
</dependency>
```

## Configuration

### Setup RESTEasy

Setup RESTEasy to serve up your API resources. Setup your own application resources the usual way by specifying a [javax.ws.rs.core.Application](http://docs.jboss.org/resteasy/docs/2.3.5.Final/userguide/html_single/index.html#javax.ws.rs.core.Application) like so:

```xml
<context-param>
    <param-name>javax.ws.rs.core.Application</param-name>
    <param-value>[you.package.YourApplication]</param-value>
</context-param>

```

<br>

Now, setup RESTEasy to serve the main [swagger resource listing](https://github.com/wordnik/swagger-core/blob/master/modules/swagger-jaxrs/src/main/scala/com/wordnik/swagger/jaxrs/listing/ApiListingResource.scala#L9) at /api-docs path:

```xml
<context-param>
    <param-name>resteasy.resources</param-name>
    <param-value>com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON</param-value>
</context-param>

```

<br>

And some additional providers to ensure correct JSON handling for Swagger:

```xml
<context-param>
    <param-name>resteasy.providers</param-name>
    <param-value>
        com.wordnik.swagger.jaxrs.json.JacksonJsonProvider,
        com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider,
        com.wordnik.swagger.jaxrs.listing.ResourceListingProvider
    </param-value>
</context-param>
```

<br>

{% include java-required-swagger-config.html %}

<br>

{% include java-optional-swagger-config.html %}

<br>

{% include java-swagger-annotations.html %}

<br>

{% include java-secure-swagger.html %}

<br>

## Sample Service
You can see a [sample service](https://github.com/ayush/swagger-samples/tree/master/service/java/tomcat-7-jersey-2) for RESTEasy 2.3.5 on Tomcat 7. To run it:

```bash
git clone git@github.com:ayush/swagger-samples.git
cd swagger-samples/service/java/tomcat-7-RESTEasy-2
mvn tomcat7:run
```

You can then see:

- Root listing at: [http://localhost:9090/resteasy/api-docs](http://localhost:9090/resteasy/api-docs)
- Library resource listing at [http://localhost:9090/resteasy/api-docs/library](http://localhost:9090/resteasy/api-docs/library)

<!-- `swagger.filter`: This is an optional feature which lets you protect access to some APIs. To do so, you can provide an implementation of [SwaggerSpecFilter](https://github.com/wordnik/swagger-core/blob/master/modules/swagger-core/src/main/scala/com/wordnik/swagger/core/filter/SpecFilter.scala#L29-L32) and give the full name of that class here.
 -->

<br>
<br>
<br>
<br>


<script type="text/javascript">
	setActive('.java-menu', 'tomcat')
	setActive('.java-sub-menu', 'resteasy-2')
</script>