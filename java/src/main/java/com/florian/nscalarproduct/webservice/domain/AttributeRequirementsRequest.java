package com.florian.nscalarproduct.webservice.domain;

import java.util.List;

public class AttributeRequirementsRequest {
    private List<AttributeRequirement> requirements;

    public AttributeRequirementsRequest() {
    }

    public List<AttributeRequirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<AttributeRequirement> requirements) {
        this.requirements = requirements;
    }


}
