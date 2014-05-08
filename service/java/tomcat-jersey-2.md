---
layout: default
title: Jersey 2.x Java service running on Tomcat 7 
selectedMenu: service
---

{% include java-jaxrs-page-header.html summaryTitle="Jersey 2.x on Tomcat 7" framework="Jersey" %}

## Dependencies
If you're using maven you'll need to add swagger's library for Jersey 2.x to your pom.xml as shown below. Be sure to lookup [the latest version number](https://oss.sonatype.org/content/repositories/releases/com/wordnik/swagger-jersey2-jaxrs_2.10/).

```xml
<dependency>
    <groupId>com.wordnik</groupId>
    <artifactId>swagger-jersey2-jaxrs_2.10</artifactId>
    <version>1.3.5</version>
</dependency>
```

## Configuration

### Setup Jersey Servlet

Setup Jersey servlet which will serve your API resources. To do this, you need to tell Jersey which packages it should look for resources in. In addition to your own packages be sure to add these two swagger packages:

1. Classes in `com.wordnik.swagger.jaxrs.listing`: Provides the main [swagger resource listing](https://github.com/wordnik/swagger-core/blob/master/modules/swagger-jaxrs/src/main/scala/com/wordnik/swagger/jaxrs/listing/ApiListingResource.scala#L9) at /api-docs path
2. `com.wordnik.swagger.jaxrs.json`: This is a provider which [configures](https://github.com/wordnik/swagger-core/blob/master/modules/swagger-jaxrs/src/main/scala/com/wordnik/swagger/jaxrs/json/JacksonJsonProvider.java#L47-L50) JSON serialization.

```xml
    <servlet>
        <servlet-name>jersey</servlet-name>
        <servlet-class>org.glassfish.jersey.servlet.ServletContainer</servlet-class>
        <init-param>
            <param-name>jersey.config.server.provider.classnames</param-name>
            <param-value>
                com.wordnik.swagger.jersey.listing.ApiListingResourceJSON,
                com.wordnik.swagger.jersey.listing.JerseyApiDeclarationProvider,
                com.wordnik.swagger.jersey.listing.JerseyResourceListingProvider
            </param-value>
        </init-param>
        <init-param>
            <param-name>jersey.config.server.provider.packages</param-name>
            <param-value>
                com.wordnik.swagger.jaxrs.json,
                [your.service.package]
            </param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
```

<br>

{% include java-required-swagger-config.html %}

{% include java-optional-swagger-config.html %}

{% include java-swagger-annotations.html %}

{% include java-secure-swagger.html %}

## Sample Service
You can see a [sample service](https://github.com/ayush/swagger-samples/tree/master/service/java/tomcat-7-jersey-2) for Jersey 2 on Tomcat 7. To run it:

```bash
git clone git@github.com:ayush/swagger-samples.git
cd swagger-samples/service/java/tomcat-7-jersey-2
mvn tomcat7:run
```

You can then see:

- Root listing at: [http://localhost:9090/api/api-docs](http://localhost:9090/api/api-docs)
- Pet resource listing at [http://localhost:9090/api/api-docs/pet](http://localhost:9090/api/api-docs/pet)
- User resource listing at [http://localhost:9090/api/api-docs/user](http://localhost:9090/api/api-docs/user)
- /store is behing API key auth here. So by suffixing `api_key=special-key` you can see:
  - In root listing see /store API via [http://localhost:9090/api/api-docs?api_key=special-key](http://localhost:9090/api/api-docs?api_key=special-key)
  - Access /store API listing via [http://localhost:9090/api/api-docs/store?api_key=special-key](http://localhost:9090/api/api-docs/store?api_key=special-key)

<!-- `swagger.filter`: This is an optional feature which lets you protect access to some APIs. To do so, you can provide an implementation of [SwaggerSpecFilter](https://github.com/wordnik/swagger-core/blob/master/modules/swagger-core/src/main/scala/com/wordnik/swagger/core/filter/SpecFilter.scala#L29-L32) and give the full name of that class here.
 -->

<br>
<br>
<br>
<br>


<script type="text/javascript">
	setActive('.java-menu', 'tomcat')
	setActive('.java-sub-menu', 'jersey-2')
</script>