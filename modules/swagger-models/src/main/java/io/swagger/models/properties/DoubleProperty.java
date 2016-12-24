package io.swagger.models.properties;

import io.swagger.models.Xml;

import java.util.ArrayList;
import java.util.List;

public class DoubleProperty extends DecimalProperty {
    public static final String FORMAT = "double";
    protected Double _default;
    protected List<Double> _enum;

    public DoubleProperty() {
        super(FORMAT);
    }

    public DoubleProperty _enum(Double value) {
        if (this._enum == null) {
            this._enum = new ArrayList<Double>();
        }
        if (!_enum.contains(value)) {
            _enum.add(value);
        }
        return this;
    }

    public DoubleProperty _enum(List<Double> value) {
        this._enum = value;
        return this;
    }

    public static boolean isType(String type, String format) {
        return TYPE.equals(type) && FORMAT.equals(format);
    }

    public DoubleProperty xml(Xml xml) {
        this.setXml(xml);
        return this;
    }

    public DoubleProperty example(Double example) {
        this.example = example;
        return this;
    }

    public DoubleProperty _default(String _default) {
        if (_default != null) {
            try {
                this._default = Double.parseDouble(_default);
            } catch (NumberFormatException e) {
                // continue;
            }
        }
        return this;
    }

    public DoubleProperty _default(Double _default) {
        this.setDefault(_default);
        return this;
    }

    public DoubleProperty vendorExtension(String key, Object obj) {
        this.setVendorExtension(key, obj);
        return this;
    }

    public DoubleProperty readOnly() {
        this.setReadOnly(Boolean.TRUE);
        return this;
    }

    public Double getDefault() {
        return _default;
    }

    public void setDefault(String _default) {
        this._default(_default);
    }

    public void setDefault(Double _default) {
        this._default = _default;
    }

    public List<Double> getEnum() {
        return _enum;
    }

    public void setEnum(List<Double> _enum) {
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
        if (!(obj instanceof DoubleProperty)) {
            return false;
        }
        DoubleProperty other = (DoubleProperty) obj;
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