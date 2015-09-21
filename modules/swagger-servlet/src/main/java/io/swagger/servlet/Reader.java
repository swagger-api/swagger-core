package io.swagger.servlet;

import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.models.Contact;
import io.swagger.models.ExternalDocs;
import io.swagger.models.License;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.parameters.Parameter;
import io.swagger.servlet.extensions.ReaderExtension;
import io.swagger.servlet.extensions.ReaderExtensions;
import io.swagger.util.PathUtils;
import io.swagger.util.ReflectionUtils;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The <code>Reader</code> class scans classes for Swagger annotations.
 */
public class Reader {

    private final Swagger swagger;

    private Reader(Swagger swagger) {
        this.swagger = swagger;
    }

    /**
     * Scans a set of classes for Swagger annotations.
     *
     * @param swagger is the Swagger instance
     * @param classes are a set of classes to scan
     */
    public static void read(Swagger swagger, Set<Class<?>> classes) {
        final Reader reader = new Reader(swagger);
        for (Class<?> cls : classes) {
            final ReaderContext context = new ReaderContext(swagger, cls, "", null, false, new ArrayList<String>(),
                    new ArrayList<String>(), new ArrayList<String>(), new ArrayList<Parameter>());
            reader.read(context);
        }
    }

    private void read(ReaderContext context) {
        final SwaggerDefinition swaggerDefinition = context.getCls().getAnnotation(SwaggerDefinition.class);
        if (swaggerDefinition != null) {
            readSwaggerConfig(swaggerDefinition);
        }
        for (Method method : context.getCls().getMethods()) {
            if (ReflectionUtils.isOverriddenMethod(method, context.getCls())) {
                continue;
            }
            final Operation operation = new Operation();
            String operationPath = null;
            String httpMethod = null;

            final Type[] genericParameterTypes = method.getGenericParameterTypes();
            final Annotation[][] paramAnnotations = method.getParameterAnnotations();

            for (ReaderExtension extension : ReaderExtensions.getExtensions()) {
                if (operationPath == null) {
                    operationPath = extension.getPath(context, method);
                }
                if (httpMethod == null) {
                    httpMethod = extension.getHttpMethod(context, method);
                }
                if (operationPath == null || httpMethod == null) {
                    continue;
                }

                if (extension.isReadable(context)) {
                    extension.setDeprecated(operation, method);
                    extension.applyConsumes(context, operation, method);
                    extension.applyProduces(context, operation, method);
                    extension.applyOperationId(operation, method);
                    extension.applySummary(operation, method);
                    extension.applyDescription(operation, method);
                    extension.applySchemes(context, operation, method);
                    extension.applySecurityRequirements(context, operation, method);
                    extension.applyTags(context, operation, method);
                    extension.applyResponses(context, operation, method);
                    extension.applyImplicitParameters(context, operation, method);
                    for (int i = 0; i < genericParameterTypes.length; i++) {
                        extension.applyParameters(context, operation, genericParameterTypes[i], paramAnnotations[i]);
                    }
                }
            }

            if (httpMethod != null) {
                if (operation.getResponses() == null) {
                    operation.defaultResponse(new Response().description("successful operation"));
                }

                final Map<String, String> regexMap = new HashMap<String, String>();
                final String parsedPath = PathUtils.parsePath(operationPath, regexMap);

                Path path = swagger.getPath(parsedPath);
                if (path == null) {
                    path = new Path();
                    swagger.path(parsedPath, path);
                }
                path.set(httpMethod.toLowerCase(), operation);
            }
        }
    }

    private void readSwaggerConfig(SwaggerDefinition config) {
        readInfoConfig(config);

        if (StringUtils.isNotBlank(config.basePath())) {
            swagger.setBasePath(config.basePath());
        }

        if (StringUtils.isNotBlank(config.host())) {
            swagger.setHost(config.host());
        }

        for (String consume : config.consumes()) {
            if (StringUtils.isNotBlank(consume)) {
                swagger.addConsumes(consume);
            }
        }

        for (String produce : config.produces()) {
            if (StringUtils.isNotBlank(produce)) {
                swagger.addProduces(produce);
            }
        }

        if (StringUtils.isNotBlank(config.externalDocs().value())) {
            ExternalDocs externalDocs = swagger.getExternalDocs();
            if (externalDocs == null) {
                externalDocs = new ExternalDocs();
                swagger.setExternalDocs(externalDocs);
            }

            externalDocs.setDescription(config.externalDocs().value());

            if (StringUtils.isNotBlank(config.externalDocs().url())) {
                externalDocs.setUrl(config.externalDocs().url());
            }
        }

        for (io.swagger.annotations.Tag tagConfig : config.tags()) {
            if (StringUtils.isNotBlank(tagConfig.name())) {
                final Tag tag = new Tag();
                tag.setName(tagConfig.name());
                tag.setDescription(tagConfig.description());

                if (StringUtils.isNotBlank(tagConfig.externalDocs().value())) {
                    tag.setExternalDocs(new ExternalDocs(tagConfig.externalDocs().value(),
                            tagConfig.externalDocs().url()));
                }

                addExtensionProperties(tagConfig.extensions(), tag.getVendorExtensions());

                swagger.addTag(tag);
            }
        }

        for (SwaggerDefinition.Scheme scheme : config.schemes()) {
            if (scheme != SwaggerDefinition.Scheme.DEFAULT) {
                swagger.addScheme(Scheme.forValue(scheme.name()));
            }
        }
    }

    private void readInfoConfig(SwaggerDefinition config) {
        final Info infoConfig = config.info();
        io.swagger.models.Info info = swagger.getInfo();
        if (info == null) {
            info = new io.swagger.models.Info();
            swagger.setInfo(info);
        }

        if (StringUtils.isNotBlank(infoConfig.description())) {
            info.setDescription(infoConfig.description());
        }

        if (StringUtils.isNotBlank(infoConfig.termsOfService())) {
            info.setTermsOfService(infoConfig.termsOfService());
        }

        if (StringUtils.isNotBlank(infoConfig.title())) {
            info.setTitle(infoConfig.title());
        }

        if (StringUtils.isNotBlank(infoConfig.version())) {
            info.setVersion(infoConfig.version());
        }

        if (StringUtils.isNotBlank(infoConfig.contact().name())) {
            Contact contact = info.getContact();
            if (contact == null) {
                contact = new Contact();
                info.setContact(contact);
            }

            contact.setName(infoConfig.contact().name());
            if (StringUtils.isNotBlank(infoConfig.contact().email())) {
                contact.setEmail(infoConfig.contact().email());
            }

            if (StringUtils.isNotBlank(infoConfig.contact().url())) {
                contact.setUrl(infoConfig.contact().url());
            }
        }

        if (StringUtils.isNotBlank(infoConfig.license().name())) {
            License license = info.getLicense();
            if (license == null) {
                license = new License();
                info.setLicense(license);
            }

            license.setName(infoConfig.license().name());
            if (StringUtils.isNotBlank(infoConfig.license().url())) {
                license.setUrl(infoConfig.license().url());
            }
        }

        addExtensionProperties(infoConfig.extensions(), info.getVendorExtensions());
    }

    @SuppressWarnings("unchecked")
    private void addExtensionProperties(Extension[] extensions, Map<String, Object> map) {
        for (Extension extension : extensions) {
            String name = extension.name();
            if (name.length() > 0) {

                if (!name.startsWith("x-")) {
                    name = "x-" + name;
                }

                if (!map.containsKey(name)) {
                    map.put(name, new HashMap<String, Object>());
                }

                map = (Map<String, Object>) map.get(name);
            }

            for (ExtensionProperty property : extension.properties()) {
                if (!property.name().isEmpty() && !property.value().isEmpty()) {

                    String propertyName = property.name();
                    if (name.isEmpty() && !propertyName.startsWith("x-")) {
                        propertyName = "x-" + propertyName;
                    }

                    map.put(propertyName, property.value());
                }
            }
        }
    }
}
