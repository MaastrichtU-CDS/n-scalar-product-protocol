package com.florian.nscalarproduct.webservice.domain;

import com.florian.nscalarproduct.data.Attribute;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttributeRequirementRequestTest {

    @Test
    public void testCoverage() {
        //making jUnit happy
        Attribute low = new Attribute(Attribute.AttributeType.real, "-1", "low");
        AttributeRequirement comparison = new AttributeRequirement(low);
        AttributeRequirementsRequest request = new AttributeRequirementsRequest();
        assertEquals(request.getRequirements(), null);

        request.setRequirements(Arrays.asList(comparison));

        assertEquals(request.getRequirements().size(), 1);

    }

}