package io.swagger.jaxrs.utils;

import io.swagger.jaxrs.Reader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

import java.util.Map;

public class PathUtils {

    private static final char COLON = ':';
    private static final char OPEN = '{';
    private static final char CLOSE = '}';
    private static final char SLASH = '/';

    public static String parsePath(String uri, Map<String, String> patterns) {
        final Logger logger = LoggerFactory.getLogger(Reader.class);
        if (StringUtils.trimToNull(uri) == null) {
            return uri;
        }
        CharacterIterator ci = new StringCharacterIterator(uri);
        StringBuilder pathBuffer = new StringBuilder();
        char c = ci.first();
        do {
            if (c == OPEN) {
                String regexBuffer = cutParameter(ci, patterns);
                if (regexBuffer == null) {
                    logger.warn("Operation path \"" + uri + "\" contains syntax error.");
                    return null;
                }
                pathBuffer.append(regexBuffer);
            } else {
                if (!(c == SLASH && pathBuffer.toString().endsWith(String.valueOf(SLASH)))) {
                    pathBuffer.append(c);
                }
            }
        } while ((c = ci.next()) != CharacterIterator.DONE);
        return pathBuffer.toString();
    }

    private static String cutParameter(CharacterIterator ci, Map<String, String> patterns) {
        StringBuilder pathBuffer = new StringBuilder();
        StringBuilder regexBuffer = new StringBuilder();
        int braceCount = 1;
        for (char regexChar = ci.next(); regexChar != CharacterIterator.DONE; regexChar = ci.next()) {
            if (regexChar == OPEN) {
                braceCount++;
            } else if (regexChar == CLOSE) {
                braceCount--;
                if (braceCount == 0) {
                    break;
                }
            }
            regexBuffer.append(regexChar);
        }
        if (braceCount != 0) {
            return null;
        }
        String regex = regexBuffer.toString();
        pathBuffer.append(OPEN);
        int index = -1;
        if ((index = regex.indexOf(COLON)) != -1) {
            final String name = regex.substring(0, index).trim();
            patterns.put(name, regex.substring(index + 1, regex.length()).trim());
            pathBuffer.append(name);
        } else {
            pathBuffer.append(regex.trim());
        }
        pathBuffer.append(CLOSE);
        return pathBuffer.toString();
    }
}
