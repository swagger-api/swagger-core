---
layout: default
title: beautify thy api
---

APIs should be beautiful. On the inside & the outside.

<br>

### API Structure
Swagger allows you to describe the structure of your APIs so that machines can read them. 

The ability of APIs to describe their own structure is the root of all awesomeness in Swagger. Why is it so great? Well, by reading your API's structure we can automatically build beautiful and interactive API documentation. We can also automatically generate client libraries for your API in many languages and explore other possibilities like automated testing.

Swagger does this by asking your API to return a JSON that contains a detailed description of your entire API. This JSON is essentially a resource listing of your API which adheres to [Swagger Specification](https://swagger.io/specification/). The specification asks you to include information like:

* What are all the operation that your API supports?
* What are your API's parameters and what does it return?
* Does your API need some authorization?
* Even fun things like terms, contact information and license to use the API.

### Expose your API

So how do we get your APIs to return a Swagger compliant resource listing? 

Well, you can [handcode](https://swagger.io/docs/specification/basic-structure/) it. But that really isn't much fun. 

How about if your server could automatically generate it for you? That is indeed possible and is supported for a number of technologies. The people who wrote Swagger Specification created support [for a few](https://swagger.io/tools/open-source/open-source-integrations/#swagger-group-projects) (listed under "Swagger-Group Projects"). The amazing community has also built support for other server-side technologies to automate generation of Swagger resource listing - check out [Tools and Integrations](https://swagger.io/tools/open-source/open-source-integrations/) for a complete list.
