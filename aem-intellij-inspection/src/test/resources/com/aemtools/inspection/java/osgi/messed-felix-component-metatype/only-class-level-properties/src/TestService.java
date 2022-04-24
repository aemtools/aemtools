package com.test;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Properties;

@Component(
        label = "Test service",
        description = "Test service description",
        immediate = true,
        metatype = true)
@Peropties({
        @Property(name = "service.vendor", value = "test", propertyPrivate = true)
})
public class TestService {

}
