package com.aemtools.sync.action

import com.aemtools.sync.logger.CRXStatusLogger
import com.aemtools.sync.packmgr.uninstall.PackageUninstaller
import com.aemtools.sync.util.SyncConstants
import com.aemtools.sync.util.getPathOnAEMInstance
import com.aemtools.sync.util.isUnderAEMRoot
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import io.wcm.tooling.commons.contentpackagebuilder.ContentPackageBuilder
import io.wcm.tooling.commons.packmgr.PackageManagerProperties
import io.wcm.tooling.commons.packmgr.install.PackageFile
import io.wcm.tooling.commons.packmgr.install.PackageInstaller
import io.wcm.tooling.commons.packmgr.install.VendorInstallerFactory
import java.io.File

/**
 * @author Dmytro Liakhov
 */
class ExportFileToInstance : AnAction() {

  override fun update(event: AnActionEvent) {
    val file = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
            ?.firstOrNull()

    if (file != null && file.isUnderAEMRoot()) {
      event.presentation.isEnabledAndVisible = true
    }
  }

  override fun actionPerformed(event: AnActionEvent) {
    val zipFile = File(SyncConstants.TMP_FILE_PACKAGE_NAME)
    val virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
            ?.firstOrNull()
    if (virtualFile == null) {
      return
    }
    val pathOnAEMInstance = virtualFile.getPathOnAEMInstance()
    val rootPath = pathOnAEMInstance.substringBeforeLast("/")

    println(virtualFile.path)
    println(pathOnAEMInstance)
    println(rootPath)

    val builder = ContentPackageBuilder()
            .name(SyncConstants.TMP_NAME)
            .group(SyncConstants.TMP_GROUP)
            .rootPath(rootPath)

    builder.build(zipFile).use { contentPackage ->
      val myFile = File(virtualFile.path)
      contentPackage.addFile(pathOnAEMInstance, myFile)
    }

    val props = PackageManagerProperties()
    props.userId = "admin"
    props.password = "admin"
    props.packageManagerUrl = "http://localhost:4502" + VendorInstallerFactory.CRX_URL

    val packageFile = PackageFile()
    packageFile.file = zipFile
    packageFile.setForce(true)

    val logger = CRXStatusLogger()
    val packageInstaller = PackageInstaller(props, logger)
    packageInstaller.installFile(packageFile)

    val uninstallerPackage = PackageUninstaller(props, logger)
    uninstallerPackage.uninstallFile(SyncConstants.TMP_NAME)
  }

}
