package io.swagger.models.properties;

import io.swagger.models.Xml;

public class BooleanProperty extends AbstractProperty implements Property {
    public static final String TYPE = "boolean";
    protected Boolean _default;

    public BooleanProperty() {
        super.type = TYPE;
    }

    public static boolean isType(String type, String format) {
        return TYPE.equals(type);
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

    public BooleanProperty vendorExtension(String key, Object obj) {
        this.setVendorExtension(key, obj);
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
        if (!(obj instanceof BooleanProperty)) {
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