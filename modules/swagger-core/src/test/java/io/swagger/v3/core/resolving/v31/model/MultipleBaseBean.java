package io.swagger.v3.core.resolving.v31.model;

@io.swagger.v3.oas.annotations.media.Schema(
        description = "MultipleBaseBean",
        subTypes = { MultipleSub1Bean.class, MultipleSub2Bean.class }
)
public class MultipleBaseBean {
    public String beanType;
    public int a;
    public String b;
}
