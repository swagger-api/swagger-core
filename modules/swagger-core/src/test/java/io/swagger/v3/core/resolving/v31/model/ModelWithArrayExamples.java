package io.swagger.v3.core.resolving.v31.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class ModelWithArrayExamples {

    @Schema(
            description = "Array of strings with multiple array examples",
            examples = {"[\"one\",\"two\",\"three\"]", "[\"four\",\"five\",\"six\"]"}
    )
    private List<String> arrayWithMultipleExamples;

    @Schema(
            description = "Array of integers with array examples",
            examples = {"[1,2,3]", "[4,5,6]"}
    )
    private List<Integer> integerArrayWithExamples;

    @Schema(
            description = "Simple string field with scalar examples",
            examples = {"foo", "bar"}
    )
    private String simpleStringWithExamples;

    @Schema(
            description = "Object field with structured examples",
            examples = {"{\"name\":\"John\",\"age\":30}", "{\"name\":\"Jane\",\"age\":25}"}
    )
    private Object objectWithExamples;

    @Schema(
            description = "Array field with nested array examples (array-of-arrays)",
            examples = {"[[\"a\",\"b\"],[\"c\",\"d\"]]", "[[\"x\",\"y\"],[\"z\"]]"}
    )
    private List<List<String>> nestedArrayWithExamples;

    public List<String> getArrayWithMultipleExamples() {
        return arrayWithMultipleExamples;
    }

    public void setArrayWithMultipleExamples(List<String> arrayWithMultipleExamples) {
        this.arrayWithMultipleExamples = arrayWithMultipleExamples;
    }

    public List<Integer> getIntegerArrayWithExamples() {
        return integerArrayWithExamples;
    }

    public void setIntegerArrayWithExamples(List<Integer> integerArrayWithExamples) {
        this.integerArrayWithExamples = integerArrayWithExamples;
    }

    public String getSimpleStringWithExamples() {
        return simpleStringWithExamples;
    }

    public void setSimpleStringWithExamples(String simpleStringWithExamples) {
        this.simpleStringWithExamples = simpleStringWithExamples;
    }

    public Object getObjectWithExamples() {
        return objectWithExamples;
    }

    public void setObjectWithExamples(Object objectWithExamples) {
        this.objectWithExamples = objectWithExamples;
    }

    public List<List<String>> getNestedArrayWithExamples() {
        return nestedArrayWithExamples;
    }

    public void setNestedArrayWithExamples(List<List<String>> nestedArrayWithExamples) {
        this.nestedArrayWithExamples = nestedArrayWithExamples;
    }
}
