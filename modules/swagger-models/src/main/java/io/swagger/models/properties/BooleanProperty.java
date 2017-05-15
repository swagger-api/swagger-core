package io.swagger.models.properties;

import io.swagger.models.Xml;

import java.util.ArrayList;
import java.util.List;

public class BooleanProperty extends AbstractProperty implements Property {
    public static final String TYPE = "boolean";
    protected Boolean _default;
    protected List<Boolean> _enum;

    public BooleanProperty() {
        super.type = TYPE;
    }

    public static boolean isType(String type, String format) {
        return TYPE.equals(type);
    }

    public BooleanProperty _enum(Boolean value) {
        if (this._enum == null) {
            this._enum = new ArrayList<Boolean>();
        }
        if (!_enum.contains(value)) {
            _enum.add(value);
        }
        return this;
    }

    public BooleanProperty xml(Xml xml) {
        this.setXml(xml);
        return this;
    }

    public BooleanProperty example(Boolean example) {
        this.example = example;
        return this;
    }

    @Override
    public void setExample(Object example) {
        if (example instanceof String) {
            this.example = Boolean.parseBoolean((String)example);
        } else {
            this.example = example;
        }
    }

    public BooleanProperty _default(String _default) {
        if(_default != null) {
            try {
                this.setDefault(Boolean.parseBoolean(_default));
            } catch (Exception e) {
                //continue
            }
        }
        return this;
    }

    public BooleanProperty _default(boolean _default) {
        this.setDefault(_default);
        return this;
    }

    public BooleanProperty vendorExtension(String key, Object obj) {
        this.setVendorExtension(key, obj);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BooleanProperty)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        BooleanProperty that = (BooleanProperty) o;

        if (_default != null ? !_default.equals(that._default) : that._default != null) {
            return false;
        }
        return _enum != null ? _enum.equals(that._enum) : that._enum == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (_default != null ? _default.hashCode() : 0);
        result = 31 * result + (_enum != null ? _enum.hashCode() : 0);
        return result;
    }

    public BooleanProperty readOnly() {
        this.setReadOnly(Boolean.TRUE);
        return this;

    }

    public Boolean getDefault() {
        return _default;
    }

    public void setDefault(String _default) {
        this._default(_default);
    }

    public void setDefault(Boolean _default) {
        this._default = _default;
    }

    public List<Boolean> getEnum() {
        return _enum;
    }

    public void setEnum(List<Boolean> _enum) {
        this._enum = _enum;
    }

}