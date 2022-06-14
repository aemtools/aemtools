package com.test;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Properties;

@Component(
        label = "Test service",
        description = "Test service description",
        immediate = true,
        metatype = true)
@Properties({
})
public class TestService {
    @Property(label = "Test property", description = "Test property description", propertyPrivate = true)
    private static final String TEST_PROPERTY_DESCRIPTION = "test.property.description";
}
