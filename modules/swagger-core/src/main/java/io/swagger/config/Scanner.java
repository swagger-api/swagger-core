package io.swagger.config;

import java.util.Set;

public interface Scanner {
    Set<Class<?>> classes();

    boolean getPrettyPrint();

    void setPrettyPrint(boolean shouldPrettyPrint);
}
