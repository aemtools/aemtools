package com.aemtools.completion.small.component

import com.aemtools.test.completion.CompletionBaseLightTest

/**
 * Test for [JcrCqComponentCompletionContributor].
 *
 * @author Kostiantyn Diachenko
 */
class JcrCqComponentCompletionContributorTest : CompletionBaseLightTest(false) {
  fun `test only hidden group`() = completionTest {
    addXml(".content.xml", """
       <jcr:root jcr:primaryType="cq:Component" componentGroup="$CARET" />
    """)
    shouldContain(
        ".hidden"
    )
  }

  fun `test hidden and project components groups`() = completionTest {
    addXml("/jcr_root/apps/myapp/components/main/.content.xml", """
       <jcr:root jcr:primaryType="cq:Component" componentGroup="$CARET" />
    """)

    addXml("/jcr_root/apps/myapp/components/comp1/.content.xml", """
            <jcr:root jcr:primaryType="cq:Component" jcr:title="Component1" componentGroup="Component Group 1"/>
        """)
    addXml("/jcr_root/apps/myapp/components/comp2/.content.xml", """
            <jcr:root jcr:primaryType="cq:Component" jcr:title="Component2" componentGroup="Component Group 2"/>
        """)

    shouldContain(
        ".hidden",
        "Component Group 1",
        "Component Group 2"
    )
  }

  fun `test hidden and project components groups sorted by popularity`() = completionTest {
    addXml("/jcr_root/apps/myapp/components/main/.content.xml", """
       <jcr:root jcr:primaryType="cq:Component" componentGroup="$CARET" />
    """)

    addXml("/jcr_root/apps/myapp/components/comp1/.content.xml", """
            <jcr:root jcr:primaryType="cq:Component" jcr:title="Component1" componentGroup="Component Group 1"/>
        """)
    addXml("/jcr_root/apps/myapp/components/comp2/.content.xml", """
            <jcr:root jcr:primaryType="cq:Component" jcr:title="Component2" componentGroup="Component Group 2"/>
        """)
    addXml("/jcr_root/apps/myapp/components/comp3/.content.xml", """
            <jcr:root jcr:primaryType="cq:Component" jcr:title="Component3" componentGroup="Component Group 2"/>
        """)

    shouldContain(
        ".hidden",
        "Component Group 2",
        "Component Group 1"
    )
  }

  fun `test resource types`() = completionTest {
    addXml("/jcr_root/apps/myapp/components/main/.content.xml", """
       <jcr:root jcr:primaryType="cq:Component" 
            jcr:title="comp" 
            componentGroup="group" 
            sling:resourceSuperType="$CARET"/>
    """)

    addXml("/jcr_root/apps/myapp/components/comp1/.content.xml", """
            <jcr:root jcr:primaryType="cq:Component" jcr:title="Component1" componentGroup="Component Group 1"/>
        """)
    addXml("/jcr_root/apps/myapp/components/comp2/.content.xml", """
            <jcr:root jcr:primaryType="cq:Component" jcr:title="Component2" componentGroup="Component Group 2"/>
        """)
    addXml("/jcr_root/apps/myapp/components/comp3/.content.xml", """
            <jcr:root jcr:primaryType="cq:Component" jcr:title="Component3" componentGroup="Component Group 2"/>
        """)

    shouldContain(
        "/apps/myapp/components/main",
        "/apps/myapp/components/comp1",
        "/apps/myapp/components/comp2",
        "/apps/myapp/components/comp3"
    )
  }

  fun `test cq_isContainer`() = completionTest {
    addXml(".content.xml", """
       <jcr:root jcr:primaryType="cq:Component" cq:isContainer="$CARET" />
    """)
    shouldContain(
        "true",
        "false"
    )
  }

  fun `test cq_noDecoration`() = completionTest {
    addXml(".content.xml", """
       <jcr:root jcr:primaryType="cq:Component" jcr:title="test1" cq:noDecoration="$CARET" />
    """)
    shouldContain(
        "true",
        "false"
    )
  }
}
