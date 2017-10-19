package com.aemtools.documentation.widget

import com.aemtools.blocks.documentation.BaseDocumentationTest

/**
 * @author Dmytro Troynikov
 */
class WidgetDocumentationProviderTest
  : BaseDocumentationTest(WidgetDocumentationProvider()) {

  fun testFieldDocumentation() = docCase {
    addXml("/dialog.xml", """
            <jcr:root>
                <item jcr:primaryType="cq:Widget"
                      xtype="tabpanel"
                      ${CARET}activeItem=''>

                </item>
            </jcr:root>
        """)
    documentation("""
            <h2>CQ.Ext.TabPanel</h2>
            <p>
                Field name: <b>activeItem</b>
            </p>
            <p>
                Defined by: <b>Container</b>
            </p>
            <p>
                A string component id or the numeric index of the component that should be initially activated within the container's layout on render. For example, activeItem: 'item-1' or activeItem: 0 (index 0 = the first item in the container's collection). activeItem only applies to layout styles that can display items one at a time (like CQ.Ext.layout.AccordionLayout, CQ.Ext.layout.CardLayout and CQ.Ext.layout.FitLayout). Related to CQ.Ext.layout.ContainerLayout.activeItem.
            </p>
        """)
  }

  fun testXTypeDocumentation() = docCase {
    addXml("/dialog.xml", """
            <jcr:root>
                <item jcr:primaryType="cq:Widget" ${CARET}xtype="pathfield"></item>
            </jcr:root>
        """)

    documentation("""
            <h2>CQ.form.PathField</h2>
            <p>
                Field name: <b>xtype</b>
            </p>
            <p>
                Defined by: <b>Component</b>
            </p>
            <p>
                The registered xtype to create. This config option is not used when passing a config object into a constructor. This config option is used only when lazy instantiation is being used, and a child item of a Container is being specified not as a fully instantiated Component, but as a Component config object. The xtype will be looked up at render time up to determine what type of child Component to create. The predefined xtypes are listed here. If you subclass Components to create your own Components, you may register them using CQ.Ext.ComponentMgr.registerType in order to be able to take advantage of lazy instantiation and rendering.
            </p>
        """)
  }

  fun testXTypeDocumentation2() = docCase {
    addXml("/dialog.xml", """
            <jcr:root>
                <item xtype="${CARET}pathfield"></item>
            </jcr:root>
        """)

    documentation("""
            <h2>PathField</h2>
            <p>The PathField is an input field designed for paths with path completion and a button to open a CQ.BrowseDialog for browsing the server repository. It can also browse page paragraphs for advanced link generation. The default configuration is for browsing pages below /content. Here are some other typical configs: Full repository browsing, showing node names, not titles var pathfield = new CQ.form.PathField({
    rootPath: "/",
    predicate: "nosystem",
    showTitlesInTree: false
}); Browsing only a subtree var pathfield = new CQ.form.PathField({
    rootPath: "/content/dam",
    showTitlesInTree: false
}); Paragraph browsing (with custom link patterns) var pathfield = new CQ.form.PathField({
    parBrowse: true,
    linkPattern: "{0}.selector.html",
    parLinkPattern: "{0}.selector.html#{1}"
}); Only (fast) path completion, but no browse dialog var pathfield = new CQ.form.PathField({
    hideTrigger: true,
    searchDelay: 100
});</p>
        """)
  }

}
