package com.aemtools.index

import com.aemtools.common.constant.const.JCR_ROOT
import com.aemtools.index.model.ClientlibraryModel
import com.aemtools.test.base.BaseLightTest
import org.assertj.core.api.Assertions.assertThat

/**
 * Test for [ClientlibraryIndex] & [ClientLibraryIndexFacade].
 *
 * @author Dmytro Troynikov
 */
class ClientlibraryIndexTest : BaseLightTest() {

  fun testMain() = fileCase {
    addXml("/$JCR_ROOT/apps/myapp/.content.xml", """
      <jcr:root jcr:primaryType="cq:ClientLibraryFolder"
        categories="[lib1, lib2]"
        />
    """)

    verify {
      val libs = ClientLibraryIndexFacade.getAllClientLibraryModels(project)
      val lib = libs.first()
      assertThat(libs)
          .hasSize(1)
      assertThat(lib)
          .isEqualTo(ClientlibraryModel(
              emptyList(),
              listOf("lib1", "lib2"),
              emptyList(), emptyList(),
              "/src/jcr_root/apps/myapp/.content.xml"
          ))
    }

  }

}
