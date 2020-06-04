package io.swagger.v3.jaxrs2.resources.model;

@io.swagger.v3.oas.annotations.media.Schema(
        description = "MultiInheritanceBaseBean",
        subTypes = { MultiInheritanceSubaBaseBean.class, MultiInheritanceSub1bBean.class },
        discriminatorProperty = "beanType"
)
public class MultiInheritanceBaseBean {
    public String beanType;
    public int aa;
    public String ab;
}