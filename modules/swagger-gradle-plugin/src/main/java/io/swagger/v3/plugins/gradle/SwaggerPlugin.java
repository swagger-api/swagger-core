package io.swagger.v3.plugins.gradle;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import io.swagger.v3.plugins.gradle.tasks.ResolveTask;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;

public class SwaggerPlugin implements Plugin<Project> {
    private static String SWAGGER_JAKARTA_REST_VERSION;

    static {
        Properties props = new Properties();
        try (InputStream is = SwaggerPlugin.class.getClassLoader().getResourceAsStream("swagger-plugin.properties")) {
            if (is != null) {
                props.load(is);
                SWAGGER_JAKARTA_REST_VERSION = props.getProperty("version", "");
            } else {
                SWAGGER_JAKARTA_REST_VERSION = "";
            }
        } catch (IOException e) {
            SWAGGER_JAKARTA_REST_VERSION = "";
        }
    }

    public void apply(Project project) {
        final Configuration config = project.getConfigurations().create("swaggerDeps")
                .setVisible(false);

        config.defaultDependencies(new Action<DependencySet>() {
            public void execute(DependencySet dependencies) {
                dependencies.add(project.getDependencies().create("org.apache.commons:commons-lang3:3.20.0"));
                dependencies.add(project.getDependencies().create("io.github.vpelikh:swagger-jakarta-rest:" + SWAGGER_JAKARTA_REST_VERSION));
                dependencies.add(project.getDependencies().create("jakarta.ws.rs:jakarta.ws.rs-api:4.0.0"));
                dependencies.add(project.getDependencies().create("jakarta.servlet:jakarta.servlet-api:6.1.0"));
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
}
