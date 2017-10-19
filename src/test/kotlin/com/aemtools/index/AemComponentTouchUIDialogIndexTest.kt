package com.aemtools.index

import com.aemtools.blocks.base.BaseLightTest
import com.aemtools.constant.const.JCR_ROOT
import com.aemtools.index.model.dialog.AemComponentTouchUIDialogDefinition
import com.aemtools.index.model.dialog.parameter.TouchUIDialogParameterDeclaration
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex

/**
 * @author Dmytro Troynikov
 */
class AemComponentTouchUIDialogIndexTest : BaseLightTest() {

  fun testMain() = fileCase {
    addXml("/$JCR_ROOT/apps/myapp/components/comp/_cq_dialog/.content.xml", """
            <jcr:root>
                <items>
                    <item1 sling:resourceType="my/sling/resource/type1" name="./name1"/>
                    <item1 sling:resourceType="my/sling/resource/type2" name="./name2"/>
                </items>
            </jcr:root>
        """)
    verify {
      val fbi = FileBasedIndex.getInstance()

      val value = fbi.getValues(AemComponentTouchUIDialogIndex.AEM_COMPONENT_TOUCH_UI_DIALOG_INDEX,
          "/apps/myapp/components/comp",
          GlobalSearchScope.projectScope(project))
          .firstOrNull()
          ?: throw AssertionError("Unable to find indexed value")

      assertEquals(
          AemComponentTouchUIDialogDefinition(
              "/src/jcr_root/apps/myapp/components/comp/_cq_dialog/.content.xml",
              "/apps/myapp/components/comp",
              listOf(
                  TouchUIDialogParameterDeclaration(
                      "my/sling/resource/type1",
                      "./name1"
                  ),
                  TouchUIDialogParameterDeclaration(
                      "my/sling/resource/type2",
                      "./name2"
                  )
              )
          ),
          value
      )
    }
  }

}
