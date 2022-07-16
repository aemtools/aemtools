package com.aemtools.test.fixture

import com.aemtools.lang.settings.AemProjectSettings
import com.aemtools.lang.settings.model.HtlVersion
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture

/**
 * @author Kostiantyn Diachenko
 */
interface HtlVersioningFixtureMixin {
  fun IdeaProjectTestFixture.setHtlVersion(htlVersion: HtlVersion) {
    AemProjectSettings.getInstance(this.project).htlVersion = htlVersion
  }
}
