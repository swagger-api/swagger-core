package io.swagger.jaxrs.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Map;

public class PathUtils {
    private static Logger LOGGER = LoggerFactory.getLogger(PathUtils.class);
    private static final char COLON = ':';
    private static final char OPEN = '{';
    private static final char CLOSE = '}';
    private static final char SLASH = '/';

    public static String parsePath(String uri, Map<String, String> patterns) {
        if (uri == null) {
            return null;
        } else if (StringUtils.isBlank(uri)) {
            return String.valueOf(SLASH);
        }
        CharacterIterator ci = new StringCharacterIterator(uri);
        StringBuilder pathBuffer = new StringBuilder();
        char c = ci.first();
        if (c == CharacterIterator.DONE) {
            return String.valueOf(SLASH);
        }
        do {
            if (c == OPEN) {
                String regexBuffer = cutParameter(ci, patterns);
                if (regexBuffer == null) {
                    LOGGER.warn("Operation path \"" + uri + "\" contains syntax error.");
                    return null;
                }
                pathBuffer.append(regexBuffer);
            } else {
                int length = pathBuffer.length();
                if (!(c == SLASH && (length != 0 && pathBuffer.charAt(length - 1) == SLASH))) {
                    pathBuffer.append(c);
                }
            }
        } while ((c = ci.next()) != CharacterIterator.DONE);
        return pathBuffer.toString();
    }

    private static String cutParameter(CharacterIterator ci, Map<String, String> patterns) {
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
        String regex = StringUtils.trimToNull(regexBuffer.toString());
        if (regex == null) {
            return null;
        }
        StringBuilder pathBuffer = new StringBuilder();
        pathBuffer.append(OPEN);
        int index = regex.indexOf(COLON);
        if (index != -1) {
            final String name = StringUtils.trimToNull(regex.substring(0, index));
            final String value = StringUtils.trimToNull(regex.substring(index + 1, regex.length()));
            if (name != null) {
                pathBuffer.append(name);
                if (value != null) {
                    patterns.put(name, value);
                }
            } else {
                return null;
            }
        } else {
            pathBuffer.append(regex);
        }
        pathBuffer.append(CLOSE);
        return pathBuffer.toString();
    }
}
