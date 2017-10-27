package com.aemtools.blocks.fixture

import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture

/**
 * @author Dmytro Troynikov
 */
interface UberJarFixtureMixin : OSGiFelixAnnotationsMixin {

  fun JavaCodeInsightTestFixture.addUberJar() {
    addClass("""
            package org.apache.sling.api.resource;

            import aQute.bnd.annotation.ProviderType;
            import java.util.Iterator;
            import org.apache.sling.api.adapter.Adaptable;

            @ProviderType
            public interface Resource extends Adaptable {
                String RESOURCE_TYPE_NON_EXISTING = "sling:nonexisting";

                String getPath();

                String getName();

                Resource getParent();

                Iterator<Resource> listChildren();

                Iterable<Resource> getChildren();

                Resource getChild(String var1);

                String getResourceType();

                String getResourceSuperType();

                boolean hasChildren();

                boolean isResourceType(String var1);

                ResourceMetadata getResourceMetadata();

                ResourceResolver getResourceResolver();

                ValueMap getValueMap();
            }
        """)

    addClass("""
            package org.apache.sling.api.resource;

            import aQute.bnd.annotation.ConsumerType;
            import java.util.Collections;
            import java.util.Map;
            import org.apache.sling.api.wrappers.ValueMapDecorator;

            @ConsumerType
            public interface ValueMap extends Map<String, Object> {
                ValueMap EMPTY = new ValueMapDecorator(Collections.emptyMap());

                <T> T get(String var1, Class<T> var2);

                <T> T get(String var1, T var2);
            }
        """)

    addClass("""
            package org.apache.sling.models.annotations;

            import java.lang.annotation.ElementType;
            import java.lang.annotation.Retention;
            import java.lang.annotation.RetentionPolicy;
            import java.lang.annotation.Target;

            @Target({ElementType.TYPE})
            @Retention(RetentionPolicy.RUNTIME)
            public @interface Model {
                Class<?>[] adaptables();

                Class<?>[] adapters() default {};

                DefaultInjectionStrategy defaultInjectionStrategy() default DefaultInjectionStrategy.REQUIRED;

                String condition() default "";
            }
        """)

    addClass("""
            package com.day.cq.commons;

            public interface Filter<T> {
                boolean includes(T var1);
            }
        """)

    addClass("""
            package com.day.cq.commons;

            public interface LabeledResource {
                String getPath();

                String getName();

                String getTitle();

                String getDescription();
            }
        """)

    addClass("""
            package com.day.cq.tagging;

            import com.day.cq.commons.Filter;
            import java.util.Iterator;
            import java.util.Locale;
            import java.util.Map;
            import org.apache.sling.api.adapter.Adaptable;
            import org.apache.sling.api.resource.Resource;

            public interface Tag extends Adaptable {
                String getName();

                String getTagID();

                String getLocalTagID();

                String getPath();

                String getTitle();

                String getTitle(Locale var1);

                String getLocalizedTitle(Locale var1);

                Map<Locale, String> getLocalizedTitles();

                String getDescription();

                String getTitlePath();

                String getTitlePath(Locale var1);

                Map<Locale, String> getLocalizedTitlePaths();

                long getCount();

                long getLastModified();

                String getLastModifiedBy();

                boolean isNamespace();

                Tag getNamespace();

                Tag getParent();

                Iterator<Tag> listChildren();

                Iterator<Tag> listChildren(Filter<Tag> var1);

                Iterator<Tag> listAllSubTags();

                Iterator<Resource> find();

                String getXPathSearchExpression(String var1);

                String getGQLSearchExpression(String var1);
            }
        """)

    addClass("""
            package org.apache.sling.api.adapter;

            import aQute.bnd.annotation.ConsumerType;

            @ConsumerType
            public interface Adaptable {
                <AdapterType> AdapterType adaptTo(Class<AdapterType> var1);
            }
        """)

    addClass("""
            package com.day.cq.wcm.api;

            import com.day.cq.commons.Filter;
            import com.day.cq.commons.LabeledResource;
            import com.day.cq.tagging.Tag;
            import org.apache.sling.api.adapter.Adaptable;
            import org.apache.sling.api.resource.Resource;
            import org.apache.sling.api.resource.ValueMap;

            import java.util.Calendar;
            import java.util.Iterator;
            import java.util.Locale;

            public interface Page extends Adaptable, LabeledResource {
                String getPath();

                PageManager getPageManager();

                Resource getContentResource();

                Resource getContentResource(String var1);

                Iterator<Page> listChildren();

                Iterator<Page> listChildren(Filter<Page> var1);

                Iterator<Page> listChildren(Filter<Page> var1, boolean var2);

                boolean hasChild(String var1);

                int getDepth();

                Page getParent();

                Page getParent(int var1);

                Page getAbsoluteParent(int var1);

                ValueMap getProperties();

                ValueMap getProperties(String var1);

                String getName();

                String getTitle();

                String getPageTitle();

                String getNavigationTitle();

                boolean isHideInNav();

                boolean hasContent();

                boolean isValid();

                long timeUntilValid();

                Calendar getOnTime();

                Calendar getOffTime();

                String getLastModifiedBy();

                Calendar getLastModified();

                String getVanityUrl();

                Tag[] getTags();

                void lock() throws WCMException;

                boolean isLocked();

                String getLockOwner();

                boolean canUnlock();

                void unlock() throws WCMException;

                Template getTemplate();

                Locale getLanguage(boolean var1);
            }

        """)

    addClass("""
            package io.sightly.java.api;

            import javax.script.Bindings;

            public interface Use {
                void init(Bindings var1);
            }
        """)

    addClass("""
            package com.adobe.cq.sightly;

            import com.day.cq.wcm.api.Page;
            import com.day.cq.wcm.api.PageManager;
            import com.day.cq.wcm.api.components.Component;
            import com.day.cq.wcm.api.designer.Design;
            import com.day.cq.wcm.api.designer.Designer;
            import com.day.cq.wcm.api.designer.Style;
            import io.sightly.java.api.Use;
            import javax.script.Bindings;
            import org.apache.sling.api.SlingHttpServletRequest;
            import org.apache.sling.api.SlingHttpServletResponse;
            import org.apache.sling.api.resource.Resource;
            import org.apache.sling.api.resource.ResourceResolver;
            import org.apache.sling.api.resource.ValueMap;
            import org.apache.sling.api.scripting.SlingScriptHelper;
            import org.slf4j.Logger;
            import org.slf4j.LoggerFactory;

            public abstract class WCMUse implements Use {
                private final Logger log = LoggerFactory.getLogger(WCMUse.class);
                private Bindings bindings;

                public WCMUse() {
                }

                public void init(Bindings scriptBindings) {
                    this.bindings = scriptBindings;

                    try {
                        this.activate();
                    } catch (Exception var3) {
                        this.log.error("Failed to activate Use class", var3);
                    }

                }

                public abstract void activate() throws Exception;

                public <T> T get(String name, Class<T> type) {
                    Object obj = this.bindings.get(name);

                    try {
                        return type.cast(obj);
                    } catch (ClassCastException var5) {
                        this.log.error("Failed to cast value", var5);
                        return null;
                    }
                }

                public SightlyWCMMode getWcmMode() {
                    return (SightlyWCMMode)this.get("wcmmode", SightlyWCMMode.class);
                }

                public PageManager getPageManager() {
                    return (PageManager)this.get("pageManager", PageManager.class);
                }

                public Page getCurrentPage() {
                    return (Page)this.get("currentPage", Page.class);
                }

                public Page getResourcePage() {
                    return (Page)this.get("resourcePage", Page.class);
                }

                public ValueMap getPageProperties() {
                    return (ValueMap)this.get("pageProperties", ValueMap.class);
                }

                public ValueMap getProperties() {
                    return (ValueMap)this.get("properties", ValueMap.class);
                }

                public Designer getDesigner() {
                    return (Designer)this.get("designer", Designer.class);
                }

                public Design getCurrentDesign() {
                    return (Design)this.get("currentDesign", Design.class);
                }

                public Style getCurrentStyle() {
                    return (Style)this.get("currentStyle", Style.class);
                }

                public Component getComponent() {
                    return (Component)this.get("component", Component.class);
                }

                public ValueMap getInheritedProperties() {
                    return (ValueMap)this.get("inheritedPageProperties", ValueMap.class);
                }

                public Resource getResource() {
                    return (Resource)this.get("resource", Resource.class);
                }

                public ResourceResolver getResourceResolver() {
                    return this.getRequest().getResourceResolver();
                }

                public SlingHttpServletRequest getRequest() {
                    return (SlingHttpServletRequest)this.get("request", SlingHttpServletRequest.class);
                }

                public SlingHttpServletResponse getResponse() {
                    return (SlingHttpServletResponse)this.get("response", SlingHttpServletResponse.class);
                }

                public SlingScriptHelper getSlingScriptHelper() {
                    return (SlingScriptHelper)this.get("sling", SlingScriptHelper.class);
                }
            }

        """)

    addClass("""
            package org.apache.sling.api;

            import aQute.bnd.annotation.ProviderType;
            import java.util.Enumeration;
            import java.util.List;
            import java.util.Locale;
            import java.util.ResourceBundle;
            import javax.annotation.CheckForNull;
            import javax.annotation.Nonnull;
            import javax.servlet.RequestDispatcher;
            import javax.servlet.http.Cookie;
            import javax.servlet.http.HttpServletRequest;
            import org.apache.sling.api.adapter.Adaptable;
            import org.apache.sling.api.request.RequestDispatcherOptions;
            import org.apache.sling.api.request.RequestParameter;
            import org.apache.sling.api.request.RequestParameterMap;
            import org.apache.sling.api.request.RequestPathInfo;
            import org.apache.sling.api.request.RequestProgressTracker;
            import org.apache.sling.api.resource.Resource;
            import org.apache.sling.api.resource.ResourceResolver;

            @ProviderType
            public interface SlingHttpServletRequest extends HttpServletRequest, Adaptable {
                @Nonnull
                Resource getResource();

                @Nonnull
                ResourceResolver getResourceResolver();

                @Nonnull
                RequestPathInfo getRequestPathInfo();

                @CheckForNull
                RequestParameter getRequestParameter(@Nonnull String var1);

                @CheckForNull
                RequestParameter[] getRequestParameters(@Nonnull String var1);

                @Nonnull
                RequestParameterMap getRequestParameterMap();

                @Nonnull
                List<RequestParameter> getRequestParameterList();

                @CheckForNull
                RequestDispatcher getRequestDispatcher(@Nonnull String var1, RequestDispatcherOptions var2);

                @CheckForNull
                RequestDispatcher getRequestDispatcher(@Nonnull Resource var1, RequestDispatcherOptions var2);

                @CheckForNull
                RequestDispatcher getRequestDispatcher(@Nonnull Resource var1);

                @CheckForNull
                Cookie getCookie(String var1);

                @CheckForNull
                String getResponseContentType();

                @Nonnull
                Enumeration<String> getResponseContentTypes();

                @CheckForNull
                ResourceBundle getResourceBundle(Locale var1);

                @CheckForNull
                ResourceBundle getResourceBundle(String var1, Locale var2);

                @Nonnull
                RequestProgressTracker getRequestProgressTracker();
            }
        """)

    addClass("""
            package org.apache.sling.api.request;

            import aQute.bnd.annotation.ProviderType;
            import javax.annotation.CheckForNull;
            import javax.annotation.Nonnull;
            import org.apache.sling.api.resource.Resource;

            @ProviderType
            public interface RequestPathInfo {
                @Nonnull
                String getResourcePath();

                @CheckForNull
                String getExtension();

                @CheckForNull
                String getSelectorString();

                @Nonnull
                String[] getSelectors();

                @CheckForNull
                String getSuffix();

                @CheckForNull
                Resource getSuffixResource();
            }
        """)

    addClass("""
            package com.adobe.cq.sightly;

            import aQute.bnd.annotation.ProviderType;
            import com.day.cq.wcm.api.WCMMode;
            import org.apache.sling.api.SlingHttpServletRequest;

            @ProviderType
            public class SightlyWCMMode {
                private final boolean isDisabled;
                private final boolean isPreview;
                private final boolean isAnalytics;
                private final boolean isReadOnly;
                private final boolean isEdit;
                private final boolean isDesign;
                private final SlingHttpServletRequest request;

                public SightlyWCMMode(SlingHttpServletRequest request) {
                    this.request = request;
                    this.isEdit = WCMMode.fromRequest(request) == WCMMode.EDIT;
                    this.isDesign = WCMMode.fromRequest(request) == WCMMode.DESIGN;
                    this.isDisabled = WCMMode.fromRequest(request) == WCMMode.DISABLED;
                    this.isPreview = WCMMode.fromRequest(request) == WCMMode.PREVIEW;
                    this.isAnalytics = WCMMode.fromRequest(request) == WCMMode.ANALYTICS;
                    this.isReadOnly = WCMMode.fromRequest(request) == WCMMode.READ_ONLY;
                }

                public boolean isDisabled() {
                    return this.isDisabled;
                }

                public boolean isPreview() {
                    return this.isPreview;
                }

                public boolean isAnalytics() {
                    return this.isAnalytics;
                }

                public boolean isReadOnly() {
                    return this.isReadOnly;
                }

                public boolean isEdit() {
                    return this.isEdit;
                }

                public boolean isDesign() {
                    return this.isDesign;
                }

                public String toString() {
                    return WCMMode.fromRequest(this.request).name();
                }
            }
        """)
  }

}
