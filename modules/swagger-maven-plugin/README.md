# swagger-maven-plugin

* Resolves project openAPI specification and saves the result in JSON, YAML or both formats.
All parameters except `outputFileName`, `outputFormat`, `skip`, `encoding` and `outputPath` correspond
to `swagger` [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties) with same name.

## Jakarta namespace support

Since version 2.1.7 Swagger Core supports also Jakarta namespace, with a parallel set of artifacts with `-jakarta` suffix, providing the same functionality as the "standard" `javax` namespace ones.
Please check [Wiki](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Getting-started) for more details

Both `javax` and `jakarta` examples are provided below 

## Configuration example

### `javax` namespace

```xml
<project>
    <build>
        <plugins>
            <plugin>
                <groupId>io.swagger.core.v3</groupId>
                <artifactId>swagger-maven-plugin</artifactId>
                <version>2.2.0</version>
                <configuration>
                    <outputFileName>openapi</outputFileName>
                    <outputPath>${project.build.directory}/generatedtest</outputPath>
                    <outputFormat>JSONANDYAML</outputFormat>
                    <resourcePackages>
                        <package>test.petstore</package>
                    </resourcePackages>
                    <prettyPrint>TRUE</prettyPrint>
                </configuration>
                <executions>
                    <execution>
                        <phase>compile</phase>
                        <goals>
                            <goal>resolve</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
    <dependencies>
        <dependency>
            <groupId>io.swagger.core.v3</groupId>
            <artifactId>swagger-jaxrs2</artifactId>
            <version>2.2.0</version>
        </dependency>

        <dependency>
            <groupId>javax.ws.rs</groupId>
            <artifactId>javax.ws.rs-api</artifactId>
            <version>2.1</version>
        </dependency>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>3.1.0</version>
        </dependency>
    </dependencies>
</project>
```

### `jakarta` namespace

```xml
<project>
    <build>
        <plugins>
            <plugin>
                <groupId>io.swagger.core.v3</groupId>
                <artifactId>swagger-maven-plugin-jakarta</artifactId>
                <version>2.2.0</version>
                <configuration>
                    <outputFileName>openapi</outputFileName>
                    <outputPath>${project.build.directory}/generatedtest</outputPath>
                    <outputFormat>JSONANDYAML</outputFormat>
                    <resourcePackages>
                        <package>test.petstore</package>
                    </resourcePackages>
                    <prettyPrint>TRUE</prettyPrint>
                </configuration>
                <executions>
                    <execution>
                        <phase>compile</phase>
                        <goals>
                            <goal>resolve</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
    <dependencies>
        <dependency>
            <groupId>io.swagger.core.v3</groupId>
            <artifactId>swagger-jaxrs2-jakarta</artifactId>
            <version>2.2.0</version>
        </dependency>

        <dependency>
            <groupId>jakarta.ws.rs</groupId>
            <artifactId>jakarta.ws.rs-api</artifactId>
            <version>3.0.0</version>
        </dependency>
        <dependency>
            <groupId>jakarta.servlet</groupId>
            <artifactId>jakarta.servlet-api</artifactId>
            <version>5.0.0</version>
        </dependency>
    </dependencies>
</project>
```

## Configuration example with provided Swagger configuration file

### `javax` namespace
 
 ```xml
<project>
    <build>
        <plugins>
            <plugin>
                <groupId>io.swagger.core.v3</groupId>
                <artifactId>swagger-maven-plugin</artifactId>
                <version>2.2.0</version>
                <configuration>
                    <outputFileName>openapi</outputFileName>
                    <outputPath>${project.build.directory}/generatedtest</outputPath>
                    <configurationFilePath>${project.basedir}/src/main/resources/configurationFile.yaml</configurationFilePath>
                </configuration>
                <executions>
                    <execution>
                        <phase>compile</phase>
                        <goals>
                            <goal>resolve</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
    ...
 ``` 

### `jakarta` namespace

 ```xml
<project>
    <build>
        <plugins>
            <plugin>
                <groupId>io.swagger.core.v3</groupId>
                <artifactId>swagger-maven-plugin-jakarta</artifactId>
                <version>2.2.0</version>
                <configuration>
                    <outputFileName>openapi</outputFileName>
                    <outputPath>${project.build.directory}/generatedtest</outputPath>
                    <configurationFilePath>${project.basedir}/src/main/resources/configurationFile.yaml</configurationFilePath>
                </configuration>
                <executions>
                    <execution>
                        <phase>compile</phase>
                        <goals>
                            <goal>resolve</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
    ...
 ``` 

#### Parameters
Parameter | Description | Required | Default
--------- | ----------- | --------- | -------
`outputPath`|output path where file(s) are saved|true|
`outputFileName`|file name (no extension)|false|`openapi`
`outputFormat`|file format (`JSON`, `YAML`, `JSONANDYAML`|false|`JSON`
`skip`|if `TRUE` skip execution|false|`FALSE`
`encoding`|encoding of output file(s)|false|
`resourcePackages`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`resourceClasses`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`prettyPrint`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|`TRUE`
`sortOutput`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|`FALSE`
`alwaysResolveAppPath`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|`FALSE`
`openapiFilePath`|path to openapi file to be merged with resolved specification, see [config](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`configurationFilePath`|path to swagger config file to be merged with resolved specification, see [config](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration)|false|
`filterClass`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`readerClass`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`scannerClass`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`readAllResources`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`ignoredRoutes`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`objectMapperProcessorClass`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`modelConverterClasses`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`contextId`|see [Context](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#context)|false|${project.artifactId}

***

Since version 2.0.8, `configurationFilePath` parameter is available, allowing to specify a path to a [swagger configuration file](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration); If single maven configuration parameters (e.g. `prettyPrint`) are also defined, these will overwrite any value set in configuration file; the same applies to `openapiFilePath` which takes precedence over `openAPI` field in configuration file.

Since version 2.1.6, `sortOutput` parameter is available, allowing to sort object properties and map keys alphabetically.
Since version 2.1.6, `objectMapperProcessorClass` allows to configure also the ObjectMapper instance used to serialize the resolved OpenAPI
Since version 2.1.9, `alwaysResolveAppPath` parameter is available, allowing to trigger resolving of Application Path from annotaion also not in runtime (e.g. using servlet in separate application, or in maven plugin at build time, etc)
