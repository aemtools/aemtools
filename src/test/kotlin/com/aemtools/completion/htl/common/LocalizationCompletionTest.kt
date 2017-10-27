package com.aemtools.completion.htl.common

import com.aemtools.blocks.completion.CompletionBaseLightTest

/**
 * @author Dmytro Troynikov
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
    shouldContain(
        "message1",
        "message2"
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
    shouldContain(
        "message1",
        "message2"
    )
  }

}
