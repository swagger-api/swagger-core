package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.core.util.ValidatorProcessor;
import io.swagger.v3.oas.annotations.parameters.ValidatedParameter;
import io.swagger.v3.oas.models.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Path("/test")
public class Ticket4804ProcessorResource {

    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    static @interface MyValidatorInterface {
        Class[] groups() default {};
    }


    public static class CustomValidatorProcessor implements ValidatorProcessor {

        @Override
        public boolean applyBeanValidatorAnnotations(Schema property, Annotation[] annotations, Schema parent, boolean applyNotNullAnnotations) {
            if (annotations == null || annotations.length < 1) {
                return false;
            }
            Map<String, Annotation> annos = new HashMap<>();
            if (annotations != null) {
                for (Annotation anno : annotations) {
                    annos.put(anno.annotationType().getName(), anno);
                }
            }
            if (annos.containsKey(MyValidatorInterface.class.getName()) && annos.containsKey(NotNull.class.getName())) {
                MyValidatorInterface myValid = (MyValidatorInterface) annos.get(MyValidatorInterface.class.getName());
                NotNull notNull = (NotNull) annos.get(NotNull.class.getName());
                for (Class group : notNull.groups()) {
                    if (Arrays.stream(myValid.groups()).anyMatch(t -> t.equals(group))) {
                        parent.addRequiredItem(property.getName());
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public Set<Class> resolveInvocationGroups(Map<String, Annotation> annos) {
            if (annos.containsKey(MyValidatorInterface.class.getName())) {
                MyValidatorInterface myValid = (MyValidatorInterface) annos.get(MyValidatorInterface.class.getName());
                return new HashSet<>(Arrays.asList(myValid.groups()));
            }
            return null;
        }

        @Override
        public Set<Annotation> resolveInvocationAnnotations(Annotation[] annotations) {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(MyValidatorInterface.class)) {
                    return Collections.singleton(annotation);
                }
            }
            return null;
        }
    }

    @PUT
    @Path("/updatecart")
    public void putCart(@Valid @MyValidatorInterface(groups = {onFoo.class}) @ValidatedParameter(onUpdate.class) Cart cart) {}

    @POST
    @Path("/createcart")
    public void postCart(@Valid @ValidatedParameter(onCreate.class) Cart cart) {}

    @PUT
    @Path("/foocart")
    public void fooCart(@Valid @ValidatedParameter(onFoo.class) Cart cart) {}

    @PUT
    @Path("/barcart")
    public void barCart(Cart cart) {}

    public static interface onCreate {}
    public static interface onUpdate {}
    public static interface onFoo {}

    public static class CartDetails {
        @NotNull(groups = {onCreate.class})
        public String name;

        @NotNull
        public String description;
    }


    public static class Cart {
        @NotNull(groups = {onCreate.class})
        public int pageSize;
        @NotNull(groups = {onFoo.class})
        public CartDetails cartDetails;

        @NotNull
        public CartDetails notNullcartDetails;
    }
}
