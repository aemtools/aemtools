package com.test;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;

@Component(
        label = "Test service",
        description = "Test service description",
        immediate = true,
        metatype = true)
public class TestService {

}
