package com.aemtools.documentation.component

import com.aemtools.test.documentation.BaseDocumentationTest

/**
 * Test for [CqComponentDocumentationProvider].
 *
 * @author Kostiantyn Diachenko
 */
class CqComponentDocumentationProviderTest : BaseDocumentationTest(CqComponentDocumentationProvider()) {
  fun `test componentGroup doc`() = docCase {
    addXml(".content.xml", """
       <jcr:root jcr:primaryType="cq:Component" ${CARET}componentGroup="" />
    """)

    documentation("""
        <h2>componentGroup</h2>
        <b>Type: </b>String<br/>
        <b>Description: </b>Group under which the component can be selected in the Components browser (touch-enabled UI) or Sidekick (classic UI).<br/>
        A value of .hidden is used for components that are not available for selection from the UI such as the actual paragraph systems.
    """)
  }

  fun `test jcr_title doc`() = docCase {
    addXml(".content.xml", """
       <jcr:root jcr:primaryType="cq:Component" ${CARET}jcr:title="" />
    """)

    documentation("""
        <h2>jcr:title</h2>
        <b>Type: </b>String<br/>
        <b>Description: </b>Title of the component.
    """)
  }

  fun `test jcr_description doc`() = docCase {
    addXml(".content.xml", """
       <jcr:root jcr:primaryType="cq:Component" ${CARET}jcr:description="" />
    """)

    documentation("""
        <h2>jcr:description</h2>
        <b>Type: </b>String<br/>
        <b>Description: </b>Description of the component.
    """)
  }

  fun `test sling_resourceSuperType doc`() = docCase {
    addXml(".content.xml", """
       <jcr:root jcr:primaryType="cq:Component" ${CARET}sling:resourceSuperType="" />
    """)

    documentation("""
        <h2>sling:resourceSuperType</h2>
        <b>Type: </b>String<br/>
        <b>Description: </b>When set, the component inherits from this component.
    """)
  }

  fun `test cq_noDecoration doc`() = docCase {
    addXml(".content.xml", """
       <jcr:root jcr:primaryType="cq:Component" ${CARET}cq:noDecoration="" />
    """)

    documentation("""
        <h2>cq:noDecoration</h2>
        <b>Type: </b>Boolean<br/>
        <b>Description: </b>If true, the component is not rendered with automatically generated div and css classes.
    """)
  }

  fun `test cq_isContainer doc`() = docCase {
    addXml(".content.xml", """
       <jcr:root jcr:primaryType="cq:Component" ${CARET}cq:isContainer="" />
    """)

    documentation("""
        <h2>cq:isContainer</h2>
        <b>Type: </b>Boolean<br/>
        <b>Description: </b>Indicates whether the component is a container component and therefore can contain other components such as a paragraph system.
    """)
  }

  fun `test dialogPath doc`() = docCase {
    addXml(".content.xml", """
       <jcr:root jcr:primaryType="cq:Component" ${CARET}dialogPath="" />
    """)

    documentation("""
        <h2>dialogPath</h2>
        <b>Type: </b>String<br/>
        <b>Description: </b>Path to a dialog to cover the case when the component does not have a dialog node.
    """)
  }

  fun `test cq_templatePath doc`() = docCase {
    addXml(".content.xml", """
       <jcr:root jcr:primaryType="cq:Component" ${CARET}cq:templatePath="" />
    """)

    documentation("""
        <h2>cq:templatePath</h2>
        <b>Type: </b>String<br/>
        <b>Description: </b>Path to a node to use as a content template when the component is added from the Components browser or Sidekick. This must be an absolute path, not relative to the component node.<br/>
        Unless you want to reuse content already available elsewhere, this is not required and cq:template is sufficient.
    """)
  }
}
