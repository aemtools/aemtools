package com.test;

import org.apache.sling.models.annotations.*;
import org.apache.sling.models.*;

@Model(
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
class TestModel {

    @org.apache.sling.models.annotations.Optional
    @Inject
    private String injected;

}
