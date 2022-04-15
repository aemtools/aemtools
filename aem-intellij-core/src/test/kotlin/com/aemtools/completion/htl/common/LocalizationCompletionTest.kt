package com.aemtools.completion.htl.common

import com.aemtools.test.completion.CompletionBaseLightTest

/**
 * @author Dmytro Primshyts
 */
class LocalizationCompletionTest : CompletionBaseLightTest(false) {

  fun testLocalizationCompletion() = completionTest {
    addXml("en.xml", """
            <jcr:root
              jcr:language="en"
              jcr:mixinTypes="[mix:language]">
                <message1
                    jcr:mixinTypes="[sling:Message]"
                    jcr:primaryType="nt:folder"
                    sling:message="Message1"/>
                <message
                    jcr:mixinTypes="[sling:Message]"
                    jcr:primaryType="nt:folder"
                    sling:key='message2'
                    sling:message="Message1"/>

            </jcr:root>
        """)

    addHtml("test.html", """
            $DOLLAR{'$CARET' @ i18n}
        """)
    shouldContain(listOf(
        "message1",
        "message2"),
        strict = false
    )
  }

  fun testLocalizationCompletionShouldNotContainDuplicates() = completionTest {
    addXml("en.xml", """
            <jcr:root
              jcr:language="en"
              jcr:mixinTypes="[mix:language]">
                <message1
                    jcr:mixinTypes="[sling:Message]"
                    jcr:primaryType="nt:folder"
                    sling:message="Message1"/>
                <message
                    jcr:mixinTypes="[sling:Message]"
                    jcr:primaryType="nt:folder"
                    sling:key='message2'
                    sling:message="Message1"/>
            </jcr:root>
        """)
    addXml("fr.xml", """
            <jcr:root
              jcr:language="en"
              jcr:mixinTypes="[mix:language]">
                <message1
                    jcr:mixinTypes="[sling:Message]"
                    jcr:primaryType="nt:folder"
                    sling:message="Message1"/>
                <message
                    jcr:mixinTypes="[sling:Message]"
                    jcr:primaryType="nt:folder"
                    sling:key='message2'
                    sling:message="Message1"/>
            </jcr:root>
        """)

    addHtml("test.html", """
            $DOLLAR{'$CARET' @ i18n}
        """)
    shouldContain(listOf(
        "message1",
        "message2"
    ),
        strict = false
    )
  }

}
