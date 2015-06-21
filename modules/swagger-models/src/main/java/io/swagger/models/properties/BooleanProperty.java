package io.swagger.models.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.Xml;

public class BooleanProperty extends AbstractProperty implements Property {
    protected Boolean _default;

    public BooleanProperty() {
        super.type = "boolean";
    }

    public static boolean isType(String type, String format) {
        if ("boolean".equals(type)) {
            return true;
        } else {
            return false;
        }
    }

    public BooleanProperty xml(Xml xml) {
        this.setXml(xml);
        return this;
    }

    public BooleanProperty example(Boolean example) {
        this.setExample(String.valueOf(example));
        return this;
    }

    public BooleanProperty _default(String _default) {
        try {
            this.setDefault(Boolean.parseBoolean(_default));
        } catch (Exception e) {
            //cotinue
        }
        return this;
    }

    public BooleanProperty _default(boolean _default) {
        this.setDefault(_default);
        return this;
    }

    public Boolean getDefault() {
        return _default;
    }

    @JsonIgnore
    public void setDefault(String _default) {
        this._default(_default);
    }

    public void setDefault(Boolean _default) {
        this._default = _default;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_default == null) ? 0 : _default.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BooleanProperty other = (BooleanProperty) obj;
        if (_default == null) {
            if (other._default != null) {
                return false;
            }
        } else if (!_default.equals(other._default)) {
            return false;
        }
        return true;
    }
}