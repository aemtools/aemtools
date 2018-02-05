package com.aemtools.sync.settings

import com.aemtools.sync.util.SyncConstants
import com.aemtools.test.base.BaseLightTest
import com.intellij.testFramework.registerServiceInstance
import junit.framework.TestCase
import org.junit.Test

class SyncSettingsTest: BaseLightTest(false) {

  private lateinit var syncSettings: SyncSettings

  override fun setUp() {
    super.setUp()
    syncSettings = SyncSettings(project)
    val instanceInfo = AemToolsProjectConfiguration()
    project.registerServiceInstance(AemToolsProjectConfiguration::class.java, instanceInfo)
  }

  @Test
  fun `test settings sync id`() {
    assertEquals(SyncConstants.SETTINGS_ID, syncSettings.id)
  }

  @Test
  fun `test settings sync display name`() {
    assertEquals(SyncConstants.DISPLAY_NAME_SETTINGS, syncSettings.displayName)
  }

  @Test
  fun `test settings sync help topic`() {
    assertEquals(SyncConstants.SETTINGS_ID, syncSettings.helpTopic)
  }

  @Test
  fun `test apply settings`() {

    TestCase.assertNotNull(syncSettings)

    syncSettings.createComponent()
    syncSettings.reset()
  }

}
