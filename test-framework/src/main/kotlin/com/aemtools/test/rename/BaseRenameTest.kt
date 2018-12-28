package com.aemtools.test.rename

import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.rename.model.fixture.IRenameFixture
import com.aemtools.test.rename.model.fixture.RenameFixture

/**
 * @author Dmytro Primshyts
 */
abstract class BaseRenameTest(withUberJar: Boolean = false) : BaseLightTest(withUberJar) {

  fun renameCase(case: IRenameFixture.() -> Unit) {
    val renameFixture = RenameFixture(myFixture)

    case.invoke(renameFixture)

    renameFixture.init()

    renameFixture.test()
  }

}
