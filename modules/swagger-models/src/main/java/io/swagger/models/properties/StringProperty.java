package io.swagger.models.properties;

import io.swagger.models.Xml;

import java.util.ArrayList;
import java.util.List;

/**
 * The StringProperty class defines properties for strings without a specific format, for standard formats which don't
 * need specific handling, or for custom formats.
 */
public class StringProperty extends AbstractProperty implements Property {
    public static final String TYPE = "string";
    protected List<String> _enum;
    protected Integer minLength = null, maxLength = null;
    protected String pattern = null;
    protected String _default;

    public enum Format {
        URI("uri"),
        URL("url");

        private final String name;

        private Format(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static Format fromName(String name) {
            for (Format item : values()) {
                if (item.getName().equals(name)) {
                    return item;
                }
            }
            return null;
        }
    }

    public StringProperty() {
        this((String) null);
    }

    public StringProperty(Format format) {
        this(format.getName());
    }

    public StringProperty(String format) {
        super.type = TYPE;
        super.format = format;
    }

    public static boolean isType(String type, String format) {
        return TYPE.equals(type);
    }

    public StringProperty xml(Xml xml) {
        this.setXml(xml);
        return this;
    }

    public StringProperty example(String example) {
        this.setExample(example);
        return this;
    }

    public StringProperty minLength(Integer minLength) {
        this.setMinLength(minLength);
        return this;
    }

    public StringProperty maxLength(Integer maxLength) {
        this.setMaxLength(maxLength);
        return this;
    }

    public StringProperty pattern(String pattern) {
        this.setPattern(pattern);
        return this;
    }

    public StringProperty _enum(String value) {
        if (this._enum == null) {
            this._enum = new ArrayList<String>();
        }
        if (!_enum.contains(value)) {
            _enum.add(value);
        }
        return this;
    }

    public StringProperty _enum(List<String> value) {
        this._enum = value;
        return this;
    }

    public StringProperty _default(String _default) {
        this._default = _default;
        return this;
    }

    public StringProperty vendorExtension(String key, Object obj) {
        this.setVendorExtension(key, obj);
        return this;
    }

    public StringProperty required(boolean required) {
        this.setRequired(required);
        return this;
    }

    public StringProperty readOnly() {
        this.setReadOnly(Boolean.TRUE);
        return this;
    }

    public List<String> getEnum() {
        return _enum;
    }

    public void setEnum(List<String> _enum) {
        this._enum = _enum;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getDefault() {
        return _default;
    }

    public void setDefault(String _default) {
        this._default = _default;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((_default == null) ? 0 : _default.hashCode());
        result = prime * result + ((_enum == null) ? 0 : _enum.hashCode());
        result = prime * result + ((maxLength == null) ? 0 : maxLength.hashCode());
        result = prime * result + ((minLength == null) ? 0 : minLength.hashCode());
        result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof StringProperty)) {
            return false;
        }
        StringProperty other = (StringProperty) obj;
        if (_default == null) {
            if (other._default != null) {
                return false;
            }
        } else if (!_default.equals(other._default)) {
            return false;
        }
        if (_enum == null) {
            if (other._enum != null) {
                return false;
            }
        } else if (!_enum.equals(other._enum)) {
            return false;
        }
        if (maxLength == null) {
            if (other.maxLength != null) {
                return false;
            }
        } else if (!maxLength.equals(other.maxLength)) {
            return false;
        }
        if (minLength == null) {
            if (other.minLength != null) {
                return false;
            }
        } else if (!minLength.equals(other.minLength)) {
            return false;
        }
        if (pattern == null) {
            if (other.pattern != null) {
                return false;
            }
        } else if (!pattern.equals(other.pattern)) {
            return false;
        }
        return true;
    }
}
