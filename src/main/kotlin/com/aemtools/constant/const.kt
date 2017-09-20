package com.aemtools.constant

/**
 * @author Dmytro_Troynikov
 */
object const {

  val AEMTOOLS: String = "aemtools"

  val DOLLAR: String = "$"

  val JCR_ROOT: String = "jcr_root"

  val JCR_ROOT_SEPARATED: String = "/$JCR_ROOT/"

  val IDEA_STRING_CARET_PLACEHOLDER: String = "IntellijIdeaRulezzz"

  val XTYPE: String = "xtype"

  val JCR_PRIMARY_TYPE: String = "jcr:primaryType"
  val JCR_TITLE: String = "jcr:title"
  val JCR_DESCRIPTION: String = "jcr:description"
  val SLING_RESOURCE_SUPER_TYPE: String = "sling:resourceSuperType"

  val DIALOG_XML: String = "dialog.xml"

  val CQ_EDITCONFIG_XML: String = "_cq_editConfig.xml"

  val REP_POLICY: String = "_rep_policy.xml"

  val SLY_TAG: String = "sly"

  /**
   * Constants specific for `.content.xml` files of `cq:Component` type.
   */
  object aem_component_declaration {

    val COMPONENT_GROUP: String = "componentGroup"

    val IS_CONTAINER: String = "cq:isContainer"

    val CQ_ICON: String = "cq:icon"
  }

  /**
   * Htl constants.
   */
  object htl {
    private val HTL_PREFIX = "data-sly-"
    val DATA_SLY_USE = "${HTL_PREFIX}use"
    val DATA_SLY_TEST = "${HTL_PREFIX}test"
    val DATA_SLY_REPEAT = "${HTL_PREFIX}repeat"
    val DATA_SLY_LIST = "${HTL_PREFIX}list"
    val DATA_SLY_TEMPLATE = "${HTL_PREFIX}template"
    val DATA_SLY_ATTRIBUTE = "${HTL_PREFIX}attribute"
    val DATA_SLY_ELEMENT = "${HTL_PREFIX}element"
    val DATA_SLY_CALL = "${HTL_PREFIX}call"
    val DATA_SLY_INCLUDE = "${HTL_PREFIX}include"
    val DATA_SLY_UNWRAP = "${HTL_PREFIX}unwrap"
    val DATA_SLY_TEXT = "${HTL_PREFIX}text"
    val DATA_SLY_RESOURCE = "${HTL_PREFIX}resource"
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
      val CONTEXT = "context"
      val RESOURCE_TYPE = "resourceType"
      val I18N = "i18n"
    }
  }

  /**
   * Java related constants.
   */
  object java {
    val ITERABLE = "java.lang.Iterable"
    val ITERATOR = "java.util.Iterator"
    val MAP = "java.util.Map"

    val USE_INTERFACE = "io.sightly.java.api.Use"
    val WCM_USE_CLASS = "com.adobe.cq.sightly.WCMUse"
    val POJO_USE = "org.apache.sling.scripting.sightly.pojo.Use"

    val SLING_MODEL = "org.apache.sling.models.annotations.Model"

    val VALUE_MAP = "org.apache.sling.api.resource.ValueMap"

    val FELIX_SERVICE_ANNOTATION = "org.apache.felix.scr.annotations.Service"
    val SLING_SERVLET_ANNOTATION = "org.apache.felix.scr.annotations.sling.SlingServlet"
    val SLING_FILTER_ANNOTATION = "org.apache.felix.scr.annotations.sling.SlingFilter"
  }

  /**
   * Xml related constants.
   */
  object xml {
    val XML_ATTRIBUTE_NAME: String = "XML_NAME"
    val XML_ATTRIBUTE_VALUE: String = "XML_ATTRIBUTE_VALUE_TOKEN"
    val XML_TAG_NAME: String = "XML_TAG_NAME"

    val SLING_OSGI_CONFIG: String = "sling:OsgiConfig"
    val CQ_COMPONENT: String = "cq:Component"
    val CQ_DIALOG: String = "cq:Dialog"
    val JCR_PRIMARY_TYPE_OSGI_CONFIG: String = "$JCR_PRIMARY_TYPE=\"$SLING_OSGI_CONFIG\""
    val JCR_PRIMARY_TYPE_CQ_COMPONENT: String = "$JCR_PRIMARY_TYPE=\"$CQ_COMPONENT\""
    val JCR_PRIMARY_TYPE_CQ_DIALOG: String = "$JCR_PRIMARY_TYPE=\"$CQ_DIALOG\""
    val JCR_LANGUAGE: String = "jcr:language"
    val SLING_KEY: String = "sling:key"
    val SLING_MESSAGE: String = "sling:message"
    val JCR_MIXIN_TYPES: String = "jcr:mixinTypes"
    val LANGUAGE_MIXIN: String = "mix:language"
  }

}
