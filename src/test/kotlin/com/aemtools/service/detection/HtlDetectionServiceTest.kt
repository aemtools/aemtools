package com.aemtools.service.detection

import com.aemtools.blocks.base.BaseLightTest
import com.aemtools.constant.const.JCR_ROOT
import com.aemtools.settings.HtlRootDirectories
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * @author Dmytro Troynikov
 */
class HtlDetectionServiceTest : BaseLightTest(false) {

    @Test
    fun `test isHtlFile should return false for non html file`() {
        assertThat(HtlDetectionService.isHtlFile("blah.xml"))
                .isFalse()
    }

    @Test
    fun `test isHtlFile should return true for all html files in tests`() {
        assertThat(HtlDetectionService.isHtlFile("test.html"))
                .isTrue()
    }

    @Test
    fun `test isHtlFile should return true for html file in jcr_root`() {
        HtlDetectionService.markAllInTest = false

        assertThat(HtlDetectionService.isHtlFile("/$JCR_ROOT/test.html"))
                .isTrue()
    }

    @Test
    fun `test isHtlRootDirectory should be false if no jcr_root present in path`() {
        assertThat(HtlDetectionService.isHtlRootDirectory("/some/path"))
                .isFalse()
    }

    @Test
    fun `test isHtlRootDirectory should be true for jcr_root containing path`() {
        assertThat(HtlDetectionService.isHtlRootDirectory("/content/jcr_root"))
                .isTrue()
    }

    @Test
    fun `test isUnderHtlRoot without jcr_root should be false`() {
        assertThat(HtlDetectionService.isUnderHtlRoot("/content/test.html"))
                .isFalse()
    }

    @Test
    fun `test isUnderHtlRoot with jcr_root should be true`() {
        assertThat(HtlDetectionService.isUnderHtlRoot("/jcr_root/test.html"))
                .isTrue()
    }

    fun `test isHtlFile should return true for htl root directory`() {
        HtlRootDirectories.getInstance()?.let {
            it.addRoot("/content/root1")
            it.addRoot("/content/root2")
        } ?: throw AssertionError("Unable to configure htl roots")

        HtlDetectionService.markAllInTest = false

        assertThat(HtlDetectionService.isHtlFile("/content/root1/test.html"))
                .isTrue()
        assertThat(HtlDetectionService.isHtlFile("/content/root2/test.html"))
                .isTrue()
        assertThat(HtlDetectionService.isHtlFile("/content/root3/test.html"))
                .isFalse()
    }

    fun `test isUnderHtlRoot should return true for htl root directory`() {
        HtlRootDirectories.getInstance()?.let {
            it.addRoot("/content/root1")
            it.addRoot("/content/root2")
        } ?: throw AssertionError("Unable to configure htl roots")

        HtlDetectionService.markAllInTest = false

        assertThat(HtlDetectionService.isUnderHtlRoot("/content/root1/test"))
                .isTrue()
        assertThat(HtlDetectionService.isUnderHtlRoot("/content/root2/test"))
                .isTrue()
        assertThat(HtlDetectionService.isUnderHtlRoot("/content/root3/test"))
                .isFalse()
    }

}
