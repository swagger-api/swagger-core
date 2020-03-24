package io.swagger.v3.jaxrs2.resources.model;

@io.swagger.v3.oas.annotations.media.Schema(
        description = "MultiInheritanceSubaBaseBean",
        subTypes = { MultiInheritanceSub1aBean.class, MultiInheritanceSub2aBean.class },
        discriminatorProperty = "beanType"
)
public abstract class MultiInheritanceSubaBaseBean extends MultiInheritanceBaseBean {
    public int a;
    public String b;
}