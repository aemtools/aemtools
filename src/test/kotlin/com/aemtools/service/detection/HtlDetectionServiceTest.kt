package com.aemtools.service.detection

import com.aemtools.blocks.base.BaseLightTest
import com.aemtools.constant.const.JCR_ROOT
import com.aemtools.settings.HtlRootDirectories
import org.assertj.core.api.Assertions.assertThat

/**
 * @author Dmytro Troynikov
 */
class HtlDetectionServiceTest : BaseLightTest(false) {

  fun `test isHtlFile should return false for non html file`() {
    assertThat(HtlDetectionService.isHtlFile("blah.xml", project))
        .isFalse()
  }

  fun `test isHtlFile should return true for all html files in tests`() {
    assertThat(HtlDetectionService.isHtlFile("test.html", project))
        .isTrue()
  }

  fun `test isHtlFile should return true for html file in jcr_root`() {
    HtlDetectionService.markAllInTest = false

    assertThat(HtlDetectionService.isHtlFile("/$JCR_ROOT/test.html", project))
        .isTrue()
    HtlDetectionService.markAllInTest = true
  }

  fun `test isHtlRootDirectory should be false if no jcr_root present in path`() {
    assertThat(HtlDetectionService.isHtlRootDirectory("/some/path", project))
        .isFalse()
  }

  fun `test isHtlRootDirectory should be true for jcr_root containing path`() {
    assertThat(HtlDetectionService.isHtlRootDirectory("/content/jcr_root", project))
        .isTrue()
  }

  fun `test isUnderHtlRoot without jcr_root should be false`() {
    assertThat(HtlDetectionService.isUnderHtlRoot("/content/test.html", project))
        .isFalse()
  }

  fun `test isUnderHtlRoot with jcr_root should be true`() {
    assertThat(HtlDetectionService.isUnderHtlRoot("/jcr_root/test.html", project))
        .isTrue()
  }

  fun `test isUnderHtlRoot should return false for htl root`() {
    HtlRootDirectories.getInstance(project)?.let {
      it.addRoot("/content/root")
    } ?: throw AssertionError("Unable to configure htl roots")

    assertThat(HtlDetectionService.isUnderHtlRoot("/content/root", project))
        .isFalse()
  }

  fun `test isHtlFile should return true for htl root directory`() {
    HtlRootDirectories.getInstance(project)?.let {
      it.addRoot("/content/root1")
      it.addRoot("/content/root2")
    } ?: throw AssertionError("Unable to configure htl roots")

    HtlDetectionService.markAllInTest = false
    assertThat(HtlDetectionService.isHtlFile("/content/root1/test.html", project))
        .isTrue()
    assertThat(HtlDetectionService.isHtlFile("/content/root2/test.html", project))
        .isTrue()
    assertThat(HtlDetectionService.isHtlFile("/content/root3/test.html", project))
        .isFalse()
    HtlDetectionService.markAllInTest = true
  }

  fun `test isUnderHtlRoot should return true for htl root directory`() {
    HtlRootDirectories.getInstance(project)?.let {
      it.addRoot("/content/root1")
      it.addRoot("/content/root2")
    } ?: throw AssertionError("Unable to configure htl roots")

    HtlDetectionService.markAllInTest = false
    assertThat(HtlDetectionService.isUnderHtlRoot("/content/root1/test", project))
        .isTrue()
    assertThat(HtlDetectionService.isUnderHtlRoot("/content/root2/test", project))
        .isTrue()
    assertThat(HtlDetectionService.isUnderHtlRoot("/content/root3/test", project))
        .isFalse()
    HtlDetectionService.markAllInTest = true
  }

  override fun setUp() {
    super.setUp()
    HtlRootDirectories.getInstance(project)?.let {
      it.directories.clear()
    }
  }
}
