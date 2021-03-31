package io.swagger.v3.jaxrs2.resources.generics.inherited;

public interface IAbstractGenericBase {
    Class<?> getDTOClazz();
    String getSingularName();
    String getPluralName();
}