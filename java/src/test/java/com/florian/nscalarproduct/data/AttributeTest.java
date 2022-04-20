package com.florian.nscalarproduct.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AttributeTest {

    @Test
    public void testAttributeNumeric() {
        Attribute a = new Attribute(Attribute.AttributeType.numeric, "1", "a");
        Attribute equal = new Attribute(Attribute.AttributeType.numeric, "1", "a");
        Attribute greater = new Attribute(Attribute.AttributeType.numeric, "2", "a");
        Attribute all = new Attribute(Attribute.AttributeType.numeric, "All", "a");
        Attribute inf = new Attribute(Attribute.AttributeType.numeric, "inf", "a");
        Attribute minusInf = new Attribute(Attribute.AttributeType.numeric, "-inf", "a");
        Attribute unknown = new Attribute(Attribute.AttributeType.numeric, "?", "a");

        assertTrue(a.equals(equal));
        assertTrue(!a.equals(greater));
        assertTrue(a.equals(all));
        assertTrue(!a.equals(inf));
        assertTrue(!a.equals(minusInf));

        assertEquals(a.compareTo(greater), -1);
        assertEquals(greater.compareTo(a), 1);

        assertEquals(a.compareTo(inf), -1);
        assertEquals(inf.compareTo(a), 1);

        assertEquals(a.compareTo(minusInf), 1);
        assertEquals(minusInf.compareTo(a), -1);

        assertTrue(inf.equals(all));
        assertTrue(minusInf.equals(all));

        assertTrue(!unknown.equals(a));
        assertTrue(!unknown.equals(equal));
        assertTrue(!unknown.equals(greater));
        assertTrue(!unknown.equals(all));
        assertTrue(!unknown.equals(inf));
        assertTrue(!unknown.equals(minusInf));
        assertTrue(unknown.equals(unknown));
    }

    @Test
    public void testAttributeReal() {
        Attribute a = new Attribute(Attribute.AttributeType.real, "1", "a");
        Attribute equal = new Attribute(Attribute.AttributeType.real, "1", "a");
        Attribute greater = new Attribute(Attribute.AttributeType.real, "2", "a");
        Attribute all = new Attribute(Attribute.AttributeType.real, "All", "a");
        Attribute inf = new Attribute(Attribute.AttributeType.real, "inf", "a");
        Attribute minusInf = new Attribute(Attribute.AttributeType.real, "-inf", "a");
        Attribute unknown = new Attribute(Attribute.AttributeType.real, "?", "a");

        assertTrue(a.equals(equal));
        assertTrue(!a.equals(greater));
        assertTrue(a.equals(all));
        assertTrue(!a.equals(inf));
        assertTrue(!a.equals(minusInf));

        assertEquals(a.compareTo(greater), -1);
        assertEquals(greater.compareTo(a), 1);

        assertEquals(a.compareTo(inf), -1);
        assertEquals(inf.compareTo(a), 1);

        assertEquals(a.compareTo(minusInf), 1);
        assertEquals(minusInf.compareTo(a), -1);

        assertTrue(inf.equals(all));
        assertTrue(minusInf.equals(all));

        assertTrue(!unknown.equals(a));
        assertTrue(!unknown.equals(equal));
        assertTrue(!unknown.equals(greater));
        assertTrue(!unknown.equals(all));
        assertTrue(!unknown.equals(inf));
        assertTrue(!unknown.equals(minusInf));
        assertTrue(unknown.equals(unknown));

    }

    @Test
    public void testAttributeBoolean() {
        Attribute a = new Attribute(Attribute.AttributeType.bool, "true", "a");
        Attribute b = new Attribute(Attribute.AttributeType.bool, "false", "a");
        Attribute all = new Attribute(Attribute.AttributeType.bool, "All", "a");
        Attribute unknown = new Attribute(Attribute.AttributeType.bool, "?", "a");

        assertTrue(!a.equals(b));
        assertTrue(a.equals(a));
        assertTrue(b.equals(all));
        assertTrue(a.equals(all));

        assertTrue(!unknown.equals(a));
        assertTrue(!unknown.equals(b));
        assertTrue(!unknown.equals(all));
        assertTrue(unknown.equals(unknown));
    }

    @Test
    public void testAttributeString() {
        Attribute a = new Attribute(Attribute.AttributeType.string, "string 1", "a");
        Attribute b = new Attribute(Attribute.AttributeType.string, "string 2", "a");
        Attribute all = new Attribute(Attribute.AttributeType.string, "All", "a");
        Attribute unknown = new Attribute(Attribute.AttributeType.string, "?", "a");

        assertTrue(!a.equals(b));
        assertTrue(a.equals(a));
        assertTrue(b.equals(all));
        assertTrue(a.equals(all));

        assertTrue(!unknown.equals(a));
        assertTrue(!unknown.equals(b));
        assertTrue(!unknown.equals(all));
        assertTrue(unknown.equals(unknown));
    }

    @Test
    public void testAttributeMixedTyping() {
        Attribute string = new Attribute(Attribute.AttributeType.string, "1", "a");
        Attribute numeric = new Attribute(Attribute.AttributeType.numeric, "1", "a");
        Attribute bool = new Attribute(Attribute.AttributeType.bool, "true", "a");
        Attribute real = new Attribute(Attribute.AttributeType.real, "1", "a");
        Attribute stringBool = new Attribute(Attribute.AttributeType.string, "1", "a");

        assertTrue(!stringBool.equals(bool));
        assertTrue(!string.equals(numeric));
        assertTrue(!string.equals(bool));
        assertTrue(!string.equals(real));
        assertTrue(string.equals(string));

        assertTrue(!numeric.equals(string));
        assertTrue(!numeric.equals(bool));
        assertTrue(!numeric.equals(real));
        assertTrue(numeric.equals(numeric));

        assertTrue(!bool.equals(numeric));
        assertTrue(!bool.equals(string));
        assertTrue(!bool.equals(real));
        assertTrue(bool.equals(bool));

        assertTrue(!real.equals(numeric));
        assertTrue(!real.equals(bool));
        assertTrue(!real.equals(string));
        assertTrue(real.equals(real));

    }
}