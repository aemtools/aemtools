package com.test;

public class Test implements javax.servlet.Servlet {

    private org.apache.sling.api.resource.ResourceResolver resolver;

    private javax.jcr.Session session;

    private com.day.cq.wcm.api.PageManager pageManager;

    private com.day.cq.wcm.api.components.ComponentManager componentManager;

    private com.day.cq.wcm.api.designer.Designer designer;

    private com.day.cq.dam.api.AssetManager assetManager;

    private com.day.cq.tagging.TagManager tagManager;

    private com.day.cq.security.UserManager userManager;

    private org.apache.jackrabbit.api.security.user.Authorizable authorizable;

    private org.apache.jackrabbit.api.security.user.User user;

    private org.apache.jackrabbit.api.security.user.UserManager userManager;

}
