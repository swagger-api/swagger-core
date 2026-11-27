package io.swagger.v3.core.issues;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.jackson.TypeNameResolver;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Reproduces GitHub Issue #5016
 * Duplicate schema names don't throw but are overwritten
 *
 * <p>Tests that a warning is logged when two different types resolve to the same schema name.
 *
 * @see <a href="https://github.com/swagger-api/swagger-core/issues/5016">...</a>
 */
public class Issue5016Test {

    private final Logger logger = (Logger) LoggerFactory.getLogger(ModelConverterContextImpl.class);
    private ListAppender<ILoggingEvent> appender;
    private Level previousLevel;

    @BeforeMethod
    public void attachAppender() {
        appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);
        previousLevel = logger.getLevel();
        logger.setLevel(Level.WARN);
    }

    @AfterMethod
    public void detachAppender() {
        logger.detachAppender(appender);
        logger.setLevel(previousLevel);
    }

    @Test
    public void testWarnsWhenTwoTypesResolveToTheSameSchemaName() {
        ModelConverters.getInstance().readAll(OrderWithClashingItems.class);

        List<String> warnings = warnings();
        assertEquals(warnings.size(), 1);
        assertTrue(warnings.get(0).contains("Schema name \"Item\""));
        assertTrue(warnings.get(0).contains(First.Item.class.getName()));
        assertTrue(warnings.get(0).contains(Second.Item.class.getName()));
    }

    @Test
    public void testDoesNotWarnWhenSchemaNamesAreUnique() {
        ModelConverters.getInstance().readAll(OrderWithRenamedItem.class);

        assertTrue(warnings().isEmpty());
    }

    @Test
    public void testDoesNotWarnForSchemaImplementation() {
        ModelConverters.getInstance().readAll(OrderWithImplementation.class);

        assertTrue(warnings().isEmpty());
    }

    @Test
    public void testDoesNotWarnWhenTheSameTypeIsUsedTwice() {
        ModelConverters.getInstance().readAll(OrderWithSameItemTwice.class);

        assertTrue(warnings().isEmpty());
    }

    @Test
    public void testWarnsWhenTwoParameterizationsResolveToTheSameSchemaName() {
        resolveWithoutTypeArgumentsInNames(HolderWithClashingRecords.class);

        List<String> warnings = warnings();
        assertEquals(warnings.size(), 1);
        assertTrue(warnings.get(0).contains("Schema name \"MyRecord\""));
        assertTrue(warnings.get(0).contains("<java.lang.String>"));
        assertTrue(warnings.get(0).contains("<java.lang.Integer>"));
    }

    @Test
    public void testDoesNotWarnWhenTheSameParameterizationIsUsedTwice() {
        resolveWithoutTypeArgumentsInNames(HolderWithSameRecordTwice.class);

        assertTrue(warnings().isEmpty());
    }

    @Test
    public void testDoesNotWarnWhenSubtypeIsAlsoReferencedDirectly() {
        ModelConverters.getInstance().readAll(OwnerWithPetAndDog.class);

        assertTrue(warnings().isEmpty());
    }

    private void resolveWithoutTypeArgumentsInNames(Class<?> type) {
        ModelResolver modelResolver = new ModelResolver(new ObjectMapper(), new RawClassTypeNameResolver());
        new ModelConverterContextImpl(modelResolver).resolve(new AnnotatedType(type));
    }

    private List<String> warnings() {
        return appender.list.stream()
                .filter(event -> event.getLevel() == Level.WARN)
                .map(ILoggingEvent::getFormattedMessage)
                .collect(Collectors.toList());
    }

    static class First {
        static class Item {
            public String name;
        }
    }

    static class Second {
        static class Item {
            public int quantity;
        }

        @Schema(name = "SecondItem")
        static class RenamedItem {
            public int quantity;
        }
    }

    static class OrderWithClashingItems {
        public First.Item first;
        public Second.Item second;
    }

    static class OrderWithRenamedItem {
        public First.Item first;
        public Second.RenamedItem second;
    }

    static class OrderWithImplementation {
        @Schema(implementation = First.Item.class)
        public Second.Item item;
    }

    static class OrderWithSameItemTwice {
        public First.Item first;
        public First.Item second;
    }

    static class RawClassTypeNameResolver extends TypeNameResolver {
        @Override
        protected String nameForGenericType(JavaType type, Set<Options> options) {
            return nameForClass(type, options);
        }
    }

    static class MyRecord<T> {
        public T value;
    }

    static class HolderWithClashingRecords {
        public MyRecord<String> text;
        public MyRecord<Integer> number;
    }

    static class HolderWithSameRecordTwice {
        public MyRecord<String> first;
        public MyRecord<String> second;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "kind")
    @JsonSubTypes({@JsonSubTypes.Type(value = Dog.class, name = "dog")})
    static class Pet {
        public String name;
    }

    static class Dog extends Pet {
        public boolean barks;
    }

    static class OwnerWithPetAndDog {
        public Pet pet;
        public Dog dog;
    }
}
