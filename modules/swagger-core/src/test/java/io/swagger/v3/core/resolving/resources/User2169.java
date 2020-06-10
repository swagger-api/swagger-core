package io.swagger.v3.core.resolving.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class User2169 {
    private String name;
    private int age;
    private String privat;
    public String publi;
    private String getter;

    public String getGetter() {
        return getter;
    }

    private String setter;

    public void setSetter(String setter) {
        this.setter = setter;
    }

    private String getterSetter;

    public String getGetterSetter() {
        return getterSetter;
    }

    public void setGetterSetter(String getterSetter) {
        this.getterSetter = getterSetter;
    }

    @JsonProperty
    private String jsonProp;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String jsonPropReadOnly;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String jsonPropWriteOnly;
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private String jsonPropReadWrite;

    private String getter_jsonProp;

    @JsonProperty
    public String getGetter_jsonProp() {
        return getter_jsonProp;
    }

    private String getter_jsonPropReadOnly;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getGetter_jsonPropReadOnly() {
        return getter_jsonPropReadOnly;
    }

    // Doesn't expect writeOnly as it's semantically wrong?
    private String getter_jsonPropWriteOnly;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getGetter_jsonPropWriteOnly() {
        return getter_jsonPropWriteOnly;
    }

    private String getter_jsonPropReadWrite;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    public String getGetter_jsonPropReadWrite() {
        return getter_jsonPropReadWrite;
    }

    private String setter_jsonProp;
    private String setter_jsonPropReadOnly;
    private String setter_jsonPropWriteOnly;
    private String setter_jsonPropReadWrite;

    @JsonProperty
    public void setSetter_jsonProp(String setter_jsonProp) {
        this.setter_jsonProp = setter_jsonProp;
    }

    // Doesn't expect readOnly as it's semantically wrong?
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public void setSetter_jsonPropReadOnly(String setter_jsonPropReadOnly) {
        this.setter_jsonPropReadOnly = setter_jsonPropReadOnly;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public void setSetter_jsonPropWriteOnly(String setter_jsonPropWriteOnly) {
        this.setter_jsonPropWriteOnly = setter_jsonPropWriteOnly;
    }

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    public void setSetter_jsonPropReadWrite(String setter_jsonPropReadWrite) {
        this.setter_jsonPropReadWrite = setter_jsonPropReadWrite;
    }

    private String gettersetter_jsonPropGet;
    private String gettersetter_jsonPropReadOnlyGet;
    private String gettersetter_jsonPropWriteOnlyGet;
    private String gettersetter_jsonPropReadWriteGet;
    private String gettersetter_jsonPropSet;
    private String gettersetter_jsonPropReadOnlySet;
    private String gettersetter_jsonPropWriteOnlySet;
    private String gettersetter_jsonPropReadWriteSet;

    @JsonProperty
    public String getGettersetter_jsonPropGet() {
        return gettersetter_jsonPropGet;
    }

    public void setGettersetter_jsonPropGet(String gettersetter_jsonPropGet) {
        this.gettersetter_jsonPropGet = gettersetter_jsonPropGet;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getGettersetter_jsonPropReadOnlyGet() {
        return gettersetter_jsonPropReadOnlyGet;
    }

    public void setGettersetter_jsonPropReadOnlyGet(String gettersetter_jsonPropReadOnlyGet) {
        this.gettersetter_jsonPropReadOnlyGet = gettersetter_jsonPropReadOnlyGet;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getGettersetter_jsonPropWriteOnlyGet() {
        return gettersetter_jsonPropWriteOnlyGet;
    }

    public void setGettersetter_jsonPropWriteOnlyGet(String gettersetter_jsonPropWriteOnlyGet) {
        this.gettersetter_jsonPropWriteOnlyGet = gettersetter_jsonPropWriteOnlyGet;
    }

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    public String getGettersetter_jsonPropReadWriteGet() {
        return gettersetter_jsonPropReadWriteGet;
    }

    public void setGettersetter_jsonPropReadWriteGet(String gettersetter_jsonPropReadWriteGet) {
        this.gettersetter_jsonPropReadWriteGet = gettersetter_jsonPropReadWriteGet;
    }

    public String getGettersetter_jsonPropSet() {
        return gettersetter_jsonPropSet;
    }

    @JsonProperty
    public void setGettersetter_jsonPropSet(String gettersetter_jsonPropSet) {
        this.gettersetter_jsonPropSet = gettersetter_jsonPropSet;
    }

    public String getGettersetter_jsonPropReadOnlySet() {
        return gettersetter_jsonPropReadOnlySet;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public void setGettersetter_jsonPropReadOnlySet(String gettersetter_jsonPropReadOnlySet) {
        this.gettersetter_jsonPropReadOnlySet = gettersetter_jsonPropReadOnlySet;
    }

    public String getGettersetter_jsonPropWriteOnlySet() {
        return gettersetter_jsonPropWriteOnlySet;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public void setGettersetter_jsonPropWriteOnlySet(String gettersetter_jsonPropWriteOnlySet) {
        this.gettersetter_jsonPropWriteOnlySet = gettersetter_jsonPropWriteOnlySet;
    }

    public String getGettersetter_jsonPropReadWriteSet() {
        return gettersetter_jsonPropReadWriteSet;
    }

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    public void setGettersetter_jsonPropReadWriteSet(String gettersetter_jsonPropReadWriteSet) {
        this.gettersetter_jsonPropReadWriteSet = gettersetter_jsonPropReadWriteSet;
    }

    private String getterIgnore_setter;
    private String getterIgnore_jsonPropSet;
    private String getterIgnore_jsonPropReadOnlySet;
    private String getterIgnore_jsonPropWriteOnlySet;
    private String getterIgnore_jsonPropReadWriteSet;

    @JsonIgnore
    public String getGetterIgnore_setter() {
        return getterIgnore_setter;
    }

    public void setGetterIgnore_setter(String getterIgnore_setter) {
        this.getterIgnore_setter = getterIgnore_setter;
    }

    @JsonIgnore
    public String getGetterIgnore_jsonPropSet() {
        return getterIgnore_jsonPropSet;
    }

    @JsonProperty
    public void setGetterIgnore_jsonPropSet(String getterIgnore_jsonPropSet) {
        this.getterIgnore_jsonPropSet = getterIgnore_jsonPropSet;
    }

    @JsonIgnore
    public String getGetterIgnore_jsonPropReadOnlySet() {
        return getterIgnore_jsonPropReadOnlySet;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public void setGetterIgnore_jsonPropReadOnlySet(String getterIgnore_jsonPropReadOnlySet) {
        this.getterIgnore_jsonPropReadOnlySet = getterIgnore_jsonPropReadOnlySet;
    }

    @JsonIgnore
    public String getGetterIgnore_jsonPropWriteOnlySet() {
        return getterIgnore_jsonPropWriteOnlySet;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public void setGetterIgnore_jsonPropWriteOnlySet(String getterIgnore_jsonPropWriteOnlySet) {
        this.getterIgnore_jsonPropWriteOnlySet = getterIgnore_jsonPropWriteOnlySet;
    }

    @JsonIgnore
    public String getGetterIgnore_jsonPropReadWriteSet() {
        return getterIgnore_jsonPropReadWriteSet;
    }

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    public void setGetterIgnore_jsonPropReadWriteSet(String getterIgnore_jsonPropReadWriteSet) {
        this.getterIgnore_jsonPropReadWriteSet = getterIgnore_jsonPropReadWriteSet;
    }

    private String setterIgnore_getter;
    private String setterIgnore_jsonPropGet;
    private String setterIgnore_jsonPropReadOnlyGet;
    private String setterIgnore_jsonPropWriteOnlyGet;
    private String setterIgnore_jsonPropReadWriteGet;

    public String getSetterIgnore_getter() {
        return setterIgnore_getter;
    }

    @JsonIgnore
    public void setSetterIgnore_getter(String setterIgnore_getter) {
        this.setterIgnore_getter = setterIgnore_getter;
    }

    @JsonProperty
    public String getSetterIgnore_jsonPropGet() {
        return setterIgnore_jsonPropGet;
    }

    @JsonIgnore
    public void setSetterIgnore_jsonPropGet(String setterIgnore_jsonPropGet) {
        this.setterIgnore_jsonPropGet = setterIgnore_jsonPropGet;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getSetterIgnore_jsonPropReadOnlyGet() {
        return setterIgnore_jsonPropReadOnlyGet;
    }

    @JsonIgnore
    public void setSetterIgnore_jsonPropReadOnlyGet(String setterIgnore_jsonPropReadOnlyGet) {
        this.setterIgnore_jsonPropReadOnlyGet = setterIgnore_jsonPropReadOnlyGet;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getSetterIgnore_jsonPropWriteOnlyGet() {
        return setterIgnore_jsonPropWriteOnlyGet;
    }

    @JsonIgnore
    public void setSetterIgnore_jsonPropWriteOnlyGet(String setterIgnore_jsonPropWriteOnlyGet) {
        this.setterIgnore_jsonPropWriteOnlyGet = setterIgnore_jsonPropWriteOnlyGet;
    }

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    public String getSetterIgnore_jsonPropReadWriteGet() {
        return setterIgnore_jsonPropReadWriteGet;
    }

    @JsonIgnore
    public void setSetterIgnore_jsonPropReadWriteGet(String setterIgnore_jsonPropReadWriteGet) {
        this.setterIgnore_jsonPropReadWriteGet = setterIgnore_jsonPropReadWriteGet;
    }

    @JsonProperty(value = "GetterJsonPropertyOnField", required = true)
    private String getterJsonPropertyOnField;
    @JsonProperty(value = "GetterJsonPropertyOnFieldReadWrite", required = true, access = JsonProperty.Access.READ_WRITE)
    private String getterJsonPropertyOnFieldReadWrite;
    @Schema(accessMode = Schema.AccessMode.READ_WRITE)
    @JsonProperty(value = "GetterJsonPropertyOnFieldReadWriteSchemaReadOnlyFalse", required = true, access = JsonProperty.Access.READ_WRITE)
    private String getterJsonPropertyOnFieldReadWriteSchemaReadOnlyFalse;
    @JsonProperty(value = "GetterJsonPropertyOnFieldReadWriteCreatorSchemaReadOnlyFalse", required = true, access = JsonProperty.Access.READ_WRITE)
    @Schema(accessMode = Schema.AccessMode.READ_WRITE)
    private String getterJsonPropertyOnFieldReadWriteCreatorSchemaReadOnlyFalse;

    @JsonProperty(value = "GetterJsonPropertyOnFieldReadOnly", required = true, access = JsonProperty.Access.READ_ONLY)
    private String getterJsonPropertyOnFieldReadOnly;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(value = "GetterJsonPropertyOnFieldSchemaReadOnlyTrue", required = true)
    private String getterJsonPropertyOnFieldSchemaReadOnlyTrue;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String getterSchemaReadOnlyTrue;

    @JsonCreator
    public User2169(@JsonProperty(value = "Name", required = true) String name,
                    @JsonProperty(value = "Age", required = true) int age,
                    @JsonProperty(value = "GetterJsonPropertyOnFieldReadWriteCreatorSchemaReadOnlyFalse", required = true, access = JsonProperty.Access.READ_WRITE) String getterJsonPropertyOnFieldReadWriteCreatorSchemaReadOnlyFalse) {
        this.name = name;
        this.age = age;
        this.getterJsonPropertyOnFieldReadWriteCreatorSchemaReadOnlyFalse = getterJsonPropertyOnFieldReadWriteCreatorSchemaReadOnlyFalse;
    }

    @JsonProperty(value = "Name", required = true)
    public String getName() {
        return name;
    }

    @JsonProperty(value = "Age", required = true)
    public int getAge() {
        return age;
    }

    public String getGetterJsonPropertyOnField() {
        return getterJsonPropertyOnField;
    }

    public String getGetterJsonPropertyOnFieldReadWrite() {
        return getterJsonPropertyOnFieldReadWrite;
    }

    public String getGetterJsonPropertyOnFieldReadWriteSchemaReadOnlyFalse() {
        return getterJsonPropertyOnFieldReadWriteSchemaReadOnlyFalse;
    }

    public String getGetterJsonPropertyOnFieldReadWriteCreatorSchemaReadOnlyFalse() {
        return getterJsonPropertyOnFieldReadWriteCreatorSchemaReadOnlyFalse;
    }

    public String getGetterJsonPropertyOnFieldReadOnly() {
        return getterJsonPropertyOnFieldReadOnly;
    }

    public String getGetterJsonPropertyOnFieldSchemaReadOnlyTrue() {
        return getterJsonPropertyOnFieldSchemaReadOnlyTrue;
    }

    public String getGetterSchemaReadOnlyTrue() {
        return getterSchemaReadOnlyTrue;
    }

    Boolean isApprovePairing;

    @JsonIgnore
    public Boolean isApprovePairing() {
        return isApprovePairing;
    }

    @JsonProperty(value = "approvePairing", access = JsonProperty.Access.WRITE_ONLY)
    public void setApprovePairing(boolean isApprovePairing) {
        this.isApprovePairing = isApprovePairing;
    }

    // not supported as not supported in json-ref; see discussion in https://github.com/OAI/OpenAPI-Specification/issues/556
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Data data;

}

class Data {
    public String foo;
}
