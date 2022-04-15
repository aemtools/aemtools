package com.aemtools.ide.htl

import com.aemtools.lang.htl.highlight.HtlHighlighter
import com.aemtools.lang.htl.icons.HtlIcons
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Test for [HtlColorsAndFontsPage].
 *
 * @author Dmytro Primshyts
 */
class HtlColorsAndFontsPageTest {

  private val tested = HtlColorsAndFontsPage()

  @Test
  fun `getHighlighter should return HtlHighlighter`() {
    assertThat(tested.highlighter)
        .isInstanceOf(HtlHighlighter::class.java)
  }

  @Test
  fun `getAdditionalHighlightingTagToDescriptorMap should return 20 items`() {
    assertThat(tested.additionalHighlightingTagToDescriptorMap)
        .hasSize(20)
  }

  @Test
  fun `getIcon should return HTL icon`() {
    assertThat(tested.icon)
        .isEqualTo(HtlIcons.HTL_FILE_ICON)
  }

  @Test
  fun `getAttributeDescriptors should return 20 descriptors`() {
    assertThat(tested.attributeDescriptors)
        .hasSize(20)
  }

  @Test
  fun `getColorDescriptors should return empty array`() {
    assertThat(tested.colorDescriptors)
        .isEmpty()
  }

  @Test
  fun `getDisplayName should return correct name`() {
    assertThat(tested.displayName)
        .isEqualTo("HTML Markup Language (HTL)")
  }

  @Test
  fun `getDemoText should not be blank`() {
    assertThat(tested.demoText)
        .isNotBlank()
  }

}
