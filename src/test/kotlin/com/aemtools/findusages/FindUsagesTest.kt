package com.aemtools.findusages

import com.aemtools.blocks.base.BaseLightTest
import com.aemtools.completion.util.findParentByType
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.xml.XmlAttribute
import org.assertj.core.api.Assertions.assertThat

/**
 * @author Dmytro Troynikov
 */
class FindUsagesTest : BaseLightTest() {

  fun testFindUsageOfPropertyMethod() = fileCase {
    addHtml("test.html", """
            <div data-sly-use.bean='com.test.Bean'>
                $DOLLAR{bean.property}
                $DOLLAR{bean.getProperty}
            </div>
        """)
    addClass("Bean", """
            package com.test;

            public class Bean {
                public String ${CARET}getProperty() {
                    return "";
                }
            }
        """)
    verify {
      val elementUnderCaret = elementUnderCaret()
      val element = elementUnderCaret.findParentByType(PsiMethod::class.java)
      val usages = myFixture.findUsages(element as PsiElement)

      assertThat(usages)
          .isNotNull

      val holders = usages.map { it.element?.text }
          .filterNotNull()

      assertContainsElements(holders, listOf(
          "bean.property",
          "bean.getProperty"
      ))
    }
  }

  fun testFindUsagesOfUseVariable() = fileCase {
    addHtml("test.html", """
            <div ${CARET}data-sly-use.bean="">
                $DOLLAR{bean}
            </div>
        """)
    verify {
      val element = elementUnderCaret()
      val attr = element.findParentByType(XmlAttribute::class.java)

      val usages = myFixture.findUsages(attr!!)

      assertThat(usages)
          .isNotNull

    }
  }

  //todo add test for nested call e.g. bean.model.field
}
