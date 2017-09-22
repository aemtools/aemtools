package com.aemtools.completion.htl

import com.aemtools.blocks.BaseVariantsCheckContributorTest.Companion.DEFAULT_CONTEXT_OBJECTS
import com.aemtools.blocks.base.model.fixture.ITestFixture
import com.aemtools.blocks.completion.CompletionBaseLightTest
import com.aemtools.constant.const.JCR_ROOT
import com.aemtools.constant.const.java.SLING_MODEL

/**
 * @author Dmytro Troynikov
 */
class CompletionRelevanceTest : CompletionBaseLightTest(true) {

  fun testDataSlyUseCompletion() = completionTest {
    addHtml("/$JCR_ROOT/apps/myapp/component/component.html", """
            <div data-sly-use.bean="$CARET"></div>
        """)
    emptySlingModel("com.test.ComponentModel.java")
    emptySlingModel("com.test.SomeOtherModel.java")

    addHtml("/$JCR_ROOT/apps/myapp/component/template.html", """
            <div data-sly-template.template="$DOLLAR{@ param}"></div>
        """)
    addHtml("/$JCR_ROOT/apps/myapp/component2/template.html", """
            <div data-sly-template.template="$DOLLAR{@ param}"></div>
        """)

    shouldContain(listOf(
        "com.test.ComponentModel",
        "template.html",
        "com.test.SomeOtherModel",
        "/apps/myapp/component2/template.html"
    ),
        ordered = true
    )
  }

  fun testPropertiesCompletion() = completionTest {
    addHtml("/$JCR_ROOT/apps/myapp/component/component.html", """
            <div>
                $DOLLAR{properties.$CARET}
            </div>
        """)
    addXml("/$JCR_ROOT/apps/myapp/component/dialog.xml", """
            <jcr:root jcr:primaryType="cq:Dialog">
                <items>
                    <item1 xtype="pathfield" name="./param1"/>
                    <item1 xtype="pathfield" name="./param2"/>
                </items>
            </jcr:root>
        """)
    addXml("/$JCR_ROOT/apps/myapp/component/.content.xml", "")
    shouldContain(listOf(
        "param1",
        "param2",
        "cq:lastReplicated",
        "cq:lastReplicatedBy",
        "cq:lastReplicationAction",
        "jcr:createdBy",
        "jcr:description",
        "jcr:lastModified",
        "jcr:lastModifiedBy",
        "jcr:mixinTypes",
        "jcr:primaryType",
        "jcr:title",
        "sling:resourceType",
        "empty",
        "entrySet",
        "hashCode",
        "keySet",
        "size",
        "values",
        "class",
        "toString"
    ),
        ordered = true
    )
  }

  fun testVariableCompletion() = completionTest {
    addHtml("test.html", """
            <div data-sly-use.bean=""></div>
            <div data-sly-test.test=""></div>
            <div data-sly-list.list="">
                <div data-sly-repeat.repeat="" class="$DOLLAR{$CARET}"></div>
            </div>
        """)
    shouldContain(listOf(
        "repeat",
        "repeatList",
        "list",
        "listList",
        "test",
        "bean",
        *DEFAULT_CONTEXT_OBJECTS.toTypedArray()
    ),
        ordered = true
    )
  }

  fun testVariableCompletion2() = completionTest {
    addHtml("test.html", """
            <div data-sly-use.bean=""></div>
            $DOLLAR{$CARET}
            <div data-sly-test.test=""></div>
            <div data-sly-list.list=""></div>
            <div data-sly-repeat.repeat=""></div>
        """)
    shouldContain(listOf(
        "bean",
        *DEFAULT_CONTEXT_OBJECTS.toTypedArray(),
        "test"
    ))
  }

  private fun ITestFixture.emptySlingModel(name: String) =
      addClass(name, """
                package ${name.substringBeforeLast(".").substringBeforeLast(".")};

                import $SLING_MODEL;

                @Model
                public class ${name.substringBeforeLast(".").substringAfterLast(".")} {}
            """)

}
