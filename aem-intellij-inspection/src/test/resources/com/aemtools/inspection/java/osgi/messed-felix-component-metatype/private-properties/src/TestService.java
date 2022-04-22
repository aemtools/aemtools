package com.test;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;

@Component(
        label = "Test service",
        description = "Test service description",
        immediate = true,
        metatype = true)
public class TestService {

    @Property(label = "Test property", description = "Test property description", propertyPrivate = true)
    private static final String TEST_PROPERTY_DESCRIPTION = "test.property.description";

}
