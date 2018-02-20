package com.test;

import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.SlingHttpServletRequest;

public class Test {

    private ResourceResolverFactory factory;

    private ResourceResolver resolver;

    public ResourceResolver simpleCase() {
        // unused resource resolver should be reported
        createResolver();

        // this resolver shouldn't be reported as it leaks out
        // of current method
        ResourceResolver resolver1 = createResolver();

        // this should be reported
        ResourceResolver resolver2 = createResolver();
        method1(resolver2);

        // this should not be reported
        ResourceResolver resolver3 = createResolver();
        methodThatCloses(resolver3);

        // this should not be reported
        this.resolver = createResolver();
        resolver = createResolver();
        ResourceResolver resolver4 = resolverFromRequest2(null);

        // this should not be reported
        ResourceResolver resolver5 = createResolver();
        resolver5.close()

        return resolver1;
    }

    // should not be reported
    private ResourceResolver createResolver2() {
        ResourceResolver result = null;
        try {
            result = createResolver();
        } catch (Exception ex) {}

        return result;
    }

    // should be reported
    private ResourceResolver createResolver3() {
        ResourceResolver result = null;
        try {
            result = createResolver();
        } catch (Exception ex) {}
        return null;
    }

    private void methodThatCloses(ResourceResolver resolver) {
        resolver.close();
    }

    private void method1(ResourceResolver resolver) {
        method2(resolver);
    }

    private void method2(ResourceResolver resolver) {

    }

    private ResourceResolver createResolver() {
        return factory.getResourceResolver(null);
    }

    private void resolverFromRequest(SlingHttpServletRequest request) {
        // should not be reported
        ResourceResolver resolver = request.getResourceResolver();
    }

    private ResourceResolver resolverFromRequest2(SlingHttpServletRequest request) {
        return request.getResourceResolver();
    }

}
