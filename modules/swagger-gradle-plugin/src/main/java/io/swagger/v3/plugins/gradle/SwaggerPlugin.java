package io.swagger.v3.plugins.gradle;

import io.swagger.v3.plugins.gradle.tasks.ResolveTask;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.DependencySet;

public class SwaggerPlugin implements Plugin<Project> {
    public void apply(Project project) {
        final Configuration config = project.getConfigurations().create("swaggerDeps")
                .setVisible(false);

        config.defaultDependencies(new Action<DependencySet>() {
            public void execute(DependencySet dependencies) {
                dependencies.add(project.getDependencies().create("org.apache.commons:commons-lang3:3.7"));
                dependencies.add(project.getDependencies().create("io.swagger.core.v3:swagger-jaxrs2:2.1.3-SNAPSHOT"));
                dependencies.add(project.getDependencies().create("javax.ws.rs:javax.ws.rs-api:2.1"));
                dependencies.add(project.getDependencies().create("javax.servlet:javax.servlet-api:3.1.0"));
            }
        });
        Task task = project.getTasks().create("resolve", ResolveTask.class);
        ((ResolveTask)task).setBuildClasspath(config);

        try {
            if (project.getTasks().findByPath("classes") != null) {
                task.dependsOn("classes");
            }
            if (project.getTasks().findByPath("compileJava") != null) {
                task.dependsOn("compileJava");
            }
            if (project.getTasks().findByPath("compileTestJava") != null) {
                task.dependsOn("compileTestJava");
            }
            if (project.getTasks().findByPath("testClasses") != null) {
                task.dependsOn("testClasses");
            }
        } catch (Exception e) {
            project.getLogger().warn("Exception in task dependencies: " + e.getMessage(), e);
        }
    }
}
