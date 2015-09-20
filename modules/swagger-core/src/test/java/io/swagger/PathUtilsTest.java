package io.swagger;

import static org.testng.Assert.assertEquals;

import io.swagger.util.PathUtils;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class PathUtilsTest {

    @Test(description = "parse regex with slash inside it from issue 1153")
    public void parseRegexWithSlashInside() {
        final Map<String, String> regexMap = new HashMap<String, String>();
        final String path = PathUtils.parsePath("/{itemId: [0-9]{4}/[0-9]{2}/[0-9]{2}/[0-9]{2}/[0-9]{2}/[0-9]{2}/[0-9]{3}/[A-Za-z0-9]+}", regexMap);
        assertEquals(path, "/{itemId}");
        assertEquals(regexMap.get("itemId"), "[0-9]{4}/[0-9]{2}/[0-9]{2}/[0-9]{2}/[0-9]{2}/[0-9]{2}/[0-9]{3}/[A-Za-z0-9]+");
    }

    @Test(description = "parse two part path with one param")
    public void parseTwoPartPathWithOneParam() {
        final Map<String, String> regexMap = new HashMap<String, String>();
        final String path = PathUtils.parsePath("/api/{itemId: [0-9]{4}/[0-9]{2,4}/[A-Za-z0-9]+}", regexMap);
        assertEquals(path, "/api/{itemId}");
        assertEquals(regexMap.get("itemId"), "[0-9]{4}/[0-9]{2,4}/[A-Za-z0-9]+");
    }

    @Test(description = "parse two part path with two params and white spaces around")
    public void parseTwoPartPathWithTwoParams() {
        final Map<String, String> regexMap = new HashMap<String, String>();
        final String path = PathUtils.parsePath("/{itemId: [0-9]{4}/[A-Za-z0-9]+}/{ api : [aA-zZ]+ }", regexMap);
        assertEquals(path, "/{itemId}/{api}");
        assertEquals(regexMap.get("itemId"), "[0-9]{4}/[A-Za-z0-9]+");
        assertEquals(regexMap.get("api"), "[aA-zZ]+");
    }

    @Test(description = "parse simple path")
    public void parseSimplePath() {
        final Map<String, String> regexMap = new HashMap<String, String>();
        final String path = PathUtils.parsePath("/api/itemId", regexMap);
        assertEquals(path, "/api/itemId");
        assertEquals(regexMap.size(), 0);
    }

    @Test(description = "parse path with param without regex")
    public void parsePathWithoutRegex() {
        final Map<String, String> regexMap = new HashMap<String, String>();
        final String path = PathUtils.parsePath("/api/{name}", regexMap);
        assertEquals(path, "/api/{name}");
        assertEquals(regexMap.size(), 0);
    }

    @Test(description = "parse path with two params in one part")
    public void parsePathWithTwoParamsInOnePart() {
        final Map<String, String> regexMap = new HashMap<String, String>();
        final String path = PathUtils.parsePath("/{a:\\w+}-{b:\\w+}/c", regexMap);
        assertEquals(path, "/{a}-{b}/c");
        assertEquals(regexMap.get("a"), "\\w+");
        assertEquals(regexMap.get("b"), "\\w+");
    }

    @Test(description = "parse path like /swagger.{json|yaml}")
    public void test() {
        final Map<String, String> regexMap = new HashMap<String, String>();
        final String path = PathUtils.parsePath("/swagger.{json|yaml}", regexMap);
        assertEquals(path, "/swagger.{json|yaml}");
        assertEquals(regexMap.size(), 0);
    }

    @Test(description = "parse path with many braces and slashes iside")
    public void parsePathWithBracesAndSlashes() {
        final Map<String, String> regexMap = new HashMap<String, String>();
        final String path = PathUtils.parsePath("/api/{regex:/(?!\\{\\})\\w*|/\\{\\w+:*([^\\{\\}]*(\\{.*\\})*)*\\}}", regexMap);
        assertEquals(path, "/api/{regex}");
        assertEquals(regexMap.get("regex"), "/(?!\\{\\})\\w*|/\\{\\w+:*([^\\{\\}]*(\\{.*\\})*)*\\}");
    }

    @Test(description = "collect path")
    public void collectPath() {
        final String path = PathUtils.collectPath("api", "/users/", "{userId}/");
        assertEquals(path, "/api/users/{userId}");
    }

    @Test(description = "collect path with many slashes inside")
    public void collectPathWithSlashesInside() {
        final String path = PathUtils.collectPath("///api/users///", "///getUser///", "/ /");
        assertEquals(path, "/api/users/getUser");
    }

    @Test(description = "not fail when passed path is null")
    public void testNullPath() {
        final Map<String, String> regexMap = new HashMap<String, String>();
        final String path = PathUtils.parsePath(null, regexMap);
        assertEquals(path, null);
    }

    @Test(description = "not fail when regex is not valid")
    public void testInvalidRegex() {
        final Map<String, String> regexMap = new HashMap<String, String>();
        final String path = PathUtils.parsePath("/api/{fail: [a-z]", regexMap);
        assertEquals(path, null);
    }

    @Test(description = "not fail when passed path is empty")
    public void testEmptyPath() {
        final Map<String, String> regexMap = new HashMap<String, String>();
        final String path = PathUtils.parsePath("", regexMap);
        assertEquals(path, "/");
    }

    @Test(description = "not fail when passed path is null")
    public void testNullCollectedPath() {
        final String path = PathUtils.collectPath(null, null);
        assertEquals(path, "/");
    }

    @Test(description = "not fail when passed path is empty")
    public void testEmptyCollectedPath() {
        final String path = PathUtils.collectPath("");
        assertEquals(path, "/");
    }
}
