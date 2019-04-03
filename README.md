[Since I could not get any feedback, this is no longer supported]
This is an extended version for Swagger Core with the addition of many features for those who would like to directly build the OpenAPI object instead of letting Swagger handle things automatically. Some options should also be valid for those using the automatic way.

By directly manipulating the OpenAPI object and this version, you have lots of customization that the standard versions can only dream of. For example:

1. You can add addition models to your OpenAPI with OpenAPIBuilderToolbox.add(api, SomeClass.class). 

2. If you only care about the documentation, not code generation and sometimes you want the Schema to have the fullname of the class (including package name), you can just set OpenAPIBuilderOptions.USE_FULLNAME to true.

3. For generic class like ClassA<T>, the generated API is sometimes ugly lile ClassAObject, and if you want to ignore that generic part to have only ClassA, you can just set OpenAPIBuilderOptions.OMIT_GENERIC to true.
 
4. For Enum, sometimes the default toString() and name() may differ, so if you want to ensure that name() is always used, you can just set OpenAPIBuilderOptions.USE_ENUMNAME to true.

5. For Enum, sometimes you have only 1 enum, but you have to use it lots of times, isn't it much better if we can declare it as a Schema and just put a reference for it instead of inlining all the time? You can achieve that easily by setting the OpenAPIBuilderOptions.RECYCLE_ENUM to true.

6. You know, you can specify parent with Schema(allOf = {...}), but then in the generated OpenAPI, all the properties from parent will be repeated again, which is a bit annoying. Therefore, how about we exclude all of those which have been declared in parent schema? Just set OpenAPIBuilderOptions.HIDE_PARENTS to true.

7. Last, but not least, sometimes, you do not want to use the default visibility, right? Normally, you want to also see the private fields, ignore setter and getter, but how can we do it in the old version? Use annotation? Oh my god, imagine that I have a bit more than 1000 classes in my project and you seriously expect me to put the damn annotations on all of them? Fat chance :V !!! Here, I expose the default object mapper in ObjectMapperFactory.defaultMapper and you can use it to control a lot more than you think.

Bonus: If you want to build the OpenAPI object directly, you can use Reflections to achieve much better results compared to letting Swagger do everything on its own.

Lam Gia Thuan

A super handsome programmer who feels frustrated with Swagger Core's lack of functionalities :) 
