package io.swagger.models.auth;

import java.net.URL;

public interface UrlMatcher {
    boolean test(URL url);
}
