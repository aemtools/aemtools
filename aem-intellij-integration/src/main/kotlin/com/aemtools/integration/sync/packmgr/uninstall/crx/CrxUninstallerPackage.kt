package com.aemtools.integration.sync.packmgr.uninstall.crx

import com.aemtools.integration.sync.packmgr.uninstall.exceptions.PackageNotUninstalledException
import io.wcm.tooling.commons.packmgr.Logger
import io.wcm.tooling.commons.packmgr.PackageManagerHelper
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.jdom2.xpath.XPathFactory

/**
 * @author Dmytro Liakhov
 */
class CrxUninstallerPackage(private val url: String) {

  fun uninstallPackage(packageFileName: String,
                       pkgmgr: PackageManagerHelper,
                       httpClient: CloseableHttpClient?,
                       log: Logger) {
    log.info("Installing $packageFileName...")

    val get = HttpGet("$url.jsp?cmd=rm&name=$packageFileName")

    val xmlResponse = pkgmgr.executePackageManagerMethodXml(httpClient, get)
    val xpath = XPathFactory.instance().compile(
        """/crx/response/status[@code="200" and .="ok"]""")

    val result = xpath.evaluate(xmlResponse)
    if (result == null) {
      throw PackageNotUninstalledException("Package $packageFileName not uninstalled!")
    } else {
      log.info("Package $packageFileName was uninstalled!")
    }
  }

}
