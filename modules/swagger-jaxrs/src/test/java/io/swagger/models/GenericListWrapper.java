package io.swagger.models;

import java.util.Collections;
import java.util.List;

public class GenericListWrapper<T> {

    public long someValue;
    public List<T> entries = Collections.emptyList();
}
