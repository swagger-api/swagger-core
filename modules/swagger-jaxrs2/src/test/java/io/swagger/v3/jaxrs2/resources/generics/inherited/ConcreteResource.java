package io.swagger.v3.jaxrs2.resources.generics.inherited;

import io.swagger.v3.jaxrs2.resources.model.MultipleSub1Bean;

public class ConcreteResource extends AbstractBaseResource<MultipleSub1Bean> {
    @Override
    public String getSingularName() {
        return "sub1";
    }

    @Override
    public String getPluralName() {
        return "sub1s";
    }

    @Override
    public Class<MultipleSub1Bean> getDTOClazz() {
        return MultipleSub1Bean.class;
    }
}