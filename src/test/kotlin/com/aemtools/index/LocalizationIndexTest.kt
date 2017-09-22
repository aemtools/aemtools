package com.aemtools.index

import com.aemtools.blocks.base.BaseLightTest
import com.aemtools.index.model.LocalizationModel
import junit.framework.TestCase

/**
 * @author Dmytro Troynikov
 */
class LocalizationIndexTest : BaseLightTest() {

  fun testLocalizationIndex() = fileCase {
    addXml(".content.xml", """
           <jcr:root
              jcr:mixinTypes="mix:language"
              jcr:language="en">
                <message jcr:mixinTypes="[sling:Message]"
                    jcr:primaryType="nt:folder"
                    sling:key="message"
                    sling:message="First message"/>

                <message-2 jcr:mixinTypes="[sling:Message]"
                    jcr:primaryType="nt:folder"
                    sling:message="Second message"/>
           </jcr:root>
        """)

    verify {
      val models = HtlIndexFacade.getAllLocalizationModels(project)

      TestCase.assertEquals(listOf(
          LocalizationModel(
              "/src/.content.xml",
              "en",
              "message",
              "First message"
          ),
          LocalizationModel(
              "/src/.content.xml",
              "en",
              "message-2",
              "Second message"
          )
      ),
          models)
    }
  }

}
