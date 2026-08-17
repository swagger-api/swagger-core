package io.swagger.v3.plugins.gradle;

import io.swagger.v3.plugins.gradle.tasks.ResolveTask;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;

public class SwaggerPlugin implements Plugin<Project> {
    private static final String PLUGIN_PROPERTIES = "/swagger-gradle-plugin.properties";
    private static final String PLUGIN_VERSION = loadPluginVersion();

    public void apply(Project project) {
        final Configuration config = project.getConfigurations().create("swaggerDeps")
                .setVisible(false);

        config.defaultDependencies(new Action<DependencySet>() {
            public void execute(DependencySet dependencies) {
                dependencies.add(project.getDependencies().create("org.apache.commons:commons-lang3:3.20.0"));
                dependencies.add(project.getDependencies().create("io.swagger.core.v3:swagger-jaxrs2:" + PLUGIN_VERSION));
                dependencies.add(project.getDependencies().create("javax.ws.rs:javax.ws.rs-api:2.1"));
                dependencies.add(project.getDependencies().create("javax.servlet:javax.servlet-api:3.1.0"));
            }
        });
        TaskProvider<ResolveTask> lazyTask = project.getTasks().register("resolve", ResolveTask.class,task -> {
            task.buildClasspath.setFrom(config);
            task.classpath.setFrom(project.getExtensions().findByType(SourceSetContainer.class).getByName("main").getRuntimeClasspath());
            task.prettyPrint.convention(false);
            task.readAllResources.convention(true);
            task.outputFormat.convention(ResolveTask.Format.JSON);
            task.skip.convention(false);
            task.encoding.convention("UTF-8");
            task.sortOutput.convention(Boolean.FALSE);
            task.alwaysResolveAppPath.convention(Boolean.FALSE);
            task.skipResolveAppPath.convention(Boolean.FALSE);
            task.openAPI31.convention(false);
            task.convertToOpenAPI31.convention(false);
            task.outputDir.convention(project.getLayout().getBuildDirectory().dir("swagger"));
        });
    }

    private static String loadPluginVersion() {
        Properties properties = new Properties();
        try (InputStream stream = SwaggerPlugin.class.getResourceAsStream(PLUGIN_PROPERTIES)) {
            if (stream == null) {
                throw new IllegalStateException("Missing " + PLUGIN_PROPERTIES);
            }
            properties.load(stream);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load " + PLUGIN_PROPERTIES, e);
        }
        String pluginVersion = properties.getProperty("plugin.version");
        validatePluginVersion(pluginVersion);
        return pluginVersion;
    }

    static void validatePluginVersion(String pluginVersion) {
        if (pluginVersion == null || pluginVersion.trim().isEmpty()) {
            throw new IllegalStateException("Missing plugin.version in " + PLUGIN_PROPERTIES);
        }
        if (pluginVersion.trim().startsWith("${") && pluginVersion.trim().endsWith("}")) {
            throw new IllegalStateException("Unresolved plugin.version in " + PLUGIN_PROPERTIES + ": " + pluginVersion);
        }
    }
}
