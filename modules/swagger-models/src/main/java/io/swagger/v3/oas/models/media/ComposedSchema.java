package io.swagger.v3.oas.models.media;

/**
 * ComposedSchema
 */

public class ComposedSchema extends Schema<Object> {

    public static ComposedSchema from(Schema subtypeModel) {
        ComposedSchema composedSchema = new ComposedSchema();
        copySchemaProperties(composedSchema, subtypeModel);


        if (shouldSetExample(subtypeModel)) {
            composedSchema.example(subtypeModel.getExample());
        }
        composedSchema.setEnum(subtypeModel.getEnum());
        return composedSchema;
    }

    private static void copySchemaProperties(ComposedSchema target, Schema source) {
        target.title(source.getTitle())
                .name(source.getName())
                .deprecated(source.getDeprecated())
                .additionalProperties(source.getAdditionalProperties())
                .description(source.getDescription())
                .discriminator(source.getDiscriminator())
                .exclusiveMaximum(source.getExclusiveMaximum())
                .exclusiveMinimum(source.getExclusiveMinimum())
                .externalDocs(source.getExternalDocs())
                .format(source.getFormat())
                .maximum(source.getMaximum())
                .maxItems(source.getMaxItems())
                .maxLength(source.getMaxLength())
                .maxProperties(source.getMaxProperties())
                .minimum(source.getMinimum())
                .minItems(source.getMinItems())
                .minLength(source.getMinLength())
                .minProperties(source.getMinProperties())
                .multipleOf(source.getMultipleOf())
                .not(source.getNot())
                .nullable(source.getNullable())
                .pattern(source.getPattern())
                .properties(source.getProperties())
                .readOnly(source.getReadOnly())
                .required(source.getRequired())
                .type(source.getType())
                .uniqueItems(source.getUniqueItems())
                .writeOnly(source.getWriteOnly())
                .xml(source.getXml())
                .extensions(source.getExtensions());
    }


    private static boolean shouldSetExample(Schema model) {
        return model.getExample() != null || model.getExampleSetFlag();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ComposedSchema {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("}");
        return sb.toString();
    }
}
