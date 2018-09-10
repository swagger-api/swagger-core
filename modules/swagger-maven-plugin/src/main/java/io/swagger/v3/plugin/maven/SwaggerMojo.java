package io.swagger.v3.plugin.maven;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Set;

@Mojo(
    name = "resolve",
    requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME,
    defaultPhase = LifecyclePhase.COMPILE,
    threadSafe = true,
    configurator = "include-project-dependencies"
)
public class SwaggerMojo extends AbstractMojo {

    public enum Format {JSON, YAML, JSONANDYAML};

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if (skip) {
            getLog().info( "Skipping OpenAPI specification resolution" );
            return;
        }
        getLog().info( "Resolving OpenAPI specification.." );
        SwaggerConfiguration config = new SwaggerConfiguration()
                .filterClass(filterClass)
                .ignoredRoutes(ignoredRoutes)
                .prettyPrint(prettyPrint)
                .readAllResources(readAllResources)
                .readerClass(readerClass)
                .scannerClass(scannerClass)
                .resourceClasses(resourceClasses)
                .resourcePackages(resourcePackages);
        try {
            OpenAPI openAPI = new JaxrsOpenApiContextBuilder()
                    .openApiConfiguration(config)
                    .buildContext(true)
                    .read();
            String openapiJson = null;
            String openapiYaml = null;
            if (Format.JSON.equals(outputFormat) || Format.JSONANDYAML.equals(outputFormat)) {
                if (prettyPrint) {
                    openapiJson = Json.pretty(openAPI);
                } else {
                    openapiJson = Json.mapper().writeValueAsString(openAPI);
                }

            }
            if (Format.YAML.equals(outputFormat) || Format.JSONANDYAML.equals(outputFormat)) {
                if (prettyPrint) {
                    openapiYaml = Yaml.pretty(openAPI);
                } else {
                    openapiYaml = Yaml.mapper().writeValueAsString(openAPI);
                }

            }
            Path path = Paths.get(outputPath, "temp");
            final File parentFile = path.toFile().getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }

            if (openapiJson != null) {
                path = Paths.get(outputPath, outputFileName + ".json");
                Files.write(path, openapiJson.getBytes(Charset.forName("UTF-8")));
            }
            if (openapiYaml != null) {
                path = Paths.get(outputPath, outputFileName + ".yaml");
                Files.write(path, openapiYaml.getBytes(Charset.forName("UTF-8")));
            }

        } catch (OpenApiConfigurationException e) {
            getLog().error( "Error resolving API specification" , e);
            throw new MojoFailureException(e.getMessage(), e);
        } catch (IOException e) {
            getLog().error( "Error writing API specification" , e);
            throw new MojoExecutionException("Failed to write API definition", e);
        } catch (Exception e) {
            getLog().error( "Error resolving API specification" , e);
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    @Parameter( property = "resolve.outputFileName", defaultValue = "openapi")
    private String outputFileName = "openapi";

    @Parameter( property = "resolve.outputPath" )
    private String outputPath;

    @Parameter( property = "resolve.outputFormat", defaultValue = "JSON")
    private Format outputFormat;

    @Parameter( property = "resolve.resourcePackages" )
    private Set<String> resourcePackages;
    @Parameter( property = "resolve.resourceClasses" )
    private Set<String> resourceClasses;
    @Parameter( property = "resolve.filterClass" )
    private String filterClass;
    @Parameter( property = "resolve.readerClass" )
    private String readerClass;
    @Parameter( property = "resolve.scannerClass" )
    private String scannerClass;
    @Parameter( property = "resolve.prettyPrint" )
    private Boolean prettyPrint;
    @Parameter( property = "resolve.readAllResources" )
    private Boolean readAllResources = Boolean.TRUE;
    @Parameter( property = "resolve.ignoredRoutes" )
    private Collection<String> ignoredRoutes;

    @Parameter( property = "resolve.skip" )
    private Boolean skip = Boolean.FALSE;

    public String getOutputPath() {
        return outputPath;
    }

}
