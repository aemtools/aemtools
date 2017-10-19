package com.aemtools.index

import com.aemtools.blocks.base.BaseLightTest
import com.aemtools.index.model.AemComponentDefinition
import com.aemtools.index.search.AemComponentSearch

/**
 * @author Dmytro Troynikov
 */
class AemComponentDeclarationIndexTest : BaseLightTest() {

  fun testMain() = fileCase {
    addXml("/apps/components/test/.content.xml", """
            <jcr:root
                jcr:primaryType="cq:Component"
                jcr:title="My Title"
                jcr:description="My Description"
                sling:resourceSuperType="MySupertype"
                componentGroup="My Group"
                cq:isContainer="true"
                cq:icon="my-icon"/>
        """)

    verify {
      val components = AemComponentSearch.allComponentDeclarations(project)

      assertEquals("One component in the project should be present", 1, components.size)

      val component = components.first()

      assertEquals(
          AemComponentDefinition(
              title = "My Title",
              description = "My Description",
              fullPath = "/src/apps/components/test/.content.xml",
              resourceSuperType = "MySupertype",
              componentGroup = "My Group",
              isContainer = true,
              cqIcon = "my-icon"
          ),
          component
      )
    }
  }

}
