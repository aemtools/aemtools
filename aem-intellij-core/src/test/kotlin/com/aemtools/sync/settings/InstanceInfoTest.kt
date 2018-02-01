package com.aemtools.sync.settings

import com.aemtools.sync.util.SyncConstants
import com.aemtools.test.base.BaseLightTest
import com.intellij.testFramework.registerServiceInstance

/**
 * @author Dmytro Liakhov
 */
class InstanceInfoTest : BaseLightTest(false) {

  private lateinit var instanceInfo: InstanceInfo

  override fun setUp() {
    super.setUp()
    instanceInfo = InstanceInfo()
    project.registerServiceInstance(InstanceInfo::class.java, instanceInfo)
  }

  fun `test should return default instance info when it is opened first time`() {
    val instance = InstanceInfo.getInstance(project)

    val defaultInstance = InstanceInfo()
    defaultInstance.enabled = false
    defaultInstance.login = SyncConstants.DEFAULT_LOGIN
    defaultInstance.password = SyncConstants.DEFAULT_PASSWORD
    defaultInstance.url = SyncConstants.DEFAULT_URL_INSTANCE

    assertEquals(defaultInstance, instance)
  }

  fun `test should return saved instance info`() {
    val instanceInfo = InstanceInfo.getInstance(project)
    val savedInstanceInfo = getInstanceInfo()
    instanceInfo.loadState(savedInstanceInfo)

    assertEquals(savedInstanceInfo, instanceInfo)
  }

  fun `test should return saved instance with missing fields`() {
    val instanceInfo = InstanceInfo.getInstance(project)
    val instanceInfoWithEmptyFields = getInstanceInfoWithEmptyFields()
    instanceInfo.loadState(instanceInfoWithEmptyFields)

    assertEquals(instanceInfoWithEmptyFields, instanceInfo)
  }

  fun `test should getState method return this object`() {
    val instanceInfo = InstanceInfo.getInstance(project)
    val thisInstanceInfo = instanceInfo.state

    assertTrue(instanceInfo === thisInstanceInfo)
  }

  private fun getInstanceInfo(): InstanceInfo {
    val instanceInfo = InstanceInfo()
    instanceInfo.apply {
      url = "http://example.com"
      password = "42"
      login = "dev"
      enabled = true
    }
    return instanceInfo
  }

  private fun getInstanceInfoWithEmptyFields(): InstanceInfo {
    val instanceInfo = InstanceInfo()
    instanceInfo.apply {
      url = ""
      password = "42"
      login = ""
      enabled = true
    }
    return instanceInfo
  }

}