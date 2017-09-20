package com.aemtools.blocks.rename

import com.aemtools.blocks.base.BaseLightTest
import com.aemtools.blocks.rename.model.fixture.IRenameFixture
import com.aemtools.blocks.rename.model.fixture.RenameFixture

/**
 * @author Dmytro Troynikov
 */
abstract class BaseRenameTest(withUberJar: Boolean = false) : BaseLightTest(withUberJar) {

  fun renameCase(case: IRenameFixture.() -> Unit) {
    val renameFixture = RenameFixture(myFixture)

    case.invoke(renameFixture)

    renameFixture.init()

    renameFixture.test()
  }

}
