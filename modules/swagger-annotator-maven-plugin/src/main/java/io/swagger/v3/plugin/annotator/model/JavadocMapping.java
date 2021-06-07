package io.swagger.v3.plugin.annotator.model;

import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JavadocMapping {

    private Set<String> conditions;

    /**
     * full name of the annotated class
     */
    private String annotationClassName;

    /**
     * the default value of the annotation
     */
    private Map<String, Object> defaultValues;

    /**
     * Key annotation method name,
     * value annotation key or annotation tag key
     */
    private Map<String, String> mapping;

    public String getCommentKey(String key) {
        if (null == mapping) {
            return null;
        }
        return mapping.get(key);
    }

    public Object getDefaultValue(String methodName) {
        if (null == defaultValues) {
            return null;
        }
        return defaultValues.get(methodName);
    }
}
