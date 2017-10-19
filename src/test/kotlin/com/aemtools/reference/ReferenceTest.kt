package com.aemtools.reference

import com.aemtools.blocks.reference.BaseReferenceTest
import com.aemtools.constant.const.JCR_ROOT
import com.aemtools.lang.htl.psi.HtlPsiFile
import com.aemtools.lang.htl.psi.HtlVariableName
import com.aemtools.reference.htl.HtlDeclarationIdentifier
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMethod

/**
 * @author Dmytro_Troynikov
 */
class ReferenceTest : BaseReferenceTest() {

  fun testReferenceToField() = testReference {
    addHtml("test.html", """
            <div data-sly-use.bean="com.test.TestClass">
                $DOLLAR{bean.${CARET}field}
            </div>
        """)
    addClass("TestClass", """
            package com.test;

            public class TestClass {
                public String field;
            }
        """)

    shouldResolveTo(PsiField::class.java)
    shouldContainText("public String field;")
  }

  fun testReferenceToPrimitiveField() = testReference {
    addHtml("test.html", """
            <div data-sly-use.bean="com.test.TestClass">
                $DOLLAR{bean.${CARET}primitive}
            </div>
        """)
    addClass("TestClass", """
            package com.test;

            public class TestClass {
                public boolean primitive;
            }
        """)

    shouldResolveTo(PsiField::class.java)
    shouldContainText("public boolean primitive;")
  }

  fun testReferenceToGetter() = testReference {
    addHtml("test.html", """
            <div data-sly-use.bean="com.test.TestClass">
                $DOLLAR{bean.${CARET}value}
            </div>
        """)
    addClass("TestClass", """
            package com.test;

            public class TestClass {
                public String getValue() { return ""; }
            }
        """)

    shouldResolveTo(PsiMethod::class.java)
    shouldContainText("public String getValue() { return \"\"; }")
  }

  fun testReferenceToGetterByNonNormalizedName() = testReference {
    addHtml("test.html", """
            <div data-sly-use.bean="com.test.TestClass">
                $DOLLAR{bean.${CARET}getValue}
            </div>
        """)
    addClass("TestClass", """
            package com.test;

            public class TestClass {
                public String getValue() { return ""; }
            }
        """)

    shouldResolveTo(PsiMethod::class.java)
    shouldContainText("public String getValue() { return \"\"; }")
  }

  fun testReferenceToDeclarationAttribute() = testReference {
    addHtml("test.html", """
            <div data-sly-use.bean="com.test.TestClass">
                $DOLLAR{${CARET}bean}
            </div>
        """)
    shouldResolveTo(HtlDeclarationIdentifier::class.java)
    shouldContainText("bean")
  }

  fun testSlyUseClassReferencesToPsiClass() = testReference {
    addHtml("test.html", """
            <div data-sly-use.bean="$DOLLAR{'com.test.${CARET}TestClass'}"></div>
        """)
    addClass("TestClass", "package com.test; public class TestClass {}")

    shouldResolveTo(PsiClass::class.java)
    shouldContainText("public class TestClass {}")
  }

  fun testReferenceToDataSlyTest() = testReference {
    addHtml("test.html", """
            <div data-sly-use.bean="com.test.TestClass">
                <sly data-sly-test.show="$DOLLAR{bean.show}">
                    $DOLLAR{${CARET}show}
                </sly>
            </div>
        """)

    shouldResolveTo(HtlDeclarationIdentifier::class.java)
    shouldContainText("show")
  }

  fun testReferenceItemToDataSlyList() = testReference {
    addHtml("test.html", """
            <div data-sly-list="iterable">
                $DOLLAR{${CARET}item}
            </div>
        """)
    shouldResolveTo(HtlDeclarationIdentifier::class.java)
    shouldContainText("item")
  }

  fun testReferenceItemListToDataSlyList() = testReference {
    addHtml("test.html", """
            <div data-sly-list="iterable">
                $DOLLAR{${CARET}itemList}
            </div>
        """)
    shouldResolveTo(HtlDeclarationIdentifier::class.java)
    shouldContainText("itemList")
  }

  fun testReferenceItemToDataSlyRepeat() = testReference {
    addHtml("test.html", """
            <div data-sly-repeat="iterable">
                $DOLLAR{${CARET}item}
            </div>
        """)
    shouldResolveTo(HtlDeclarationIdentifier::class.java)
    shouldContainText("item")
  }

  fun testReferenceItemListToDataSlyRepeat() = testReference {
    addHtml("test.html", """
            <div data-sly-repeat="iterable">
                $DOLLAR{${CARET}itemList}
            </div>
        """)
    shouldResolveTo(HtlDeclarationIdentifier::class.java)
    shouldContainText("itemList")
  }

  fun testReferenceTemplateParameter() = testReference {
    addHtml("test.html", """
            <div data-sly-template.template='$DOLLAR{@ param}'>
                $DOLLAR{${CARET}param}
            </div>
        """)
    shouldResolveTo(HtlVariableName::class.java)
    shouldContainText("param")
  }

  fun testReferenceToIncludeFile() = testReference {
    addHtml("$JCR_ROOT/apps/components/comp/comp.html", """
            <div data-sly-include='${CARET}file.html'></div>
        """)
    addHtml("$JCR_ROOT/apps/components/comp/file.html", "included")

    shouldResolveTo(HtlPsiFile::class.java)
    shouldContainText("included")
  }

  fun testReferenceToIncludedFileFromEl() = testReference {
    addHtml("$JCR_ROOT/apps/components/comp/comp.html", """
            <div data-sly-include="$DOLLAR{'${CARET}file.html'}"></div>
        """)
    addHtml("$JCR_ROOT/apps/components/comp/file.html", "included")

    shouldResolveTo(HtlPsiFile::class.java)
    shouldContainText("included")
  }

  fun testReferenceToUseFileFromEl() = testReference {
    addHtml("$JCR_ROOT/apps/components/comp/comp.html", """
            <div data-sly-use.template="$DOLLAR{'${CARET}file.html'}"></div>
        """)
    addHtml("$JCR_ROOT/apps/components/comp/file.html", "included")

    shouldResolveTo(HtlPsiFile::class.java)
    shouldContainText("included")
  }

}
