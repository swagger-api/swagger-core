package io.swagger.v3.plugin.annotator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.swagger.v3.plugin.annotator.annotator.Annotator;
import io.swagger.v3.plugin.annotator.annotator.impl.ClassAnnotator;
import io.swagger.v3.plugin.annotator.annotator.impl.FieldAnnotator;
import io.swagger.v3.plugin.annotator.annotator.impl.MethodAnnotator;
import io.swagger.v3.plugin.annotator.annotator.impl.ParameterAnnotator;
import io.swagger.v3.plugin.annotator.model.AnnotatorConfig;
import io.swagger.v3.plugin.annotator.model.JavadocMapping;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.lang.annotation.ElementType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotatorMojo extends AbstractMojo {

    protected final Log log;

    @Parameter(defaultValue = "${project}", readonly = true)
    protected MavenProject project;

    /**
     * What annotations need to be made for the matching type
     */
    @Parameter(property = "configs", required = true)
    protected Set<AnnotatorConfig> configs;

    @Parameter(property = "sourceDirectory")
    protected String sourceDirectory;

    @Parameter(property = "outputDirectory")
    protected String outputDirectory;

    public AnnotatorMojo() {
        this.log = getLog();
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        log.info("annotator-maven-plugin started !!!");
        String sourceDirectory = StringUtils.isNotBlank(this.sourceDirectory) ?
                this.sourceDirectory : project.getBuild().getSourceDirectory();
        log.info("sourceDirectory->" + sourceDirectory);
        String outputDirectory = StringUtils.isNotBlank(this.outputDirectory) ?
                this.outputDirectory : project.getBuild().getOutputDirectory();
        log.info("outputDirectory->" + outputDirectory);
        if (CollectionUtils.isEmpty(configs)) {
            log.error("configs is empty !");
            return;
        }
        Map<ElementType, Set<JavadocMapping>> config = new HashMap<>(16);
        for (AnnotatorConfig annotatorConfig : configs) {
            ElementType type = annotatorConfig.getAnnotateType();
            Set<JavadocMapping> set = config.get(type);
            if (null == set) {
                set = new HashSet<>();
            }
            set.addAll(annotatorConfig.getJavadocMappings());
            config.put(type, set);
        }
        log.info("configs->" + JSON.toJSONString(config, SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat));
        ByteBuddyAgent.install();
        List<Annotator> annotators = getSupportedAnnotators();
        for (File javaFile : getJavaFiles(sourceDirectory)) {
            try {
                JavaClassSource source = Roaster.parse(JavaClassSource.class, javaFile);
                Class<?> clazz = Class.forName(source.getQualifiedName());
                DynamicType.Builder<?> builder = new ByteBuddy().redefine(clazz);
                for (Annotator annotator : annotators) {
                    builder = annotator.annotate(builder, config.get(annotator.annotateType()), source);
                }
                DynamicType.Unloaded<?> unloaded = builder.make();
                unloaded.saveIn(new File(outputDirectory));
                unloaded.load(clazz.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
            } catch (Throwable t) {
                log.error(t.getMessage(), t);
            }
        }
        log.info("annotator-maven-plugin finished !!!");
    }

    protected List<Annotator> getSupportedAnnotators() {
        List<Annotator> annotators = new LinkedList<>();
        annotators.add(new ClassAnnotator(this.log));
        annotators.add(new FieldAnnotator(this.log));
        annotators.add(new MethodAnnotator(this.log));
        annotators.add(new ParameterAnnotator(this.log));
        return annotators;
    }

    public Set<File> getJavaFiles(String rootPath) {
        Set<File> allJavaFiles = new LinkedHashSet<>();
        // Set the root path of the java file
        File rootDir = new File(rootPath);
        if (rootDir.isDirectory()) {
            File[] files = rootDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        allJavaFiles.addAll(getJavaFiles(file.getAbsolutePath()));
                    } else {
                        allJavaFiles.add(file);
                    }
                }
            }
        }
        return allJavaFiles;
    }
}
