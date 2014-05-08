---
layout: default
title: Swaggerize a Java service
selectedMenu: service
---

<br>

{% include menu-java.html %}

{% include menu-java-tomcat.html %}

## Summary
Jersey 1.x on Tomcat 7

1. Tell Jersey about Swagger.
2. Configure Swagger so that it knows a few things about your API.
3. Annotate your resources so Swagger can serve them via its resource listing.
4. Annotate your models so Swagger can include type information in its its resource listing.
5. Optionally, put API access behind OAuth.

## Dependencies
If you're using maven you'll need to add swagger's library for Jersey 1.x to your pom.xml as shown below. Be sure to lookup [the latest version number](https://oss.sonatype.org/content/repositories/releases/com/wordnik/swagger-jersey-jaxrs_2.10/).

```xml
<dependency>
    <groupId>com.wordnik</groupId>
    <artifactId>swagger-jersey-jaxrs_2.10</artifactId>
    <version>1.3.5</version>
</dependency>
```

## Configuration

### Setup Jersey Servlet

Setup Jersey servlet which will serve your API resources. To do this, you need to tell Jersey which packages it should look for resources in. In addition to your own packages be sure to add these two swagger packages:

1. `com.wordnik.swagger.jaxrs.json`: Provides the main [swagger resource listing](https://github.com/wordnik/swagger-core/blob/master/modules/swagger-jaxrs/src/main/scala/com/wordnik/swagger/jaxrs/listing/ApiListingResource.scala#L9) at /api-docs path
2. `com.wordnik.swagger.jaxrs.listing`: This is a provider which [configures](https://github.com/wordnik/swagger-core/blob/master/modules/swagger-jaxrs/src/main/scala/com/wordnik/swagger/jaxrs/json/JacksonJsonProvider.java#L47-L50) JSON serialization.

```xml
<servlet>
    <servlet-name>jersey</servlet-name>
    <servlet-class>com.sun.jersey.spi.container.servlet.ServletContainer</servlet-class>
    <init-param>
        <param-name>com.sun.jersey.config.property.packages</param-name>
        <param-value>
            com.wordnik.swagger.jaxrs.json;[your.service.package];com.wordnik.swagger.jaxrs.listing
        </param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>
```

<br>

### Required Swagger configuration

There are a couple of things you need to configure for your Swagger deployment. This includes:

1. `api.version`: This is the version of your API so you can put what you want here.
2. `swagger.api.basepath`: The basepath from which swagger resource listing is served.

```xml
<servlet>
    <servlet-name>DefaultJaxrsConfig</servlet-name>
    <servlet-class>com.wordnik.swagger.jaxrs.config.DefaultJaxrsConfig</servlet-class>
    <init-param>
        <param-name>api.version</param-name>
        <param-value>1.0.0</param-value>
    </init-param>
    <init-param>
        <param-name>swagger.api.basepath</param-name>
        <param-value>http://localhost:9090/api</param-value>
    </init-param>
    <load-on-startup>2</load-on-startup>
</servlet>
```

### Optional Swagger configuration
If you like, you can also give the following information about your API to Swagger:

1. title
2. description
3. termsOfServiceUrl
4. contact
5. license
6. licenseUrl

Lets do this in a servlet too:

```java
import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.model.ApiInfo;

public class Bootstrap extends HttpServlet {
  static {
        ApiInfo info = new ApiInfo(
          "Swagger Sample App",                             /* title */
          "This is a sample server Petstore server.  You can find out more about Swagger " + 
          "at <a href=\"http://swagger.wordnik.com\">http://swagger.wordnik.com</a> or on irc.freenode.net, #swagger.  For this sample, " + 
          "you can use the api key \"special-key\" to test the authorization filters", 
          "http://helloreverb.com/terms/",                  /* TOS URL */
          "apiteam@wordnik.com",                            /* Contact */
          "Apache 2.0",                                     /* license */
          "http://www.apache.org/licenses/LICENSE-2.0.html" /* license URL */
        );

        ConfigFactory.config.setApiInfo(info);
    }
}
```

And of course add this servlet to web.xml:

```xml
    <servlet>
        <servlet-name>Bootstrap</servlet-name>
        <servlet-class>com.wordnik.swagger.sample.Bootstrap</servlet-class>
        <load-on-startup>2</load-on-startup>
    </servlet>
```

Note that `ConfigFactory.config`, which we're using in our sample Bootstap above, lets you configure [several other properties](https://github.com/wordnik/swagger-core/blob/master/modules/swagger-core/src/main/scala/com/wordnik/swagger/config/SwaggerConfig.scala#L10-L13) programatically.

### Annotate your code

There is more information available in [Swagger annotations javadoc]({{site.baseurl}}javadocs/jaxrs-annotations/index.html).

### OAuth with Swagger


3. `swagger.filter`: This is an optional feature which lets you protect access to some APIs. To do so, you can provide an implementation of [SwaggerSpecFilter](https://github.com/wordnik/swagger-core/blob/master/modules/swagger-core/src/main/scala/com/wordnik/swagger/core/filter/SpecFilter.scala#L29-L32) and give the full name of that class here.





<script type="text/javascript">
	setActive('.java-menu', 'tomcat')
	setActive('.java-sub-menu', 'jersey1')
</script>