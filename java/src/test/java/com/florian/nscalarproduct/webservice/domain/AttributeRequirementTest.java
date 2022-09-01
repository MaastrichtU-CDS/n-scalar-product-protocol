package com.florian.nscalarproduct.webservice.domain;

import com.florian.nscalarproduct.data.Attribute;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AttributeRequirementTest {

    @Test
    public void testRequirementsReal() {
        Attribute low = new Attribute(Attribute.AttributeType.real, "-1", "low");
        Attribute medium = new Attribute(Attribute.AttributeType.real, "0.5", "medium");
        Attribute high = new Attribute(Attribute.AttributeType.real, "3", "high");

        Attribute upperLimit = new Attribute(Attribute.AttributeType.real, "2", "upperLimit");
        Attribute lowerLimit = new Attribute(Attribute.AttributeType.real, "0", "lowerLimit");

        AttributeRequirement comparison = new AttributeRequirement(medium);
        AttributeRequirement range = new AttributeRequirement(lowerLimit, upperLimit);

        assertEquals(comparison.checkRequirement(low), false);
        assertEquals(comparison.checkRequirement(medium), true);
        assertEquals(comparison.checkRequirement(high), false);

        assertEquals(range.checkRequirement(low), false);
        assertEquals(range.checkRequirement(medium), true);
        assertEquals(range.checkRequirement(high), false);
        assertEquals(range.checkRequirement(upperLimit), false);
        assertEquals(range.checkRequirement(lowerLimit), true);
    }

    @Test
    public void testRequirementsNumeric() {
        Attribute low = new Attribute(Attribute.AttributeType.numeric, "-2", "low");
        Attribute medium = new Attribute(Attribute.AttributeType.numeric, "1", "medium");
        Attribute high = new Attribute(Attribute.AttributeType.numeric, "3", "high");

        Attribute upperLimit = new Attribute(Attribute.AttributeType.numeric, "2", "upperLimit");
        Attribute lowerLimit = new Attribute(Attribute.AttributeType.numeric, "0", "lowerLimit");

        AttributeRequirement comparison = new AttributeRequirement(medium);
        AttributeRequirement range = new AttributeRequirement(lowerLimit, upperLimit);

        assertEquals(comparison.checkRequirement(low), false);
        assertEquals(comparison.checkRequirement(medium), true);
        assertEquals(comparison.checkRequirement(high), false);

        assertEquals(range.checkRequirement(low), false);
        assertEquals(range.checkRequirement(medium), true);
        assertEquals(range.checkRequirement(high), false);
        assertEquals(range.checkRequirement(upperLimit), false);
        assertEquals(range.checkRequirement(lowerLimit), true);
    }

    @Test
    public void testString() {
        Attribute a = new Attribute(Attribute.AttributeType.string, "a", "a");
        Attribute b = new Attribute(Attribute.AttributeType.string, "b", "a");

        AttributeRequirement aR = new AttributeRequirement(a);
        AttributeRequirement bR = new AttributeRequirement(b);
        assertFalse(aR.equals(bR));
        assertTrue(aR.equals(aR));
    }

    @Test
    public void testUnknown() {
        //making jUnit happy
        Attribute a = new Attribute(Attribute.AttributeType.string, "a", "a");
        Attribute unknown = new Attribute(Attribute.AttributeType.string, "?", "a");

        AttributeRequirement aR = new AttributeRequirement(a);
        AttributeRequirement unknownR = new AttributeRequirement(unknown);

        assertFalse(unknownR.checkRequirement(a));
        assertTrue(unknownR.checkRequirement(unknown));
    }

    @Test
    public void testInfinities() {
        Attribute upperLimit = new Attribute(Attribute.AttributeType.numeric, "inf", "upperLimit");
        Attribute lowerLimit = new Attribute(Attribute.AttributeType.numeric, "-inf", "upperLimit");
        Attribute zero = new Attribute(Attribute.AttributeType.numeric, "0", "zero");
        Attribute one = new Attribute(Attribute.AttributeType.numeric, "1", "value");

        AttributeRequirement negativeInf = new AttributeRequirement(lowerLimit, one);
        AttributeRequirement positiveINf = new AttributeRequirement(zero, upperLimit);
        AttributeRequirement inf = new AttributeRequirement(lowerLimit, upperLimit);

        assertTrue(negativeInf.checkRequirement(zero));
        assertTrue(positiveINf.checkRequirement(one));
        assertTrue(inf.checkRequirement(zero));
        assertTrue(inf.checkRequirement(one));
    }
}