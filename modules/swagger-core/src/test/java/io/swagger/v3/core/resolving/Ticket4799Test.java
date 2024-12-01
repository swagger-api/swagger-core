package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.resolving.resources.JsonViewObject;
import io.swagger.v3.oas.models.media.Schema;
import org.junit.Test;
import org.testng.Assert;

import javax.validation.Constraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.Map;

import static io.swagger.v3.core.resolving.SwaggerTestBase.mapper;

public class Ticket4799Test {

  @Test
  @JsonView(JsonViewObject.View.Protected.class)
  public void testCompositeConstraintsAreRespected() {
    ObjectMapper mapper = mapper();
    final ModelResolver modelResolver = new ModelResolver(mapper);
    final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

    Schema model = context.resolve(new AnnotatedType(ClassWithAnnotation.class));

    Map<String, Schema<?>> properties = model.getProperties();
    Assert.assertEquals(properties.size(), 1);
    Schema<?> nameSchema = properties.get("name");
    Assert.assertNotNull(nameSchema);
    Assert.assertEquals(nameSchema.getMinLength(), 5);
    Assert.assertEquals(nameSchema.getMaxLength(), 10);
    Assert.assertEquals(nameSchema.getPattern(), "^[0-9]*$");
    Assert.assertEquals(model.getRequired(), Collections.singletonList("name"));
  }

  public static final class ClassWithAnnotation {
    @Pattern(regexp = "^[0-9]*$")
    @CompositeAnnotation
    public String name;
  }

  @Size(min = 5, max = 10)
  @NotNull
  @Constraint(validatedBy = {})
  @Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
  @Retention(RetentionPolicy.RUNTIME)
  public @interface CompositeAnnotation {
  }
}
