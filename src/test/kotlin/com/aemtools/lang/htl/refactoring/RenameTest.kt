package com.aemtools.lang.htl.refactoring

import com.intellij.testFramework.LightPlatformCodeInsightTestCase

/**
 * @author Dmytro_Troynikov
 */
class RenameTest : LightPlatformCodeInsightTestCase() {

    override fun isRunInWriteAction(): Boolean = false

    override fun getTestDataPath(): String =
            "src/test/resources/refactoring/"

    //TODO add tests
}