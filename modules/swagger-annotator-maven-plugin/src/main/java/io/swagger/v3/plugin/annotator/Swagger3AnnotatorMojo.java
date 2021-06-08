package io.swagger.v3.plugin.annotator;

import io.swagger.v3.plugin.annotator.model.AnnotatorConfig;
import io.swagger.v3.plugin.annotator.model.JavadocMapping;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.ElementType;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;

@Mojo(name = "swagger3-annotator",
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME,
        defaultPhase = LifecyclePhase.COMPILE,
        threadSafe = true)
public class Swagger3AnnotatorMojo extends AnnotatorMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (CollectionUtils.isEmpty(super.configs)) {
            log.warn("you have not configured it, it will be configured for you automatically...");
            super.configs = new LinkedHashSet<>();
            //打在类上的注解
            Map<String, String> apiMapping = new HashMap<>();
            apiMapping.put("tags", "description");
            Map<String, Object> apiDefaultValues = new HashMap<>();
            apiDefaultValues.put("protocols", "http,https");
            super.configs.add(AnnotatorConfig.builder()
                    .annotateType(ElementType.TYPE)
                    .javadocMappings(new HashSet<>(Collections.singletonList(JavadocMapping.builder()
                            .conditions(new HashSet<>(Collections.singletonList("org.springframework.stereotype.Controller")))
                            .annotationClassName("io.swagger.annotations.Api")
                            .mapping(apiMapping)
                            .defaultValues(apiDefaultValues)
                            .build())))
                    .build());

            //打在方法上的注解
            Map<String, String> operationMapping = new HashMap<>();
            operationMapping.put("summary", "description");
            super.configs.add(AnnotatorConfig.builder()
                    .annotateType(ElementType.METHOD)
                    .javadocMappings(new HashSet<>(Collections.singletonList(JavadocMapping.builder()
                            .conditions(new HashSet<>(Collections.singletonList("org.springframework.web.bind.annotation.RequestMapping")))
                            .annotationClassName("io.swagger.v3.oas.annotations.Operation")
                            .mapping(operationMapping)
                            .build())))
                    .build());
            Map<String, String> apiResponseMapping = new HashMap<>();
            apiResponseMapping.put("description", "@return");
            super.configs.add(AnnotatorConfig.builder()
                    .annotateType(ElementType.METHOD)
                    .javadocMappings(new HashSet<>(Collections.singletonList(JavadocMapping.builder()
                            .conditions(new HashSet<>(Collections.singletonList("org.springframework.web.bind.annotation.RequestMapping")))
                            .annotationClassName("io.swagger.v3.oas.annotations.responses.ApiResponse")
                            .mapping(apiResponseMapping)
                            .build())))
                    .build());

            //打在参数上的注解
            Map<String, String> apiParam = new HashMap<>();
            apiParam.put("name", "paramName");
            apiParam.put("description", "description");
            super.configs.add(AnnotatorConfig.builder()
                    .annotateType(ElementType.PARAMETER)
                    .javadocMappings(new HashSet<>(Collections.singletonList(JavadocMapping.builder()
                            .conditions(new HashSet<>(Collections.singletonList("org.springframework.web.bind.annotation.RequestParam")))
                            .annotationClassName("io.swagger.v3.oas.annotations.Parameter")
                            .mapping(apiParam)
                            .build())))
                    .build());
            log.warn("automatic configuration is complete !");
        }
        super.execute();
    }
}
