package com.aemtools.codeinsight.html.annotator

import com.aemtools.common.util.getHtmlFile
import com.aemtools.test.base.BaseLightTest
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import org.assertj.core.api.Assertions.assertThat

/**
 * Test for [HtlAttributesAnnotator].
 *
 * @author Dmytro Primshyts
 */
abstract class HtlAttributesAnnotatorTest : BaseLightTest() {

  fun testUnusedDataSlyUse() {
    myFixture.configureByText("test.html", """
            <div data-sly-use.<warning descr="null">bean</warning>=""></div>
        """)
    myFixture.testHighlighting()
  }

  fun testUnusedDataSlyTest() {
    myFixture.configureByText("test.html", """
            <div data-sly-test.<warning descr="null">test</warning>=""></div>
        """)
    myFixture.testHighlighting()
  }

  fun testUnusedDataSlyList() {
    myFixture.configureByText("test.html", """
            <div data-sly-list.<warning descr="null">model</warning>=""></div>
        """)
    myFixture.testHighlighting()
  }

  fun testUnusedDataSlyRepeat() {
    myFixture.configureByText("test.html", """
            <div data-sly-repeat.<warning descr="null">model</warning>=""></div>
        """)
    myFixture.testHighlighting()
  }

  fun testLocalUsageDetectorFindsUsedDataSlyUseVariable() {
    myFixture.configureByText("test.html", """
            <sly data-sly-use.clientLib="clientlib.html"></sly>
            <sly data-sly-call="$DOLLAR{clientLib.css @ categories='site'}"></sly>
        """)

    assertThat(HtlAttributeUsageDetector.isUsed(attribute("data-sly-use.clientLib")))
        .isTrue()
  }

  fun testLocalUsageDetectorKeepsUnusedDataSlyUseVariableUnused() {
    myFixture.configureByText("test.html", """
            <sly data-sly-use.clientLib="clientlib.html"></sly>
            <sly data-sly-call="$DOLLAR{other.css @ categories='site'}"></sly>
        """)

    assertThat(HtlAttributeUsageDetector.isUsed(attribute("data-sly-use.clientLib")))
        .isFalse()
  }

  fun testLocalUsageDetectorRespectsShadowedDeclarations() {
    myFixture.configureByText("test.html", """
            <sly data-sly-use.bean="first.html"></sly>
            <sly data-sly-use.bean="second.html"></sly>
            <sly data-sly-call="$DOLLAR{bean.template}"></sly>
        """)

    val declarations = attributes("data-sly-use.bean")
    assertThat(HtlAttributeUsageDetector.isUsed(declarations[0]))
        .isFalse()
    assertThat(HtlAttributeUsageDetector.isUsed(declarations[1]))
        .isTrue()
  }

  fun testLocalUsageDetectorIgnoresAccessIdentifierNames() {
    myFixture.configureByText("test.html", """
            <sly data-sly-use.template="unused.html"></sly>
            <sly data-sly-use.bean="used.html"></sly>
            <sly data-sly-call="$DOLLAR{bean.template}"></sly>
        """)

    assertThat(HtlAttributeUsageDetector.isUsed(attribute("data-sly-use.template")))
        .isFalse()
    assertThat(HtlAttributeUsageDetector.isUsed(attribute("data-sly-use.bean")))
        .isTrue()
  }

  private fun attribute(name: String): XmlAttribute {
    return attributes(name).first()
  }

  private fun attributes(name: String): List<XmlAttribute> {
    val htmlFile = myFixture.file.getHtmlFile()
    return PsiTreeUtil.findChildrenOfType(htmlFile, XmlAttribute::class.java)
        .filter { it.name == name }
  }

}
