package io.swagger.models.properties;

import io.swagger.models.Xml;

import java.util.ArrayList;
import java.util.List;

public class FloatProperty extends DecimalProperty {
    private static final String FORMAT = "float";
    protected List<Float> _enum;

    protected Float _default;

    public FloatProperty() {
        super(FORMAT);
    }

    public FloatProperty _enum(Float value) {
        if (this._enum == null) {
            this._enum = new ArrayList<Float>();
        }
        if (!_enum.contains(value)) {
            _enum.add(value);
        }
        return this;
    }

    public FloatProperty _enum(List<Float> value) {
        this._enum = value;
        return this;
    }

    public static boolean isType(String type, String format) {
        return TYPE.equals(type) && FORMAT.equals(format);
    }

    public FloatProperty xml(Xml xml) {
        this.setXml(xml);
        return this;
    }

    public FloatProperty example(Float example) {
        this.setExample(String.valueOf(example));
        return this;
    }

    public FloatProperty _default(String _default) {
        if (_default != null) {
            try {
                this._default = Float.parseFloat(_default);
            } catch (NumberFormatException e) {
                // continue;
            }
        }
        return this;
    }

    public FloatProperty _default(Float _default) {
        this.setDefault(_default);
        return this;
    }

    public FloatProperty vendorExtension(String key, Object obj) {
        this.setVendorExtension(key, obj);
        return this;
    }

    public Float getDefault() {
        return _default;
    }

    public void setDefault(String _default) {
        this._default(_default);
    }

    public void setDefault(Float _default) {
        this._default = _default;
    }

    public List<Float> getEnum() {
        return _enum;
    }

    public void setEnum(List<Float> _enum) {
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
        if (!(obj instanceof FloatProperty)) {
            return false;
        }
        FloatProperty other = (FloatProperty) obj;
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