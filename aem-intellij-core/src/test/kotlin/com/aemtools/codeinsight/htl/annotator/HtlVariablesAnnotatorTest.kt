package com.aemtools.codeinsight.htl.annotator

import com.aemtools.test.base.BaseLightTest

/**
 * @author Dmytro Primshyts
 */
class HtlVariablesAnnotatorTest : BaseLightTest() {

  fun testGlobalProperties() = testGlobalVariable("properties")
  fun testGlobalPageProperties() = testGlobalVariable("pageProperties")
  fun testGlobalInheritedPageProperties() = testGlobalVariable("inheritedPageProperties")
  fun testGlobalComponent() = testGlobalVariable("component")
  fun testGlobalComponentContext() = testGlobalVariable("componentContext")
  fun testGlobalCurrentDesign() = testGlobalVariable("currentDesign")
  fun testGlobalCurrentSession() = testGlobalVariable("currentSession")
  fun testGlobalCurrentStyle() = testGlobalVariable("currentStyle")
  fun testGlobalDesigner() = testGlobalVariable("designer")
  fun testGlobalEditContext() = testGlobalVariable("editContext")
  fun testGlobalLog() = testGlobalVariable("log")
  fun testGlobalOut() = testGlobalVariable("out")
  fun testGlobalPageManager() = testGlobalVariable("pageManager")
  fun testGlobalReader() = testGlobalVariable("reader")
  fun testGlobalRequest() = testGlobalVariable("request")
  fun testGlobalResource() = testGlobalVariable("resource")
  fun testGlobalResourceDesign() = testGlobalVariable("resourceDesign")
  fun testGlobalResourcePage() = testGlobalVariable("resourcePage")
  fun testGlobalResponse() = testGlobalVariable("response")
  fun testGlobalSling() = testGlobalVariable("sling")
  fun testGlobalSlyWcmHelper() = testGlobalVariable("slyWcmHelper")
  fun testGlobalWcmmode() = testGlobalVariable("wcmmode")
  fun testGlobalXssApi() = testGlobalVariable("xssAPI")

  fun testLocalUseVariable() {
    myFixture.configureByText("test.html", """
            <div <info descr="null">data-sly-use</info>.<weak_warning descr="null">bean</weak_warning>=""></div>
            $DOLLAR{<weak_warning descr="null">bean</weak_warning>}
        """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun testUnresolvedVariable() {
    myFixture.configureByText("test.html", """
            $DOLLAR{<weak_warning descr="Cannot resolve symbol 'bean'">bean</weak_warning>}
        """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun testGlobalVariable(variableName: String) {
    myFixture.configureByText("test.html", """
            $DOLLAR{<weak_warning descr="Context Object">$variableName</weak_warning>}
        """)
    myFixture.checkHighlighting(true, true, true)
  }

}
