package com.aemtools.completion.htl.common

import com.aemtools.blocks.BaseVariantsCheckContributorTest.Companion.CONTEXT_PARAMETERS
import com.aemtools.blocks.BaseVariantsCheckContributorTest.Companion.CONTEXT_VALUES
import com.aemtools.blocks.BaseVariantsCheckContributorTest.Companion.DEFAULT_CONTEXT_OBJECTS
import com.aemtools.blocks.completion.CompletionBaseLightTest

/**
 * @author Dmytro_Troynikov
 */
class OptionsCompletionTest : CompletionBaseLightTest(true) {

  fun testOptionsContextValues() = completionTest {
    addHtml("test.html", """
            $DOLLAR{var @ context='$CARET'}
        """)
    shouldContain(CONTEXT_VALUES)
  }

  fun testOptionsContextValuesInAttribute() = completionTest {
    addHtml("test.html", """
            <div class="$DOLLAR{property @ context='$CARET'}"></div>
        """)
    shouldContain(CONTEXT_VALUES)
  }

  fun testOptionsContextValuesAbsentWithoutString() = completionTest {
    addHtml("test.html", """
            $DOLLAR{variable @ context=$CARET}
        """)
    shouldNotContain(CONTEXT_VALUES)
  }

  fun testOptionsDefaultContextParameters() = completionTest {
    addHtml("test.html", """
            $DOLLAR{@ $CARET}
        """)
    shouldContain(CONTEXT_PARAMETERS)
  }

  fun testOptionsCompletionWithCorruptedEl() = completionTest {
    addHtml("test.html", """
            <div>
                $DOLLAR{corrupted '' @}
                $DOLLAR{@ $CARET}
            </div>
        """)
    shouldContain(CONTEXT_PARAMETERS)
  }

  fun testOptionsDefaultContextParametersFiltering() = completionTest {
    addHtml("test.html", """
            <div>
                $DOLLAR{@ i18n, $CARET, join=','}
            </div>
        """)
    shouldContain(CONTEXT_PARAMETERS - listOf("i18n", "join"))
  }

  fun testOptionsContextObjectsShouldBeProposedAsCompletionVariants() = completionTest {
    addHtml("test.html", """
            $DOLLAR{@ option=$CARET}
        """)
    shouldContain(DEFAULT_CONTEXT_OBJECTS)
  }

  fun testOptionsWithinDataSlyResourceShouldHaveResourceTypeAsFirstOption() = completionTest {
    addHtml("test.html", """
            <div data-sly-resource='$DOLLAR{@ $CARET}'></div>
        """)
    shouldContain(listOf("resourceType") + CONTEXT_PARAMETERS)
  }

  fun testOptionsResourceTypeVariants() = completionTest {
    addHtml("/jcr_root/apps/components/component/test.html", """
            <div data-sly-resource='$DOLLAR{@ resourceType="$CARET"}'></div>
        """)
    addXml("/jcr_root/apps/components/component1/.content.xml", text = """
            <jcr:root jcr:primaryType="cq:Component" jcr:title='my title' componentGroup='group'/>
        """)
    addXml("/jcr_root/apps/components/component2/.content.xml", text = """
            <jcr:root jcr:primaryType="cq:Component" jcr:title='my title' componentGroup='group'/>
        """)

    shouldContain(
        "/apps/components/component1",
        "/apps/components/component2"
    )
  }

  fun testOptionsResourceTypeVariantsFilterOutCurrentComponent() = completionTest {
    addHtml("/jcr_root/apps/myapp/components/comp1/comp1.html", """
            <div data-sly-resource='$DOLLAR{@ resourceType="$CARET"}'></div>
        """)
    addXml("/jcr_root/apps/myapp/components/comp1/.content.xml", """
            <jcr:root jcr:primaryType="cq:Component" jcr:title="Component1" componentGroup="group"/>
        """)
    addXml("/jcr_root/apps/myapp/components/comp2/.content.xml", """
            <jcr:root jcr:primaryType="cq:Component" jcr:title="Component2" componentGroup="group"/>
        """)

    shouldContain(
        "/apps/myapp/components/comp2"
    )
    shouldNotContain(
        "/apps/myapp/components/comp1"
    )
  }

}
