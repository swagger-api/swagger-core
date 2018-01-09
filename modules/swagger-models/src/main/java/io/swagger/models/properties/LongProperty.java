package io.swagger.models.properties;

import io.swagger.models.Xml;

import java.util.ArrayList;
import java.util.List;

public class LongProperty extends BaseIntegerProperty {
    public static final String FORMAT = "int64";
    protected Long _default;
    protected List<Long> _enum;

    public LongProperty() {
        super(FORMAT);
    }

    public LongProperty _enum(Long value) {
        if (this._enum == null) {
            this._enum = new ArrayList<Long>();
        }
        if (!_enum.contains(value)) {
            _enum.add(value);
        }
        return this;
    }

    public LongProperty _enum(List<Long> value) {
        this._enum = value;
        return this;
    }

    public static boolean isType(String type, String format) {
        return TYPE.equals(type) && FORMAT.equals(format);
    }

    public LongProperty xml(Xml xml) {
        this.setXml(xml);
        return this;
    }

    public LongProperty example(Long example) {
        this.example = example;
        return this;
    }

    public LongProperty _default(String _default) {
        if (_default != null) {
            try {
                this._default = Long.parseLong(_default);
            } catch (NumberFormatException e) {
                // continue;
            }
        }
        return this;
    }

    public LongProperty _default(Long _default) {
        this.setDefault(_default);
        return this;
    }

    public LongProperty vendorExtension(String key, Object obj) {
        this.setVendorExtension(key, obj);
        return this;
    }

    public LongProperty readOnly() {
        this.setReadOnly(Boolean.TRUE);
        return this;
    }

    public Long getDefault() {
        return _default;
    }

    public void setDefault(String _default) {
        this._default(_default);
    }

    public void setDefault(Long _default) {
        this._default = _default;
    }

    public List<Long> getEnum() {
        return _enum;
    }

    public void setEnum(List<Long> _enum) {
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
        if (!(obj instanceof LongProperty)) {
            return false;
        }
        LongProperty other = (LongProperty) obj;
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