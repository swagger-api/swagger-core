package io.swagger.v3.oas.models;

import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

/**
 * Tests for the OpenAPI 3.2 {@code query} HTTP method support on {@link PathItem}.
 */
public class PathItemTest {

    @Test
    public void testQueryBuilderAndAccessors() {
        Operation query = new Operation();
        PathItem pathItem = new PathItem().query(query);

        assertSame(pathItem.getQuery(), query);

        Operation other = new Operation();
        pathItem.setQuery(other);
        assertSame(pathItem.getQuery(), other);
    }

    @Test
    public void testOperationSetterWithQueryMethod() {
        Operation query = new Operation();
        PathItem pathItem = new PathItem();
        pathItem.operation(PathItem.HttpMethod.QUERY, query);

        assertSame(pathItem.getQuery(), query);
    }

    @Test
    public void testHttpMethodEnumContainsQuery() {
        assertEquals(PathItem.HttpMethod.valueOf("QUERY"), PathItem.HttpMethod.QUERY);
    }

    @Test
    public void testReadOperationsIncludesQuery() {
        Operation query = new Operation();
        PathItem pathItem = new PathItem().query(query);

        List<Operation> operations = pathItem.readOperations();
        assertTrue(operations.contains(query));
    }

    @Test
    public void testReadOperationsMapIncludesQuery() {
        Operation query = new Operation();
        PathItem pathItem = new PathItem().query(query);

        Map<PathItem.HttpMethod, Operation> map = pathItem.readOperationsMap();
        assertSame(map.get(PathItem.HttpMethod.QUERY), query);
    }

    @Test
    public void testEqualsAndHashCodeConsiderQuery() {
        Operation query = new Operation().operationId("search");

        PathItem passedObj = new PathItem().query(query);
        PathItem createdObj = new PathItem().query(new Operation().operationId("search"));
        PathItem emptyObj = new PathItem();

        assertEquals(passedObj, createdObj);
        assertEquals(passedObj.hashCode(), createdObj.hashCode());
        assertNotEquals(passedObj, emptyObj);
    }

    @Test
    public void testToStringContainsQuery() {
        PathItem pathItem = new PathItem().query(new Operation());
        assertTrue(pathItem.toString().contains("query:"));
    }
}
