package com.aemtools.common.constant

/**
 * @author Dmytro Primshyts
 */
object const {

  const val AEMTOOLS: String = "aemtools"

  const val DOLLAR: String = "$"

  const val JCR_ROOT: String = "jcr_root"

  const val JCR_ROOT_SEPARATED: String = "/$JCR_ROOT/"

  const val IDEA_STRING_CARET_PLACEHOLDER: String = "IntellijIdeaRulezzz"

  const val XTYPE: String = "xtype"

  const val JCR_PRIMARY_TYPE: String = "jcr:primaryType"
  const val SLING_RESOURCE_TYPE: String = "sling:resourceType"
  const val JCR_TITLE: String = "jcr:title"
  const val JCR_DESCRIPTION: String = "jcr:description"
  const val SLING_RESOURCE_SUPER_TYPE: String = "sling:resourceSuperType"

  const val DIALOG_XML: String = "dialog.xml"

  const val CQ_EDITCONFIG_XML: String = "_cq_editConfig.xml"

  const val REP_POLICY: String = "_rep_policy.xml"

  const val SLY_TAG: String = "sly"

  const val CLIENTLIB_TEMPLATE: String = "/libs/granite/sightly/templates/clientlib.html"

  /**
   * Constants specific for `.content.xml` files of `cq:Component` type.
   */
  object aem_component_declaration {

    const val COMPONENT_GROUP: String = "componentGroup"

    const val IS_CONTAINER: String = "cq:isContainer"

    const val CQ_ICON: String = "cq:icon"
  }

  /**
   * Htl constants.
   */
  object htl {
    private const val HTL_PREFIX = "data-sly-"
    const val DATA_SLY_USE = "${HTL_PREFIX}use"
    const val DATA_SLY_TEST = "${HTL_PREFIX}test"
    const val DATA_SLY_REPEAT = "${HTL_PREFIX}repeat"
    const val DATA_SLY_LIST = "${HTL_PREFIX}list"
    const val DATA_SLY_TEMPLATE = "${HTL_PREFIX}template"
    const val DATA_SLY_ATTRIBUTE = "${HTL_PREFIX}attribute"
    const val DATA_SLY_ELEMENT = "${HTL_PREFIX}element"
    const val DATA_SLY_CALL = "${HTL_PREFIX}call"
    const val DATA_SLY_INCLUDE = "${HTL_PREFIX}include"
    const val DATA_SLY_UNWRAP = "${HTL_PREFIX}unwrap"
    const val DATA_SLY_TEXT = "${HTL_PREFIX}text"
    const val DATA_SLY_RESOURCE = "${HTL_PREFIX}resource"
    val HTL_ATTRIBUTES = listOf(
        DATA_SLY_USE,
        DATA_SLY_TEST,
        DATA_SLY_REPEAT,
        DATA_SLY_LIST,
        DATA_SLY_TEMPLATE,
        DATA_SLY_ATTRIBUTE,
        DATA_SLY_ELEMENT,
        DATA_SLY_CALL,
        DATA_SLY_INCLUDE,
        DATA_SLY_UNWRAP,
        DATA_SLY_TEXT,
        DATA_SLY_RESOURCE
    )

    val DECLARATION_ATTRIBUTES = HTL_ATTRIBUTES - listOf(
        DATA_SLY_UNWRAP,
        DATA_SLY_CALL,
        DATA_SLY_TEXT,
        DATA_SLY_RESOURCE,
        DATA_SLY_INCLUDE
    )

    val SINGLE_ATTRIBUTES = HTL_ATTRIBUTES - DECLARATION_ATTRIBUTES

    // Attributes, which can meet in tag only one time
    val UNIQUE_HTL_ATTRIBUTES = listOf(
        DATA_SLY_LIST,
        DATA_SLY_UNWRAP,
        DATA_SLY_REPEAT
    )

    /**
     * Options constants.
     */
    object options {
      const val CONTEXT = "context"
      const val RESOURCE_TYPE = "resourceType"
      const val I18N = "i18n"

      const val CATEGORIES = "categories"
    }
  }

  /**
   * HTML related constants.
   */
  object html {

    private val WINDOW_EVENT_ATTRIBUTES = listOf(
        "onafterprint",
        "onbeforeprint",
        "onbeforeunload",
        "onerror",
        "onhashchange",
        "onload",
        "onmessage",
        "onoffline",
        "ononline",
        "onpagehide",
        "onpageshow",
        "onpopstate",
        "onresize",
        "onstorage",
        "onunload"
    )

    private val FORM_EVENTS = listOf(
        "onblur",
        "onchange",
        "oncontextmenu",
        "onfocus",
        "oninput",
        "oninvalid",
        "onreset",
        "onsearch",
        "onselect",
        "onsubmit"
    )

    private val KEYBOARD_EVENTS = listOf(
        "onkeydown",
        "onkeypress",
        "onkeyup"
    )

    private val MOUSE_EVENTS = listOf(
        "onclick",
        "ondblclick",
        "onmousedown",
        "onmousemove",
        "onmouseout",
        "onmouseover",
        "onmouseup",
        "onmousewheel",
        "onwheel"
    )

    private val DRAG_EVENTS = listOf(
        "ondrag",
        "ondragend",
        "ondragenter",
        "ondragleave",
        "ondragover",
        "ondragstart",
        "ondrop",
        "onscroll"
    )

    private val CLIPBOARD_EVENTS = listOf(
        "oncopy",
        "oncut",
        "onpaste"
    )

    private val MEDIA_EVENTS = listOf(
        "onabort",
        "oncanplay",
        "oncanplaythrough",
        "oncuechange",
        "ondurationchange",
        "onemptied",
        "onended",
        "onerror",
        "onloadeddata",
        "onloadedmetadata",
        "onloadstart",
        "onpause",
        "onplay",
        "onplaying",
        "onprogress",
        "onratechange",
        "onseeked",
        "onseeking",
        "onstalled",
        "onsuspend",
        "ontimeupdate",
        "onvolumechange",
        "onwaiting"
    )

    private val MISC_EVENTS = listOf(
        "onshow",
        "ontoggle"
    )

    val JS_ATTRIBUTES = hashSetOf(
        *(WINDOW_EVENT_ATTRIBUTES
            + FORM_EVENTS
            + KEYBOARD_EVENTS
            + MOUSE_EVENTS
            + DRAG_EVENTS
            + CLIPBOARD_EVENTS
            + MEDIA_EVENTS
            + MISC_EVENTS).toTypedArray()
    )

  }

  /**
   * Java related constants.
   */
  object java {
    const val ITERABLE = "java.lang.Iterable"
    const val ITERATOR = "java.util.Iterator"
    const val MAP = "java.util.Map"

    const val USE_INTERFACE = "io.sightly.java.api.Use"
    const val WCM_USE_CLASS = "com.adobe.cq.sightly.WCMUse"
    const val POJO_USE = "org.apache.sling.scripting.sightly.pojo.Use"
    const val WCM_API_COMPONENT = "com.day.cq.wcm.api.components.Component"

    const val SLING_MODEL = "org.apache.sling.models.annotations.Model"
    const val OPTIONAL = "org.apache.sling.models.annotations.Optional"

    val VALUE_MAP = "org.apache.sling.api.resource.ValueMap"

    const val FELIX_SERVICE_ANNOTATION = "org.apache.felix.scr.annotations.Service"
    const val FELIX_COMPONENT_ANNOTATION = "org.apache.felix.scr.annotations.Component"
    const val SLING_SERVLET_ANNOTATION = "org.apache.felix.scr.annotations.sling.SlingServlet"
    const val SLING_FILTER_ANNOTATION = "org.apache.felix.scr.annotations.sling.SlingFilter"
    const val SLING_HEALTH_CHECK_ANNOTATION = "org.apache.sling.hc.annotations.SlingHealthCheck"

    const val DS_COMPONENT_ANNOTATION = "org.osgi.service.component.annotations.Component"
    const val DS_DESIGNATE_ANNOTATION = "org.osgi.service.metatype.annotations.Designate"
    const val DS_ATTRIBUTE_DEFINITION_ANNOTATION = "org.osgi.service.metatype.annotations.AttributeDefinition"
    const val DS_OBJECT_CLASS_DEFINITION_ANNOTATION = "org.osgi.service.metatype.annotations.ObjectClassDefinition"

    const val FELIX_PROPERTY_ANNOTATION = "org.apache.felix.scr.annotations.Property"
    const val FELIX_PROPERTIES_ANNOTATION = "org.apache.felix.scr.annotations.Properties"
  }

  object osgi {
    const val DESIGNATE_OCD_ANNOTATION_ATTRIBUTE = "ocd"
  }

  /**
   * Xml related constants.
   */
  object xml {
    const val XML_ATTRIBUTE_NAME: String = "XML_NAME"
    const val XML_ATTRIBUTE_VALUE: String = "XML_ATTRIBUTE_VALUE_TOKEN"
    const val XML_TAG_NAME: String = "XML_TAG_NAME"

    const val SLING_OSGI_CONFIG: String = "sling:OsgiConfig"
    const val CQ_COMPONENT: String = "cq:Component"
    const val CQ_DIALOG: String = "cq:Dialog"
    const val CQ_CLIENTLIBRARY_FOLDER = "cq:ClientLibraryFolder"
    const val JCR_PRIMARY_TYPE_OSGI_CONFIG: String = "$JCR_PRIMARY_TYPE=\"$SLING_OSGI_CONFIG\""
    const val JCR_PRIMARY_TYPE_CQ_COMPONENT: String = "$JCR_PRIMARY_TYPE=\"$CQ_COMPONENT\""
    const val JCR_PRIMARY_TYPE_CQ_DIALOG: String = "$JCR_PRIMARY_TYPE=\"$CQ_DIALOG\""
    const val JCR_LANGUAGE: String = "jcr:language"
    const val SLING_KEY: String = "sling:key"
    const val SLING_MESSAGE: String = "sling:message"
    const val JCR_MIXIN_TYPES: String = "jcr:mixinTypes"
    const val LANGUAGE_MIXIN: String = "mix:language"
  }

}
