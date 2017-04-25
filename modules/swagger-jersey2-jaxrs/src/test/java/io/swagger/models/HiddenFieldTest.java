package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.converter.ModelConverters;
import io.swagger.models.Model;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.*;

public class HiddenFieldTest {

    private final static int NUMBER_OF_PROPERTIES = 2;

    @Test(description = "it shouldn't ignore a hidden field")
    public void testHiddenField() {
        final Map<String, Model> models = ModelConverters.getInstance().read(ModelWithHiddenFields.class);

        final Model model = models.get("ModelWithHiddenFields");
        assertNotNull(model);
        assertEquals(model.getProperties().size(), NUMBER_OF_PROPERTIES);

        final Property idValue = model.getProperties().get("id");
        assertTrue(idValue instanceof LongProperty);
        assertTrue(idValue.getRequired());

        final Property nameValue = model.getProperties().get("name");
        assertTrue(nameValue instanceof StringProperty);

        final Property passwordValue = model.getProperties().get("password");
        assertNull(passwordValue);

        final Property emailValue = model.getProperties().get("email");
        assertNull(emailValue);
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    class ModelWithHiddenFields {
        @ApiModelProperty(required = true)
        public Long id = null;

        @ApiModelProperty(required = true, hidden = false)
        public String name = null;

        @ApiModelProperty(hidden = true)
        public String password = null;

        @ApiModelProperty(value = "Required Email", required = true, hidden = true)
        public String email = null;

        @JsonCreator
        private ModelWithHiddenFields(@JsonProperty("id") Long id,
                                      @JsonProperty("name") String name,
                                      @JsonProperty("password") String password,
                                      @JsonProperty("email") String email) {
            this.id = id;
            this.name = name;
            this.password = password;
            this.email = email;
        }
    }
}
