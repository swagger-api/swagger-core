package io.swagger.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathUtils {
    private static Logger LOGGER = LoggerFactory.getLogger(PathUtils.class);
    private static final char COLON = ':';
    private static final char OPEN = '{';
    private static final char CLOSE = '}';
    private static final char SLASH = '/';
    private static final Pattern TRIM_PATTERN = Pattern.compile("^/*(.*?)/*$");

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

    public static String collectPath(String... pathParts) {
        final StringBuilder sb = new StringBuilder();
        for (String item : pathParts) {
            if (StringUtils.isBlank(item)) {
                continue;
            }
            final String path = trimPath(item);
            if (StringUtils.isNotBlank(path)) {
                sb.append(SLASH).append(path);
            }
        }
        return sb.length() > 0 ? sb.toString() : String.valueOf(SLASH);
    }

    private static String trimPath(String value) {
        final Matcher matcher = TRIM_PATTERN.matcher(value);
        return matcher.find() && StringUtils.isNotBlank(matcher.group(1)) ? matcher.group(1) : null;
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

    public static HostAndPath parseSwaggerPath(String absolutePath) {

        final HostAndPath result = new HostAndPath();

        if (absolutePath != null) {
            String[] parts = absolutePath.split("://");
            if (parts.length > 1) {
                final String scheme = parts[0];
                final String path = parts[1];
                int firstPos = path.indexOf("/");
                int lastPos = path.length();
                if (path.endsWith("/swagger") || path.endsWith("/swagger.json") ||
                        path.endsWith("/swagger.yaml")) {
                    lastPos = path.lastIndexOf("/");
                }
                if (firstPos < 0) {
                    lastPos = firstPos = path.length();
                }
                if (firstPos == lastPos) {
                    ++lastPos;
                }
                result.setScheme(scheme.toLowerCase());
                result.setBasePath(path.substring(firstPos, lastPos));
                result.setHost(path.substring(0, firstPos));
            }
        }

        return result;
    }

    public static class HostAndPath {
        private String scheme;
        private String host;
        private String basePath;

        public HostAndPath(String scheme, String basePath, String host) {
            this.scheme = scheme;
            this.basePath = basePath;
            this.host = host;
        }

        public HostAndPath() {
        }

        public String getScheme() {
            return scheme;
        }

        public void setScheme(String scheme) {
            this.scheme = scheme;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getBasePath() {
            return basePath;
        }

        public void setBasePath(String basePath) {
            this.basePath = basePath;
        }
    }
}
