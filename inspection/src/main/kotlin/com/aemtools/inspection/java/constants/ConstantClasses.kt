package com.aemtools.inspection.java.constants

/**
 * @author Dmytro Troynikov
 */
object ConstantClasses {

  val JACKRABIT_JCR_CONSTANTS: String = "org.apache.jackrabbit.JcrConstants"
  val CQ_COMMONS_JCR_JCR_CONSTANTS: String = "com.day.cq.commons.jcr.JcrConstants"
  val DAY_CRX_JCR_CONSTANTS: String = "com.day.crx.JcrConstants"
  val DAM_CONSTANTS: String = "com.day.cq.dam.api.DamConstants"
  val NAME_CONSTANTS: String = "com.day.cq.wcm.api.NameConstants"
  val CONFIGURATION_CONSTANTS: String = "com.day.cq.wcm.webservicesupport.ConfigurationConstants"
  val JCR_RESOURCE_CONSTANTS: String = "org.apache.sling.jcr.resource.JcrResourceConstants"
  val TAG_CONSTANTS: String = "com.day.cq.tagging.TagConstants"
  val REPLICATION_STATUS: String = "com.day.cq.replication.ReplicationStatus"
  val JCR_PACKAGE: String = "org.apache.jackrabbit.vault.packaging.JcrPackage"
  val JCR_PACKAGE_DEFINITION: String = "org.apache.jackrabbit.vault.packaging.JcrPackageDefinition"
  val RESOURCE_RESOLVER_FACTORY: String = "org.apache.sling.api.resource.ResourceResolverFactory"
  val WORKFLOW_EVENT: String = "com.adobe.granite.workflow.event.WorkflowEvent"
  val SLING_CONSTANTS: String = "org.apache.sling.api.SlingConstants"
  val HTTP_CONSTANTS: String = "org.apache.sling.api.servlets.HttpConstants"
  val OSGI_CONSTANTS: String = "org.osgi.framework.Constants"
  val ENGINE_CONSTANTS: String = "org.apache.sling.engine.EngineConstants"

  /**
   * List of all OOTB constant holder classes.
   */
  val ALL: List<String> = listOf(
      JACKRABIT_JCR_CONSTANTS,
      CQ_COMMONS_JCR_JCR_CONSTANTS,
      DAY_CRX_JCR_CONSTANTS,
      DAM_CONSTANTS,
      NAME_CONSTANTS,
      CONFIGURATION_CONSTANTS,
      JCR_RESOURCE_CONSTANTS,
      TAG_CONSTANTS,
      REPLICATION_STATUS,
      JCR_PACKAGE,
      JCR_PACKAGE_DEFINITION,
      RESOURCE_RESOLVER_FACTORY,
      WORKFLOW_EVENT,
      SLING_CONSTANTS,
      HTTP_CONSTANTS,
      OSGI_CONSTANTS,
      ENGINE_CONSTANTS
  )

  /**
   * List of too "common" or vague constants that are highly likely to be
   * used in some unrelated context.
   */
  val EXLUSIONS: Set<String> = setOf(
      // com.day.cq.dam.api.DamConstants
      "/apps",
      "/libs",
      "/var/dam",
      "/content/dam",
      "renditions",
      "metadata",
      "modifier",
      "original",
      "currentOriginal",
      "subassets",
      "image/png",
      "onTime",
      "offTime",
      "dc:title",
      "dam",
      "asset",
      "externalSystem",
      "license",
      "rendition",
      "subasset",
      "assetExpired",
      "assetExpiring",
      "processingProfile",
      "metadataProfile",
      "videoProfile",
      "imageProfile",
      "folderThumbnail",
      "downloadUrl",
      "approved",
      "pending",
      "rejected",
      "pages",

      // com.day.cq.wcm.api.NameConstants
      "dialog",
      "icon.png",
      "thumbnail.png",
      "ranking",
      "sitePath",
      "params",
      "virtual",
      "className",
      "deleted",
      "deletedBy",
      "propertyName",
      "accept",
      "groups",
      "parameters",
      "shortTitle",
      "pageTitle",
      "navTitle",
      "hideInNav",
      "onTime",
      "offTime",
      "vanityUrl",

      // com.day.cq.tagging.TagConstants
      "nirvana",
      ":",
      "/",
      " / ",
      "default",
      "default:",

      // com.day.cq.replication.ReplicationStatus
      "cq:lastPublished",
      "cq:lastPublishedBy",

      // org.apache.jackrabbit.vault.packaging.JcrPackage
      "application/zip",

      // org.apache.jackrabbit.vault.packaging.JcrPackageDefinition
      "lastUnpacked",
      "lastUnpackedBy",
      "version",
      "lastWrapped",
      "lastWrappedBy",
      "buildCount",
      "name",
      "group",
      "requiresRoot",
      "requiresRestart",
      "dependencies",
      "subPackages",
      "lastUnwrapped",
      "lastUnwrappedBy",
      "acHandling",
      "cndPattern",
      "filter",
      "root",
      "mode",
      "rules",
      "type",
      "pattern",

      // com.adobe.granite.workflow.event.WorkflowEvent
      "TimeStamp",
      "User",
      "Delagatee",
      "JobFailed",

      // org.apache.sling.api.SlingConstants
      "sling",
      "path",
      "userid",

      // org.osgi.framework.Constants
      "processor",
      "osname",
      "osversion",
      "language",
      "singleton",
      "always",
      "never",
      "version",
      "resolution",
      "mandatory",
      "optional",
      "uses",
      "include",
      "exclude",
      "visibility",
      "private",
      "extension",
      "framework",
      "lazy",
      "osgi",
      "onFirstInit",
      "boot",
      "ext",
      "app",
      "framework",
      "objectClass",

      // org.apache.sling.engine.EngineConstants
      "filter.scope",
      "COMPONENT",
      "ERROR",
      "INCLUDE",
      "FORWARD",
      "REQUEST"
  )

}
