package com.aemtools.sync.packmgr.uninstall

import com.aemtools.sync.packmgr.uninstall.crx.CrxUninstallerPackage
import io.wcm.tooling.commons.packmgr.Logger
import io.wcm.tooling.commons.packmgr.PackageManagerException
import io.wcm.tooling.commons.packmgr.PackageManagerHelper
import io.wcm.tooling.commons.packmgr.PackageManagerProperties
import java.io.IOException

/**
 * @author Dmytro Liakhov
 */
class PackageUninstaller (private val props: PackageManagerProperties,
                          private val log: Logger) {
  val pkgmgr: PackageManagerHelper

  init {
    pkgmgr = PackageManagerHelper(props, log)
  }

  /**
   * Uninstall package via package manager.
   * @param packageFileName AEM content package
   */
  fun uninstallFile(packageFileName: String) {
    try {
      pkgmgr.httpClient.use { httpClient ->

        // before install: if bundles are still stopping/starting, wait for completion
        pkgmgr.waitForBundlesActivation(httpClient)

        log.info("Delete " + packageFileName + " from " + props.packageManagerUrl)

        val uninstaller = CrxUninstallerPackage(props.packageManagerUrl)
        uninstaller.uninstallPackage(packageFileName, pkgmgr, httpClient, log)
      }
    } catch (ex: IOException) {
      throw PackageManagerException("Install operation failed.", ex)
    }
  }

}