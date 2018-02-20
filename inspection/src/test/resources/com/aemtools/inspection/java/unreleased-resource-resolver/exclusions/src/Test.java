package com.test;

import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.SlingHttpServletRequest;

public class Test {

    public ResourceResolver simpleCase(SlingHttpServletRequest request) {
       ResourceResolver resolver = resolverFromRequest(request);

       ResourceResolver resolver2 = recursiveCreation("test", request);
    }

    private ResourceResolver resolverFromRequest(SlingHttpServletRequest request) {
        return request.getResourceResolver();
    }

    private ResourceResolver recursiveCreation(String param, SlingHttpServletRequest request) {
        return resolverFromRequest(request);
    }

}
