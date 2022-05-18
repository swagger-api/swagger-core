package io.swagger.v3.plugins.gradle.tasks;

import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Classpath;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CacheableTask
public class ResolveTask extends DefaultTask {

    private static Logger LOGGER = Logging.getLogger(ResolveTask.class);

    public enum Format {JSON, YAML, JSONANDYAML};

    private String outputFileName = "openapi";

    private String outputPath;
    private File outputDir;

    private File openApiFile;

    private Format outputFormat = Format.JSON;

    private Set<String> resourcePackages;
    private Set<String> resourceClasses;
    private String filterClass;
    private String readerClass;
    private String scannerClass;
    private Boolean prettyPrint = false;
    private Boolean readAllResources = Boolean.TRUE;
    private Collection<String> ignoredRoutes;
    private Iterable<File> buildClasspath;
    private Iterable<File> classpath;

    private Boolean skip = Boolean.FALSE;

    private String encoding = "UTF-8";

    private LinkedHashSet<String> modelConverterClasses;
    private String objectMapperProcessorClass;

    private Boolean sortOutput = Boolean.FALSE;
    private Boolean alwaysResolveAppPath = Boolean.FALSE;


    private String contextId;

    private Boolean openAPI31 = false;

    @Input
    @Optional
    public String getOutputFileName() {
        return outputFileName;
    }

    @InputFile
    @Optional
    @PathSensitive(PathSensitivity.RELATIVE)
    public File getOpenApiFile() {
        return openApiFile;
    }

    public void setOpenApiFile(File openApiFile) {
        this.openApiFile = openApiFile;
    }

    @Classpath
    public Iterable<File> getClasspath() {
        return classpath;
    }

    public void setClasspath(Iterable<File> classpath) {
        this.classpath = classpath;
    }

    @Classpath
    @Optional
    public Iterable<File> getBuildClasspath() {
        return buildClasspath;
    }

    public void setBuildClasspath(Iterable<File> buildClasspath) {
        this.buildClasspath = buildClasspath;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    /**
     * @deprecated Use {@linkplain #getOutputDir()} instead.
     */
    @Deprecated
    @Input
    @Optional
    public String getOutputPath() {
        return outputPath;
    }

    /**
     * @deprecated Use {@linkplain #setOutputDir(File)} instead.
     */
    @Deprecated
    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
        outputDir = new File(outputPath);
    }

    @OutputDirectory
    public File getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    @Input
    @Optional
    public Format getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(Format outputFormat) {
        this.outputFormat = outputFormat;
    }

    @Input
    @Optional
    public Set<String> getResourcePackages() {
        return resourcePackages;
    }

    public void setResourcePackages(Set<String> resourcePackages) {
        this.resourcePackages = resourcePackages;
    }

    /**
     * @since 2.0.6
     */
    @Input
    @Optional
    public LinkedHashSet<String> getModelConverterClasses() {
        return modelConverterClasses;
    }

    /**
     * @since 2.0.6
     */
    public void setModelConverterClasses(LinkedHashSet<String> modelConverterClasses) {
        this.modelConverterClasses = modelConverterClasses;
    }

    @Input
    @Optional
    public Set<String> getResourceClasses() {
        return resourceClasses;
    }

    public void setResourceClasses(Set<String> resourceClasses) {
        this.resourceClasses = resourceClasses;
    }

    @Input
    @Optional
    public String getFilterClass() {
        return filterClass;
    }

    public void setFilterClass(String filterClass) {
        this.filterClass = filterClass;
    }

    @Input
    @Optional
    public String getReaderClass() {
        return readerClass;
    }

    public void setReaderClass(String readerClass) {
        this.readerClass = readerClass;
    }

    /**
     * @since 2.0.6
     */
    @Input
    @Optional
    public String getObjectMapperProcessorClass() {
        return objectMapperProcessorClass;
    }

    /**
     * @since 2.0.6
     */
    public void setObjectMapperProcessorClass(String objectMapperProcessorClass) {
        this.objectMapperProcessorClass = objectMapperProcessorClass;
    }

    /**
     * @since 2.0.6
     */
    @Input
    @Optional
    public String getContextId() {
        return contextId;
    }

    /**
     * @since 2.0.6
     */
    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    @Input
    @Optional
    public String getScannerClass() {
        return scannerClass;
    }

    public void setScannerClass(String scannerClass) {
        this.scannerClass = scannerClass;
    }

    @Input
    @Optional
    public Boolean getPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(Boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    @Input
    @Optional
    public Boolean getReadAllResources() {
        return readAllResources;
    }

    public void setReadAllResources(Boolean readAllResources) {
        this.readAllResources = readAllResources;
    }

    @Input
    @Optional
    public Collection<String> getIgnoredRoutes() {
        return ignoredRoutes;
    }

    public void setIgnoredRoutes(Collection<String> ignoredRoutes) {
        this.ignoredRoutes = ignoredRoutes;
    }

    @Input
    @Optional
    public Boolean getSkip() {
        return skip;
    }

    public void setSkip(Boolean skip) {
        this.skip = skip;
    }

    @Input
    @Optional
    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String resourceClasses) {
        this.encoding = encoding;
    }

    @Input
    @Optional
    public Boolean getSortOutput() {
        return sortOutput;
    }

    public void setSortOutput(Boolean sortOutput) {
        this.sortOutput = sortOutput;
    }

    @Input
    @Optional
    public Boolean getAlwaysResolveAppPath() {
        return alwaysResolveAppPath;
    }

    public void setAlwaysResolveAppPath(Boolean alwaysResolveAppPath) {
        this.alwaysResolveAppPath = alwaysResolveAppPath;
    }

    /**
     * @since 2.2.0
     */
    @Input
    @Optional
    public Boolean getOpenAPI31() {
        return openAPI31;
    }

    public void setOpenAPI31(Boolean openAPI31) {
        this.openAPI31 = openAPI31;
    }

    @TaskAction
    public void resolve() throws GradleException {
        if (skip) {
            LOGGER.info( "Skipping OpenAPI specification resolution" );
            return;
        }
        LOGGER.info( "Resolving OpenAPI specification.." );

        Stream<URL> classpathStream = StreamSupport.stream(getClasspath().spliterator(), false).map(f -> {
            try {
                return f.toURI().toURL();
            } catch (MalformedURLException e) {
                throw new GradleException(
                    String.format("Could not create classpath for annotations task %s.", getName()), e);
            }
        });

        Stream<URL> buildClasspathStream = StreamSupport.stream(getBuildClasspath().spliterator(), false).map(f -> {
            try {
                return f.toURI().toURL();
            } catch (MalformedURLException e) {
                throw new GradleException(
                    String.format("Could not create classpath for annotations task %s.", getName()), e);
            }
        });

        URL[] urls = Stream.concat(classpathStream, buildClasspathStream)
            .distinct()
            .toArray(URL[]::new);

        //ClassLoader classLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]), Thread.currentThread().getContextClassLoader());
        ClassLoader classLoader = new URLClassLoader(urls);

        try {
            Class swaggerLoaderClass = classLoader.loadClass("io.swagger.v3.jaxrs2.integration.SwaggerLoader");
            Object swaggerLoader = swaggerLoaderClass.newInstance();

            Method method =  null;
            method=swaggerLoaderClass.getDeclaredMethod("setOutputFormat",String.class);
            method.invoke(swaggerLoader, outputFormat.name());

            if (openApiFile != null) {
                if (openApiFile.exists() && openApiFile.isFile()) {
                    String openapiFileContent = new String(Files.readAllBytes(openApiFile.toPath()), encoding);
                    if (StringUtils.isNotBlank(openapiFileContent)) {
                        method=swaggerLoaderClass.getDeclaredMethod("setOpenapiAsString",String.class);
                        method.invoke(swaggerLoader, openapiFileContent);
                    }
                }
            }

            if (resourcePackages != null && !resourcePackages.isEmpty()) {
                method=swaggerLoaderClass.getDeclaredMethod("setResourcePackages",String.class);
                method.invoke(swaggerLoader, resourcePackages.stream().map(Object::toString).collect(Collectors.joining(",")));
            }
            if (resourceClasses != null && !resourceClasses.isEmpty()) {
                method=swaggerLoaderClass.getDeclaredMethod("setResourceClasses",String.class);
                method.invoke(swaggerLoader, resourceClasses.stream().map(Object::toString).collect(Collectors.joining(",")));
            }
            if (modelConverterClasses != null && !modelConverterClasses.isEmpty()) {
                method=swaggerLoaderClass.getDeclaredMethod("setModelConverterClasses",String.class);
                method.invoke(swaggerLoader, modelConverterClasses.stream().map(Object::toString).collect(Collectors.joining(",")));
            }
            if (ignoredRoutes != null && !ignoredRoutes.isEmpty()) {
                method=swaggerLoaderClass.getDeclaredMethod("setIgnoredRoutes",String.class);
                method.invoke(swaggerLoader, ignoredRoutes.stream().map(Object::toString).collect(Collectors.joining(",")));
            }

            if (StringUtils.isNotBlank(filterClass)) {
                method=swaggerLoaderClass.getDeclaredMethod("setFilterClass",String.class);
                method.invoke(swaggerLoader, filterClass);
            }

            if (StringUtils.isNotBlank(readerClass)) {
                method=swaggerLoaderClass.getDeclaredMethod("setReaderClass",String.class);
                method.invoke(swaggerLoader, readerClass);
            }

            if (StringUtils.isNotBlank(scannerClass)) {
                method=swaggerLoaderClass.getDeclaredMethod("setScannerClass",String.class);
                method.invoke(swaggerLoader, scannerClass);
            }

            if (StringUtils.isNotBlank(contextId)) {
                method=swaggerLoaderClass.getDeclaredMethod("setContextId",String.class);
                method.invoke(swaggerLoader, contextId);
            }

            if (StringUtils.isNotBlank(objectMapperProcessorClass)) {
                method=swaggerLoaderClass.getDeclaredMethod("setObjectMapperProcessorClass",String.class);
                method.invoke(swaggerLoader, objectMapperProcessorClass);
            }

            method=swaggerLoaderClass.getDeclaredMethod("setPrettyPrint", Boolean.class);
            method.invoke(swaggerLoader, prettyPrint);

            method=swaggerLoaderClass.getDeclaredMethod("setSortOutput", Boolean.class);
            method.invoke(swaggerLoader, sortOutput);

            method=swaggerLoaderClass.getDeclaredMethod("setAlwaysResolveAppPath", Boolean.class);
            method.invoke(swaggerLoader, alwaysResolveAppPath);

            method=swaggerLoaderClass.getDeclaredMethod("setReadAllResources", Boolean.class);
            method.invoke(swaggerLoader, readAllResources);

            method=swaggerLoaderClass.getDeclaredMethod("setOpenAPI31", Boolean.class);
            method.invoke(swaggerLoader, openAPI31);

            method=swaggerLoaderClass.getDeclaredMethod("resolve");
            Map<String, String> specs = (Map<String, String>)method.invoke(swaggerLoader);

            if (specs.get("JSON") != null) {
                Path path = outputDir.toPath().resolve(String.format("%s.json", outputFileName));
                Files.write(path, specs.get("JSON").getBytes(Charset.forName(encoding)));
            }
            if (specs.get("YAML") != null) {
                Path path = outputDir.toPath().resolve(String.format("%s.yaml", outputFileName));
                Files.write(path, specs.get("YAML").getBytes(Charset.forName(encoding)));
            }
        } catch (IOException e) {
            throw new GradleException("Failed to write API definition: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new GradleException(e.getMessage(), e);
        }
    }
}
