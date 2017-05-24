package com.aemtools.index

import com.aemtools.blocks.base.BaseLightTest
import com.aemtools.constant.const.JCR_ROOT
import com.aemtools.index.model.AemComponentClassicDialogDefinition
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex

/**
 * @author Dmytro Troynikov
 */
class AemComponentClassicDialogIndexTest : BaseLightTest() {

    fun testMain() = fileCase {
        addXml("/$JCR_ROOT/apps/components/comp/dialog.xml", """
            <jcr:root
                jcr:primaryType="cq:Dialog">
                <items>
                    <item1 xtype='pathfield'
                           name='./path1'/>
                    <item2 xtype='pathfield'
                            name='./path2'/>
                </items>
            </jcr:root>
        """)

        verify {
            val fbi = FileBasedIndex.getInstance()

            val value = fbi.getValues(
                    AemComponentClassicDialogIndex.AEM_COMPONENT_CLASSIC_DIALOG_INDEX_ID,
                    "/apps/components/comp",
                    GlobalSearchScope.projectScope(project))
                    .first()

            assertEquals(
                    AemComponentClassicDialogDefinition(
                            "/src/jcr_root/apps/components/comp/dialog.xml",
                            "/apps/components/comp",
                            listOf(
                                    AemComponentClassicDialogDefinition.ClassicDialogParameterDeclaration(
                                            "pathfield",
                                            "./path1"
                                    ),
                                    AemComponentClassicDialogDefinition.ClassicDialogParameterDeclaration(
                                            "pathfield",
                                            "./path2"
                                    )
                            )
                    ),
                    value
            )
        }
    }

}
