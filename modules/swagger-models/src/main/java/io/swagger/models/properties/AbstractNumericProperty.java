package io.swagger.models.properties;

public abstract class AbstractNumericProperty extends AbstractProperty implements Property {
    protected Double minimum, maximum, multipleOf;
    protected Boolean exclusiveMinimum, exclusiveMaximum;

    public AbstractNumericProperty minimum(Double minimum) {
        this.setMinimum(minimum);
        return this;
    }

    public AbstractNumericProperty maximum(Double maximum) {
        this.setMaximum(maximum);
        return this;
    }

    public AbstractNumericProperty exclusiveMinimum(Boolean exclusiveMinimum) {
        this.setExclusiveMinimum(exclusiveMinimum);
        return this;
    }

    public AbstractNumericProperty exclusiveMaximum(Boolean exclusiveMaximum) {
        this.setExclusiveMaximum(exclusiveMaximum);
        return this;
    }

    public AbstractNumericProperty multipleOf(Double multipleOf) {
        this.setMultipleOf(multipleOf);
        return this;
    }

    public Double getMinimum() {
        return minimum;
    }

    public void setMinimum(Double minimum) {
        this.minimum = minimum;
    }

    public Double getMaximum() {
        return maximum;
    }

    public void setMaximum(Double maximum) {
        this.maximum = maximum;
    }

    public Boolean getExclusiveMinimum() {
        return exclusiveMinimum;
    }

    public void setExclusiveMinimum(Boolean exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
    }

    public Boolean getExclusiveMaximum() {
        return exclusiveMaximum;
    }

    public void setExclusiveMaximum(Boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
    }

    public Double getMultipleOf() {
        return multipleOf;
    }

    public void setMultipleOf(Double multipleOf) {
        this.multipleOf = multipleOf;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((exclusiveMaximum == null) ? 0 : exclusiveMaximum.hashCode());
        result = prime * result
                + ((exclusiveMinimum == null) ? 0 : exclusiveMinimum.hashCode());
        result = prime * result + ((maximum == null) ? 0 : maximum.hashCode());
        result = prime * result + ((minimum == null) ? 0 : minimum.hashCode());
        result = prime * result + ((multipleOf == null) ? 0 : multipleOf.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractNumericProperty other = (AbstractNumericProperty) obj;
        if (exclusiveMaximum == null) {
            if (other.exclusiveMaximum != null) {
                return false;
            }
        } else if (!exclusiveMaximum.equals(other.exclusiveMaximum)) {
            return false;
        }
        if (exclusiveMinimum == null) {
            if (other.exclusiveMinimum != null) {
                return false;
            }
        } else if (!exclusiveMinimum.equals(other.exclusiveMinimum)) {
            return false;
        }
        if (maximum == null) {
            if (other.maximum != null) {
                return false;
            }
        } else if (!maximum.equals(other.maximum)) {
            return false;
        }
        if (minimum == null) {
            if (other.minimum != null) {
                return false;
            }
        } else if (!minimum.equals(other.minimum)) {
            return false;
        }
        if (multipleOf == null) {
            if (other.multipleOf != null) {
                return false;
            }
        } else if (!multipleOf.equals(other.multipleOf)) {
            return false;
        }
        return true;
    }
}
