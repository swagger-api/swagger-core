package io.swagger.models.properties;

import io.swagger.models.Xml;

public class IntegerProperty extends BaseIntegerProperty {
    private static final String FORMAT = "int32";
    protected Integer _default;

    public IntegerProperty() {
        super(FORMAT);
    }

    public static boolean isType(String type, String format) {
        return TYPE.equals(type) && FORMAT.equals(format);
    }

    public IntegerProperty xml(Xml xml) {
        this.setXml(xml);
        return this;
    }

    public IntegerProperty example(Integer example) {
        this.setExample(String.valueOf(example));
        return this;
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

    public Integer getDefault() {
        return _default;
    }

    public void setDefault(String _default) {
        this._default(_default);
    }

    public void setDefault(Integer _default) {
        this._default = _default;
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