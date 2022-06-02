package io.swagger.v3.plugin.maven;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import io.swagger.v3.core.filter.OpenAPISpecFilter;
import io.swagger.v3.core.filter.SpecFilter;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.GenericOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;

import static java.lang.String.format;

@Mojo(
    name = "resolve",
    requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME,
    defaultPhase = LifecyclePhase.COMPILE,
    threadSafe = true,
    configurator = "include-project-dependencies"
)
public class SwaggerMojo extends AbstractMojo {

    public enum Format {JSON, YAML, JSONANDYAML}

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().info( "Skipping OpenAPI specification resolution" );
            return;
        }
        getLog().info( "Resolving OpenAPI specification.." );

        if (project != null) {
            String pEnc = project.getProperties().getProperty("project.build.sourceEncoding");
            if (StringUtils.isNotBlank(pEnc)) {
                projectEncoding = pEnc;
            }
        }
        if (StringUtils.isBlank(encoding)) {
            encoding = projectEncoding;
        }

        // read swagger configuration if one was provided
        Optional<SwaggerConfiguration> swaggerConfiguration =
                readStructuredDataFromFile(configurationFilePath, SwaggerConfiguration.class, "configurationFilePath");

        // read openApi config, if one was provided
        Optional<OpenAPI> openAPIInput =
                readStructuredDataFromFile(openapiFilePath, OpenAPI.class, "openapiFilePath");

        config = mergeConfig(openAPIInput.orElse(null), swaggerConfiguration.orElse(new SwaggerConfiguration()));

        setDefaultsIfMissing(config);

        try {
            GenericOpenApiContextBuilder builder = new JaxrsOpenApiContextBuilder()
                    .openApiConfiguration(config);
            if (StringUtils.isNotBlank(contextId)) {
                builder.ctxId(contextId);
            }
            OpenApiContext context = builder.buildContext(true);
            OpenAPI openAPI = context.read();

            if (StringUtils.isNotBlank(config.getFilterClass())) {
                try {
                    OpenAPISpecFilter filterImpl = (OpenAPISpecFilter) this.getClass().getClassLoader().loadClass(config.getFilterClass()).newInstance();
                    SpecFilter f = new SpecFilter();
                    openAPI = f.filter(openAPI, filterImpl, new HashMap<>(), new HashMap<>(),
                            new HashMap<>());
                } catch (Exception e) {
                    getLog().error("Error applying filter to API specification", e);
                    throw new MojoExecutionException("Error applying filter to API specification: " + e.getMessage(), e);
                }
            }

            String openapiJson = null;
            String openapiYaml = null;
            if (Format.JSON.equals(outputFormat) || Format.JSONANDYAML.equals(outputFormat)) {
                if (config.isPrettyPrint() != null && config.isPrettyPrint()) {
                    openapiJson = context.getOutputJsonMapper().writer(new DefaultPrettyPrinter()).writeValueAsString(openAPI);
                } else {
                    openapiJson = context.getOutputJsonMapper().writeValueAsString(openAPI);
                }
            }
            if (Format.YAML.equals(outputFormat) || Format.JSONANDYAML.equals(outputFormat)) {
                if (config.isPrettyPrint() != null && config.isPrettyPrint()) {
                    openapiYaml = context.getOutputYamlMapper().writer(new DefaultPrettyPrinter()).writeValueAsString(openAPI);
                } else {
                    openapiYaml = context.getOutputYamlMapper().writeValueAsString(openAPI);
                }
            }
            Path path = Paths.get(outputPath, "temp");
            final File parentFile = path.toFile().getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }

            if (openapiJson != null) {
                path = Paths.get(outputPath, outputFileName + ".json");
                Files.write(path, openapiJson.getBytes(Charset.forName(encoding)));
                getLog().info( "JSON output: " + path.toFile().getCanonicalPath());
            }
            if (openapiYaml != null) {
                path = Paths.get(outputPath, outputFileName + ".yaml");
                Files.write(path, openapiYaml.getBytes(Charset.forName(encoding)));
                getLog().info( "YAML output: " + path.toFile().getCanonicalPath());
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

    private void setDefaultsIfMissing(SwaggerConfiguration config) {

        if (prettyPrint == null) {
            prettyPrint = Boolean.FALSE;
        }
        if (readAllResources == null) {
            readAllResources = Boolean.TRUE;
        }
        if (sortOutput == null) {
            sortOutput = Boolean.FALSE;
        }
        if (alwaysResolveAppPath == null) {
            alwaysResolveAppPath = Boolean.FALSE;
        }
        if (openapi31 == null) {
            openapi31 = Boolean.FALSE;
        }
        if (config.isPrettyPrint() == null) {
            config.prettyPrint(prettyPrint);
        }
        if (config.isReadAllResources() == null) {
            config.readAllResources(readAllResources);
        }
        if (config.isSortOutput() == null) {
            config.sortOutput(sortOutput);
        }
        if (config.isAlwaysResolveAppPath() == null) {
            config.alwaysResolveAppPath(alwaysResolveAppPath);
        }
        if (config.isOpenAPI31() == null) {
            config.setOpenAPI31(openapi31);
        }
    }

    /**
     * Read the content of given file as either json or yaml and maps it to given class
     *
     * @param filePath    to read content from
     * @param outputClass to map to
     * @param configName  for logging, what user config will be read
     * @param <T>         mapped type
     * @return empty optional if not path was given or the file was empty, read instance otherwis
     * @throws MojoFailureException if given path is not file, could not be read or is not proper json or yaml
     */
    private <T> Optional<T> readStructuredDataFromFile(String filePath, Class<T> outputClass, String configName)
            throws MojoFailureException {
        try {
            // ignore if config is not provided
            if (StringUtils.isBlank(filePath)) {
                return Optional.empty();
            }

            Path pathObj = Paths.get(filePath);

            // if file does not exist or is not an actual file, finish with error
            if (!pathObj.toFile().exists() || !pathObj.toFile().isFile()) {
                throw new IllegalArgumentException(
                        format("passed path does not exist or is not a file: '%s'", filePath));
            }

            String fileContent = new String(Files.readAllBytes(pathObj), encoding);

            // if provided file is empty, log warning and finish
            if (StringUtils.isBlank(fileContent)) {
                getLog().warn(format("It seems that file '%s' defined in config %s is empty",
                        pathObj.toString(), configName));
                return Optional.empty();
            }

            // get mappers in the order based on file extension
            List<BiFunction<String, Class<T>, T>> mappers = getSortedMappers(pathObj);

            T instance = null;
            Throwable caughtEx = null;

            // iterate through mappers and see if one is able to parse
            for (BiFunction<String, Class<T>, T> mapper : mappers) {
                try {
                    instance = mapper.apply(fileContent, outputClass);
                    break;
                } catch (Exception e) {
                    caughtEx = e;
                }
            }

            // if no mapper could read the content correctly, finish with error
            if (instance == null) {
                if (caughtEx == null) {
                    caughtEx = new IllegalStateException("undefined state");
                }
                getLog().error(format("Could not read file '%s' for config %s", pathObj.toString(), configName), caughtEx);
                throw new IllegalStateException(caughtEx.getMessage(), caughtEx);
            }

            return Optional.of(instance);
        } catch (Exception e) {
            getLog().error(format("Error reading/deserializing config %s file", configName), e);
            throw new MojoFailureException(e.getMessage(), e);
        }
    }

    /**
     * Get sorted list of mappers based on given filename.
     * <p>
     * Will sort the 2 supported mappers: json and yaml based on what file extension is used.
     *
     * @param pathObj to get extension from.
     * @param <T>     mapped type
     * @return list of mappers
     */
    private <T> List<BiFunction<String, Class<T>, T>> getSortedMappers(Path pathObj) {
        String ext = FileUtils.extension(pathObj.toString());
        boolean yamlPreferred = false;
        if (ext.equalsIgnoreCase("yaml") || ext.equalsIgnoreCase("yml")) {
            yamlPreferred = true;
        }

        List<BiFunction<String, Class<T>, T>> list = new ArrayList<>(2);

        list.add((content, typeClass) -> {
            try {
                return Json.mapper().readValue(content, typeClass);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        });
        list.add((content, typeClass) -> {
            try {
                return Yaml.mapper().readValue(content, typeClass);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        });

        if (yamlPreferred) {
            Collections.reverse(list);
        }

        return Collections.unmodifiableList(list);
    }

    private SwaggerConfiguration mergeConfig(OpenAPI openAPIInput, SwaggerConfiguration config) {
        // overwrite all settings provided by other maven config
        if (StringUtils.isNotBlank(filterClass)) {
            config.filterClass(filterClass);
        }
        if (isCollectionNotBlank(ignoredRoutes)) {
            config.ignoredRoutes(ignoredRoutes);
        }
        if (prettyPrint != null) {
            config.prettyPrint(prettyPrint);
        }
        if (sortOutput != null) {
            config.sortOutput(sortOutput);
        }
        if (alwaysResolveAppPath != null) {
            config.alwaysResolveAppPath(alwaysResolveAppPath);
        }
        if (readAllResources != null) {
            config.readAllResources(readAllResources);
        }
        if (StringUtils.isNotBlank(readerClass)) {
            config.readerClass(readerClass);
        }
        if (StringUtils.isNotBlank(scannerClass)) {
            config.scannerClass(scannerClass);
        }
        if (isCollectionNotBlank(resourceClasses)) {
            config.resourceClasses(resourceClasses);
        }
        if (openAPIInput != null) {
            config.openAPI(openAPIInput);
        }
        if (isCollectionNotBlank(resourcePackages)) {
            config.resourcePackages(resourcePackages);
        }
        if (StringUtils.isNotBlank(objectMapperProcessorClass)) {
            config.objectMapperProcessorClass(objectMapperProcessorClass);
        }
        if (isCollectionNotBlank(modelConverterClasses)) {
            config.modelConverterClasses(modelConverterClasses);
        }

        return config;
    }

    private boolean isCollectionNotBlank(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    @Parameter( property = "resolve.outputFileName", defaultValue = "openapi")
    private String outputFileName = "openapi";

    @Parameter( property = "resolve.outputPath" )
    private String outputPath;

    @Parameter( property = "resolve.outputFormat", defaultValue = "JSON")
    private Format outputFormat = Format.JSON;

    @Parameter( property = "resolve.resourcePackages" )
    private Set<String> resourcePackages;
    @Parameter( property = "resolve.resourceClasses" )
    private Set<String> resourceClasses;
    @Parameter( property = "resolve.modelConverterClasses" )
    private LinkedHashSet<String> modelConverterClasses;
    @Parameter( property = "resolve.filterClass" )
    private String filterClass;
    @Parameter( property = "resolve.readerClass" )
    private String readerClass;
    @Parameter( property = "resolve.scannerClass" )
    private String scannerClass;
    /**
     * @since 2.0.6
     */
    @Parameter( property = "resolve.objectMapperProcessorClass" )
    private String objectMapperProcessorClass;
    @Parameter(property = "resolve.prettyPrint")
    private Boolean prettyPrint;
    @Parameter(property = "resolve.readAllResources")
    private Boolean readAllResources;
    @Parameter( property = "resolve.ignoredRoutes" )
    private Collection<String> ignoredRoutes;
    /**
     * @since 2.0.6
     */
    @Parameter(property = "resolve.contextId", defaultValue = "${project.artifactId}")
    private String contextId;

    @Parameter( property = "resolve.skip" )
    private Boolean skip = Boolean.FALSE;

    @Parameter( property = "resolve.openapiFilePath")
    private String openapiFilePath;

    /**
     * @since 2.0.8
     */
    @Parameter(property = "resolve.configurationFilePath")
    private String configurationFilePath;

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Parameter( property = "resolve.encoding" )
    private String encoding;

    /**
     * @since 2.1.6
     */
    @Parameter(property = "resolve.sortOutput")
    private Boolean sortOutput;

    /**
     * @since 2.1.9
     */
    @Parameter(property = "resolve.alwaysResolveAppPath")
    private Boolean alwaysResolveAppPath;

    /**
     * @since 2.2.0
     */
    @Parameter(property = "resolve.openapi31")
    private Boolean openapi31;


    private String projectEncoding = "UTF-8";
    private SwaggerConfiguration config;

    public String getOutputPath() {
        return outputPath;
    }

    public String getOpenapiFilePath() {
        return openapiFilePath;
    }

    String getConfigurationFilePath() {
        return configurationFilePath;
    }

    void setContextId(String contextId) {
        this.contextId = contextId;
    }

    SwaggerConfiguration getInternalConfiguration() {
        return config;
    }
}
