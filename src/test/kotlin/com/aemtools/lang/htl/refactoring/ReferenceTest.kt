package com.aemtools.lang.htl.refactoring

import com.aemtools.blocks.reference.BaseReferenceTest
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMethod
import com.intellij.psi.xml.XmlAttributeValue

/**
 * @author Dmytro_Troynikov
 */
class ReferenceTest : BaseReferenceTest() {

    override fun getTestDataPath(): String =
            "src/test/resources/refactoring/"

    fun testReferenceToField() = testReference {
        addHtml("test.html", """
            <div data-sly-use.bean="com.test.TestClass">
                ${'$'}{bean.<caret>field}
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
                ${'$'}{bean.<caret>primitive}
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
                ${'$'}{bean.<caret>value}
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
                ${'$'}{bean.<caret>getValue}
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
                ${'$'}{<caret>bean}
            </div>
        """)
        shouldResolveTo(XmlAttributeValue::class.java)
        shouldContainText("\"com.test.TestClass\"")
    }

    fun testSlyUseClassReferencesToPsiClass() = testReference {
        addHtml("test.html", """
            <div data-sly-use.bean="${'$'}{'com.test.<caret>TestClass'}"></div>
        """)
        addClass("TestClass", "package com.test; public class TestClass {}")

        shouldResolveTo(PsiClass::class.java)
        shouldContainText("public class TestClass {}")
    }

    fun testReferenceToDataSlyTest() = testReference {
        addHtml("test.html", """
            <div data-sly-use.bean="com.test.TestClass">
                <sly data-sly-test.show="${'$'}{bean.show}">
                    ${'$'}{<caret>show}
                </sly>
            </div>
        """)

        shouldResolveTo(XmlAttributeValue::class.java)
        shouldContainText("\"${'$'}{bean.show}\"")
    }

    fun testReferenceItemToDataSlyList() = testReference {
        addHtml("test.html", """
            <div data-sly-list="iterable">
                ${'$'}{<caret>item}
            </div>
        """)
        shouldResolveTo(XmlAttributeValue::class.java)
        shouldContainText("\"iterable\"")
    }

    fun testReferenceItemListToDataSlyList() = testReference {
        addHtml("test.html", """
            <div data-sly-list="iterable">
                ${'$'}{<caret>itemList}
            </div>
        """)
        shouldResolveTo(XmlAttributeValue::class.java)
        shouldContainText("\"iterable\"")
    }

    fun testReferenceItemToDataSlyRepeat() = testReference {
        addHtml("test.html", """
            <div data-sly-repeat="iterable">
                ${'$'}{<caret>item}
            </div>
        """)
        shouldResolveTo(XmlAttributeValue::class.java)
        shouldContainText("\"iterable\"")
    }

    fun testReferenceItemListToDataSlyRepeat() = testReference {
        addHtml("test.html", """
            <div data-sly-repeat="iterable">
                ${'$'}{<caret>itemList}
            </div>
        """)
        shouldResolveTo(XmlAttributeValue::class.java)
        shouldContainText("\"iterable\"")
    }

}