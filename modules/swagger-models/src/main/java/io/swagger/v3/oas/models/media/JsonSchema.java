package io.swagger.v3.oas.models.media;

import io.swagger.v3.oas.models.SpecVersion;

/**
 * JsonSchema
 */

public class JsonSchema extends Schema<Object> {

    public JsonSchema (){
        specVersion(SpecVersion.V31);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class JsonSchema {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("}");
        return sb.toString();
    }
}
