---
layout: default
title: beautify thy api
---

APIs should be beautiful. On the inside & the outside.

<br>

### API Structure
Swagger allows you to describe the structure of your APIs so that machines can read them. 

The ability of APIs to describe their own structure is the root of all awesomeness in Swagger. Why is it so great? Well, by reading your API's structure we can automatically build beautiful and interactive API documentation. We can also automatically generate client libraries for your API in many languages and explore other possibilities like automated testing.

Swagger does this by asking your API to return a JSON that contains a detailed description of your entire API. This JSON is essentially a resource listing of your API which adheres to [Swagger Specification](https://github.com/wordnik/swagger-spec/blob/master/versions/1.2.md). The specification asks you to include information like:

* What are all the operation that your API supports?
* What are your API's parameters and what does it return?
* Does your API need some authorization?
* Even fun things like terms, contact information and license to use the API.

### Expose your API

So how do we get your APIs to return a swagger compliant resource listing? 

Well, you can [handcode](https://github.com/wordnik/swagger-core/tree/master/samples/no-server) it. But that really isn't much fun. 

How about if your server could automatically generate it for you? That is indeed possible and is supported for a number of technologies. The people who wrote swagger specification created support for a few (in bold below). The amazing swagger community has built support for a number of server side technologies to automate generation of swagger resource listing. 

Click on 'Swaggerize your service' above for in depth tutorials on some of these technologies. A more complete listing of platforms and technolgies which work with Swagger is below:

Platform | Technology
--- | --- 
**Java** | **[JAX-RS](https://github.com/wordnik/swagger-core/tree/master/samples/java-jaxrs) ([Jersey](https://github.com/wordnik/swagger-core/tree/master/samples/java-jersey2), [Resteasy](https://github.com/wordnik/swagger-core/tree/master/samples/java-resteasy), [CXF](https://github.com/wordnik/swagger-core/tree/master/samples/java-jaxrs-cxf))**, <br>**[Play Framework](https://github.com/wordnik/swagger-core/tree/master/samples/java-play2)**<br>[Spring using swagger-springmvc](https://github.com/martypitt/swagger-springmvc) or  [swagger4spring-web](https://github.com/wkennedy/swagger4spring-web)<br>[Maven](https://github.com/kongchen/swagger-maven-plugin)<br>[Doclet](https://github.com/ryankennedy/swagger-jaxrs-doclet)
**Scala** | [Scalatra](http://www.scalatra.org/2.2/guides/swagger.html)<br>**[Play Framework](https://github.com/wordnik/swagger-core/tree/master/samples/scala-play2)**<br>[Spray](https://github.com/gettyimages/spray-swagger)
**Clojure** | [octohipster](https://github.com/myfreeweb/octohipster)<br>[ring-swagger](https://github.com/metosin/ring-swagger)
**Node.js** | **[Express](https://github.com/wordnik/swagger-node-express)**<br>[Express with swagger-jack](https://github.com/worldline/swagger-jack)<br>[hapi](https://github.com/glennjones/hapi-swagger)<br>[Standard HTTP/Express, Spec validation etc via Swagger Framework](https://github.com/silas/swagger-framework)
**ColdFusion / CFML** | [swagger-docs-cfml](https://github.com/webonix/swagger-docs-cfml)
**Go** | [go-restful](https://github.com/emicklei/go-restful)<br>[Sashay](https://bitbucket.org/seanerussell/sashay)
**.Net** | [ServiceStack](https://github.com/ServiceStack/ServiceStack)<br>[fubumvc-swagger](https://github.com/KevM/fubumvc-swagger)<br>[Swashbuckle](https://github.com/domaindrivendev/Swashbuckle)<br>[Swagger.Net](https://github.com/Swagger-Net/Swagger.Net)
**PHP** | [PHP](https://packagist.org/packages/zircote/swagger-php)<br>[Symfony](https://github.com/nelmio/NelmioApiDocBundle)<br>[Restler](https://github.com/Luracast/Restler)
**Python** | [Django](https://github.com/marcgibbons/django-rest-swagger)<br>[Django Tastypie](https://github.com/concentricsky/django-tastypie-swagger)<br>[flask-restful](https://github.com/rantav/flask-restful-swagger)
**Ruby** | [grape](https://github.com/tim-vandecasteele/grape-swagger)<br>[Rails](https://github.com/richhollis/swagger-docs)<br>[From source](https://github.com/solso/source2swagger)
**Misc** | [Alternative Swagger UI](https://github.com/stemey/gform-admin)
