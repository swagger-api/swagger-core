package io.swagger.v3.plugins.gradle.tasks;

import org.apache.commons.lang3.StringUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.SetProperty;
import org.gradle.api.tasks.*;
import org.jetbrains.annotations.Nullable;

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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CacheableTask
public class ResolveTask extends DefaultTask {

    public enum Format {JSON, YAML, JSONANDYAML}

    @Input
    @Optional
    public final Property<String> outputFileName = getProject().getObjects().property(String.class);
    @OutputDirectory
    public final DirectoryProperty outputDir = getProject().getObjects().directoryProperty();
    @InputFile
    @Optional
    @PathSensitive(PathSensitivity.RELATIVE)
    public final RegularFileProperty openApiFile = getProject().getObjects().fileProperty();
    @Input
    @Optional
    public final Property<Format> outputFormat = getProject().getObjects().property(Format.class);
    @Input
    @Optional
    public final SetProperty<String> resourcePackages = getProject().getObjects().setProperty(String.class);
    @Input
    @Optional
    public final SetProperty<String> resourceClasses = getProject().getObjects().setProperty(String.class);
    @Input
    @Optional
    public final Property<String> filterClass = getProject().getObjects().property(String.class);
    @Input
    @Optional
    public final Property<String> readerClass = getProject().getObjects().property(String.class);
    @Input
    @Optional
    public final Property<String> scannerClass = getProject().getObjects().property(String.class);
    @Input
    @Optional
    public final Property<Boolean> prettyPrint = getProject().getObjects().property(Boolean.class);
    @Input
    @Optional
    public final Property<Boolean> readAllResources = getProject().getObjects().property(Boolean.class);
    @Input
    @Optional
    public final SetProperty<String> ignoredRoutes = getProject().getObjects().setProperty(String.class);
    @Classpath
    @Optional
    public final ConfigurableFileCollection buildClasspath = getProject().getObjects().fileCollection();
    @Classpath
    public final ConfigurableFileCollection classpath = getProject().getObjects().fileCollection();
    /**
     * Completely skips execution of the task.
     *
     * @deprecated if you want to skip the task do not execute it in the first place or remove the task dependency
     * that causes it to run
     */
    @Deprecated
    @Input
    @Optional
    public final Property<Boolean> skip = getProject().getObjects().property(Boolean.class);
    @Input
    @Optional

    public final Property<String> encoding = getProject().getObjects().property(String.class);
    /**
     * @since 2.0.6
     */
    @Input
    @Optional
    public final SetProperty<String> modelConverterClasses = getProject().getObjects().setProperty(String.class);
    @Input
    @Optional
    public final Property<String> objectMapperProcessorClass = getProject().getObjects().property(String.class);
    @Input
    @Optional
    public final Property<Boolean> sortOutput = getProject().getObjects().property(Boolean.class);
    @Input
    @Optional

    public final Property<Boolean> alwaysResolveAppPath = getProject().getObjects().property(Boolean.class);
    @Input
    @Optional


    public final Property<Boolean> skipResolveAppPath = getProject().getObjects().property(Boolean.class);
    @Input
    @Optional
    public final Property<String> contextId = getProject().getObjects().property(String.class);
    @Input
    @Optional
    public final Property<Boolean> openAPI31 = getProject().getObjects().property(Boolean.class);

    /**
     * @since 2.2.12
     */
    @Input
    @Optional
    public final Property<Boolean> convertToOpenAPI31 = getProject().getObjects().property(Boolean.class);

    /**
     * @since 2.2.24
     */
    @Input
    @Optional
    public final Property<String> schemaResolution = getProject().getObjects().property(String.class);

    @Input
    @Optional
    private Property<String> openAPIVersion = getProject().getObjects().property(String.class);;

    @Input
    @Optional
    public final Property<String> defaultResponseCode = getProject().getObjects().property(String.class);

    public Property<String> getOutputFileName() {
        return outputFileName;
    }

    public RegularFileProperty getOpenApiFile() {
        return openApiFile;
    }
    public DirectoryProperty getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputPath) {
        this.outputDir.set(getProject().file(outputPath));
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName.set(outputFileName);
    }


    public void setOpenApiFile(File openApiFile) {
        this.openApiFile.set(openApiFile);
    }

    public ConfigurableFileCollection getClasspath() {
        return classpath;
    }

    public void setClasspath(Iterable<File> classpath) {
        this.classpath.setFrom(classpath);
    }

    public ConfigurableFileCollection getBuildClasspath() {
        return buildClasspath;
    }

    public void setBuildClasspath(Iterable<File> buildClasspath) {
        this.buildClasspath.setFrom(buildClasspath);
    }

    /**
     * @deprecated Use {@linkplain #outputDir} instead.
     */
    @Deprecated
    @Internal
    public Provider<String> getOutputPath() {
        return outputDir.map(dir -> dir.getAsFile().getPath());
    }

    /**
     * @deprecated Use {@linkplain #outputDir} instead.
     */
    @Deprecated
    public void setOutputPath(String outputPath) {
        this.outputDir.set(getProject().file(outputPath));
    }

    public Property<Format> getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat.set(Format.valueOf(outputFormat));
    }

    public void setOutputFormat(Format outputFormat) {
        this.outputFormat.set(outputFormat);
    }

    public SetProperty<String> getResourcePackages() {
        return resourcePackages;
    }

    public void setResourcePackages(Set<String> resourcePackages) {
        this.resourcePackages.set(resourcePackages);
    }

    public SetProperty<String> getModelConverterClasses() {
        return modelConverterClasses;
    }

    public void setModelConverterClasses(Set<String> modelConverterClasses) {
        this.modelConverterClasses.set(modelConverterClasses);
    }

    public SetProperty<String> getResourceClasses() {
        return resourceClasses;
    }

    public void setResourceClasses(Set<String> resourceClasses) {
        this.resourceClasses.set(resourceClasses);
    }

    public Property<String> getFilterClass() {
        return filterClass;
    }

    public void setFilterClass(String filterClass) {
        this.filterClass.set(filterClass);
    }

    public Property<String> getReaderClass() {
        return readerClass;
    }

    public void setReaderClass(String readerClass) {
        this.readerClass.set(readerClass);
    }

    public Property<String> getObjectMapperProcessorClass() {
        return objectMapperProcessorClass;
    }

    /**
     * @since 2.0.6
     */
    public void setObjectMapperProcessorClass(String objectMapperProcessorClass) {
        this.objectMapperProcessorClass.set(objectMapperProcessorClass);
    }

    public Property<String> getDefaultResponseCode() {
        return defaultResponseCode;
    }

    /**
     * @since 2.2.17
     */
    public void setDefaultResponseCode(String defaultResponseCode) {
        this.defaultResponseCode.set(defaultResponseCode);
    }

    public Property<String> getContextId() {
        return contextId;
    }

    /**
     * @since 2.0.6
     */
    public void setContextId(String contextId) {
        this.contextId.set(contextId);
    }

    public Property<String> getScannerClass() {
        return scannerClass;
    }

    public void setScannerClass(String scannerClass) {
        this.scannerClass.set(scannerClass);
    }

    public Property<Boolean> getPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(@Nullable String prettyPrint) {
        setPrettyPrint(prettyPrint == null ? null : Boolean.valueOf(prettyPrint));
    }

    public void setPrettyPrint(@Nullable Boolean prettyPrint) {
        this.prettyPrint.set(prettyPrint);
    }

    public Property<Boolean> getReadAllResources() {
        return readAllResources;
    }

    public void setReadAllResources(Boolean readAllResources) {
        this.readAllResources.set(readAllResources);
        ;
    }

    public void setReadAllResources(@Nullable String readAllResources) {
        setReadAllResources(readAllResources == null ? null : Boolean.valueOf(readAllResources));
    }

    public SetProperty<String> getIgnoredRoutes() {
        return ignoredRoutes;
    }

    public void setIgnoredRoutes(Collection<String> ignoredRoutes) {
        this.ignoredRoutes.set(ignoredRoutes);
    }

    public Property<Boolean> getSkip() {
        return skip;
    }

    public void setSkip(Boolean skip) {
        this.skip.set(skip);
    }

    public Property<String> getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding.set(encoding);
    }

    public Property<Boolean> getSortOutput() {
        return sortOutput;
    }

    public void setSortOutput(Boolean sortOutput) {
        this.sortOutput.set(sortOutput);
    }

    public Property<Boolean> getAlwaysResolveAppPath() {
        return alwaysResolveAppPath;
    }

    public void setAlwaysResolveAppPath(Boolean alwaysResolveAppPath) {
        this.alwaysResolveAppPath.set(alwaysResolveAppPath);
    }

    public void setAlwaysResolveAppPath(@Nullable String alwaysResolveAppPath) {
        setAlwaysResolveAppPath(alwaysResolveAppPath == null ? null : Boolean.valueOf(alwaysResolveAppPath));
    }

    public Property<Boolean> getSkipResolveAppPath() {
        return skipResolveAppPath;
    }

    /**
     * @since 2.2.15
     */
    public void setSkipResolveAppPath(Boolean skipResolveAppPath) {
        this.skipResolveAppPath.set(skipResolveAppPath);
        ;
    }

    public Property<Boolean> getOpenAPI31() {
        return openAPI31;
    }

    public void setOpenAPI31(Boolean openAPI31) {
        this.openAPI31.set(openAPI31);
    }

    public void setOpenAPI31(@Nullable String openAPI31) {
        setOpenAPI31(openAPI31 == null ? null : Boolean.valueOf(openAPI31));
    }

    public Property<Boolean> getConvertToOpenAPI31() {
        return convertToOpenAPI31;
    }

    public void setConvertToOpenAPI31(Boolean convertToOpenAPI31) {
        this.convertToOpenAPI31.set(convertToOpenAPI31);
        if (Boolean.TRUE.equals(convertToOpenAPI31)) {
            this.openAPI31.set(Boolean.TRUE);
        }
    }

    public void setConvertToOpenAPI31(@Nullable String convertToOpenAPI31) {
        setConvertToOpenAPI31(convertToOpenAPI31 == null ? null : Boolean.valueOf(convertToOpenAPI31));
    }

    public Property<String> getSchemaResolution() {
        return schemaResolution;
    }

    public void setSchemaResolution(String schemaResolution) {
        this.schemaResolution.set(schemaResolution);
    }

    public Property<String> getOpenAPIVersion() {
        return openAPIVersion;
    }

    public void setOpenAPIVersion(String openAPIVersion) {
        this.openAPIVersion.set(openAPIVersion);
    }

    @TaskAction
    public void resolve() throws GradleException {
        if (skip.getOrElse(false)) {
            getLogger().warn("You use the deprecated 'skip' property. For better performance prevent the execution instead (for example by calling the `compileJava` task instead)");
            getLogger().info("Skipping OpenAPI specification resolution");
            return;
        }
        getLogger().info("Resolving OpenAPI specification..");
        Stream<URL> classpathStream = classpath.getFiles().stream().map(f -> {
            try {
                return f.toURI().toURL();
            } catch (MalformedURLException e) {
                throw new GradleException(
                        String.format("Could not create classpath for annotations task %s.", getName()), e);
            }
        });


        Stream<URL> buildClasspathStream = buildClasspath.getFiles().stream().map(f -> {
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

        try (URLClassLoader classLoader = new URLClassLoader(urls)) {
            Class<?> swaggerLoaderClass = classLoader.loadClass("io.swagger.v3.jaxrs2.integration.SwaggerLoader");
            Object swaggerLoader = swaggerLoaderClass.newInstance();

            Method method = null;
            method = swaggerLoaderClass.getDeclaredMethod("setOutputFormat", String.class);
            method.invoke(swaggerLoader, outputFormat.get().name());

            if (openApiFile.isPresent()) {
                final File openApiFileHandle = openApiFile.get().getAsFile();
                if (openApiFileHandle.exists() && openApiFileHandle.isFile()) {
                    String openapiFileContent = new String(Files.readAllBytes(openApiFileHandle.toPath()), encoding.get());
                    if (StringUtils.isNotBlank(openapiFileContent)) {
                        method = swaggerLoaderClass.getDeclaredMethod("setOpenapiAsString", String.class);
                        method.invoke(swaggerLoader, openapiFileContent);
                    }
                }
            }

            if (resourcePackages.isPresent() && !resourcePackages.get().isEmpty()) {
                method = swaggerLoaderClass.getDeclaredMethod("setResourcePackages", String.class);
                method.invoke(swaggerLoader, resourcePackages.get().stream().map(Object::toString).collect(Collectors.joining(",")));
            }
            if (resourceClasses.isPresent() && !resourceClasses.get().isEmpty()) {
                method = swaggerLoaderClass.getDeclaredMethod("setResourceClasses", String.class);
                method.invoke(swaggerLoader, resourceClasses.get().stream().map(Object::toString).collect(Collectors.joining(",")));
            }
            if (modelConverterClasses.isPresent() && !modelConverterClasses.get().isEmpty()) {
                method = swaggerLoaderClass.getDeclaredMethod("setModelConverterClasses", String.class);
                method.invoke(swaggerLoader, modelConverterClasses.get().stream().map(Object::toString).collect(Collectors.joining(",")));
            }
            if (ignoredRoutes.isPresent() && !ignoredRoutes.get().isEmpty()) {
                method = swaggerLoaderClass.getDeclaredMethod("setIgnoredRoutes", String.class);
                method.invoke(swaggerLoader, ignoredRoutes.get().stream().map(Object::toString).collect(Collectors.joining(",")));
            }

            if (filterClass.isPresent() && StringUtils.isNotBlank(filterClass.get())) {
                method = swaggerLoaderClass.getDeclaredMethod("setFilterClass", String.class);
                method.invoke(swaggerLoader, filterClass.get());
            }

            if (readerClass.isPresent() && StringUtils.isNotBlank(readerClass.get())) {
                method = swaggerLoaderClass.getDeclaredMethod("setReaderClass", String.class);
                method.invoke(swaggerLoader, readerClass.get());
            }

            if (scannerClass.isPresent() && StringUtils.isNotBlank(scannerClass.get())) {
                method = swaggerLoaderClass.getDeclaredMethod("setScannerClass", String.class);
                method.invoke(swaggerLoader, scannerClass.get());
            }

            if (contextId.isPresent() && StringUtils.isNotBlank(contextId.get())) {
                method = swaggerLoaderClass.getDeclaredMethod("setContextId", String.class);
                method.invoke(swaggerLoader, contextId.get());
            }

            if (objectMapperProcessorClass.isPresent() && StringUtils.isNotBlank(objectMapperProcessorClass.get())) {
                method = swaggerLoaderClass.getDeclaredMethod("setObjectMapperProcessorClass", String.class);
                method.invoke(swaggerLoader, objectMapperProcessorClass.get());
            }

            if (defaultResponseCode.isPresent() && StringUtils.isNotBlank(defaultResponseCode.get())) {
                method = swaggerLoaderClass.getDeclaredMethod("setDefaultResponseCode", String.class);
                method.invoke(swaggerLoader, defaultResponseCode.get());
            }

            method = swaggerLoaderClass.getDeclaredMethod("setPrettyPrint", Boolean.class);
            method.invoke(swaggerLoader, prettyPrint.get());

            method = swaggerLoaderClass.getDeclaredMethod("setSortOutput", Boolean.class);
            method.invoke(swaggerLoader, sortOutput.get());

            method = swaggerLoaderClass.getDeclaredMethod("setAlwaysResolveAppPath", Boolean.class);
            method.invoke(swaggerLoader, alwaysResolveAppPath.get());

            method = swaggerLoaderClass.getDeclaredMethod("setSkipResolveAppPath", Boolean.class);
            method.invoke(swaggerLoader, skipResolveAppPath.get());

            method = swaggerLoaderClass.getDeclaredMethod("setReadAllResources", Boolean.class);
            method.invoke(swaggerLoader, readAllResources.get());

            if (openAPI31.isPresent() && !openAPI31.get() && convertToOpenAPI31.get()) {
                throw new GradleException("`convertToOpenAPI31` can't be enabled when `openAPI31` support is explicity disabled");
            }
            if (openAPI31.isPresent()) {
                method = swaggerLoaderClass.getDeclaredMethod("setOpenAPI31", Boolean.class);
                method.invoke(swaggerLoader, openAPI31.get());
            }

            if (convertToOpenAPI31.isPresent()) {
                method = swaggerLoaderClass.getDeclaredMethod("setConvertToOpenAPI31", Boolean.class);
                method.invoke(swaggerLoader, convertToOpenAPI31.get());
            }
            if (schemaResolution.isPresent()) {
                method = swaggerLoaderClass.getDeclaredMethod("setSchemaResolution", String.class);
                method.invoke(swaggerLoader, schemaResolution.get());
            }
            if (openAPIVersion.isPresent()) {
                method = swaggerLoaderClass.getDeclaredMethod("setOpenAPIVersion", String.class);
                method.invoke(swaggerLoader, openAPIVersion.get());
            }

            method = swaggerLoaderClass.getDeclaredMethod("resolve");
            Map<String, String> specs = (Map<String, String>) method.invoke(swaggerLoader);

            final Path outputFile = outputDir.getAsFile().get().toPath();
            if (specs.get("JSON") != null) {
                Path path = outputFile.resolve(String.format("%s.json", outputFileName.get()));
                Files.write(path, specs.get("JSON").getBytes(Charset.forName(encoding.get())));
                getLogger().debug("Saved openapi to {}", path.toAbsolutePath());
            }
            if (specs.get("YAML") != null) {
                Path path = outputFile.resolve(String.format("%s.yaml", outputFileName.get()));
                Files.write(path, specs.get("YAML").getBytes(Charset.forName(encoding.get())));
                getLogger().debug("Saved openapi to {}", path.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new GradleException("Failed to write API definition: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new GradleException(e.getMessage(), e);
        }
    }
}
