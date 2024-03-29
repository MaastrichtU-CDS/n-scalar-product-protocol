package com.florian.nscalarproduct.data;

public class Attribute implements Comparable<Attribute> {
    public enum AttributeType { bool, string, numeric, real }

    private static final String INFINITY = "inf";
    private static final String MINUS_INFINITY = "-inf";

    private AttributeType type;
    private String value;
    private String attributeName;
    private String id;

    public Attribute() {
    }

    public Attribute(AttributeType type, String value, String attributeName) {
        this.type = type;
        this.value = value;
        this.attributeName = attributeName;
    }

    public boolean isUnknown() {
        return this.value.equals("?");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getValue() {
        return value;
    }

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    @Override
    public int compareTo(Attribute attribute) {
        if (this.isUnknown() || attribute.isUnknown()) {
            // Only equal if both are missing values
            return value.equals(attribute.getValue()) ? 0 : 1;
        } else if (this.isAll() || attribute.isAll()) {
            //if either is equal to all always return true
            return 0;
        }
        if (type == AttributeType.bool) {
            return Boolean.parseBoolean(value) == Boolean.parseBoolean(attribute.getValue()) ? 0 : 1;
        } else if (type == AttributeType.string) {
            return value.equals(attribute.getValue()) ? 0 : 1;
        } else if (type == AttributeType.numeric || type == AttributeType.real) {
            Double a = 0.0;
            Double b = 0.0;
            if (value.equals(INFINITY)) {
                //Everything is smaller if the value you compare it to is infinity
                //so return 1
                return 1;
            } else if (value.equals(MINUS_INFINITY)) {
                if (attribute.getValue().equals(MINUS_INFINITY)) {
                    return 0;
                } else {
                    return -1;
                }
            } else {
                a = Double.parseDouble(value);
            }
            if (attribute.getValue().equals(INFINITY)) {
                //If you get here then value is less than infinity, so attribute is bigger
                //so return -1
                return -1;
            } else if (attribute.getValue().equals(MINUS_INFINITY)) {
                return 1;
            } else {
                b = Double.parseDouble(attribute.getValue());
            }
            return Double.compare(a, b);
        }
        //should never come here, but java wants it
        return 0;
    }

    private boolean isAll() {
        //check if the attribute represents all posisble values
        return value.equals("All");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Attribute)) {
            return false;
        }
        Attribute attribute = (Attribute) o;
        return type == attribute.type && compareTo(attribute) == 0 && attributeName.equals(attribute.attributeName);
    }


    @Override
    public int hashCode() {
        int hash = 1;
        final int prime = 5;

        hash = prime * hash + (type == null ? 0 : type.hashCode());
        hash = prime * hash + (value == null ? 0 : value.hashCode());
        hash = prime * hash + (attributeName == null ? 0 : attributeName.hashCode());
        hash = prime * hash + (id == null ? 0 : id.hashCode());
        return hash;
    }
}
