package com.aemtools.index

import com.aemtools.common.constant.const.JCR_ROOT
import com.aemtools.test.base.BaseLightTest

/**
 * @author Dmytro Troynikov
 */
class ClientlibraryIndexTest : BaseLightTest() {

  fun testMain() = fileCase {
    addXml("/$JCR_ROOT/apps/myapp/.content.xml", """
      <jcr:root jcr:primaryType="cq:ClientLibraryFolder"


        />
    """)


  }



}
