package io.swagger.models.properties;

import io.swagger.models.Xml;

import java.util.ArrayList;
import java.util.List;

public class IntegerProperty extends BaseIntegerProperty {
    public static final String FORMAT = "int32";
    protected Integer _default;
    protected List<Integer> _enum;

    public IntegerProperty() {
        super(FORMAT);
    }

    public IntegerProperty _enum(Integer value) {
        if (this._enum == null) {
            this._enum = new ArrayList<Integer>();
        }
        if (!_enum.contains(value)) {
            _enum.add(value);
        }
        return this;
    }

    public IntegerProperty _enum(List<Integer> value) {
        this._enum = value;
        return this;
    }

    public static boolean isType(String type, String format) {
        return TYPE.equals(type) && FORMAT.equals(format);
    }

    public IntegerProperty xml(Xml xml) {
        this.setXml(xml);
        return this;
    }

    public IntegerProperty example(Integer example) {
        this.example = example;
        return this;
    }

    public IntegerProperty readOnly() {
        this.setReadOnly(Boolean.TRUE);
        return this;
    }

    @Override
    public void setExample(Object example) {
        if (example instanceof String) {
            try {
                this.example = Integer.parseInt((String)example);
            } catch (NumberFormatException e) {
                this.example = example;
            }
        } else {
            this.example = example;
        }
    }

    public IntegerProperty _default(String _default) {
        if (_default != null) {
            try {
                this._default = Integer.parseInt(_default);
            } catch (NumberFormatException e) {
                // continue;
            }
        }
        return this;
    }

    public IntegerProperty _default(Integer _default) {
        this.setDefault(_default);
        return this;
    }

    public IntegerProperty vendorExtension(String key, Object obj) {
        this.setVendorExtension(key, obj);
        return this;
    }

    public Integer getDefault() {
        return _default;
    }

    public void setDefault(String _default) {
        this._default(_default);
    }

    public void setDefault(Integer _default) {
        this._default = _default;
    }

    public List<Integer> getEnum() {
        return _enum;
    }

    public void setEnum(List<Integer> _enum) {
        this._enum = _enum;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((_default == null) ? 0 : _default.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof IntegerProperty)) {
            return false;
        }
        IntegerProperty other = (IntegerProperty) obj;
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